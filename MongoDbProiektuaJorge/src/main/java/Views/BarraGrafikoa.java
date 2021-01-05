/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import com.mongodb.client.FindIterable;
import static exekutagarri.StudentsExekutagarri.datuakHartuZatika;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import model.Score;
import model.Student;

/**
 *
 * @author Jorge
 */
public class BarraGrafikoa extends JFrame {

    int xpos = 250;
    int xsize = 800;
    int ysize = 850;
    FindIterable<Student> estadisticas;

    public static void main(String[] args) throws InterruptedException {
        try {
            JFrame barraGrafikoa = new BarraGrafikoa(datuakHartuZatika(5, -1));
            barraGrafikoa.setVisible(true);
        } catch (Exception e) {
            System.err.println("Errore bat gertatu da.\n" + e);
        }
    }

    public BarraGrafikoa(FindIterable<Student> estadisticas) {
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(xsize, ysize);
        this.setTitle("Barra Motako Grafikoa");
        this.estadisticas = estadisticas;
    }

    @Override
    public void paint(Graphics g) {
        int ypos = 20;
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fontMetrics = g2d.getFontMetrics();

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", 20, 20));
        
        String titulo = "NOTA ALTUEN BARRA GRAFIKOA";
        g2d.drawString(titulo, xsize / 2 - fontMetrics.stringWidth(titulo), 75);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", 14, 15));

        int i = 0;
        
        for (Student student : this.estadisticas) {
            // TODO: Pintar las barras, por student

            String name = student.getName();
            // Para solo un nombre de cada y que no se repita.
            g2d.setColor(Color.BLACK);
            g2d.drawString(name, 150 - fontMetrics.stringWidth(name), 150  + ypos);
            
            int j = 0;
            for (Score score : student.getScores()) {
            
                // Pintar los types(scores) alineado a la derecha
                g2d.setColor(Color.BLACK);              

                // Cacular % del tamaño de la barra
                double porcentaje = (double) (score.getScore()) / 100;

                int finalYPos = 80 + ypos;
                
                if (j == 0) {
                    finalYPos += 10;
                } else if (j == 2) {
                    finalYPos -= 10;
                }
                
                // Pintar los nombres de las barras
                g2d.drawString(score.getType(), xpos - fontMetrics.stringWidth(score.getType()), finalYPos + 20);
                
                // Setear el color
                if(score.getScore() < 50.00){
                    g2d.setColor(Color.red);
                }else if(score.getScore() > 50 && score.getScore() < 75){
                    g2d.setColor(Color.blue);
                }else if (score.getScore() > 75){
                    g2d.setColor(Color.green);
                }
                
                // Printear la barra
                g2d.fillRect(20 + xpos, finalYPos, (int) (300 * porcentaje), 25);

                // Printear los numeros del final de la barra.
                g2d.setColor(Color.BLACK);
                
                double roundScore = Math.round(score.getScore() * 100) / 100.0;
                // Ejemplo: 28.578954654 // 2857.8954654 // 2858 // 28.58               
                g2d.drawString(Double.toString(roundScore), (int) (300 * porcentaje) + xpos + 30, finalYPos + 20);

                ypos += 50;
                i++;
                j++;
            }
        }

    }

}