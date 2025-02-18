package net.juligames.tresor.views;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.views.common.Common;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TresorWindow extends BasicWindow {
    public TresorWindow(@NotNull TresorGUI gui, @NotNull String basicKey) {
        super(gui.getText(basicKey + ".title", true));
        this.setHints(Set.of(Window.Hint.EXPANDED));
        this.setMenuBar(Common.getMenu(gui));
    }


}
