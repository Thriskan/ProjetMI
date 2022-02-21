/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Palette controller
 * Manage different mode of the multimodal system
 * /!\ only REC is enabled because we already saved gestures from Learning mode into "savings" file /!\
 * Allow palette full clean
 * Useful to enable/disable micro (needed to have better performances with sra5)
 * @author caros
 */
public class PaletteController extends javax.swing.JFrame {
    
    /**
     * Two modes defined
     * APP : Learning mode
     * REC : Recognition mode
     */
    public enum Mode {
        APP,
        REC
    }
    
    //private Mode currentMode = Mode.APP;
    private Mode currentMode = Mode.REC;

    
    private boolean isMicroActivated = false;
    
    public PaletteController() {
        initComponents();
        addRadioToGroup();
                
        
        rbApprentissage.setEnabled(false);
        rbReconnaissance.setSelected(true);
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(Mode currentMode) {
        this.currentMode = currentMode;
    }

    public JRadioButton getRbApprentissage() {
        return rbApprentissage;
    }

    public JRadioButton getRbReconnaissance() {
        return rbReconnaissance;
    }

    public JTextField getTfApprentissage() {
        return tfApprentissage;
    }

    public JLabel getLbReconnaissance() {
        return lbReconnaissance;
    }

    public void setLbReconnaissance(JLabel aLabel) {
        this.lbReconnaissance = aLabel;
    }

    public boolean isMicroActivated() {
        return isMicroActivated;
    }

    public JLabel getLbCurrentStateValue() {
        return lbCurrentStateValue;
    }
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        rbApprentissage = new javax.swing.JRadioButton();
        rbReconnaissance = new javax.swing.JRadioButton();
        tfApprentissage = new javax.swing.JTextField();
        lbReconnaissance = new javax.swing.JLabel();
        btCleanAll = new javax.swing.JButton();
        tbMicroActivation = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        lbCurrentStateValue = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        rbApprentissage.setText("Apprentissage");
        rbApprentissage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbApprentissageActionPerformed(evt);
            }
        });

        rbReconnaissance.setText("Reconnaissance");
        rbReconnaissance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbReconnaissanceActionPerformed(evt);
            }
        });

        tfApprentissage.setText("Nom de la forme");

        lbReconnaissance.setText("Commande reconnue");

        btCleanAll.setText("Clean All");
        btCleanAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCleanAllActionPerformed(evt);
            }
        });

        tbMicroActivation.setText("Micro OFF");
        tbMicroActivation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbMicroActivationActionPerformed(evt);
            }
        });

        jLabel2.setText("Current_state : ");

        lbCurrentStateValue.setText("IDLE");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tbMicroActivation, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(btCleanAll)
                                .addGap(51, 51, 51)
                                .addComponent(jLabel2)
                                .addGap(0, 43, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(rbApprentissage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(rbReconnaissance, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfApprentissage)
                                    .addComponent(lbReconnaissance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbCurrentStateValue, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(101, 101, 101))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbApprentissage)
                    .addComponent(tfApprentissage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbReconnaissance)
                    .addComponent(lbReconnaissance))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btCleanAll)
                    .addComponent(jLabel2)
                    .addComponent(lbCurrentStateValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tbMicroActivation)
                .addContainerGap(161, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbApprentissageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbApprentissageActionPerformed
        currentMode = Mode.APP;
    }//GEN-LAST:event_rbApprentissageActionPerformed

    private void rbReconnaissanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbReconnaissanceActionPerformed
        currentMode = Mode.REC;
    }//GEN-LAST:event_rbReconnaissanceActionPerformed

    private void btCleanAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCleanAllActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btCleanAllActionPerformed

    private void tbMicroActivationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbMicroActivationActionPerformed
        if (isMicroActivated){
            isMicroActivated = false;
            tbMicroActivation.setText("Micro OFF");
        }
        else{
            isMicroActivated = true;
            tbMicroActivation.setText("Micro ON");
        }
    }//GEN-LAST:event_tbMicroActivationActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PaletteController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PaletteController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PaletteController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PaletteController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PaletteController().setVisible(true);
            }
        });
    }
    
    private void addRadioToGroup(){
        buttonGroup1.add(rbApprentissage);
        buttonGroup1.add(rbReconnaissance);
    }

    public JButton getBtCleanAll() {
        return btCleanAll;
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCleanAll;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lbCurrentStateValue;
    private javax.swing.JLabel lbReconnaissance;
    private javax.swing.JRadioButton rbApprentissage;
    private javax.swing.JRadioButton rbReconnaissance;
    private javax.swing.JToggleButton tbMicroActivation;
    private javax.swing.JTextField tfApprentissage;
    // End of variables declaration//GEN-END:variables
}
