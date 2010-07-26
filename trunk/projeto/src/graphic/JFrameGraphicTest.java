/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JFrameGraphicTest.java
 *
 * Created on 25/07/2010, 08:23:58
 */
package graphic;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.entities.AirlineNetwork;
import main.heuristic.AircraftRotationParameters;
import main.heuristic.GRASPNetWorkConstruct;
import main.heuristic.GRASPParameters;
import main.reader.AircraftFileReader;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author alexanderdealmeidapinto
 */
public class JFrameGraphicTest extends javax.swing.JFrame {

    private JPanelAircraftControler jpac = new JPanelAircraftControler();

    /** Creates new form JFrameGraphicTest */
    public JFrameGraphicTest() {
        initComponents();
        jScrollPane1.getViewport().add(jpac);
        initGraphics();
        jpac.initConfigures();
    }

    public void initGraphics() {
        AirlineNetwork airlineNetwork = new AirlineNetwork();

        try {
            AircraftFileReader.readDataFromFile("instances/01", airlineNetwork);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        GRASPNetWorkConstruct gRASPNetWorkConstruct = new GRASPNetWorkConstruct(airlineNetwork, GRASPParameters.defaultParameters, AircraftRotationParameters.defaultParameters);
        gRASPNetWorkConstruct.resolve();

        jpac.initChart(airlineNetwork);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("File");

        jMenuItem1.setText("Executar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        initGraphics();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new JFrameGraphicTest().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    
}