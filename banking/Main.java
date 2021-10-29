package banking;


import java.util.Scanner;

public class Main {

    protected static Bank bank = new Bank();
    protected static Database database = new Database();

    public static void main(String[] args) {

        // create new database
        database.createTable();
        bank.menu();
    }



}