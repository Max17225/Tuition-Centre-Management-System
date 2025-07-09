/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Util.DataManager;


// Service class for verify user credential.

public class AuthService {
    
    // To check the id input by the user is inside database or not.
    // param(inputId) : The Id input by the user.
    // return(boolean): If founded Id, then true.
    public static boolean foundId(String inputId) {
    char userRole = inputId.charAt(0);

    switch (userRole) {
        case 'A' -> {
            var admin = DataManager.of(DataModel.Admin.class).getRecordById(inputId);
            return admin != null;
        }
        case 'T' -> {
            var tutor = DataManager.of(DataModel.Tutor.class).getRecordById(inputId);
            return tutor != null;
        }
        case 'R' -> {
            var recep = DataManager.of(DataModel.Receptionist.class).getRecordById(inputId);
            return recep != null;
        }
        case 'S' -> {
            var student = DataManager.of(DataModel.Student.class).getRecordById(inputId);
            return student != null;
        }
        default -> {
            return false;
        }
    }
}
    
    
    // Check if the password input by the user can match with their password in the database.
    // param(userID)       : ID input by the user.
    // param(inputPassword): Password input by the user.
    // return(boolean)     : True, if password was correct.
    public static boolean passwordIsCorrect(String inputId, String inputPassword) {
        char userRole = inputId.charAt(0);

        switch (userRole) {
            case 'A' -> {
                var targetAdmin = DataManager.of(DataModel.Admin.class).getRecordById(inputId);
                return (targetAdmin.getPassword().equals(inputPassword));
            }
            case 'T' -> {
                var targetTutor = DataManager.of(DataModel.Tutor.class).getRecordById(inputId);
                return (targetTutor.getPassword().equals(inputPassword));
            }
            case 'R' -> {
                var targetRecep = DataManager.of(DataModel.Receptionist.class).getRecordById(inputId);
                return (targetRecep.getPassword().equals(inputPassword));
            }
            case 'S' -> {
                var targetStudent = DataManager.of(DataModel.Student.class).getRecordById(inputId);
                return (targetStudent.getPassword().equals(inputPassword));
            }
            default -> {
                return false;
            }
        }
    }
    
}
