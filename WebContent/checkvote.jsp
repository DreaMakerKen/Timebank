<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<title>check vote</title>
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
	<%@ page import="vote.*"%>
	<%
	 String id = request.getParameter("id");
	 String kind = request.getParameter("kind");
	 String amount = request.getParameter("amount");
	 String person = request.getParameter("person");
	 String time = request.getParameter("time");
	 int votenumber = vBlockchainTest.getVoterSum(kind);
	 %>

<div class="container">
		<h1> <p class="fontCh">琩高щ布戈癟</p></h1>
		<br> <p class="fontCh">﹚基狝叭:<%=kind%></p>
		<br> <p class="fontCh">蹲瞯:<%=amount%></p>
		<br> <p class="fontCh">祇癬:<%=person %></p>
		<br> <p class="fontCh">篒ゎ丁:<%=time%></p>
		<br> <p class="fontCh">щ布计:<%=votenumber%></p>
		
		
	</div>
</body>
</html>