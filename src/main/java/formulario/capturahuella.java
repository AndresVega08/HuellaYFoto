package formulario;
import bd.ConexionBD;
import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.capture.event.DPFPErrorEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPErrorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPErrorEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import com.sun.istack.internal.logging.Logger;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Andres Felipe Vega p.
 */
public class capturahuella extends javax.swing.JFrame {
        
    public capturahuella() {
  
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Imposible modificar el tema visual", "LookAndFeel invalido.",
                    JOptionPane.ERROR_MESSAGE);
        }
        initComponents();
        setLocationRelativeTo(null);
    }
        private DPFPCapture Lector = DPFPGlobal.getCaptureFactory().createCapture();
        private DPFPEnrollment Reclutador = DPFPGlobal.getEnrollmentFactory().createEnrollment();
        private DPFPVerification Verificador = DPFPGlobal.getVerificationFactory().createVerification();
        private DPFPTemplate template;
        public static String TEMPLATE_PROPERTY = "template";
        
        protected void Iniciar (){
            Lector.addDataListener (new DPFPDataAdapter(){
                @Override public void dataAcquired(final DPFPDataEvent e){
                    SwingUtilities.invokeLater(new Runnable() {@Override 
                    public void run(){
                    EnviarTexto("La huella digital ha sido capturada");
                        try {
                            ProcesarCaptura(e.getSample());
                        } catch (DPFPImageQualityException ex) {
                            java.util.logging.Logger.getLogger(capturahuella.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }});}});
            
            
            Lector.addReaderStatusListener(new DPFPReaderStatusAdapter(){
            @Override public void readerConnected(final DPFPReaderStatusEvent e){
            SwingUtilities.invokeLater(new Runnable() {@Override
            public void run(){
            EnviarTexto("El sensor de huella esta activo o conectado");
            }});}

            public void readerDisconnect(final DPFPReaderStatusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {@Override 
                    public void run(){
                    EnviarTexto("El sensor de huella esta desactivado o apagado");
                    }});}
                    });
            
            Lector.addSensorListener(new DPFPSensorAdapter(){
            @Override public void fingerTouched(final DPFPSensorEvent e){
            SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
            EnviarTexto("El dedo ha colocado sobre el lector de huella");
            }});} 
            @Override public void fingerGone(final DPFPSensorEvent e){
                SwingUtilities.invokeLater(new Runnable(){@Override 
                public void run(){
                    EnviarTexto("El dedo ha sido quitado del lector de huella");
                }});}
                    });
            Lector.addErrorListener(new DPFPErrorAdapter(){
            public void errorReader(final DPFPErrorEvent e){
            SwingUtilities.invokeLater(new Runnable(){@Override
            public void run(){
            EnviarTexto("Error:"+ e.getError());
        }});}
        });
        }
        
        public DPFPFeatureSet featuresincripcion;
        public DPFPFeatureSet featuresverificacion;
        public DPFPFeatureSet extraerCaracteristicas(DPFPSample sample, DPFPDataPurpose purpose){
            DPFPFeatureExtraction extractor =
            DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
            try {
                return extractor.createFeatureSet(sample, purpose);
            } catch (DPFPImageQualityException e) {
                return null;
            }
        }  
        public Image CrearImagenHuella(DPFPSample sample) {
            return DPFPGlobal.getSampleConversionFactory().createImage(sample);
        }
        public void DibujarHuella(Image image){
            lblImagenHuella.setIcon(new ImageIcon(
            image.getScaledInstance(lblImagenHuella.getWidth(),
                    lblImagenHuella.getHeight(),
                    Image.SCALE_DEFAULT)));
            repaint();
        }
        
        public void EstadoHuellas(){
            EnviarTexto("Muestra de huellas necesarias para ser guardada: "+
                    Reclutador.getFeaturesNeeded());
        }
        public void EnviarTexto(String string){
            txtArea.append(string + "\n");
        }
        public void start(){
            Lector.startCapture();
            EnviarTexto("Utilizando el Lector de huellas");
        }
        public void stop(){
            Lector.stopCapture();
            EnviarTexto("No se esta usando el Lector de huellas");
        }
        
        public DPFPTemplate getTemplate(){
            return template;
        }
        
        public void setTemplate(DPFPTemplate template){
            DPFPTemplate old = this.template;
            this.template = template;
            firePropertyChange(TEMPLATE_PROPERTY, old, template);
        }
        
        public void ProcesarCaptura(DPFPSample sample) throws DPFPImageQualityException{
            featuresincripcion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);   
            featuresverificacion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
        
        
        if( featuresincripcion != null)
        try{
            System.out.println("Las caracteristicas de la huella han sido creadas");
            Reclutador.addFeatures(featuresincripcion);
            
            Image image=CrearImagenHuella(sample);
            DibujarHuella(image);
            
            btnVerificar.setEnabled(true);
            btnIdentificar.setEnabled(true);
            
        }catch (DPFPImageQualityException ex){
            System.err.println("Error: "+ ex.getMessage());
        } 
        finally {
            EstadoHuellas();
            
                switch(Reclutador.getTemplateStatus())
                {
                    case TEMPLATE_STATUS_READY:
                        stop();
                        setTemplate(Reclutador.getTemplate());
                        EnviarTexto("La plantilla de la huella ha sido creada, ya puede verificarla o identificarla");
                        btnIdentificar.setEnabled(false);
                        btnVerificar.setEnabled(false);
                        btnGuardar.setEnabled(true);
                        btnGuardar.grabFocus();
                        break;
                        
                    case TEMPLATE_STATUS_FAILED:
                        Reclutador.clear();
                        stop();
                        EstadoHuellas();
                        setTemplate(null);
                        JOptionPane.showMessageDialog(capturahuella.this, "La plantilla de la huella no pudo ser creada, repita el proceso");
                        start();
                        break;
                }
        }
        }
        
        ConexionBD cn = new ConexionBD();
        public void guardarHuella() throws SQLException{
            ByteArrayInputStream datosHuella = new ByteArrayInputStream(template.serialize());
            Integer tamañoHuella=template.serialize().length;
            
            String nombre = JOptionPane.showInputDialog("Nombre: ");
            try{
                Connection c=cn.conectar();
                PreparedStatement guardarStmt = c.prepareStatement
                                ("INSERT INTO somhue(huenombre, huehuella) values(?,?)"); 
                    guardarStmt.setString(1,nombre);
                    guardarStmt.setBinaryStream(2, datosHuella, tamañoHuella);
                    
                    guardarStmt.execute();
                    guardarStmt.close();
                
                JOptionPane.showMessageDialog(null,"Huella guarda correctamente");
                cn.desconectar();
                btnGuardar.setEnabled(false);
                btnVerificar.grabFocus();
             } catch(SQLException ex){
                 System.err.println("Error al guardar los datos de la huella");
             }finally {cn.desconectar();
            
        }
        }
        public void verificarHuella(String nom) {
            try{
                //Establece los valores de la sentencia SQL
                Connection c=cn.conectar();
                //Obtiene la platilla correspondiente a la persona 
                PreparedStatement verificarStmt = c.prepareStatement("SELECT huehuella FROM somhue WHERE huenombre=?");
                verificarStmt.setString(1, nom);
                ResultSet rs = verificarStmt.executeQuery();
                
                //Si se encuentra el nombre en la base de datos
                if(rs.next()){
                    //Lee la plantilla en la base de datos 
                    byte templateBuffer[] = rs.getBytes("huehuella");
                    //Crea una plantilla apartir de la guardada en la base de datos
                    DPFPTemplate referenceTemplate = DPFPGlobal.getTemplateFactory().createTemplate(templateBuffer);
                    //Envia la plantilla creada al objeto contenedor del Template del componente huella digital
                    setTemplate(referenceTemplate);
                    //Compara las caracteristicas de la huella recientemente capturada con la plantilla guardada
                    DPFPVerificationResult result = Verificador.verify(featuresverificacion, getTemplate());
                    //Compara las platillas (actual y bd)
                    if (result.isVerified())
                        JOptionPane.showMessageDialog(null,"La huella capturada coincide con la de "+nom,"verificacion de huella", JOptionPane.INFORMATION_MESSAGE);
                        else
                        JOptionPane.showMessageDialog(null, "No corresponde la huella con "+nom, "verificacion de huella", JOptionPane.ERROR_MESSAGE);
                //Si no encuentra alguna huella correspondiente al nombre lo indica con el mensaje 
                } else {
                    JOptionPane.showMessageDialog(null,"No existe un registro de huella para "+nom, "Verificacion de huella", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                //Si ocurre un error lo indica en la consola 
                System.err.println("Error al verificar los datos de la huella");
                
            } finally {
                cn.desconectar();
            }
        }
        
        public void identificarHuella()throws IOException{
            try{
                //Establece los valores para la sentencia SQL
                Connection c=cn.conectar();
                //Obtiene todas las huellas
                PreparedStatement identificarStmt = c.prepareStatement("SELECT huenombre, huehuella FROM somhue");
                ResultSet rs = identificarStmt.executeQuery();
                //Si se encuentra el nombre en la base de datos
                while(rs.next()){
                    //Lee la plantilla 
                    byte templateBuffer[] = rs.getBytes("huehuella");
                    String nombre = rs.getString("huenombre");
                    //Crea una nueva plantilla a partir de la guardada en la base de datos
                    DPFPTemplate referenceTemplate = DPFPGlobal.getTemplateFactory().createTemplate(templateBuffer);
                    //Envia la plantilla creada al objeto contenedor del Template
                    //del componente huella digital
                    setTemplate(referenceTemplate);
                    //Compara las caracteristicas de la huella capturada con las huellas guardadas
                    DPFPVerificationResult result = Verificador.verify(featuresverificacion, getTemplate());
                    //Compara las plstillas (base vs actual)
                    
                    if ( result.isVerified()){
                        //Crea la imagen de los datos guardados de las huellas guardadas en la bd
                        JOptionPane.showMessageDialog(null, "La huella capturada es de "+nombre,"Verificacion de huella", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
                //Si no encuentra la huella dara el aviso
                JOptionPane.showMessageDialog(null,"No existe ningun registro que coincida con la huella",
                        "Verificacion de huella", JOptionPane.ERROR_MESSAGE);
                setTemplate(null);
            } catch (SQLException e ) {
                //Si ocurre un error lo mostrara en la consola 
                System.err.print("Error al identificar la huella dactilar."+e.getMessage());
            }finally {
                cn.desconectar();
            }
        }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        PanHuella = new javax.swing.JPanel();
        lblImagenHuella = new javax.swing.JLabel();
        PanBtns = new javax.swing.JPanel();
        btnVerificar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnIdentificar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextArea();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        PanHuella.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Huellas Digitales", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        javax.swing.GroupLayout PanHuellaLayout = new javax.swing.GroupLayout(PanHuella);
        PanHuella.setLayout(PanHuellaLayout);
        PanHuellaLayout.setHorizontalGroup(
            PanHuellaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanHuellaLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblImagenHuella, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanHuellaLayout.setVerticalGroup(
            PanHuellaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanHuellaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImagenHuella, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addContainerGap())
        );

        PanBtns.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Acciones con Huella", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        btnVerificar.setText("Verificar");
        btnVerificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnIdentificar.setText("Identificar");
        btnIdentificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIdentificarActionPerformed(evt);
            }
        });

        btnSalir.setText("Atras");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanBtnsLayout = new javax.swing.GroupLayout(PanBtns);
        PanBtns.setLayout(PanBtnsLayout);
        PanBtnsLayout.setHorizontalGroup(
            PanBtnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanBtnsLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(PanBtnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnVerificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnIdentificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 228, Short.MAX_VALUE)
                .addGroup(PanBtnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(46, 46, 46))
        );
        PanBtnsLayout.setVerticalGroup(
            PanBtnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanBtnsLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(PanBtnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVerificar)
                    .addComponent(btnGuardar))
                .addGap(18, 18, 18)
                .addGroup(PanBtnsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIdentificar)
                    .addComponent(btnSalir))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        txtArea.setColumns(20);
        txtArea.setRows(5);
        jScrollPane1.setViewportView(txtArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PanHuella, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanBtns, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanHuella, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanBtns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        stop();
    }//GEN-LAST:event_formWindowClosing

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
        Iniciar();
        start();
        EstadoHuellas();
        btnGuardar.setEnabled(false);
        btnIdentificar.setEnabled(false);
        btnVerificar.setEnabled(false);
        btnSalir.grabFocus();
    }//GEN-LAST:event_formWindowOpened

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        stop();
        this.setVisible(false);
        menu d = new menu();
        d.setVisible(true);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnIdentificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIdentificarActionPerformed
        try{
            identificarHuella();
            Reclutador.clear();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(capturahuella.class.getName()).log(Level.SEVERE,null, ex);
        }
    }//GEN-LAST:event_btnIdentificarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try{
            guardarHuella();
            Reclutador.clear();
            lblImagenHuella.setIcon(null);
            start();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(capturahuella.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnVerificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarActionPerformed
        String nombre = JOptionPane.showInputDialog("Nombre a verificar:");

        verificarHuella(nombre);
        Reclutador.clear();
    }//GEN-LAST:event_btnVerificarActionPerformed

    
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
            java.util.logging.Logger.getLogger(capturahuella.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(capturahuella.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(capturahuella.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(capturahuella.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new capturahuella().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanBtns;
    private javax.swing.JPanel PanHuella;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnIdentificar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnVerificar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblImagenHuella;
    private javax.swing.JTextArea txtArea;
    // End of variables declaration//GEN-END:variables

    

    
}
