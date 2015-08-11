package ca.uhnresearch.pughlab.authentication;

import java.util.Collection;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

public class LdapAccountContextMapper implements UserDetailsContextMapper {

	private UserGrantedAuthoritiesMapper userAuthoritiesMapper;

	private Collection<? extends GrantedAuthority> getUserAuthorities(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
		if (userAuthoritiesMapper == null) {
			return authorities;
		} else {
			return userAuthoritiesMapper.mapUserAuthorities(ctx, username, authorities);
		}
	}
	
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
		final Collection<? extends GrantedAuthority> augmented = getUserAuthorities(ctx, username, authorities);
		
		LdapAccountDetails.Essence p = new LdapAccountDetails.Essence(ctx);
		p.setUsername(username);
		p.setAuthorities(augmented);
		
		return p.createUserDetails();
	}

	@Override
	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
		LdapAccountDetails p = (LdapAccountDetails) user;
		p.populateContext(ctx);
	}

	/**
	 * @return the userAuthoritiesMapper
	 */
	public UserGrantedAuthoritiesMapper getUserAuthoritiesMapper() {
		return userAuthoritiesMapper;
	}

	/**
	 * @param userAuthoritiesMapper the userAuthoritiesMapper to set
	 */
	public void setUserAuthoritiesMapper(
			UserGrantedAuthoritiesMapper userAuthoritiesMapper) {
		this.userAuthoritiesMapper = userAuthoritiesMapper;
	}
}
