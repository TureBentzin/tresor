package net.juligames.tresor;


import com.googlecode.lanterna.TextColor;
import org.jetbrains.annotations.NotNull;

import static com.googlecode.lanterna.TextColor.ANSI.*;

/**
 * @author Ture Bentzin
 * @since 17-02-2025
 */
public record Theme(String name, TextColor backgroundColor, TextColor foregroundColor,
                    TextColor highlightBackgroundColor, TextColor highlightForegroundColor, TextColor borderColor,

                    char borderHorizontal, char borderVertical) {

    public Theme {
        if (borderColor == null) {
            borderColor = backgroundColor;
        }
    }

    public static @NotNull TextColor invert(@NotNull TextColor textColor) {
        int r = 255 - textColor.getRed(), g = 255 - textColor.getGreen(), b = 255 - textColor.getBlue();
        return new TextColor.RGB(r, g, b);
    }

    private Theme(@NotNull String name, @NotNull TextColor backgroundColor, @NotNull TextColor contentColor, @NotNull TextColor highlightColor) {
        this(name, backgroundColor, contentColor, highlightColor, invert(highlightColor), contentColor, ' ', ' ');
    }

    private Theme(@NotNull String name, @NotNull TextColor color, @NotNull TextColor highlightColor) {
        this(name, color, invert(color), highlightColor, invert(highlightColor), color, ' ', ' ');
    }

    public static final @NotNull Theme DEFAULT = new Theme("Default", YELLOW_BRIGHT, BLACK, BLUE, WHITE, BLACK, ' ', ' ');

    public static final @NotNull Theme VAMPIRE = new Theme("Vampire", BLACK, WHITE, BLACK, RED, BLUE,

            '─', '│');


}
