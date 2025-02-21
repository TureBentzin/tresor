package net.juligames.tresor.views;

import com.googlecode.lanterna.TerminalSize;
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

    private boolean permanent;
    private final @NotNull String contentName;

    private static @NotNull String generateTitle(@NotNull TresorGUI gui) {
        return gui.getTextWithParams("app.title", false,
                Map.of("snapshot?", ProjectPropertiesUtil.isSnapshot() ? " (SNAPSHOT)" : "",
                        "dev?", ProjectPropertiesUtil.isDevelopment() ? " (DEV)" : "",
                        "version", ProjectPropertiesUtil.getGitVersion(),
                        "build", ProjectPropertiesUtil.getGitCommit(),
                        "date", ProjectPropertiesUtil.getGitBuildTime(),
                        "branch", ProjectPropertiesUtil.getGitBranch(),
                        "brand", ProjectPropertiesUtil.getArtifactId()));
    }

    private final @NotNull Panel contentPanel;

    public TresorWindow(@NotNull TresorGUI gui, @NotNull String basicKey) {
        this(gui, basicKey, true);
    }

    public TresorWindow(@NotNull TresorGUI gui, @NotNull String basicKey, boolean permanent) {
        this(gui, basicKey, new LinearLayout(Direction.VERTICAL), permanent);
    }

    public TresorWindow(@NotNull TresorGUI gui, @NotNull String basicKey, @NotNull LayoutManager layoutManager) {
        this(gui, basicKey, layoutManager, true);
    }

    public TresorWindow(@NotNull TresorGUI gui, @NotNull String basicKey, @NotNull LayoutManager layoutManager, boolean permanent) {
        super(generateTitle(gui));
        contentPanel = new Panel(layoutManager);
        if (permanent) {
            this.setHints(Set.of(Hint.EXPANDED));
            this.setMenuBar(Common.getMenu(gui));
        } else {
            this.setHints(Set.of(Hint.CENTERED));
            Button component = new Button(gui.getText("window.common.close", false), this::close);
            component.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.End));
            String label = component.getLabel();
            component.setPreferredSize(new TerminalSize(label.length() + 2, 1));
            contentPanel.addComponent(component);
        }

        contentName = gui.getText(basicKey + ".title", false);
        setComponent(contentPanel.withBorder(Borders.singleLineBevel(contentName)));
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

    public boolean isPermanent() {
        return permanent;
    }


    public @NotNull String getContentName() {
        return contentName;
    }

    public static void checkAuthorization(@NotNull TresorGUI gui) {
        if (!gui.getAuthenticationController().isAuthenticated()) {
            gui.showError("validation.not_authed");
            gui.switchWindow(LoginView.getLoginWindow(gui));
        }
    }
}
