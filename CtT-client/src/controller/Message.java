/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author LU
 */
public class Message {
    
    private String command;
    private String decision;
    private List<String> arguments;
    
    public Message()
    {
        arguments = new ArrayList<>();
        command = "";
        decision = "";
    }
    
    public Message(String msg)
    {
        arguments = new ArrayList<>();
        String[] list = split(msg,2);
        command = list[0];
        if(this.hasDecision())
        {
            list = split(list[1],2);
            decision = list[0];
        }
        else decision = "";
        if(this.hasArgumentWithSpace()) arguments.add(list[1]);
        if(this.hasArguments())
        {
            list = split(list[1],0);
            for(String s:list)
                addArgument(s);
        }
        
    }
    
    private static String[] split(String msg, int i)
    {
        return msg.split(" ",i);
    }
    
    private boolean hasDecision()
    {
        return command.equals("MESSAGE")
                || command.equals("TABLE_CREATE")
                || command.equals("TABLE_JOIN")
                || command.equals("TABLE_LIST");
    }
    
    private boolean hasArgumentWithSpace()
    {
        return decision.equals("ERR");
    }
    
    private boolean hasArguments()
    {
        return (command.equals("TABLE_LIST") && decision.equals("ACK"));
    }
    
    public final void addArgument(String arg)
    {
        arguments.add(arg);
    }
    
    @Override
    public String toString()
    {
        String res = command;
        if(!decision.equals("")) res = res+" "+decision;
        for(String s:arguments) res = res+" "+s;
        return res+";";
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String getArgument(int i){
        return arguments.get(i);
    }
    
    
    public String getCommand() {
        return command;
    }

    public String getDecision() {
        return decision;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }
    
    
    
}
