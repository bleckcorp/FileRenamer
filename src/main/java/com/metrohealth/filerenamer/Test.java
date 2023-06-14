package com.metrohealth.filerenamer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String[] fileNames =  {"Mail Merge for Providders Paid In May 2023 Updated_1-1","Mail Merge for Providders Paid In May 2023 Updated_2-2","Mail Merge for Providders Paid In May 2023 Updated_10-10","Mail Merge for Providders Paid In May 2023 Updated_11-11","Mail Merge for Providders Paid In May 2023 Updated_3-3"};
        Arrays.sort(fileNames, new FileNameComparator());

        for (String fileName : fileNames) {
            System.out.println(fileName);
        }
    }
}

class FileNameComparator implements Comparator<String> {
    public int compare(String fileName1, String fileName2) {
        String[] parts1 = fileName1.split("\\.");
        String[] parts2 = fileName2.split("\\.");

        String name1 = parts1[0].replaceAll("[^a-zA-Z0-9]", "");
        String name2 = parts2[0].replaceAll("[^a-zA-Z0-9]", "");

        int result = compareName(name1, name2);
        if (result != 0) {
            return result;  // Compare based on name if different
        }

        // If names are the same, compare based on natural numeric order
        int number1 = extractNumber(parts1[0]);
        int number2 = extractNumber(parts2[0]);

        return Integer.compare(number1, number2);
    }

    private int compareName(String name1, String name2) {
        int minLength = Math.min(name1.length(), name2.length());
        for (int i = 0; i < minLength; i++) {
            char c1 = name1.charAt(i);
            char c2 = name2.charAt(i);
            if (c1 != c2) {
                if (Character.isDigit(c1) && Character.isDigit(c2)) {
                    // Compare numeric part if both characters are digits
                    return compareNumeric(name1.substring(i), name2.substring(i));
                } else {
                    // Compare based on lexicographic order otherwise
                    return Character.compare(c1, c2);
                }
            }
        }
        return Integer.compare(name1.length(), name2.length());
    }

    private int compareNumeric(String num1, String num2) {
        int number1 = Integer.parseInt(num1);
        int number2 = Integer.parseInt(num2);
        return Integer.compare(number1, number2);
    }

    private int extractNumber(String name) {
        int numberStartIndex = name.lastIndexOf(' ') + 1;
        String number = name.substring(numberStartIndex);
        return Integer.parseInt(number);
    }
}
