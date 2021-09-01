package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Main {
    final static Scanner scanner = new Scanner(System.in);
    final static int[] GRADE_LEVELS = {0, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 24};

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

            List<String> wordList = new LinkedList<>(Arrays.asList(text.split("[\\s.?!-]")));
            wordList.removeAll(Collections.singleton(""));
            for (String word :
                    wordList) {
                word = word.replaceAll(",", "");
            }

            int[] syllables = new int[wordList.size()];
            for (int i = 0; i < syllables.length; i++) {
                syllables[i] = countSyllables(wordList.get(i));
            }

            int numberOfSyllables = IntStream.of(syllables).sum();
            int numberOfPolysyllables = 0;
            for (int syllable : syllables) {
                if (syllable > 2) {
                    numberOfPolysyllables++;
                }
            }

            System.out.printf("The text is:\n%s\n\n", text);

            System.out.printf("Words: %d\nSentences: %d\nCharacters: %d\nSyllables: %d\nPolysyllables: %d\n",
                    numberOfWords, numberOfSentences, numberOfCharacters, numberOfSyllables, numberOfPolysyllables);
            System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            String command = scanner.nextLine();
            System.out.println();
            switch (command) {
                case "ARI":
                    double ari = calculateARI(numberOfSentences, numberOfWords, numberOfCharacters);
                    System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n",
                            ari, GRADE_LEVELS[Math.toIntExact(Math.round(ari))]);
                    break;
                case "FK":
                    double fk = calculateFK(numberOfSentences, numberOfWords, numberOfCharacters);
                    System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n",
                            fk, GRADE_LEVELS[Math.toIntExact(Math.round(fk))]);
                    break;
                case "SMOG":
                    double smog = calculateSMOG(numberOfSentences, numberOfPolysyllables);
                    System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n",
                            smog, GRADE_LEVELS[Math.toIntExact(Math.round(smog))]);
                    break;
                case "CL":
                    double cl = calculateCL(numberOfSentences, numberOfWords, numberOfCharacters);
                    System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n",
                            cl, GRADE_LEVELS[Math.toIntExact(Math.round(cl))]);
                    break;
                case "all":
                    calculateAverage(numberOfSentences, numberOfWords,
                            numberOfPolysyllables, numberOfSyllables, numberOfCharacters);
                    break;
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int countSyllables(String s) {
        final Pattern p = Pattern.compile("([ayeiou]+)");
        s = s.toLowerCase();
        final Matcher m = p.matcher(s);
        int count = 0;
        while (m.find()) {
            count++;
        }
        if (s.endsWith("e")) {
            count--;
        }

        return count <= 0 ? 1 : count;
    }

    public static double calculateARI(int sentences, int words, int characters) {
        double index = 4.71 * ((double) characters / (double) words)
                + 0.5 * ((double) words / (double) sentences) - 21.43;
        int scoreToInt = (int) Math.round(index);
        if (scoreToInt > 13) {
            scoreToInt = 13;
        }

        System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n",
                index, GRADE_LEVELS[scoreToInt]);

        return GRADE_LEVELS[scoreToInt];
    }

    public static double calculateFK(int sentences, int words, int syllables) {
        double index = 0.39 * ((double) words) / ((double) sentences)
                + 11.8 * ((double) syllables) / ((double) words) - 15.59;
        int scoreToInt = (int) Math.round(index);
        if (scoreToInt > 13) {
            scoreToInt = 13;
        }

        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).\n",
                        index, GRADE_LEVELS[scoreToInt]);

        return GRADE_LEVELS[scoreToInt];
    }

    public static double calculateSMOG(int sentences, int polysyllables) {
        double index = 1.043 * Math.sqrt(polysyllables * 30 / (double) sentences) + 3.1291;
        int scoreToInt = (int) Math.round(index);
        if (scoreToInt > 13) {
            scoreToInt = 13;
        }

        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).\n",
                index, GRADE_LEVELS[scoreToInt]);

        return GRADE_LEVELS[scoreToInt];
    }

    public static double calculateCL(int sentences, int words, int characters) {
        double l = (double) characters / (double) words * 100;
        double s = (double) sentences / (double) words * 100;
        double index = 0.0588 * l - 0.296 * s - 15.8;
        int scoreToInt = (int) Math.round(index);
        if (scoreToInt > 13) {
            scoreToInt = 13;
        }

        System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).\n",
                index, GRADE_LEVELS[scoreToInt]);

        return GRADE_LEVELS[scoreToInt];
    }

    public static void calculateAverage(int sentences, int words, int polysyllables, int syllables, int characters) {
        double ari = calculateARI(sentences, words, characters);
        double fk = calculateFK(sentences, words, syllables);
        double smog = calculateSMOG(sentences, polysyllables);
        double cl = calculateCL(sentences, words, characters);

        double average = (ari + fk + smog + cl) / 4.0;
        System.out.printf("This text should be understood in average by %.2f-year-olds.", average);
    }
}
