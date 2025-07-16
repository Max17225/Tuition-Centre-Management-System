/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;
import java.util.List;

/**
 *
 * @author nengz
 */
public class StudentRequest implements DataSerializable {
    
    private String requestId;
    private String studentId;
    private String requestDetails; // This holds the combined request info (e.g., "ADD: Math (Notes: ...)")
    private String status;       // "PENDING", "ACCEPTED", "REJECTED", "CANCELLED"

    /**
     * Constructor 1: For creating NEW StudentRequest objects from GUI input.
     * This is used when a student submits a new request through the user interface.
     * * @param requestId The unique ID for the new request.
     * @param studentId The ID of the student submitting the request.
     * @param requestDetails A detailed description of the request.
     * @param status The initial status of the request (e.g., "PENDING").
     */
    public StudentRequest(String requestId, String studentId, String requestDetails, String status) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.requestDetails = requestDetails;
        this.status = status;
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
        return String.join(",", requestId, studentId, requestDetails, status);
    }

    // Get info
    /**
     * Returns the unique ID of the request.
     * @return The request ID.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Returns the ID of the student who made the request.
     * @return The student ID.
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Returns the detailed description of the request.
     * @return The request details.
     */
    public String getRequestDetails() {
        return requestDetails;
    }

    /**
     * Returns the current status of the request.
     * @return The request status ("PENDING", "ACCEPTED", "REJECTED", "CANCELLED").
     */
    public String getStatus() {
        return status;
    }

    // Set info
    /**
     * Sets the status of the student request.
     * @param status The new status for the request.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the detailed description of the request.
     * @param requestDetails The new request details.
     */
    public void setRequestDetails(String requestDetails) {
        this.requestDetails = requestDetails;
    }
}