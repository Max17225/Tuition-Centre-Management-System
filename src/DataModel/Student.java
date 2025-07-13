/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;

public class Student extends User implements DataSerializable {
    
    private String address;
    final private String enrollmentId;
    private String icPassport;  
    
    
    //constructor
    public Student(String id, String username, String password, String phoneNumber, String icPassport) {
        super(id, username, password, phoneNumber);
        this.address = "Empty";
        this.enrollmentId = "Empty";
        this.icPassport = icPassport;
    }
    
    public Student(String id, String username, String password, String phoneNumber, String country, String email ,String address, String enrollmentId, String icPassport) {
    super(id, username, password, phoneNumber);
    this.country = country;
    this.email = email;
    this.address = address;
    this.enrollmentId = enrollmentId;
    this.icPassport = icPassport;
    }   
    
    // Get info
    public String getEnrollmentId() {return this.enrollmentId;}

    @Override
    public String toDataLine() {
        return String.join(",", id, username, password, phoneNumber, country, email, address, enrollmentId, icPassport);
    }
}

