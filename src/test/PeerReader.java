package test;
import java.io.BufferedReader;
import vote.*;
import java.io.File;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.tribes.io.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import TCHAIN.*;
public class PeerReader extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(PeerReader.class);
	
    private Socket socket;

    private ArrayList<String> receivedData = new ArrayList<String>();
    private ArrayList<Block> receivedData2 = new ArrayList<Block>();
    
    public PeerReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
        	
           // BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           // String input;
        	//ObjectReader in = new  ObjectReader(socket);
        	ObjectInputStream in = new java.io.ObjectInputStream(socket.getInputStream());
        	Object a=in.readObject();
          if(a instanceof Block)
          {
        	Block input;
        	//input=(Block)in.readObject();
        	input=(Block)a;
        	receivedData2.add(input);
        	System.out.println("取得的數據");
        	System.out.println("from:"+socket.getPort()+" "+input);
        	Blockchain blockchain=Blockchain.createBlockchain("1CkdJLnnUyBBGDp4vb9aF3xC683VPs4Z6f");
            blockchain.addBlock(input);
       	    new UTXOSet(blockchain).update(input);
            
          }
          if(a instanceof vBlock)
          {
        	  vBlock input;
        	  input=(vBlock)a;
        	  
        	  System.out.println("取得的數據");
        	  System.out.println("from:"+socket.getPort()+" "+input);
        	  vBlockchain  blockchain=vBlockchain.newBlockchain();
        	  blockchain.addBlock(input);
        	  
          }
          if(a instanceof String)
          {
        	  String input;
        	  input=(String)a;
        	  
        	  System.out.println("取得的數據");
        	  System.out.println("from:"+socket.getPort()+" "+input);
        
          }
         /*  if(input.getIndex()==0)
           {
        	   System.out.println("傳送同步區塊");
           }
          else
           {*/
         // if(Block.getBlock2().getIndex()==input.getIndex()-1)
          // {
        	   
            // Blockchain blockchain=Blockchain.createBlockchain("1CkdJLnnUyBBGDp4vb9aF3xC683VPs4Z6f");
             //blockchain.addBlock(input);
        	 // new UTXOSet(blockchain).update(input);
       //    }
         //  else
          // {
        	  
        	   //System.out.println("鏈長較短");
        	 //  System.out.println("同步區塊");
        	//  Block block=new Block("",0,"",input.getTransactions(), Instant.now().getEpochSecond(),0);
        	  // PeerWriter pw=new PeerWriter(socket);
        	  // pw.write(block);
           //}
           //}
        /*   while ((input = in.readLine()) != null) {
                receivedData.add(input);
                System.out.println("from:"+socket.getPort()+" "+input);
                System.out.println("取得的區塊數據");
                
            */
        	       	
        } catch (Exception e) {
        	System.out.printf("Peer " + socket.getInetAddress() +" "+socket.getPort()+ " disconnected."+e);
        }
    }

    public List<String> readData() {
        ArrayList<String> inputBuffer = new ArrayList<String>(receivedData);
        receivedData.clear(); //clear 'buffer'
        return inputBuffer;
    }
    public List<Block> readData2() {
        ArrayList<Block> inputBuffer = new ArrayList<Block>(receivedData2);
        receivedData2.clear(); //clear 'buffer'
        return inputBuffer;
    }
    }
