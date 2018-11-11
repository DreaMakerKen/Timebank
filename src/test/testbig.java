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

public class testbig{
  public static void main(String[] args)throws IOException, InterruptedException
  {
	  Scanner sc=new Scanner(System.in);
	  int port =6779;
	  System.out.println("Starting peer network");
	  PeerNetwork peerNetwork = new PeerNetwork(port);
      peerNetwork.start();
      System.out.println("Starting RPC daemon...  ");
      RpcServer rpcAgent = new RpcServer(port+1);
      rpcAgent.start();
      System.out.println("[  RPC agent is Started in port:"+(port+1)+"  ]");
    ArrayList<String> peers = new ArrayList<String>();
		File peerFile = new File("peers.list");
		if (!peerFile.exists()) {
			String host = InetAddress.getLocalHost().toString();
			FileUtils.writeStringToFile(peerFile, host.substring(5)+":"+port,StandardCharsets.UTF_8,true);
			//System.out.println("new file");
		}else{
			for (String peer : FileUtils.readLines(peerFile,StandardCharsets.UTF_8)) {
				
				String[] addr = peer.split(":");
				if(CommonUtils.isLocal(addr[0])&&String.valueOf(port).equals(addr[1])){
					continue;
				}
				System.out.println("\npeer:"+peer);
				peers.add(peer);
				
				peerNetwork.connect(addr[0], Integer.parseInt(addr[1]));
			}
		}
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
			        pt.send(sc.next());
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
  }
}