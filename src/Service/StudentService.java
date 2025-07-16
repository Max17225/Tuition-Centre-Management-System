/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DataModel.Student;
import DataModel.StudentRequest;
import DataModel.Subject; 
import Util.DataManager;

import java.util.ArrayList;
import java.util.List;
import Util.IdGenerator; 

/**
 *
 * @author nengz
 */

/**
 * Provides business logic and operations related to student functionalities.
 * This includes managing subject change requests, viewing schedules, and payments.
 */
public class StudentService {

    private DataManager<StudentRequest> studentRequestManager;
    private DataManager<Subject> subjectManager; // 

    /**
     * Constructor for StudentService.
     * Initializes the DataManagers required for student operations.
     */
    public StudentService() {
        this.studentRequestManager = DataManager.of(StudentRequest.class);
        this.subjectManager = DataManager.of(Subject.class); // Initialize if needed for other methods
    }

    /**
     * Submits a new subject change request on behalf of a student.
     * This method validates input and saves the request to the database.
     *
     * @param student The Student object making the request.
     * @param selectedSubject The name of the subject the student wishes to change to.
     * @param notes Additional notes or reason for the request.
     * @return true if the request was submitted successfully, false otherwise.
     */
    public boolean submitSubjectChangeRequest(Student student, String selectedSubject, String notes) {
        if (student == null) {
            System.err.println("Error: Student object cannot be null for submitting a request.");
            return false;
        }
        if (selectedSubject == null || selectedSubject.trim().isEmpty()) {
            System.err.println("Error: Selected subject cannot be empty.");
            return false;
        }
        if (notes == null || notes.trim().isEmpty()) {
            System.err.println("Error: Notes/reason for request cannot be empty.");
            return false;
        }

        // Generate a unique request ID using IdGenerator
        String requestId = IdGenerator.getNewId(StudentRequest.class);
        String studentId = student.getId(); 
        
        // Combine request details
        String requestDetails = "CHANGE_SUBJECT: " + selectedSubject + " (Notes: " + notes + ")";
        String status = "PENDING"; // Initial status for all new requests

        // Create the StudentRequest object
        StudentRequest newRequest = new StudentRequest(requestId, studentId, requestDetails, status);

        // Save the request using DataManager
        try {
            studentRequestManager.appendOne(newRequest);
            System.out.println("Subject change request submitted successfully for student " + studentId + ". Request ID: " + requestId);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to submit subject change request for student " + studentId + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all subject change requests for a specific student.
     *
     * @param studentId The ID of the student whose requests are to be retrieved.
     * @return A List of StudentRequest objects belonging to the specified student.
     * Returns an empty list if no requests are found or an error occurs.
     */
    public List<StudentRequest> getStudentRequests(String studentId) {
        List<StudentRequest> allRequests = studentRequestManager.readFromFile();
        List<StudentRequest> studentSpecificRequests = new ArrayList<>();

        for (StudentRequest request : allRequests) {
            if (request.getStudentId().equalsIgnoreCase(studentId)) {
                studentSpecificRequests.add(request);
            }
        }
        return studentSpecificRequests;
    }

    // --- Placeholder methods for other student functionalities ---

    /**
     * Placeholder for viewing a student's schedule.
     * This method would typically interact with a ClassSchedule DataManager.
     * @param studentId The ID of the student.
     * @return A string representation of the schedule (or a list of schedule objects).
     */
    public String viewSchedule(String studentId) {
        // Implement logic to retrieve and format student's schedule
        return "Schedule for student " + studentId + ": Not yet implemented.";
    }

    /**
     * Placeholder for viewing a student's payment history.
     * This method would typically interact with a Payment DataManager.
     * @param studentId The ID of the student.
     * @return A string representation of payment history (or a list of payment objects).
     */
    public String viewPaymentHistory(String studentId) {
        // Implement logic to retrieve and format student's payment history
        return "Payment history for student " + studentId + ": Not yet implemented.";
    }
}
  
