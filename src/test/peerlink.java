package test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
/**
 * Servlet implementation class peerlink
 */
@WebServlet(description = "peerlink", urlPatterns = { "/peerlink" })
public class peerlink extends HttpServlet {
	private static final long serialVersionUID = 1L;
       Scanner sc=new Scanner(System.in);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public peerlink() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException{
		 int port = 1900;
		 List<String> peer=null;
		 // System.out.println(ip);
		  System.out.println("Starting peer network");
		  PeerNetwork peerNetwork = new PeerNetwork(port);
	      peerNetwork.start();
	      
	    ArrayList<String> peers = new ArrayList<String>();
			File peerFile = new File("peers.list");
			if (!peerFile.exists()) {
				//String host = InetAddress.getLocalHost().toString(0);
				//String host = getIp();
				String host="127.0.0.1";
				System.out.println();
				try
				{
				FileUtils.writeStringToFile(peerFile, host+":"+port,StandardCharsets.UTF_8,true);
				}
				catch(Exception e)
				{
					
				}
				
				//System.out.println("new file");
			}else{
				 try
				 {
					  peer=FileUtils.readLines(peerFile,StandardCharsets.UTF_8);
				 }
				 catch(Exception e)
				 {
					 System.out.println("wrong");
				 }
				for (int i=0;i<peer.size();i++) {

					String[] addr = peer.get(i).split(":");
				
					//if(CommonUtils.isLocal(addr[0])&&String.valueOf(port).equals(addr[1])){
					if(addr[0].equals("127.0.0.1") &&String.valueOf(port).equals(addr[1])) {
						System.out.println("YEs");
						continue;
					}
					System.out.println("\npeer:"+peer.get(i));
					peers.add(peer.get(i));
					
					peerNetwork.connect(addr[0], Integer.parseInt(addr[1]));
				}
				
			}
			
		
		/*	for (String peer : peerNetwork.peers) {
					if (!peers.contains(peer)) {
						peers.add(peer);
						System.out.print("add peer to file:"+peer);
						try
						{
						FileUtils.writeStringToFile(peerFile, "\r\n"+peer,StandardCharsets.UTF_8,true);
						}
						catch(Exception e)
						{
							
						}
						
					}
				}*/
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
			
			//String s=sc.next();
			//peerNetwork.broadcast(s)	;
			//System.out.println(peers);
	
		
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");  
        PrintWriter out = response.getWriter();  
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");  
        out.println("<HTML>");  
        out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");  
        out.println("  <BODY>");  
        out.print("    This is ");  
        out.print(this.getClass());  
        out.println(", using the GET method");  
        out.println("  </BODY>");  
        out.println("</HTML>");  
        out.flush();  
        out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  response.setContentType("text/html");  
	        PrintWriter out = response.getWriter();  
	        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");  
	        out.println("<HTML>");  
	        out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");  
	        out.println("  <BODY>");  
	        out.print("    This is ");  
	        out.print(this.getClass());  
	        out.println(", using the POST method");  
	        out.println("  </BODY>");  
	        out.println("</HTML>");  
	        out.flush();  
	        out.close();
	}

}
