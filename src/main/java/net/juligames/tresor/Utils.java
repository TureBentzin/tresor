package net.juligames.tresor;

import com.googlecode.lanterna.gui2.BasePane;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Container;
import com.googlecode.lanterna.gui2.Interactable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }


    public static void findFocus(@NotNull BasePane basePane, @NotNull Container container) {
        List<Component> children = container.getChildrenList();
        for (Component component : children) {
            if (component instanceof Interactable) {
                basePane.setFocusedInteractable((Interactable) component);
                return;
            }
            if (component instanceof Container) {
                findFocus(basePane, (Container) component);
                // Stop searching if focus is already set
                if (basePane.getFocusedInteractable() != null) {
                    return;
                }
            }
        }
    }
}
