<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<title>Give money vote</title>
</head>

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

<body>
<div class="container">
<form action="givemoneywriteinblock.jsp" method="post">
<h1 class="font">是否同意創建貨幣投票!</h1><br>
<%@ page import="vote.*" %>

 <%//vBlockchainTest.printChain(); %>
 <% //String amount = request.getParameter("amount");
	//String who = request.getParameter("who");
  %>
	
	<div class="row"> 
		<div class="col-25">
			<p class="fontCh"> 創建貨幣給哪個節點： </P>
		</div>
		<br>
		<div class="col-75">
			<input name="towhichpeer" type="text" placeholder="給哪個節點">
			<br>
		</div>
	</div> 
	
	<div class="row">
		<div class="col-25">
			<p class="fontCh"> 多少錢：  </P>
		</div>
		<br>
		<div class="col-75">
			<input name="money" type="text" placeholder="多少貨幣">
			<br>
		</div>
	</div> 
	
	<div class="row">
		<div class="col-25">
			<p class="fontCh"> 同意：  </P>
		</div>
		<br>
		<div class="col-50">
			<div>
            	<input type="radio" id="approve" name="agree" value="Yes" checked />
            	<label for="approve">同意</label>
        	</div>

        	<div>
            	<input type="radio" id="disapprove" name="agree" value="No" />
            	<label for="disagree">不同意</label>
        	</div>
			<br>
		</div>
	</div>
	
		
	
	 <div class="row">
		<div class="col-25">
			<p class="fontCh"> 投票者：  </P>
		</div>
		<br>
		<div class="col-75">
			<input name="voter" type="text" placeholder="投票人">
			<br>
		</div>
	</div>
	
	<div class="col-75">
	<input type="reset" value="reset">
	
	<input name="submit" type="submit" value="send">
	</div>
</form>
</div>
</body>
</html>