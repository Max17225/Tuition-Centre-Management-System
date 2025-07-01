/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import java.util.List;
import Util.DataManager;


// Service class for verify user credential.

public class AuthService {
    
    // Used for convert userType to their file name.
    // param(userType): The type of the login user, Must be admin/receptionist/tutor/student.
    // return(String) : Will return a file name in string format (e.g. "Admin.txt").
    public static String getUserFileType(String userType) {
        return switch (userType.toLowerCase()) {
            case "admin"       -> "Admin.txt";
            case "receptionist"-> "Receptionist.txt";
            case "tutor"       -> "Tutor.txt";
            case "student"     -> "Student.txt";
            default -> throw new IllegalArgumentException(
                "Invalid userType: '" + userType + "'. Expected: admin, receptionist, tutor, or student.");
        };
    }
    
    
    // To check the ID is correct or not.
    // param(inputID) : ID input by the user.
    // param(userType): The type of the login user, Must be admin/receptionist/tutor/student.
    // return(boolean): True, if found the ID input by user in the database.
    public static boolean foundID(String inputID, String userType) {
        // if getUserRecordByID is not null then return true
        return DataManager.getUserRecordByID(inputID, userType) != null;
    }
    
    
    // Check if the password input by the user can match with their password in the database.
    // param(userID)       : ID input by the user.
    // param(inputPassword): Password input by the user.
    // param(userType)     : The type of the login user, Must be admin/receptionist/tutor/student.
    // return(boolean)     : True, if password was correct.
    public static boolean passwordIsCorrect(String userID, String inputPassword, String userType) {
        List<String> userRecord = DataManager.getUserRecordByID(userID, userType);

        if (userRecord == null) return false;
        
        // return true, if matching
        return userRecord.get(2).trim().equals(inputPassword);
    }
}
