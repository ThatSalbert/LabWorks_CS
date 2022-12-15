package Implementations.SymmetricCiphers.SDES;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SDES {
    private final int[] permutationKeyArray = new int[10];
    private int P10[];
    private int P8[];
    private int[] key1;
    private int[] key2;

    private int[] IP = new int[8];
    private int[] EP = new int[8];
    private int[] IP_inv = new int[8];

    //Constants
    private int[][] S0 = { { 1, 0, 3, 2 }, { 3, 2, 1, 0 }, { 0, 2, 1, 3 }, { 3, 1, 3, 2 } };
    private int[][] S1 = { { 0, 1, 2, 3 }, { 2, 0, 1, 3 }, { 3, 0, 1, 0 }, { 2, 1, 0, 3 } };

    public SDES(String permutationKey){
        this.P10 = permutation(10);
        this.P8 = permutation(8);

        int[][] keys = keyGen(permutationKey);
        this.key1 = keys[0];
        this.key2 = keys[1];
    }

    static int[] inversePermutation(int[] arr, int size) {
        int[] arr2 = new int[size];
        for (int i = 0; i < size; i++){
            arr2[arr[i] - 1] = i + 1;
        }
        return arr2;
    }

    public int[] permutation(int n){
        Integer[] arr = new Integer[n];
        for(int i = 0; i < n; i++){
            arr[i] = i+1;
        }
        List<Integer> intList = Arrays.asList(arr);
        Collections.shuffle(intList);
        intList.toArray(arr);

        int[] result = new int[n];
        for(int i = 0; i < n; i++){
            result[i] = intList.get(i);
        }

        return result;
    }

    private int[][] keyGen(String key){
        for(int i = 0; i < 10; i++){
            this.permutationKeyArray[i] = key.charAt(P10[i] - 1);
        }

        int Ls[] = new int[5];
        int Rs[] = new int[5];
        int[] key1 = new int[8];
        int[] key2 = new int[8];

        for(int i = 0; i < 5; i++){
            Ls[i] = permutationKeyArray[i];
            Rs[i] = permutationKeyArray[i + 5];
        }

        int[] Ls_1 = shift(Ls, 1);
        int[] Rs_1 = shift(Rs, 1);

        for(int i = 0; i < 5; i++){
            permutationKeyArray[i] = Ls_1[i];
            permutationKeyArray[i + 5] = Rs_1[i];
        }

        for(int i = 0; i < 8; i++){
            key1[i] = permutationKeyArray[P8[i] - 1];
        }

        int[] Ls_2 = shift(Ls, 2);
        int[] Rs_2 = shift(Rs, 2);

        for(int i = 0; i < 5; i++){
            permutationKeyArray[i] = Ls_2[i];
            permutationKeyArray[i + 5] = Rs_2[i];
        }

        for(int i = 0; i < 8; i++){
            key2[i] = permutationKeyArray[P8[i] - 1];
        }

        return new int[][]{key1, key2};
    }

    private int[] shift(int[] ar, int n){
        while(n > 0){
            int temp = ar[0];
            for(int i = 0; i < ar.length - 1; i++){
                ar[i] = ar[i + 1];
            }
            ar[ar.length - 1] = temp;
            n--;
        }
        return ar;
    }

    public int[] encryptMessage(String message){
        int[] messageArray = new int[message.length()];
        for(int i = 0; i < messageArray.length; i++){
            messageArray[i] = message.charAt(i);
        }
        int[] arr = new int[8];

        int[] IP_ = permutation(8);
        for(int i = 0; i < 8; i++){
            this.IP[i] = IP_[i];
        }
        int[] IP_Inv_ = inversePermutation(this.IP, this.IP.length);
        for(int i = 0; i < 8; i++){
            this.IP_inv[i] = IP_Inv_[i];
        }

        for(int i = 0; i < 8; i++){
            arr[i] = messageArray[this.IP[i] - 1];
        }
        int[] arr1 = function_(arr, key1);

        int[] after_swap = swap(arr1, arr1.length / 2);

        int[] arr2 = function_(after_swap, key2);

        int[] ciphertext = new int[8];

        for(int i = 0; i < 8; i++){
            ciphertext[i] = arr2[IP_inv[i] - 1];
        }

        return ciphertext;
    }

    String binary_(int val){
        if (val == 0)
            return "00";
        else if (val == 1)
            return "01";
        else if (val == 2)
            return "10";
        else
            return "11";
    }

    int[] function_(int[] ar, int[] key_){
        int[] l = new int[4];
        int[] r = new int[4];

        for(int i = 0; i < 4; i++){
            l[i] = ar[i];
            r[i] = ar[i + 4];
        }

        int[] ep = new int[8];
        int[] leftSEP = permutation(4);
        int[] rightSEP = permutation(4);
        for(int i = 0; i < 4; i++){
            this.EP[i] = leftSEP[i];
        }
        for(int i = 4; i < 8; i++){
            this.EP[i] = rightSEP[i-4];
        }

        for(int i = 0; i < 8; i++){
            ep[i] = r[this.EP[i] - 1];
        }

        for(int i = 0; i < 8; i++){
            ar[i] = key_[i] ^ ep[i];
        }

        int[] l_1 = new int[4];
        int[] r_1 = new int[4];

        for(int i = 0; i < 4; i++){
            l_1[i] = ar[i];
            r_1[i] = ar[i + 4];
        }

        int row, col, val;

        row = Integer.parseInt("" + l_1[0] + l_1[3], 2);
        col = Integer.parseInt("" + l_1[1] + l_1[2], 2);
        val = S0[row][col];
        String str_l = binary_(val);

        row = Integer.parseInt("" + r_1[0] + r_1[3], 2);
        col = Integer.parseInt("" + r_1[1] + r_1[2], 2);
        val = S1[row][col];
        String str_r = binary_(val);

        int[] r_ = new int[4];
        for(int i = 0; i < 2; i++){
            char c1 = str_l.charAt(i);
            char c2 = str_r.charAt(i);
            r_[i] = Character.getNumericValue(c1);
            r_[i + 2] = Character.getNumericValue(c2);
        }
        int[] r_p4 = new int[4];
        int[] p4 = permutation(4);
        for(int i = 0; i < 4; i++){
            r_p4[i] = r_[p4[i] - 1];
        }

        for(int i = 0; i < 4; i++){
            l[i] = l[i] ^ r_p4[i];
        }

        int[] output = new int[8];
        for(int i = 0; i < 4; i++){
            output[i] = l[i];
            output[i + 4] = r[i];
        }
        return output;
    }

    int[] swap(int[] array, int n){
        int[] l = new int[n];
        int[] r = new int[n];

        for(int i = 0; i < n; i++){
            l[i] = array[i];
            r[i] = array[i + n];
        }

        int[] output = new int[2 * n];
        for(int i = 0; i < n; i++){
            output[i] = r[i];
            output[i + n] = l[i];
        }

        return output;
    }

    public int[] decryptMessage(String message){
        int[] messageArray = new int[message.length()];
        for(int i = 0; i < messageArray.length; i++){
            messageArray[i] = message.charAt(i);
        }
        int[] arr = new int[8];

        for(int i = 0; i < 8; i++){
            arr[i] = messageArray[this.IP[i] - 1];
        }

        int[] arr1 = function_(arr, key2);

        int[] after_swap = swap(arr1, arr1.length / 2);

        int[] arr2 = function_(after_swap, key1);

        int[] decrypted = new int[8];

        for(int i = 0; i < 8; i++){
            decrypted[i] = arr2[IP_inv[i] - 1];
        }

        return decrypted;
    }
}
