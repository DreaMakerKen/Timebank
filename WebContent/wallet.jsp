<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="BIG5">
<title>Insert title here</title>
</head>
<body>
<%@ page import="test.*" %>
<%@ page import="TCHAIN.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*,java.net.*"%>
<% String str=BlockchainTest.createWallet(); 
   StringTokenizer token=new StringTokenizer(str);   
    String address=token.nextToken();
    String privatekey=token.nextToken();
   
%>
<p>�A�����]�a�}�� <%=address %></p><br>
<p>�A���p�_�� <%=privatekey %></p>
<% 
FileWriter fw = new FileWriter("D:\\123\\Tbank2\\key.txt");
fw.write("�a�}:"+address+"\r\n"+"�p�_:"+privatekey+"\r\n");
fw.close();
String path = "D:/123/Tbank2/"; //�ɮץD�n��m�ؿ�
String filename = "key.txt"; //�ɮצW
//���U���A D:/123.xls ���ɮסC
filename = new String(filename.getBytes("ISO-8859-1"),"Big5");

File file = new File(path+filename);
if(file.exists()){//�����ɮ׬O�_�s�b
try{
response.setHeader("Content-Disposition","attachment; filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\""); 
OutputStream output = response.getOutputStream();
InputStream in = new FileInputStream(file);
byte[] b = new byte[2048];
int len;

while((len = in.read(b))>0){
output.write(b,0,len);
}
in.close();
output.flush();
output.close(); //������y
out.clear();
out = pageContext.pushBody();
}catch(Exception ex){
out.println("Exception : "+ex.toString());
out.println("<br/>");
}
}else{
out.println(filename+" : ���ɮפ��s�b");
out.println("<br/>");
}
%>
</body>
</html>