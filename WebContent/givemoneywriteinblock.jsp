<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>You has Voted Money Give</title>
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

<%
	 String towhichpeer = request.getParameter("towhichpeer");
	 String money = request.getParameter("money");
	 String agree = request.getParameter("agree");
	 String voter = request.getParameter("voter");
	 boolean approve;
	 if(agree.equals("Yes")){
			approve = true; 
	 }
	 else{
			approve = false; 
	 }
	 if(vBlockchainTest.isVoted("MakemoneyProcess", towhichpeer, money, voter)){
		 response.sendRedirect("http://localhost:8080/Tbank4/hasVoted.jsp");
		 System.out.print("ERROR: 你已投過票");
	 }
	 else{
	 	try{
	 		vBlockchainTest.addBlock("MakemoneyProcess", towhichpeer, money, approve, voter);
	 		}
			catch (Exception e) {
				System.out.print("ERROR: fail"+e);	
			}
	 
	 	vBlockchainTest.printChain();
	 }
	 
	/* if(vBlockchainTest.getVoterSum(issue)==3){
		 vBlockchainTest.sum_up(issue);
		 System.out.println("已結算:"+issue);
	 }
	 */
%>
	<div class="container">
		<h1> <p class="fontCh">你的投票資訊</p></h1>
		<br> <p class="fontCh">創建貨幣給哪個節點:<%=towhichpeer%></p>
		<br> <p class="fontCh">多少錢:<%=money%></p>
		<br> <p class="fontCh">同意與否:<%=agree%></p>
		<br> <p class="fontCh">投票者:<%=voter%></p>
	</div>
</body>
</html>