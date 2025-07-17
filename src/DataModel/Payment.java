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
    private String status; 

    // Constructor
    public Payment(String paymentId, String receptionistId, String studentId, String subjectId, String amount, String paymentDate, String status) {
        this.paymentId = paymentId;
        this.receptionistId = receptionistId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status; 
    }

    // Save to text file format
    @Override
    public String toDataLine() {
        return String.join(",", paymentId, receptionistId, studentId, subjectId, amount, paymentDate, status);}

    // Get info
    @Override
    public String getId() {return paymentId;}
    public String getReceptionistId() {return receptionistId;}
    public String getStudentId() {return studentId;}
    public String getSubjectId() {return subjectId;}
    public String getAmount() {return amount;}
    public String getPaymentDate() {return paymentDate;}
    public String getStatus() {return status;}

    // Set info
    public void setStatus(String status) {this.status = status;}
    public void setPaymentDate(String paymentDate) {this.paymentDate = paymentDate;}
    public void setAmount(String amount) {this.amount = amount;}
    
}