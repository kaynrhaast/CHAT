/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author T-Gamer
 */
public class Servidor extends Thread {

  private static ArrayList<BufferedWriter> clientes;
  private static ServerSocket server;
  private String nome;
  private Socket con;
  private InputStream in;
  private InputStreamReader inr;
  private BufferedReader bfr;

  public Servidor(Socket con) {
    this.con = con;
    try {
      in = con.getInputStream();
      inr = new InputStreamReader(in);
      bfr = new BufferedReader(inr);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void Run() {
    try {
      String msg;
      OutputStream ou = this.con.getOutputStream();
      Writer ouw = new OutputStreamWriter(ou);
      BufferedWriter bfw = new BufferedWriter(ouw);
      clientes.add(bfw);
      nome = msg = bfr.readLine();
      while(!"Sair".equalsIgnoreCase(msg)&& msg!=null){
        msg = bfr.readLine();
        sendToAll(bfw,msg);
        System.out.println(msg);
      }
            
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendToAll(BufferedWriter bfwSaida, String msg) throws IOException{
    BufferedWriter bws;
    
    for(BufferedWriter bw : clientes){
      bws = (BufferedWriter)bw;
      if(!(bfwSaida == bws)){
        bw.write(nome +"->"+msg+"\r\n");
        bw.flush();
      }
    }
    
  }

  public static void main(String[] args) {
    try {
      JLabel lblMsg = new JLabel("Porta do servidor");
      JTextField txtPort = new JTextField("8000");
      Object[] texts = {lblMsg,txtPort};
      JOptionPane.showMessageDialog(null, texts);
      server = new ServerSocket(Integer.parseInt(txtPort.getText()));
      clientes = new ArrayList<BufferedWriter>(); 
      JOptionPane.showMessageDialog(null, "Servidor ativo na porta "+ txtPort.getText());
      while(true){
        System.out.println("Aguardando conexão...");
        Socket con = server.accept();
        System.out.println("Cliente conectado...");
        Thread t = new Servidor(con);
        t.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
