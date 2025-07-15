package Service;
import java.util.*;
import java.util.stream.Collectors;
import DataModel.*;
import Util.*;
public class AdminService {
    // ----------------------- TUTOR MANAGEMENT ------------------------
    public static boolean registerTutor(String name, String username, String password, String email, String contact) {
        if (!InputValidator.emailFormatIsValid(email)) return false;

        String newId = IdGenerator.getNewId(Tutor.class);
        Tutor newTutor = new Tutor(newId, name, username, password, email, contact);

        DataManager<Tutor> tutorManager = DataManager.of(Tutor.class);
        tutorManager.appendOne(newTutor);
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
    public static Map<String, Double> getMonthlyIncomeReportByLevel(String month) {
        Map<String, Double> incomeByLevel = new HashMap<>();

        List<Payment> payments = DataManager.of(Payment.class).readFromFile();
        List<Subject> subjects = DataManager.of(Subject.class).readFromFile();
        List<Enrollment> enrollments = DataManager.of(Enrollment.class).readFromFile();

        Map<String, Subject> subjectMap = subjects.stream()
            .collect(Collectors.toMap(Subject::getId, s -> s));

        for (Payment payment : payments) {
            if (!payment.getMonth().equalsIgnoreCase(month)) continue;

            Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getId().equals(payment.getEnrollmentId()))
                .findFirst().orElse(null);
            if (enrollment == null) continue;

            String[] subjectIds = { enrollment.getSubjectId1(), enrollment.getSubjectId2(), enrollment.getSubjectId3() };

            for (String subjectId : subjectIds) {
                if (subjectId == null || subjectId.isBlank()) continue;
                Subject subject = subjectMap.get(subjectId);
                if (subject == null) continue;

                String level = subject.getLevel();
                double fee = Double.parseDouble(subject.getFeePerMonth());

                incomeByLevel.put(level, incomeByLevel.getOrDefault(level, 0.0) + fee);
            }
        }

        return incomeByLevel;
    }
    
    public static Map<String, Double> getMonthlyIncomeReportBySubject(String month) {
        Map<String, Double> incomeBySubject = new HashMap<>();

        List<Payment> payments = DataManager.of(Payment.class).readFromFile();
        List<Subject> subjects = DataManager.of(Subject.class).readFromFile();
        List<Enrollment> enrollments = DataManager.of(Enrollment.class).readFromFile();

        Map<String, Subject> subjectMap = subjects.stream()
            .collect(Collectors.toMap(Subject::getId, s -> s));

        for (Payment payment : payments) {
            if (!payment.getMonth().equalsIgnoreCase(month)) continue;

            Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getId().equals(payment.getEnrollmentId()))
                .findFirst().orElse(null);
            if (enrollment == null) continue;

            String[] subjectIds = { enrollment.getSubjectId1(), enrollment.getSubjectId2(), enrollment.getSubjectId3() };

            for (String subjectId : subjectIds) {
                if (subjectId == null || subjectId.isBlank()) continue;
                Subject subject = subjectMap.get(subjectId);
                if (subject == null) continue;

                String subjectName = subject.getSubjectName();
                double fee = Double.parseDouble(subject.getFeePerMonth());

                incomeBySubject.put(subjectName, incomeBySubject.getOrDefault(subjectName, 0.0) + fee);
            }
        }

        return incomeBySubject;
    }
    
    // ----------------------- ADMIN PROFILE UPDATE ------------------------
    public static boolean updateAdminProfile(String adminId, String name, String email, String contact) {
        if (!InputValidator.emailFormatIsValid(email)) return false;

        DataManager<Admin> adminManager = DataManager.of(Admin.class);
        List<Admin> allAdmins = adminManager.readFromFile();

        for (Admin admin : allAdmins) {
            if (admin.getId().equals(adminId)) {
                admin.setName(name);
                admin.setEmail(email);
                admin.setContact(contact);
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
