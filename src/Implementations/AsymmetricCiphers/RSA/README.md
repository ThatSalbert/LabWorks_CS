# Laboratory Work 3: Asymmetric Ciphers

## RSA Cipher

Before encryption and decryption of the message, a public and a private key must be generated.
To generate the keys, first the program will generate 2 random prime numbers. In this program, the numbers
will be of the type *BigInteger* of bit length 512 bits. The function that generates the numbers: 

```java
BigInteger.probablePrime(512, new Random())
```

After the prime numbers *p* and *q* were generated, the generation of keys can be started.
The first part of public key *n* is calculated using the formula:

$n = p \cdot q$

After calculating *n*, the second part of the public key *e* needs to be found. *e* needs to follow the following criteria:

$-1 < e < \phi (n)$ where $\phi (n) = (p - 1)\cdot (q - 1)$

The following function finds *e*:
```java
private BigInteger findCoprime(BigInteger phi){
        //Generating a random number that will be used to select candidates to be tested for primality as a set of bits
        Random random = new Random();
        BigInteger coprimeNumber;
        //A while loop that will generate the coprime number e that meets the criteria -1 < e < phi(n)
        do{
        coprimeNumber = BigInteger.probablePrime(maxSize, random);
        } while (gcd(phi, coprimeNumber).compareTo(BigInteger.ONE) > 0 && coprimeNumber.compareTo(phi) < 0);
        return coprimeNumber;
        }
```

Now that the public key was generated, a private key must be generated as well. 

Private key *d* is found by finding the modular inverse of *e*. 
To find the modular inverse, the Extended Euclidean Algorithm will be used.

$ex + \phi (n)y = gcd(e, \phi (n))$


```java
private BigInteger[] modInverse(BigInteger e, BigInteger phi){
        //The function will return an array of size 3
        //Where on position 0 stands the GCD of the two given numbers
        //Where on position 1 stands x also the modular inverse that will be extracted
        //Where on position 2 stands y
        if(phi.equals(BigInteger.ZERO)){
        return new BigInteger[]{e, BigInteger.ONE, BigInteger.ZERO};
        }
        BigInteger[] ans = modInverse(phi, e.mod(phi));
        return new BigInteger[]{ans[0], ans[2], ans[1].subtract(e.divide(phi).multiply(ans[2]))};
        }
```

After the both keys were generated. The encryption and decryption of the messages can begin. 

Encryption is done using the formula:

$E = m^{e} mod N$

Decryption is done using the formula:

$D = E^{D} mod N$
```java
public byte[] encryptMessage(byte[] message){
        byte[] encryptedMessage = new BigInteger(message).modPow(this.e, this.n).toByteArray();
        return encryptedMessage;
    }

    public byte[] decryptMessage(byte[] encryptedMessage){
        byte[] decryptedMessage = new BigInteger(encryptedMessage).modPow(this.d, this.n).toByteArray();
        return decryptedMessage;
    }
```