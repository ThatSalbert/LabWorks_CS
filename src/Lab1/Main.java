package Lab1;

import Lab1.CaesarCipherA.CaesarCipherA;
import Lab1.CaesarCipherB.CaesarCipherB;
import Lab1.Vigenere.Vigenere;
import Lab1.Playfair.Playfair;

public class Main {
    public static void main(String[] args) {
        CaesarCipherA cipherA = new CaesarCipherA(16);
        String encryptedMessageCaesarA = cipherA.encryptMessage("Attack and defend");
        System.out.println("\u001B[32m" + "Caesar Cipher A Encrypted Message: " + "\u001B[0m" + encryptedMessageCaesarA);
        String decryptedMessageCaesarA = cipherA.decryptMessage(encryptedMessageCaesarA);
        System.out.println("\u001B[32m" + "Caesar Cipher A Decrypted Message: " + "\u001B[0m" + decryptedMessageCaesarA);

        System.out.println();

        CaesarCipherB cipherB = new CaesarCipherB(15, "Interesting");
        String encryptedMessageCaesarB = cipherB.encryptMessage("Some random string");
        System.out.println("\u001B[32m" + "Caesar Cipher B Encrypted Message: " + "\u001B[0m" + encryptedMessageCaesarB);
        String decryptedMessageCaesarB = cipherB.decryptMessage(encryptedMessageCaesarB);
        System.out.println("\u001B[32m" + "Caesar Cipher B Decrypted Message: " + "\u001B[0m" + decryptedMessageCaesarB);

        System.out.println();

        Vigenere vigenere = new Lab1.Vigenere.Vigenere("Abracadabra");
        String messageToEncrypt = "HelloThereYoungTraveler";
        vigenere.extendKey(messageToEncrypt);
        String encryptedMessageVigenere = vigenere.encryptMessage(messageToEncrypt);
        System.out.println("\u001B[32m" + "Vigenere Cipher Encrypted Message: " + "\u001B[0m" + encryptedMessageVigenere);
        String decryptedMessageVigenere = vigenere.decryptMessage(encryptedMessageVigenere);
        System.out.println("\u001B[32m" + "Vigenere Cipher Decrypted Message: " + "\u001B[0m" + decryptedMessageVigenere);

        System.out.println();

        Playfair playfair = new Playfair("Intuitive");
        String encryptedMessagePlayfair = playfair.encryptMessage("Mellow");
        System.out.println("\u001B[32m" + "Playfair Cipher Encrypted Message: " + "\u001B[0m" + encryptedMessagePlayfair);
        String decryptedMessagePlayfair = playfair.decryptMessage(encryptedMessagePlayfair);
        System.out.println("\u001B[32m" + "Playfair Cipher Decrypted Message: " + "\u001B[0m" + decryptedMessagePlayfair);
    }
}
