package test;
import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.*;
import org.apache.commons.io.FileUtils;
import TCHAIN.*;
import vote.*; 
 public class peer{
		public static String getIp() throws Exception {
	        URL whatismyip = new URL("http://checkip.amazonaws.com");
	        BufferedReader in = null;
	        try {
	            in = new BufferedReader(new InputStreamReader(
	                    whatismyip.openStream()));
	            String ip = in.readLine();
	            return ip;
	        } finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	 public static void main(String args[]) throws Exception
	 {
		 ArrayList<String> peers = new ArrayList<String>();
		 Scanner sc=new Scanner(System.in);
	      String ip=getIp();	
		  int port =1234;
		  System.out.println(ip);
		  System.out.println("Starting peer network");
		  PeerNetwork peerNetwork = new PeerNetwork(port);
	      peerNetwork.start();
	      File peerFile = new File("D:\\123\\Tbank2\\peers.list");
		/*	if (!peerFile.exists()) {
				//String host = InetAddress.getLocalHost().toString(0);
				//String host = getIp();
				String host="127.0.0.1";
				System.out.println();
				FileUtils.writeStringToFile(peerFile, host+":"+port,StandardCharsets.UTF_8,true);
				//System.out.println("new file");
			}
			else{
				
				for (String peer : FileUtils.readLines(peerFile,StandardCharsets.UTF_8)) {

					String[] addr = peer.split(":");
				
					//if(CommonUtils.isLocal(addr[0])&&String.valueOf(port).equals(addr[1])){
					if(addr[0].equals("127.0.0.1") &&String.valueOf(port).equals(addr[1])) {
						System.out.println("YEs");
						continue;
					}
					System.out.println("\npeer:"+peer);
					peers.add(peer);
					
					peerNetwork.connect(addr[0], Integer.parseInt(addr[1]));
				    
					
				}
				
			}*/
	     while(true) {
	    
	    
			
		
		//	Block block=Block.getBlock2();
			//peerNetwork.broadcast(block);
			
			
				
			/*for (String peer : peerNetwork.peers) {
					if (!peers.contains(peer)) {
						peers.add(peer);
						System.out.print("add peer to file:"+peer);
						FileUtils.writeStringToFile(peerFile, "\r\n"+peer,StandardCharsets.UTF_8,true);
					}
				}*/
				
		 //  peerNetwork.broadcast(lastBlockHash);
				
				
		/*	ServerSocket server ;
			 server = new ServerSocket(1500); 
			
			 
		     Socket socket;
		   
		    	    	socket = server.accept();
		   		     System.out.println("取得連線 : InetAddress = " + 
		   			    		socket.getInetAddress()  ); 
		   		              java.io.ObjectInputStream in ; 
		   			    		         in = new 
		   			    		        		   java.io.ObjectInputStream(socket.getInputStream()); 
		   			    		      socket.setSoTimeout(15000);
		   			    		        		        Object data = (Object)in.readObject(); 
		   			    		        		        System.out.println("我取得的值:"+data.toString()); 
		   			    		        		        in.close(); 
		   			    		        		        in = null ; 
		   			    		        		        socket.close(); 
		   			    		        		        server.close();
		   		
		    	       */
	    	
	    	 String data=sc.next();
	    	   	
			if (!peerFile.exists()) {
				//String host = InetAddress.getLocalHost().toString(0);
				//String host = getIp();
				String host="127.0.0.1";
				System.out.println();
				FileUtils.writeStringToFile(peerFile, host+":"+port,StandardCharsets.UTF_8,true);
				//System.out.println("new file");
			}
			else{
				
				for (String peer : FileUtils.readLines(peerFile,StandardCharsets.UTF_8)) {

					String[] addr = peer.split(":");
				
					//if(CommonUtils.isLocal(addr[0])&&String.valueOf(port).equals(addr[1])){
					if(addr[0].equals("127.0.0.1") &&String.valueOf(port).equals(addr[1])) {
						System.out.println("YEs");
						continue;
					}
					System.out.println("\npeer:"+peer);
					
					peers.add(peer);
					peerNetwork.connect(addr[0], Integer.parseInt(addr[1]));
				    
					
				}
				
			}
			
			TimeUnit.SECONDS.sleep(5);
		    peerNetwork.broadcast(data);
		    TimeUnit.SECONDS.sleep(5);
		    peerNetwork.peerThreads.clear();
		   // peerNetwork.peers.clear();
		     
		     
				//System.out.println("案任意鍵繼續");
			//	String s=sc.next();
        //     Block block=BlockchainTest.send("1DFVSsd45fUD4R1eCDDcNGqdsqAWtofXT", "1CkdJLnnUyBBGDp4vb9aF3xC683VPs4Z6f", 2, "wash", "1","1CkdJLnnUyBBGDp4vb9aF3xC683VPs4Z6f");
          
		 
			/*	for (PeerThread pt : peerNetwork.peerThreads) {
				       
					if (pt == null || pt.peerReader == null) {
						break;
					}
					List<Block> dataList = pt.peerReader.readData2();
					
					if (dataList == null) {
						System.out.println("Null return, retry.");
						System.exit(-5);
						break;
					}
			}*/
				
		}
		
	 }
	 
 }