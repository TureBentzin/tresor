package net.juligames.tresor.views;

import com.googlecode.lanterna.gui2.*;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.model.ProjectPropertiesUtil;
import net.juligames.tresor.views.common.Common;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TresorWindow extends BasicWindow {

    private static @NotNull String generateTitle(@NotNull TresorGUI gui, @NotNull String basicKey) {
        String title = gui.getText(basicKey + ".title", true);

        if (ProjectPropertiesUtil.isSnapshot()) {
            title += " (SNAPSHOT)";
        }

        if (ProjectPropertiesUtil.isDevelopment()) {
            title += " (DEV)";
        }
        return title;
    }

    private @NotNull Panel contentPanel;

    public TresorWindow(@NotNull TresorGUI gui, @NotNull String basicKey) {
        this(gui, basicKey, new LinearLayout(Direction.VERTICAL));
    }

    public TresorWindow(@NotNull TresorGUI gui, @NotNull String basicKey, @NotNull LayoutManager layoutManager) {
        super(generateTitle(gui, basicKey));
        this.setHints(Set.of(Hint.EXPANDED));
        this.setMenuBar(Common.getMenu(gui));
        contentPanel = new Panel(layoutManager);

        this.setComponent(contentPanel.withBorder(Borders.singleLineBevel()));
    }

    public @NotNull Panel getContentPanel() {
        return contentPanel;
    }
}
