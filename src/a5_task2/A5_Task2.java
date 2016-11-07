/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a5_task2;
import static a5_task2.ReadFromFile.*;
import static a5_task2.NetworkDerby.*;
import static a5_task2.PrintMenu.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;
/**
 * This class contains the main() method and calls 3 methods,
 * readFile() to read the data present in the text file,
 * derbyConnectAndCreate() to connect to the database, create a table if not present and store the data in the table from the text file and
 * printMenu() used the print the interactive menu to the user.
 * @author VinayaSaiD
 */
public class A5_Task2 {
    static ArrayList<String> linesFromTextFile = new ArrayList<String>();
    public static Connection conn;
    public static Statement s;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception
    {   // read the line from the text file
        linesFromTextFile = readFile("EmployeeList.txt");
        NetworkDerby database = new NetworkDerby();
        // Connect to the database
        database.derbyConnectAndCreate(linesFromTextFile);
        printMenu();
    }
}
