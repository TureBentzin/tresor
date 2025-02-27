package net.juligames.tresor.views;


import com.googlecode.lanterna.gui2.Window;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.utils.ViewUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ture Bentzin
 * @since 27-02-2025
 */
public class PrivateMessagesView {

    private static final @NotNull Logger log = LoggerFactory.getLogger(PrivateMessagesView.class);

    public static @NotNull Window getPrivateMessagesWindow(@NotNull TresorGUI gui) {
        return ViewUtils.authenticatedWindow(gui, "private_messages", window -> {
            log.info("Creating private messages window");
        });
    }
}
