package org.terrier.matching.models;



/**
 * Implements the weighting model proposed by Rousseau et al. 2012.
 * It is assumed that the term frequency in a document represents the degree
 * of the term in the corresponding document.
 * @author jz
 *
 */
public class TWID extends WeightingModel {
	private static final long serialVersionUID = 1L;
	
	/** The parameter b.*/
	private double b;

	
	/** A default constructor.*/
	public TWID() {
		super();
		b=0.003d;
	}
	
	/**
	 * Returns the name of the model.
	 * @return the name of the model
	 */
	@Override
	public String getInfo() {
		return "TWID";
	}

	/**
	 * Uses TWID to compute a weight for a term in a document.
	 * @param tf The term frequency in the document
	 * @param docLength the document's length
	 * @return the score assigned to a document with the given 
	 *         tf and docLength, and other preset parameters
	 */
	@Override
	public double score(double tf, double docLength) {

		double tw = tf / (1-b+b*docLength/averageDocumentLength);
		double idf = WeightingModelLibrary.log( (numberOfDocuments+1) / documentFrequency );
		return tw*idf;
		
	}

	@Deprecated
	@Override
	public double score(double tf, double docLength, double n_t, double F_t,
			double _keyFrequency) {
		System.err.println("This function is deprecated, do not use it !!");
		return score(tf, docLength);
	}

	/**
	 * Sets the b parameter 
	 * @param _b the b parameter value to use.
	 */
	public void setParameter(double _b) {
	    this.b = _b;
	}


	/**
	 * Returns the b parameter 
	 */
	public double getParameter() {
	    return this.b;
	}
}
