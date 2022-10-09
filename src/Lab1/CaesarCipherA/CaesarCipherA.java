package Lab1.CaesarCipherA;

public class CaesarCipherA {
    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private final int substitutionKey;

    public CaesarCipherA(final int substitutionKey) {
        this.substitutionKey = substitutionKey;
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
