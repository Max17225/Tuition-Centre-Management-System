/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface;

/**
 *
 * @author nengz
 */
import java.util.Scanner;
import java.util.List;
import Util.DataManager;

// UserInterface class for display Tutor UI.

public class TutorUI {
    
    public static void showTutorMenu(String userID, String userFileType) {
        Scanner scanner = new Scanner(System.in);
        List<String> userInfo = DataManager.getUserRecordByID(userID, userFileType);
        String userName = userInfo.get(1);
        
        
        while (true) {
            System.out.println("[ Welcome back " + userName + "(Tutor)" + " ]");
            System.out.println("1 - Add Class");
            System.out.println("2 - Update Class Infomation");
            System.out.println("3 - View My Classes");
            System.out.println("4 - View My Student");
            System.out.println("5 - Update My Profile");
            System.out.println("0 - Exit");
            System.out.print("=> ");
            
            String userInput = scanner.nextLine().trim();
            
            switch(userInput) {
                case"0" -> {System.out.println("Exiting System..."); return;}
                case"1" -> {}
                case"2" -> {}
                case"3" -> {}
                case"4" -> {}
                case"5" -> {}
                default -> {System.out.println("Invalid Input!!!");}
            }        
        }
    }
}