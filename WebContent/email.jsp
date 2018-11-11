<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<title>Insert title here</title>
</head>
<body>
<%@ page import="org.apache.commons.mail.DefaultAuthenticator" %>
<%@ page import="org.apache.commons.mail.Email" %>
<%@ page import="org.apache.commons.mail.EmailException" %>
<%@ page import="org.apache.commons.mail.HtmlEmail" %>
<%
      String subject="測試使用 Gmail SMTP SSL發信";
      String message = "<html><head><title>測試</title></head><body>這是一封測試信，收到請自行刪除</body></html>";
      Email email = new HtmlEmail();
      String authuser = "bvcasdzx2001@gmail.com";
      String authpwd = "a3243080953";
      email.setHostName("smtp.gmail.com");
      email.setSmtpPort(465);
      email.setAuthenticator(new DefaultAuthenticator(authuser, authpwd));
      email.setDebug(true);
      email.setSSL(true);
      email.setSslSmtpPort("465");
      email.setCharset("UTF-8");
      email.setSubject(subject);
      try {
          email.setFrom("bvcasdzx2001@gmail.com", "網站客服中心");
          email.setMsg(message);
          email.addTo("bvcasdzx2001@yahoo.com.tw", "親愛的會員");
          email.send();
          out.println("郵件發送成功");
      } catch (EmailException e) {

         e.printStackTrace();

      }  

  %>
</body>
</html>