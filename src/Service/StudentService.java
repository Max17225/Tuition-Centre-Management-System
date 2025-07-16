/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DataModel.Student;
import DataModel.StudentRequest;
import DataModel.Subject;
import DataModel.Enrollment;
import Util.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator; // For safe removal during iteration
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
    private DataManager<Subject> subjectManager;
    private DataManager<Student> studentManager;
    private DataManager<Enrollment> enrollmentManager;

    /**
     * Constructor for StudentService.
     * Initializes the DataManagers required for student operations.
     */
    public StudentService() {
        this.studentRequestManager = DataManager.of(StudentRequest.class);
        this.subjectManager = DataManager.of(Subject.class);
        this.studentManager = DataManager.of(Student.class);
        this.enrollmentManager = DataManager.of(Enrollment.class);
    }

    /**
     * Retrieves a list of subjects the given student is currently enrolled in.
     * @param studentId The ID of the student.
     * @return A List of Subject objects the student is enrolled in. Returns an empty list if no enrollment is found or no subjects are enrolled.
     */
    public List<Subject> getEnrolledSubjects(String studentId) {
        List<Subject> enrolled = new ArrayList<>();
        // Assuming student has an enrollmentId, and we can retrieve the Enrollment object
        // For simplicity, we'll assume a direct lookup or iterate if necessary.
        // In a real system, you might have a Student-Enrollment mapping.
        
        // Find the student's enrollment record
        // Assuming Student object has an enrollmentId or you can find enrollment by student ID
        Student student = studentManager.getRecordById(studentId);
        if (student == null || student.getEnrollmentId() == null || student.getEnrollmentId().isEmpty() || "Empty".equalsIgnoreCase(student.getEnrollmentId())) {
            return enrolled; // No enrollment found for this student
        }

        Enrollment enrollment = enrollmentManager.getRecordById(student.getEnrollmentId());

        if (enrollment != null) {
            // Get subject IDs from the enrollment and retrieve Subject objects
            if (!"Empty".equalsIgnoreCase(enrollment.getSubjectId1())) {
                Subject sub1 = subjectManager.getRecordById(enrollment.getSubjectId1());
                if (sub1 != null) {
                    enrolled.add(sub1);
                }
            }
            if (!"Empty".equalsIgnoreCase(enrollment.getSubjectId2())) {
                Subject sub2 = subjectManager.getRecordById(enrollment.getSubjectId2());
                if (sub2 != null) {
                    enrolled.add(sub2);
                }
            }
            if (!"Empty".equalsIgnoreCase(enrollment.getSubjectId3())) {
                Subject sub3 = subjectManager.getRecordById(enrollment.getSubjectId3());
                if (sub3 != null) {
                    enrolled.add(sub3);
                }
            }
        }
        return enrolled;
    }


    /**
     * Submits a subject change request for a student.
     * Handles adding new subjects or replacing existing ones based on oldSubjectName.
     * @param student The logged-in Student object.
     * @param oldSubjectName The name of the subject to drop or "None (Add New Subject)".
     * @param newSubjectName The name of the new subject to enroll in.
     * @param reason The reason for the change.
     * @return true if the request was submitted successfully, false otherwise.
     */
    public boolean submitSubjectChangeRequest(Student student, String oldSubjectName, String newSubjectName, String reason) {
        if (student == null) {
            System.err.println("Error: Student object is null.");
            return false;
        }

        // Get IDs for subjects
        String oldSubjectId = null;
        if ("None (Add New Subject)".equalsIgnoreCase(oldSubjectName)) {
            oldSubjectId = "None"; // Special placeholder for adding a new subject
        } else {
            // Find old subject ID
            Subject oldSub = getSubjectByName(oldSubjectName);
            if (oldSub != null) {
                oldSubjectId = oldSub.getId();
            } else {
                System.err.println("Error: Old subject '" + oldSubjectName + "' not found.");
                return false;
            }
        }

        Subject newSub = getSubjectByName(newSubjectName);
        if (newSub == null) {
            System.err.println("Error: New subject '" + newSubjectName + "' not found.");
            return false;
        }
        String newSubjectId = newSub.getId();

        // Generate a new request ID
        String requestId = IdGenerator.getNewId(StudentRequest.class);

        // Create the StudentRequest object
        StudentRequest newRequest = new StudentRequest(requestId, student.getId(), oldSubjectId, newSubjectId, reason);

        // Save the request to file
        try {
            studentRequestManager.appendOne(newRequest);
            return true;
        } catch (Exception e) {
            System.err.println("Error submitting subject change request: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a student already has a pending subject change request for a specific old and new subject.
     * @param studentId The ID of the student.
     * @param oldSubjectId The ID of the old subject (or "None" for adding a new subject).
     * @param newSubjectId The ID of the new subject.
     * @return true if a pending request exists, false otherwise.
     */
    public boolean hasPendingSubjectChangeRequest(String studentId, String oldSubjectId, String newSubjectId) {
        List<StudentRequest> requests = studentRequestManager.readFromFile();
        if (requests == null) {
            return false;
        }
        for (StudentRequest req : requests) {
            if (req.getStudentId().equals(studentId) &&
                req.getOldSubjectId().equalsIgnoreCase(oldSubjectId) &&
                req.getNewSubjectId().equalsIgnoreCase(newSubjectId) &&
                req.getStatus().equalsIgnoreCase("Pending")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to get a Subject object by its name.
     * @param subjectName The name of the subject.
     * @return The Subject object, or null if not found.
     */
    private Subject getSubjectByName(String subjectName) {
        List<Subject> all = subjectManager.readFromFile();
        if (all == null) return null;
        for (Subject sub : all) {
            if (sub.getSubjectName().equalsIgnoreCase(subjectName)) {
                return sub;
            }
        }
        return null;
    }

    /**
     * Retrieves the name of a subject given its ID.
     * Handles "None" and "Empty" IDs for displaying purposes.
     * @param subjectId The ID of the subject.
     * @return The subject name, "None (Add New Subject)" if ID is "None", or "Unknown Subject" if not found.
     */
    public String getSubjectNameById(String subjectId) {
        if (subjectId == null || "Empty".equalsIgnoreCase(subjectId)) {
            return "N/A"; // Or "No Subject" depending on desired display for truly empty slots
        }
        if ("None".equalsIgnoreCase(subjectId)) {
            return "None (Add New Subject)"; // Special case for adding new subject
        }
        Subject subject = subjectManager.getRecordById(subjectId);
        return (subject != null) ? subject.getSubjectName() : "Unknown Subject";
    }


    /**
     * Cancels a student subject change request by updating its status.
     * @param requestId The ID of the request to cancel.
     * @return true if the request was successfully cancelled, false otherwise.
     */
    public boolean cancelStudentRequest(String requestId) {
        List<StudentRequest> allRequests = studentRequestManager.readFromFile();
        if (allRequests == null) {
            return false;
        }

        boolean foundAndCancelled = false;
        for (StudentRequest req : allRequests) {
            if (req.getId().equals(requestId)) {
                req.setStatus("Cancelled"); // Update the status
                foundAndCancelled = true;
                break;
            }
        }

        if (foundAndCancelled) {
            studentRequestManager.overwriteFile(allRequests); // Overwrite the file with updated list
            return true;
        }
        return false; // Request not found or not in a cancellable state
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