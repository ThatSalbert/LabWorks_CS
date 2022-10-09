package Lab1.CaesarCipherB;

import java.util.LinkedHashSet;
import java.util.Set;

public class CaesarCipherB {
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private final int substitutionKey;
    private final String permutationKey;

    public CaesarCipherB(final int substitutionKey, final String permutationKey) {
        this.substitutionKey = substitutionKey;
        this.permutationKey = permutationKey;
        this.alphabet = removeDuplicates(permutationKey) + this.alphabet;
        this.alphabet = removeDuplicates(this.alphabet);
    }

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

    public String encryptMessage(final String message){
        String messageToEncrypt = message.toLowerCase().replaceAll("\\s", "");
        StringBuilder encryptedMessage = new StringBuilder();
        for(int i = 0; i < messageToEncrypt.length(); i++){
            int x = alphabet.indexOf(messageToEncrypt.charAt(i));
            int enc = (x + substitutionKey) % alphabet.length();
            char encrypt = alphabet.charAt(enc);

            encryptedMessage.append(encrypt);

        }
        return encryptedMessage.toString();
    }

    public String decryptMessage(final String encryptedMessage){
        String messageToDecrypt = encryptedMessage.toLowerCase().replaceAll("\\s", "");
        StringBuilder decryptedMessage = new StringBuilder();
        for(int i = 0; i < messageToDecrypt.length(); i++){
            int x =  alphabet.indexOf(messageToDecrypt.charAt(i));
            int dec = (x - substitutionKey) % alphabet.length();
            if (dec < 0) {
                dec = alphabet.length() + dec;
            }
            char decrypt = alphabet.charAt(dec);

            decryptedMessage.append(decrypt);
        }
        return decryptedMessage.toString();
    }
}
