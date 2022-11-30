package Implementations.Hashing;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Database {
    private String username;
    private String password;
    private static final HashMap<Integer, Database> databaseHashMap = new HashMap<>();

    private Database(int user_id, String username, String password) throws NoSuchAlgorithmException {
        this.username = username;
        this.password = passwordHash(password);
        databaseHashMap.put(user_id, this);
    }

    private String passwordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printHexBinary(encodedPassword);
    }

    public static Database getDataById(int user_id){
        if(databaseHashMap.get(user_id) != null) {
            return databaseHashMap.get(user_id);
        } else {
            System.out.println("User does not exist.");
        }
        return null;
    }

    public static Database getData(int user_id, String username, String password) throws NoSuchAlgorithmException {
        if(databaseHashMap.get(user_id) == null) {
            return new Database(user_id, username, password);
        }
        return getDataById(user_id);
    }

    public static void getAllData(){
        for(Integer key: databaseHashMap.keySet()){
            System.out.println("User ID: " + key + ", Username: " + databaseHashMap.get(key).username + ", Password: " + databaseHashMap.get(key).password);
        }
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
