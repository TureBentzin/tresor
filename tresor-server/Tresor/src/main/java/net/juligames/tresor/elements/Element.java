package net.juligames.tresor.elements;


import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.Terminal;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;

/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public abstract class Element {


    private final @NotNull TerminalPosition position;
    private final @NotNull TerminalSize size;

    public Element(@NotNull TerminalPosition position, @NotNull TerminalSize size) {
        this.position = position;
        this.size = size;
    }

    /**
     * Get the position of the element.
     *
     * @return the position of the element.
     */
    @NotNull TerminalPosition getPosition() {
        return position;
    }

    @NotNull TerminalSize getSize() {
        return size;
    }

    @Range(from = 0, to = Integer.MAX_VALUE)
    final int getWidth() {
        return getSize().getColumns();
    }

    @Range(from = 0, to = Integer.MAX_VALUE)
    final int getHeight() {
        return getSize().getRows();
    }

    final @NotNull TerminalPosition getBottomRight() {
        return getPosition().withRelative(getWidth(), getHeight());
    }

    final @NotNull TerminalPosition getTopRight() {
        return getPosition().withRelative(getWidth(), 0);
    }

    final @NotNull TerminalPosition getBottomLeft() {
        return getPosition().withRelative(0, getHeight());
    }



    //Maybe: @implNote This method might be called from different threads for multiple elements. Implementations should be thread-safe.


    /**
     * Draw the element on the screen.
     *
     * @return if the element was drawn successfully. If false, the element was not drawn and this method promises that no changes were made to the screen.
     * @throws IOException if an I/O error occurs. The element may have been partially drawn.
     */
    abstract boolean draw(final @NotNull TresorGUI gui) throws IOException;


    final boolean drawBorder(int thickness, char border, @NotNull TextGraphics section) {
        TerminalPosition topLeft = getPosition();
        TerminalPosition bottomRight = getBottomRight();

        for (int i = 0; i < thickness; i++) {
            section.drawLine(topLeft.withRelative(i, i), bottomRight.withRelative(-i, -i), border);
            section.drawLine(topLeft.withRelative(i, i), bottomRight.withRelative(-i, -i), border);
        }

        return true;
    }



}
