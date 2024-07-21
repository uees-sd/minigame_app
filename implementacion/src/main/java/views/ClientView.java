package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ClientView extends JFrame {
    private JLabel questionLabel;
    private JButton answerButton1;
    private JButton answerButton2;
    private JButton answerButton3;
    private JButton answerButton4;
    private JButton loginButton;
    private JButton registerButton;
    private JButton createRoomButton;
    private JButton joinRoomButton;
    private JPanel loginPanel;
    private JPanel roomPanel;
    private JPanel questionPanel;
    private JTextArea messageArea;

    public ClientView() {
        setTitle("Math Quiz Game");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 1));

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginPanel.add(new JLabel("Welcome to the Game!", SwingConstants.CENTER));
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        roomPanel = new JPanel();
        roomPanel.setLayout(new GridLayout(2, 1));
        createRoomButton = new JButton("Create Room");
        joinRoomButton = new JButton("Join Room");
        roomPanel.add(createRoomButton);
        roomPanel.add(joinRoomButton);

        questionLabel = new JLabel("Question will appear here", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        answerButton1 = new JButton("Answer 1");
        answerButton2 = new JButton("Answer 2");
        answerButton3 = new JButton("Answer 3");
        answerButton4 = new JButton("Answer 4");

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(2, 2));
        answerPanel.add(answerButton1);
        answerPanel.add(answerButton2);
        answerPanel.add(answerButton3);
        answerPanel.add(answerButton4);

        questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        questionPanel.add(answerPanel, BorderLayout.CENTER);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(loginPanel, BorderLayout.WEST);
        mainPanel.add(roomPanel, BorderLayout.EAST);
        mainPanel.add(questionPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public void setQuestion(String question, String[] answers) {
        questionLabel.setText(question);
        answerButton1.setText(answers[0]);
        answerButton2.setText(answers[1]);
        answerButton3.setText(answers[2]);
        answerButton4.setText(answers[3]);
    }

    public void addAnswerButtonListener(ActionListener listener) {
        answerButton1.addActionListener(listener);
        answerButton2.addActionListener(listener);
        answerButton3.addActionListener(listener);
        answerButton4.addActionListener(listener);
    }

    public void addLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addRegisterButtonListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void addCreateRoomButtonListener(ActionListener listener) {
        createRoomButton.addActionListener(listener);
    }

    public void addJoinRoomButtonListener(ActionListener listener) {
        joinRoomButton.addActionListener(listener);
    }

    public void showLoginPanel(boolean show) {
        loginPanel.setVisible(show);
    }

    public void showRoomOptions(boolean show) {
        roomPanel.setVisible(show);
    }

    public void enableQuestionPanel(boolean enable) {
        questionLabel.setEnabled(enable);
        answerButton1.setEnabled(enable);
        answerButton2.setEnabled(enable);
        answerButton3.setEnabled(enable);
        answerButton4.setEnabled(enable);
    }

    public void displayMessage(String message) {
        messageArea.append(message + "\n");
    }
}
