/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author T-Gamer
 */
public class Cliente extends JFrame implements ActionListener, KeyListener {

  private static final long serialVersionUID = 1L;
  private JTextArea texto;
  private JTextField txtMsg;
  private JButton btnSend;
  private JButton btnSair;
  private JLabel lblMessage;
  private JLabel lblHistorico;
  private JLabel lblMsg;
  private JPanel pnlContent;
  private Socket socket;
  private OutputStream ou;
  private Writer ouw;
  private BufferedWriter bfw;
  private JTextField txtIP;
  private JTextField txtPorta;
  private JTextField txtNome;

  public Cliente() throws IOException {
    lblMessage = new JLabel("Verificar!");
    txtIP = new JTextField("127.0.0.1");
    txtPorta = new JTextField("8000");
    txtNome = new JTextField("Cliente");
    Object[] texts = {lblMessage, txtIP, txtPorta, txtNome};
    JOptionPane.showMessageDialog(null, texts);
    pnlContent = new JPanel();
    texto = new JTextArea(10, 20);
    texto.setEditable(false);
    texto.setBackground(new Color(240, 240, 240));
    txtMsg = new JTextField(20);
    lblHistorico = new JLabel("Historico");
    lblMsg = new JLabel("Mensagem");
    btnSend = new JButton("Enviar");
    btnSend.setToolTipText("Enviar Mensagem");
    btnSend.addActionListener(this);
    btnSend.addKeyListener(this);
    btnSair = new JButton("Sair");
    btnSair.setToolTipText("Sair do chat");
    btnSair.addActionListener(this);
    btnSair.addKeyListener(this);
    txtMsg.addKeyListener(this);
    JScrollPane scroll = new JScrollPane(texto);
    texto.setLineWrap(true);
    pnlContent.add(lblHistorico);
    pnlContent.add(scroll);
    pnlContent.add(lblMsg);
    pnlContent.add(txtMsg);
    pnlContent.add(btnSair);
    pnlContent.add(btnSend);
    pnlContent.setBackground(Color.LIGHT_GRAY);
    texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
    txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
    setTitle(txtNome.getText());
    setContentPane(pnlContent);
    setLocationRelativeTo(null);
    setResizable(false);
    setSize(250, 300);
    setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  public void Conectar() throws IOException {

    socket = new Socket(txtIP.getText(), Integer.parseInt(txtPorta.getText()));
    ou = socket.getOutputStream();
    ouw = new OutputStreamWriter(ou);
    bfw = new BufferedWriter(ouw);
    bfw.write(txtNome.getText() + "\r\n");
    bfw.flush();
  }

  public void EnviarMensagem(String msg) throws IOException {

    if (msg.equals("Sair")) {
      bfw.write("Desconectado \r\n");
      texto.append("Desconectado \r\n");
    } else {
      bfw.write(msg + "\r\n");
      texto.append(txtNome.getText() + " diz -> " + txtMsg.getText() + "\r\n");
    }
    bfw.flush();
    txtMsg.setText("");
  }

 public void Escutar() throws IOException{
                           
   InputStream in = socket.getInputStream();
   InputStreamReader inr = new InputStreamReader(in);
   BufferedReader bfr = new BufferedReader(inr);
   String msg = "";
                           
    while(!"Sair".equalsIgnoreCase(msg))
                                      
       if(bfr.ready()){
         msg = bfr.readLine();
       if(msg.equals("Sair"))
         texto.append("Servidor caiu! \r\n");
        else
         texto.append(msg+"\r\n");         
        }
}
 
  public void Sair() throws IOException {
    EnviarMensagem("Sair");
    bfw.close();
    ouw.close();
    ou.close();
    socket.close();
  }

  @Override
  public void actionPerformed(ActionEvent ae) {
    try {
      if (ae.getActionCommand().equals(btnSend.getActionCommand())) {
        EnviarMensagem(txtMsg.getText());
      } else if (ae.getActionCommand().equals(btnSair.getActionCommand())) {
        Sair();
      }
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }

  @Override
  public void keyTyped(KeyEvent ke) {

  }

  @Override
  public void keyPressed(KeyEvent ke) {
    if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
      try {
        EnviarMensagem(txtMsg.getText());
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent ke) {
  }

  public static void main(String[] args) throws IOException {

    Cliente app = new Cliente();
    app.Conectar();
    app.Escutar();
  }

}
