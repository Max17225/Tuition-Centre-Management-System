/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;

public class Student extends User{
    
    private String address;
    final private String enrollmentId;

    private String icPassport;
    
    //constructor
    public Student(String id, String username, String password, String icPassport, String phoneNumber) {
        super(id, username, password, phoneNumber); 
        this.address = "Empty";
        this.enrollmentId = "Empty";
        this.icPassport = icPassport;
    }
    
    public Student(String id, String username, String password, String enrollmentId, String icPassport, String phoneNumber, String email, String address) {
    super(id, username, password, phoneNumber);
    this.email = email;
    this.address = address;
    this.enrollmentId = enrollmentId;
    this.icPassport = icPassport;
    }   
    
    // Get info
    public String getEnrollmentId() {return this.enrollmentId;}
    public String getIcPassport() {return this.icPassport;}
    public String getAddress() {return this.address;}
    
    // Set info
     public void setIcPassport(String newIcPassport) {
        if (newIcPassport == null || newIcPassport.trim().isEmpty()) {
            throw new IllegalArgumentException("IC/Passport cannot be empty or null.");
        }
        this.icPassport = newIcPassport.trim();
    }
        
     public void setAddress(String newAddress) {
        if (newAddress == null || newAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty or null.");
        }
        this.address = newAddress.trim();
     }
     
    @Override
    public String toDataLine() {
        return String.join(",", id, username, password, enrollmentId, icPassport, phoneNumber, email, address);
    }
}

