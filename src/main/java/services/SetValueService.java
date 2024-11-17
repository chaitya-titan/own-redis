package services;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class SetValueService {
    private Socket clientSocket;
    private HashMap<String, String> map = new HashMap<>();

    public SetValueService(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    public void setValue(String key, String value) throws IOException {
        if(map.containsKey(key)){
            return;
        }
        map.put(key, value);
        clientSocket.getOutputStream().write(String.format("$%d\r\n%s\r\n", "OK".length(), "OK")
                .getBytes());;
    }

    public void getValue(String key) throws IOException {
        if(!map.containsKey(key)){
            return ;
        }
        String value =  map.get(key);

        clientSocket.getOutputStream().write(String.format("$%d\r\n%s\r\n", value.length(), value)
                .getBytes());;
    }
}
