/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Util.FileManager;
import java.util.List;

public class Main {
    public static void main(String[] Args) {
        List<List<String>> data = FileManager.getDataList("Admin.txt");
        System.out.println(data);
    }
}
