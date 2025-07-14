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
import java.util.*;
import Util.*;
import DataModel.*;
import Service.TutorService;



// UserInterface class for display Tutor UI.

public class TutorUI {
    
    public static void showTutorMenu(String userId) {

        Scanner scanner = new Scanner(System.in);
        DataManager<Tutor> tutorManager = DataManager.of(Tutor.class);
        Tutor target = tutorManager.getRecordById(userId);
        
        while (true) {
            System.out.println("[ Welcome back " + target.getUsername() + "(Tutor)" + " ]");
            System.out.println("1 - Update Subject Fee");
            System.out.println("2 - Class Schedule Manage");
            System.out.println("3 - View My Time Table");
            System.out.println("4 - View My Student");
            System.out.println("5 - Update My Profile");
            System.out.println("0 - Exit");
            System.out.print("=> ");
            
            String userInput = scanner.nextLine().trim();
            
            switch(userInput) {
                case"0" -> {System.out.println("Exiting System..."); return;}
                case"1" -> {showFeeUpdateMenu(userId);}
                case"2" -> {showScheduleManageMenu(userId);}
                case"3" -> {}
                case"4" -> {}
                case"5" -> {}
                default -> {System.out.println("Invalid Input!!!");}
            }        
        }
    }
    
    private static void showFeeUpdateMenu(String userId) {
        Scanner scanner = new Scanner(System.in);

        List<Subject> userSubjects = TutorService.getMySubject(userId);

        if (userSubjects.isEmpty()) {
            System.out.println("You are not assigned to any subjects.");
            return;
        }

        while (true) {
            System.out.println("\n[ Choose a Subject to Update Fee ]");
            
            for (int i = 0; i < userSubjects.size(); i++) {
                Subject sub = userSubjects.get(i);
                System.out.printf("%d - %s (Level %s) - Fee: %s\n", i + 1, sub.getSubjectName(), sub.getLevel(), sub.getFeePerMonth());
            }

            System.out.println("0 - Exit");
            System.out.print("=> ");
            String userInput = scanner.nextLine().trim();

            try {
                int userChoice = Integer.parseInt(userInput);

                if (userChoice == 0) {
                    System.out.println("Exiting Fee Update Menu...");
                    break;
                }

                if (userChoice >= 1 && userChoice <= userSubjects.size()) {
                    Subject chosenSubject = userSubjects.get(userChoice - 1);

                    System.out.print("Enter new fee for " + chosenSubject.getSubjectName() + ": ");
                    String newFee = scanner.nextLine().trim();

                    if (!newFee.matches("\\d+(\\.\\d{1,2})?")) {
                        System.out.println("Invalid fee format. Example: 100 or 100.50");
                        continue;
                    }

                    boolean success = TutorService.updateSubjectFee(chosenSubject.getId(), newFee);
                    if (success) {
                        System.out.println("Fee updated successfully!");

                        userSubjects = TutorService.getMySubject(userId);
                        
                    } else {
                        System.out.println("Failed to update fee.");
                    }

                } else {
                    System.out.println("Choice out of range.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    

    private static void showScheduleManageMenu(String userId) {

        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("[ Class Schedule Management ]");
            System.out.println("1 - Add new class for subject");
            System.out.println("2 - Update class schedule");
            System.out.println("0 - Exit");
            System.out.print("=> ");
            
            String userInput = scanner.nextLine().trim();
            
            try {
                int intUserInput = Integer.parseInt(userInput);

                switch (intUserInput) {
                    case 1 -> {
                        List<Subject> userSubjects = TutorService.getMySubject(userId);
                        List<Subject> subjectWithoutSchedule = TutorService.getMySubjectWithoutSchedule(userId);
                        if (userSubjects.isEmpty()) {
                            System.out.println("No Subject Available, register with Admin");
                            return;
                            
                        } else if (subjectWithoutSchedule.isEmpty()) {
                            System.out.println("All of the Subject already have schedule, u can change it in update class schedule");
                            return;
                            
                        } else {
                            while (true) {
                                subjectWithoutSchedule = TutorService.getMySubjectWithoutSchedule(userId);
                                
                                System.out.println("[ Choose a subject to create class ]");
                                if (!TutorService.getMySubjectWithoutSchedule(userId).isEmpty()) {
                                    for (int i = 0; i < subjectWithoutSchedule.size(); i++) {
                                        System.out.println(i + 1 + " " + subjectWithoutSchedule.get(i).getSubjectName() + " " + subjectWithoutSchedule.get(i).getLevel());
                                    }
                                }
                                System.out.println("0 - Exit");
                                
                                System.out.println("=> ");
                                String strSubjectChoice = scanner.nextLine().trim();
                                try {
                                    int subjectChoice = Integer.parseInt(strSubjectChoice);
                                    
                                    if (subjectChoice == 0) {
                                        return;
                                        
                                    } else if (subjectChoice >= 1 && subjectChoice <= subjectWithoutSchedule.size()) {
                                        
                                        Subject chosenSubject = subjectWithoutSchedule.get(subjectChoice - 1);
                                        
                                        while (true) {
                                            System.out.println("Enter schedule time for each day (24H format like 0900-1100), or leave blank to skip:");
                                            
                                            Map<String, String> newScheduleMap = TutorService.newScheduleCreation();
                                            
                                            if (newScheduleMap.isEmpty()) {
                                                System.out.println("No Schedule added!!");
                                                break;
                                            }
                                            
                                            if (TutorService.noTimeConflictInSameLevel(chosenSubject, newScheduleMap)) {
                                                DataModel.ClassSchedule newSchedule = new ClassSchedule(IdGenerator.getNewId(ClassSchedule.class), chosenSubject.getId(), Service.TutorService.formatScheduleMapToString(newScheduleMap));
                                                DataManager<ClassSchedule> scheduleManager = DataManager.of(ClassSchedule.class);
                                                scheduleManager.appendOne(newSchedule);
                                                System.out.println("Schedule Create Successful!!!");
                                                break;
                                            }
                                        }
                                    } else {
                                        System.out.println("Out of Range");
                                    }

                                } catch(NumberFormatException e) {
                                    System.out.println("Invalid Choice");
                                }
                            }
                        }
                    }
                    case 2 -> {
                        while (true) {
                            List<Subject> mySubjectWithSchedule = TutorService.getMySubjectWithSchedule(userId);
                            
                            int index = 0;
                            
                            if (mySubjectWithSchedule.isEmpty()) {System.out.println("No Schedule Yet, Create in add new Class For subject"); return;}
                            
                            System.out.println("[ Choose a Subject to Update Schedule ]");
                            for (Subject subject : mySubjectWithSchedule) {
                                System.out.println(index + 1 + " - " + subject.getSubjectName() + " " + subject.getLevel());
                                index++;
                            }
                            System.out.println("0 - Exit");
                            
                            System.out.print("=> ");
                            String userUpdateInput = scanner.nextLine();
                            
                            try {
                                int userUpdateInputInt = Integer.parseInt(userUpdateInput);
                                
                                if (userUpdateInputInt == 0) {
                                    return;
                                    
                                } else if (userUpdateInputInt >= 1 && userUpdateInputInt <= mySubjectWithSchedule.size()) {
                                    String choosenSubjectId = mySubjectWithSchedule.get(userUpdateInputInt - 1).getId();
                                    
                                    DataManager<ClassSchedule> scheduleManager = DataManager.of(ClassSchedule.class);
                                    List<ClassSchedule> allSchedule = scheduleManager.readFromFile();
                                    
                                    boolean stopFinding = false;
                                    for (ClassSchedule schedule : allSchedule) {
                                        if (schedule.getSubjectId().equals(choosenSubjectId)) {
                                            
                                            while (true) {
                                                Map<String, String> newScheduleMap = TutorService.newScheduleCreation();
                                                if (TutorService.noTimeConflictInSameLevel(mySubjectWithSchedule.get(userUpdateInputInt - 1), newScheduleMap) && TutorService.noTimeConflictWithOwnSchedule(userId, newScheduleMap)) {
                                                    String scheduleLine = TutorService.formatScheduleMapToString(newScheduleMap);
                                                    schedule.setScheduleInweek(scheduleLine);
                                                    scheduleManager.overwriteFile(allSchedule);
                                                    System.out.println("Update Successful");
                                                    stopFinding = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (stopFinding) {break;}
                                    }
                                    
                                } else {
                                    System.out.println("Out Of Range");
                                }
                                
                                
                                
                            } catch(NumberFormatException e) {
                                System.out.println("Invalid Input");
                            }
                        }
                    }
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Out Of Range");

                }
                
            } catch(NumberFormatException e) {
                System.out.println("Invalid Input");
            }
        }     
    }

    
    public static void main(String[] args) {  
        showTutorMenu("T0001");
    }
}