package DataModel;

public class Receptionist extends User {

    public Receptionist(String id, String username, String password, String phoneNumber) {
        super(id, username, password, phoneNumber);
    }

    public Receptionist(String id, String username, String password, String phoneNumber, String email, String country) {
        super(id, username, password, phoneNumber);
        this.country = country;
        this.email = email;
    }

    // ✅ This constructor is needed for `Receptionist::new`
    public Receptionist(String[] parts) {
        super(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
        this.country = parts.length > 4 ? parts[4].trim() : "Empty";
        this.email = parts.length > 5 ? parts[5].trim() : "Empty";
    }

    @Override
    public String toDataLine() {
        return String.join(",", id, username, password, phoneNumber, country, email);
    }
}
