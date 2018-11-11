<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>接收方確認服務</title>

</head>
<style>

.container {
    border-radius: 5px;
    background-color: #FFCC66;
    padding: 300px;
    width: auto;
    height: auto;
	
}
p.font {
	font-family: "Comic Sans MS", cursive, sans-serif;
	font-size: "100px";
	text-align: center;
} 
p.fontCh {
	font-family: "Microsoft JhengHei", cursive, sans-serif;
	font-size: "100px";
	text-align: center;
}
</style>


<body>
  <%@ page import="java.util.List" %>
<%@ page import="test.*" %>
<%@ page import="TCHAIN.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
   <%@ page import="org.apache.commons.mail.DefaultAuthenticator" %>
		   <%@ page import="org.apache.commons.mail.Email" %>
		   <%@ page import="org.apache.commons.mail.EmailException" %>
		   <%@ page import="org.apache.commons.mail.HtmlEmail" %>
<% 
   //RocksDBUtils.getInstance();
  
   String from = request.getParameter("from_name");
		String to=request.getParameter("to_name");
		int  amount=Integer.parseInt(request.getParameter("amount"));
		String privatekey=request.getParameter("privatekey");
		String kind="freetime";
		String email=request.getParameter("email");
   		//String data=request.getParameter("user_data2");
   		String data=new String(request.getParameter("user_data2").getBytes("ISO-8859-1"),"UTF-8"); 
  
   
	
   
	   
	   //System.out.println(from+" "+to+" "+amount+" "+kind+" "+data);
	  
  
	  	 Block newblock =  BlockchainTest.send(from, privatekey,to, amount,kind,data,"1CkdJLnnUyBBGDp4vb9aF3xC683VPs4Z6f" );
		   String address = "127.0.0.1"; 
		   int port = 8765; 
		   Socket client = new Socket();
	        InetSocketAddress isa = new InetSocketAddress(address,port);
	        try {
	            client.connect(isa, 10000);
	            ObjectOutputStream outB = new ObjectOutputStream(client.getOutputStream());
	            
	            
	            outB.writeObject(newblock);
	            outB.flush();
	            outB.close();
	            outB = null;
	            client.close();
	            client = null;
	 
	        } catch (java.io.IOException e) {
	            System.out.println("Socket連線有問題 !");
	            System.out.println("IOException :" + e.toString());
	        }
	        %>
	        <div class="container">
	        
            <h1><p class="fontCh">你所輸入的交易資訊</p></h1><br> <p class="fontCh">受服務者:<%=from%></p>
			<br> 	<p class="fontCh">服務者:<%=to%></p>
			<br> 	<p class="fontCh">金額:<%=amount%></p>
			<br>    <p class="fontCh">種類:<%=kind%></p>
		    <br>    <p class="fontCh">備註:<%=data%></p>
		    <h4><p class="fontCh">時間分發成功</p> </h4>
		    </div>


</body>
</html>