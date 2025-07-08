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
import Util.DataManager;


// UserInterface class for display Tutor UI.

public class TutorUI {
    
    public static void showTutorMenu(String userId) {

        Scanner scanner = new Scanner(System.in);
        Util.DataManager<DataModel.Tutor> tutorManager = DataManager.of(DataModel.Tutor.class);
        DataModel.Tutor target = tutorManager.getRecordById(userId);
        
        while (true) {
            System.out.println("[ Welcome back " + target.getUsername() + "(Tutor)" + " ]");
            System.out.println("1 - Add Class");
            System.out.println("2 - Update Subject Infomation");
            System.out.println("3 - Create Class Schedule");
            System.out.println("4 - View My Time Table");
            System.out.println("5 - View My Student");
            System.out.println("6 - Update My Profile");
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
                case"6" -> {}
                default -> {System.out.println("Invalid Input!!!");}
            }        
        }
    }
    
    public static void main(String[] args) {
        showTutorMenu("T0001");
    }
}