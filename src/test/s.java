package test; 
 
import java.net.ServerSocket; 
import java.net.Socket; 
import TCHAIN.*; 
 
public class s extends java.lang.Thread{ 
  
  private boolean OutServer = false; 
  private ServerSocket server ; 
  private final int ServerPort = 1111; 
 
  
  public s() 
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
        
        Block data = (Block)in.readObject(); 
        System.out.println("�ڨ��o����:"+data.toString()); 
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
    (new s()).start(); 
  } 
  
}