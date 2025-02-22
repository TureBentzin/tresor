package net.juligames.tresor.rest;


/**
 * @author Ture Bentzin
 * @since 22-02-2025
 */
public record UnprocessableEntity(String ctx, String loc, String msg, String type) {
}
