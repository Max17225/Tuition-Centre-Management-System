/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;

/**
 *
 * @author nengz
 */

// Abstract class for inheritance

public abstract class User implements DataSerializable {
    final protected String id;
    protected String username;
    protected String password;
    protected String phoneNumber;
    protected String email;
    protected String country;

    // Constructor 
    public User(String id, String username, String password, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        // email and country will default empty, user can update it by themself
        this.email = "Empty";
        this.country = "Empty";
    }

    // GetInfo
    @Override
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getCountry() { return country; }

    // SetInfo
    public void setUsername(String newUsername) { this.username = newUsername; }
    public void setPassword(String newPassword) { this.password = newPassword; }
    public void setPhoneNumber(String newPhoneNumber) { this.phoneNumber = newPhoneNumber; }
    public void setEmail(String newEmail) { this.email = newEmail; }
    public void setCountry(String newCountry) { this.country = newCountry; }
}
