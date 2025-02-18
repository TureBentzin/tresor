package net.juligames.tresor.views;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Window;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.model.ProjectPropertiesUtil;
import net.juligames.tresor.views.common.Common;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TresorWindow extends BasicWindow {

    private static @NotNull String generateTitle(@NotNull TresorGUI gui, String basicKey) {
        String title = gui.getText(basicKey + ".title", true);

        if (ProjectPropertiesUtil.isSnapshot()) {
            title += " (SNAPSHOT)";
        }

        if (ProjectPropertiesUtil.isDevelopment()) {
            title += " (DEV)";
        }
        return title;
    }

    public TresorWindow(@NotNull TresorGUI gui, @NotNull String basicKey) {
        super(generateTitle(gui, basicKey));
        this.setHints(Set.of(Window.Hint.EXPANDED));
        this.setMenuBar(Common.getMenu(gui));
    }


}
