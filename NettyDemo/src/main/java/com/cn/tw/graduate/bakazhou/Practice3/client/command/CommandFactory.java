package com.cn.tw.graduate.bakazhou.Practice3.client.command;

public class CommandFactory{
    private static CommandFactory command = null;
    public static CommandFactory getInstance(){
        if (command == null){
            command = new CommandFactory();
        }
        return command;
    }
    public Command getCommand(String type){
        switch (type){
            case "send":
                return new SendCommand();
            case "gsend":
                return new SendCommand();
            case "gcreate":
                return new SendCommand();
            case "gmembers":
                return new SendCommand();
            case "gjoin":
                return new SendCommand();
            case "gquit":
                return new SendCommand();
            case "quit":
                return new SendCommand();
            default:
                throw new RuntimeException("命令行错误,请输入正确的命令");
        }
    }
}
