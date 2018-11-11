package TCHAIN;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcThread extends Thread {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcThread.class);
	
    private Socket socket;
    public String res;
    public String req;

   
    public RpcThread(Socket socket){
        this.socket = socket;
    }
  
    @Override
    public void run(){
    	System.out.println("[   Welcome RPC Daemon    ]");  	
        try{
            req = null;
            res = null;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;
            System.out.println("[   Welcome RPC Daemon    ]");
            while((input = in.readLine()) != null){
                if ("HELP".equalsIgnoreCase(input)){
                	out.println("################################################# COMMANDS ##################################################");
                    out.println("#     1) getinfo             - Gets Your Wallet Address.                                                    #");
                    out.println("#     2) getbalance          - Gets A Wallet's Balance                                                      #");
                    out.println("#     3) getchain            - Gets block chain infomations.                                                #");
                    out.println("#     4) send <vac> <address>  - Send <vac> to <address> as Transaction                                     #");
                    out.println("#     5) mine <difficulty>   - Mine <difficulty> with Block                                                 #");
                    out.println("#############################################################################################################");
                } else {
                    req = input;
                    while (res == null){
                    	TimeUnit.MILLISECONDS.sleep(2500);
                    }
                    out.println(res);
                    req = null;
                    res = null;
                }
            }
        } catch (Exception e){
        	System.out.println("An RPC client has disconnected.");
        }
    }
}