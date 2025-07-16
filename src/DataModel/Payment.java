package DataModel;

public class Payment implements DataSerializable {
    private final String billId;
    private final String recepId;
    private final String studentId;
    private final String subjectId;
    private final String amount;
    private final String paymentDate;

    public Payment(String billId, String recepId, String studentId, String subjectId, String amount, String paymentDate) {
        this.billId = billId;
        this.recepId = recepId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    @Override
    public String getId() {
        return billId;
    }

    @Override
    public String toDataLine() {
        return String.join(",", billId, recepId, studentId, subjectId, amount, paymentDate);
    }

    // Getters
    public String getRecepId() { return recepId; }
    public String getStudentId() { return studentId; }
    public String getSubjectId() { return subjectId; }
    public String getAmount() { return amount; }
    public String getPaymentDate() { return paymentDate; }
}
