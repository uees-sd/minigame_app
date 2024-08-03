package ec.edu.views;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import ec.edu.controllers.ClientController;

public class RoomPanel extends JPanel {
    private ClientController client;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private Image backgroundImage;
    private Map<String, Color> userColors;
    private Random random;

    public RoomPanel(ClientController client) {
        this.client = client;
        this.backgroundImage = new ImageIcon("implementacion/src/main/java/ec/edu/utils/any-colorful-uno-game-cards-on-blue-background-uno-is-an-american-shedding-type-card-ga (1).jpeg").getImage();
        this.userColors = new HashMap<>();
        this.random = new Random();
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel roomLabel = new JLabel("Enter Room Code:");
        roomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roomLabel.setForeground(new Color(0, 51, 102));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(roomLabel, gbc);

        JTextField roomField = new JTextField(10);
        roomField.setBorder(new LineBorder(new Color(0, 51, 102), 1));
        roomField.setFont(new Font("Arial", Font.PLAIN, 14));
        roomField.setBackground(new Color(255, 255, 255));
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(roomField, gbc);

        JButton createRoomButton = new JButton("Create Room");
        styleButton(createRoomButton);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(createRoomButton, gbc);

        JButton joinRoomButton = new JButton("Join Room");
        styleButton(joinRoomButton);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(joinRoomButton, gbc);

        JButton leaveRoomButton = new JButton("Leave Room");
        styleButton(leaveRoomButton);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(leaveRoomButton, gbc);

        JButton startGameButton = new JButton("Start Game");
        styleButton(startGameButton);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(startGameButton, gbc);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("Arial", Font.PLAIN, 14));
        userList.setForeground(new Color(0, 51, 102));
        userList.setBorder(new LineBorder(new Color(0, 51, 102), 1));
        userList.setCellRenderer(new UserCellRenderer());
        JScrollPane userScrollPane = new JScrollPane(userList);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(userScrollPane, gbc);

        createRoomButton.addActionListener(e -> {
            String roomCode = roomField.getText().trim();
            if (!roomCode.isEmpty()) {
                client.setCurrentRoomCode(roomCode);
                client.sendMessage("CREATE_ROOM:" + roomCode + ":" + client.getUsername());
            }
        });

        joinRoomButton.addActionListener(e -> {
            String roomCode = roomField.getText().trim();
            if (!roomCode.isEmpty()) {
                client.setCurrentRoomCode(roomCode);
                client.sendMessage("JOIN_ROOM:" + roomCode + ":" + client.getUsername());
            }
        });

        leaveRoomButton.addActionListener(e -> {
            if (client.getCurrentRoomCode() != null) {
                client.sendMessage("LEAVE_ROOM:" + client.getCurrentRoomCode() + ":" + client.getUsername());
                client.setCurrentRoomCode(null);
                userListModel.clear();
            }
        });

        startGameButton.addActionListener(e -> client.switchToGamePanel());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 51, 102));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new LineBorder(new Color(0, 51, 102), 1));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
    }

    public void addUser(String username) {
        if (!userColors.containsKey(username)) {
            userColors.put(username, getRandomColor());
        }
        userListModel.addElement(username);
    }

    public void removeUser(String username) {
        userListModel.removeElement(username);
        userColors.remove(username);
    }

    private Color getRandomColor() {
        float hue = random.nextFloat();
        float saturation = 0.5f + random.nextFloat() * 0.5f;
        float brightness = 0.7f + random.nextFloat() * 0.3f;
        return Color.getHSBColor(hue, saturation, brightness);
    }

    private class UserCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                String username = (String) value;
                label.setForeground(userColors.get(username));
                label.setFont(new Font("Arial", Font.BOLD, 14));
            }
            return component;
        }
    }
}
