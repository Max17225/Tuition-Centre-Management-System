/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import java.util.List;
import Util.FileManager;


// Service class for verify user credential.

public class AuthService {
    
    // Used for convert userType to their file name.
    // param(userType): The type of the login user, Must be admin/receptionist/tutor/student.
    // return(String) : Will return a file name in string format (e.g. "Admin.txt").
    private static String getUserFileType(String userType) {
        return switch (userType.toLowerCase()) {
            case "admin"       -> "Admin.txt";
            case "receptionist"-> "Receptionist.txt";
            case "tutor"       -> "Tutor.txt";
            case "student"     -> "Student.txt";
            default -> throw new IllegalArgumentException(
                "Invalid userType: '" + userType + "'. Expected: admin, receptionist, tutor, or student.");
        };
    }
    
    // Used for getting the info(1 row) by the ID input by the user.
    // param(userID)  : The ID input by the user (e.g. "A0001").
    // param(userType): The type of the login user, Must be admin/receptionist/tutor/student.
    // return(List)   : The list which contain info of the user (e.g. ["A0001", "userName", ...]).
    private static List<String> getUserRecordByID(String userID, String userType) {
        List<List<String>> allUsers = FileManager.getDataList(getUserFileType(userType));
        
        for (List<String> user : allUsers) {
            // Check if user data is not empty and ID is correct
            if (!user.isEmpty() && user.get(0).trim().equals(userID)) {
                return user;
            }
        }
        return null;
    }
    
    // To check the ID is correct or not.
    // param(inputID) : ID input by the user.
    // param(userType): The type of the login user, Must be admin/receptionist/tutor/student.
    // return(boolean): True, if found the ID input by user in the database.
    public static boolean foundID(String inputID, String userType) {
        // if getUserRecordByID is not null then return true
        return getUserRecordByID(inputID, userType) != null;
    }
    
    // Check if the password input by the user can match with their password in the database.
    // param(userID)       : ID input by the user.
    // param(inputPassword): Password input by the user.
    // param(userType)     : The type of the login user, Must be admin/receptionist/tutor/student.
    // return(boolean)     : True, if password was correct.
    public static boolean passwordIsCorrect(String userID, String inputPassword, String userType) {
        List<String> userRecord = getUserRecordByID(userID, userType);

        if (userRecord == null) return false;
        
        // return true, if matching
        return userRecord.get(2).trim().equals(inputPassword);
    }
}
