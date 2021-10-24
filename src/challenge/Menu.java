package challenge;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    public static String gameS = "game";
    public static String optionsS = "options";
    public JButton game;
    public JButton options;

    public Menu() {
        setLayout(null);

        game = new JButton(gameS);
        game.setActionCommand(gameS);
        game.addActionListener(this);
        game.setBounds(0, 620, 80, 40);
        add(game);

        options = new JButton(optionsS);
        options.setActionCommand(optionsS);
        options.addActionListener(this);
        options.setBounds(0, 660, 80, 40);
        add(options);

        setBackground(Color.RED);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(gameS)) {
            JPanel jp = new Game();
            Main.frame.setContentPane(jp);
            Main.frame.pack();
            jp.requestFocusInWindow();
        } else if (e.getActionCommand().equals(optionsS)) {
            JPanel jp = new Options();
            Main.frame.setContentPane(jp);
            Main.frame.pack();
            jp.requestFocusInWindow();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700, 700);
    }

}
