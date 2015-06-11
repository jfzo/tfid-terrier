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
	
	/** The constant k_1.*/
	private double k_1 = 1.2d;
	

	/** The parameter b.*/
	private double b=0.003d;

	
	/** A default constructor.*/
	public TWID() {
		super();
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
	 * Recall that tf must be decreased in one unit, since because
	 * feasibility issues, this value was increased in the indexing stage.
	 * @param tf The term frequency in the document
	 * @param docLength the document's length
	 * @return the score assigned to a document with the given 
	 *         tf and docLength, and other preset parameters
	 */
	@Override
	public double score(double tf, double docLength) {

		/*
		double tw = (tf - 1) / (1-b+b*docLength/averageDocumentLength);
		double idf = WeightingModelLibrary.log( (numberOfDocuments+1) / documentFrequency+1 );
		return tw*idf;
		*/
		if(tf > 1){
			tf = tf - 1;
			double Robertson_tf = k_1*tf/( tf+k_1*(1-b+b*docLength/averageDocumentLength) );
			double idf = WeightingModelLibrary.log(numberOfDocuments/documentFrequency+1);
			return keyFrequency * Robertson_tf * idf;
		}else{
			return 0.0;
		}
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
