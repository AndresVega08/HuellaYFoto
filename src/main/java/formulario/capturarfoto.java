
package formulario;

import bd.ConexionBD;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import sun.awt.image.ByteArrayImageSource;

/**
 *
 * @author Andres Felipe Vega P.
 */


public class capturarfoto extends javax.swing.JFrame {
        ImageIcon foto;
        int largoCamara = 332;
        int anchoCamara = 269;
        BufferedImage ruta;
        int contador = 0;
        Dimension dimension = new Dimension(largoCamara, anchoCamara);
        Dimension dimension1 = WebcamResolution.VGA.getSize();
        Webcam webcam = Webcam.getDefault();
        WebcamPanel webcamPanel = new WebcamPanel(webcam, dimension, false);
       
   
    public capturarfoto() {
        initComponents();
        setLocationRelativeTo(null);
        webcam.setViewSize(dimension1);
        webcamPanel.setFillArea(true);
        pnlCamara.setLayout(new FlowLayout());
        pnlCamara.add(webcamPanel);
        lblNombreCamara.setText(webcam.toString());
        apagarBotones();
        
    }
    
    public void apagarBotones(){
        btnApagar.setEnabled(false);
        btnCapturar.setEnabled(false);
        btnGuardar.setEnabled(false);
    }
    
    public void prenderBotones(){
        btnApagar.setEnabled(true);
        btnCapturar.setEnabled(true);
        btnBack.setEnabled(false);
    }
    
    public void cambioTexto(){
        btnApagar.setText("Iniciando camara...");
        btnCapturar.setText("Iniciando camara...");
        btnGuardar.setText("Iniciando camara...");
        btnIniciar.setText("Iniciando camara...");
        
    }
    
    public void cargaCamara(){
        btnApagar.setText("APAGAR");
        btnCapturar.setText("CAPTURAR FOTO");
        btnGuardar.setText("GUARDAR FOTO");
        btnIniciar.setText("INICIAR");
    }
    
    public void capturarImagen(){
        btnCapturar.setText("Tomar otra foto");
        btnGuardar.setEnabled(true);
        foto = new ImageIcon(webcam.getImage());
        Icon iconoFoto = new ImageIcon(foto.getImage().getScaledInstance(lblFotoTomada.getWidth(), lblFotoTomada.getHeight(), Image.SCALE_SMOOTH));
        lblFotoTomada.setIcon(iconoFoto);
        ruta = webcam.getImage();
        
    
    }
    
    public void guardarFoto() {
        
        ConexionBD cn = new ConexionBD();
            
            String nombre = JOptionPane.showInputDialog("¿Cual es el nombre de la persona?: ");
            try{
                Connection c=cn.conectar();
                PreparedStatement guardarStmt = c.prepareStatement
                                ("INSERT INTO somfoto(nomfoto, foto) values(?,?)"); 
                    guardarStmt.setString(1,nombre);
                    guardarStmt.setString(2,foto.toString());
                    guardarStmt.execute();
                    guardarStmt.close();
                JOptionPane.showMessageDialog(null,"Imagen guardada");
                cn.desconectar();
             } catch(SQLException ex){
                 System.err.println("Error al guardar la imagen");
             }finally {cn.desconectar();
    }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnIniciar = new javax.swing.JButton();
        btnApagar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblNombreCamara = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnCapturar = new javax.swing.JButton();
        pnlCamara = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        lblFotoTomada = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Camara");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Opciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 18))); // NOI18N

        btnIniciar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnIniciar.setText("INICIAR");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });

        btnApagar.setText("APAGAR");
        btnApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApagarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Camara en uso");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblNombreCamara.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblNombreCamara.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNombreCamara.setText("-");

        btnBack.setText("ATRAS");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombreCamara, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(lblNombreCamara)
                .addGap(48, 48, 48))
        );

        btnGuardar.setText("Guardar Foto");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCapturar.setText("CAPTURAR FOTO");
        btnCapturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapturarActionPerformed(evt);
            }
        });

        pnlCamara.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout pnlCamaraLayout = new javax.swing.GroupLayout(pnlCamara);
        pnlCamara.setLayout(pnlCamaraLayout);
        pnlCamaraLayout.setHorizontalGroup(
            pnlCamaraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 332, Short.MAX_VALUE)
        );
        pnlCamaraLayout.setVerticalGroup(
            pnlCamaraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 269, Short.MAX_VALUE)
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblFotoTomada.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(btnCapturar, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(pnlCamara, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFotoTomada, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(pnlCamara, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(lblFotoTomada, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCapturar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
    cargaCamara();
    Thread hilo = new Thread(){
        @Override
        public void run(){
            webcamPanel.start();
            prenderBotones();
            cargaCamara();
        }
        
    };
        
    hilo.setDaemon(true);
    hilo.start();
    btnIniciar.setEnabled(false);
        
        
        
        
    }//GEN-LAST:event_btnIniciarActionPerformed

    private void btnApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApagarActionPerformed
        webcamPanel.stop();
        apagarBotones();
        btnIniciar.setEnabled(true);
        lblFotoTomada.setIcon(null);
        btnBack.setEnabled(true);
    }//GEN-LAST:event_btnApagarActionPerformed

    private void btnCapturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapturarActionPerformed
       capturarImagen();
        
        
    }//GEN-LAST:event_btnCapturarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

                guardarFoto();

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        
        this.setVisible(false);
        menu x = new menu();
        x.setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new capturarfoto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApagar;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCapturar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblFotoTomada;
    private javax.swing.JLabel lblNombreCamara;
    private javax.swing.JPanel pnlCamara;
    // End of variables declaration//GEN-END:variables
}
