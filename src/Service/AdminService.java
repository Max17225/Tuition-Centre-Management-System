package Service;
import java.util.*;
import DataModel.*;
import Util.*;
import java.time.LocalDate;
public class AdminService {
    // ----------------------- TUTOR MANAGEMENT ------------------------
    public static boolean registerTutor(String username, String password, String email, String contact, String subjectName, String level, String fee) {
        if (!InputValidator.emailFormatIsValid(email)) return false;

        String newId = IdGenerator.getNewId(Tutor.class);
        Tutor newTutor = new Tutor(newId, "", username, password, email, contact);

        DataManager<Tutor> tutorManager = DataManager.of(Tutor.class);
        tutorManager.appendOne(newTutor);
        
        if (subjectName != null && !subjectName.isEmpty() &&
            level != null && !level.isEmpty() &&
            fee != null && !fee.isEmpty()) {
            
            // Assign subject only if values are provided
            String subjectId = IdGenerator.getNewId(Subject.class);
            Subject subject = new Subject(subjectId, subjectName, level, newId, fee);

            DataManager<Subject> subjectManager = DataManager.of(Subject.class);
            subjectManager.appendOne(subject);
        }
        return true;
    }
    
    public static boolean deleteTutor(String tutorId) {
        DataManager<Tutor> tutorManager = DataManager.of(Tutor.class);
        List<Tutor> allTutors = tutorManager.readFromFile();
        boolean removed = allTutors.removeIf(t -> t.getId().equals(tutorId));

        if (removed) {
            tutorManager.overwriteFile(allTutors);
        }

        return removed;
    }
    
    public static boolean assignTutorToSubject(String tutorId, String subjectName, String level, String fee) {
        String subjectId = IdGenerator.getNewId(Subject.class);
        Subject newSubject = new Subject(subjectId, tutorId, subjectName, level, fee);

        DataManager<Subject> subjectManager = DataManager.of(Subject.class);
        subjectManager.appendOne(newSubject);
        return true;
    }
    
     // ----------------------- RECEPTIONIST MANAGEMENT ------------------------
    public static boolean registerReceptionist(String name, String username, String password, String email, String contact) {
        if (!InputValidator.emailFormatIsValid(email)) return false;

        String newId = IdGenerator.getNewId(Receptionist.class);
        Receptionist newReceptionist = new Receptionist(newId, name, username, password, email, contact);

        DataManager<Receptionist> receptionistManager = DataManager.of(Receptionist.class);
        receptionistManager.appendOne(newReceptionist);
        return true;
    }
    
    public static boolean deleteReceptionist(String receptionistId) {
        DataManager<Receptionist> receptionistManager = DataManager.of(Receptionist.class);
        List<Receptionist> allReceptionists = receptionistManager.readFromFile();
        boolean removed = allReceptionists.removeIf(r -> r.getId().equals(receptionistId));

        if (removed) {
            receptionistManager.overwriteFile(allReceptionists);
        }

        return removed;
    }
    
    // ----------------------- INCOME REPORTING ------------------------
    public static int getMonthlyProfit(int year, int month) {
        DataManager<Payment> paymentManager = DataManager.of(Payment.class);
        List<Payment> payments = paymentManager.readFromFile();

        int total = 0;
        for (Payment payment : payments) {
            try {
                LocalDate date = LocalDate.parse(payment.getPaymentDate()); // e.g., "2025-07-01"
                if (date.getYear() == year && date.getMonthValue() == month) {
                    total += Integer.parseInt(payment.getAmount());
                }
            } catch (Exception e) {
                System.err.println("Invalid payment entry: " + payment.toDataLine());
            }
        }
        return total;
}


    // ----------------------- ADMIN PROFILE UPDATE ------------------------
    public static boolean updateAdminProfile(String adminId, String name, String email, String contact) {
        if (!InputValidator.emailFormatIsValid(email)) return false;

        DataManager<Admin> adminManager = DataManager.of(Admin.class);
        List<Admin> allAdmins = adminManager.readFromFile();

        for (Admin admin : allAdmins) {
            if (admin.getId().equals(adminId)) {
                admin.setUsername(name);
                admin.setEmail(email);
                admin.setPhoneNumber(contact);
                adminManager.overwriteFile(allAdmins);
                return true;
            }
        }

        return false;
    }
    
    // ----------------------- GETTERS / UTILITY ------------------------
    public static List<Tutor> getAllTutors() {
        return DataManager.of(Tutor.class).readFromFile();
    }

    public static List<Receptionist> getAllReceptionists() {
        return DataManager.of(Receptionist.class).readFromFile();
    }
    
    public static List<Subject> getAllSubjects() {
        return DataManager.of(Subject.class).readFromFile();
    }

    public static Tutor getTutorById(String tutorId) {
        return getAllTutors().stream()
            .filter(t -> t.getId().equals(tutorId))
            .findFirst().orElse(null);
    }

    public static Receptionist getReceptionistById(String id) {
        return getAllReceptionists().stream()
            .filter(r -> r.getId().equals(id))
            .findFirst().orElse(null);
    }

}
