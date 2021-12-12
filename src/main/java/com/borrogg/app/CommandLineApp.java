package com.borrogg.app;

import com.borrogg.entities.Client;
import com.borrogg.entities.Department;
import com.borrogg.enums.Position;
import com.borrogg.service.DepartmentService;
import com.borrogg.service.ClientService;
import com.borrogg.service.FileService;
import com.borrogg.util.SearchEntity;
import com.borrogg.util.ZipDirectory;
import dnl.utils.text.table.TextTable;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static com.borrogg.util.ProgramUtil.*;

@Component
public class CommandLineApp implements CommandLineRunner {

    private Client currentUser;
    private long sessionStart;

    @PersistenceContext
    private EntityManager entityManager;

    private final ClientService clientService;
    private final DepartmentService departmentService;
    private final FileService fileService;

    @Autowired
    public CommandLineApp(ClientService clientService, DepartmentService departmentService, FileService fileService) {
        this.clientService = clientService;
        this.departmentService = departmentService;
        this.fileService = fileService;
    }

    @Override
    public void run(String... args) {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println(INPUT_LOGIN);
            String login = in.nextLine();
            System.out.println(INPUT_PASS);
            String pass = in.nextLine();
            if (enterAuth(login, pass)) {
                break;
            }
        }

        while (true) {
            System.out.println(MENU);
            int inputDecimal = 0;
            try {
                inputDecimal = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(INCORRECT_INPUT);
            }

            // Active Work...
            if (inputDecimal == 1) {
                getClientInfo(in);
            } else if (inputDecimal == 2) {
                getInfoOfAllClients();
            } else if (inputDecimal == 3) {
                getAllPositions();
            } else if (inputDecimal == 4) {
                addNewClient(in);
            } else if (inputDecimal == 5) {
                removeClient(in);
            } else if (inputDecimal == 6) {
                changeClientData(in);
            } else if (inputDecimal == 7) {
                getClientsInfoByFIO(in);
            } else if (inputDecimal == 8) {
                getClientsInfoByPhone(in);
            } else if (inputDecimal == 9) {
                deleteFile(in);
            } else if (inputDecimal == 10) {
                addNewFile(in);
            } else if (inputDecimal == 11) {
                downloadFile(in);
            } else if (inputDecimal == 12) {
                downloadAllFiles(in);
            } else if (inputDecimal == 13) {
                findFilesByFormatAndSize(in);
            } else if (inputDecimal == 14) {
                createReport();
            } else if (inputDecimal == 15) {
                zipAllFiles(in);
            } else if (inputDecimal == 16) {
                in.close();
                break;
            } else {
                System.out.println(INCORRECT_INPUT);
            }
        }
    }

    private boolean enterAuth(String login, String pass) {
        currentUser = clientService.getClientByLogin(login);
        if (!(Objects.nonNull(currentUser) && BCrypt.checkpw(pass, currentUser.getPass()))) {
            currentUser = null;
            System.out.println(INCORRECT_INPUT_LOGIN_OR_PASS);
            return false;
        }
        sessionStart = System.currentTimeMillis();
        return true;
    }

    private boolean isSessionEnd() {
        return System.currentTimeMillis() > sessionStart + 60_000;
    }

    private void zipAllFiles(Scanner in) {
        System.out.println("Введите путь для выгрузки(например D:\\sedForSave): ");
        String path = in.nextLine();

        ZipDirectory zipDir = new ZipDirectory();
        File inputDir = new File(PATH_TO_FILES);
        File outputZipFile = new File(path + "\\files.zip");
        zipDir.zipDirectory(inputDir, outputZipFile);
    }

    private void createReport() {
        List<SearchEntity> searchEntities = getSearchEntities();
        Object[][] data = new Object[searchEntities.size()][4];
        for (int i = 0; i < searchEntities.size(); i++) {
            data[i][0] = i;
            data[i][1] = searchEntities.get(i).getDepartment();
            data[i][2] = searchEntities.get(i).getSizeSum();
            data[i][3] = searchEntities.get(i).getAvgCountFiles();
        }
        TextTable tt = new TextTable(new String[]{"Номер", "Отдел", "Размер всех файлов", "Среднее количество файлов"}, data);
        tt.printTable();
    }

    private List<SearchEntity> getSearchEntities() {
        try {
            Query query = entityManager.createNativeQuery(UNIQUE_QUERY);
            List<Object[]> list = (List<Object[]>) query.getResultList();
            List<SearchEntity> searchEntities = new ArrayList<>();
            for (Object[] objects : list) {
                searchEntities.add(new SearchEntity(objects));
            }
            return searchEntities;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void findFilesByFormatAndSize(Scanner in) {
        System.out.println(SELECT_FORMAT_FOR_SEARCH);
        int formatId = getNum(in) + 1;
        if (formatId > 0 && formatId < 5) {
            String format = getFormatById(formatId);
            int from;
            int to;
            while (true) {
                try {
                    System.out.println(INPUT_RANGE_SIZE_FILE);
                    String query2 = in.nextLine();
                    from = Integer.parseInt(query2.split("-")[0]);
                    to = Integer.parseInt(query2.split("-")[1]);
                    break;
                } catch (Exception e) {
                    System.out.println(INCORRECT_INPUT);
                }
            }
            List<com.borrogg.entities.File> files = fileService.findAllByFormatAndSizeKBBetween(format, from, to);

            lookAtResultFile(files);
        } else {
            System.out.println(INCORRECT_INPUT);
        }
    }

    private String getFormatById(int formatId) {
        if (formatId == 1) {
            return ".png";
        } else if (formatId == 2) {
            return ".jpeg";
        } else if (formatId == 3) {
            return ".bmp";
        } else if (formatId == 4) {
            return ".gif";
        }
        return null;
    }

    private void deleteFile(Scanner in) {
        System.out.println(SELECT_USER);

        List<Client> clients = getAndShowClients();

        int num = getNum(in);

        Client selectedClient = clients.get(num);
        List<com.borrogg.entities.File> files = fileService.getFilesByClientId(selectedClient);
        System.out.println(SELECT_FILE_TO_DELETE);
        for (int i = 0; i < files.size(); i++) {
            System.out.println("Номер: " + (i + 1) + " Название: " + files.get(i).getName());
        }
        int numFile = getNum(in);

        File file = new File(PATH_TO_FILES + "\\" +  fileService.getFileById(files.get(numFile).getFileId()).getName());
        if (fileService.isLastFileInStorage(file.getName())) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                System.out.println(ERROR_ON_DELETE_FILE);
            }
        }

        fileService.deleteFile(files.get(numFile));
        System.out.println(DELETE_FILE_COMPLETE);
    }

    private void downloadAllFiles(Scanner in) {
        List<com.borrogg.entities.File> files = fileService.getAllFiles();
        System.out.println(INPUT_PATH_TO_DOWNLOAD);
        String pathToSave = in.nextLine();
        for (com.borrogg.entities.File value : files) {
            File file = new File(PATH_TO_FILES + "\\" + fileService.getFileById(value.getFileId()).getName());
            if (saveFileInSystem(file, pathToSave + "\\" + value.getName()))
                return;
        }
        System.out.println(DOWNLOAD_ALL_FILE_COMPLETE);
    }

    private void downloadFile(Scanner in) {
        System.out.println(SELECT_USER);

        List<Client> clients = getAndShowClients();

        int num = getNum(in);

        Client selectedClient = clients.get(num);
        List<com.borrogg.entities.File> files = fileService.getFilesByClientId(selectedClient);
        System.out.println(SELECT_FILE_TO_DOWNLOAD);
        for (int i = 0; i < files.size(); i++) {
            System.out.println("Номер: " + (i + 1) + " Название: " + files.get(i).getName());
        }
        int numFile = getNum(in);
        System.out.println(INPUT_PATH_TO_DOWNLOAD);
        String pathToSave = in.nextLine();

        File file = new File(PATH_TO_FILES + "\\" + fileService.getFileById(files.get(numFile).getFileId()).getName());

        if (saveFileInSystem(file, pathToSave + "\\" + files.get(numFile).getName()))
            return;
        System.out.println(DOWNLOAD_FILE_COMPLETE);

    }

    private void addNewFile(Scanner in) {
        // C:\Users\Евгений\Documents\The Witcher 3\gamesaves\AutoSave_53db9_7e404400_5d46d66.png
        System.out.println(SELECT_USER);

        List<Client> clients = getAndShowClients();

        int num = getNum(in);

        Client selectedClient = clients.get(num);
        System.out.println(INPUT_FILENAME);
        String input = in.nextLine();
        if (input.endsWith(".jpeg") || input.endsWith(".png") || input.endsWith(".bmp") || input.endsWith(".gif")) {
            String path = input.substring(0, input.lastIndexOf("\\"));
            String fileName = input.substring(input.lastIndexOf("\\") + 1);
            File file = new File(path, fileName);
            if (file.exists() && file.isFile() && file.length() < 10_000_000) {
                if (fileService.getFilesByClientId(selectedClient).size() < 20) {

                    if (saveFileInSystem(file, PATH_TO_FILES + "\\" + fileName)) return;

                    com.borrogg.entities.File fileToSave = new com.borrogg.entities.File();
                    fileToSave.setName(fileName);
                    fileToSave.setFormat(fileName.substring(fileName.lastIndexOf(".")));
                    fileToSave.setDateDownload(LocalDate.now());
                    fileToSave.setSizeKB((int) (file.length() / 1000));
                    fileToSave.setClient(selectedClient);
                    fileService.addFile(fileToSave);
                    System.out.println(CREATE_FILE_COMPLETE);
                } else {
                    System.out.println(INCORRECT_FILES_COUNT);
                }
            } else {
                System.out.println(INCORRECT_FILE_SIZE);
            }
        } else {
            System.out.println(INCORRECT_FILE_FORMAT);
        }

    }

    private boolean saveFileInSystem(File file, String fileCode) {
        byte[] fileByte;
        try (FileInputStream stream = new FileInputStream(file)) {
            fileByte = stream.readAllBytes();
        } catch (IOException e) {
            System.out.println(ERROR_ON_READ_FILE);
            return true;
        }

        Path path1 = Paths.get(fileCode);
        Path file2;
        int i = 1;
//        while(true) {
            try {
                file2 = Files.createFile(path1);
//                break;
            } catch (FileAlreadyExistsException e) {
                return false;
            }
            catch (IOException e) {
                System.out.println(ERROR_ON_CREATE_FILE);
                return true;
            }
//        }
        try (FileOutputStream stream = new FileOutputStream(file2.toString())) {
            stream.write(fileByte);
        } catch (IOException e) {
            System.out.println(ERROR_ON_WRITE_FILE);
            try {
                Files.delete(path1);
            } catch (IOException ex) {
                System.out.println(ERROR_ON_RETURN_CHANGES);
                return true;
            }
            return true;
        }
        return false;
    }

    private void changeClientData(Scanner in) {
        System.out.println(SELECT_USER);

        List<Client> clients = getAndShowClients();

        int num = getNum(in);

        Client selectedClient = clients.get(num);
        System.out.println(CURRENT_USER_NAME + selectedClient.getFIO());
        System.out.println(INPUT_OR_ENTER);
        String value = in.nextLine();
        if (!value.isEmpty()) {
            selectedClient.setFIO(value);
        }
        System.out.println(CURRENT_USER_DOB + selectedClient.getDateOfBirth());
        System.out.println(INPUT_OR_ENTER);
        LocalDate dob = getDateOfBirth(in);
        if (dob != null) {
            selectedClient.setDateOfBirth(dob);
        }
        System.out.println(CURRENT_USER_DEPARTMENT + selectedClient.getDepartment().getName());
        System.out.println(INPUT_OR_ENTER);
        Department department = getDepartment(in, selectedClient);
        if (department != null) {
            selectedClient.setDepartment(department);
        }
        System.out.println(CURRENT_USER_POSITION + selectedClient.getPosition());
        System.out.println(INPUT_OR_ENTER);
        Position position = getPosition(in, selectedClient.getDepartment());
        if (department != null) {
            selectedClient.setPosition(position);
        }
        System.out.println(CURRENT_USER_PHONE + selectedClient.getPhone());
        System.out.println(INPUT_OR_ENTER);
        value = in.nextLine();
        if (!value.isEmpty()) {
            selectedClient.setPhone(value);
        }

        clientService.updateClient(selectedClient);
        System.out.println(UPDATE_USER_SUCCESS);
    }

    private void getClientsInfoByPhone(Scanner in) {
        System.out.println(INPUT_QUERY_FOR_SEARCH);
        String query = in.nextLine();
        List<Client> clients = clientService.findByPhonePattern(query);

        lookAtResult(clients);
    }

    private void lookAtResult(List<Client> clients) {
        if (!clients.isEmpty()) {
            System.out.println(QUERY_RESULT);
            for (Client client : clients) {
                String filesNames = getFilesNames(client);
                System.out.printf((CLIENT_INFO) + "%n", client.getClientId(),
                        client.getFIO(), client.getDateOfBirth(),
                        client.getDepartment().getName(), client.getPosition(),
                        client.getPhone(), filesNames);
            }
        } else {
            System.out.println(NOTHING_FOUND);
        }
    }

    private void lookAtResultFile(List<com.borrogg.entities.File> files) {
        if (!files.isEmpty()) {
            System.out.println(QUERY_RESULT);
            for (com.borrogg.entities.File file : files) {
                System.out.printf((FILE_INFO) + "%n", file.getFileId(),
                        file.getName(), file.getFormat(),
                        file.getDateDownload().toString(), file.getSizeKB().toString(),
                        file.getClient().getFIO());
            }
        } else {
            System.out.println(NOTHING_FOUND);
        }
    }

    private void getClientsInfoByFIO(Scanner in) {
        System.out.println(INPUT_QUERY_FOR_SEARCH);
        String query = in.nextLine();
        List<Client> clients = clientService.findByFIOPattern(query);

        lookAtResult(clients);
    }

    private void removeClient(Scanner in) {
        System.out.println(SELECT_USER);
        List<Client> clients = getAndShowClients();
        int num = getNum(in);

        clientService.removeClient(clients.get(num));
        System.out.println(REMOVE_USER_SUCCESS);
    }

    private List<Client> getAndShowClients() {
        List<Client> clients = clientService.getAll();
        for (int i = 0; i < clients.size(); i++) {
            System.out.println("Номер: " + (i + 1) + " ФИО: " + clients.get(i).getFIO());
        }
        return clients;
    }

    private int getNum(Scanner in) {
        int num;
        while (true) {
            try {
                num = Integer.parseInt(in.nextLine()) - 1;
                break;
            } catch (NumberFormatException e) {
                System.out.println(INCORRECT_INPUT);
            }
        }
        return num;
    }

    private void addNewClient(Scanner in) {
        clientService.addClient(createClientFromConsole(in));
        System.out.println(ADD_NEW_USER_SUCCESS);
    }

    private void getAllPositions() {
        System.out.println(INFO_ABOUT_ALL_POSITIONS);
        for (Position position : Position.values()) {
            System.out.println(position.name() + " - " + position.getValue());
        }
    }

    private void getInfoOfAllClients() {
        System.out.println(INFO_ABOUT_ALL_CLIENTS);
        List<Client> clients = clientService.getAll();
        for (Client client : clients) {
            String filesNames = getFilesNames(client);
            System.out.printf((CLIENT_INFO) + "%n", client.getClientId(),
                    client.getFIO(), client.getDateOfBirth(),
                    client.getDepartment().getName(), client.getPosition(),
                    client.getPhone(), filesNames + "\n");
        }
    }

    private void getClientInfo(Scanner in) {
        System.out.println(SELECT_ID_CLIENT_TO_SEE_INFO);
        List<Client> clients = clientService.getAll();
        for (Client client : clients) {
            System.out.println("ID: " + client.getClientId() + " ФИО: " + client.getFIO());
        }

        int id;
        while (true) {
            try {
                id = Integer.parseInt(in.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println(INCORRECT_INPUT);
            }
        }
        Client selectedClient = clientService.getClient(id);


        String filesNames = getFilesNames(selectedClient);

        System.out.printf((CLIENT_INFO) + "%n", selectedClient.getClientId(),
                selectedClient.getFIO(), selectedClient.getDateOfBirth(),
                selectedClient.getDepartment().getName(), selectedClient.getPosition(),
                selectedClient.getPhone(), filesNames);
    }

    private String getFilesNames(Client selectedClient) {
        StringBuilder filesNames = new StringBuilder();
        List<com.borrogg.entities.File> files = selectedClient.getFiles();
        if (files.size() != 0) {
            for (int i = 0; i < files.size(); i++) {
                filesNames.append(i + 1).append(". ").append(files.get(i).getName()).append("\n");
            }
            return filesNames.toString();
        }
        return "Файлов нет!";
    }

    private Client createClientFromConsole(Scanner in) {
        Client client = new Client();

        client.setFIO(getFIO(in));
        client.setDateOfBirth(getDateOfBirth(in));
        client.setDepartment(getDepartment(in, client));
        client.setPosition(getPosition(in, client.getDepartment()));
        client.setPhone(getPhone(in));

        return client;
    }

    private String getPhone(Scanner in) {
        System.out.println(INPUT_PHONE);
        return in.nextLine();
    }

    private String getFIO(Scanner in) {
        System.out.println(INPUT_FIO);
        return in.nextLine();
    }

    private Position getPosition(Scanner in, Department department) {
        while (true) {
            System.out.println(CHOOSE_POSITION);
            int position = 0;
            try {
                String input = in.nextLine();
                if (input.isEmpty()) {
                    return null;
                }
                position = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(INCORRECT_INPUT);
            }
            if (position == 1) {
                return Position.USER;
            } else if (position == 2) {
                if (!clientService.isDepartmentHasABoss(department)) {
                    return Position.BOSS;
                } else {
                    System.out.println(BOSS_IN_DEPARTMENT_EXIST);
                }
            } else if (position == 3) {
                return Position.ADMIN;
            } else {
                System.out.println(INCORRECT_INPUT);
            }
        }
    }

    private Department getDepartment(Scanner in, Client client) {
        while (true) {
            List<Department> departmentList = departmentService.getDepartments();
            System.out.println(INPUT_NUM_DEPARTMENT);
            for (int i = 0; i < departmentList.size(); i++) {
                System.out.println(i + 1 + ". " + departmentList.get(i).getName());
            }
            int numDep = 0;
            try {
                String input = in.nextLine();
                if (input.isEmpty() && client.getDepartment() != null) {
                    return null;
                }
                numDep = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(INCORRECT_INPUT);
            }
            if (numDep <= departmentList.size() && numDep > 0) {
                return departmentList.get(numDep - 1);
            } else {
                System.out.println(INCORRECT_INPUT);
            }
        }
    }

    private LocalDate getDateOfBirth(Scanner in) {
        LocalDate dateOfBirth;
        while (true) {
            System.out.println(INPUT_DATE_OF_BIRTH);
            String dob = in.nextLine();
            if (dob.isEmpty()) {
                return null;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
            try {
                dateOfBirth = LocalDate.ofInstant(simpleDateFormat.parse(dob).toInstant(), ZoneId.systemDefault());
                break;
            } catch (ParseException e) {
                System.out.println(INCORRECT_DATE);
            }
        }
        return dateOfBirth;
    }
}
