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
		<h1> <p class="fontCh">�d�ߧ벼��T</p></h1>
		<br> <p class="fontCh">�w���A��:<%=kind%></p>
		<br> <p class="fontCh">�ײv:<%=amount%></p>
		<br> <p class="fontCh">�o�_�H:<%=person %></p>
		<br> <p class="fontCh">�I��ɶ�:<%=time%></p>
		<br> <p class="fontCh">�H�벼��:<%=votenumber%></p>
		
		
	</div>
</body>
</html>