package ca.uhnresearch.pughlab.authentication.repository;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.UserInfoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static org.mitre.util.jpa.JpaUtil.getSingleResult;
import static org.mitre.util.jpa.JpaUtil.saveOrUpdate;

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
public class LdapUserInfoRepository implements UserInfoRepository {

	@PersistenceContext
	private EntityManager manager;
	
	/**
	 * Looks for a UserInfo record, and if found returns it, otherwise a new record is generated and 
	 * saved if the currently authenticated user matches the key and value. 
	 * @param query
	 * @param key
	 * @param value
	 * @return
	 */
	private UserInfo findOrSave(TypedQuery<DefaultUserInfo> query, String key, String value) {
		UserInfo result = getSingleResult(query.getResultList());
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	@Transactional
	public UserInfo getBySubject(String sub) {
		TypedQuery<DefaultUserInfo> query = manager.createNamedQuery("DefaultUserInfo.getBySubject", DefaultUserInfo.class);
		query.setParameter("sub", sub);
		return findOrSave(query, "sub", sub);
	}

	/**
	 * Get a single UserInfo object by its username
	 */
	@Override
	@Transactional
	public UserInfo getByUsername(String username) {
		TypedQuery<DefaultUserInfo> query = manager.createNamedQuery("DefaultUserInfo.getByUsername", DefaultUserInfo.class);
		query.setParameter("username", username);
		return findOrSave(query, "username", username);
	}

	@Override
	@Transactional
	public UserInfo save(UserInfo userInfo) {
		DefaultUserInfo dui = (DefaultUserInfo)userInfo;
		return saveOrUpdate(dui.getId(), manager, dui);
	}

	@Override
	@Transactional
	public void remove(UserInfo userInfo) {
		DefaultUserInfo dui = (DefaultUserInfo)userInfo;
		UserInfo found = manager.find(DefaultUserInfo.class, dui.getId());

		if (found != null) {
			manager.remove(userInfo);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	@Transactional
	public Collection<DefaultUserInfo> getAll() {

		TypedQuery<DefaultUserInfo> query = manager.createNamedQuery("DefaultUserInfo.getAll", DefaultUserInfo.class);

		return query.getResultList();
	}

}