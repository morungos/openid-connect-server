package ca.uhnresearch.pughlab.authentication;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SimpleUserGrantedAuthoritiesMapper implements UserGrantedAuthoritiesMapper {

	private static final GrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
	
	private static final GrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");

	Set<String> adminUsers;

	@Override
	public Collection<? extends GrantedAuthority> mapUserAuthorities(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
		Set<GrantedAuthority> augmented = new HashSet<GrantedAuthority>();
		augmented.addAll(authorities);
		if (adminUsers != null && adminUsers.contains(username)) {
			augmented.add(ROLE_ADMIN);
		}
		augmented.add(ROLE_USER);
		return augmented;
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

	/**
	 * Sets the list of admin users as a single string. This is easier for externalized
	 * configuration, as it can be passed from an environment variable. 
	 */
	public void setAdminUserList(String adminUsers) {
		String[] names = adminUsers.trim().split("\\s*#\\s*");
		List<String> users = Arrays.asList(names);
		setAdminUsers(new HashSet<String>(users));
	}
}
