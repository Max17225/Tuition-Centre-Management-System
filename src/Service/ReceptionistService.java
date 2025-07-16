package Service;

import Util.DataManager;
import DataModel.Enrollment;

public class ReceptionistService {

    public static void registerEnrollmentOnly(Enrollment enrollment) {
        DataManager<Enrollment> manager = DataManager.of(Enrollment.class);
        manager.appendOne(enrollment);
    }
}
