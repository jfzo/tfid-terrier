/**
 * 
 */
package org.terrier.indexing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.terrier.structures.indexing.Indexer;
import org.terrier.structures.indexing.classical.BasicIndexer;
import org.terrier.structures.indexing.classical.BasicIndexerForDegrees;
import org.terrier.utility.ApplicationSetup;

/**
 * Collection that read input data from 
 * the biterm net information instead of a classic document collection.
 * @author jz
 *
 */
public class DegreeByDocCollection implements Collection {
	protected static final Logger logger = Logger.getLogger(DegreeByDocCollection.class);
	protected BufferedReader reader;
	protected boolean endOfCollectionReached;
	
	/** The identifier of a document in the collection.*/
	protected int Docid = 0;

	
	public DegreeByDocCollection(){
		this(ApplicationSetup.getProperty("singlefile.path", "biterm_degrees_bydoc.csv"));
	}
	
	public DegreeByDocCollection(String inputpath) {
		logger.debug("DegreeByDocCollection created.");
		// TODO Auto-generated constructor stub
		try {
			reader = new BufferedReader(new FileReader(inputpath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		endOfCollectionReached = false;
	}

	/**
	 * Closes the reader and sets a flag indicating the end of the collection.
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		reader.close();
		endOfCollectionReached = true;
	}

	/** 
	 * Move the collection to the start of the next document.
	 * @return boolean true if there exists another document in the collection,
	 *         otherwise it returns false. 
	 */
	@Override
	public boolean nextDocument() {
		// TODO Auto-generated method stub
		if( endOfCollectionReached )
			return false;
		
		int line;
		try {
			while( (line = reader.read()) != -1 ){

				if((char)line == '>'){ // file name
					Docid++;
					return true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.debug("No MORE Documents");
		endOfCollectionReached = true;
		return false;
	}

	/** 
	 * Get the document object representing the current document.
	 * @return Document the current document;
	 */
	@Override
	public Document getDocument() {
		// TODO Auto-generated method stub
		StringBuilder docno = new StringBuilder();
		
		char line = 0;
		try {
			while( (line = (char) reader.read()) != -1 ){
				if(line == '\n'){ // file name					
					break;
				}
				docno.append( line );
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(line == -1){ // EOF
			logger.debug("END OF COLLECTION REACHED");
			endOfCollectionReached = true;
		}
		
		logger.debug( "Processing document "+docno.toString() );
		Document doc = new DegreesDocument(reader);
		doc.getAllProperties().put("docno", docno.toString());
		doc.getAllProperties().put("docid", Docid+"");

		return doc;
	}

	/* (non-Javadoc)
	 * @see org.terrier.indexing.Collection#endOfCollection()
	 */
	@Override
	public boolean endOfCollection() {
		// TODO Auto-generated method stub
		return endOfCollectionReached;
	}

	/* (non-Javadoc)
	 * @see org.terrier.indexing.Collection#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		logger.debug(" RESET called !");
		Docid = 0;
	}

}
