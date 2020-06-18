package ru.gb.java1.lesson8;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ru.gb.java1.lesson8.GameMode.HvsAI;
import static ru.gb.java1.lesson8.GameMode.HvsH;

class SettingsWindow extends JFrame {
    private final static int MIN_FIELD_SIZE = 3;
    private final static int MAX_FIELD_SIZE = 10;

    private JLabel gameModeLabel = new JLabel("Game mode:");
    private JRadioButton hvsaiRadio = new JRadioButton("Human vs. AI");
    private JRadioButton hvshRadio = new JRadioButton("Human vs. human");
    private ButtonGroup gameModeGroup = new ButtonGroup();

    private JLabel fieldSizeLabel = new JLabel("Field size: " + MIN_FIELD_SIZE);
    private JSlider fieldSizeSlider = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
    private JLabel winLineLabel = new JLabel("Win line: " + MIN_FIELD_SIZE);
    private JSlider winLineSlider = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);

    private JButton startButton = new JButton("Начать");

    SettingsWindow(final MainWindow mainWindow) {
        setTitle("Новая игра");
        setBounds(300, 300, 400, 400);

        setLayout(new GridLayout(10, 1));
        add(gameModeLabel);
        add(hvsaiRadio);
        add(hvshRadio);
        add(fieldSizeLabel);
        add(fieldSizeSlider);
        add(winLineLabel);
        add(winLineSlider);
        add(startButton);

        hvsaiRadio.setSelected(true);
        gameModeGroup.add(hvsaiRadio);
        gameModeGroup.add(hvshRadio);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameMode gameMode = hvsaiRadio.isSelected() ? HvsAI : HvsH;
                mainWindow.startGame(gameMode, fieldSizeSlider.getValue(), winLineSlider.getValue());
                setVisible(false);
            }
        });

        fieldSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = fieldSizeSlider.getValue();
                fieldSizeLabel.setText("Field size: " + currentValue);
                winLineSlider.setMaximum(currentValue);
            }
        });

        winLineSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = winLineSlider.getValue();
                winLineLabel.setText("Win line: " + currentValue);
            }
        });
    }
}
