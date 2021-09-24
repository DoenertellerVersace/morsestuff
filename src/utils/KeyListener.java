package utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class KeyListener extends JFrame
    implements java.awt.event.KeyListener, ActionListener {

    static JTextArea displayArea;
    static final String newline = System.getProperty("line.separator");

    static int lastKeyPressed;
    static long pressedLastKeyTime;
    static long releasedLastKeyTime = 0;
    static boolean stoppedListening = false;

    static final StringBuilder byteStringBuilder = new StringBuilder();

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                displayArea.setText("Listening...");
            }
        });
    }

    private static void createAndShowGUI() {
        KeyListener frame = new KeyListener("Morse Code Recording");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                displayArea.requestFocusInWindow();
            }
        });

        frame.addComponentsToPane();

        frame.pack();
        frame.setVisible(true);
    }

    private void addComponentsToPane() {

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFocusable(true);
        displayArea.addKeyListener(this);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 30));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(375, 125));

        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    public KeyListener(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE && keyCode != lastKeyPressed && !stoppedListening) {
            displayArea.setText("Recording...");
            lastKeyPressed = keyCode;
            pressedLastKeyTime = System.currentTimeMillis();
            if (!(releasedLastKeyTime == 0)) {
                addByteString(pressedLastKeyTime - releasedLastKeyTime, false);
            }
        } else if (keyCode == KeyEvent.VK_ENTER) {
            stoppedListening = true;
            releasedLastKeyTime = 0;
            displayArea.setText("Pausing...");
            System.out.println(byteStringBuilder);
            byteStringBuilder.setLength(0);
//            translateToMorse(byteStringBuilder);
        } else if (keyCode == KeyEvent.VK_PLUS) {
            stoppedListening = false;
            displayArea.setText("Listening...");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE && keyCode == lastKeyPressed) {
            releasedLastKeyTime = System.currentTimeMillis();
            addByteString(releasedLastKeyTime - pressedLastKeyTime, true);
            lastKeyPressed = 0;
        }
    }

    private String translateToMorse(StringBuilder stringBuilder) {
        String input = stringBuilder.toString();
        StringBuilder morseSB = new StringBuilder();
        return morseSB.toString();
    }

    private void addByteString(long duration, boolean signal) {
        StringBuilder morseSignal = new StringBuilder();
        if (duration > 1200) {
            byteStringBuilder.append(newline);
        }
        for (int i = 0; i < (duration / 10); i++) {
            morseSignal.append(signal ? 1 : 0);
        }
        byteStringBuilder.append(morseSignal).append(" ");
    }
}