/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataModel;

/**
 *
 * @author nengz
 */

// A interface used for make sure everything in data model can convert to a String line and save into text file.

public interface DataSerializable {
    // this both method can be use for every data model class
    String toDataLine();
    String getId();
}
