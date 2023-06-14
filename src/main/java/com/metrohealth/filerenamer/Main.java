package com.metrohealth.filerenamer;

import java.util.Scanner;

public class Main {

        public static void main(String[] args) {
            String sourcePath = "";
            String destinationPath = "";
            String csvPath = "";
            String fileExtension = "";

            //Using Scanner get the variables above
            System.out.println("Enter the source folder path where the files are: ");
            Scanner scanner = new Scanner(System.in);
            sourcePath = scanner.nextLine();
            System.out.println("Enter the destination path where the renamed copies will exist: ");
            scanner = new Scanner(System.in);
            destinationPath = scanner.nextLine();
            System.out.println("Enter the csv path where the desired names are: ");
             scanner = new Scanner(System.in);
            csvPath = scanner.nextLine();
            System.out.println("Enter the file extension e.g pdf: ");
             scanner = new Scanner(System.in);
            fileExtension = scanner.nextLine();

            CsvReader.titleList= CsvReader.readCSVFile(csvPath);
            CsvReader.sortedFiles = CsvReader.copyAndSortFiles(sourcePath, destinationPath);
            CsvReader.renameFiles(CsvReader.titleList, CsvReader.sortedFiles, fileExtension);
        }
}
