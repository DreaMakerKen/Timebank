
package TCHAIN; 
 
import java.net.InetSocketAddress; 
import java.net.Socket; 
import java.io.ObjectOutputStream; 
import java.util.*;
public class SendClient { 
  private String address = "127.0.0.1"; 
  private int port = 8765; 
  public SendClient() 
  { 
    //準備要傳送的資料 
  //  Datatest data = new Datatest(); 
    //  data.setP(102928); 
	   
	   Scanner scanner = new Scanner(System.in); 
       String data;
    
    Socket client = new Socket() ; 
        InetSocketAddress isa = new 
InetSocketAddress(this.address,this.port); 
        try 
        { 
          client.connect(isa,10000); 
          ObjectOutputStream out = new 
ObjectOutputStream(client.getOutputStream());
          while(true)
          {	  
          System.out.print("請輸入要加入區塊的資料\n");
          data=scanner.nextLine();
          if(data.equals("T"))
        	  break;
          //送出object 
          out.writeObject(data);
    //      out.flush(); 
           
  //        out = null ; 
          data = null ;
          }
          out.flush();
          out.close(); 
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