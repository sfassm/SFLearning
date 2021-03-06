package com.ibm.labsvcbb.sso.tests.utils;

import java.security.Principal;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import com.ibm.json.java.JSONObject;
import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;
import com.ibm.websphere.security.cred.WSCredential;

public class UserHelper {
	Subject _s;

	public UserHelper() {
		try {
			_s = WSSubject.getCallerSubject();
		} catch (WSSecurityException e) {
			e.printStackTrace();
			_s = null;
		}
	}

	boolean isAuthenticated() {
		boolean result = false;

		/*
		 * if there is a subject and the public credentials of that subject
		 * contains at least one WSCredential that is not unauthenticated, then
		 * return true
		 */
		if (_s != null) {
			Set<WSCredential> publicWSCreds = _s
					.getPublicCredentials(WSCredential.class);
			if (publicWSCreds != null) {
				for (Iterator<WSCredential> i = publicWSCreds.iterator(); !result
						&& i.hasNext();) {
					WSCredential cred = i.next();
					result = !cred.isUnauthenticated();
				}
			}
		}
		return result;
	}

	public String getPrincipalName() {
		String result = null;
		if (_s != null) {
			Set<Principal> principals = _s.getPrincipals();
			if (principals != null && principals.size() > 0) {
				result = principals.iterator().next().getName();
			}
		}
		return result;
	}

	public SimpleJWT getIDToken() {
		SimpleJWT result = null;

		/*
		 * Look for a private credential of type java.util.Hashtable, and then
		 * within there for an entry with name "id_token". This was
		 * reverse-engineered from looking at dump.jsp
		 */
		String idtokenStr = getPrivateHashtableAttr("id_token");
		if (idtokenStr != null) {
			try {
				result = new SimpleJWT(idtokenStr);
			} catch (Exception e) {
				e.printStackTrace();
				result = null;
			}
		}

		return result;
	}

	public String getAccessToken() {
		/*
		 * Look for a private credential of type java.util.Hashtable, and then
		 * within there for an entry with name "access_token". This was
		 * reverse-engineered from looking at dump.jsp
		 */
		String result = getPrivateHashtableAttr("access_token");

		return result;
	}

	protected String getPrivateHashtableAttr(String attrName) {
		String result = null;
		if (_s != null) {
			@SuppressWarnings("rawtypes")
			Set<Hashtable> privateHTs = _s
					.getPrivateCredentials(Hashtable.class);
			if (privateHTs != null && privateHTs.size() > 0) {
				result = (String) privateHTs.iterator().next().get(attrName);
			}
		}
		return result;
	}
	
	protected String getIDTokenClaimsValue(String idTokenClaimsAttrName) {
		String result = null;

			SimpleJWT idtoken = getIDToken();
			if (idtoken != null) {
				JSONObject claims = idtoken.getClaims();
				if (claims != null) {
					result = (String) claims.get(idTokenClaimsAttrName);
				}
			}

		return result;
	}

	public String getEmailAddress() {
		String result = null;

		/*
		 * Where the email address is located is going to depend somehwat on how the user
		 * authenticated. This method tries to be a little clever about figuring
		 * that out.
		 */
		
		// first try emailAddress claim
		result = getIDTokenClaimsValue("emailAddress");
		
		if (result == null) {
			// others to follow?
		}
		
		return result;
	}
	
	public String getDisplayName() {
		/*
		 * Look for an idtoken claim called displayName.
		 * If not found, use principal name 
		 */
		String result = null;
		
		result = getIDTokenClaimsValue("displayName");
		
		if (result == null) {
			result = getPrincipalName();
		}
		
		return result;
	}
}
