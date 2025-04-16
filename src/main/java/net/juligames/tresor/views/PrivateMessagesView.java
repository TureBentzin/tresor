package net.juligames.tresor.views;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.error.MissingAuthenticationException;
import net.juligames.tresor.model.ProjectPropertiesUtil;
import net.juligames.tresor.rest.InboxElement;
import net.juligames.tresor.utils.ViewUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Transient;
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

            panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

            Button writeMessageButton = new Button(gui.getText("window.private_messages.write_message.button", false));
            writeMessageButton.addListener(e -> openWriteMessageWindow(gui, window));
            panel.addComponent(writeMessageButton);

            panel.addComponent(getConversationContainer(gui));
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


    public static @NotNull Container getConversationContainer(@NotNull TresorGUI gui) {
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2)); //TODO make this dynamic?

       /* try {

        } catch (MissingAuthenticationException ignored) {

        }
        */

        appendUnreadInboxElements(gui, panel);

        return panel.withBorder(Borders.singleLine(gui.getText("window.private_messages.conversation.title", false)));
    }

    private static void appendUnreadInboxElements(@NotNull TresorGUI gui, @NotNull Panel panel) {
        InboxElement test = new InboxElement("This is a message", "Sender", false);
        InboxElement test1 = new InboxElement("Please care about the usage!", "TDR", false);
        InboxElement test2 = new InboxElement("Help my chess account was compromised!", "RilxDarki", false);
        InboxElement test3 = new InboxElement("Did you ever think about rewriting this server in rust?\n You know that oxidizing it would improve performance by a lot!\n I would suggest rewriting it in rust. Whatever. How much is 100 Steam Trucks at your shop? How much Cargo do they hold?", "DSeeLP", false);

        Set<InboxElement> elements = Set.of(test, test1, test2, test3);

        elements.forEach(element -> {
            // build element container
            Panel elementContainer = new Panel();
            elementContainer.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

            TextBox content = new TextBox();

            content.setText(gui.getTextWithParams("window.private_messages.conversation.element.content", false,
                    Map.of(
                            "message", element.message(),
                            "sender", element.sender()
                    )
            ));

            content.setReadOnly(true);
            content.setPreferredSize(new TerminalSize(80, 5));

            elementContainer.addComponent(content);

            // add button to mark as read
            Button markAsReadButton = new Button(gui.getText("window.private_messages.conversation.element.read", true));

            elementContainer.addComponent(markAsReadButton);
            Container container = elementContainer.withBorder(Borders.singleLine(gui.getTextWithParams("window.private_messages.conversation.element.title", false,
                    Map.of(
                            "sender", element.sender()
                    ))));
            panel.addComponent(container);
            panel.addComponent(new EmptySpace(TerminalSize.ONE));
        });



    }

    public static @NotNull Container getArchivedMessageContainer(@NotNull TresorGUI gui, int messageID) {
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        return panel.withBorder(Borders.singleLineReverseBevel());
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
            try {
                gui.getPrivateMessageController().sendMessage(message.getText(), recipient.getSelectedItem());
            } catch (MissingAuthenticationException ignored) {
            }
            window.close();
        });
        panel.addComponent(sendButton);

        window.setComponent(panel);
        gui.getGui().addWindowAndWait(window);
        gui.getGui().removeWindow(window);
        gui.getGui().setActiveWindow(parent);
    }
}
