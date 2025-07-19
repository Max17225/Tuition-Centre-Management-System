/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;

/**
 *
 * @author nengz
 */
public class ClassSchedule implements DataSerializable {
    final private String scheduleId;
    final private String subjectId;
    private String scheduleInWeek;
    
    //constructor
    public ClassSchedule(String scheduleId, String subjectId, String scheduleInWeek) {
        this.scheduleId = scheduleId;
        this.subjectId = subjectId;
        this.scheduleInWeek = scheduleInWeek;
    }
    
    //get info
    @Override
    public String getId() {return this.scheduleId;}
    public String getSubjectId() {return this.subjectId;}
    public String getScheduleInWeek() {return this.scheduleInWeek;}
    
    //set info
    public void setScheduleInWeek(String newScheduleInWeek) {this.scheduleInWeek = newScheduleInWeek;}
    
    
    @Override
    public String toDataLine() {
        return String.join(",", scheduleId, subjectId, scheduleInWeek);
    }   
}
