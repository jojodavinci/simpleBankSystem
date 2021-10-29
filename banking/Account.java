package banking;

import java.util.Scanner;

public class Account {

    protected CreditCard creditCard;
    int id;
    String cardNumber;
    String pin;
    int balance;
    Scanner scanner = new Scanner(System.in);

    public Account() { }

    void createAccount() {
        creditCard = new CreditCard();
        System.out.println("Your card has been created\n" +
                "Your card number:\n" +
                creditCard.getCardNumber() +
                "\nYour card PIN:\n" +
                creditCard.getPin() + "\n");
    }

    void menu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
        switch (scanner.nextInt()) {
            case 1:
                System.out.println("\nBalance: " + balance +"\n");
                //System.out.println("id: " + id);
                //System.out.println("CardNumber: " + cardNumber);
                //System.out.println("Pin: " + pin +"\n");
                menu();
            case 2:
                System.out.println("\nEnter income:");
                setBalance();
                System.out.println("Income was added!\n");
                menu();
            case 3:
                transfer();
                menu();
            case 4:
                Main.database.deleteAccount(id);
                System.out.println("\nThe account has been closed!\n");
                Main.bank.menu();
            case 5:
                System.out.println("\nYou have successfully logged out!\n");
                Main.bank.menu();
            case 0:
                System.out.println("\nBey!");
                //Main.database.deleteAllData();
                System.exit(0);
            default:
                System.out.println("\nWrong input!\n");
                menu();
        }
    }

    void setBalance() {
        int income = scanner.nextInt();
        Main.database.setBalance(cardNumber, balance + income);
        balance = Main.database.getBalance(cardNumber);
    }

    void transfer() {
        System.out.println("\nTransfer\n" +
                "Enter card number:");
        String transferNumber = scanner.next();
        // check if the accounts are the same
        if (cardNumber.equals(transferNumber)) {
            System.out.println("You can't transfer money to the same account!\n");
            menu();
        }

        // check if the receiver's card number pass the Luhn algorithm
        StringBuilder transNum = new StringBuilder(transferNumber);
        int i1 = Main.bank.account.creditCard.luhnAlgorithm(transNum);
        int i2 = Integer.parseInt(String.valueOf(transferNumber.charAt(15)));
        if (i1 != i2) {
            System.out.println("Probably you made a mistake in the card number. Please try again!\n");
            menu();
        }

        // check if the receiver's card number exist
        if (!Main.database.checkCardNumber(transferNumber)) {
            System.out.println("Such a card does not exist.\n");
            menu();
        }

        System.out.println("Enter how much money you want to transfer:");
        Scanner scanner = new Scanner(System.in);
        int difference = scanner.nextInt();

        // check if there is enough on own account
        if ((Main.database.getBalance(cardNumber) - difference) < 0) {
            System.out.println("Not enough money!\n");
            menu();
        }

        // if every thing is fine, do transfer
        Main.database.transfer(cardNumber, transferNumber, difference);
        System.out.println("Succes!\n");
    }
}
