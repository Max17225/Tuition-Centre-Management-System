/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;

/**
 *
 * @author nengz
 */
public class Enrollment implements DataSerializable {
    private final String enrollmentId;
    private final String enrollmentDate;
    private String level;
    private String subjectId1;
    private String subjectId2;
    private String subjectId3;
    
    // constructors
    public Enrollment(String enrollmentId, String enrollmentDate, String level) {
        this.enrollmentId = enrollmentId;
        this.enrollmentDate = enrollmentDate;
        this.level = level;
        this.subjectId1 = "Empty";
        this.subjectId2 = "Empty";
        this.subjectId3 = "Empty";
    }
    
    public Enrollment(String enrollmentId, String enrollmentDate, String level, String subjectId1) {
        this.enrollmentId = enrollmentId;
        this.enrollmentDate = enrollmentDate;
        this.level = level;
        this.subjectId1 = subjectId1;
        this.subjectId2 = "Empty";
        this.subjectId3 = "Empty";
    }
    
    public Enrollment(String enrollmentId, String enrollmentDate, String level, String subjectId1, String subjectId2) {
        this.enrollmentId = enrollmentId;
        this.enrollmentDate = enrollmentDate;
        this.level = level;
        this.subjectId1 = subjectId1;
        this.subjectId2 = subjectId2;
        this.subjectId3 = "Empty";
    }
    
    public Enrollment(String enrollmentId, String enrollmentDate, String level, String subjectId1, String subjectId2, String subjectId3) {
        this.enrollmentId = enrollmentId;
        this.enrollmentDate = enrollmentDate;
        this.level = level;
        this.subjectId1 = subjectId1;
        this.subjectId2 = subjectId2;
        this.subjectId3 = subjectId3;
    }
    
    // to data line
    @Override
    public String toDataLine() {
        // do the code here
        return String.join(",", enrollmentId, enrollmentDate, level, subjectId1, subjectId2, subjectId3);
    }
    
    // get info
    @Override
    public String getId() {return enrollmentId;}
    public String getDate() {return enrollmentDate;}
    public String getLevel() {return level;}
    public String getSubjectId1() {return subjectId1;}
    public String getSubjectId2 () {return subjectId2;}
    public String getSubjectId3 () {return subjectId3;}
    
    // set info
    public void setLevel(String newLevel) {this.level = newLevel;}
    public void setSubjectId1(String newSubjectId) {this.subjectId1 = newSubjectId;}
    public void setSubjectId2(String newSubjectId) {this.subjectId2 = newSubjectId;}
    public void setSubjectId3(String newSubjectId) {this.subjectId3 = newSubjectId;}
}
