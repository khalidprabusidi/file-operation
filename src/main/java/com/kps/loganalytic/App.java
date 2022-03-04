package com.kps.loganalytic;

public class App {
    public static void main( String[] args ) {
        try {
            if(args.length == 5 && args[0].equals("analytics")){
                LogCLI logCLI = new LogCLI(args);
                logCLI.exec();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
