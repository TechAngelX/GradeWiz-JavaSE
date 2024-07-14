import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private JFrame frame;
    private JTextField numComponentsField;
    private JTextField[] weightingFields;
    private JTextField[] scoreFields;
    private int numComponents;
    private double[] weightings;
    private double[] scores;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("GradeWiz ✔");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300); // Adjusted size for a smaller window
        frame.setLocationRelativeTo(null);
        frame.setLayout(new CardLayout());
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);

        createMenuBar();
        showNumComponentsScreen();

        frame.setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());

        JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(e -> showNumComponentsScreen());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(aboutItem);
        fileMenu.add(resetItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);
    }

    private void showAboutDialog() {
        // Define the content of the About dialog with a clickable link
        String message = "<html><h1><b>GradeWiz ✔</b></h1>" +
                "Java SE Version 1.0<br><br>"+
                "A simple app that calculates a final module mark<br>" +
                "based on component weightings and component marks.<br><br>" +
                "© 2024 Ricki Angel<br>" +
                "<a href='https://github.com/TechAngelX/GradeWiz-JavaSE.git'>Visit the GitHub repository</a><br>" +
                "</html>";

        // Create the About dialog with the message and set it to be clickable
        JOptionPane.showMessageDialog(frame,
                new JLabel(message) { // Use JLabel to display the HTML content
                    @Override
                    public void setText(String text) {
                        super.setText(text);
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover
                        addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseClicked(java.awt.event.MouseEvent e) {
                                try {
                                    java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://github.com/TechAngelX/GradeWiz-JavaSE.git"));
                                } catch (java.io.IOException ioException) {
                                    ioException.printStackTrace(); // Handle the exception if the URL cannot be opened
                                }
                            }
                        });
                    }
                },
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    private void showNumComponentsScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Number of components (1-5):");
        numComponentsField = new JTextField(2);
        numComponentsField.setPreferredSize(new Dimension(50, 30));
        JButton nextButton = new JButton("Next");

        nextButton.addActionListener(e -> {
            try {
                numComponents = Integer.parseInt(numComponentsField.getText());
                if (numComponents < 1 || numComponents > 5) {
                    throw new NumberFormatException();
                }
                weightings = new double[numComponents];
                scores = new double[numComponents];
                showWeightingScreen();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a number of components between 1 and 5.");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(numComponentsField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(nextButton, gbc);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showWeightingScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        weightingFields = new JTextField[numComponents];
        JLabel label = new JLabel("Enter % weightings for each component:");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(label, gbc);

        gbc.gridwidth = 1;
        for (int i = 0; i < numComponents; i++) {
            JLabel componentLabel = new JLabel("Component " + (i + 1) + ":");

            JTextField weightingField = new JTextField(2);
            weightingField.setPreferredSize(new Dimension(5, 30));
            weightingFields[i] = weightingField;

            JPanel weightingPanel = new JPanel(new BorderLayout());
            weightingPanel.add(weightingField, BorderLayout.CENTER);
            weightingPanel.add(new JLabel("%"), BorderLayout.EAST);

            gbc.gridy = i + 1;
            gbc.gridx = 0;
            panel.add(componentLabel, gbc);

            gbc.gridx = 1;
            panel.add(weightingPanel, gbc);
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showNumComponentsScreen());

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            try {
                double totalWeight = 0;
                for (int i = 0; i < numComponents; i++) {
                    weightings[i] = Double.parseDouble(weightingFields[i].getText());
                    if (weightings[i] < 0) throw new NumberFormatException();
                    totalWeight += weightings[i];
                }

                if (totalWeight != 100) {
                    throw new NumberFormatException();
                }

                showScoreScreen();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid weightings that sum up to 100%.");
            }
        });

        gbc.gridy = numComponents + 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(backButton, gbc);

        gbc.gridx = 1;
        panel.add(nextButton, gbc);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showScoreScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        scoreFields = new JTextField[numComponents];
        JLabel label = new JLabel("Enter scores for each component:");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(label, gbc);

        gbc.gridwidth = 1;
        for (int i = 0; i < numComponents; i++) {
            JLabel componentLabel = new JLabel("Component " + (i + 1) + " score:");

            JTextField scoreField = new JTextField(5);
            scoreField.setPreferredSize(new Dimension(60, 30));
            scoreFields[i] = scoreField;

            // Creating a JPanel to hold the JTextField and % JLabel
            JPanel scorePanel = new JPanel(new BorderLayout());
            scorePanel.add(scoreField, BorderLayout.CENTER);
            scorePanel.add(new JLabel("%"), BorderLayout.EAST);

            gbc.gridy = i + 1;
            gbc.gridx = 0;
            panel.add(componentLabel, gbc);

            gbc.gridx = 1;
            panel.add(scorePanel, gbc);
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showWeightingScreen());

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> {
            try {
                for (int i = 0; i < numComponents; i++) {
                    scores[i] = Double.parseDouble(scoreFields[i].getText());
                }
                showResultScreen();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers for the scores.");
            }
        });

        gbc.gridy = numComponents + 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(backButton, gbc);

        gbc.gridx = 1;
        panel.add(calculateButton, gbc);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }
    private void showResultScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        double totalMark = calculateTotalMark();
        JLabel resultLabel = new JLabel("Total module mark: " + String.format("%.2f", totalMark));
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultLabel.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(resultLabel, gbc);

        for (int i = 0; i < numComponents; i++) {
            JLabel componentLabel = new JLabel("Component " + (i + 1) + " mark:");

            // Format the mark and weighting percentage for display
            JLabel markLabel = new JLabel(String.format("%.2f (%.0f%%)", scores[i] * (weightings[i] / 100), weightings[i]));

            // Create a panel for the mark label and percentage sign
            JPanel endScorePanel = new JPanel(new BorderLayout());
            endScorePanel.add(markLabel, BorderLayout.CENTER);
            // No need to add another JLabel("%") since the percentage is already included in markLabel

            gbc.gridy = i + 1;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            panel.add(componentLabel, gbc);

            gbc.gridx = 1;
            panel.add(endScorePanel, gbc);
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showScoreScreen());

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> showNumComponentsScreen());

        gbc.gridy = numComponents + 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;  // Reset weightx for buttons
        panel.add(backButton, gbc);

        gbc.gridx = 1;
        panel.add(restartButton, gbc);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }


    private double calculateTotalMark() {
        double totalMark = 0;
        for (int i = 0; i < numComponents; i++) {
            totalMark += scores[i] * (weightings[i] / 100);
        }
        return totalMark;
    }
}
