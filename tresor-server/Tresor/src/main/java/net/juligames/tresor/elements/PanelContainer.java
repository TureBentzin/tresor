package net.juligames.tresor.elements;


import com.googlecode.lanterna.graphics.TextGraphics;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;

/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public class PanelContainer extends Element {
    private final @NotNull HashSet<Panel> elements = new HashSet<>();

    public PanelContainer(@NotNull TresorGUI gui) {
        super(gui);
    }

    @Override
    final boolean draw(@NotNull TextGraphics section) throws IOException {

        //create new sections for each panel. All of equal size.




        for (Panel element : elements) {
            //element.draw(section);
        }
        return false;
    }
}
