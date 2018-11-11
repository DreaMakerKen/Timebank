<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
.container {
    border-radius: 5px;
    background-color: #f2f2f2;
    padding: 145px;
	
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
<title>被服務方</title>
</head>
<body>
<div class="container">
<form action="hello3.jsp" method="post">
  <h1><p class="font">Transcation測試</p></h1><br>
 
  <%@ page import="test.*" %>
<%@ page import="TCHAIN.*" %>



	
      
  <div class="row"> 
	<div class="col-25">
		<p class="fontCh"> 受服務者： </P>
	</div>
	<br>
	<div class="col-75">
		<input name="from_name" type="text" placeholder="Your Address">
		<br>
	</div>
</div> 
<div class="row">
	<div class="col-25">
		<p class="fontCh"> 服務者：  </P>
	</div>
	<br>
	<div class="col-75">
		<input name="to_name" type="text" placeholder="Transcation Address">
		<br>
	</div>
</div> 
<div class="row">
	<div class="col-25">
		<p class="fontCh"> 金額: </P>
	</div>
	<br>
	<div class="col-75">
		<input name="amount" type="number" step="0.01" min="0" placeholder="Transcation Amount">
		<br>
	</div>
</div>
<div class="row">
	<div class="col-25">
		<p class="fontCh"> 私鑰: </P>
	</div>
	<br>
	<div class="col-75">
		<input name="privatekey" type="text"  placeholder="privatekey">
		<br>
	</div>
</div>
<div class="row">
	<div class="col-25">
		<p class="fontCh"> 種類：  </P>
	</div>
	<br>
	<div class="col-75">
		<input name="kind" type="text" placeholder="Transcation Kind">
		<br>
	</div>
</div> 
<div class="row">
	<div class="col-25">
		<p class="fontCh"> 備註: </P>
	</div>
	<br>
	<div class="col-75">
		<textarea style="width:50%;height:50%;" name="user_data2" placeholder="Write something" >
		</textarea>
		<br>
	</div>
</div>
<div class="row">
	<div class="col-25">
		<p class="fontCh"> email: </P>
	</div>
	<br>
	<div class="col-75">
		<textarea style="width:50%;height:50%;" name="email" placeholder="Write something" >
		</textarea>
		<br>
	</div>
</div>
<div class="col-75">
	<input type="reset" value="reset">
	
	<input name="submit" type="submit" value="send">
</div>
</form>
</div>
<input type = "button" value = "創建錢包地址" onclick = "window.location.href = 'wallet.jsp'">
</body>
</html>