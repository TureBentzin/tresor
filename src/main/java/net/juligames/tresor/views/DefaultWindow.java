package net.juligames.tresor.views;

import com.googlecode.lanterna.gui2.Window;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * The window that's created on connection. When its closed, the application will exit.
 */
public class DefaultWindow extends TresorWindow {

    public DefaultWindow(@NotNull TresorGUI gui) {
        super(gui, "window.default");
    }
}
