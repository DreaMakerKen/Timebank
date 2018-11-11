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
<title>Make Money Sum up item</title>
</head>
<body>
	<div class="container">
	<form action="makemoneysumup.jsp" method="post">
		
		<div class="row"> 
		<div class="col-25">
			<p class="fontCh"> 創建貨幣投票結算項目： </P>
		</div>
		<br>
		<div class="col-75">
			<input name="towhichpeer" type="text" placeholder="創建貨幣給哪個節點">
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