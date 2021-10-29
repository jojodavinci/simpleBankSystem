package banking;

import java.util.Scanner;

public class Login {

    Scanner scanner = new Scanner(System.in);
    String cardNumber;
    String pin;

    public Login() {
        logIntoAccount();
    }

    void logIntoAccount() {
        System.out.println("Enter your card number:");
        cardNumber = scanner.next();

        System.out.println("Enter your PIN:");
        pin = scanner.next();

        if (checkCardNumber() && checkPIN()) {
            System.out.println("\nYou have successfully logged in!\n");
            Main.bank.account.cardNumber = cardNumber;
            Main.bank.account.pin = pin;
            Main.bank.account.id = getId();
            Main.bank.account.balance = getBalance();
            Main.bank.account.menu();
        } else {
            System.out.println("\nWrong card number or PIN!\n");
            Main.bank.menu();
        }
    }

    boolean checkCardNumber() {
        return Main.database.checkCardNumber(cardNumber);
    }

    boolean checkPIN() {
        return Main.database.checkPin(cardNumber, pin);
    }

    int getBalance() {
        return Main.database.getBalance(cardNumber);
    }

    int getId() {return Main.database.getID(cardNumber);}
}
