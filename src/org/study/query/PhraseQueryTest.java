package org.study.query;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.search.Sort;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class PhraseQueryTest extends TestCase{

	private IndexReader reader;
	private IndexSearcher searcher;
	private String filePath = "E:\\lucene\\index";
	
	protected void setUp() throws Exception{
		Directory dir = FSDirectory.open(new File(filePath));
		IndexWriter writer = new IndexWriter(dir,new IndexWriterConfig(Version.LUCENE_4_9,new WhitespaceAnalyzer(Version.LUCENE_4_9)));
		
		Document doc = new Document();
		IndexableField field = new TextField( "field","quick a fox animal big hunger", Field.Store.YES);
		doc.add(field);
		//writer.addDocument(doc);
		writer.close();
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
	
	private boolean matched(String[] phrase,int slop) throws IOException {
		PhraseQuery query = new PhraseQuery();
		query.setSlop(slop);
		for(String item : phrase){
			query.add(new Term("field",item));
		}

		TopDocs matches = searcher.search(query, 10);
		for(ScoreDoc doc : matches.scoreDocs){
			Document document = searcher.doc(doc.doc);
			System.out.println(document.getField("field").stringValue());
		}
		
		
		return matches.totalHits > 0 ? true : false;
		
	}
	
//	public void testPrefixQuery() throws IOException{
//		Term term = new Term("field","quick");
//		PrefixQuery  query = new PrefixQuery(term);
//		TopDocs matches = searcher.search(query, 10);
//		if(matches != null && matches.scoreDocs.length > 0){
//			for(ScoreDoc item : matches.scoreDocs){
//				Document document = searcher.doc(item.doc);
//				System.out.println(document.getField("field").stringValue());
//			}
//		}
//		Assert.assertTrue(matches.totalHits > 0);
//	}
	
	public  void testSlop() throws Exception{
		String[] phrase = new String[]{"animal","quick"};
		Assert.assertTrue(this.matched(phrase, 5));
	}
	
}
