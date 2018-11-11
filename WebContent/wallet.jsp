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
<p>你的錢包地址為 <%=address %></p><br>
<p>你的私鑰為 <%=privatekey %></p>
<% 
FileWriter fw = new FileWriter("D:\\123\\Tbank2\\key.txt");
fw.write("地址:"+address+"\r\n"+"私鑰:"+privatekey+"\r\n");
fw.close();
String path = "D:/123/Tbank2/"; //檔案主要放置目錄
String filename = "key.txt"; //檔案名
//欲下載再 D:/123.xls 之檔案。
filename = new String(filename.getBytes("ISO-8859-1"),"Big5");

File file = new File(path+filename);
if(file.exists()){//檢驗檔案是否存在
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
output.close(); //關閉串流
out.clear();
out = pageContext.pushBody();
}catch(Exception ex){
out.println("Exception : "+ex.toString());
out.println("<br/>");
}
}else{
out.println(filename+" : 此檔案不存在");
out.println("<br/>");
}
%>
</body>
</html>