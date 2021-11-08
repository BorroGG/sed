package com.borrogg.app;

import com.borrogg.entities.Client;
import com.borrogg.entities.Department;
import com.borrogg.enums.Position;
import com.borrogg.service.DepartmentService;
import com.borrogg.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import static com.borrogg.util.ProgramUtil.*;

@Component
public class CommandLineApp implements CommandLineRunner {

    private final ClientService clientService;
    private final DepartmentService departmentService;

    @Autowired
    public CommandLineApp(ClientService clientService, DepartmentService departmentService) {
        this.clientService = clientService;
        this.departmentService = departmentService;
    }

    @Override
    public void run(String... args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println(MENU);
            int inputDecimal = 0;
            try {
                inputDecimal = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(INCORRECT_INPUT);
            }

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
                in.close();
                break;
            } else {
                System.out.println(INCORRECT_INPUT);
            }
        }
    }

    private void changeClientData(Scanner in) {
        System.out.println(SELECT_USER);

        List<Client> clients = getAndShowClients();

        int num = getNum(in);

        Client selectedClient = clients.get(num);
        System.out.println(CURRENT_USER_NAME + selectedClient.getFIO());
        System.out.println(INPUT_OT_ENTER);
        String value = in.nextLine();
        if (!value.isEmpty()) {
            selectedClient.setFIO(value);
        }
        System.out.println(CURRENT_USER_DOB + selectedClient.getDateOfBirth());
        System.out.println(INPUT_OT_ENTER);
        LocalDate dob = getDateOfBirth(in);
        if (dob != null) {
            selectedClient.setDateOfBirth(dob);
        }
        System.out.println(CURRENT_USER_DEPARTMENT + selectedClient.getDepartment().getName());
        System.out.println(INPUT_OT_ENTER);
        Department department = getDepartment(in, selectedClient);
        if (department != null) {
            selectedClient.setDepartment(department);
        }
        System.out.println(CURRENT_USER_POSITION + selectedClient.getPosition());
        System.out.println(INPUT_OT_ENTER);
        Position position = getPosition(in, selectedClient.getDepartment());
        if (department != null) {
            selectedClient.setPosition(position);
        }
        System.out.println(CURRENT_USER_PHONE + selectedClient.getPhone());
        System.out.println(INPUT_OT_ENTER);
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
                System.out.printf((CLIENT_INFO) + "%n", client.getClientId(),
                        client.getFIO(), client.getDateOfBirth(),
                        client.getDepartment().getName(), client.getPosition(), client.getPhone());
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
            System.out.println(position);
        }
    }

    private void getInfoOfAllClients() {
        System.out.println(INFO_ABOUT_ALL_CLIENTS);
        List<Client> clients = clientService.getAll();
        for (Client client : clients) {
            System.out.printf((CLIENT_INFO) + "%n", client.getClientId(),
                    client.getFIO(), client.getDateOfBirth(),
                    client.getDepartment().getName(), client.getPosition(), client.getPhone());
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
        System.out.printf((CLIENT_INFO) + "%n", selectedClient.getClientId(),
                selectedClient.getFIO(), selectedClient.getDateOfBirth(),
                selectedClient.getDepartment().getName(), selectedClient.getPosition(), selectedClient.getPhone());
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
