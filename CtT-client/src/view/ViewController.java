/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.awt.EventQueue;
import java.util.List;

/**
 *
 * @author LU
 */
public class ViewController {
    
    private final Controller mainController;
    private ConnectionWindow conWindow;
    private TablesWindow tabWindow;
    private ErrorMessage endMessage;
    
    public ViewController(Controller c){
        this.mainController = c;
    }
    
    public void openConnectionWindow(){
        ViewController c = this;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                conWindow = new ConnectionWindow(c);
            }
        });
    }
    public void openTableWindow(){
        closeConnectionWindow();
        ViewController c = this;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                tabWindow = new TablesWindow(c);
            }
        });
    }
    public void openErrorWindow(String title, String text){
        
        closeConnectionWindow();
        closeTableWindow();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                endMessage = new ErrorMessage(title,text);
            }
        });
    }
    public void closeConnectionWindow(){
        this.conWindow.setVisible(false);
    }
    public void closeTableWindow(){
        this.tabWindow.setVisible(false);
    }
    
    
    
    public void actionConnect(String IP, String port){
        if(!IP.matches("^\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}$")) 
            setConnectionError("Podano nieprawidłowy adres IP.");
        else if(!port.matches("^\\d+$"))
            setConnectionError("Podano nieprawidłowy numer portu.");
        else this.mainController.connect(IP, Integer.parseInt(port));
    }
    public void actionUpdateTableList(){
        mainController.sendTablesUpdate();
    }
    public void actionCreateTable(String name){
        if(!name.matches("^[a-zA-Z0-9]+$"))
            setCreateTableError("Nazwa stołu zawiera niedozwolone znaki.");
        else
            this.mainController.sendTableCreate(name);
    }
    public void actionJoinTable(String name){
        if(name==null)
            this.tabWindow.setTableListError("Nie wybrano rozgrywki");
        else
            this.mainController.sendTableJoin(name);
    }
    public void actionSendMessage(String msg){
        
    }
    
    
    public void setTablesList(List<String> list){
        this.tabWindow.clearTableList();
        list.stream().forEach((s) -> {
            this.tabWindow.addToTableList(s);
        });
    }
    
    public void setConnectionError(String err){
        this.conWindow.setErrorMessage(err);
    }
    public void setCreateTableError(String err){
        this.tabWindow.setCreateError(err);
    }
    public void setTablesError(String err){
        this.tabWindow.setTableListError(err);
    }
    
    
    
    
    
}
