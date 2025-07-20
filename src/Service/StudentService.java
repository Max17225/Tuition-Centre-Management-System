/**
 * StudentService.java
 *
 * This service class provides core business logic and helper methods
 * for handling student-related functionalities such as profile updates,
 * subject enrollment, schedule viewing, subject change requests, and payment processing.
 * 
 * It acts as a central access point for retrieving and manipulating student data,
 * and interacts with various data models including:
 * - Student
 * - Subject
 * - Enrollment
 * - StudentRequest
 * - Payment
 * - ClassSchedule
 * 
 * This class is used by the following student-facing GUI components:
 * - StudentGUI
 * - StudentProfileGUI
 * - ChangeSubjectGUI
 * - ViewStudentScheduleGUI
 * - StudentPaymentGUI
 * 
 * Depends on:
 * - DataManager (for file I/O)
 * - IdGenerator (for unique ID assignment)
 */

package Service;

import DataModel.Student;
import DataModel.StudentRequest;
import DataModel.Subject;
import DataModel.Enrollment;
import DataModel.Payment;
import DataModel.Tutor;
import DataModel.ClassSchedule;
import Util.DataManager;
import Util.IdGenerator;

import java.util.*;
import java.time.LocalDate;

public class StudentService {

    private final DataManager<StudentRequest> studentRequestManager;
    private final DataManager<Subject> subjectManager;
    private final DataManager<Student> studentManager;
    private final DataManager<Enrollment> enrollmentManager;
    private final DataManager<Payment> paymentManager;

    /**
     * Constructor initializes data managers
     */
    public StudentService() {
        this.studentRequestManager = DataManager.of(StudentRequest.class);
        this.subjectManager = DataManager.of(Subject.class);
        this.studentManager = DataManager.of(Student.class);
        this.enrollmentManager = DataManager.of(Enrollment.class);
        this.paymentManager = DataManager.of(Payment.class);
    }

    /**
     * Retrieves a Student object by ID.
     * @param studentId The ID of the student.
     * @return Student object or null if not found.
     * Used in: StudentProfileGUI
     */
    public Student getStudentById(String studentId) {
        return studentManager.getRecordById(studentId);
    }

    /**
     * Retrieves a list of subjects a student is enrolled in.
     * @param studentId The ID of the student.
     * @return List of Subject objects.
     * Used in: ChangeSubjectGUI, StudentPaymentGUI
     */
    public List<Subject> getEnrolledSubjects(String studentId) {
        List<Subject> enrolled = new ArrayList<>();
        Student student = studentManager.getRecordById(studentId);
        if (student == null || student.getEnrollmentId() == null || student.getEnrollmentId().equalsIgnoreCase("Empty")) {
            return enrolled;
        }
        Enrollment enrollment = enrollmentManager.getRecordById(student.getEnrollmentId());
        if (enrollment != null) {
            if (!"Empty".equalsIgnoreCase(enrollment.getSubjectId1())) enrolled.add(subjectManager.getRecordById(enrollment.getSubjectId1()));
            if (!"Empty".equalsIgnoreCase(enrollment.getSubjectId2())) enrolled.add(subjectManager.getRecordById(enrollment.getSubjectId2()));
            if (!"Empty".equalsIgnoreCase(enrollment.getSubjectId3())) enrolled.add(subjectManager.getRecordById(enrollment.getSubjectId3()));
        }
        return enrolled;
    }

    /**
     * Updates the profile of a student.
     * @param updatedStudent The updated Student object.
     * @return true if successfully updated, false otherwise.
     * Used in: StudentProfileGUI
     */
    public boolean updateStudentProfile(Student updatedStudent) {
        List<Student> allStudents = studentManager.readFromFile();
        boolean updated = false;
        for (int i = 0; i < allStudents.size(); i++) {
            if (allStudents.get(i).getId().equals(updatedStudent.getId())) {
                allStudents.set(i, updatedStudent);
                updated = true;
                break;
            }
        }
        if (updated) studentManager.overwriteFile(allStudents);
        return updated;
    }

    /**
     * Retrieves a subject name based on its ID.
     * @param subjectId The subject ID.
     * @return Subject name string.
     * Used in: ChangeSubjectGUI (drop-down display)
     */
    public String getSubjectNameById(String subjectId) {
        if (subjectId == null || subjectId.equalsIgnoreCase("Empty")) return "N/A";
        if (subjectId.equalsIgnoreCase("None")) return "None (Add New Subject)";
        Subject subject = subjectManager.getRecordById(subjectId);
        return (subject != null) ? subject.getSubjectName() : "Unknown Subject";
    }

    /**
     * Submits a subject change request.
     * @param student The Student object.
     * @param oldSubjectName The current subject name.
     * @param newSubjectName The new subject name.
     * @param reason The reason for the change.
     * @return true if successful, false otherwise.
     * Used in: ChangeSubjectGUI
     */
    public boolean submitSubjectChangeRequest(Student student, String oldSubjectName, String newSubjectName, String reason) {
        if (student == null) return false;

        String oldSubjectId = "None".equalsIgnoreCase(oldSubjectName) ? "None" : getSubjectIdByName(oldSubjectName);
        String newSubjectId = getSubjectIdByName(newSubjectName);

        if (oldSubjectId == null || newSubjectId == null) return false;

        String requestId = IdGenerator.getNewId(StudentRequest.class);
        StudentRequest request = new StudentRequest(requestId, student.getId(), oldSubjectId, newSubjectId, reason);

        try {
            studentRequestManager.appendOne(request);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Cancels a student subject change request.
     * @param requestId The ID of the request to cancel.
     * @return true if cancelled, false otherwise.
     * Used in: ChangeSubjectGUI
     */
    public boolean cancelStudentRequest(String requestId) {
        List<StudentRequest> allRequests = studentRequestManager.readFromFile();
        for (StudentRequest req : allRequests) {
            if (req.getId().equals(requestId)) {
                req.setStatus("Cancelled");
                studentRequestManager.overwriteFile(allRequests);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for a pending subject change request.
     * @param studentId The student's ID.
     * @param oldSubjectId Old subject ID.
     * @param newSubjectId New subject ID.
     * @return true if a pending request exists.
     * Used in: ChangeSubjectGUI
     */
    public boolean hasPendingSubjectChangeRequest(String studentId, String oldSubjectId, String newSubjectId) {
        return studentRequestManager.readFromFile().stream()
                .anyMatch(req -> req.getStudentId().equals(studentId)
                        && req.getOldSubjectId().equalsIgnoreCase(oldSubjectId)
                        && req.getNewSubjectId().equalsIgnoreCase(newSubjectId)
                        && req.getStatus().equalsIgnoreCase("Pending"));
    }

    /**
    * Generates the weekly class schedule map for a student.
    * 
    * @param student The Student object.
    * @return A map of day to schedule entries. Example key: "Monday", value: List of subject strings.
    * 
    * Used in: ViewStudentScheduleGUI
    */
   public static Map<String, List<String>> getStudentWeeklySchedule(Student student) {
       Map<String, List<String>> weekSchedule = new LinkedHashMap<>();

       // Initialize days of the week
       Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
               .forEach(day -> weekSchedule.put(day, new ArrayList<>()));

       // Get enrollment record
       Enrollment enrollment = DataManager.of(Enrollment.class).getRecordById(student.getEnrollmentId());
       if (enrollment == null) return weekSchedule;

       // Collect subject IDs from enrollment
       List<String> enrolledSubjectIds = Arrays.asList(
               enrollment.getSubjectId1(), enrollment.getSubjectId2(), enrollment.getSubjectId3()
       );

       // Load subject, schedule, and tutor data
       List<ClassSchedule> schedules = DataManager.of(ClassSchedule.class).readFromFile();

       Map<String, Subject> subjectMap = new HashMap<>();
       for (Subject s : DataManager.of(Subject.class).readFromFile()) {
           subjectMap.put(s.getId(), s);
       }

       Map<String, Tutor> tutorMap = new HashMap<>();
       for (Tutor t : DataManager.of(Tutor.class).readFromFile()) {
           tutorMap.put(t.getId(), t);
       }

       // Loop over all schedules and match against enrolled subjects
       for (ClassSchedule schedule : schedules) {
           if (enrolledSubjectIds.contains(schedule.getSubjectId())) {
               Subject subject = subjectMap.get(schedule.getSubjectId());
               if (subject != null) {
                   // Get tutor name if available
                   String tutorName = "Unknown Tutor";
                   Tutor tutor = tutorMap.get(subject.getTutorId());
                   if (tutor != null) tutorName = tutor.getUsername();

                   // Process each time slot in the schedule string
                   for (String pair : schedule.getScheduleInWeek().split("\\|")) {
                       String[] parts = pair.split(":");
                       if (parts.length == 2) {
                           String day = parts[0];
                           String time = parts[1];

                           String entry = subject.getSubjectName()
                                   + " (" + time + ") - Level " + subject.getLevel()
                                   + " | Tutor: " + tutorName;

                           weekSchedule.getOrDefault(day, new ArrayList<>()).add(entry);
                       }
                   }
               }
           }
       }

    return weekSchedule;
}


    /**
     * Retrieves all payment records for a student.
     * @param studentId The student ID.
     * @return List of Payment objects.
     * Used in: StudentPaymentGUI
     */
    public List<Payment> getPaymentsForStudent(String studentId) {
        List<Payment> allPayments = paymentManager.readFromFile();
        List<Payment> filtered = new ArrayList<>();
        for (Payment p : allPayments) if (p.getStudentId().equals(studentId)) filtered.add(p);
        return filtered;
    }

    /**
     * Retrieves subject list with payment status for student.
     * @param studentId The student ID.
     * @return List of SubjectPaymentDetail.
     * Used in: StudentPaymentGUI
     */
    public List<SubjectPaymentDetail> getEnrolledSubjectsWithPaymentStatusAndAmount(String studentId) {
        List<SubjectPaymentDetail> result = new ArrayList<>();
        List<Payment> payments = getPaymentsForStudent(studentId);
        for (Subject subject : getEnrolledSubjects(studentId)) {
            String status = "Unpaid";
            for (Payment p : payments) {
                if (p.getSubjectId().equals(subject.getId())) {
                    status = p.getStatus();
                    break;
                }
            }
            result.add(new SubjectPaymentDetail(subject, status));
        }
        return result;
    }

    /**
     * Records a subject payment initiated by student.
     * @param studentId The student ID.
     * @param subjectId The subject ID.
     * @param amount The amount paid.
     * @return true if recorded successfully.
     * Used in: StudentPaymentGUI
     */
    public boolean recordSubjectPayment(String studentId, String subjectId, String amount) {
        List<Payment> payments = getPaymentsForStudent(studentId);
        for (Payment p : payments) {
            if (p.getSubjectId().equals(subjectId)) {
                if ("Paid".equalsIgnoreCase(p.getStatus()) || "Pending".equalsIgnoreCase(p.getStatus())) {
                    return false;
                } else {
                    p.setAmount(amount);
                    p.setDate(LocalDate.now().toString());
                    p.setStatus("Pending");
                    paymentManager.updateRecord(p);
                    return true;
                }
            }
        }
        
        Payment newPayment = new Payment(IdGenerator.getNewId(Payment.class), "N/A", studentId, subjectId, amount, LocalDate.now().toString(), "Pending");
        paymentManager.appendOne(newPayment);
        return true;
    }

    /**
     * Calculates the total outstanding fee for the student.
     * @param studentId The student ID.
     * @return total amount due.
     * Used in: StudentPaymentGUI
     */
    public double calculateTotalOutstandingAmount(String studentId) {
        double total = 0.0;
        for (SubjectPaymentDetail detail : getEnrolledSubjectsWithPaymentStatusAndAmount(studentId)) {
            if ("Unpaid".equalsIgnoreCase(detail.status) || "Pending".equalsIgnoreCase(detail.status)) {
                try {
                    total += Double.parseDouble(detail.subject.getFeePerMonth());
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return total;
    }

    /**
     * Helper data holder for subject and payment status.
     * Used in: StudentPaymentGUI
     */
    public static class SubjectPaymentDetail {
        public Subject subject;
        public String status;

        public SubjectPaymentDetail(Subject subject, String status) {
            this.subject = subject;
            this.status = status;
        }
    }

    /**
     * Helper method to find subject ID by name.
     */
    private String getSubjectIdByName(String subjectName) {
        for (Subject sub : subjectManager.readFromFile()) {
            if (sub.getSubjectName().equalsIgnoreCase(subjectName)) {
                return sub.getId();
            }
        }
        return null;
    }
}
