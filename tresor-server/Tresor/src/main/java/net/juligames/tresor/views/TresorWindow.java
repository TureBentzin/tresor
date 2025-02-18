package net.juligames.tresor.views;

import com.googlecode.lanterna.gui2.*;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.model.ProjectPropertiesUtil;
import net.juligames.tresor.views.common.Common;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("DeprecatedIsStillUsed")
public class TresorWindow extends BasicWindow {

    private static @NotNull String generateTitle(@NotNull TresorGUI gui) {
        return gui.getTextWithParams("app.title", false,
                Map.of("snapshot?", ProjectPropertiesUtil.isSnapshot() ? " (SNAPSHOT)" : "",
                        "development?", ProjectPropertiesUtil.isDevelopment() ? " (DEV)" : ""));
    }

    private final @NotNull Panel contentPanel;

    public TresorWindow(@NotNull TresorGUI gui, @NotNull String basicKey) {
        this(gui, basicKey, new LinearLayout(Direction.VERTICAL));
    }

    public TresorWindow(@NotNull TresorGUI gui, @NotNull String basicKey, @NotNull LayoutManager layoutManager) {
        super(generateTitle(gui));
        this.setHints(Set.of(Hint.EXPANDED));
        this.setMenuBar(Common.getMenu(gui));
        contentPanel = new Panel(layoutManager);
        setComponent(contentPanel.withBorder(Borders.singleLineBevel(gui.getText(basicKey + ".title", false))));
    }

    @Override
    public @NotNull Container getComponent() {
        return (Container) super.getComponent();
    }

    @ApiStatus.Internal
    @Override
    public void setComponent(@NotNull Component component) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace.length > 2) {
            StackTraceElement caller = stackTrace[2];
            if (!caller.getClassName().equals(TresorWindow.class.getName())) {
                throw new UnsupportedOperationException("Setting component directly is not supported!");
            }
        }

        super.setComponent(component);
    }

    public @NotNull Panel getContentPanel() {
        return contentPanel;
    }
}
