package banking;

import java.util.Scanner;

public class Bank {

    protected Account account = new Account();

    public void menu() {

        Scanner scanner = new Scanner(System.in);
        System.out.println(
                "1. Create an account\n" +
                        "2. Log into account\n" +
                        "0. Exit");
        switch (scanner.nextInt()) {
            case 1:
                System.out.println();
                account.createAccount();
                menu();
            case 2:
                System.out.println();
                Login login = new Login();
                menu();
            case 0:
                System.out.println("\nBey!");
                System.exit(0);
            default:
                System.out.println("Wrong input!\n");
                menu();
        }
    }
}
