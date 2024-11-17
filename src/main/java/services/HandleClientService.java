package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class HandleClientService {
    public void handleClient(Socket clientSocket){
        ArrayList<ArrayList<String>> arr = new ArrayList<>();
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            SetValueService setValueService = new SetValueService(clientSocket);
            while (true) {
                String line = input.readLine();
                System.out.println(line);
                if (line.equalsIgnoreCase("ping")) {
                    clientSocket.getOutputStream().write("+PONG\r\n".getBytes());
                }else if(line.equalsIgnoreCase("echo")){
                    input.readLine();
                    String data = input.readLine();
                    clientSocket.getOutputStream().write(
                            String.format("$%d\r\n%s\r\n", data.length(), data)
                                    .getBytes());
                }else if(line.equalsIgnoreCase("set")){
                    input.readLine();
                    String key = input.readLine();
                    input.readLine();
                    String value = input.readLine();

                    System.out.println(key + " " + value);

                    setValueService.setValue(key, value);

                }else if(line.equalsIgnoreCase("get")){
                    input.readLine();
                    String key = input.readLine();

                    System.out.println(key);

                    setValueService.getValue(key);
                }
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }
}
