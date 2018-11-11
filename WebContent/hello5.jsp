<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>接收方確認服務</title>
</head>
<body>
  <%@ page import="java.util.List" %>
<%@ page import="test.*" %>
<%@ page import="TCHAIN.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
   <%@ page import="org.apache.commons.mail.DefaultAuthenticator" %>
		   <%@ page import="org.apache.commons.mail.Email" %>
		   <%@ page import="org.apache.commons.mail.EmailException" %>
		   <%@ page import="org.apache.commons.mail.HtmlEmail" %>
<% FileReader fr = new FileReader("D:\\123\\Tbank2\\transacation.txt");
   BufferedReader br = new BufferedReader(fr);

   String from = request.getParameter("from_name");
		String to=request.getParameter("to_name");
		int  amount=Integer.parseInt(request.getParameter("amount"));
		String kind=request.getParameter("kind");
		String email=request.getParameter("email");
   		//String data=request.getParameter("user_data2");
   		String data=new String(request.getParameter("user_data2").getBytes("ISO-8859-1"),"UTF-8"); 
   String line=null;
   String line2=null;
   FileWriter fw = new FileWriter("D:\\123\\Tbank2\\transacation2.txt");
	
   while((line=br.readLine())!=null)
   {
	   StringTokenizer token=new StringTokenizer(line);
	       String from_1=token.nextToken();
	       String to_1=token.nextToken();
	       String amount_1=token.nextToken();
	       String kind_1=token.nextToken();
	       String email_1=token.nextToken();
	   //System.out.println(from+" "+to+" "+amount+" "+kind+" "+data);
	   String s=from_1+" "+to_1+" "+amount_1+" "+kind_1;
	   System.out.println(s.equals(from+" "+to+" "+amount+" "+kind));
	   if(s.equals(from+" "+to+" "+amount+" "+kind))
	   {
		   System.out.println("確認交易訊息正確");
		   
		   String subject="測試使用 Gmail SMTP SSL發信";
		      String message = "<html><head><title>時間銀行交易確認信</title></head><body>確認交易取消</body></html>";
		      Email email2 = new HtmlEmail();
		      String authuser = "bvcasdzx2001@gmail.com";
		      String authpwd = "a3243080953";
		      email2.setHostName("smtp.gmail.com");
		      email2.setSmtpPort(465);
		      email2.setAuthenticator(new DefaultAuthenticator(authuser, authpwd));
		      email2.setDebug(true);
		      email2.setSSL(true);
		      email2.setSslSmtpPort("465");
		      email2.setCharset("UTF-8");
		      email2.setSubject(subject);
		      try {
		          email2.setFrom("bvcasdzx2001@gmail.com", "時間銀行中心");
		          email2.setMsg(message);
		          email2.addTo(email, "親愛的會員");
		          email2.addTo(email_1, "親愛的會員");
		          email2.send();
		          out.println("郵件發送成功");
		      } catch (EmailException e) {

		         e.printStackTrace();

		      }  
	  	
		   
	   }
	   else
	   {
		   System.out.println("不存在此筆交易");
		   fw.write(line+"\r\n");
	   }
   }
   fw.close();
   br.close();
   FileReader fr2 = new FileReader("D:\\123\\Tbank2\\transacation2.txt");
   BufferedReader br2 = new BufferedReader(fr2);
   FileWriter fw2 = new FileWriter("D:\\123\\Tbank2\\transacation.txt");
   while((line=br2.readLine())!=null)
   {
	   fw2.write(line+"\r\n");
   }
   fw2.close();
   br2.close();
  // BlockchainTest.send(from, to, amount, "wash", )
%>

</body>
</html>