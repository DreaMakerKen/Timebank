
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>傳送方</title>
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

<%@ page import="java.util.*" %>
<%@ page import="test.*" %>
<%@ page import="TCHAIN.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.InetSocketAddress" %>
<%@ page import="java.net.Socket" %>
<%//int port =1900; %>
<%// PeerNetwork peerNetwork = new PeerNetwork(port); %>
<%//BlockchainTest.printAddresses(); %>
  <%  BlockchainTest.createBlockchain("1DFVSsd45fUD4R1eCDDcNGqdsqAWtofXT");
       String from = request.getParameter("from_name");
   		String to=request.getParameter("to_name");
   		int   amount=Integer.parseInt(request.getParameter("amount"));
   		String kind=request.getParameter("kind");
   		String email=request.getParameter("email");
   		//String data=request.getParameter("user_data2");
   		String data=new String(request.getParameter("user_data2").getBytes("ISO-8859-1"),"UTF-8"); 
   	 int balance=BlockchainTest.getBalance(from);
   //	RocksDBUtils.getInstance().closeDB();
   		FileWriter fw = new FileWriter("D:\\123\\Tbank2\\transacation.txt",true);
   		
   		fw.write(from+" "+to+" "+amount+" "+kind+" "+email+"\r\n");
   		
   		
   		
   		fw.close();
   		
   	  
   	/*	Socket client = new Socket();
       InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 8765);
        try {
            client.connect(isa, 10000);
            BufferedOutputStream out_2 = new BufferedOutputStream(client
                    .getOutputStream());
            // 送出字串
            String s="Send From Client:"+"from: "+from+" to:"+to+" amount:"+amount;
            out_2.write(s.getBytes());
            
            out_2.flush();
            out_2.close();
            out_2 = null;
            client.close();
            client = null;
 
        } catch (java.io.IOException e) {
            System.out.println("Socket連線有問題 !");
            System.out.println("IOException :" + e.toString());
        }
   */
   		//BlockchainTest.createWallet();
   		//BlockchainTest.createBlockchain("1NiM83wF4TY6QpJ3q2RcqAm2oS12EDMxZC");
   		
   		%>
   		<c:set var="b" value="<%=balance%>"/>
   		<c:set var="a" value="<%=amount%>"/>
   	<div class="container">
        
                        <h1><p class="fontCh">你所輸入的交易資訊</p></h1><br> <p class="fontCh">受服務者:<%=from%></p>
        				<br> 	<p class="fontCh">服務者:<%=to%></p>
        				<br> 	<p class="fontCh">金額:<%=amount%></p>
        				<br>    <p class="fontCh">種類:<%=kind%></p>
        			    <br>    <p class="fontCh">備註:<%=data%></p>
        			    <c:choose>
        			   <c:when test="${b>a}">
        			<h4><p class="fontCh">    必須等待接收方確認訊息後才會將交易完成 </p> </h4>
        			</c:when>
        			
        			<c:otherwise>
        			  <h4><p class="fontCh"> 餘額不足    </p> </h4>
        			</c:otherwise>
        			</c:choose>
    </div>         				
</body>
</html>