package pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.b2bo.vocabulary;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;

/**
 * Interface that has set of all Properties of Entity elemeny of b2boo onthology
 * @author KPodlaski
 *
 */
public interface Entity {
	String uri = "http://purl.org/b2bo#";
	Property ACCOUNT_NUMBER = new PropertyImpl(uri,"accountNumber");
	Property FLAT_NUMBER = new PropertyImpl(uri,"flatNumber");
	Property HOUSE_NUMBER = new PropertyImpl(uri,"houseNumber");
	Property LOCALITY = new PropertyImpl(uri,"locality");
	Property POST_OFFICE = new PropertyImpl(uri,"postOffice");
	Property POSTAL_CODE = new PropertyImpl(uri,"postalCode");
	Property STREET = new PropertyImpl(uri,"street");
	Property BANK_NAME = new PropertyImpl(uri,"bankName");
	Property MAILTO = new PropertyImpl(uri,"mailto");
	Property NAME = new PropertyImpl(uri,"name");
	Property NIP = new PropertyImpl(uri,"nip");
	Property REGON = new PropertyImpl(uri,"regon");
	Property ADDRESS = new PropertyImpl(uri,"address");
}
