package net.juligames.tresor.views.test;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.BasicTextImage;
import com.googlecode.lanterna.graphics.TextImage;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import net.juligames.tresor.Tresor;
import net.juligames.tresor.TresorGUI;
import net.juligames.tresor.views.TresorWindow;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

public class ColorTestView {

    private static final @NotNull Logger log = LoggerFactory.getLogger(ColorTestView.class);


    public static @NotNull Window getColorTestWindow(@NotNull TresorGUI gui) {
        TresorWindow window = new TresorWindow(gui, "window.color-test", false);
        window.setHints(Set.of(Window.Hint.CENTERED, Window.Hint.FIT_TERMINAL_WINDOW));

        Panel contentPanel = window.getContentPanel();
        contentPanel.addComponent(new Label(gui.getText("window.color-test.content", false)));

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.addItem("/color.png");

        contentPanel.addComponent(comboBox);

        ImageComponent imageComponent = new ImageComponent();

        try {
            TextImage image = fromPNG(comboBox.getText(), gui.getGui().getScreen().getTerminalSize());
            imageComponent.setTextImage(image);
            contentPanel.addComponent(imageComponent);
        } catch (Exception e) {
            Label errorLabel = new Label("Failed to load image: " + e.getMessage());
            contentPanel.addComponent(errorLabel);
        }
        return window;
    }

    /**
     * Load a PNG image from the resources and convert it to a TextImage
     * @param resourcePath The path to the PNG image in the resources
     * @param maximumSize The maximum size of the resulting TextImage
     * @return The TextImage representing the PNG image
     */
    public static @NotNull TextImage fromPNG(@NotNull String resourcePath, @NotNull TerminalSize maximumSize) {
        try (InputStream imageStream = ColorTestView.class.getResourceAsStream(resourcePath)) {
            if (imageStream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }

            BufferedImage bufferedImage = ImageIO.read(imageStream);
            if (bufferedImage == null) {
                throw new RuntimeException("Failed to read the image. Ensure it's a valid PNG file.");
            }

            int targetWidth = Math.min(bufferedImage.getWidth(), maximumSize.getColumns());
            int targetHeight = Math.min(bufferedImage.getHeight(), maximumSize.getRows());
            BufferedImage resizedImage = resizeImage(bufferedImage, targetWidth, targetHeight);

            TerminalSize terminalSize = new TerminalSize(targetWidth, targetHeight);
            TextImage textImage = new BasicTextImage(terminalSize);

            for (int y = 0; y < terminalSize.getRows(); y++) {
                for (int x = 0; x < terminalSize.getColumns(); x++) {
                    int rgb = resizedImage.getRGB(x, y);
                    TextCharacter c = TextCharacter.fromCharacter(' ', TextColor.ANSI.WHITE,
                            new TextColor.RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF))[0];
                    textImage.setCharacterAt(x, y, c);
                }
            }
            return textImage;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image from resources: " + resourcePath, e);
        }
    }

    /**
     * Resize the image before converting it to a TextImage
     * @param originalImage The original image to resize
     * @param width The target width
     * @param height The target height
     * @return The resized image
     */
    private static @NotNull BufferedImage resizeImage(@NotNull BufferedImage originalImage, int width, int height) {
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();
        return resizedImage;
    }

}
