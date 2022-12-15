package Implementations.Hashing;

import Implementations.AsymmetricCiphers.RSA.RSA;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SignatureCheck {
    private String message;
    private byte[] encryptedMessage;
    private byte[] decryptedMessage;
    private byte[] hashedMessage1;
    private byte[] hashedMessage2;

    public SignatureCheck(String message) throws NoSuchAlgorithmException {
        this.message = message;
        System.out.println("\n" + "\u001B[32m" + "Original message: " + "\u001B[0m" + this.message);
        performEncryption(this.message);
        performHashing();
    }

    private void performEncryption(String message){
        RSA RSAcipher = new RSA(BigInteger.probablePrime(512, new Random()), BigInteger.probablePrime(512, new Random()));
        this.encryptedMessage = RSAcipher.encryptMessage(message.getBytes());
        System.out.println("\u001B[32m" + "Encrypted Message (RSA): " + "\u001B[0m" + RSA.bytesToString(this.encryptedMessage));
        this.decryptedMessage = RSAcipher.decryptMessage(this.encryptedMessage);
        System.out.println("\u001B[32m" + "Decrypted Message (RSA): " + "\u001B[0m" + new String(this.decryptedMessage));
    }

    private void performHashing() throws NoSuchAlgorithmException {
        this.hashedMessage1 = MessageDigest.getInstance("SHA-256").digest(message.getBytes(StandardCharsets.UTF_8));
        System.out.println("\u001B[32m" + "Hashed original message: " + "\u001B[0m" + DatatypeConverter.printHexBinary(hashedMessage1));

        this.hashedMessage2 = MessageDigest.getInstance("SHA-256").digest(new String(decryptedMessage).getBytes(StandardCharsets.UTF_8));
        System.out.println("\u001B[32m" + "Hashed decrypted message: " + "\u001B[0m" + DatatypeConverter.printHexBinary(hashedMessage2));

        if (DatatypeConverter.printHexBinary(hashedMessage1).equals(DatatypeConverter.printHexBinary(hashedMessage2))){
            System.out.println("\u001B[32m" + "Result: " + "\u001B[0m" + "Hashes are the same.");
        } else {
            System.out.println("\u001B[32m" + "Result: " + "\u001B[0m" + "Hashes are not the same.");
        }
    }
}
