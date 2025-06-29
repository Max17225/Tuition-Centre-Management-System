/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

// Utility class for validating user input in correct format.

public class InputValidator {
    
    // Verify for correct email format.
    // Param(userEmail): Email input from the user
    // return(boolean) : If correct format return true.
    public static boolean emailFormatIsValid(String userEmail) {
    if (userEmail == null) return false;

    userEmail = userEmail.trim().toLowerCase();
    return userEmail.endsWith("@gmail.com") || userEmail.endsWith("@mail.com");
    }
    
    
    
}
