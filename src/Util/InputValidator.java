/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

// Utility class for validating user input in correct format.

public class InputValidator {
    
    // Verify for correct email format.
    // param(userEmail): Email input from the user
    // return(boolean) : If correct format return true.
    public static boolean emailFormatIsValid(String email) {
        if (email == null) return false;
        email = email.trim().toLowerCase();

        // Check general email format using regex
        boolean formatOk = email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

        // Check domain is allowed
        boolean domainOk = email.endsWith("@gmail.com") || email.endsWith("@mail.com");

        return formatOk && domainOk;
    }
    
    // Verify for correct time input format.
    // param(timeStr) : Time input in string format
    // return(boolean): True if user input a correct format like 1300-1400
    public static boolean isValidTime(String timeStr) {
        if (!timeStr.matches("\\d{4}-\\d{4}")) return false;

        try {
            
            int start = Integer.parseInt(timeStr.substring(0, 4));
            int end = Integer.parseInt(timeStr.substring(5, 9));

            int startHour = start / 100;
            int startMin = start % 100;
            int endHour = end / 100;
            int endMin = end % 100;

            // Check the time format
            if (startHour < 0 || startHour > 23 || startMin < 0 || startMin > 59) return false;
            if (endHour < 0 || endHour > 23 || endMin < 0 || endMin > 59) return false;

            // Make sure starttime always lower then end time
            int startTotalMin = startHour * 60 + startMin;
            int endTotalMin = endHour * 60 + endMin;

            return startTotalMin < endTotalMin;
            
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Verify for correct Level format input
    // param(leve) : String input of level
    // return(bool): if is valid return true
    public static boolean isValidLevel(String level) {
        return level != null && level.matches("L[1-5]");
    }
}
