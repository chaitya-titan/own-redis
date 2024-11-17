package services;

import java.io.IOException;
import java.net.Socket;

public class ConfigGetService {
    private Socket clientSocket;

    public ConfigGetService(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    private String createString(String s){
        String res = "$";
        res += String.valueOf(s.length());
        res += "\r\n";
        res += s;
        res += "\r\n";
        return res;
    }

    public String getDirConfig(String dir) throws IOException {
        String s = "*2\r\n";
        s += createString("dir");
        s += createString(dir);
        System.out.println("s " + s);
        return s;
    }

    public String getFileNameConfig(String fileName) throws IOException {
        String s = "*2\r\n";
        s += createString("dbfilename");
        s += createString(fileName);
        return s;
    }
}
