/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a5_task2;

import static a5_task2.NetworkDerby.printSQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import static a5_task2.A5_Task2.*;

/**
 * This class is used to print the interactive menu on the console and accepts the input from the user,
 * based on hi choice calls different methods to perform various operations.
 * @author VinayaSaiD
 */
public class PrintMenu {
    public static void printMenu() throws Exception
    {
        // to print the interactive menu for user
        boolean wronginput = true;
        Scanner input = new Scanner(System.in); 
        int optionChoosen = 0;
        while(wronginput == true)
        {
            // Print the options on the console for the user to select from
            System.out.println("\n\n\n\n");
            System.out.println("Please select your option from below:");
            System.out.println("1. Top-earning sailors in each position ");
            System.out.println("2. The sailors earning the least, highest and above average salaries");
            System.out.println("3. A list of Sailors whose Lastname starts with an alphabet and earn more than certain Salary");
            System.out.println("4. Exit the System");
            optionChoosen = input.nextInt();
            if (optionChoosen == 1 || optionChoosen  == 2 || optionChoosen == 3 || optionChoosen == 4 )
            {   wronginput = false;
            }
            else
            {   System.out.println("\nWrong input entered \n");
            }
        }
        // based on the option selected using the switch case to call the respective classes
        switch (optionChoosen)
        {
            case 1 : PerformOperations.topEarnings();System.out.println("\n\n\n"); printMenu();
            case 2 : PerformOperations.extremeSailors() ; System.out.println("\n\n\n"); printMenu();
            case 3 : PerformOperations.nameAndEarning() ; System.out.println("\n\n\n"); printMenu();
            case 4 :
            {   // close all the resources opened
                System.out.println("Closing the Connection to the database.");
                NetworkDerby db = new NetworkDerby();
                db.closeTheResources();
                System.exit(0);
                break;
            }
            default: 
            {   // close all the resources opened
                System.out.println("Closing the Connection to the database.");
                NetworkDerby db = new NetworkDerby();
                db.closeTheResources();
                System.exit(0);
            }
            
        }

    }
}
