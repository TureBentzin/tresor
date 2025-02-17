package net.juligames.tresor.elements;


import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.Terminal;
import net.juligames.tresor.Theme;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;

/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public abstract class Element {

    private final @NotNull TresorGUI gui;

    public Element(final @NotNull TresorGUI gui) {
        this.gui = gui;
    }

    protected final @NotNull Terminal getTerminal() {
        return gui.getTerminal();
    }

    /**
     * Only use this method if you need to draw outside of the element's section.
     * Drawing outside of the section is not recommended and considered dangerous.
     *
     * @return the entire graphics of the GUI
     */
    @ApiStatus.Internal
    protected final @NotNull TextGraphics getEntireGraphics() {
        return gui.getTextGraphics();
    }

    protected final @NotNull Theme getTheme() {
        return gui.getTheme();
    }

    protected final void applyTheme(final @NotNull TextGraphics section) {
        section.setBackgroundColor(getTheme().backgroundColor());
        section.setForegroundColor(getTheme().foregroundColor());
    }

    protected final void applyHighlightTheme(final @NotNull TextGraphics section) {
        section.setBackgroundColor(getTheme().highlightBackgroundColor());
        section.setForegroundColor(getTheme().highlightForegroundColor());
    }


    /**
     * Get the size of the element.
     *
     * @param section the section to get the size of
     * @return the size of the section
     * @deprecated use {@link TextGraphics#getSize()} instead
     */
    @Deprecated(forRemoval = true)
    protected final @NotNull TerminalSize getSize(final @NotNull TextGraphics section) {
        return section.getSize();
    }

    @Range(from = 0, to = Integer.MAX_VALUE)
    protected final int getWidth(final @NotNull TextGraphics section) {
        return section.getSize().getColumns();
    }

    @Range(from = 0, to = Integer.MAX_VALUE)
    protected final int getHeight(final @NotNull TextGraphics section) {
        return section.getSize().getRows();
    }

    protected final @NotNull TerminalPosition getBottomRight(final @NotNull TextGraphics section) {
        return new TerminalPosition(getWidth(section), getHeight(section));
    }

    protected final @NotNull TerminalPosition getTopLeft() {
        return new TerminalPosition(0, 0);
    }

    protected final @NotNull TerminalPosition getTopRight(final @NotNull TextGraphics section) {
        return new TerminalPosition(getWidth(section), 0);
    }

    protected final @NotNull TerminalPosition getBottomLeft(final @NotNull TextGraphics section) {
        return new TerminalPosition(0, getHeight(section));
    }

    protected final @NotNull TerminalPosition getCenter(final @NotNull TextGraphics section) {
        return new TerminalPosition(getWidth(section) / 2, getHeight(section) / 2);
    }

    protected final @NotNull TerminalPosition getCenterLeft(final @NotNull TextGraphics section) {
        return new TerminalPosition(0, getHeight(section) / 2);
    }

    protected final @NotNull TerminalPosition getCenterRight(final @NotNull TextGraphics section) {
        return new TerminalPosition(getWidth(section), getHeight(section) / 2);
    }

    protected final @NotNull TerminalPosition getCenterTop(final @NotNull TextGraphics section) {
        return new TerminalPosition(getWidth(section) / 2, 0);
    }

    protected final @NotNull TerminalPosition getCenterBottom(final @NotNull TextGraphics section) {
        return new TerminalPosition(getWidth(section) / 2, getHeight(section));
    }

    //Maybe: @implNote This method might be called from different threads for multiple elements. Implementations should be thread-safe.


    /**
     * Draw the element on the screen.
     *
     * @return if the element was drawn successfully. If false, the element was not drawn and this method promises that no changes were made to the screen.
     * @throws IOException if an I/O error occurs. The element may have been partially drawn.
     */
    abstract boolean draw(final @NotNull TextGraphics section) throws IOException;

    protected final boolean drawBorder(final @NotNull TextGraphics section, char borderChar, int thickness) {
        int width = getWidth(section);
        int height = getHeight(section);

        if (thickness < 1) {
            return false;
        }

        for (int i = 0; i < thickness; i++) {
            section.drawLine(0, i, width, i, borderChar);
            section.drawLine(0, height - i, width, height - i, borderChar);
            section.drawLine(i, 0, i, height, borderChar);
            section.drawLine(width - i, 0, width - i, height, borderChar);
        }

        return true;
    }

    protected final boolean fill(char fillChar, final @NotNull TextGraphics section, @NotNull TextColor color) {

        TextColor originalColor = section.getBackgroundColor();

        section.fill(fillChar);

        return true;
    }
}
