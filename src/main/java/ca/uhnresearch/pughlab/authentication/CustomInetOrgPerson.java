package ca.uhnresearch.pughlab.authentication;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

public class CustomInetOrgPerson extends CustomPerson {
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private String carLicense;
	// Person.cn
	private String destinationIndicator;
	private String departmentNumber;
	// Person.description
	private String displayName;
	private String employeeNumber;
	private String homePhone;
	private String homePostalAddress;
	private String initials;
	private String mail;
	private String mobile;
	private String o;
	private String ou;
	private String postalAddress;
	private String postalCode;
	private String roomNumber;
	private String street;
	// Person.sn
	// Person.telephoneNumber
	private String title;
	private String uid;

	public String getUid() {
		return uid;
	}

	public String getMail() {
		return mail;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public String getInitials() {
		return initials;
	}

	public String getDestinationIndicator() {
		return destinationIndicator;
	}

	public String getO() {
		return o;
	}

	public String getOu() {
		return ou;
	}

	public String getTitle() {
		return title;
	}

	public String getCarLicense() {
		return carLicense;
	}

	public String getDepartmentNumber() {
		return departmentNumber;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public String getHomePostalAddress() {
		return homePostalAddress;
	}

	public String getMobile() {
		return mobile;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getStreet() {
		return street;
	}

	protected void populateContext(DirContextAdapter adapter) {
		super.populateContext(adapter);
		adapter.setAttributeValue("carLicense", carLicense);
		adapter.setAttributeValue("departmentNumber", departmentNumber);
		adapter.setAttributeValue("destinationIndicator", destinationIndicator);
		adapter.setAttributeValue("displayName", displayName);
		adapter.setAttributeValue("employeeNumber", employeeNumber);
		adapter.setAttributeValue("homePhone", homePhone);
		adapter.setAttributeValue("homePostalAddress", homePostalAddress);
		adapter.setAttributeValue("initials", initials);
		adapter.setAttributeValue("mail", mail);
		adapter.setAttributeValue("mobile", mobile);
		adapter.setAttributeValue("postalAddress", postalAddress);
		adapter.setAttributeValue("postalCode", postalCode);
		adapter.setAttributeValue("ou", ou);
		adapter.setAttributeValue("o", o);
		adapter.setAttributeValue("roomNumber", roomNumber);
		adapter.setAttributeValue("street", street);
		adapter.setAttributeValue("uid", uid);
		adapter.setAttributeValues("objectclass", new String[] { "top", "person",
				"organizationalPerson", "inetOrgPerson" });
	}

	public static class Essence extends CustomPerson.Essence {
		public Essence() {
		}

		public Essence(CustomInetOrgPerson copyMe) {
			super(copyMe);
			setCarLicense(copyMe.getCarLicense());
			setDepartmentNumber(copyMe.getDepartmentNumber());
			setDestinationIndicator(copyMe.getDestinationIndicator());
			setDisplayName(copyMe.getDisplayName());
			setEmployeeNumber(copyMe.getEmployeeNumber());
			setHomePhone(copyMe.getHomePhone());
			setHomePostalAddress(copyMe.getHomePostalAddress());
			setInitials(copyMe.getInitials());
			setMail(copyMe.getMail());
			setMobile(copyMe.getMobile());
			setO(copyMe.getO());
			setOu(copyMe.getOu());
			setPostalAddress(copyMe.getPostalAddress());
			setPostalCode(copyMe.getPostalCode());
			setRoomNumber(copyMe.getRoomNumber());
			setStreet(copyMe.getStreet());
			setTitle(copyMe.getTitle());
			setUid(copyMe.getUid());
		}

		public Essence(DirContextOperations ctx) {
			super(ctx);
			setCarLicense(ctx.getStringAttribute("carLicense"));
			setDepartmentNumber(ctx.getStringAttribute("departmentNumber"));
			setDestinationIndicator(ctx.getStringAttribute("destinationIndicator"));
			setDisplayName(ctx.getStringAttribute("displayName"));
			setEmployeeNumber(ctx.getStringAttribute("employeeNumber"));
			setHomePhone(ctx.getStringAttribute("homePhone"));
			setHomePostalAddress(ctx.getStringAttribute("homePostalAddress"));
			setInitials(ctx.getStringAttribute("initials"));
			setMail(ctx.getStringAttribute("mail"));
			setMobile(ctx.getStringAttribute("mobile"));
			setO(ctx.getStringAttribute("o"));
			setOu(ctx.getStringAttribute("ou"));
			setPostalAddress(ctx.getStringAttribute("postalAddress"));
			setPostalCode(ctx.getStringAttribute("postalCode"));
			setRoomNumber(ctx.getStringAttribute("roomNumber"));
			setStreet(ctx.getStringAttribute("street"));
			setTitle(ctx.getStringAttribute("title"));
			setUid(ctx.getStringAttribute("uid"));
		}

		protected LdapUserDetailsImpl createTarget() {
			return new CustomInetOrgPerson();
		}

		public void setMail(String email) {
			((CustomInetOrgPerson) instance).mail = email;
		}

		public void setUid(String uid) {
			((CustomInetOrgPerson) instance).uid = uid;

			if (instance.getUsername() == null) {
				setUsername(uid);
			}
		}

		public void setInitials(String initials) {
			((CustomInetOrgPerson) instance).initials = initials;
		}

		public void setO(String organization) {
			((CustomInetOrgPerson) instance).o = organization;
		}

		public void setOu(String ou) {
			((CustomInetOrgPerson) instance).ou = ou;
		}

		public void setRoomNumber(String no) {
			((CustomInetOrgPerson) instance).roomNumber = no;
		}

		public void setTitle(String title) {
			((CustomInetOrgPerson) instance).title = title;
		}

		public void setCarLicense(String carLicense) {
			((CustomInetOrgPerson) instance).carLicense = carLicense;
		}

		public void setDepartmentNumber(String departmentNumber) {
			((CustomInetOrgPerson) instance).departmentNumber = departmentNumber;
		}

		public void setDisplayName(String displayName) {
			((CustomInetOrgPerson) instance).displayName = displayName;
		}

		public void setEmployeeNumber(String no) {
			((CustomInetOrgPerson) instance).employeeNumber = no;
		}

		public void setDestinationIndicator(String destination) {
			((CustomInetOrgPerson) instance).destinationIndicator = destination;
		}

		public void setHomePhone(String homePhone) {
			((CustomInetOrgPerson) instance).homePhone = homePhone;
		}

		public void setStreet(String street) {
			((CustomInetOrgPerson) instance).street = street;
		}

		public void setPostalCode(String postalCode) {
			((CustomInetOrgPerson) instance).postalCode = postalCode;
		}

		public void setPostalAddress(String postalAddress) {
			((CustomInetOrgPerson) instance).postalAddress = postalAddress;
		}

		public void setMobile(String mobile) {
			((CustomInetOrgPerson) instance).mobile = mobile;
		}

		public void setHomePostalAddress(String homePostalAddress) {
			((CustomInetOrgPerson) instance).homePostalAddress = homePostalAddress;
		}
	}
}
