package net.juligames.tresor.views;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.lang.Translations;
import net.juligames.tresor.views.test.ColorTestView;
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
        // appearancePanel.addComponent(2, new CheckBox("placeholder"));

        //window.setFocusedInteractable(languageSelection.getChildren().stream().filter(component -> component instanceof Interactable).map(component -> (Interactable) component).findFirst().orElse(null));
        // Utils.findFocus(window, languageSelection);

        appearancePanel.setPreferredSize(appearancePanel.calculatePreferredSize());
        window.getContentPanel().addComponent(appearancePanel.withBorder(Borders.singleLine(gui.getText("window.settings.appearance.title", false))));

        settingsViewMap.put(gui, window);
        return window;
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
