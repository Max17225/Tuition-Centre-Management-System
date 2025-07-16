/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;

public class Receptionist extends User implements DataSerializable {
    private String country;
    private String email;

    // Constructor 1: Basic info only
    public Receptionist(String id, String username, String password, String phoneNumber) {
        super(id, username, password, phoneNumber); 
    }

    // Constructor 2: With extra fields (country + email)
    public Receptionist(String id, String username, String password, String phoneNumber, String country, String email) {
        super(id, username, password, phoneNumber);
        this.country = country;
        this.email = email;
    }

    // Optional: Getters and setters
    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toDataLine() {
        // Format: R001,username,password,phone,country,email
        return String.join(",", 
            getId(), 
            getUsername(), 
            getPassword(), 
            getPhoneNumber(), 
            country != null ? country : "", 
            email != null ? email : ""
        );
    }
}
