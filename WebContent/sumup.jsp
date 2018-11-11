<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
.container {
    border-radius: 5px;
    background-color: #f2f2f2;
    padding: 150px;
	
}
.row:after {
    content: "";
    display: table;
    clear: both;
}
.col-25 {
    
    width: 25%;
    float: left;
    margin-top: 6px;
}
.col-75 {
    float: left;
    width: 75%;
    margin-top: 6px;
}

.col-50 {
    float: left;
    width: 50%;
    margin-top: 6px;
}

input,textarea{
	width:50%;
	padding: 12px 20px;
	margin: 8px 0;
	box-sizing:border-box;
	border:none;
	background-color:rgb(255, 181, 142);
	color:white;
}
input[type=submit],input[type=reset] {
	width:10%;
    background-color: #4CAF50;
    color: white;
    padding: 12px 16px;
    border: none;
    border-radius: 10px;
    cursor: pointer;
    float: right;
}

p.font {
    
	
	font-family: "Comic Sans MS", cursive, sans-serif;
} 

p.fontCh {
	font-family: "Microsoft JhengHei", cursive, sans-serif;
}
</style>
<meta charset="UTF-8">
<title>Sum Up</title>
</head>
<body>
	<%@ page import="vote.*" %>
	<%@ page import="java.io.*" %>
	<%@ page import="java.net.*" %>
	<%
	//BlockchainTest.sum_up().sumUpIssue= request.getParameter("issue");
	String issue = request.getParameter("issue");
	vBlockchainTest.sum_up(issue);
	String lastblockhash =vRocksDBUtils.getInstance().getLastBlockHash();
	  
	  vBlock block= vRocksDBUtils.getInstance().getBlock(lastblockhash);
	  String address = "127.0.0.1"; 
	   int port = 8765; 
	   Socket client = new Socket();
     InetSocketAddress isa = new InetSocketAddress(address,port);
     try {
         client.connect(isa, 10000);
         ObjectOutputStream outB = new ObjectOutputStream(client.getOutputStream());
         
         
         outB.writeObject(block);
         outB.flush();
         outB.close();
         outB = null;
         client.close();
         client = null;

     } catch (java.io.IOException e) {
         System.out.println("Socket連線有問題 !");
         System.out.println("IOException :" + e.toString());
     }
	//vBlockchainTest.printChain();
	System.out.println("Sum up success");
	
	%>
	
	<div class="container">
		<h1> <p class="fontCh">投票資訊</p></h1>
		<br> <p class="fontCh">定價服務:<%=issue%></p>
		<br> <p class="fontCh">已改變價格:<%=vBlockchainTest.getPrice(issue)%></p>
	</div>
	
</body>
</html>