package Service;

import Util.DataManager;
import DataModel.Enrollment;

public class ReceptionistService {
    
    // Register Enrollment Only
    // param(enrollment): The new enrollment object
    public static void registerEnrollmentOnly(Enrollment enrollment) {
        DataManager<Enrollment> manager = DataManager.of(Enrollment.class);
        manager.appendOne(enrollment);
    }
    
    // Register Student Only
    // param(newStudent): The new Student object
    public static void registerStudentOnly(DataModel.Student newStudent) {
        DataManager.of(DataModel.Student.class).appendOne(newStudent);
    }
}
