package com.carservice.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file operations
 */
public class FileHandler {

    /**
     * Checks if a file exists
     */
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * Ensures a file exists, creating it if necessary
     */
    public static void ensureFileExists(String filePath) throws IOException {
        File file = new File(filePath);

        // Create parent directories if needed
        if (!file.getParentFile().exists()) {
            boolean created = file.getParentFile().mkdirs();
            System.out.println("Created directory: " + file.getParentFile().getAbsolutePath() + " - Success: " + created);
        }

        // Create the file if it doesn't exist
        if (!file.exists()) {
            boolean created = file.createNewFile();
            System.out.println("Created file: " + filePath + " - Success: " + created);
        }
    }

    /**
     * Generic method to write content to a file
     */
    public static void writeToFile(String filePath, String content, boolean append) throws IOException {
        // Ensure the file exists
        ensureFileExists(filePath);

        // Write to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            writer.write(content);
        }
    }

    /**
     * Append a line to a file
     */
    public static void appendLine(String filePath, String line) throws IOException {
        writeToFile(filePath, line + System.lineSeparator(), true);
    }

    /**
     * Generic method to read lines from a file
     */
    public static List<String> readLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();

        if (!fileExists(filePath)) {
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }

    /**
     * Method to create a directory if it doesn't exist
     */
    public static boolean createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return true;
    }

    /**
     * Method to delete a file
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.delete();
    }

    /**
     * Method to read all content from a file as a single string
     */
    public static String readFileAsString(String filePath) throws IOException {
        if (!fileExists(filePath)) {
            return "";
        }

        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    /**
     * Method to backup a file before making changes
     */
    public static boolean backupFile(String filePath) {
        if (!fileExists(filePath)) {
            return false;
        }

        String backupFilePath = filePath + ".bak";
        try {
            // If a backup already exists, delete it
            deleteFile(backupFilePath);

            // Copy the original file to backup
            Files.copy(Paths.get(filePath), Paths.get(backupFilePath));
            return true;
        } catch (IOException e) {
            System.err.println("Error backing up file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to restore a file from backup
     */
    public static boolean restoreFromBackup(String filePath) {
        String backupFilePath = filePath + ".bak";

        if (!fileExists(backupFilePath)) {
            return false;
        }

        try {
            // Delete the original file if it exists
            deleteFile(filePath);

            // Copy the backup to the original file
            Files.copy(Paths.get(backupFilePath), Paths.get(filePath));
            return true;
        } catch (IOException e) {
            System.err.println("Error restoring from backup: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to check if a file is empty
     */
    public static boolean isFileEmpty(String filePath) throws IOException {
        if (!fileExists(filePath)) {
            return true;
        }

        return Files.size(Paths.get(filePath)) == 0;
    }

    /**
     * Method to count the number of lines in a file
     */
    public static int countLines(String filePath) throws IOException {
        if (!fileExists(filePath)) {
            return 0;
        }

        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        }

        return lineCount;
    }
}