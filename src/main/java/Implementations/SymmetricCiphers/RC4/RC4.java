package Implementations.SymmetricCiphers.RC4;

public class RC4 {
    private String permutationKey;
    private int[] stringToBytes(String input){
        byte[] bytes = input.getBytes();
        int[] intBytes = new int[input.length()];

        for(int counter = 0; counter < input.length(); counter++){
            intBytes[counter] = bytes[counter];
        }

        return intBytes;
    }

    private String bytesToString(int[] input){
        byte[] bytes = new byte[input.length];

        for(int counter = 0; counter < input.length; counter++){
            bytes[counter] = (byte) input[counter];
        }

        return new String(bytes);
    }

    public RC4 (String permutationKey){
        this.permutationKey = permutationKey;
    }

    public int[] keyArrayGen(){
        int[] S = new int[256];
        int[] keyToBytes = stringToBytes(this.permutationKey);

        for(int counter = 0; counter < 256; counter++){
            S[counter] = counter;
        }

        int j = 0;
        for(int counter = 0; counter < 256; counter++){
            j = (j + S[counter] + keyToBytes[counter % keyToBytes.length]) % 256;
            int tmp = S[counter];
            S[counter] = S[j];
            S[j] = tmp;
        }

        return S;
    }

    public String processMessage (String message){
        int[] S = keyArrayGen();
        int[] messageToBytes = stringToBytes(message);
        int[] processedMessageBytes = new int[message.length()];

        int i = 0, j = 0;
        for(int counter = 0; counter < message.length(); counter++){
            i = (i + 1) % 256;
            j = (j + S[i]) % 256;

            int tmp = S[counter];
            S[counter] = S[j];
            S[j] = tmp;

            int k = S[(S[i] + S[j]) % 256];
            processedMessageBytes[counter] = messageToBytes[counter] ^ k;
        }

        return bytesToString(processedMessageBytes);
    }
}
