package com.winestore.winestore.tempStore;
import java.util.concurrent.ConcurrentHashMap;

public class TempUserDataStore {
    private static final ConcurrentHashMap<String, String> tempUserData = new ConcurrentHashMap<>();

    public static void save(String email, String password) {
        tempUserData.put(email, password);
    }

    public static String getPassword(String email) {
        return tempUserData.get(email);
    }

    public static void remove(String email) {
        tempUserData.remove(email);
    }

    public static boolean contains(String email) {
        return tempUserData.containsKey(email);
    }
}
