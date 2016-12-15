package ca.uhnresearch.pughlab.authentication;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class AdministrationAuthenticationProvider implements AuthenticationProvider {
	
	public AdministrationAuthenticationProvider(String username, String hash) {
		this.username = username;
		this.hash = hash;
	}
	
	private String username;
	private String hash;
 
    @Override
    public Authentication authenticate(Authentication authentication) 
      throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
         
        if (username.equals(name) && BCrypt.checkpw(password, hash)) {
            // use the credentials and authenticate against the third-party system
            return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<GrantedAuthority>());
        } else {
            return null;
        }
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
	public void setUsername(String username) {
		this.username = username;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}