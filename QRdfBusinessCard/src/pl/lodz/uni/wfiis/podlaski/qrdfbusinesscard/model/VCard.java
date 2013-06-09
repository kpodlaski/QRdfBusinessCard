package pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.b2bo.vocabulary.Entity;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Model class of Business card associated with RDF b2boo ontology
 * This Business card describes Company 
 * @author KPodlaski
 *
 */
public class VCard implements Serializable{

	private static final long serialVersionUID = 5391415719677920552L;
	private long id = -1;
	/**
	 * Uri to rdf description file (probably internet address)
	 */
	private String uri;
	private String name;
	private String nip;
	private String regon;
	private VCardAddress address;
	private String bankName;
	private String accountNumber;
	private String mailto;
	/**
	 * true if VCard has its representation in Phone Contacts
	 */
	private boolean inContacts;
	
	public VCard()
	{
		this.address = new VCardAddress();
	}
	
	/**
	 * 
	 * @param properties Map values to be used in creation, map of form propertyName => value of property i.e. name => CompanyName
	 * @throws NoSuchMethodException Thrown if property in the map does not have appropriate setter method
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException Thrown if object property  have different data type than provided one
	 * @throws InvocationTargetException
	 * 
	 * 
	 */
	public VCard(Map<String,Object> properties) throws NoSuchMethodException, IllegalArgumentException
	{
		this.address = new VCardAddress();
		try{
			Method m;
		for(String w : properties.keySet())
		{
			String methName = String.format("set%s%s",w.toUpperCase().charAt(0),w.substring(1));
			m = getClass().getDeclaredMethod(methName, properties.get(w).getClass());
			m.invoke(this, properties.get(w));
		}
		}
		catch (SecurityException e) {
			throw new RuntimeException("Internal Error",e);
		}
		catch (IllegalAccessException e){
			throw new RuntimeException("Internal Error",e);
		}
		catch (InvocationTargetException e){
			throw new RuntimeException("Internal Error",e);
		}
	}
	
	/**
	 * Creates VCard object from RDF jena Resource 
	 *  
	 * @param resource RDF Resource that implement b2boo onthology 
	 */
	public VCard(Resource resource) {
		
		setAccountNumber((String) resource.getProperty(Entity.ACCOUNT_NUMBER).getObject().asLiteral().getValue());
		setBankName(((Resource) resource.getProperty(Entity.BANK_NAME).getObject()).getURI());
		setMailto(((Resource) resource.getProperty(Entity.MAILTO).getObject()).getURI());
		setName((String) resource.getProperty(Entity.NAME).getObject().asLiteral().getValue());
		setNip((String) resource.getProperty(Entity.NIP).getObject().asLiteral().getValue());
		setRegon((String) resource.getProperty(Entity.REGON).getObject().asLiteral().getValue());
		setAddress(new VCardAddress(resource.getPropertyResourceValue(Entity.ADDRESS)));
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNip() {
		return nip;
	}
	public void setNip(String nip) {
		this.nip = nip;
	}
	public String getRegon() {
		return regon;
	}
	public void setRegon(String regon) {
		this.regon = regon;
	}
	public VCardAddress getAddress() {
		return address;
	}
	public void setAddress(VCardAddress address) {
		this.address = address;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getMailto() {
		return mailto;
	}
	public void setMailto(String mailto) {
		this.mailto = mailto;
	}
	public void setMail(String mail)
	{
		this.mailto="mailto:"+mail;
	}
	public String getMail()
	{
		return this.mailto.substring(7);
	}
	public String getLocality() {
		return address.getLocality();
	}
	public void setLocality(String locality) {
		address.setLocality(locality);
	}
	public String getStreet() {
		return address.getStreet();
	}
	public void setStreet(String street) {
		address.setStreet(street);
	}
	public int getHouseNumber() {
		return address.getHouseNumber();
	}
	public void setHouseNumber(Integer houseNumber) {
		address.setHouseNumber(houseNumber.intValue());
	}
	public int getFlatNumber() {
		return address.getFlatNumber();
	}
	public void setFlatNumber(Integer flatNumber) {
		address.setFlatNumber(flatNumber.intValue());
	}
	public String getPostalCode() {
		return address.getPostalCode();
	}
	public void setPostalCode(String postalCode) {
		address.setPostalCode(postalCode);
	}
	public String getPostOffice() {
		return address.getPostOffice();
	}
	public void setPostOffice(String postOffice) {
		address.setPostOffice(postOffice);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isInContacts() {
		return inContacts;
	}

	public void setInContacts(boolean inContacts) {
		this.inContacts = inContacts;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String toString()
	{
		return name;
	}
	
	/**
	 * Internal class that encapsulates address information
	 * @author KPodlaski
	 *
	 */
	class VCardAddress implements Serializable {

		private static final long serialVersionUID = -7409603201499645822L;
		private String locality;
		private String street;
		private int houseNumber;
		private int flatNumber;
		private String postalCode;
		private String postOffice;
		
		public VCardAddress(){}
		
		/**
		 * Creates VCard object from RDF jena Resource 
		 *  
		 * @param resource RDF Resource that implement b2boo onthology 
		 */
		public VCardAddress(Resource resource) {
				setFlatNumber((Integer) resource.getProperty(Entity.FLAT_NUMBER).getObject().asLiteral().getValue());
				setHouseNumber((Integer) resource.getProperty(Entity.HOUSE_NUMBER).getObject().asLiteral().getValue());
				setLocality((String) resource.getProperty(Entity.LOCALITY).getObject().asLiteral().getValue());
				setPostalCode((String) resource.getProperty(Entity.POSTAL_CODE).getObject().asLiteral().getValue());
				setPostOffice((String) resource.getProperty(Entity.POST_OFFICE).getObject().asLiteral().getValue());
				setStreet((String) resource.getProperty(Entity.STREET).getObject().asLiteral().getValue());
		}
		public String getLocality() {
			return locality;
		}
		public void setLocality(String locality) {
			this.locality = locality;
		}
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public int getHouseNumber() {
			return houseNumber;
		}
		public void setHouseNumber(int houseNumber) {
			this.houseNumber = houseNumber;
		}
		public int getFlatNumber() {
			return flatNumber;
		}
		public void setFlatNumber(int flatNumber) {
			this.flatNumber = flatNumber;
		}
		public String getPostalCode() {
			return postalCode;
		}
		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}
		public String getPostOffice() {
			return postOffice;
		}
		public void setPostOffice(String postOffice) {
			this.postOffice = postOffice;
		}
		
		
	}

	
}
