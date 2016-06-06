/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 *
 * @author Zygis
 */
public class PanelForm extends javax.swing.JPanel {
    private BufferedImage image;

    public PanelForm(String path_to_image) {
        try {
            image = ImageIO.read(getClass().getResource(path_to_image));
        } catch (IOException ex) {
            ex.printStackTrace();
        }  
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

}

