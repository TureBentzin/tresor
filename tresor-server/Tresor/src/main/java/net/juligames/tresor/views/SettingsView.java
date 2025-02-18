package net.juligames.tresor.views;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.lang.Translations;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SettingsView {

    private static final @NotNull HashMap<TresorGUI, Window> settingsViewMap = new HashMap<>();

    public static @NotNull Window getSettingsWindow(@NotNull TresorGUI gui) {
        if (settingsViewMap.containsKey(gui)) {
            return settingsViewMap.get(gui);
        }
        Window window = new TresorWindow(gui, "window.settings");
        window.setComponent(getSettingsPanel(gui));
        settingsViewMap.put(gui, window);
        return window;
    }

    private static @NotNull Component getSettingsPanel(@NotNull TresorGUI gui) {
        Panel panel = new Panel(new GridLayout(2));
        GridLayout gridLayout = (GridLayout) panel.getLayoutManager();
        gridLayout.setRightMarginSize(1);
        gridLayout.setTopMarginSize(1);

        panel.addComponent(getLanguageSelection(gui));
        panel.addComponent(getRegeneratePanel(gui));

        //TODO: user gets trapped in settings view?

        return panel.withBorder(Borders.singleLineBevel());
    }

    //language selection
    private static @NotNull Component getLanguageSelection(@NotNull TresorGUI gui) {

        Panel languagePanel = new Panel(new GridLayout(2));
        languagePanel.withBorder(Borders.singleLine(gui.getText("window.settings.language.title", false)));

        Label label = new Label(gui.getText("window.settings.language.content", false));
        label.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER));

        languagePanel.addComponent(label);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER));
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
        return languagePanel.withBorder(Borders.singleLineBevel(gui.getText("window.settings.language.title", false)));
    }

    private static @NotNull Component getRegeneratePanel(@NotNull TresorGUI gui) {
        Panel panel = new Panel(new GridLayout(2));
        panel.addComponent(new Label(gui.getText("window.settings.regenerate.content", false)));
        panel.addComponent(getRegenerateButton(gui));
        return panel.withBorder(Borders.singleLineBevel());
    }

    private static @NotNull Button getRegenerateButton(@NotNull TresorGUI gui) {
        Button button = new Button(gui.getText("window.settings.regenerate.button", false));
        button.addListener((button1) -> {
            gui.regenerate();
            try {
                gui.getGui().getScreen().refresh();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return button;
    }


    public static void remove(@NotNull TresorGUI gui) {
        settingsViewMap.remove(gui);
    }

    private SettingsView() {
        throw new IllegalStateException("Utility class");
    }
}
