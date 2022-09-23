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
                return new GsendCommand();
            case "gcreate":
                return new GcreateCommand();
            case "gmembers":
                return new GmembersCommand();
            case "gjoin":
                return new GjoinCommand();
            case "gquit":
                return new GquitCommand();
            case "quit":
                return new QuitCommand();
            default:
                throw new RuntimeException("命令行错误,请输入正确的命令");
        }
    }
}
