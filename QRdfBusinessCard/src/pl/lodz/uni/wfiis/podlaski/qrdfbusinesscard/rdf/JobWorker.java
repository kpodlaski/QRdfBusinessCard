package pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.rdf;

/**
 * Interface of job worker that can return a result
 * @author KPodlaski
 *
 */
public interface JobWorker {
	/**
	 * 
	 * @return result of JobWorker
	 */
	Object getJobResult();
}
