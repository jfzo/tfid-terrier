/**
 * 
 */
package org.terrier.indexing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Document extension that incorporates the degree information.
 * @author jz
 *
 */
public class DegreesDocument implements Document {
	protected static final Logger logger = Logger.getLogger(DegreesDocument.class);
	protected Map<String, String> properties = null;
	protected BufferedReader reader;
	protected int in_degree, out_degree; // current term in and out degree
	private boolean endOfDocument;
	
	public DegreesDocument(BufferedReader _reader) {
		// TODO Auto-generated constructor stub
		properties = new HashMap<String, String>();
		reader = _reader;
		endOfDocument = false;
		//logger.info("Document created!");
	}


	/**
	 * 
	 * @return the in degree of the term in the document net.
	 */
	public int getCurrentTermInDegree(){
		//logger.info("IN_DEGREE:"+in_degree);
		return in_degree;
	}
	
	/**
	 * 
	 * @return the out degree of the term in the document net.
	 */
	public int getCurrentTermOutDegree(){
		//logger.info("OUT_DEGREE:"+out_degree);
		return out_degree;
	}
	
	/**
	 * 
	 * @return the sum of in+out degrees of the term in the document net.
	 */
	public int getCurrentTermDegree() {
		// TODO Auto-generated method stub
		return in_degree + out_degree;
	}

	
	/**
	 * Gets a new line from the input collection.
	 * Return null when an empty line or EOF is found.
	 */
	@Override
	public String getNextTerm() {
		// TODO Auto-generated method stub
		String line = null;
		try {
			line = reader.readLine();
			if(line.length() == 0 || line == null){
					endOfDocument = true;
					return null;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//logger.info("Current line:"+line);
		String[] tinfo = line.split(":"); // term in and out
		in_degree = Integer.parseInt(tinfo[1]);
		out_degree = Integer.parseInt(tinfo[2]);
		
		return tinfo[0];
	}

	
	/**
	 * Returns null because there is no support for fields with
	 * file documents.
	 * @return null.
	 */
	@Override
	public Set<String> getFields() {
		return Collections.emptySet();
	}

	
	/** 
	 * Returns true when the end of the document has been reached, and there
	 * are no other terms to be retrieved from it.
	 * @return boolean true if there are no more terms in the document, otherwise
	 *         it returns false.
     */
	@Override
	public boolean endOfDocument() {
		// TODO Auto-generated method stub
		return endOfDocument;
	}

	/* (non-Javadoc)
	 * @see org.terrier.indexing.Document#getReader()
	 */
	@Override
	public Reader getReader() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.terrier.indexing.Document#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String name) {
		return properties.get(name.toLowerCase());
	}

	/** Allows access to a named property of the Document. Examples might be URL, filename etc. 
	  * @param name Name of the property. It is suggested, but not required that this name
	  * should not be case insensitive.
	  * @since 1.1.0 */
	@Override
	public Map<String, String> getAllProperties() {
		// TODO Auto-generated method stub
		return properties;
	}


}
