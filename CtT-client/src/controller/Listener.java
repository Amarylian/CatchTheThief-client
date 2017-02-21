/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author LU
 */
public class Listener{
    
    private Socket socket;
    private BufferedReader in;
    private OutputStream out;
    private Controller controller;
    
    public Listener()
    {
    }
    
    public Listener(Controller c)
    {
        this.controller = c;
    }
    
    public boolean createSocket(String IP, int port)
    {
        try {
            socket = new Socket(IP,port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = socket.getOutputStream();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    
    public void listen() throws IOException
    {
        int c;
        String sMessage = "";
        while((c = in.read()) >0 )
        {
            if((char)c!=';') sMessage = sMessage+((char)c);
            else 
            {
                Message msg = new Message(sMessage);
                System.out.println(msg);
                this.controller.proceedMessage(msg);
                sMessage = "";
            }
        }
        
        this.controller.lostConnection();
        
    }
    
    public synchronized void send(Message msg)
    {
        try {
            out.write(msg.toString().getBytes());
            } catch (IOException ex) {
                System.out.println("Błąd: Nie udało się wysłać wiadomości");
            }
    }
    
}
