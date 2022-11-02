package Implementations.ClassicalCiphers.Vigenere;

public class Vigenere {
    private String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String permutationKey;

    public Vigenere(String permutationKey) {
        this.permutationKey = permutationKey.toUpperCase();
    }

    public void extendKey(String message){
        while(permutationKey.length() <= message.replaceAll("\\s", "").length()){
            this.permutationKey += this.permutationKey;
        }
        if (this.permutationKey.length() > message.length()){
            this.permutationKey = this.permutationKey.substring(0, message.length());
        }
    }

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
}
