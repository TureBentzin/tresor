package net.juligames.tresor.utils;


import ch.qos.logback.core.net.SocketConnector;
import com.googlecode.lanterna.gui2.TextGUIThread;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import net.juligames.tresor.TresorGUI;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author Ture Bentzin
 * @since 21-02-2025
 */
public class TresorExceptionHandler implements TextGUIThread.ExceptionHandler {

    private @NotNull
    final TresorGUI gui;

    private static final @NotNull Logger log = LoggerFactory.getLogger(TresorExceptionHandler.class);

    public TresorExceptionHandler(@NotNull TresorGUI gui) {
        this.gui = gui;
    }

    @Override
    public boolean onIOException(@NotNull IOException e) {
        log.error("IOException - killing Thread", e);
        return true;
    }

    @Override
    public boolean onRuntimeException(@NotNull RuntimeException e) {
        showError(e);
        return true;
    }

    private void showError(@NotNull Exception e) {
        gui.getGui().getActiveWindow().close();
        int errorID = Objects.hash(gui, e);

        String message = e.getMessage() + "\n Error ID: " + errorID;

        log.error("Error ID: {}", errorID, e);

        gui.getGui().addWindowAndWait(new MessageDialogBuilder()
                .setTitle("Error: " + e.getClass().getSimpleName())
                .setText(message)
                .addButton(MessageDialogButton.OK)
                .setExtraWindowHints(List.of(Window.Hint.FIT_TERMINAL_WINDOW))
                .build());
    }
}
