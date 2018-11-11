<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<title>Insert title here</title>
</head>
<style>
	.block{
		    margin: auto;
            border-radius: 20px;
            padding: 10px;
            background-color: rgb(155, 255, 164);
	}
	p.font{
		font-family: "Comic Sans MS", cursive, sans-serif;
	
	}
	h1.fonth
	{
	  font-family: "Comic Sans MS", cursive, sans-serif;
	}
</style>
<body>
<%@ page import="test.*" %>
<%@ page import="TCHAIN.*" %>
<h1>Blockchaindata</h1>
<% Blockchain blockchain = Blockchain.initBlockchainFromDB();
for (Blockchain.BlockchainIterator iterator = blockchain.getBlockchainIterator(); iterator.hashNext(); ) {
    Block block = iterator.next();
    if (block != null) {
        boolean validate = ProofOfWork.newProofOfWork(block).validate();
       
        Transaction[] tx=block.getTransactions();
        int amount=tx[0].getOutputs()[0].getValue();
          String i="\nindex:"+block.getIndex();
          String h="\nhash:"+block.getHash();
          String p="\nPrevBlockHash"+block.getPrevBlockHash();
          String t="\ntimestamp:"+block.getTimeStamp();
          String f="\nfrom:"+tx[0].getFrom();
          String tt="\nto:"+tx[0].getTo();
          String k="\nkind:"+tx[0].getKind();
          String d="\nData:"+tx[0].getData();
          String a="\namount:"+amount;
          String v="\nvalidate = " + validate+"\n";%>
         <div class="block">
          <p>第<%=block.getIndex() %>個區塊資料:<p><br>
          <p class="font"><%=i %></p><br>
          <p class="font"><%=h %></p><br>
          <p class="font"><%=p %></p><br>
          <p class="font"><%=t %></p><br>
          <p class="font"><%=f %></p><br>
          <p class="font"><%=tt %></p><br>
          <p class="font"><%=k %></p><br>
          <p class="font"><%=d %></p><br>
          <p class="font"><%=a %></p><br>
          <p><%=v %></p><br>
          </div>
          <br>
   <%        
     //   System.out.println(block.toString() + ", validate = " + validate);
       // System.out.println(block.getTransactions().getTxId());
    }
} %>
</body>
</html>