/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;


import java.io.*;
import java.nio.file.*;
import java.util.*;


// Utility class for access or manage data files used in the system.
// Expected file format: comma-separated values (CSV).
// Support reading CSV-like files into structured lists.
// Located in the /Data/ directory relative to the working directory.
 
public class FileManager {
    
    // Reads a CSV file from the Data directory and returns each line as a list of strings.
    // param(fileName): The name of the file to read (e.g. "students.txt").
    // return(List)   : A list of rows, where each row is a list of trimmed values (e.g. [[userID, userName, ...], [userID, ...]]).
    public static List<List<String>> getDataList(String fileName) {
        List<List<String>> allDataList = new ArrayList<>();
        
        // Get the file path like Data/fileName.
        Path path = Paths.get("Data", fileName);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            
            // Convert each line of String data to a List by split the "," and add the List to the allDataList.
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                List<String> row = new ArrayList<>();
                for (String val : values) {
                    row.add(val.trim());
                }
                allDataList.add(row);
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + path + " → " + e.getMessage());
        }

        return allDataList;
    }
    
    // Appends a single String line of text to the specified data file.
    // param(line)     : The new data(string) that u want to add.
    // param(fileName) : The name of the file to append (e.g., "students.txt").
    public static void appendData(String line, String fileName) {
        Path path = Paths.get("Data", fileName);
        
        // This will try to append new data in the file, if file does not exist it will create a new file.
        try {
            boolean fileExists = Files.exists(path);
            boolean isEmpty = fileExists && Files.size(path) == 0;

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (!isEmpty && fileExists) {
                    writer.newLine(); // Only add newline if file has content
                }
                writer.write(line);
            }

        } catch (IOException e) {
            System.out.println("Error appending to file: " + path + " → " + e.getMessage());
        }
    }
}
