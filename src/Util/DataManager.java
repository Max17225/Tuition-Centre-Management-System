/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;


import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

// Utility class for access or manage data files used in the system.
// Expected file format     : comma-separated values (CSV).
// Example of use this class: DataManager<Tutor> tutorManager = DataManager.of(Tutor.class);
// Located in the /Data/ directory relative to the working directory.

public class DataManager<T extends DataModel.DataSerializable> { // extends DataSerializable, because this DataManager only can use the class in DataModel
    // A variable(rowToObject), store function.
    private final Function<List<String>, T> rowToObject; 
    // fileName will the manager use.
    private final String fileName;
    // A Variable to Store each Function with corresponding Class.
    private static final Map<Class<?>, Function<List<String>, ?>> converterMap = new HashMap<>();
    // A Variable to Store eatch fileName with corresponding Class.
    private static final Map<Class<?>, String> fileNameMap = new HashMap<>();
    
    // Register Function here
    static {
        //---------------------------TUTOR----------------------------------------
        converterMap.put(DataModel.Tutor.class, row -> 
            new DataModel.Tutor(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5))
        ); 
        fileNameMap.put(DataModel.Tutor.class, "Tutor.txt");
        
        //---------------------------ADMIN----------------------------------------
        converterMap.put(DataModel.Admin.class, row ->
            new DataModel.Admin(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5))
        );
        fileNameMap.put(DataModel.Admin.class, "Admin.txt");
        
        //---------------------------RECEPTIONIST----------------------------------------
        converterMap.put(DataModel.Receptionist.class, row ->
            new DataModel.Receptionist(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5))
        );
        fileNameMap.put(DataModel.Receptionist.class, "Receptionist.txt");
        
        //---------------------------STUDENT----------------------------------------
        converterMap.put (DataModel.Student.class, row ->
            new DataModel.Student(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5), row.get(6), row.get(7))
        );
        fileNameMap.put(DataModel.Student.class, "Student.txt");
        
        //---------------------------STUDENT REQUEST----------------------------------------
        converterMap.put(DataModel.StudentRequest.class, row ->
            new DataModel.StudentRequest(row.get(0), row.get(1), row.get(2), row.get(3)) 
        );
        fileNameMap.put(DataModel.StudentRequest.class, "StudentRequest.txt"); 
        
        //---------------------------SUBJECT----------------------------------------
        converterMap.put(DataModel.Subject.class, row ->
            new DataModel.Subject(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4))
        );
        fileNameMap.put(DataModel.Subject.class, "Subject.txt");
        
        //---------------------------CLASS SCHEDULE----------------------------------------
        converterMap.put(DataModel.ClassSchedule.class, row ->
            new DataModel.ClassSchedule(row.get(0), row.get(1), row.get(2))
        );
        fileNameMap.put (DataModel.ClassSchedule.class, "ClassSchedule.txt");
        
        //---------------------------Enrollment----------------------------------------------
        converterMap.put(DataModel.Enrollment.class, row -> 
            new DataModel.Enrollment(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5))
        );
        fileNameMap.put (DataModel.Enrollment.class, "Enrollment.txt");
        
        // Add more DataModel here
    }                                                                   

    // Constructor of FileManager
    // param(rowToObject): The function that can convert a row data(list of string) to Object.
    // param(fileName)   : The name of the file that manager will use.
    public DataManager(Function<List<String>, T> rowToObject, String fileName) { 
        this.rowToObject = rowToObject;
        this.fileName = fileName;
    }
    
    // ------------------------Static Method to auto generate constructor easily------------------------------
    
    // This will try to get the method with the Class u want
    // param(clazz)    : Write down the class u want(e.g. Tutor.class)
    // return(Function): Return a function that will convert a list of string to a class
    public static <T extends DataModel.DataSerializable> DataManager<T> of(Class<T> clazz) {
        Function<List<String>, ?> converter = converterMap.get(clazz);
        String fileName = fileNameMap.get(clazz);

        if (converter == null || fileName == null) {
            throw new IllegalArgumentException("No converter or file registered for " + clazz.getSimpleName());
        }

        return new DataManager<>((Function<List<String>, T>) converter, fileName);
    }
    
    
    // -------------------------------------Function of Manager---------------------------------------------
    
    // To encapsulate all the raw data into a Object, and store these object in a List.
    // return (List)   : Return a list of Object 
    public List<T> readFromFile() {
        List<T> list = new ArrayList<>();
        Path path = Paths.get("Data", fileName);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // skip the empty row
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                List<String> row = Arrays.stream(parts).map(String::trim).toList();

                // if there is some data is malformed it will skip and warning
                if (row.size() < 3) {
                    System.err.println("Warning: Skipping malformed line: [" + line + "]");
                    continue;
                }

                T obj = rowToObject.apply(row);
                list.add(obj);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return list;
    }


    // Use to convert one record(raw data) to one Object
    // param (inputId) : The id u want of your target
    // return (Object)   : Return a Object 
    public T getRecordById(String inputId) {
        List<T> list = readFromFile();
        for (T item : list) {
            if (item.getId().equalsIgnoreCase(inputId)) {
                return item;
            }
        }
        return null;
    }

    // Overwrite everything in a file, normally used to modify data
    // param (list): A list of object(must specify what DataModel) will be write down to a file, one record one line
    public void overwriteFile(List<T> list) {
        Path path = Paths.get("Data", fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (T item : list) {
                writer.write(item.toDataLine());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    // appendOne string line to a choosen file
    // param (item): A new Object that u want to add into database.
    public void appendOne(T item) {
        Path path = Paths.get("Data", fileName);

        try {
            boolean fileExists = Files.exists(path);
            boolean isEmpty = fileExists && Files.size(path) == 0;

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (!isEmpty && fileExists) {
                    writer.newLine(); 
                }
                writer.write(item.toDataLine());
            }

        } catch (IOException e) {
            System.out.println("Error appending to file: " + e.getMessage());
        }
    }
    
    // Update a object info by own id in a class and update into text file
    // param(updatedItem) : The new version of object 
    public void updateRecord(T updatedItem) {
        List<T> list = readFromFile();
        boolean updated = false;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equalsIgnoreCase(updatedItem.getId())) {
                list.set(i, updatedItem);  
                updated = true;
                break;
            }
        }

        if (updated) {
            overwriteFile(list); 
        } else {
            System.err.println("Update failed: ID not found - " + updatedItem.getId());
        }
    }
} 
