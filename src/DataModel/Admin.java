/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;

public class Admin extends User {
    
    //constructor
    public Admin(String id, String username, String password, String phoneNumber) {
        super(id, username, password, phoneNumber); 
    }

    public Admin(String id, String username, String password, String phoneNumber, String country, String email) {
    super(id, username, password, phoneNumber);
    this.country = country;
    this.email = email;
    }
    
    // To data line for text file saving
    @Override
    public String toDataLine() {
        return String.join(",", id, username, password, phoneNumber, country, email);
    }
}