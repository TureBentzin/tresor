package net.juligames.tresor.elements;


import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;

/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public interface Element {

    /**
     * Get the position of the element.
     * @return the position of the element.
     */
    @NotNull TerminalPosition getPosition();

    @NotNull TerminalSize getSize();

    @Range(from = 0, to = Integer.MAX_VALUE)
    default int getWidth() {
        return getSize().getColumns();
    }

    @Range(from = 0, to = Integer.MAX_VALUE)
    default int getHeight() {
        return getSize().getRows();
    }

    /**
     * Draw the element on the screen.
     * @return if the element was drawn successfully. If false, the element was not drawn and this method promises that no changes were made to the screen.
     * @throws IOException if an I/O error occurs. The element may have been partially drawn.
     * @implNote This method might be called from different threads for multiple elements. Implementations should be thread-safe.
     */
    boolean draw() throws IOException;



}
