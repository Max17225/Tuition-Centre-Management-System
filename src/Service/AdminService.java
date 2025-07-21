package Service;
import java.util.*;
import DataModel.*;
import Util.*;
import java.time.LocalDate;


public class AdminService {
    
    // ----------------------- TUTOR MANAGEMENT ------------------------
    // Use to register new Tutor
    // return(bool): return true if success 
    public static boolean registerTutor(String username, String password, String phoneNumber, String email, String country) {
        if (!InputValidator.emailFormatIsValid(email)) return false;

        String newId = IdGenerator.getNewId(Tutor.class);
        Tutor newTutor = new Tutor(newId, username, password, phoneNumber, email, country);

        DataManager<Tutor> tutorManager = DataManager.of(Tutor.class);
        tutorManager.appendOne(newTutor);
 
        return true;
    }
    
    // Use to delete tutor
    // return(bool): return true if success
    public static boolean deleteTutor(String tutorId) {
        return DataManager.of(Tutor.class).deleteById(tutorId);
    }
    
    // Assign subject for tutor
    // return(bool): return true if success
    public static boolean assignSubjectToTutor(String tutorId, String subjectName, String level, String fee) {
        DataManager<Subject> subjectManager = DataManager.of(Subject.class);

        // 3. Assign new subject to tutor
        String subjectId = IdGenerator.getNewId(Subject.class);
        Subject newSubject = new Subject(subjectId, subjectName, level, tutorId, fee);
        subjectManager.appendOne(newSubject);

        return true;
    }
    
    // ----------------------- RECEPTIONIST MANAGEMENT ------------------------

    // Registers a new Receptionist if the email format is valid
    public static boolean registerReceptionist(String username, String password, String phoneNumber, String email, String country) {
        if (!InputValidator.emailFormatIsValid(email)) return false; // Validate email format

        String newId = IdGenerator.getNewId(Receptionist.class); // Generate new unique ID
        Receptionist newReceptionist = new Receptionist(newId, username, password, phoneNumber, email, country);

        DataManager<Receptionist> receptionistManager = DataManager.of(Receptionist.class);
        receptionistManager.appendOne(newReceptionist); // Append to file
        return true;
    }

    // Deletes a receptionist by ID and updates the file
    public static boolean deleteReceptionist(String receptionistId) {
        return DataManager.of(Receptionist.class).deleteById(receptionistId);
    }

// ----------------------- INCOME REPORTING ------------------------

    // Returns total income for a specific month and year
    public static int getMonthlyIncome(int year, int month) {
        DataManager<Payment> paymentManager = DataManager.of(Payment.class);
        List<Payment> payments = paymentManager.readFromFile();

        int total = 0;
        for (Payment payment : payments) {
            try {
                LocalDate date = LocalDate.parse(payment.getPaymentDate()); // Parse payment date (e.g., "2025-07-01")
                if (date.getYear() == year && date.getMonthValue() == month) {
                    total += Integer.parseInt(payment.getAmount()); // Add amount if date matches
                }
            } catch (Exception e) {
                System.err.println("Invalid payment entry: " + payment.toDataLine()); // Handle malformed entries
            }
        }
        return total;
    }

// ----------------------- ADMIN PROFILE UPDATE ------------------------

    // Updates admin profile based on ID, returns false if not found
    public static boolean updateAdminProfile(String id, String name, String phoneNumber, String email, String country) {
        Admin targetAdmin = DataManager.of(Admin.class).getRecordById(id); // Look up admin by ID

        if (targetAdmin == null) {
            System.err.println("Admin ID Not Found");
            return false;
        } else {
            // Update fields
            targetAdmin.setUsername(name);
            targetAdmin.setPhoneNumber(phoneNumber);
            targetAdmin.setEmail(email);
            targetAdmin.setCountry(country);

            DataManager.of(Admin.class).updateRecord(targetAdmin); // Save updated admin
            return true;
        }
    }
}

