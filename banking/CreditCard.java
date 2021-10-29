package banking;

import java.util.Random;

public class CreditCard {

    protected String cardNumber;
    protected String pin;
    protected int balance;

    public CreditCard() {
        this.cardNumber = setCardNumber();
        this.pin = setPIN();
        this.balance = 0;

        // insert new card data in database
        Main.database.newAccuont(cardNumber, pin, balance);
    }
    String setCardNumber() {
        StringBuilder newCardNumber = new StringBuilder("400000");
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            newCardNumber.append(random.nextInt(10));
        }
        // for Checksum
        newCardNumber.append(luhnAlgorithm(newCardNumber));
        String cardNumber = newCardNumber.toString();
        // check database if cardnumber allready exists
        if (Main.database.checkCardNumber(cardNumber)) {
            setCardNumber();
        }
        return cardNumber;
    }

    // for calculating Checksum
    public int luhnAlgorithm(StringBuilder cardNumber) {
        int sum = 0;
        for (int i = 0; i < 15; i++) {
            int add = 0;
            if (i % 2 == 0) {
                add = Integer.parseInt(String.valueOf(cardNumber.charAt(i))) * 2;
                if (add > 9) {
                    add -= 9;
                }
            } else {
                add = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            }

            //System.out.println("add: " + add);
            sum += add;
        }
        //System.out.println("add: " + sum);
        int checksum = 0;
        for (int i = 0; i < 10; i++) {
            if ((sum + i) % 10 == 0) {
                checksum = i;
                break;
            }
        }
        return checksum;

    }

    String setPIN() {
        StringBuilder newPIN = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            newPIN.append(random.nextInt(10));
        }
        return newPIN.toString();
    }

    String getCardNumber() {
        return cardNumber;
    }

    String getPin() {
        return pin;
    }

}
