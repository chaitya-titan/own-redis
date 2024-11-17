package services;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SetValueService {
    private Socket clientSocket;
    private HashMap<String, String> map = new HashMap<>();
    private HashMap<String, Long> expiryMap = new HashMap<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

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
            clientSocket.getOutputStream().write("$-1\r\n".getBytes());
            return ;
        }
        String value =  map.get(key);

        clientSocket.getOutputStream().write(String.format("$%d\r\n%s\r\n", value.length(), value)
                .getBytes());;
    }

    public void setExpiryMap(String key, String value, String expiry) throws IOException {
        long time = Long.parseLong(expiry);
        long expiryTime = System.currentTimeMillis() + time;

        map.put(key, value);
        expiryMap.put(key, expiryTime);
        clientSocket.getOutputStream().write(String.format("$%d\r\n%s\r\n", "OK".length(), "OK")
                .getBytes());;
        executorService.schedule(() -> deleteKeyIfExpired(key), time, TimeUnit.MILLISECONDS);
    }

    private void deleteKeyIfExpired(String key){
        Long expiryTime = expiryMap.get(key);
        if (expiryTime != null && System.currentTimeMillis() >= expiryTime) {
            map.remove(key);
            expiryMap.remove(key);
        }
    }
}
