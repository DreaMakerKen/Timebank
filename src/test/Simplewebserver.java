package test;
 
import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import org.apache.commons.io.FileUtils;
 import TCHAIN.*;
 import vote.*;
public class Simplewebserver extends java.lang.Thread {
	
    private boolean OutServer = false;
    private ServerSocket server;
    private final int ServerPort = 8765;// 要監控的port
    
    public Simplewebserver() {
        try {
            server = new ServerSocket(ServerPort);
 
        } catch (java.io.IOException e) {
            System.out.println("Socket啟動有問題 !");
            System.out.println("IOException :" + e.toString());
        }
    }
 
    public void run() {
        Socket socket;
        java.io.ObjectInputStream in;
 
        System.out.println("伺服器已啟動 !");
        
        while (!OutServer) {
            socket = null;
            try {
                synchronized (server) {
                    socket = server.accept();
                }
                System.out.println("取得連線 : InetAddress = "
                        + socket.getInetAddress());
                // TimeOut時間
                socket.setSoTimeout(15000);
 
                in = new java.io.ObjectInputStream(socket.getInputStream());
                Object a=in.readObject();
                //Block block=(Block)in.readObject();
                if(a instanceof Block)
                {
                	Block block;
                	block=(Block)a;
                	System.out.println("我取得的值:" + block.getHash());
                	in.close();
                    in = null;
                    socket.close();
                    
                     Socket socket2=new Socket("127.0.0.1",1500);
                    // PeerThread pt = new PeerThread(socket2);
                   //  pt.start();
                     //pt.send(block);
                     
                     
                  //   peernetwork.connect("127.0.0.1", 1900); 
                   ObjectOutputStream outB = new ObjectOutputStream(socket2
    	                    .getOutputStream());
    	            outB.writeObject(block);
    	            outB.flush();
    	            outB.close();
    	            outB = null;
    	          System.out.println("結束");
    	          socket2.close();
                }
                if(a instanceof vBlock)
                {
                	vBlock block;
                	block=(vBlock)a;
                	System.out.println("我取得的值:" + block.getHash());
                	in.close();
                    in = null;
                    socket.close();
                    
                     Socket socket2=new Socket("127.0.0.1",1500);
                    // PeerThread pt = new PeerThread(socket2);
                   //  pt.start();
                     //pt.send(block);
                     
                     
                  //   peernetwork.connect("127.0.0.1", 1900); 
                   ObjectOutputStream outB = new ObjectOutputStream(socket2
    	                    .getOutputStream());
    	            outB.writeObject(block);
    	            outB.flush();
    	            outB.close();
    	            outB = null;
    	          System.out.println("結束");
    	          socket2.close();
                }
                
 
            } catch (java.io.IOException | ClassNotFoundException e) {
                System.out.println("Socket連線有問題 !");
                System.out.println("IOException :" + e.toString());
            }
 
        }
    }
 
    public static void main(String args[]) throws IOException, InterruptedException,Exception{
       (new Simplewebserver()).start();
       
		
		//System.out.println(peers);
    }
}
