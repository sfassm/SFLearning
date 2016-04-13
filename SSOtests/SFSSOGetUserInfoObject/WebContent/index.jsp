<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Title: SSOGetUserInfo Object Servlet</title>
</head>
<body>
<h1>TEST SSO and GetUserInfo</h1>
<!-- SF: Add your JSP Body for creating HTML output content here. Also,
		add input FORM(s) 
		NOTE: POST action URI is different for local Liberty test server and in Bluemix:
		Local:   <FORM method="POST" action="/SFSSOGetUserInfo/SFSSOGetUserInfoServlet">
		Bluemix: <FORM method="POST" action="/SFSSOTestServlet">
		-->
<h3>Show all information about the linked Bluemix services (VCAP_SERVICES):</h3>
	<!-- local: /SFSSOGetUserInfo/bluemixenv, Bluemix: /bluemixenv -->
	<FORM method="POST" action="/bluemixenv">
		<input type="submit" name="SubmitBtnAction" value="Show VCAP_SERVICE"/>
	</FORM>	
	
	<br>
<div>
<h3>SIGN IN using the configured SSO Service.</h3>
<!-- local: /SFSSOGetUserInfo/login, Bluemix: /login -->
	<FORM method="POST" action="/login">
		As a result, the user information object content returned by the selected Authentication Provider will be displayed. <br>
		<input type="submit" name="SubmitBtnAction" value="Login using BMSSO"/>
	</FORM>	
</div>



</body>
</html>