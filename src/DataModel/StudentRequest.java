/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;

/**
 *
 * @author nengz
 */
public class StudentRequest implements DataSerializable {
    
    private String requestId;
    private String studentId;
    private String oldSubjectId; 
    private String newSubjectId;
    private String status;       // "Pending", "Accepted", "Rejected", "Cancelled"
    private String reason;

    /**
     * Constructor 1: For creating NEW StudentRequest objects from GUI input.
     * This is used when a student submits a new request through the user interface.
     * @param requestId The unique ID for the new request.
     * @param studentId The ID of the student submitting the request.
     * @param oldSubjectId The ID of the subject to be dropped.
     * @param newSubjectId The ID of the new subject the student wishes to enroll in.
     * @param reason A detailed description of the request.
     */
    public StudentRequest(String requestId, String studentId, String oldSubjectId, String newSubjectId, String reason) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.oldSubjectId = oldSubjectId;
        this.newSubjectId = newSubjectId;
        this.reason = reason;
        this.status = "Pending"; // Default status for new requests
    }
    
    /**
     * Constructor 2: For loading StudentRequest objects from a data file.
     * This constructor takes all fields, including the status from the file.
     * @param requestId The unique ID for the request.
     * @param studentId The ID of the student.
     * @param oldSubjectId The ID of the subject to be dropped.
     * @param newSubjectId The ID of the new subject.
     * @param reason The reason for the request.
     * @param status The current status of the request.
     */
    public StudentRequest(String requestId, String studentId, String oldSubjectId, String newSubjectId, String reason, String status) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.oldSubjectId = oldSubjectId;
        this.newSubjectId = newSubjectId;
        this.reason = reason;
        this.status = status; // Use the status passed from the file
    }
    
    /**
     * Implements the getId() method from the DataSerializable interface.
     * Returns the unique identifier for this student request.
     * @return The requestId of this student request.
     */
    @Override
    public String getId() { 
        return this.requestId;
    }

    /**
     * Implements the toDataLine() method from the DataSerializable interface.
     * Converts the object's current state into a comma-separated string,
     * suitable for saving to a text file.
     * The order of fields here must match the order expected by the constructor
     * that takes a List<String> (Constructor 2).
     * @return A comma-separated string representation of the StudentRequest object.
     */
    @Override
    public String toDataLine() { 
        return String.join(",", requestId, studentId, oldSubjectId, newSubjectId, reason, status);
    }

    // Get info
    public String getRequestId() {return requestId;}
    public String getStudentId() {return studentId;}
    public String getOldSubjectId() {return oldSubjectId;}
    public String getNewSubjectId() {return newSubjectId;}
    public String getReason() {return reason;}
    public String getStatus() {return status;}
    
    // Set info
    public void setStatus(String status) {this.status = status;}
    public void setReason(String reason) { this.reason = reason; } 
    public void setOldSubjectId(String oldSubjectId) { this.oldSubjectId = oldSubjectId; }
    public void setNewSubjectId(String newSubjectId) { this.newSubjectId = newSubjectId; }
    
}