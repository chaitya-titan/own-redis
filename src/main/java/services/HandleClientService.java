package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HandleClientService {
    public void handleClient(Socket clientSocket){
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (true) {
                String line = input.readLine();
                System.out.println(line);
                if (line.equalsIgnoreCase("ping")) {
                    clientSocket.getOutputStream().write("+PONG\r\n".getBytes());
                }
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }
}
