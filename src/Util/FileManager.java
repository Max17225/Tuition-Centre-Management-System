/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

/**
 *
 * @author nengz
 */
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileManager {

    public static List<List<String>> getDataList(String fileName) {
        List<List<String>> allDataList = new ArrayList<>();

        Path path = Paths.get("Data", fileName);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;

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
}
