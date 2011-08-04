/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JFrameGraphicTest.java
 *
 * Created on 25/07/2010, 08:23:58
 */
package gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import main.entities.AirlineNetwork;
import main.entities.ExecutionInfo;
import main.entities.Flight;
import main.heuristic.ARPParameters;
import main.heuristic.GRASPConstruct;
import main.heuristic.GRASPParameters;
import main.heuristic.SolverManager;
import main.reader.ARPFileReader;
import util.InstanceUtil;
import util.SolutionUtil;
import util.TimeUtil;

/**
 *
 * @author alexanderdealmeidapinto
 */
public class JFrameGraphicTest extends javax.swing.JFrame {

    public static JFrameGraphicTest instance;
    JFileChooser fileChooserFormattedInstance = new JFileChooser("/home/alexander/Documents/Documents/Mestrado/Projeto/svn/trunk/AIRotation/instances");
    private JPanelARPControler jpac = new JPanelARPControler();
    JDialogResultInfo jDialogResultInfo = null;
    private AirlineNetwork airlineNetwork;

    /** Creates new form JFrameGraphicTest */
    public JFrameGraphicTest() {
        initComponents();
        jScrollPane1.getViewport().add(jpac);
        instance = this;

        jDialogResultInfo = new JDialogResultInfo(this, false);
        fileChooserFormattedInstance.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //        new Thread() {
//
//            @Override
//            public void run() {
//                initGraphics();
//
//            }
//        }.start();
    }

    public void initSystem(File inputFile) {
        this.airlineNetwork = new AirlineNetwork(inputFile.getAbsolutePath());
        System.out.println("Instancia " + airlineNetwork.getPathInstance() + " Numero de voos " + airlineNetwork.getFlights().size());
        try {
            ARPFileReader.readDataFromFile(airlineNetwork.getPathInstance(), airlineNetwork);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        jpac.initChart(airlineNetwork);
    }

    public void loadInstance() {
        fileChooserFormattedInstance.showOpenDialog(null);
        File inputFile = fileChooserFormattedInstance.getSelectedFile();
        initSystem(inputFile);
    }

    public void initGraphics(boolean usingsolver) {


        fileChooserFormattedInstance.showOpenDialog(null);
        File inputFile = fileChooserFormattedInstance.getSelectedFile();
        ArrayList<Integer> conts = new ArrayList<Integer>();
        int menorcusto = 9999999;

        jpac.setStop(false);

        int cont = 0;

        cont++;

        initSystem(inputFile);

        long init = System.currentTimeMillis();

        try {
            SolverManager.executeSolver(airlineNetwork, usingsolver);
        } catch (Exception ex) {
            Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
        }

//        if (!usingsolver) {
////            GRASPConstruct gRASPConstruct = new GRASPConstruct(airlineNetwork, GRASPParameters.defaultParameters, ARPParameters.defaultParameters);
////            gRASPConstruct.GRASPResolve();
//            try {
//                SolverManager.executeSolver(airlineNetwork, usingsolver);
//            } catch (Exception ex) {
//                Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//
//            try {
//                SolverManager.executeSolver(airlineNetwork, usingsolver);
//            } catch (Exception ex) {
//                Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }

        long duration = System.currentTimeMillis() - init;


        jpac.initChart(airlineNetwork);

        jpac.initConfigures();

        if (menorcusto > airlineNetwork.getBestNetworkCost()) {
            menorcusto = airlineNetwork.getBestNetworkCost();
        }

        conts.add(cont);

        ExecutionInfo executionInfo = new ExecutionInfo();
        executionInfo.configure(airlineNetwork, duration);

        jDialogResultInfo.configure(executionInfo);

        jDialogResultInfo.setVisible(true);



    }

    public static void setPercentComplete(int value) {
        instance.getJPanelAircraftControler().setPercentComplete(value);
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
        jMenuItemExecuteNormal = new javax.swing.JMenuItem();
        jMenuItemExecuteFromSolver = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("File");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItemExecuteNormal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.META_MASK));
        jMenuItemExecuteNormal.setText("Executar");
        jMenuItemExecuteNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExecuteNormalActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemExecuteNormal);

        jMenuItemExecuteFromSolver.setText("Executar com o SOLVER");
        jMenuItemExecuteFromSolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExecuteFromSolverActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemExecuteFromSolver);

        jMenuItem6.setText("Carregar Instância");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ferramentas");

        jMenuItem2.setText("Configurações");
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Formatar resultado");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Completar instância");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("Gerar instancia para o solver");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem7.setText("Escrever arquivo de entrada");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem1.setText("Extender instância");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem8.setText("Formatar voos para o Artigo");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem9.setText("Formatar tempos para o Artigo");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Artigo");
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        jMenuItem10.setText("Gerar solução para o artigo");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem10);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemExecuteNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExecuteNormalActionPerformed
        // TODO add your handling code here:


        new Thread() {

            @Override
            public void run() {

                initGraphics(false);

                long init = System.currentTimeMillis();
                //InstanceUtil.generatePieces(jpac.getAirlineNetwork());
                long time = System.currentTimeMillis() - init;

                System.out.println("Tempo = " + time / 1000);
            }
        }.start();
    }//GEN-LAST:event_jMenuItemExecuteNormalActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        if (fileChooserFormattedInstance.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooserFormattedInstance.getSelectedFile();
            try {
                SolutionUtil.writeFormatedSolutionForInstance(jpac.getAirlineNetwork().getBestNetwork(), selectedFile);
                JOptionPane.showMessageDialog(null, "Solução gravada com sucesso no arquivo " + selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        InstanceUtil.generatePieces(jpac.getAirlineNetwork());
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        if (fileChooserFormattedInstance.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooserFormattedInstance.getSelectedFile();
            try {
                SolutionUtil.writeFormatedAirlineNetworkForCplex(jpac.getAirlineNetwork(), selectedFile);
                JOptionPane.showMessageDialog(null, "Solução gravada com sucesso no arquivo " + selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        if (fileChooserFormattedInstance.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooserFormattedInstance.getSelectedFile();
            try {
                SolutionUtil.writeFormatedInputAirlineNetwork(jpac.getAirlineNetwork(), selectedFile);
                JOptionPane.showMessageDialog(null, "Solução gravada com sucesso no arquivo " + selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItemExecuteFromSolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExecuteFromSolverActionPerformed
        new Thread() {

            @Override
            public void run() {

                initGraphics(true);

                long init = System.currentTimeMillis();
                //InstanceUtil.generatePieces(jpac.getAirlineNetwork());
                long time = System.currentTimeMillis() - init;

                System.out.println("Tempo = " + time / 1000);
            }
        }.start();
    }//GEN-LAST:event_jMenuItemExecuteFromSolverActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        BufferedWriter bw = null;
        try {
            int oneday = 24 * 60;
            // TODO add your handling code here:
            fileChooserFormattedInstance.showOpenDialog(null);
            File inputFile = fileChooserFormattedInstance.getSelectedFile();
            initSystem(inputFile);
            File outputFlights = new File(inputFile.getAbsolutePath() + File.separator + "voos_extended.txt");
            bw = new BufferedWriter(new FileWriter(outputFlights));
            for (int i = 0; i < 7; i++) {
                for (Flight flight : airlineNetwork.getFlights()) {
                    if (i != 0) {
                        bw.write(flight.getName() + "_" + i);
                    } else {
                        bw.write(flight.getName());
                    }

                    bw.write(" ");
                    bw.write(TimeUtil.getFormatedTime(flight.getDepartureTime() + (i * oneday)) + " ");
                    bw.write(TimeUtil.getFormatedTime(flight.getArrivalTime() + (i * oneday)) + " ");
                    bw.write(flight.getDepartureCity().getName() + " " + flight.getArrivalCity().getName());
                    bw.write("\n");
                }
            }

            JOptionPane.showMessageDialog(null, "Instância extendida gerada com sucesso.\n" + outputFlights.getAbsolutePath(), "Geração de instância extendida", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao gerar Instância extendida.\n", "Geração de instância extendida", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }



    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        loadInstance();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:

        JFileChooser chooser = new JFileChooser("");

        chooser.showSaveDialog(null);

        File seleFile = chooser.getSelectedFile();
        try {
            SolutionUtil.writeFormatedInputFlights(jpac.getAirlineNetwork(), seleFile);
            JOptionPane.showMessageDialog(null, "Salvo com sucesso");
        } catch (IOException ex) {
            Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed

        JFileChooser chooser = new JFileChooser("");

        chooser.showSaveDialog(null);

        File seleFile = chooser.getSelectedFile();
        try {
            SolutionUtil.writeFormatedInputFlightsTimes(airlineNetwork, seleFile);
            JOptionPane.showMessageDialog(null, "Salvo com sucesso");
        } catch (IOException ex) {
            Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        if (fileChooserFormattedInstance.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooserFormattedInstance.getSelectedFile();
            try {
                SolutionUtil.writeFormatedSolutionForArticle(jpac.getAirlineNetwork().getBestNetwork(), selectedFile);
                JOptionPane.showMessageDialog(null, "Solução gravada com sucesso no arquivo " + selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
        // TODO add your handling code here:
        if (fileChooserFormattedInstance.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooserFormattedInstance.getSelectedFile();
            try {
                SolutionUtil.writeFormatedSolutionForInstance(jpac.getAirlineNetwork().getBestNetwork(), selectedFile);
                JOptionPane.showMessageDialog(null, "Solução gravada com sucesso no arquivo " + selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenu3ActionPerformed

    private void prepareForSolver(File entrada) {
        try {
            File file = new File("./solver/entrada.txt");
            SolutionUtil.writeFormatedAirlineNetworkForCplex(jpac.getAirlineNetwork(), file);

            Process process = Runtime.getRuntime().exec("./solver/main ./solver/entrada.txt");
            process.waitFor();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível executar o SOLVER\n" + ex.getMessage());
            Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new JFrameGraphicTest().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItemExecuteFromSolver;
    private javax.swing.JMenuItem jMenuItemExecuteNormal;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    private JPanelARPControler getJPanelAircraftControler() {
        return this.jpac;
    }
}