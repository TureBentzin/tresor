package net.juligames.tresor.views.test;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.views.TresorWindow;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ColorTestView {

    public static @NotNull Window getColorTestWindow(@NotNull TresorGUI gui) {
        TresorWindow window = new TresorWindow(gui, "window.colortest");
        window.setHints(Set.of(Window.Hint.CENTERED));

        return window;
    }

}
