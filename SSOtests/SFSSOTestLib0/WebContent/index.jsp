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
		Local:   <FORM method="POST" action="/SFSSOTestLib0/SFSSOTestServlet">
		Bluemix: <FORM method="POST" action="/SFSSOTestServlet">
		-->
<h3>SFSSOTestServlet invocation example</h3>
	<FORM method="POST" action="/SFSSOTestServlet">
		Enter your data for submission here: 
		<input type="text" name="postValueParam" value="defaultValue"/>
		<input type="submit" name="SubmitBtnAction" value="Submit Form"/>
	</FORM>
<h3>Retrieve SSO Service Binding information (BluemixEnvConfiguration)</h3>
	<FORM method="POST" action="/bluemixenv">
		Show Bluemix environment settings for this app: 
		<input type="submit" name="SubmitBtnAction" value="Show"/>
	</FORM>	
	
<h3>Retrieve detailed JAAS Subject information after Authentication</h3>
<div>
Using dumbSubject.jsp:
	Click <a href="dumpSubject.jsp">here</a> for a more elaborate breakdown of the JAAS Subject.
</div>
<div>
	<FORM method="POST" action="/sso">
		Use SFSSOServlet: Show JAAS Subject info from WSSubject of SSO Service): 
		<input type="submit" name="SubmitBtnAction" value="Show"/>
	</FORM>	
</div>



</body>
</html>