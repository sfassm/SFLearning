<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Title: Simple JSP for POST Request to StartPageServlet</title>
</head>
<body>
<h1>H1: Simple JSP for POST Request to StartPageServlet</h1>
<!-- SF: Add your JSP Body for creating HTML output content here. Also,
		add input FORM(s) 
		NOTE: POST action URI is different for local Liberty test server and in Bluemix:
		Local:   <FORM method="POST" action="/EnvTestProject/StartPageServlet">
		Bluemix: <FORM method="POST" action="/StartPageServlet">
		-->

	<FORM method="POST" action="/EnvTestProject/StartPageServlet">
		Enter your data for submission here: 
		<input type="text" name="postValueParam" value="defaultValue"/>
		<input type="submit" name="SubmitBtnAction" value="Submit Form"/>
	</FORM>
</body>
</html>