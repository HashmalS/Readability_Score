package readability;

import java.util.Scanner;

public class Main {
    final static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        String inputString = scanner.nextLine();
        System.out.print(inputString.length() > 100? "HARD" : "EASY");
    }
}
