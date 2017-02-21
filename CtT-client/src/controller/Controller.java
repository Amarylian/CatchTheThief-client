/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import view.ViewController;

/**
 *
 * @author LU
 */
public class Controller {
    
    private final ViewController viewController;
    private final Semaphore connectionSem;
    private final Listener listener;
    
    
    public Controller() throws InterruptedException
    {
        this.viewController = new ViewController(this);
        this.listener = new Listener(this);
        
        this.connectionSem = new Semaphore(1,true);
        this.connectionSem.acquire();
    }
    
    public void start() throws InterruptedException, IOException
    {
        this.viewController.openConnectionWindow();
        this.connectionSem.acquire();
        this.viewController.openTableWindow();
        this.listener.listen();
    }
    
    public void lostConnection()
    {
        this.viewController.openErrorWindow("Utracono połaczenie", "Utracono połaczenie z serwerem. Dalsza rozgrywka nie jest możliwa.");
    }
    
    public void connect(String IP, int portNumber)
    {
        if(listener.createSocket(IP, portNumber))
        {
            this.connectionSem.release();
        }
        else
            this.viewController.setConnectionError("Nie udało się nawiązać połączenia");
    }
    
    public void sendTablesUpdate(){
        Message msg = new Message();
        msg.setCommand("TABLE_LIST");
        this.listener.send(msg);
    }
    
    public void sendTableCreate(String name){
        Message msg = new Message();
        msg.setCommand("TABLE_CREATE");
        msg.addArgument(name);
        this.listener.send(msg);
    }
    
    public void sendTableJoin(String name){
        Message msg = new Message();
        msg.setCommand("TABLE_JOIN");
        msg.addArgument(name);
        this.listener.send(msg);
    }
    
    public void proceedMessage(Message msg){
        switch(msg.getCommand()){
            case "TABLE_LIST":
                if(msg.getDecision().equals("ERR")) this.viewController.setTablesError(msg.getArgument(0));
                else {
                    this.viewController.setTablesError("");
                    this.viewController.setTablesList(msg.getArguments());
                }
                break;
            case "TABLE_CREATE":
                if(msg.getDecision().equals("ERR")) this.viewController.setCreateTableError(msg.getArgument(0));
                else {
                    this.viewController.setCreateTableError("");
                    this.sendTablesUpdate();
                }
                break;
            case "TABLE_JOIN":
                if(msg.getDecision().equals("ERR")) this.viewController.setTablesError(msg.getArgument(0));
                else{
                    System.out.println("Dołączono do stołu ;)");
                }
                break;
            default:
                System.out.println("Błąd: Nie rozpoznano polecenia "+msg.getCommand());
        }
    }
    
    
    
}
