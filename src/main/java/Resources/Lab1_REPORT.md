# Laboratory Work 1: Classical ciphers & Caesar cipher

## Caesar cipher with one key used for substitution

To encrypt the message using the Caesar cipher with substitution key, the following expression was used:

$em = enc_{k}(x) = x + k (mod n)$

Where:

- ***x*** is the given message
- ***k*** is the substitution key
- ***n*** is the size of the alphabet

Function that encrypts the message:
```java
public String encryptMessage(final String message){
    //Transforms the given message to a lower case message and removes whitespaces.
    String messageToEncrypt = message.toLowerCase().replaceAll("\\s", "");
    StringBuilder encryptedMessage = new StringBuilder();
    for(int i = 0; i < messageToEncrypt.length(); i++){
        //Gets the index of the character to be encrypted
        int x = alphabet.indexOf(messageToEncrypt.charAt(i));
        //The expression described above
        int enc = (x + substitutionKey) % alphabet.length();
        //Finds the character in the alphabet
        char encrypt = alphabet.charAt(enc);
        //Adds encrypted letter to the encryptedMessage StringBuilder
        encryptedMessage.append(encrypt);
    }
    //Returns the encrypted message
    return encryptedMessage.toString();
}
```

The encryption is done using the following expression:

$dm = dec_{k}(x) = x - k (mod n)$

Function that encrypts the message:

```java
public String decryptMessage(final String encryptedMessage){
    //Transforms the given message to a lower case message and removes whitespaces.
    String messageToDecrypt = encryptedMessage.toLowerCase().replaceAll("\\s", "");
    StringBuilder decryptedMessage = new StringBuilder();
    for(int i = 0; i < messageToDecrypt.length(); i++){
        //Gets the index of the character to be decrypted
        int x =  alphabet.indexOf(messageToDecrypt.charAt(i));
        //The expression described above
        int dec = (x - substitutionKey) % alphabet.length();
        //Makes sure it's not out of bounds
        if (dec < 0) {
            dec = alphabet.length() + dec;
        }
        //Finds the character in the alphabet
        char decrypt = alphabet.charAt(dec);
        //Adds decrypted letter to the decryptedMessage StringBuilder
        decryptedMessage.append(decrypt);
    }
    //Returns the decrypted message
    return decryptedMessage.toString();
}
```

## Caesar cipher with one key used for substitution and permutation of the alphabet

To encrypt the message using the Caesar cipher with substitution key and a permutation key, the same expressions were used.
The permutation key will be added at the front of the alphabet and then it will be sent to a new function called *removeDuplicates()* to remove the duplicated letters from the now concatenated alphabet.

```java
private String removeDuplicates(String someString){
        char[] perm = someString.toLowerCase().replaceAll("\\s", "").toCharArray();
        Set<Character> characterSet = new LinkedHashSet<Character>();
        for (char c : perm){
            characterSet.add(c);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(Character character : characterSet){
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }

```

To encrypt and decrypt the messages, the same functions were used as in the previous cipher.


## Implementations.ClassicalCiphers.Vigenere cipher

To encrypt the message using the Implementations.ClassicalCiphers.Vigenere cipher, the key will be copied until it matches the length of the given message.
For this, the function *extendKey()* was added.

```java
public void extendKey(String message){
    while(permutationKey.length() <= message.replaceAll("\\s", "").length()){
        this.permutationKey += this.permutationKey;
    }
    if (this.permutationKey.length() > message.length()){
        //Removes the part that goes beyond the length of the message
        this.permutationKey = this.permutationKey.substring(0, message.length());
    }
}
```

The encryption is done using the following expression:

$em = enc_{k}(x) = x + k (mod n)$

Where:

- ***x*** is the given message
- ***k*** is the substitution key
- ***n*** is the size of the alphabet

Function that encrypts the message:

```java
public String encryptMessage(final String message){
    String messageToEncrypt = message.toUpperCase().replaceAll("\\s", "");
    StringBuilder encryptedMessage = new StringBuilder();
    for (int i = 0; i < messageToEncrypt.length(); i++){
        int enc = (messageToEncrypt.charAt(i) + permutationKey.charAt(i)) % alphabet.length();
        char encrypt = alphabet.charAt(enc);
        encryptedMessage.append(encrypt);
    }
    return encryptedMessage.toString();
}
```

To decrypt the message, the following expression is used:

$dm = dec_{k}(x) = (x - k + 26) (mod n)$

Function that decrypts the message:

```java
public String decryptMessage(final String encryptedMessage){
    String messageToDecrypt = encryptedMessage.toUpperCase().replaceAll("\\s", "");
    StringBuilder decryptedMessage = new StringBuilder();
    for (int i = 0; i < messageToDecrypt.length(); i++){
        int dec = (messageToDecrypt.charAt(i) - permutationKey.charAt(i) + 26) % alphabet.length();
        char decrypt = alphabet.charAt(dec);
        decryptedMessage.append(decrypt);
    }
    return decryptedMessage.toString();
}
```

## Implementations.ClassicalCiphers.Playfair cipher

To encrypt the message using the Implementations.ClassicalCiphers.Playfair cipher, the key will be added at the front of the alphabet and will go throught the *removeDuplicates()* function.
After that, the alphabet will be transformed into an array of size *5x5* (letter *j* will be removed to fit the alphabet into the array). The function that Transforms the alphabet to an array:

```java
private void alphabetToArray(String alphabet){
    this.alphabetArray = new String[5][5];
    int counter = 0;
    for(int i = 0; i < 5; i++){
        for(int j = 0; j < 5; j++){
            this.alphabetArray[i][j] = String.valueOf(alphabet.charAt(counter));
            counter++;
        }
    }
}
```

Before the encryption starts, a few conditions needs to be met.
If there is a group of letters next to each other that are the same, letter *x* will be added between them.
If the message length is odd then we add at the end of the message letter *z*.
The following function checks if every condition is met:

```java
private String checkConditions(String message){
    StringBuilder messageBuilder = new StringBuilder(message);
    for(int i = 0; i < message.length(); i++){
        if(messageBuilder.charAt(i) == messageBuilder.charAt(i+1)){
            messageBuilder.insert(i+1, "x");
        }
    }
    if(messageBuilder.length() % 2 != 0){
        messageBuilder.append("z");
    }
    return messageBuilder.toString();
}
```

If all the conditions are met, then the message cand be split every 2 characters to start the encryption.
This is done in the following function:

```java
private String[] split(String message){
    String messageToSplit = checkConditions(message);
    ArrayList<String> splitMessage = new ArrayList<String>();
    for(int i = 0; i < messageToSplit.length(); i += 2){
        String leftOfPair = String.valueOf(messageToSplit.charAt(i));
        String rightOfPair = String.valueOf(messageToSplit.charAt(i+1));
        splitMessage.add(leftOfPair + rightOfPair);
    }
    return splitMessage.toArray(new String[splitMessage.size()]);
}
```

**Now the encryption can begin.**

The pairs of letters are taken and checked for their positions on the created *5x5* array of the alphabet.

- If the letters are in the same row then the encrypted letters will be taken from the column to the right.
- If the letters are in the same column then the encrypted letters will be taken from the row from below.
- If the latters are not in the same column and row then an imaginary trinagle is formed and the letters on the horizontal opposite corner of the rectangle are taken.

```java
public String encryptMessage(String message){
    String[] splitMessage = split(message.toLowerCase());
    String[] encryptedMessageArray = new String[splitMessage.length];
    for(int i = 0; i < splitMessage.length; i++){
        int[] firstLetterSearch = searchFor(String.valueOf(splitMessage[i].charAt(0)));
        int[] secondLetterSearch = searchFor(String.valueOf(splitMessage[i].charAt(1)));
        if(firstLetterSearch[0] == secondLetterSearch[0]){
            int rowToCopy = firstLetterSearch[0];
            int columnToCopy1 = firstLetterSearch[1] + 1;
            int columnToCopy2 = secondLetterSearch[1] + 1;
            if(columnToCopy1 > 4){
                columnToCopy1 = 0;
            }
            if (columnToCopy2 > 4){
                columnToCopy2 = 0;
            }
            encryptedMessageArray[i] = alphabetArray[rowToCopy][columnToCopy1] + alphabetArray[rowToCopy][columnToCopy2];
        } else if (firstLetterSearch[1] == secondLetterSearch[1]){
            int columnToCopy = firstLetterSearch[1];
            int rowToCopy1 = firstLetterSearch[0] + 1;
            int rowToCopy2 = secondLetterSearch[0] + 1;
            if(rowToCopy1 > 4){
                rowToCopy1 = 0;
            }
            if(rowToCopy2 > 4){
                rowToCopy2 = 0;
            }
            encryptedMessageArray[i] = alphabetArray[rowToCopy1][columnToCopy] + alphabetArray[rowToCopy2][columnToCopy];
        } else {
            encryptedMessageArray[i] = alphabetArray[firstLetterSearch[0]][secondLetterSearch[1]] + alphabetArray[secondLetterSearch[0]][firstLetterSearch[1]];
        }
    }
    return String.join("", encryptedMessageArray);
}
```

To decrypt the message, the encrypted message will also be split every 2 characters using the *split()* function:
```java
String[] splitEncryptedMessage = encryptedMessage.split("(?<=\\G..)");
```
The pairs of letters are taken and checked for their positions on the created *5x5* array of the alphabet.

- If the letters are in the same row then the decrypted letters will be taken from the column to the left.
- If the letters are in the same column then the decrypted letters will be taken from the row from above.
- If the latters are not in the same column and row then an imaginary trinagle is formed and the letters on the horizontal opposite corner of the rectangle are taken.

```java
public String decryptMessage(String encryptedMessage){
    String[] splitEncryptedMessage = encryptedMessage.split("(?<=\\G..)");
    String[] decryptedMessageArray = new String[splitEncryptedMessage.length];
    for(int i = 0; i < splitEncryptedMessage.length; i++){
    int[] firstLetterSearch = searchFor(String.valueOf(splitEncryptedMessage[i].charAt(0)));
        int[] secondLetterSearch = searchFor(String.valueOf(splitEncryptedMessage[i].charAt(1)));
        if(firstLetterSearch[0] == secondLetterSearch[0]){
            int rowToCopy = firstLetterSearch[0];
            int columnToCopy1 = firstLetterSearch[1] - 1;
            int columnToCopy2 = secondLetterSearch[1] - 1;
            if(columnToCopy1 < 0){
                columnToCopy1 = 4;
            }
            if (columnToCopy2 < 0){
                columnToCopy2 = 4;
            }
            decryptedMessageArray[i] = alphabetArray[rowToCopy][columnToCopy1] + alphabetArray[rowToCopy][columnToCopy2];
        } else if (firstLetterSearch[1] == secondLetterSearch[1]){
            int columnToCopy = firstLetterSearch[1];
            int rowToCopy1 = firstLetterSearch[0] - 1;
            int rowToCopy2 = secondLetterSearch[0] - 1;
            if(rowToCopy1 < 0){
                rowToCopy1 = 4;
            }
            if(rowToCopy2 < 0){
                rowToCopy2 = 4;
            }
            decryptedMessageArray[i] = alphabetArray[rowToCopy1][columnToCopy] + alphabetArray[rowToCopy2][columnToCopy];
        } else {
            decryptedMessageArray[i] = alphabetArray[firstLetterSearch[0]][secondLetterSearch[1]] + alphabetArray[secondLetterSearch[0]][firstLetterSearch[1]];
        }
    }
    return String.join("", decryptedMessageArray);
}
```