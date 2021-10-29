package banking;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class Database {

    // connect to database
    public Connection connect() {
        // db parameters
        Connection conn = null;
        String url = "jdbc:sqlite:card.s3db";
            // create a connection to the database or create new if not exist
        try {
            conn = DriverManager.getConnection(url);
            //System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println("exception at connect():" + e.getMessage());
        }
        return conn;
    }

    // create table if not exist
    public void createTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER,\n"
                + "	number TEXT,\n"
                + "	pin TEXT,\n"
                + "	balance INTEGER DEFAULT 0\n"
                + ");";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("exception at createTable():" + e.getMessage());
        }

    }

    public void newAccuont(String number, String pin, int balance) {
        String sql = "INSERT INTO card (id, number, pin, balance) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, getNextId());
            pstmt.setString(2, number);
            pstmt.setString(3, pin);
            pstmt.setInt(4, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("exception at newAccount():" + e.getMessage());
        }
    }

    public boolean checkCardNumber(String cardNumber) {
        String sql = "SELECT number FROM card Where number =" + cardNumber;
        boolean state = false;
        String number = "";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            //
            ResultSet rs  = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                number = rs.getString("number");
            }
            if (number.equals(cardNumber)) {
                state = true;
            }
        } catch (SQLException e) {
            System.out.println("exception at checkCardNumber():" + e.getMessage());
        }
        return state;
    }

    public boolean checkPin(String cardNumber, String pinInput) {
        String sql = "SELECT number, pin FROM card Where number =" + cardNumber;
        boolean state = false;
        String pin = "";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            //
            ResultSet rs  = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                pin = rs.getString("pin");
            }
            if (pin.equals(pinInput)) {
                state = true;
            }
        } catch (SQLException e) {
            System.out.println("exception at checkPin():" + e.getMessage());
        }
        return state;
    }

    public int getBalance(String cardNumber) {
        String sql = "SELECT number, balance FROM card WHERE number =" + cardNumber;
        int balance = 1;
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            //
            ResultSet rs  = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                balance = rs.getInt("balance");
            }
        } catch (SQLException e) {
            System.out.println("exception from database getBalance()" + e.getMessage());
        }
        return balance;
    }


   public void transfer(String cardNumber, String transferNumber, int difference) {
        try (Connection conn = this.connect()){

            // Disable auto-commit mode
            conn.setAutoCommit(false);

            // deduct difference from own account
            setBalance(cardNumber,getBalance(cardNumber) - difference);

            // add difference to the receiver's account
            setBalance(transferNumber, getBalance(transferNumber) + difference);

            conn.commit();

        } catch (SQLException e) {
            System.out.println("exception from database transfer()" + e.getMessage());
        }
   }

    public int getNextId() {
        String sql = "SELECT COUNT(*) FROM card" ;
        int id = 0;
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            //
            ResultSet rs  = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                id = rs.getInt("COUNT(*)");
            }
        } catch (SQLException e) {
            System.out.println("exception at getNextId():" + e.getMessage());
        }
        return id;
    }

    public int getID(String cardNumber) {
        String sql = "SELECT id FROM card WHERE number =" + cardNumber;
        int id = 1;
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            //
            ResultSet rs  = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("exception from database getID()" + e.getMessage());
        }
        return id;
    }

    public void setBalance(String number, int balance) {
        String sql = "UPDATE card " +
                "SET balance = ? " +
                "WHERE number = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){
            // set the corresponding param
            pstmt.setInt(1, balance);
            pstmt.setString(2, number);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("exception from database setBalance()" + e.getMessage());
        }
    }

    public void deleteAccount(int id) {
        String sql = "DELETE FROM card WHERE id = ?";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("exception in database deleteAccount()" + e.getMessage());
        }
    }

    public void deleteAllData() {
        String sql = "DROP TABLE card";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            // execute the delete statement
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("exception in database deleteAllData()" + e.getMessage());
        }
    }

}
