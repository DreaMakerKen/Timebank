
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
    //�ǳƭn�ǰe����� 
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
          System.out.print("�п�J�n�[�J�϶������\n");
          data=scanner.nextLine();
          if(data.equals("T"))
        	  break;
          //�e�Xobject 
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
          System.out.println("Socket�s�u�����D !" ); 
      System.out.println("IOException :" + e.toString()); 
        } 
  } 
  
  public static void main(String args[]) 
  { 
    new SendClient(); 
  } 
}