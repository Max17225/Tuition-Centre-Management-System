package Util;

import javax.swing.*;
import java.awt.*;

public class WindowUtils {

    /**
     * Centers a given window (JFrame or JDialog) on the screen.
     */
    public static void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }

    /**
     * Opens a new JFrame centered on screen and optionally disposes the current frame.
     *
     * @param newFrame       the new JFrame to show
     * @param disposeCurrent whether to close the current frame
     * @param currentFrame   the current frame to dispose (if applicable)
     */
    public static void openCenteredFrame(JFrame newFrame, boolean disposeCurrent, JFrame currentFrame) {
        newFrame.pack();
        centerWindow(newFrame);
        newFrame.setResizable(false);
        newFrame.setVisible(true);

        if (disposeCurrent && currentFrame != null) {
            currentFrame.dispose();
        }
    }
}
