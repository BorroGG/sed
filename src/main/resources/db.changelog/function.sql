CREATE FUNCTION file_format(form varchar)
    RETURNS TABLE (client_id int, fio varchar, format varchar) AS $$
BEGIN
RETURN QUERY (SELECT client.client_id, client.fio, file.format FROM client
                  JOIN file ON client.client_id = file.client_id
                  WHERE form = file.format);
END;
$$LANGUAGE 'plpgsql';