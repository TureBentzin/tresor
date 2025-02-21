package net.juligames.tresor.views;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.lang.Translations;
import net.juligames.tresor.views.test.ColorTestView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SettingsView {


    public static @NotNull Window getSettingsWindow(@NotNull TresorGUI gui) {
        TresorWindow window = new TresorWindow(gui, "window.settings");
        window.setStrictFocusChange(true);
        window.getContentPanel().addComponent(new Button(gui.getText("window.color-test.title", false), () -> {
            gui.getGui().addWindowAndWait(ColorTestView.getColorTestWindow(gui));
        }));
        Panel appearancePanel = new Panel(new LinearLayout(Direction.VERTICAL));
        Container languageSelection = getLanguageSelection(gui);

        appearancePanel.addComponent(0, languageSelection);

        appearancePanel.addComponent(new Label(gui.getText("window.settings.regenerate.content", false)));
        appearancePanel.addComponent(getRegenerateButton(gui));

        appearancePanel.setPreferredSize(appearancePanel.calculatePreferredSize());
        window.getContentPanel().addComponent(appearancePanel.withBorder(Borders.singleLine(gui.getText("window.settings.appearance.title", false))));

        window.getContentPanel().addComponent(getDebugInformation(gui));

        return window;
    }

    private static @NotNull Container getDebugInformation(@NotNull TresorGUI gui) {
        Panel debugPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        debugPanel.addComponent(new Label(gui.getText("window.settings.debug.content", false)));
        debugPanel.addComponent(new Label("JWT Fingerprint: " + gui.getAuthenticationController().getJwtFingerprint()));

        debugPanel.addComponent(gui.getTextAsLabel("window.settings.debug.windows", false));
        Table<String> table = new Table<>("Position", "Window", "Hash");
        for (Window window : gui.getGui().getWindows()) {
            String windowName = window.getTitle();
            if (window instanceof TresorWindow) {
                windowName += "(" + ((TresorWindow) window).getContentName() + ")";
            }

            if (windowName.isEmpty()) {
                windowName = window.getClass().getSimpleName();
            }

            table.getTableModel().addRow(window.getPosition().toString(), windowName, String.valueOf(window.hashCode()));
        }
        debugPanel.addComponent(table);

        return debugPanel.withBorder(Borders.singleLine(gui.getText("window.settings.debug.title", false)));
    }

    //language selection
    private static @NotNull Container getLanguageSelection(@NotNull TresorGUI gui) {

        Panel languagePanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        languagePanel.withBorder(Borders.singleLine(gui.getText("window.settings.language.title", false)));

        Label label = new Label(gui.getText("window.settings.language.content", false));
        languagePanel.addComponent(label);

        ComboBox<String> comboBox = new ComboBox<>();
        List<String> languages = Translations.getAvailableMessageSets();
        languages.forEach(comboBox::addItem);
        comboBox.setSelectedItem(gui.getMessageSet());
        comboBox.addListener((selectedItem, oldSelection, ignored) -> {
            gui.setMessageSet(languages.get(selectedItem));
            try {
                gui.getGui().getScreen().refresh();
            } catch (IOException ignoredException) {
            }
        });


        languagePanel.addComponent(comboBox);
        return languagePanel.withBorder(Borders.singleLine(gui.getText("window.settings.language.title", false)));
    }


    private static @NotNull Button getRegenerateButton(@NotNull TresorGUI gui) {
        Button button = new Button(gui.getText("window.settings.regenerate.button", false));
        button.addListener((button1) -> {
            gui.showError("app.not_implemented"); //TODO regenerate?
            try {
                gui.getGui().getScreen().refresh();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return button;
    }


    private SettingsView() {
        throw new IllegalStateException("Utility class");
    }
}
