/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;

/**
 *
 * @author nengz
 */

public class Tutor extends User {
    public Tutor(String id, String username, String password, String phoneNumber) {
        super(id, username, password, phoneNumber); 
    }
    
    public Tutor(String id, String username, String password, String phoneNumber, String email, String country) {
    super(id, username, password, phoneNumber);
    this.country = country;
    this.email = email;
    }
    
    @Override
    public String toDataLine() {
        return String.join(",", id, username, password, phoneNumber, country, email);
    }  
}