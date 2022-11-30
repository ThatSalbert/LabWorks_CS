# Laboratory Work 4: Hash functions and Digital Signatures

## Hashing

For the hashing part of the laboratory work, an in memory database was created. The database is made by
the singleton pattern: a single instance of a hashmap where Integer is the id field and 

```java
public class Database {
    private String username;
    private String password;
    private static final HashMap<Integer, Database> databaseHashMap = new HashMap<>();
}
```