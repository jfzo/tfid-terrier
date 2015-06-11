package cl.tfid.test;

import java.io.IOException;
import java.util.Map;

import org.terrier.structures.BitIndexPointer;
import org.terrier.structures.DocumentIndex;
import org.terrier.structures.Index;
import org.terrier.structures.Lexicon;
import org.terrier.structures.LexiconEntry;
import org.terrier.structures.MetaIndex;
import org.terrier.structures.PostingIndex;
import org.terrier.structures.bit.DirectIndex;
import org.terrier.structures.postings.IterablePosting;

public class IndexExplorer {

	public static void main(String[] args) {
        if (args.length == 0 ){
            System.err.println("Usage: java -cp $CPATH IndexExplorer index_path [term to search]");
            return;
        }
		System.out.println("Opening index...");
		Index index = Index.createIndex(
				args[0], "data");
		PostingIndex<?> inv = index.getInvertedIndex();
		MetaIndex meta = index.getMetaIndex();
		Lexicon<String> lex = index.getLexicon();


        if (args.length > 1) {
			/** print documents containing a specific term. **/
			System.out.println("Searching for word " + args[1]);

			// Getting entry for term
			LexiconEntry le = lex.getLexiconEntry(args[1].toLowerCase());
			if (le != null)
				System.out.println("Term " + args[1] + " occurs in "
						+ le.getDocumentFrequency() + " documents");
			else {
				System.out.println("Term " + args[1] + " does not occur");
				return;
			}

			IterablePosting postings = null;
			try {
				postings = inv.getPostings((BitIndexPointer) le);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				while (postings.next() != IterablePosting.EOL) {
					String docno = meta.getItem("docno", postings.getId());
					System.out.println(docno + " with frequency "
							+ postings.getFrequency());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			/** print all terms in eahc document. **/
			try {
				printAllDocs(index, 10);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * What terms occur in the 'docid'th document?
	 * 
	 * @param docid
	 * @param index
	 * @throws IOException
	 */
	public static void printDocTerms(int docid, Index index) throws IOException {
		PostingIndex<?> di = index.getDirectIndex();
		DocumentIndex doi = index.getDocumentIndex();
		MetaIndex meta = index.getMetaIndex();
		Lexicon<String> lex = index.getLexicon();
		IterablePosting postings = null;

		postings = di
				.getPostings((BitIndexPointer) doi.getDocumentEntry(docid));
		String docno = meta.getItem("docno", docid);
		System.out.println("DOC: " + docno);
				
		while (postings.next() != IterablePosting.EOL) {
			Map.Entry<String, LexiconEntry> lee = lex.getLexiconEntry(postings
					.getId());
			System.out.println(lee.getKey() + " with frequency "
					+ postings.getFrequency() +"(df:"+  lee.getValue().getDocumentFrequency() +")");
		}
	}

	public static void printAllDocs(Index index, int maxdocs) throws IOException {
		DocumentIndex doi = index.getDocumentIndex();

		System.out.println("Nr. of docs " + doi.getNumberOfDocuments());
		for (int i = 0; i < Math.min(maxdocs, doi.getNumberOfDocuments() ); i++) {
			printDocTerms(i, index);
			System.out.println(" ** ");
		}
	}
}
