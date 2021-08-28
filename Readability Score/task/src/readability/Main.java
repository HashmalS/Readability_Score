package readability;

import java.util.Scanner;

public class Main {
    final static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        String inputString = scanner.nextLine();
        String[] sentences = inputString.split("[.!?]");

        int numberOfWords = 0;
        int numberOfSentences = 0;
        for (String sentence :
                sentences) {
            numberOfSentences++;
            numberOfWords += sentence.split("\\s+").length;
        }

        boolean isHardToRead = (double) (numberOfWords / numberOfSentences) > 10;
        System.out.println(isHardToRead ? "HARD" : "EASY");
    }
}
