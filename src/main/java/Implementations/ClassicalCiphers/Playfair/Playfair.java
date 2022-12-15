package Implementations.ClassicalCiphers.Playfair;

import java.util.*;

public class Playfair {
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private String[][] alphabetArray;
    private String[] splitMessage;

    public Playfair(String permutationKey){
        this.alphabet = removeDuplicates(permutationKey) + this.alphabet;
        this.alphabet = removeDuplicates(this.alphabet).replaceAll("j", "");
        alphabetToArray(alphabet);
    }
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
    private String checkConditions(String message){
        StringBuilder messageBuilder = new StringBuilder(message);
        for(int i = 0; i < message.length(); i++){
            if(i < message.length()-1 && messageBuilder.charAt(i) == messageBuilder.charAt(i+1)){
                messageBuilder.insert(i+1, "x");
            }
        }
        if(messageBuilder.length() % 2 != 0){
            messageBuilder.append("z");
        }
        return messageBuilder.toString();
    }

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

    private int[] searchFor(String letter){
        for (int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(Objects.equals(this.alphabetArray[i][j], letter)){
                    return new int[] {i, j};
                }
            }
        }
        return null;
    }

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
}
