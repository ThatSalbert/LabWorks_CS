package Implementations.Hashing;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Database {
    private final String password;
    private static final HashMap<String, Database> databaseHashMap = new HashMap<>();

    private Database(String username, String password) throws NoSuchAlgorithmException {
        this.password = passwordHash(password);
        databaseHashMap.put(username, this);
    }

    private String passwordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printHexBinary(encodedPassword);
    }

    public static Database getDataById(String username){
        if(databaseHashMap.get(username) != null) {
            return databaseHashMap.get(username);
        } else {
            return null;
        }
    }

    public static Database getData(String username, String password) throws NoSuchAlgorithmException {
        if(databaseHashMap.get(username) == null) {
            return new Database(username, password);
        }
        return getDataById(username);
    }

    public static void getAllData(){
        for(String key: databaseHashMap.keySet()){
            System.out.println("Username: " + key + ", Password: " + databaseHashMap.get(key).password);
        }
    }

    public String getPassword(){
        return password;
    }

    public static boolean authenticate(String username, String password) throws NoSuchAlgorithmException {
        Database temp = getDataById(username);
        if(temp != null){
            byte[] hashedPassword = MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8));
            if(DatatypeConverter.printHexBinary(hashedPassword).equals(getDataById(username).getPassword())){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
