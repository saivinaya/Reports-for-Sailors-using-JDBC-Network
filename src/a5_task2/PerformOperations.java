/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a5_task2;

import java.sql.*;
import java.util.ArrayList;
import static a5_task2.NetworkDerby.*;
import static a5_task2.PrintMenu.printMenu;
import static a5_task2.A5_Task2.*;
import java.util.Scanner;

/**
 * This file is used to perform various operations on the Sailors Table created,
 * topEarnings() is used to get the people who are earning the highest salary in each position,
 * nameAndEarning() is used to get the details of people whose salary is greater than entered value and whose last name starts with the alphabet entered,
 * extremeSailors() is used to get the lowest paid, highest paid and all the sailors above the average salary and 
 * toPrintLines() is used to print the details in the result set based on certain criteria.
 * @author VinayaSaiD
 */
public class PerformOperations {
    
    public static void topEarnings()
    {
        ResultSet rs = null;
        try
        {
            String query = "SELECT * FROM Sailor WHERE Position = ? ORDER BY Salary DESC";
            PreparedStatement pStmt = conn.prepareStatement(query);
            String[] positionsList = {"Engineer","Captain","Cook","Mechanic"};
            for(String i : positionsList)
            {
                pStmt.setString(1, i);
                rs = pStmt.executeQuery();
                System.out.println("\nHighest paid person for position as " + i);
                System.out.format("\n%-20s %-15s %-15s\n","Employee Name", "Position", "Salary");
                double maxSalary = 0.0;
                while (rs.next()) {
                  if (maxSalary < rs.getDouble("Salary"))
                  { maxSalary = rs.getDouble("Salary");
                  }
                }
                rs = pStmt.executeQuery();
                toPrintLines(rs,maxSalary);
            }
        }
        catch (SQLException sqle)
        {
            printSQLException(sqle);
        } 
        finally {
            try 
            {   if (rs != null) 
                {   rs.close();
                    rs = null;
                }
            } 
            catch (SQLException sqle) 
            {   printSQLException(sqle);
            }
        }
    }
    
    public static void nameAndEarning()
    {
        // Take the salary above whih the records need to be displayed
        boolean wronginput = true;
        Scanner input = new Scanner(System.in); 
        int SalaryLevel = 0;
        while(wronginput == true)
        {
            System.out.println("Please Enter the salary above which you want the records:");
            SalaryLevel = input.nextInt();
            if (SalaryLevel > 30000 && SalaryLevel < 100000 )
            {   wronginput = false;
            }
            else
            {   System.out.println("\nWrong input entered. Salaries are between 40000 and 100000.");
            }
        }
        
        // take the Alphabet for which the last name should start with
        boolean wronginput1 = true;
        Scanner input1 = new Scanner(System.in); 
        String startingChar="";
        while(wronginput1 == true)
        {
            System.out.println("Please Enter the Starting Alphabet for which you want the records:");
            startingChar = input1.next();
            if (startingChar.length() == 1 )
            {   wronginput1 = false;
            }
            else
            {   System.out.println("\nWrong input entered. There should be only one character entered.");
            }
        }
        
        ResultSet rs = null;
        try
        {
            String query = "SELECT * FROM Sailor WHERE Salary > ? ORDER BY Salary ASC ";
            PreparedStatement pStmt = conn.prepareStatement(query);
            pStmt.setInt(1, SalaryLevel);
            rs = pStmt.executeQuery();
            
            int counter = 0;
            while (rs.next()) {
                String first = rs.getString("FirstName");
                String last = rs.getString("LastName");
                String position = rs.getString("Position");
                double salary = rs.getDouble("Salary");
                // Print only the ones whose last name starts with the alphabet entered
                if (last.startsWith(startingChar.toUpperCase()))
                {
                    if (counter == 0)
                    {
                        System.out.format("\n\n\n%-20s %-15s %-15s\n","Employee Name", "Position", "Salary");
                        counter = counter + 1;
                    }
                System.out.format("%-20s %-15s %-15.2f\n", (first +" " +last),position,salary);
                }
          } // end of while
           if (counter == 0)
           {
               System.out.println("There are no records matching this criteria");
           }
         }
        catch (SQLException sqle)
        {
            printSQLException(sqle);
        } 
        finally {
            try 
            {   if (rs != null) 
                {   rs.close();
                    rs = null;
                }
            } 
            catch (SQLException sqle) 
            {   printSQLException(sqle);
            }
        }
    }
    
    public static void extremeSailors()
    {
        ResultSet rs = null;
        try
        {
            rs = runQuery("SELECT * FROM Sailor ORDER BY Salary DESC");
            System.out.println("Sailor with Highest Salary");
            System.out.format("%-20s %-15s %-15s\n","Employee Name", "Position", "Salary");
            double highSalary = 0.0;
            while (rs.next()) {
                  if (highSalary < rs.getDouble("Salary"))
                  { highSalary = rs.getDouble("Salary");
                  }
                }
            rs = runQuery("SELECT * FROM Sailor ORDER BY Salary DESC");
            toPrintLines(rs,highSalary);
            System.out.println("");
            rs = runQuery("SELECT * FROM Sailor ORDER BY Salary ASC");
            double lowSalary = 0.0;
            int a = 0;
            while (rs.next()) 
            {   if (a==0)
                {   lowSalary = rs.getDouble("Salary");
                    a = 1;
                }
                else
                {   if (lowSalary > rs.getDouble("Salary"))
                    { lowSalary = rs.getDouble("Salary");
                    }
                }
            }
            System.out.println("Sailor with Lowest Salary");
            System.out.format("%-20s %-15s %-15s\n","Employee Name", "Position", "Salary");
            rs = runQuery("SELECT * FROM Sailor ORDER BY Salary ASC");
            toPrintLines(rs,lowSalary);
            System.out.println("");
            rs = runQuery("SELECT * from Sailor  where Salary > (select avg(Salary) from Sailor) ORDER BY Salary DESC");
            System.out.println("Sailors with Salary above Average");
            System.out.format("\n\n%-20s %-15s %-15s\n","Employee Name", "Position", "Salary");
            while (rs.next()) {
                String first = rs.getString("FirstName");
                String last = rs.getString("LastName");
                String position = rs.getString("Position");
                double salary = rs.getDouble("Salary");
                System.out.format("%-20s %-15s %-15.2f\n", (first +" " +last),position,salary);
          } // end of while
            }
        catch (SQLException sqle)
        {
            printSQLException(sqle);
        } 
        finally {
            try 
            {   if (rs != null) 
                {   rs.close();
                    rs = null;
                }
            } 
            catch (SQLException sqle) 
            {   printSQLException(sqle);
            }
        }
    }
    
    public static void toPrintLines(ResultSet result1, double compareSalary)
    {
        try
        {   while (result1.next()) {
                String first = result1.getString("FirstName");
                String last = result1.getString("LastName");
                String position = result1.getString("Position");
                double salary = result1.getDouble("Salary");
                if (compareSalary == salary)
                {   System.out.format("%-20s %-15s %-15.2f\n", (first +" " +last),position,salary);
                }
            } // end of while
        }
        catch (SQLException sqle)
        {   printSQLException(sqle);
        } 
    }
}
