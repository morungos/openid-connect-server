package edu.mit.kit.repository.impl;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.InetOrgPerson;

import ca.uhnresearch.pughlab.authentication.CustomInetOrgPerson;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;

// TODO: Make this class more pluggable and configurable

public class LdapUserInfoRepository implements UserInfoRepository {

	private static final Logger log = LoggerFactory.getLogger(LdapUserInfoRepository.class);

	// lookup result cache, key from username to userinfo
	private LoadingCache<String, UserInfo> cache;

	private CacheLoader<String, UserInfo> cacheLoader = new CacheLoader<String, UserInfo>() {
		@Override
		public UserInfo load(String username) throws Exception {
			
			log.info("Loading user info for {}", username);
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if (principal instanceof CustomInetOrgPerson) {
				CustomInetOrgPerson person = (CustomInetOrgPerson) principal;
				UserInfo ui = new DefaultUserInfo();
				if (person.getUsername().equals(username)) {
					ui.setPreferredUsername(person.getUsername());
					ui.setSub(person.getUsername());
					ui.setEmail(person.getMail());
					ui.setEmailVerified(false);
					ui.setPhoneNumber(person.getTelephoneNumber());
					ui.setName(person.getDisplayName());
					ui.setGivenName(person.getGivenName());
					ui.setFamilyName(person.getSn());
					ui.setMiddleName(person.getInitials());
					return ui;
				}
			}
			throw new IllegalArgumentException("User not found: " + username);			
		}
		
	};
	
	
	public LdapUserInfoRepository() {
		this.cache = CacheBuilder.newBuilder()
					.maximumSize(100)
					.expireAfterAccess(14, TimeUnit.DAYS)
					.build(cacheLoader);
	}
	
	
	@Override
	public UserInfo getBySubject(String sub) {
		// TODO: right now the subject is the username, should probably change
		return getByUsername(sub);
	}

	@Override
	public UserInfo save(UserInfo userInfo) {
		// read-only repository, unimplemented
		return userInfo;
	}

	@Override
	public void remove(UserInfo userInfo) {
		// read-only repository, unimplemented
		
	}

	@Override
	public Collection<? extends UserInfo> getAll() {
		// return a copy of the currently cached users
		return cache.asMap().values();
	}

	@Override
	public UserInfo getByUsername(String username) {
		log.info("getByUsername for {}", username);
		try {
			return cache.get(username);
		} catch (UncheckedExecutionException ue) {
			return null;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

}