/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a5_task2;
import java.sql.*;
import java.util.ArrayList;
import static a5_task2.A5_Task2.*;

/**
 * This class deals with the database and operations on it,
 * derbyConnectAndCreate() method connects to the database, creates a table and adds data to the table
 * printSQLException() is to print details of an SQLException chain, Details included are SQL State, Error code, Exception message, 
 * runQuery() is used to run the query on the Sailor table and
 * closeTheResources() is used to close the statement and the connection to the Derby database.
 * @author VinayaSaiD
 */
public class NetworkDerby
{   /* the framework used is network */
    public static String framework = "derbyclient";
    public static String protocol = "jdbc:derby://localhost:1527/";

    public void derbyConnectAndCreate(ArrayList<String> textFileList)
    {   System.out.println("SimpleApp starting in " + framework + " mode");

        String dbName = "EmployeeDB"; // the name of the database
        //String url = protocol + dbName;
        String url = protocol + dbName + ";create=true";
        String username = "admin1";
        String password = "admin123";
        try
        {   
            conn =  DriverManager.getConnection (url, username, password);
            s = conn.createStatement();
            System.out.println("Connected to database " + dbName);

            // We want to control transactions manually. Autocommit is on by
            // default in JDBC.
            conn.setAutoCommit(false);
            
            DatabaseMetaData dbm = conn.getMetaData();
            // check if "employee" table is there, if not create the table and add the data from the text file
            ResultSet tables = dbm.getTables(null, null, "SAILOR", null);
            if (!tables.next()) 
            {
                s.executeUpdate("create table Sailor(LastName varchar(50), FirstName varchar(50), Position varchar(40), Salary int)");
                System.out.println("Created table Sailor");
                 // add rows to the created table using a PreparedStatement 
                PreparedStatement psInsert = conn.prepareStatement("insert into Sailor values (?, ?, ?, ?)");
                String eachline;
                String[] tempArray;
                for (int i=0;i< (textFileList.size());i++)
                {   // nserting the data from the text file in the Array list into the Table
                    eachline = textFileList.get(i);
                    tempArray = eachline.split(",");
                    double salary = Double.parseDouble(tempArray[3]);
                    psInsert.setString(1, tempArray[0]);
                    psInsert.setString(2, tempArray[1]);
                    psInsert.setString(3, tempArray[2]);
                    psInsert.setDouble(4, salary);
                    psInsert.executeUpdate();

                }
                System.out.println("Inserted all Employee details from Text File.\n ");
                // Comminting the changes into the database
                conn.commit();
                // closing the prepared statement created before
                try {
                    if (psInsert != null) 
                    {   psInsert.close();
                        psInsert = null;
                    }
                } 
                catch (SQLException sqle) 
                {   printSQLException(sqle);
                }
            }
            else
            {
                System.out.println("Table already exists");
            }
        }
        catch (SQLException sqle)
        {   printSQLException(sqle);
        } 
    }

    public static void printSQLException(SQLException e)
    {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (e != null)
        {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }
    
    public void closeTheResources()
    {
        try
        {   DriverManager.getConnection("jdbc:derby:;shutdown=true");
        }
        catch (SQLException se)
        {   if (( (se.getErrorCode() == 50000) && ("XJ015".equals(se.getSQLState()) ))) 
            {   // we got the expected exception
                System.out.println("Derby shut down normally");
            } 
            else 
            {   // if the error code or SQLState is different, we have
                // an unexpected exception (shutdown failed)
                System.err.println("Derby did not shut down normally");
                printSQLException(se);
            }
        }
        try 
        {   // close the statement 
            if (s != null) {
                s.close();
                s = null;
            }
        } 
        catch (SQLException sqle) 
        {   printSQLException(sqle);
        }
        try 
        {   if (conn != null) 
            {
                conn.commit();
                conn.close();
                conn = null;
            }
        }
        catch (SQLException sqle) 
        {   printSQLException(sqle);
        }
    }
    
    public static ResultSet runQuery(String query)
    {   ResultSet rs = null;
        try
        {   // running the query that is sent on the Quiz Table
            rs = s.executeQuery(query);
            return rs;
        }
        catch (SQLException sqle)
        {   printSQLException(sqle);
        } 
        return rs;
    }
}