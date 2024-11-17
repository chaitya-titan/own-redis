package controllers;

import services.ConfigGetService;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ConfigController {
    private final Socket clientSocket;
    ConfigGetService configGetService;

    public ConfigController(Socket clientSocket){
        this.clientSocket = clientSocket;
        this.configGetService = new ConfigGetService(clientSocket);
    }

    public void handleGetConfigCommand(BufferedReader input, String[] args) throws IOException {
        while (true){
            String ans = "";
            String line = input.readLine();
            if(!line.startsWith("$") && !line.equalsIgnoreCase("get")){
                if(line.equalsIgnoreCase("dir")){
                    ans = configGetService.getDirConfig(args[1]);
                }else{
                    ans = configGetService.getFileNameConfig(args[2]);
                }
                clientSocket.getOutputStream().write(ans.getBytes());
                clientSocket.getOutputStream().flush();
                break;
            }
        }
    }
}
