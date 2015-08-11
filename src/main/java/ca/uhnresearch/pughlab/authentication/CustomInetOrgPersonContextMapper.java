package ca.uhnresearch.pughlab.authentication;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

public class CustomInetOrgPersonContextMapper implements UserDetailsContextMapper {
	
	private static final GrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
	
	private static final GrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
	
	Set<String> adminUsers;
	
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
		Set<GrantedAuthority> augmented = new HashSet<GrantedAuthority>();
		augmented.addAll(authorities);
		if (adminUsers != null && adminUsers.contains(username)) {
			augmented.add(ROLE_ADMIN);
		}
		augmented.add(ROLE_USER);
		
		CustomInetOrgPerson.Essence p = new CustomInetOrgPerson.Essence(ctx);
		p.setUsername(username);
		p.setAuthorities(augmented);
		
		return p.createUserDetails();
	}

	/**
	 * @return the adminUsers
	 */
	public Set<String> getAdminUsers() {
		return adminUsers;
	}


	/**
	 * @param adminUsers the adminUsers to set
	 */
	public void setAdminUsers(Set<String> adminUsers) {
		this.adminUsers = adminUsers;
	}
	
	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
		CustomInetOrgPerson p = (CustomInetOrgPerson) user;
		p.populateContext(ctx);
	}
}
