<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>You has Voted</title>
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
<%@ page import="vote.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%System.out.println("已完成投票!");%>
	
	<%
	 String issue = request.getParameter("issue");
	 String price = request.getParameter("price");
	 String agree = request.getParameter("agree");
	 String voter = request.getParameter("voter");
	 boolean approve;
	 if(agree.equals("Yes")){
			approve = true; 
	 }
	 else{
			approve = false; 
	 }
	 
	 if(vBlockchainTest.isVoted("data", issue, price, voter)){
		 response.sendRedirect("http://localhost:8080/voteweb/hasVoted.jsp");
		 System.out.print("ERROR: 你已投過票");
	 }
	 else{
	 	try{vBlockchainTest.addBlock("data", issue, price, approve, voter);}
			catch (Exception e) {
				System.out.print("ERROR: fail"+e);	
			}
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
	 }
	 
/*	 if(vBlockchainTest.getVoterSum(issue)==3){
		 vBlockchainTest.sum_up(issue);
		 System.out.println("已結算:"+issue);
	 }*/
	  
	   
	 //vBlockchainTest.printChain();
	%>
	<div class="container">
		<h1> <p class="fontCh">你的投票資訊</p></h1>
		<br> <p class="fontCh">欲改變定價服務:<%=issue%></p>
		<br> <p class="fontCh">價格:<%=price%></p>
		<br> <p class="fontCh">同意與否:<%=agree%></p>
		<br> <p class="fontCh">投票者:<%=voter%></p>
	</div>

</body>
</html>