<!-- SF: IMport the Java Security and WebSphere Security libs: -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    session="true"
    import="java.util.Set"
    import="java.security.Principal"
	import="javax.security.auth.Subject"
	import="com.ibm.websphere.security.auth.WSSubject"
%>
<!-- SF: Example of how to use the WAS Liberty WSSubject object: -->
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-control", "no-store");
	
	Subject s = WSSubject.getCallerSubject();
	String username="unknown";
	if (s != null) {
		Set<Principal> principals = s.getPrincipals();
		if (principals != null && principals.size() > 0) {
			// in production this should be html encoded for safety
			username = principals.iterator().next().getName();
		}
	}
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JAAS Subject Retrieval</title>
<H2>Example of how to retrieve JAAS Subject information using a JSP</H2>
</head>
<body>
NOTE: The "username" is extracted from the first available Principal in the JAAS Subject, 
which is retrieved using the WSSubject class from WebSphere.

Hello: <%=username%>
<br />
Click <a href="dumpSubject.jsp">here</a> for a more elaborate breakdown of the JAAS Subject.
</html>
</body>