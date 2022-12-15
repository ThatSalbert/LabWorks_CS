# Laboratory Work 4: Hash functions and Digital Signatures

## Hashing

For the hashing part of the laboratory work, an in memory database was created. The database is made by
the singleton pattern: a single instance of a hashmap where the key Integer is the id field and value is the
Database class with the String fields username and password.

```java
public class Database {
    private String username;
    private String password;
    private static final HashMap<Integer, Database> databaseHashMap = new HashMap<>();
}
```

*getData()* function will be called to insert values into the hashmap described above if the given user information
is not already in the hashmap. 

```java
public static Database getData(int user_id, String username, String password) throws NoSuchAlgorithmException {
    if(databaseHashMap.get(user_id) == null) {
        return new Database(user_id, username, password);
    }
    return getDataById(user_id);
}

private Database(int user_id, String username, String password) throws NoSuchAlgorithmException {
    this.username = username;
    this.password = passwordHash(password);
    databaseHashMap.put(user_id, this);
}
```

Inside the function that creates the new user the password will be immediately hashed using the *passwordHash()* function.
The hashing is done by using the MessageDigest class which has one-way hashing functions that take arbitrary-sized data and output a fixed-length hash value.
The function *MessageDigest.getInstance("SHA-256")* is called that will hash the password using the SHA-256 hashing algorithm.
Then the given user password will be digested using the *digest()* function that will make the final hash computation operations such as padding
and will return an array of bytes. In the end, the array of bytes is transformed into a string of hexadecimal values using the DatatypeConverter with the
*printHexBinary()* function.

```java
private String passwordHash(String password) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] encodedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));
    return DatatypeConverter.printHexBinary(encodedPassword);
}
```

# Signature Check

For the signature check part of the laboratory work, the original message is being encrypted and decrypted
using the RSA cipher from the previous laboratory work. The original message is also hashed using the same method
as described above: using MessageDigest class with the *getInstance("SHA-256")*. The decrypted message is also hashed
using the same method. 

At the end, the *String.equals()* function is called to check if both hash strings are equal.
```java
if (DatatypeConverter.printHexBinary(hashedMessage1).equals(DatatypeConverter.printHexBinary(hashedMessage2))){
        System.out.println("\u001B[32m" + "Result: " + "\u001B[0m" + "Hashes are the same.");
    } else {
        System.out.println("\u001B[32m" + "Result: " + "\u001B[0m" + "Hashes are not the same.");
}
```