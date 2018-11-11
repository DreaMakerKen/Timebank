package test;

import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PeerThread extends Thread
{
	//private static final Logger LOGGER = LoggerFactory.getLogger(PeerThread.class);
    private Socket socket;
    public PeerReader peerReader;
    public PeerWriter peerWriter;
    
    public Socket getSocket()
    {
    	return this.socket;
    }
    public PeerThread(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
    	System.out.println("\nGot connection from " + socket.getInetAddress() +":"+socket.getPort() +".");
       
      
       peerReader = new PeerReader(socket);
       peerReader.start();
       
    
       peerWriter = new PeerWriter(socket);
       peerWriter.start();
       
       
    }

  
    public void send(Object data)
    {
        if (peerWriter == null)
        {
        	System.out.printf("Couldn't send " + data + " when outputThread is null");
        }
        else
        {
            peerWriter.write(data);
        }
    }
}