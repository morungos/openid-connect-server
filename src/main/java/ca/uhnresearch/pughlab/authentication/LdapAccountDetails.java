package ca.uhnresearch.pughlab.authentication;

/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.ldap.LdapUtils;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

/**
* UserDetails implementation whose properties are based on the LDAP schema for
* <tt>Person</tt>.
*
* @author Luke
* @since 2.0
*/
public class LdapAccountDetails extends LdapUserDetailsImpl {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	
	private BasicAttributes attributes = new BasicAttributes();
	
	protected LdapAccountDetails() {
	}
	
	public String getAttributeAsString(String attrID) {
		Attribute att = attributes.get(attrID);
		if (att == null) {
			return null;
		}
		
		try {
			return att.get().toString();
		} catch (NamingException e) {
			return null;
		}
	}
	
	protected void populateContext(DirContextAdapter adapter) {
		
		Enumeration<? extends Attribute> iterator = attributes.getAll();
		while(iterator.hasMoreElements()) {
			Attribute a = iterator.nextElement();
			adapter.setAttribute(a);
		}
	}

	public static class Essence extends LdapUserDetailsImpl.Essence {
		
		BasicAttributes attributes = new BasicAttributes();

		public Essence() {
		}

		public Essence(DirContextOperations ctx) {
			super(ctx);
			
			Enumeration<? extends Attribute> iterator = ctx.getAttributes().getAll();
			while(iterator.hasMoreElements()) {
				Attribute a = iterator.nextElement();
				attributes.put(a);
			}
		}

		public Essence(LdapAccountDetails copyMe) {
			super(copyMe);
			
			Enumeration<? extends Attribute> iterator = copyMe.attributes.getAll();
			while(iterator.hasMoreElements()) {
				Attribute a = iterator.nextElement();
				setAttribute(a);
			}
		}

		protected LdapUserDetailsImpl createTarget() {
			return new LdapAccountDetails();
		}

		public void setAttribute(Attribute att) {
			((LdapAccountDetails) instance).attributes.put(att);
		}

		public LdapUserDetails createUserDetails() {
			LdapAccountDetails p = (LdapAccountDetails) super.createUserDetails();
			p.attributes = attributes;
			return p;
		}
	}
}