package net.juligames.tresor.rest;


import org.jetbrains.annotations.Range;

/**
 * @author Ture Bentzin
 * @since 26-02-2025
 */
public record BalanceResponse(@Range(from = 0, to = Integer.MAX_VALUE) int balance) {

}
