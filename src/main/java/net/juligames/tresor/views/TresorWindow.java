package net.juligames.tresor.views;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.model.ProjectPropertiesUtil;
import net.juligames.tresor.views.common.Common;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("DeprecatedIsStillUsed")
public class TresorWindow extends BasicWindow {

    private static final Logger log = LoggerFactory.getLogger(TresorWindow.class);
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

    /**
     * ONLY USE THIS IF YOU EXACTLY KNOW WHAT YOU ARE DOING!
     * @param containerA The first container
     * @param containerB The second container
     *  @param horizontal If true, the containers will be split horizontally, otherwise vertically
     */
    @SuppressWarnings("UnstableApiUsage")
    @ApiStatus.Internal
    @Contract(mutates = "this")
    public void split(@NotNull Container containerA, @NotNull Container containerB, boolean horizontal) {
        if (containerA == containerB) throw new IllegalArgumentException("Container A and B must be different!");
        if (contentPanel.getChildCount() > 0) throw new IllegalStateException("Content panel already has children!");

        SplitPanel panel = horizontal
                ? SplitPanel.ofHorizontal(containerA, containerB)
                : SplitPanel.ofVertical(containerA, containerB);

        // edit contentPanel with reflection
        synchronized (contentPanel) {
            try {
                Field field = getClass().getField("contentPanel");
                field.setAccessible(true);
                field.set(this, panel);
                field.setAccessible(false);

                //success:
                setComponent(panel.withBorder(Borders.singleLineBevel(contentName)));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("I said you should only use this if you know what you are doing!");
                throw new RuntimeException(e);
            }
        }
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
