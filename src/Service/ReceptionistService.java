package Service;

import DataModel.Enrollment;
import DataModel.Student;
import Util.FileHandler;

public class ReceptionistService {

    // Old method (optional, still works if needed)
    public static void registerStudent(Student student, Enrollment enrollment) {
        FileHandler.write("Student.txt", student.toDataLine());
        FileHandler.write("Enrollment.txt", enrollment.toDataLine());
    }

    // ✅ New method – only saves Enrollment to Enrollment.txt
    public static void registerEnrollmentOnly(Enrollment enrollment) {
        FileHandler.write("Enrollment.txt", enrollment.toDataLine());
    }
}
