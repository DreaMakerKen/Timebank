package TCHAIN;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.*;
import java.net.*;
import java.util.*;

public class Simplewebserver implements Runnable {

    ServerSocket server = null ;

    public static void main(String args[]) throws Exception {
        new Simplewebserver(80);        // HTTP port
    }

    public Simplewebserver(int port) throws Exception {

        server = new ServerSocket(port) ;

        System.out.println("Start Simple Web Server...");
    	System.out.println(RocksDBUtils.getInstance().getLastIndex());
        run();
    }

    public void run() {

        try {

            //
            // 1, 等待一個新的連接請求(Request).
            //
    
            Socket s = server.accept();

            //
            // 2, 開新Thread處理新連接請求.
            //

            Thread task = new Thread(this);
            task.start();

            //
            // 3, 處理請求內容.
            //

            try {
          
                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                DataOutputStream os = new DataOutputStream(s.getOutputStream());

                handleRequest(s, reader, os);
            
            }
            finally {
                s.close();
            }
        } catch (Exception e) {
        }
    }

    void handleRequest(Socket s, BufferedReader reader, DataOutputStream os) throws Exception {

        try {

            //
            // 1, 讀取HTTP Header字串(以一個空行作結束)
            //

            String request = "";

            while (true) {

                String line = reader.readLine();
                if (null == line) {
                    break;
                }

                request += line + "\r\n";
                if (0 == line.length()) {
                    break;
                }
            }

            System.out.print(request);

            //
            // 2, 解出請求的資源路徑(或包含?query字串)
            //

            String path = getPath(request);
            path = java.net.URLDecoder.decode(path, "UTF-8");

            //
            // 3, 處理請求的資源. (測試: 只處理Homepage的請求)
            //

            if ("/".equals(path) || "index.html".equals(path)) {
              String homepage = "<!doctype html>\r\n" + 
                		"<html>\r\n" + 
                		"<head>\r\n" + 
                		"<meta charset=\"utf-8\">\r\n" + 
                		"<title>send</title>\r\n" + 
                		"</head>\r\n" + 
                		"\r\n" + 
                		"<body>\r\n" + 
                		"<form action=\"print\" method=\"post\">\r\n" + 
                		"  POST<br>\r\n" + 
                		"  name\r\n" + 
                		"  <input name=\"user_name\" type=\"text\">\r\n" + 
                		"  money<input name=\"user_pass\" type=\"text\">\r\n" + 
                		"  <input name=\"submit\" type=\"submit\" value=\"token\">\r\n" + 
                		"</form>\r\n" + 
                		"</body>\r\n" + 
                		"</html>";
              
                os.writeBytes(
                     "HTTP/1.0 200 OK\r\nContent-Type: text/html\r\nContent-Length:" +
                     homepage.length()+
                     "\r\n\r\n" +
                     homepage);

                 return;
            }
            if ("/print".equals(path))
            		{
                          String s1="<!doctype html>\r\n" + 
                          		"<html>\r\n" + 
                        		"<head>\r\n" + 
                        		"<meta charset=\"utf-8\">\r\n" + 
                        		"<title>send</title>\r\n" + 
                        		"</head>\r\n" + 
                        		"\r\n" + 
                        		"<body>\r\n" + 
                        		"<form action=\"print\" method=\"post\">\r\n" + 
                        		"  POST<br>\r\n" + 
                        		"  name\r\n" + 
                        		"  <input name=\"user_name\" type=\"text\">\r\n" + 
                        		"  money<input name=\"user_pass\" type=\"text\">\r\n" + 
                        		"  <input name=\"submit\" type=\"submit\" value=\"token\">\r\n" + 
                        		"</form>\r\n" + 
                        		"</body>\r\n" + 
                        		"</html>";
                          os.writeBytes(
                                  "HTTP/1.0 200 OK\r\nContent-Type: text/html\r\nContent-Length:" +
                                  s1.length()+
                                  "\r\n\r\n" +
                                  s1);

                          return;
            		}
           
            //
            // TODO: 處理其它請求.
            //

            os.writeBytes("HTTP/1.0 404 File Not Found\r\n\r\n");

        } catch (Exception e) {
            os.writeBytes("HTTP/1.0 500 Internal Server Error\r\n\r\n");
        }
    }

    String getPath(String request) {

        //
        // 截取介於"GET" ... "HTTP/"之間的資源路徑(包含Query對如果有指定的話),
        //

        int beginStart = request.indexOf("GET");
        if (beginStart < 0) {
            return null;
        }

        int beginEnd = beginStart + 3;
        int endStart = request.indexOf("HTTP/", beginEnd);
        if (endStart < 0) {
            return null;
        }

        return request.substring(beginEnd, endStart).trim();
    }

}
