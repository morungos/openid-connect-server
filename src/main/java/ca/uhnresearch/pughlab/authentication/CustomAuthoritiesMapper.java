package ca.uhnresearch.pughlab.authentication;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

public class CustomAuthoritiesMapper implements GrantedAuthoritiesMapper {

	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities( Collection<? extends GrantedAuthority> arg0) {
		return arg0;
	}

}
