package test;
import java.io.*;
import java.net.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class filiserver {
	private static String unrarCmd = "C:\\Program Files\\WinRAR\\UnRar x ";  
    public static void main(String[] args) { 
        try { 
            int port = 1234;
            System.out.println("²���ɮױ���..."); 
            System.out.printf("�N�����ɮש�s����: %d%n", port); 

            ServerSocket serverSkt = new ServerSocket(port); 
            
            while(true) { 
                System.out.println("��ť��...."); 
                
                Socket clientSkt = serverSkt.accept();
                
                System.out.printf("�P %s �إ߳s�u%n", 
                        clientSkt.getInetAddress().toString());  
                
                // ���o�ɮצW��
                String fileName = new BufferedReader(
                                    new InputStreamReader(
                                      clientSkt.getInputStream())).readLine();
                
                System.out.printf("�����ɮ� %s ...", fileName); 

                BufferedInputStream inputStream = 
                    new BufferedInputStream(clientSkt.getInputStream()); 
                BufferedOutputStream outputStream = 
                    new BufferedOutputStream(new FileOutputStream(fileName)); 
                
                int readin; 
                while((readin = inputStream.read()) != -1) { 
                    outputStream.write(readin);
                    Thread.yield();
                } 

                outputStream.flush();
                outputStream.close();                
                inputStream.close(); 
                
                clientSkt.close(); 
                
                  
               
                System.out.println("\n�ɮױ��������I"); 
                String rarFileName="D:\\123\\blockchain.rar";
                String destDir="D:\\123\\Tbank2\\";
                unrarCmd += rarFileName + " " + destDir;  
                try {  
                    Runtime rt = Runtime.getRuntime();  
                    Process p = rt.exec(unrarCmd);   
                } catch (Exception e) {  
                    System.out.println(e.getMessage());     
                }  
            } 
        } 
        catch(Exception e) { 
            e.printStackTrace(); 
        } 
    }     
}