package Implementations.AsymmetricCiphers.RSA;

import java.math.BigInteger;
import java.util.Random;

public class RSA {
    private final int maxSize = 512;
    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;

    public static String bytesToString(byte[] input){
        StringBuilder result = new StringBuilder();
        for(byte b : input){
            result.append(b);
        }
        return result.toString();
    }

    public RSA(BigInteger p, BigInteger q){
        this.p = p;
        this.q = q;
        this.n = p.multiply(q);
        this.phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        this.e = findCoprime(this.phi);
        this.d = modInverse(this.e, this.phi)[1];
    }

    private BigInteger[] modInverse(BigInteger e, BigInteger phi){
        if(phi.equals(BigInteger.ZERO)){
            return new BigInteger[]{e, BigInteger.ONE, BigInteger.ZERO};
        }
        BigInteger[] ans = modInverse(phi, e.mod(phi));
        return new BigInteger[]{ans[0], ans[2], ans[1].subtract(e.divide(phi).multiply(ans[2]))};
    }

    private BigInteger findCoprime(BigInteger phi){
        Random random = new Random();
        BigInteger coprimeNumber;
        do{
            coprimeNumber = BigInteger.probablePrime(maxSize, random);
        } while (gcd(phi, coprimeNumber).compareTo(BigInteger.ONE) > 0 && coprimeNumber.compareTo(phi) < 0);
        return coprimeNumber;
    }

    private BigInteger gcd(BigInteger p, BigInteger q){
        if (q.equals(BigInteger.ZERO)){
            return p;
        }
        return gcd(q, p.mod(q));
    }

    public byte[] encryptMessage(byte[] message){
        byte[] encryptedMessage = new BigInteger(message).modPow(this.e, this.n).toByteArray();
        return encryptedMessage;
    }

    public byte[] decryptMessage(byte[] encryptedMessage){
        byte[] decryptedMessage = new BigInteger(encryptedMessage).modPow(this.d, this.n).toByteArray();
        return decryptedMessage;
    }
}
