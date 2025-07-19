/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import java.util.List;
import java.util.Map;

// Utility class for generate new unique id for new record data.

public class IdGenerator {
    
    // A map which store all the DataModel with the Id prefix.
    private static final Map<Class<?>, String> PREFIX_MAP = Map.of(
        DataModel.Admin.class, "A",
        DataModel.Tutor.class, "T",
        DataModel.Student.class, "S",
        DataModel.Receptionist.class, "R",
        DataModel.Subject.class, "SUB",
        DataModel.ClassSchedule.class, "SCH",
        DataModel.Enrollment.class, "ENR",
        DataModel.Payment.class, "PYM",
        DataModel.StudentRequest.class, "SR"
    );
    
    
    // Private method use to support getNewId().
    // this method will find out the latest Id of in the database.
    // param(clazz): The class of the DataModel that u want to find.(e.g. u want to find tutor record, then write Tutor.class)
    private static <T extends DataModel.DataSerializable> int getLatestId(Class<T> clazz) {
        int latestId = 0;
        Util.DataManager<T> manager = DataManager.of(clazz);
        List<T> allRecord = manager.readFromFile();

        if (allRecord == null || allRecord.isEmpty()) {
            return 0; // If database was empty, then return 0
        }

        for (T record : allRecord) {
            String rawId = record.getId();
            String numericPart = rawId.replaceAll("\\D+", ""); // Remove non-digits

            if (!numericPart.isEmpty()) { // make sure only digit
                try {
                    int idNumber = Integer.parseInt(numericPart);
                    if (idNumber > latestId) {
                        latestId = idNumber;
                    }
                } catch (NumberFormatException ignored) { // if there is some data was invalid, then it will ignore, wont crash the whole program.
                }
            }
        }

        return latestId;
    }

    
    // This method will generate a new unique id for chosen class
    // param(clazz)  : The class of the DataModel that u want to find.(e.g. u want to find tutor record, then write Tutor.class)
    // return(String): A new Id.
    public static <T extends DataModel.DataSerializable> String getNewId(Class<T> clazz) {
        String prefix = PREFIX_MAP.get(clazz); // get the prefix with corresponding DataModel(class)
        int newIdNumber = getLatestId(clazz) + 1;
        return prefix + String.format("%04d", newIdNumber);
    }
}
