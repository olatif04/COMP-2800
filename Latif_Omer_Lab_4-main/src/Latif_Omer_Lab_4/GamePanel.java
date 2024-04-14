package Latif_Omer_Lab_4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Arc2D;

public class GamePanel extends JPanel implements Runnable {

    private double[] values = new double[4];
    private double rotationAngle = 0.0;
    private Thread animationThread;

    public GamePanel() {
        initUI();
        initAnimationThread();
    }

    private void initUI() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JTextField textField1 = new JTextField(8);
        JTextField textField2 = new JTextField(8);
        JTextField textField3 = new JTextField(8);
        JTextField textField4 = new JTextField(8);

        JButton drawButton = new JButton("Draw");
        JButton rotateButton = new JButton("Rotate");

        addComponents(new JComponent[]{textField1, textField2, textField3, textField4, drawButton, rotateButton});

        drawButton.addActionListener(e -> updateValues(textField1, textField2, textField3, textField4));
        rotateButton.addActionListener(e -> rotate());
    }

    private void addComponents(JComponent[] components) {
        for (JComponent component : components) {
            add(component);
        }
    }

    private void updateValues(JTextField... textFields) {
        try {
            for (int i = 0; i < textFields.length; i++) {
                values[i] = Double.parseDouble(textFields[i].getText());
            }
            repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rotate() {
        rotationAngle += Math.toRadians(90);
        repaint();
    }

    private void initAnimationThread() {
        animationThread = new Thread(this);
        animationThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPieChart(g);
    }

    private void drawPieChart(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int centerX = getWidth() / 2 - 150;
        int centerY = getHeight() / 2 - 150;
        int size = 300;

        double total = 0;
        for (double val : values) total += val;

        double startAngle = Math.toDegrees(rotationAngle);
        for (int i = 0; i < values.length; i++) {
            double extent = 360 * (values[i] / total);
            g2.setColor(getColor(i));
            g2.fill(new Arc2D.Double(centerX, centerY, size, size, startAngle, extent, Arc2D.PIE));
            startAngle += extent;
        }
    }

    private Color getColor(int index) {
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
        return colors[index % colors.length];
    }

    @Override
    public void run() {
        // Animation logic or thread management could be added here if needed
    }
}
