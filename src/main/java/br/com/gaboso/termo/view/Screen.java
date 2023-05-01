package br.com.gaboso.termo.view;

import br.com.gaboso.termo.controller.TermoController;
import br.com.gaboso.termo.core.exception.LanguageException;
import br.com.gaboso.termo.enums.LanguageEnum;
import com.formdev.flatlaf.FlatDarculaLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

public class Screen {

    private final TermoController termoController = new TermoController();

    private JFrame mainFrame;

    private JTextField textFieldLettersUncertain;
    private JTextField textFieldLettersCertain;
    private JTextField textFieldLettersDontHave;
    private JList<String> listDictionary;

    private Screen() {
        initialize();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FlatDarculaLaf.setup();
                UIManager.setLookAndFeel(new FlatDarculaLaf());
                Screen window = new Screen();
                window.mainFrame.setVisible(true);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
    }

    private void initialize() {
        mainFrame = this.buildMainFrame();

        JLabel labelLettersUncertain = this.buildLabel("Letters which has, but don't know the right position");
        mainFrame.getContentPane().add(labelLettersUncertain, "cell 0 0,grow");

        textFieldLettersUncertain = this.buildField();
        mainFrame.getContentPane().add(textFieldLettersUncertain, "cell 0 1,grow");

        JLabel labelLettersCertain = this.buildLabel("Letters which know the right position");
        mainFrame.getContentPane().add(labelLettersCertain, "cell 0 2,grow");

        textFieldLettersCertain = this.buildField();
        mainFrame.getContentPane().add(textFieldLettersCertain, "cell 0 3,grow");

        JLabel labelLettersDontHave = this.buildLabel("Letters which know dont have");
        mainFrame.getContentPane().add(labelLettersDontHave, "cell 0 4,grow");

        textFieldLettersDontHave = this.buildField();
        mainFrame.getContentPane().add(textFieldLettersDontHave, "cell 0 5,grow");

        JLabel labelDicitionary = this.buildLabel("Select the dictionary:");
        mainFrame.getContentPane().add(labelDicitionary, "cell 0 6,grow");

        Object[] languages = Arrays.stream(LanguageEnum.values()).map(LanguageEnum::getTitle).toArray();
        listDictionary = new JList(languages);
        listDictionary.setSelectedIndex(0);
        mainFrame.getContentPane().add(listDictionary, "cell 0 7,grow");


        JButton buttonClearFields = this.buildButton("Clear Fields");
        buttonClearFields.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                clearFields();
            }
        });
        mainFrame.getContentPane().add(buttonClearFields, "cell 0 8,grow");

        JButton buttonFindWords = this.buildButton("Find Words");
        buttonFindWords.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                findWords();
            }
        });
        mainFrame.getContentPane().add(buttonFindWords, "cell 0 9,grow");
    }

    private JFrame buildMainFrame() {
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setTitle("Termo Killer");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new MigLayout("", "8[384px]8", "8[24px][24px]16[24px][24px]16[24px][24px]16[24px][24px]16"));
        return frame;
    }

    private JLabel buildLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.LEADING);
        label.setOpaque(true);
        return label;
    }

    private JTextField buildField() {
        JTextField field = new JTextField();
        field.setColumns(10);

        return field;
    }

    private JButton buildButton(String text) {
        final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

        JButton button = new JButton(text);
        button.setMargin(new Insets(2, 2, 2, 2));
        button.setCursor(handCursor);
        button.setBorder(new EmptyBorder(0, 0, 0, 0));

        return button;
    }

    private void clearFields() {
        textFieldLettersUncertain.setText("");
        textFieldLettersCertain.setText("");
        textFieldLettersDontHave.setText("");
    }

    private void findWords() {
        String lettersDontHave = textFieldLettersDontHave.getText();
        String lettersCertain = textFieldLettersCertain.getText();
        String lettersUncertain = textFieldLettersUncertain.getText();

        if (termoController.isInvalid(lettersDontHave) && termoController.isInvalid(lettersCertain) && termoController.isInvalid(lettersUncertain)) {
            JOptionPane.showMessageDialog(mainFrame, "Give some hint", "Termo Killer", JOptionPane.WARNING_MESSAGE);
        }

        String selectedDictionary = listDictionary.getSelectedValue();
        try {
            LanguageEnum language = LanguageEnum.fromTitle(selectedDictionary);
            List<String> suggestions = termoController.getSuggestions(lettersDontHave, lettersCertain, lettersUncertain, language);
            JOptionPane.showMessageDialog(mainFrame, String.join("\n", suggestions), "Termo Killer", JOptionPane.INFORMATION_MESSAGE);
        } catch (LanguageException e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
