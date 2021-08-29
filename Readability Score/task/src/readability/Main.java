package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    final static Scanner scanner = new Scanner(System.in);
    final static String[] SCORE_CONVERSION = {"", "5-6", "6-7", "7-9", "9-10", "10-11", "11-12", "12-13", "13-14",
                                "14-15", "15-16", "16-17", "17-18", "18-24", "24+"};
    public static void main(String[] args) {
        try (Scanner fileScanner = new Scanner(new File(args[0]))) {
            String newLine;
            String text = "";
            while (fileScanner.hasNext()) {
                newLine = fileScanner.nextLine();
                text = text.concat(newLine).concat(" ");
            }
            text = text.trim();
            int numberOfSentences = text.split("[.!?]").length;
            int numberOfWords = text.split("\\s+").length;
            int numberOfCharacters = text.replace(" ", "").replace("\t", "").length();

            System.out.printf("The text is:\n%s\n\n", text);

            double score = 4.71 * ((double) numberOfCharacters / (double) numberOfWords)
                    + 0.5 * ((double) numberOfWords / (double) numberOfSentences) - 21.43;

            int scoreToInt = (int) Math.ceil(score);
            if (scoreToInt > 14) {
                scoreToInt = 14;
            }
            String result = String.format("This text should be understood by %s-year-olds.",
                    SCORE_CONVERSION[scoreToInt]);

            System.out.printf("Words: %d\nSentences: %d\nCharacters: %d\nThe score is: %.2f\n%s",
                    numberOfWords, numberOfSentences, numberOfCharacters, score, result);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
