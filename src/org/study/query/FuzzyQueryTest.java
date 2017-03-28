package org.study.query;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import junit.framework.TestCase;

public class FuzzyQueryTest extends TestCase{
	private IndexReader reader;
	private IndexSearcher searcher;
	private String filePath = "E:\\lucene\\index";
	
	protected void setUp() throws Exception{
		Directory dir = FSDirectory.open(new File(filePath));
	    reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);
	}
	
	public void tearDown(){
		try {
			if(reader != null){
				reader.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public  void testFuzzy() throws Exception{
		Query query = new FuzzyQuery(new Term("field","hunry"));
		TopDocs docs = searcher.search(query, 10);
		if(docs.totalHits > 0){
			ScoreDoc scoreDoc = docs.scoreDocs[0];
			Document doc = searcher.doc(scoreDoc.doc);
			String content = doc.get("field");
			System.out.println(content);
		}
		//LowerCaseFilter  t;
		assertTrue("find document",docs.totalHits > 0);
		
	}
	
	
}
