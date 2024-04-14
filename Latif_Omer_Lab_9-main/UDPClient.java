import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        final InetAddress serverIP;
        final int serverPort = 1234;
        DatagramSocket clientSocket;

        try {
            serverIP = InetAddress.getByName("127.0.0.1");
            clientSocket = new DatagramSocket();

            // Equipment selection dropdown
            String[] equipmentChoices = {"None", "Gear 1", "Gear 2", "Gear 3", "Gear 4", "Gear 5"};
            JComboBox<String> equipmentDropdown = new JComboBox<>(equipmentChoices);
            equipmentDropdown.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedGear = String.valueOf(equipmentDropdown.getSelectedIndex());
                    byte[] serverData = selectedGear.getBytes();
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(serverData, serverData.length, serverIP, serverPort);
                        clientSocket.send(sendPacket);
                        System.out.println("Equipment selection sent to server: " + selectedGear);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            JFrame selectionFrame = new JFrame("Select Your Equipment");
            selectionFrame.setLayout(new BorderLayout());
            selectionFrame.add(new JLabel("Choose Equipment:"), BorderLayout.NORTH);
            selectionFrame.add(equipmentDropdown, BorderLayout.CENTER);
            selectionFrame.pack();
            selectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            selectionFrame.setLocationRelativeTo(null);
            selectionFrame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
