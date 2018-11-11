package test;

import java.io.*;
import java.net.*;

public class  fileclient{
    public static void run() {        
        try { 
            System.out.println("檔案傳送..."); 

            String remoteHost="127.0.0.1";
            int port = 1234;
            File file = new File("D:\\123\\blockchain.rar"); 
            
            System.out.printf("遠端主機: %s%n", remoteHost); 
            System.out.printf("遠端主機連接埠: %d%n", port);
            System.out.printf("傳送檔案: %s%n", file.getName());

            Socket skt = new Socket(remoteHost, port); 

            System.out.println("連線成功！嘗試傳送檔案....");

            PrintStream printStream = 
                new PrintStream(skt.getOutputStream()); 
            printStream.println(file.getName()); 

            System.out.print("OK! 傳送檔案...."); 
            
            BufferedInputStream inputStream = 
                new BufferedInputStream( 
                        new FileInputStream(file)); 

            int readin; 
            while((readin = inputStream.read()) != -1) { 
                 printStream.write(readin); 
                 Thread.yield();
            } 

            printStream.flush();
            printStream.close();
            inputStream.close(); 
            
            skt.close();
            
            System.out.println("\n檔案傳送完畢！"); 
        } 
        catch(Exception e) { 
            e.printStackTrace(); 
        } 
    } 
}