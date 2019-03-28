package minesweeper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    public DBConnection() {
        // Step 1: Loading or registering Oracle JDBC driver class
        try {

            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        } catch (ClassNotFoundException cnfex) {

            System.out.println("Problem in loading or " + "registering MS Access JDBC driver");
            cnfex.printStackTrace();
        }

    }

    public Connection connection() {
        try {

            String msAccDB = System.getProperty("user.dir") + "//DB//db.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB;

            // Step 2.A: Create and get connection using DriverManager class
            return DriverManager.getConnection(dbURL);

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            return null;
        }
    }

    public boolean isSaved() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {

            connection = connection();
            if (connection != null) {
                // Step 2.B: Creating JDBC Statement 
                statement = connection.createStatement();

                // Step 2.C: Executing SQL & retrieve data into ResultSet
                resultSet = statement.executeQuery("SELECT * FROM Mines");
            }

            // processing returned data and printing into console
            while (resultSet.next()) {

                System.out.println(""+resultSet.getString(4));
                String a=resultSet.getString(4);
                System.out.println(a);
                if ("x".equals(a)) {
                    resultSet.close();
                    statement.close();
                    connection.close();
                    return false;
                } else {
                    resultSet.close();
                    statement.close();

                    // and then finally close connection
                    connection.close();
                    return true;
                }
            }
            return false;

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            return false;

        } 
        
    }

    public void getCells(Cell c[][], int rows, int cols) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet=null;
       // Cell c1[][]=new Cell[rows][cols];
        try {

            connection = connection();
            if (connection != null) {
                // Step 2.B: Creating JDBC Statement 
                statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM Cell");
                // Step 2.C: Executing SQL & retrieve data into ResultSet
                while (resultSet.next()) {

                    System.out.println("2:"+resultSet.getInt(2));
                    System.out.println("2:"+resultSet.getInt(3));
                    System.out.println("2:"+resultSet.getInt(4));
                    System.out.println("2:"+resultSet.getBoolean(5));
                    System.out.println("2:"+resultSet.getBoolean(6));
                    System.out.println("2:"+resultSet.getBoolean(7));
                    Cell cell=new Cell();
                    
                    cell.loadCell(resultSet.getInt(2),resultSet.getInt(3),resultSet.getInt(4),
                            resultSet.getBoolean(5), resultSet.getBoolean(6),resultSet.getBoolean(7));
                
                    c[resultSet.getInt(2)][resultSet.getInt(3)]=cell;
                }
				//c=c1;

            }

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();

        } finally {

            // Step 3: Closing database connection
            try {
                if (null != connection) {

                    // cleanup resources, once after processing
                    statement.close();

                    // and then finally close connection
                    connection.close();
                }
            } catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }
    }

    public void setCells(Cell c[][], int rows, int cols) {
        Connection connection = null;
        Statement statement = null;
        try {

            connection = connection();
            if (connection != null) {
                // Step 2.B: Creating JDBC Statement 
                statement = connection.createStatement();

                // Step 2.C: Executing SQL & retrieve data into ResultSet
                statement.executeUpdate("DELETE * FROM Cell");

                for (int i = 0; i < rows; i++) {

                    for (int j = 0; j < cols; j++) {
                        // System.out.println(""+i+":"+j+":"+":"+s[i][j]); 
                        statement.executeUpdate("INSERT INTO Cell(Row,Col,val,isOpened,hasBomb,hasFlag) "
                                + "VALUES(" + i + "," + j + "," + c[i][j].getValue() + "," + c[i][j].isOpened + "," + c[i][j].hasBomb + "," + c[i][j].hasFlag + ")");
                    }

                }
            }

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();

        } finally {

            // Step 3: Closing database connection
            try {
                if (null != connection) {

                    // cleanup resources, once after processing
                    statement.close();

                    // and then finally close connection
                    connection.close();
                }
            } catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }
    }

    public void getMines(String s[][], int rows, int cols) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {

            connection = connection();
            if (connection != null) {
                // Step 2.B: Creating JDBC Statement 
                statement = connection.createStatement();

                // Step 2.C: Executing SQL & retrieve data into ResultSet
                resultSet = statement.executeQuery("SELECT * FROM Mines");
            }

            // processing returned data and printing into console
            while (resultSet.next()) {

                s[resultSet.getInt(2)][resultSet.getInt(3)] = resultSet.getString(4);
            }

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();

        } finally {

            // Step 3: Closing database connection
            try {
                if (null != connection) {

                    // cleanup resources, once after processing
                    resultSet.close();
                    statement.close();

                    // and then finally close connection
                    connection.close();
                }
            } catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }
    }

    public void setMines(String s[][], int rows, int cols) {
        Connection connection = null;
        Statement statement = null;
        try {

            connection = connection();
            if (connection != null) {
                // Step 2.B: Creating JDBC Statement 
                statement = connection.createStatement();

                // Step 2.C: Executing SQL & retrieve data into ResultSet
                statement.executeUpdate("DELETE * FROM Mines");

                for (int i = 0; i < rows; i++) {

                    for (int j = 0; j < cols; j++) {
                        // System.out.println(""+i+":"+j+":"+":"+s[i][j]); 
                        statement.executeUpdate("INSERT INTO Mines(Row,Col,val) VALUES(" + i + "," + j + ",'" + s[i][j] + "')");
                    }

                }
            }

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();

        } finally {

            // Step 3: Closing database connection
            try {
                if (null != connection) {

                    // cleanup resources, once after processing
                    statement.close();

                    // and then finally close connection
                    connection.close();
                }
            } catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }
    }
}
