package net.juligames.tresor.views;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.error.MissingAuthenticationException;
import net.juligames.tresor.model.ProjectPropertiesUtil;
import net.juligames.tresor.utils.ViewUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ture Bentzin
 * @since 27-02-2025
 */
public class PrivateMessagesView {

    private static final @NotNull Logger log = LoggerFactory.getLogger(PrivateMessagesView.class);

    public static @NotNull Window getPrivateMessagesWindow(@NotNull TresorGUI gui) {
        return ViewUtils.authenticatedWindow(gui, "private_messages", window -> {
            Panel panel = window.getContentPanel();

            panel.setLayoutManager(new GridLayout(2));

            Button writeMessageButton = new Button(gui.getText("window.private_messages.write_message.button", false));
            writeMessageButton.addListener(e -> openWriteMessageWindow(gui, window));
            panel.addComponent(writeMessageButton);
        });
    }

    public static @NotNull ComboBox<String> getRecipientsDropdown(@NotNull TresorGUI gui) {
        ComboBox<String> comboBox = new ComboBox<>();
        try {
            gui.getUserController().getValidUsernames().forEach(comboBox::addItem);
        } catch (
                MissingAuthenticationException ignored) { //this exception can be ignored as we dont need any special handling
        }

        return comboBox;
    }

    public static void openWriteMessageWindow(@NotNull TresorGUI gui, @NotNull TresorWindow parent) {
        Window window = new BasicWindow(gui.getText("window.private_messages.write_message.title", false));
        window.setHints(Set.of(Window.Hint.CENTERED));
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));

        TextBox message = new TextBox(new TerminalSize(50, 4));
        message.setCaretWarp(true);

        ComboBox<String> recipient = getRecipientsDropdown(gui);
        panel.addComponent(gui.getTextAsLabel("window.private_messages.write_message.recipient", false));
        panel.addComponent(recipient);


        message.setText(gui.getTextWithParams("window.private_messages.write_message.default", false, Map.of(
                "sender", gui.getAuthenticationController().getUsername().orElse("?"),
                "brand", ProjectPropertiesUtil.getArtifactId()
        )));
        panel.addComponent(gui.getTextAsLabel("window.private_messages.write_message.message", false));
        panel.addComponent(message);
        panel.addComponent(new Button(gui.getText("window.private_messages.write_message.button_clear", false), () -> message.setText("")));
        panel.addComponent(new EmptySpace(TerminalSize.ONE));

        Button sendButton = new Button(gui.getText("window.private_messages.write_message.button_send", false));
        sendButton.addListener(e -> {
            //send message
            log.info("Sending \"{}\" to: {}", message.getText(), recipient.getSelectedItem());
            window.close();
        });
        panel.addComponent(sendButton);

        window.setComponent(panel);
        gui.getGui().addWindowAndWait(window);
        gui.getGui().removeWindow(window);
        gui.getGui().setActiveWindow(parent);
    }
}
