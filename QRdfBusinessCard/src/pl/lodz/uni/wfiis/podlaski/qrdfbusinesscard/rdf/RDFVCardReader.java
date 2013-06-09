package pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.rdf;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.VCard;

import android.os.AsyncTask;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * Object responsible to read rdf description of b2bo Entity and translate this entity into {@link VCard} object
 * @author KPodlaski
 *
 */
public class RDFVCardReader extends Thread implements JobWorker{

	private final URL cardURL;
	private final ResultsListener client;
	private HashMap<String, Object> wlasnosci = new HashMap<String, Object>();
	private HashSet<String> parsedResources = new HashSet<String>();
	private VCard vCard;
	public boolean finished = false;
	
	/**
	 * 
	 * @param cardURL url to rdf description of b2bo entity
	 * @param client object that waits for result of the Job
	 * @throws MalformedURLException problem with given parameter cardURL
	 */
	public RDFVCardReader(String cardURL,ResultsListener client) throws MalformedURLException {
		super();
		this.cardURL = new URL(cardURL);
		this.client = client;
	}
	
	public void run(){
		try {
			init();
			finished = true;
			client.jobFinished(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Deprecated
	private void initOldVersion() throws IOException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		Model model = ModelFactory.createDefaultModel();
		model.read(cardURL.openStream(),null, "TURTLE");
		Resource sbj = model.getResource("http://example.org/firma");
		parseResource(sbj);
		vCard = new VCard(wlasnosci);
	}
	
	private void init() throws IOException
	{
		Model model = ModelFactory.createDefaultModel();
		model.read(cardURL.openStream(),null, "TURTLE");
		Resource sbj = model.getResource("http://example.org/firma");
		vCard = new VCard(sbj);
		vCard.setUri(cardURL.toString());
	}
	
	/**
	 * 
	 * @return VCard created during Job
	 */
	public VCard getVcard() 
	{
		return vCard;
	}
	
	private void parseResource(Resource res)
	{
		StmtIterator it2 = res.listProperties();
		while (it2.hasNext())
		{
			Statement stm = it2.next();
			if (stm.getObject().isLiteral())
			{
				wlasnosci.put(stm.getPredicate().toString().replaceFirst("\\S+#", ""), stm.getObject().asLiteral().getValue());
			}
			if (stm.getObject().isResource())
			{
				if (parsedResources.contains(stm.getObject().toString()))
					return;
				parsedResources.add(stm.getObject().toString());
				parseResource((Resource) stm.getObject());
			}
		}
	}
	
	
	@Override
	public Object getJobResult() {
			return getVcard();
	}

}
