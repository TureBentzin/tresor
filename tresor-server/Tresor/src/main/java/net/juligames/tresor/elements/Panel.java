package net.juligames.tresor.elements;


import com.googlecode.lanterna.graphics.TextGraphics;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public class Panel extends Element{
    public Panel(@NotNull TresorGUI gui) {
        super(gui);
    }

    @Override
    boolean draw(@NotNull TextGraphics section) throws IOException {
        return false;
    }
}
