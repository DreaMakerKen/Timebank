package TCHAIN; 
 
import java.net.ServerSocket; 
import java.net.Socket; 
 
 
 
public class Sendserver extends java.lang.Thread{ 
  
  private boolean OutServer = false; 
  private ServerSocket server ; 
  private final int ServerPort = 8765; 
 
  
  public Sendserver() 
  { 
    try 
    { 
      server = new ServerSocket(ServerPort); 
      
    } 
    catch(java.io.IOException e) 
    { 
      System.out.println("Socket�Ұʦ����D !" ); 
      System.out.println("IOException :" + e.toString()); 
    } 
  } 
  

 
  
  
  public void run() 
  { 
    Socket socket ; 
    java.io.ObjectInputStream in ; 
    Blockchain blockchain = Blockchain.newBlockchain();
    System.out.println("���A���w�Ұ� !"  ); 
    while(!OutServer) 
    { 
      socket = null; 
      try 
      { 
        synchronized(server) 
        { 
          socket = server.accept(); 
        } 
        System.out.println("���o�s�u : InetAddress = " + 
socket.getInetAddress()  ); 
        socket.setSoTimeout(15000); 
        
 
        in = new 
java.io.ObjectInputStream(socket.getInputStream()); 
       while(true)
    	   {
        String data = (String)in.readObject(); 
        System.out.println("�ڨ��o�����:"+data);
       if(data.equals("T"))
    	   break;
        try
        {
        blockchain.addBlock(data);
        for (Blockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hashNext(); ) {
            Block block = iterator.next();

            if (block != null) {
                boolean validate = ProofOfWork.newProofOfWork(block).validate();
                System.out.println(block.toString() + ", validate = " + validate);
            }
        }
        }
            catch (Exception e) {
                e.printStackTrace();
        }
    	   }
        in.close(); 
        in = null ; 
        socket.close(); 
        
        
        
      } 
      catch(java.io.IOException e) 
      { 
        System.out.println("Socket�s�u�����D !" ); 
        System.out.println("IOException :" + e.toString()); 
      } 
      catch(java.lang.ClassNotFoundException e) 
      { 
        System.out.println("ClassNotFoundException :" + 
e.toString()); 
      } 
    } 
  } 
  
  public static void main(String args[]) 
  { 
    (new Sendserver()).start(); 
  } 
  
}