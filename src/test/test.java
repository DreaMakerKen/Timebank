package test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import TCHAIN.BlockchainTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
public class test{
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
  public static void main(String args[])throws IOException, InterruptedException,Exception
  {
	 
	  //System.out.println(getIp());
	 Scanner sc=new Scanner(System.in);
      String ip=getIp();	
	  int port = 1900;
	  System.out.println(ip);
	  System.out.println("Starting peer network");
	  PeerNetwork peerNetwork = new PeerNetwork(port);
      peerNetwork.start();
      
    ArrayList<String> peers = new ArrayList<String>();
		File peerFile = new File("D:\\123\\Tbank2\\peers.list");
		if (!peerFile.exists()) {
			//String host = InetAddress.getLocalHost().toString(0);
			//String host = getIp();
			String host="127.0.0.1";
			System.out.println();
			FileUtils.writeStringToFile(peerFile, host+":"+port,StandardCharsets.UTF_8,true);
			//System.out.println("new file");
		}else{
			
			for (String peer : FileUtils.readLines(peerFile,StandardCharsets.UTF_8)) {

				String[] addr = peer.split(":");
			
				//if(CommonUtils.isLocal(addr[0])&&String.valueOf(port).equals(addr[1])){
				if(addr[0].equals(getIp()) &&String.valueOf(port).equals(addr[1])) {
					System.out.println("YEs");
					continue;
				}
				System.out.println("\npeer:"+peer);
				peers.add(peer);
				
				peerNetwork.connect(addr[0], Integer.parseInt(addr[1]));
			}
			
		}
		BlockchainTest.printChain();
		while (true) {
			for (String peer : peerNetwork.peers) {
				if (!peers.contains(peer)) {
					peers.add(peer);
					System.out.print("add peer to file:"+peer);
					FileUtils.writeStringToFile(peerFile, "\r\n"+peer,StandardCharsets.UTF_8,true);
				}
			}
			peerNetwork.peers.clear();


			for (PeerThread pt : peerNetwork.peerThreads) {
			       
				if (pt == null || pt.peerReader == null) {
					break;
				}
				List<String> dataList = pt.peerReader.readData();
				if (dataList == null) {
					System.out.println("Null return, retry.");
					System.exit(-5);
					break;
				}
		}
			
	}
	
	//	String s=sc.next();
		//peerNetwork.broadcast(s)	;
		//System.out.println(peers);
		
		
	}
	
			
	
  }
