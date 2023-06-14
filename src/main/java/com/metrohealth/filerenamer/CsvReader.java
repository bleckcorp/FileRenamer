package com.metrohealth.filerenamer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    public static List<String> titleList = new ArrayList<>();
    public static List<File> sortedFiles = new ArrayList<>();

    static List<String> readCSVFile(String filePath) {
        List<String> titles = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming the title is in the first column (index 0)
                String[] data = line.split(",");
                if (data.length > 0) {
                    String title = data[0].trim();
                    titles.add(title);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return titles;
    }


    public static List<File> copyAndSortFiles(String folderPath, String destinationPath) {

        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                // Copy files to a temporary directory
                File tempDir = new File(destinationPath);
                if (!tempDir.exists()) {
                    tempDir.mkdirs();
                }

                for (File file : files) {
                    if (file.isFile()) {
                        try {
                            Path source = file.toPath();
                            Path destination = tempDir.toPath().resolve(file.getName());
                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                            sortedFiles.add(destination.toFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Sort the files based on their names
                sortedFiles.sort((o1, o2) -> {
                    String fileName1 = o1.getName();
                    String fileName2 = o2.getName();
                  return  compareByLexicography(fileName1, fileName2);
                });
            }
        }

        return sortedFiles;
    }
    public static void renameFiles(List<String> desiredTitles, List<File> files, String fileExtension) {
        if (desiredTitles.size() != files.size()) {
            System.out.println("Error: The titles list and files list have different sizes.");
            return;
        }

        for (int i = 0; i < desiredTitles.size(); i++) {
            File file = files.get(i);
            String desiredTitle = desiredTitles.get(i);


            // Construct the new file path
            File renamedFile = new File(file.getParent(), desiredTitle + "." + fileExtension);

            // Rename the file
            if (file.renameTo(renamedFile)) {
                System.out.println("Renamed file: " + file.getName() + " to: " + renamedFile.getName());
            } else {
                System.out.println("Failed to rename file: " + file.getName());
            }
        }
    }

    private static int compareByLexicography(String fileName1, String fileName2) {
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

    static private int compareName(String name1, String name2) {
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

    static private int compareNumeric(String num1, String num2) {
        int number1 = Integer.parseInt(num1);
        int number2 = Integer.parseInt(num2);
        return Integer.compare(number1, number2);
    }

    static private int extractNumber(String name) {
        int numberStartIndex = name.lastIndexOf(' ') + 1;
        String number = name.substring(numberStartIndex);
        return Integer.parseInt(number);
    }
}

