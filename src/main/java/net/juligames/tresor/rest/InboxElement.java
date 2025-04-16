package net.juligames.tresor.rest;

public record InboxElement(String message, String sender, boolean read) {

    public InboxElement {
        if (message == null || sender == null) {
            throw new IllegalArgumentException("Message and sender cannot be null");
        }
    }
}
