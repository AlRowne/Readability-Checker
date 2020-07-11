package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static String fileToString(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
    public static int sentenceCount(String fileString) {
        return fileString.replaceAll("\\s", "").split("[.!?]").length;
    }

    public static int wordCount(String fileString) {
        return fileString.split("\\s").length;
    }

    public static int charCount(String fileString) {
        return fileString.replaceAll("\\s", "").length();
    }
    
    public static int syllableCount(String fileString) {
        String[] wordsLowerCase = fileString.toLowerCase().split("\\s");
        String vowel = "[aeiouy]";
        int syllableCount = 0;
        boolean isPreviousVowel = false;
        int tempCount = 0;

        for (String word : wordsLowerCase) {
            for (String letter : word.split("")) {
                if (letter.matches(vowel) && !isPreviousVowel) {
                    tempCount++;
                    isPreviousVowel = true;
                }
                if (!letter.matches(vowel)) isPreviousVowel = false;
            }
            if (word.charAt(word.length() - 1) == 'e') tempCount--;
            syllableCount += Math.max(tempCount, 1);
            tempCount = 0;
        }

        return syllableCount;

    }
    public static int polySyllables(String fileString) {
        String[] wordsLowerCase = fileString.toLowerCase().split("\\s");
        String vowel = "[aeiouy]";
        int polySyllableCount = 0;
        boolean isPreviousVowel = false;

        for (String word : wordsLowerCase) {
            int tempCount = 0;
            for (String letter : word.split("")) {
                if (letter.matches(vowel) && !isPreviousVowel) {
                    tempCount++;
                    isPreviousVowel = true;
                }
                if (!letter.matches(vowel)) isPreviousVowel = false;
            }
            if (word.charAt(word.length() - 1) == 'e') tempCount--;
            if (tempCount > 2) polySyllableCount++;
        }

        return polySyllableCount;

    }

    public static double ARIscore(int sentenceCount, int wordCount, int charCount) {
        return ((4.71 * ((double) charCount / wordCount)) + (0.5 * ((double) wordCount / sentenceCount)) - 21.43);
    }

    public static double FK(int sentenceCount, int wordCount, int syllableCount) {
        return 0.39 * wordCount / sentenceCount + 11.8 * syllableCount / wordCount - 15.59;
    }

    public static double SMOG(int sentenceCount, int polysyllables) {
        return 1.043 * Math.sqrt(polysyllables * 30.0 / sentenceCount) + 3.1291;
    }

    public static double CL(int sentenceCount, int wordCount, int charCount) {
        double avgCharPer100Words = charCount / (wordCount / 100.0);
        double avgSentPer100Words = sentenceCount / (wordCount / 100.0);

        return 0.0588 * avgCharPer100Words - 0.296 * avgSentPer100Words - 15.8;
    }
    
    public static int recommendedAge(double score) {
        int age = 0;
        switch ((int) Math.ceil(score)) {
            case 1:
                age = 6;
                break;
            case 2:
                age = 7;
                break;
            case 3:
                age = 9;
                break;
            case 4:
                age = 10;
                break;
            case 5:
                age = 11;
                break;
            case 6:
                age = 12;
                break;
            case 7:
                age = 13;
                break;
            case 8:
                age = 14;
                break;
            case 9:
                age = 15;
                break;
            case 10:
                age = 16;
                break;
            case 11:
                age = 17;
                break;
            case 12:
                age = 18;
                break;
            default:
                age = 24;
                break;
        }
        return age;

    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        String file = fileToString(args[0]);
        int sentenceCount = sentenceCount(file);
        int wordCount = wordCount(file);
        int charCount = charCount(file);
        int syllableCount = syllableCount(file);
        int polySyllableCount = polySyllables(file);

        System.out.println("The text is:\n" + file + "\n");
        System.out.println("Words: " + wordCount);
        System.out.println("Sentences: " + sentenceCount);
        System.out.println("Characters: " + charCount);
        System.out.println("Syllables: " + syllableCount);
        System.out.println("Polysyllables: " + polySyllableCount);
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String chooseScore = sc.next().toLowerCase();

        switch (chooseScore) {
            case "ari":
                double score = ARIscore(sentenceCount, wordCount, charCount);
                int age = recommendedAge(score);
                System.out.printf("Automated Readability Index: %f (about %d year olds).%n", score, age);
                break;
            case "fk":
                score = FK(sentenceCount, wordCount, syllableCount);
                age = recommendedAge(score);
                System.out.printf("Flesch–Kincaid readability tests: %f (about %d year olds).%n", score, age);
                break;
            case "smog":
                score = SMOG(sentenceCount, polySyllableCount);
                age = recommendedAge(score);
                System.out.printf("Simple Measure of Gobbledygook: %f (about %d year olds).%n", score, age);
                break;
            case "cl":
                score = CL(sentenceCount, wordCount, charCount);
                age = recommendedAge(score);
                System.out.printf("Coleman–Liau index: %f (about %d year olds).%n", score, age);
                break;
            case "all":
                score = ARIscore(sentenceCount, wordCount, charCount);
                age = recommendedAge(score);
                System.out.printf("Automated Readability Index: %f (about %d year olds).%n", score, age);
                score = FK(sentenceCount, wordCount, syllableCount);
                age = recommendedAge(score);
                System.out.printf("Flesch–Kincaid readability tests: %f (about %d year olds).%n", score, age);
                score = SMOG(sentenceCount, polySyllableCount);
                age = recommendedAge(score);
                System.out.printf("Simple Measure of Gobbledygook: %f (about %d year olds).%n", score, age);
                score = CL(sentenceCount, wordCount, charCount);
                age = recommendedAge(score);
                System.out.printf("Coleman–Liau index: %f (about %d year olds).%n", score, age);
        }
    }


}