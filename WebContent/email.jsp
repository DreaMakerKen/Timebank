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
      String subject="���ըϥ� Gmail SMTP SSL�o�H";
      String message = "<html><head><title>����</title></head><body>�o�O�@�ʴ��իH�A����Цۦ�R��</body></html>";
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
          email.setFrom("bvcasdzx2001@gmail.com", "�����ȪA����");
          email.setMsg(message);
          email.addTo("bvcasdzx2001@yahoo.com.tw", "�˷R���|��");
          email.send();
          out.println("�l��o�e���\");
      } catch (EmailException e) {

         e.printStackTrace();

      }  

  %>
</body>
</html>