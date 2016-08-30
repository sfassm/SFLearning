<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Title: IBM BlueID and BMSSO GetUserInfo Object Servlet</title>
</head>
<body>
<h1>TEST Auth Service instance and GetUserInfo (BMSSO/BlueID)</h1>
<!-- SF: Add your JSP Body for creating HTML output content here. Also,
		add input FORM(s) 
		NOTE: POST action URI is different for local Liberty test server and in Bluemix:
		Local:   <FORM method="POST" action="/SFBlueIDGetUserInfo/BlueIDConfigServlet">
		Bluemix: <FORM method="POST" action="/BlueIDConfigServlet">
		-->
<h3>Show all information about the configured Authentication Provider or linked Bluemix services (Configuration File or VCAP_SERVICES) or provide your own configuration:</h3>
	<!-- local: /SFBlueIDGetUserInfo/blueidenv, Bluemix: /blueidenv -->
	<FORM method="POST" action="/SFBlueIDGetUserInfo/blueidenv">
		<fieldset>
			<input type="radio" name="postUsedConfig" value="vcapinfo"/>Use VCAP information when Authentication service is bound (else defaults)<br>
			<input type="radio" checked name="postUsedConfig" value="propfile"/>Use configuration in included properties file or provide below<br>
			
			<!--  <b>Enter your configuration data here (otherwise default values from "config.properties" are used):</b><br> -->
			Service Name (appName for BlueID): <input type="text" name="postServiceName" value="default"/> <br>
			Service Label (clientName for BlueID): <input type="text" name="postServiceLabel" value="default"/> <br>
			Client ID: <input type="text" name="postClientId" value="default"/> <br>
			Client Secret: <input type="text" name="postClientSecret" value="default"/> <br>
			Issuer: <input type="text" name="postIssuer" value="default"/> <br>
			Token Endpoint URL: <input type="text" name="postTokenEndpointUrl" value="default"/> <br>
			Auth Endpoint URL: <input type="text" name="postAuthEndpointUrl" value="default"/> <br>
			Auth Redirect URI: <input type="text" name="postAuthRedirectUri" value="default"/> <br>
		</fieldset><br>
		
		<input type="submit" name="SubmitBtnAction" value="Configure Auth Service"/>
	</FORM>	
	
	<br>

<div>
<h3>SIGN IN using the configured Authentication Service.</h3>
<!-- local: /SFBlueIDGetUserInfo/login, Bluemix: /login -->
	<FORM method="POST" action="/SFBlueIDGetUserInfo/login">
		As a result, the user information object content returned by the Authentication Provider will be displayed. <br>
		<input type="submit" name="SubmitBtnAction" value="Login"/>
	</FORM>	
</div>



</body>
</html>