/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;

public class Payment implements DataSerializable {
    private String paymentId;
    private String receptionistId;
    private String studentId;
    private String subjectId;
    private String amount;
    private String paymentDate;

    // Constructor
    public Payment(String paymentId, String receptionistId, String studentId, String subjectId, String amount, String paymentDate) {
        this.paymentId = paymentId;
        this.receptionistId = receptionistId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    // Save to text file format
    @Override
    public String toDataLine() {
        // Format: PYM001,R001,S001,SUB001,250.00,2025-07-08
        return String.join(",", 
            paymentId, 
            receptionistId, 
            studentId, 
            subjectId, 
            amount, 
            paymentDate
        );
    }

    // Return Payment ID
    @Override
    public String getId() {
        return paymentId;
    }

    // Optional: Getters
    public String getReceptionistId() {
        return receptionistId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }
}
