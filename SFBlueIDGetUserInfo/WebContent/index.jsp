<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Title: IBM BlueID GetUserInfo Object Servlet</title>
</head>
<body>
<h1>TEST BlueID and GetUserInfo</h1>
<!-- SF: Add your JSP Body for creating HTML output content here. Also,
		add input FORM(s) 
		NOTE: POST action URI is different for local Liberty test server and in Bluemix:
		Local:   <FORM method="POST" action="/SFBlueIDGetUserInfo/BlueIDConfigServlet">
		Bluemix: <FORM method="POST" action="/BlueIDConfigServlet">
		-->
<h3>Show all information about the configured Authentication Provider or linked Bluemix services (Configuration File or VCAP_SERVICES):</h3>
	<!-- local: /SFBlueIDGetUserInfo/blueidenv, Bluemix: /blueidenv -->
	<FORM method="POST" action="/SFBlueIDGetUserInfo/blueidenv">
		<input type="submit" name="SubmitBtnAction" value="Show Service Config"/>
	</FORM>	
	
	<br>
<div>
<h3>SIGN IN using the configured IBM BlueID Service.</h3>
<!-- local: /SFBlueIDGetUserInfo/login, Bluemix: /login -->
	<FORM method="POST" action="/SFBlueIDGetUserInfo/login">
		As a result, the user information object content returned by the IBM BlueID Authentication Provider will be displayed. <br>
		<input type="submit" name="SubmitBtnAction" value="Login using IBM BlueID"/>
	</FORM>	
</div>



</body>
</html>