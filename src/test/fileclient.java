package test;

import java.io.*;
import java.net.*;

public class  fileclient{
    public static void run() {        
        try { 
            System.out.println("�ɮ׶ǰe..."); 

            String remoteHost="127.0.0.1";
            int port = 1234;
            File file = new File("D:\\123\\blockchain.rar"); 
            
            System.out.printf("���ݥD��: %s%n", remoteHost); 
            System.out.printf("���ݥD���s����: %d%n", port);
            System.out.printf("�ǰe�ɮ�: %s%n", file.getName());

            Socket skt = new Socket(remoteHost, port); 

            System.out.println("�s�u���\�I���նǰe�ɮ�....");

            PrintStream printStream = 
                new PrintStream(skt.getOutputStream()); 
            printStream.println(file.getName()); 

            System.out.print("OK! �ǰe�ɮ�...."); 
            
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
            
            System.out.println("\n�ɮ׶ǰe�����I"); 
        } 
        catch(Exception e) { 
            e.printStackTrace(); 
        } 
    } 
}