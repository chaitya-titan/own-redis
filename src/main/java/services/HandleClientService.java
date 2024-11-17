package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class HandleClientService {
    public void handleClient(Socket clientSocket) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            SetValueService setValueService = new SetValueService(clientSocket);
            String commandsLength = "";

            while (true) {

                String line = input.readLine();
                System.out.println(line);

                if (line == null) {
                    break; // If input is null, terminate the connection.
                }

                if(line.startsWith("*")){
                    commandsLength = line.substring(1,2);
                }


                switch (line.toLowerCase()) {
                    case "ping":
                        clientSocket.getOutputStream().write("+PONG\r\n".getBytes());
                        break;

                    case "echo":
                        input.readLine();
                        String data = readLineOrNull(input);
                        if (data != null) {
                            clientSocket.getOutputStream().write(
                                    String.format("$%d\r\n%s\r\n", data.length(), data).getBytes());
                        }
                        break;

                    case "set":
                        handleSetCommand(commandsLength, input, setValueService);
                        break;

                    case "get":
                        handleGetCommand(input, setValueService);
                        break;

                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleSetCommand(String commandsLength, BufferedReader input, SetValueService setValueService) throws IOException {
        input.readLine();
        String key = readLineOrNull(input);
        input.readLine();
        String value = readLineOrNull(input);

        if(!commandsLength.equalsIgnoreCase("5")){
            setValueService.setValue(key, value);
            return;
        }
        String check = readLineOrNull(input);
        String px = readLineOrNull(input);
        String time = null;
        input.readLine();
        time = readLineOrNull(input);
        setValueService.setExpiryMap(key, value, time);
    }

    private void handleGetCommand(BufferedReader input, SetValueService setValueService) throws IOException {
        input.readLine();
        String key = readLineOrNull(input);
        if (key != null) {
            setValueService.getValue(key);
        }
    }

    private String readLineOrNull(BufferedReader input) throws IOException {
        String line = input.readLine();
        return (line != null && !line.trim().isEmpty()) ? line : null;
    }
}
