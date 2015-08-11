package ca.uhnresearch.pughlab.authentication;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

public class AuthenticationProvider extends LdapAuthenticationProvider {

	public AuthenticationProvider(LdapAuthenticator authenticator) {
		super(authenticator);
	}

	public AuthenticationProvider(LdapAuthenticator authenticator, LdapAuthoritiesPopulator authoritiesPopulator) {
		super(authenticator, authoritiesPopulator);
	}
	
	protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken authentication) {
		return super.doAuthentication(authentication);
	}
}
