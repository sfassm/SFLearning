<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Title: INDEX.JSP for POST Request to SFSSOTestServlet</title>
</head>
<body>
<h1>Index.jsp invokes POST Request to SFSSOTestServlet and SSO Service information</h1>
<!-- SF: Add your JSP Body for creating HTML output content here. Also,
		add input FORM(s) 
		NOTE: POST action URI is different for local Liberty test server and in Bluemix:
		Local:   <FORM method="POST" action="/SFSSOStandAloneTest/SFSSOTestServlet">
		Bluemix: <FORM method="POST" action="/SFSSOTestServlet">
		-->
<h3>SFSSOTestServlet invocation example</h3>
	<FORM method="POST" action="/SFSSOTestServlet">
		Enter your data for submission here: 
		<input type="text" name="postValueParam" value="defaultValue"/>
		<input type="submit" name="SubmitBtnAction" value="Submit Form"/>
	</FORM>
	<!-- local: /SFSSOStandAloneTest/bluemixenv, Bluemix: /bluemixenv -->
<h3>Retrieve SSO Service Binding information (BluemixEnvConfiguration)</h3>
	<FORM method="POST" action="/bluemixenv">
		Show Bluemix environment settings for this app (SFSSOServiceConfig): 
		<input type="submit" name="SubmitBtnAction" value="Show"/>
	</FORM>	
	
<div>
<!-- local: /SFSSOStandAloneTest/login, Bluemix: /login -->
	<FORM method="POST" action="/login">
		Use SFSSOLoginServlet (redirecting to configured SSO Service): 
		<input type="submit" name="SubmitBtnAction" value="Login"/>
	</FORM>	
</div>



</body>
</html>