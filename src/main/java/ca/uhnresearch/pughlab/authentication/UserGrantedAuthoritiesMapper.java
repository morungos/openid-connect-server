package ca.uhnresearch.pughlab.authentication;

import java.util.Collection;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;

public interface UserGrantedAuthoritiesMapper {

	Collection<? extends GrantedAuthority> mapUserAuthorities(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities);
}
