package net.juligames.tresor.elements;

// Header, ApplicationPane (Element), footer


import com.googlecode.lanterna.graphics.TextGraphics;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public class ApplicationFrame extends Element {

    private @NotNull Element header;

    private @NotNull Element pane;

    private @NotNull Element footer;

    public ApplicationFrame(@NotNull TresorGUI gui) {
        super(gui);
    }

    @Override
    boolean draw(@NotNull TextGraphics section) throws IOException {
        return false;
    }
}
