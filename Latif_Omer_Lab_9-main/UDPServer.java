import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.*;

public class UDPServer extends JPanel implements KeyListener {
    private BufferedImage avatarImage;
    private BufferedImage equipmentImage;
    private BufferedImage[] gearOptions;
    private int gearIndex = 0;
    private int avatarPosX = 200;
    private int avatarPosY = 200;

    public UDPServer() {
        try {
            // Load avatar and equipment images
            avatarImage = ImageIO.read(new File("player.png"));
            equipmentImage = ImageIO.read(new File("armors.png"));

            // Initialize gear options
            int totalOptions = 6; // Includes base option (no gear)
            gearOptions = new BufferedImage[totalOptions];
            int singleWidth = equipmentImage.getWidth() / 3;
            int singleHeight = equipmentImage.getHeight() / 2;

            // First row gear options
            for (int i = 0; i < 3; i++) {
                gearOptions[i] = equipmentImage.getSubimage(i * singleWidth, 0, singleWidth, singleHeight);
            }

            // Second row gear options
            for (int i = 0; i < 3; i++) {
                gearOptions[i + 3] = equipmentImage.getSubimage(i * singleWidth, singleHeight, singleWidth, singleHeight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(600, 400));
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the avatar
        g.drawImage(avatarImage, avatarPosX, avatarPosY, this);

        // Apply selected gear
        if (gearIndex != 0) {
            BufferedImage chosenGear = gearOptions[gearIndex - 1];
            g.drawImage(chosenGear, avatarPosX, avatarPosY, this);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                avatarPosY -= 20;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                avatarPosY += 20;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                avatarPosX -= 20;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                avatarPosX += 20;
                break;
            default:
                break;
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame appFrame = new JFrame("Avatar Customization Panel");
        UDPServer panel = new UDPServer();
        appFrame.add(panel);
        appFrame.pack();
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setLocationRelativeTo(null);
        appFrame.setVisible(true);

        try {
            DatagramSocket serverSocket = new DatagramSocket(1234);

            while (true) {
                DatagramPacket incomingPacket = new DatagramPacket(new byte[1024], 1024);
                serverSocket.receive(incomingPacket);

                String clientMessage = new String(incomingPacket.getData()).trim();
                int selectedGear = Integer.parseInt(clientMessage);
                panel.gearIndex = selectedGear;

                byte[] confirmMessage = "Gear choice acknowledged".getBytes();
                DatagramPacket confirmationPacket = new DatagramPacket(confirmMessage, confirmMessage.length, incomingPacket.getAddress(), incomingPacket.getPort());
                serverSocket.send(confirmationPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
