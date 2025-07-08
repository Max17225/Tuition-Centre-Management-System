/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;

/**
 *
 * @author nengz
 */
public class Subject implements DataSerializable{
    final private String subjectId;
    final private String subjectName;
    final private String level;
    final private String tutorId;
    private String feePerMonth;

    //constructor
    public Subject(String subjectName, String level, String tutorId, String feePerMonth) {
        this.subjectId = Util.IdGenerator.getNewId(Subject.class);
        this.subjectName = subjectName;
        this.level = level;
        this.tutorId = tutorId;
        this.feePerMonth = feePerMonth;
    }
    
    
    // get info
    @Override
    public String getId() {return this.subjectId;}
    public String getSubjectName() {return this.subjectName;}
    public String getLevel() {return this.level;}
    public String getTutorId() {return this.tutorId;}
    public String getFeePerMonth() {return this.feePerMonth;}
    
    // set info
    public void setFee(String newFee) {this.feePerMonth = newFee;}
    
    @Override
    public String toDataLine() {
        // do the code here
        return String.join(",", subjectId, subjectName, level, tutorId, feePerMonth);
    }
}
