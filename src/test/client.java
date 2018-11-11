package test; 
package TCHAIN;
import java.net.InetSocketAddress; 
import java.net.Socket; 
import java.io.ObjectOutputStream; 
public class client { 
  private String address = "127.0.0.1"; 
  private int port = 8765; 
  public client() 
  { 
    //準備要傳送的資料 
    Block block = new Block(); 
      data.setP(102928); 
    
    Socket client = new Socket() ; 
        InetSocketAddress isa = new 
InetSocketAddress(this.address,this.port); 
        try 
        { 
          client.connect(isa,10000); 
          ObjectOutputStream out = new 
ObjectOutputStream(client.getOutputStream()); 
          //送出object 
          out.writeObject(data); 
          out.flush(); 
          out.close(); 
          out = null ; 
          data = null ; 
          client.close(); 
          client = null ; 
          
        } 
        catch(java.io.IOException e) 
        { 
          System.out.println("Socket連線有問題 !" ); 
      System.out.println("IOException :" + e.toString()); 
        } 
  } 
  
  public static void main(String args[]) 
  { 
    new SendClient(); 
  } 
}