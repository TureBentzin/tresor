package net.juligames.tresor.rest;


/**
 * @author Ture Bentzin
 * @since 26-02-2025
 */
public record BalanceResponse(int balance) {
    public BalanceResponse {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }

    public int getBalance() {
        return balance;
    }
}
