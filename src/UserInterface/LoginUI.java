/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface;

import java.util.Scanner;
import Service.AuthService;


// UserInterface class for display Login UI.
// We will make it to GUI later.

public class LoginUI {
    
    // Used to get the type of the user by the first charecter of their ID input.
    // param(inputID): ID input by the user.
    private static String getUserType(String inputID) {
        if (inputID.isEmpty()) return null;
        
        return switch ((inputID.charAt(0))) {
            case 'A' -> "Admin";
            case 'R' -> "Receptionist";
            case 'T' -> "Tutor";
            case 'S' -> "Student";
            default  -> null;
        };
    }
    
    // Used to display login menu
    public static void showLoginMenu() {
        Scanner scanner = new Scanner(System.in);
        String inputID, userType;
        final int MAX_ATTEMPTS = 3;
        
        // ---------- ID Verification ----------
        int idAttempts = 0;
        
        while (true) {
            System.out.println("[ Welcome to ATC ]");
            System.out.print("User ID: ");
            inputID = scanner.nextLine().trim();

            userType = getUserType(inputID);

            if (userType == null) {
                System.out.println("Error: Invalid ID format. Must start with A/R/T/S.");            
            } else if (!AuthService.foundID(inputID, userType)) {
                System.out.println("Error: ID not found in records.");          
            } else {
                break; // Valid ID and user type founded
            }

            idAttempts++;
            if (idAttempts >= MAX_ATTEMPTS) {
                System.out.println("Error: Too many invalid attempts.");
                // Maybe we need to add something in here at the future
                return;
            }
        }

        // ---------- Password Verification ----------
        int passwordAttempts = 0;

        while (true) {
            System.out.print("Password: ");
            String inputPassword = scanner.nextLine().trim();

            if (AuthService.passwordIsCorrect(inputID, inputPassword, userType)) {
                // Future update here
                switch(userType) {
                    case"Admin"        -> {}
                    case"Receptionist" -> {}
                    case"Tutor"        -> {}
                    case"Student"      -> {}
                }
                
                // this SHIT for TRY only must REMEMBER to DELETE!!
                System.out.println("Login successful!!! Delete me after u update");
                
            } else {
                System.out.println("Error: Incorrect password.");
                passwordAttempts++;
                if (passwordAttempts >= MAX_ATTEMPTS) {
                    System.out.println("Error: Too many failed attempts. Access denied.");
                    // maybe we need to add something in here 
                    return;
                }
            }
        }
    }
}
