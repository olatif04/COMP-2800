package Latif_Omer_Lab_4;

import javax.swing.*;

public class Main


{
    public static void main(String args[]){
        JFrame mainWindow = new JFrame("Pie Chart");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(700, 500);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setResizable(false);


        GamePanel gamePanel = new GamePanel();
        mainWindow.add(gamePanel);
        mainWindow.setVisible(true);

        // start our game loop
//        gamePanel.start();
    }
}