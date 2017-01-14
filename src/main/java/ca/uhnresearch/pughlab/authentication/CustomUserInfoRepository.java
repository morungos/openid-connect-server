package ca.uhnresearch.pughlab.authentication;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.UserInfoRepository;
import org.mitre.util.jpa.JpaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * A modified JpaUserInfoRepository which behaves very much as if it is a 
 * persisted cache of user info data. When we look for an item and fail to
 * find it, rather than just failing, we look to see if we are currently
 * authenticated, and if so, we map the LDAP data into a UserInfo object
 * and persist that. This means that on first access we get a new saved
 * user record to back up our tokens. 
 * 
 * @author Stuart Watt
 *
 */
@Repository
public class CustomUserInfoRepository implements UserInfoRepository {

	private static final Logger log = LoggerFactory.getLogger(CustomUserInfoRepository.class);

	private String adminPrincipal = null;
	private String adminMail = null;
	private String adminDisplayName = null;
	private String adminGivenName = null;
	private String adminFamilyName = null;
	
	@PersistenceContext
	private EntityManager manager;
	
	private UserInfo getUserInfoFromPrincipal(String username) throws IllegalArgumentException {
		
		log.info("Loading user info for {}", username);
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof LdapAccountDetails) {
			LdapAccountDetails person = (LdapAccountDetails) principal;
			UserInfo ui = new DefaultUserInfo();
			if (person.getUsername().equals(username)) {
				ui.setPreferredUsername(person.getUsername());
				ui.setSub(person.getUsername());
				
				final String mail = person.getAttributeAsString("mail");
				if (mail != null) {
					ui.setEmail(mail);
					ui.setEmailVerified(false);
				}
				
				final String displayName = person.getAttributeAsString("displayName");
				if (displayName != null) {
					ui.setName(displayName);
				}
				
				final String givenName = person.getAttributeAsString("givenName");
				if (givenName != null) {
					ui.setGivenName(givenName);
				}

				final String familyName = person.getAttributeAsString("sn");
				if (familyName != null) {
					ui.setFamilyName(familyName);
				}

				final String initials = person.getAttributeAsString("initials");
				if (initials != null) {
					ui.setMiddleName(initials);
				}

				return ui;
			}
		} else if (principal instanceof String && ((String)principal).equals(adminPrincipal)) {
			UserInfo ui = new DefaultUserInfo();
			ui.setPreferredUsername(adminPrincipal);
			ui.setSub(adminPrincipal);
			ui.setEmail(adminMail);
			ui.setName(adminDisplayName);
			ui.setGivenName(adminGivenName);
			ui.setFamilyName(adminFamilyName);
			return ui;
		}
		throw new IllegalArgumentException("User not found: " + username);			
	}
	
	/**
	 * Looks for a UserInfo record, and if found returns it, otherwise a new record is generated and 
	 * saved if the currently authenticated user matches the key and value. 
	 * @param query
	 * @param key
	 * @param value
	 * @return
	 */

//	@Override
//	@Transactional
//	public UserInfo getBySubject(String sub) {
//		log.info("getBySubject for {}", sub);
//
//		TypedQuery<DefaultUserInfo> query = manager.createNamedQuery("DefaultUserInfo.getBySubject", DefaultUserInfo.class);
//		query.setParameter("sub", sub);
//		return JpaUtil.getSingleResult(query.getResultList());
//	}

	/**
	 * Get a single UserInfo object by its username
	 */
	@Override
	@Transactional
	public UserInfo getByUsername(String username) {
		log.info("getByUsername for {}", username);
		
		// Logic here is a wee bit harder. If we can find the object, we should return it.
		// If not, we should use getUserInfoFromPrincipal() and persist it. 
		
		UserInfo found = null;
		try {
			TypedQuery<DefaultUserInfo> query = manager.createNamedQuery("DefaultUserInfo.getByUsername", DefaultUserInfo.class);
			query.setParameter("username", username);
			found = JpaUtil.getSingleResult(query.getResultList());
			
			if (found == null) {
				found = getUserInfoFromPrincipal(username);
				DefaultUserInfo dui = (DefaultUserInfo)found;
				JpaUtil.saveOrUpdate(dui.getId(), manager, dui);
			}
			
		} catch (IllegalArgumentException e) {
			// Nothing needed here
		}
		return found;
	}

	@Override
	public UserInfo getByEmailAddress(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAdminPrincipal() {
		return adminPrincipal;
	}

	public void setAdminPrincipal(String adminPrincipal) {
		this.adminPrincipal = adminPrincipal;
	}

	public String getAdminMail() {
		return adminMail;
	}

	public void setAdminMail(String adminMail) {
		this.adminMail = adminMail;
	}

	public String getAdminDisplayName() {
		return adminDisplayName;
	}

	public void setAdminDisplayName(String adminDisplayName) {
		this.adminDisplayName = adminDisplayName;
	}

	public String getAdminGivenName() {
		return adminGivenName;
	}

	public void setAdminGivenName(String adminGivenName) {
		this.adminGivenName = adminGivenName;
	}

	public String getAdminFamilyName() {
		return adminFamilyName;
	}

	public void setAdminFamilyName(String adminFamilyName) {
		this.adminFamilyName = adminFamilyName;
	}

	public EntityManager getManager() {
		return manager;
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

}