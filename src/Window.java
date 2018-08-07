import javax.swing.*;
import java.awt.*;

public class Window extends Canvas
{
    public Window(int width, int height, String title, BattleshipDriver battleship)
    {
        JFrame frame = new JFrame(title);

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(battleship);
        frame.setVisible(true);
    }
}
