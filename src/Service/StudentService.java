/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DataModel.Student;
import DataModel.StudentRequest;
import DataModel.Subject;
import DataModel.Enrollment;
import DataModel.Payment;
import Util.DataManager;

import java.util.ArrayList;
import java.util.List;
import Util.IdGenerator;
import java.time.LocalDate;

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
    private DataManager<Payment> paymentManager;

    /**
     * Constructor for StudentService.
     * Initializes the DataManagers required for student operations.
     */
    public StudentService() {
        this.studentRequestManager = DataManager.of(StudentRequest.class);
        this.subjectManager = DataManager.of(Subject.class);
        this.studentManager = DataManager.of(Student.class);
        this.enrollmentManager = DataManager.of(Enrollment.class);
        this.paymentManager = DataManager.of(Payment.class);
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
     * Updates an existing student's profile in the data file.
     * Reads all students, finds the one to update by ID, replaces it,
     * and overwrites the file.
     * @param updatedStudent The Student object with updated information.
     * @return true if the student was successfully updated, false otherwise.
     */
    public boolean updateStudentProfile(Student updatedStudent) {
        List<Student> allStudents = studentManager.readFromFile();
        boolean updated = false;
        if (allStudents != null) {
            for (int i = 0; i < allStudents.size(); i++) {
                // Ensure the comparison is by unique ID
                if (allStudents.get(i).getId().equals(updatedStudent.getId())) {
                    allStudents.set(i, updatedStudent); // Replace the old student object with the updated one
                    updated = true;
                    break;
                }
            }
            if (updated) {
                studentManager.overwriteFile(allStudents); // Write the updated list back to the file
            }
        }
        return updated;
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

    
     // --- Payment-related methods ---

    /**
     * Represents the payment details for a specific subject for a student.
     */
    public static class SubjectPaymentDetail {
        public Subject subject;
        public String status; // "Unpaid", "Pending", "Paid"

        public SubjectPaymentDetail(Subject subject, String status) {
            this.subject = subject;
            this.status = status;
        }
    }

    /**
     * Retrieves a list of enrolled subjects for a student along with their payment status and amount.
     * @param studentId The ID of the student.
     * @return A list of SubjectPaymentDetail objects.
     */
    public List<SubjectPaymentDetail> getEnrolledSubjectsWithPaymentStatusAndAmount(String studentId) {
        List<SubjectPaymentDetail> details = new ArrayList<>();
        List<Subject> enrolledSubjects = getEnrolledSubjects(studentId); // Reusing existing method

        // ALTERNATIVE: Read all payments and filter manually (since DataManager won't be changed)
        List<Payment> allPayments = paymentManager.readFromFile();
        List<Payment> studentPayments = new ArrayList<>();
        if (allPayments != null) {
            for (Payment payment : allPayments) {
                if (payment.getStudentId().equals(studentId)) {
                    studentPayments.add(payment);
                }
            }
        }

        for (Subject subject : enrolledSubjects) {
            String currentStatus = "Unpaid"; // Default status
            
            // Check for existing payments for this student and subject
            for (Payment payment : studentPayments) {
                if (payment.getSubjectId().equals(subject.getId())) {
                    currentStatus = payment.getStatus();
                    break; 
                }
            }
            details.add(new SubjectPaymentDetail(subject, currentStatus));
        }
        return details;
    }

    /**
     * Records a payment for a specific subject for a student.
     * Sets the status to "Pending" upon initiation by the student.
     * @param studentId The ID of the student making the payment.
     * @param subjectId The ID of the subject the payment is for.
     * @param amount The amount being paid.
     * @return true if the payment was recorded successfully, false otherwise.
     */
    public boolean recordSubjectPayment(String studentId, String subjectId, String amount) {
        // First, check if a payment record (Paid or Pending) already exists for this subject.
        // If it's already 'Paid', we don't allow a new payment.

        List<Payment> allPayments = paymentManager.readFromFile();
        List<Payment> studentPayments = new ArrayList<>();
        if (allPayments != null) {
            for (Payment payment : allPayments) {
                if (payment.getStudentId().equals(studentId)) {
                    studentPayments.add(payment);
                }
            }
        }
        
        for (Payment existingPayment : studentPayments) {
            if (existingPayment.getSubjectId().equals(subjectId)) {
                if ("Paid".equalsIgnoreCase(existingPayment.getStatus()) || "Pending".equalsIgnoreCase(existingPayment.getStatus())) {
                    System.out.println("Payment for subject " + subjectId + " is already " + existingPayment.getStatus() + ". Cannot initiate new payment.");
                    return false; // Cannot pay again if already paid or pending
                } else {
                    // If status is "Unpaid" or any other non-final status, we can update it.
                    // This scenario is for retrying a previously failed/cancelled payment or updating an 'Unpaid' to 'Pending'.
                    existingPayment.setAmount(amount); // Update amount in case it changed
                    existingPayment.setPaymentDate(LocalDate.now().toString());
                    existingPayment.setStatus("Pending"); // Set to pending
                    paymentManager.updateRecord(existingPayment); // Use updateRecord instead of update
                    System.out.println("Updated payment for subject " + subjectId + " to Pending.");
                    return true;
                }
            }
        }
        
        // If no existing record or existing record was truly 'Unpaid' and needs a new record
        String paymentId = IdGenerator.getNewId(Payment.class);
        // Receptionist ID is set to "N/A" for student-initiated payments.
        // It will be updated by a receptionist when they process and confirm the payment.
        Payment newPayment = new Payment(paymentId, "N/A", studentId, subjectId, amount, LocalDate.now().toString(), "Pending");
        paymentManager.appendOne(newPayment); // Use appendOne instead of add
        System.out.println("New payment initiated for subject " + subjectId + " with status Pending.");
        return true;
    }

    /**
     * Calculates the total outstanding amount for a student, summing up fees for subjects
     * that are either "Unpaid" or "Pending".
     * @param studentId The ID of the student.
     * @return The total outstanding amount as a double.
     */
    public double calculateTotalOutstandingAmount(String studentId) {
        double total = 0.0;
        List<SubjectPaymentDetail> paymentDetails = getEnrolledSubjectsWithPaymentStatusAndAmount(studentId);

        for (SubjectPaymentDetail detail : paymentDetails) {
            if ("Unpaid".equalsIgnoreCase(detail.status) || "Pending".equalsIgnoreCase(detail.status)) {
                try {
                    total += Double.parseDouble(detail.subject.getFeePerMonth()); 
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing fees for subject " + detail.subject.getSubjectName() + ": " + e.getMessage());
                }
            }
        }
        return total;
    }
}