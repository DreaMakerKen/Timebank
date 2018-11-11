package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import vote.*; 

public class PeerNetwork extends Thread {
	
	
    private int port;
    private boolean runFlag = true;
    
    public List<PeerThread> peerThreads;
    public List<String> peers;
    public int getPort()
    {
      return port;
    }
    public PeerNetwork() {
        this.port = 8015;
        this.peerThreads = new ArrayList<PeerThread>();
        this.peers = new ArrayList<String>();
    }

    public PeerNetwork(int port) {
    	this.port = port;
    	this.peerThreads = new ArrayList<PeerThread>();
    	this.peers = new ArrayList<String>();
    }


    public void connect(String host, int port){
    	Socket socket = null;
    	try {
    		socket = new Socket();
    		System.out.println("要連線的host:"+host+"  port:"+port);
    		socket.connect(new InetSocketAddress(host,port),10000); 
			String remoteHost = socket.getInetAddress().getHostAddress();
			int remotePort = socket.getPort();
			System.out.println("socket " + remoteHost + ":" + remotePort + " connected.");
			peers.add(remoteHost + ":" + remotePort);
			PeerThread pt = new PeerThread(socket);
			peerThreads.add(pt);
			pt.start();
		} catch (IOException  e ) {
			System.out.print("socket " + host +":"+port+ " can't connect."+" "+e);
		}
    	
    }

    @Override
    public void run() {
    	 
        try {
        	
        	//System.out.println("1234");
            ServerSocket listenSocket = new ServerSocket(port);
          
            while (runFlag) 
            {
            	PeerThread peerThread = new PeerThread(listenSocket.accept());
                peerThreads.add(peerThread);
                peerThread.start();
                
             
            }
            listenSocket.close();
        } catch (Exception e) {
        	System.out.print(e+"\npeer already start");
        }
    }

    public  void broadcast(Object data) throws IOException {
        for (PeerThread pt: peerThreads) {
        	//System.out.println("Sent:" + data);
            if( pt!=null && (pt.getSocket().getPort()==1900 || pt.getSocket().getPort()==1234)  ){
            	pt.send(data);
            }
        }
        /*
    	FileReader fr = new FileReader("D:\\123\\Tbank2\\peers.list");
      String line=null;
    	BufferedReader br = new BufferedReader(fr);
    	 while((line=br.readLine())!=null)
    	{
    		 if(line.substring(10).equals(this.port+""))
    		 {
    			 
    		 }
    		 else
    		 {
    			 Socket bb=new Socket("127.0.0.1",Integer.parseInt(line.substring(10)));
    			 
    		 }
    	     }*/
    }
}