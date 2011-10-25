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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import main.entities.AirlineNetwork;
import main.entities.ExecutionInfo;
import main.entities.Flight;
import main.entities.Track;
import main.heuristic.ARPParameters;
import main.heuristic.SolverManager;
import main.heuristic.advanced.ARPAdvanced;
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
    JFileChooser fileChooserFormattedInstance = new JFileChooser("/Users/alexander/Documents/Mestrado/dissertacao/trunk/AIRotation/instances/");
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
        configDialogParams();
        //        new Thread() {
//
//            @Override
//            public void run() {
//                initGraphics();
//
//            }
//        }.start();
    }
    
    private void configDialogParams(){
        
        jDialogParameters.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet.");
                initDialogParams();
                
            }

            public void windowClosing(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet.");
                saveDialogParams();
            }

            public void windowClosed(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void windowIconified(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void windowDeiconified(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void windowActivated(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void windowDeactivated(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        
        
    }
    
    public void initDialogParams(){
        System.out.println("Carregando Dialog Params");
        jSpinnerMaxDelay.setValue(ARPParameters.defaultParameters.getMaximumDelay());
        jSpinnerCrewChangeTime.setValue(ARPParameters.defaultParameters.getCrewChangeTime());
        jSpinnerCrewMaxFlights.setValue(ARPParameters.defaultParameters.getMaxCrewFlights());
        jSpinnerMaxCrewFlightTime.setValue(ARPParameters.defaultParameters.getMaxCrewFlightTime());
        
        jSpinnerProbArcT1.setValue(ARPParameters.defaultParameters.getProbabilityType1Arc());
        jSpinnerProbArcT2.setValue(ARPParameters.defaultParameters.getProbabilityType2Arc());
        jSpinnerProbArcT3.setValue(ARPParameters.defaultParameters.getProbabilityType3Arc());
        jSpinnerProbArcT4.setValue(ARPParameters.defaultParameters.getProbabilityType4Arc());
    }
    
    public void saveDialogParams(){
        System.out.println("Salvando Dialog Params");
        ARPParameters.defaultParameters.setMaximumDelay((Integer) jSpinnerMaxDelay.getValue());
        ARPParameters.defaultParameters.setCrewChangeTime((Integer) jSpinnerCrewChangeTime.getValue());
        ARPParameters.defaultParameters.setMaxCrewFlights((Integer) jSpinnerCrewMaxFlights.getValue());
        ARPParameters.defaultParameters.setMaxCrewFlightTime((Integer) jSpinnerMaxCrewFlightTime.getValue());
        
        ARPParameters.defaultParameters.setProbabilityType1Arc((Integer) jSpinnerProbArcT1.getValue());
        ARPParameters.defaultParameters.setProbabilityType2Arc((Integer) jSpinnerProbArcT2.getValue());
        ARPParameters.defaultParameters.setProbabilityType3Arc((Integer) jSpinnerProbArcT3.getValue());
        ARPParameters.defaultParameters.setProbabilityType4Arc((Integer) jSpinnerProbArcT4.getValue());
        
        
    }

    public String initSystem() {

        fileChooserFormattedInstance.showOpenDialog(null);
        File inputFile = fileChooserFormattedInstance.getSelectedFile();

        this.airlineNetwork = new AirlineNetwork(inputFile.getAbsolutePath());
        System.out.println("Instancia " + airlineNetwork.getPathInstance() + " Numero de voos " + airlineNetwork.getFlights().size());
        try {
            ARPFileReader.readDataFromFile(airlineNetwork.getPathInstance(), airlineNetwork);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        jpac.initChart(airlineNetwork);
        return inputFile.getAbsolutePath();
    }

    public void executeCpp(AirlineNetwork airlineNetwork, boolean usingSolver, int max_delay) {
        try {
            SolverManager.executeSolver(airlineNetwork, usingSolver, max_delay);
        } catch (Exception ex) {
            Logger.getLogger(JFrameGraphicTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initGraphics(boolean usingsolver) {



        jpac.setStop(false);


        initSystem();

        long init = System.currentTimeMillis();

        executeCpp(airlineNetwork, usingsolver, ARPParameters.defaultParameters.getMaximumDelay());

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


        showInterface(airlineNetwork, duration);



    }

    public void showInterface(AirlineNetwork airlineNetwork, long duration) {
        jpac.initChart(airlineNetwork);

        jpac.initConfigures();


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

        jDialogParameters = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerCrewMaxFlights = new javax.swing.JSpinner();
        jSpinnerMaxCrewFlightTime = new javax.swing.JSpinner();
        jSpinnerCrewChangeTime = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSpinnerProbArcT1 = new javax.swing.JSpinner();
        jSpinnerProbArcT2 = new javax.swing.JSpinner();
        jSpinnerProbArcT3 = new javax.swing.JSpinner();
        jSpinnerProbArcT4 = new javax.swing.JSpinner();
        jSpinnerMaxDelay = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemExecuteNormal = new javax.swing.JMenuItem();
        jMenuItemExecuteFromSolver = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItemCompleteInstance = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        jDialogParameters.setTitle("Parâmetros");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Atraso máximo de um voo:");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Tripulação"));

        jLabel2.setText("Tempo para mudança de tripulação:");

        jLabel3.setText("Máximo de voos de um tripulante:");

        jLabel4.setText("Tempo máximo de voo de um tripulante:");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(51, 51, 51)
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinnerCrewChangeTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel3)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel4))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jSpinnerMaxCrewFlightTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jSpinnerCrewMaxFlights, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(168, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(new java.awt.Component[] {jSpinnerCrewChangeTime, jSpinnerCrewMaxFlights, jSpinnerMaxCrewFlightTime}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jSpinnerCrewChangeTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel3)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSpinnerCrewMaxFlights, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel4)
                    .add(jSpinnerMaxCrewFlightTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Probabilidades dos tipos de arcos"));

        jLabel5.setText("Arco do Tipo 1:");

        jLabel6.setText("Arco do Tipo 2:");

        jLabel7.setText("Arco do Tipo 3:");

        jLabel8.setText("Arco do Tipo 4:");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel8)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinnerProbArcT4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinnerProbArcT1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinnerProbArcT2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinnerProbArcT3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(322, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(new java.awt.Component[] {jSpinnerProbArcT1, jSpinnerProbArcT2, jSpinnerProbArcT3, jSpinnerProbArcT4}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel5)
                    .add(jSpinnerProbArcT1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel6)
                    .add(jSpinnerProbArcT2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel7)
                    .add(jSpinnerProbArcT3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(12, 12, 12)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel8)
                    .add(jSpinnerProbArcT4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinnerMaxDelay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel1)
                    .add(jSpinnerMaxDelay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(14, 14, 14)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jDialogParametersLayout = new org.jdesktop.layout.GroupLayout(jDialogParameters.getContentPane());
        jDialogParameters.getContentPane().setLayout(jDialogParametersLayout);
        jDialogParametersLayout.setHorizontalGroup(
            jDialogParametersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialogParametersLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDialogParametersLayout.setVerticalGroup(
            jDialogParametersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDialogParametersLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("Arquivo");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

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

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.META_MASK));
        jMenuItem11.setText("Advanced Use");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem11);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ferramentas");

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

        jMenuItemCompleteInstance.setText("Completar Instância");
        jMenuItemCompleteInstance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCompleteInstanceActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemCompleteInstance);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Configuração");
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

        jMenuItem2.setText("Parâmetros");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

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
                SolutionUtil.writeFormatedAirlineNetworkForCplex(jpac.getAirlineNetwork(), selectedFile, 10);
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

            String inputPath = initSystem();
            File outputFlights = new File(inputPath + File.separator + "voos_extended.txt");
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
        initSystem();
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

private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
// TODO add your handling code here:
    initSystem();


    List<AirlineNetwork> dailyAirline = ARPAdvanced.divideAirlinesPerDay(airlineNetwork, 2 * 60);

    System.out.println("Iniciando uso avançado...");
    long initTime = System.currentTimeMillis();
    int current_day = 1;
    for (AirlineNetwork airlineNetworkDaily : dailyAirline) {
        System.out.printf("##############DAY %d################ (Size = %d)\n", current_day, airlineNetworkDaily.getFlights().size());
        for (Flight flight : airlineNetworkDaily.getFlights()) {
            System.out.println("F  -> " + flight.toString());
        }
        System.out.println("####################################");
        current_day++;
    }



    int day = 0;
    for (AirlineNetwork airlineNetwork1 : dailyAirline) {
        System.out.println("Otimizado executando no dia " + (day++));
        executeCpp(airlineNetwork1, true, ARPParameters.defaultParameters.getMaximumDelay());
    }

    AirlineNetwork convertedAirline = ARPAdvanced.convertTracksToFlights(dailyAirline);

    executeCpp(convertedAirline, true, 0);

    airlineNetwork = ARPAdvanced.recoverFlightsOfConvertedTracks(convertedAirline, dailyAirline);


    System.out.println("Solution <><><><><><><> Size = " + airlineNetwork.getBestNetwork().size());
    for (Track track : airlineNetwork.getBestNetwork()) {

        System.out.println(">>>>> Track " + track.getName());

        for (Flight flight : track.getFlights()) {
            System.out.println("F-> " + flight.toString());
        }
    }

    //Faz cada trilho virar um voo, depois executa novamente, e depois remonta

    System.out.println("Finish... <<<< " + airlineNetwork.getLowTime());

    showInterface(airlineNetwork, (System.currentTimeMillis() - initTime));

}//GEN-LAST:event_jMenuItem11ActionPerformed

private void jMenuItemCompleteInstanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCompleteInstanceActionPerformed
    // TODO add your handling code here:
    InstanceUtil.completeSolution(airlineNetwork);
    
    showInterface(airlineNetwork, 0l);
}//GEN-LAST:event_jMenuItemCompleteInstanceActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
// TODO add your handling code here:
    
    jDialogParameters.setModal(true);
    jDialogParameters.pack();
    jDialogParameters.setVisible(true);
}//GEN-LAST:event_jMenuItem2ActionPerformed

    private void prepareForSolver(File entrada) {
        try {
            File file = new File("./solver/entrada.txt");
            SolutionUtil.writeFormatedAirlineNetworkForCplex(jpac.getAirlineNetwork(), file, 10);

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
    private javax.swing.JDialog jDialogParameters;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItemCompleteInstance;
    private javax.swing.JMenuItem jMenuItemExecuteFromSolver;
    private javax.swing.JMenuItem jMenuItemExecuteNormal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinnerCrewChangeTime;
    private javax.swing.JSpinner jSpinnerCrewMaxFlights;
    private javax.swing.JSpinner jSpinnerMaxCrewFlightTime;
    private javax.swing.JSpinner jSpinnerMaxDelay;
    private javax.swing.JSpinner jSpinnerProbArcT1;
    private javax.swing.JSpinner jSpinnerProbArcT2;
    private javax.swing.JSpinner jSpinnerProbArcT3;
    private javax.swing.JSpinner jSpinnerProbArcT4;
    // End of variables declaration//GEN-END:variables

    private JPanelARPControler getJPanelAircraftControler() {
        return this.jpac;
    }
}
