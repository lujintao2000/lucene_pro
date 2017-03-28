package org.study.query;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class QueryTest {
	private IndexReader reader;
	private IndexSearcher searcher;
	private String filePath = "E:\\lucene\\index";
	
	@Before
	public void setUp() throws Exception{
		Directory dir = FSDirectory.open(new File(filePath));
		IndexWriter writer = new IndexWriter(dir,new IndexWriterConfig(Version.LUCENE_4_9,new SimpleAnalyzer(Version.LUCENE_4_9)));
		
		Document doc = new Document();
		//doc.add(new TextField( "content2","hello the club back until it is horizontal", Field.Store.YES));
		//doc.add(new IntField("age",23,Field.Store.YES));
		doc.add(new StringField("company","Wuhan2",Field.Store.YES));
		//doc.add(new TextField("field","Look,there  is a fork",Field.Store.YES));
		writer.addDocument(doc);
		writer.close();
	    reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);
		
		
	}
	
	@After
	public void tearDown(){
		try {
			if(reader != null){
				reader.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean matched(String[] phrase,int slop) throws IOException {
//		PhraseQuery query = new PhraseQuery();
//		query.setSlop(slop);
//		for(String item : phrase){
//			query.add(new Term("field",item));
//		}
		BooleanQuery query = new BooleanQuery();
		Query termQuery  = new TermQuery(new Term("field","quick"));
		query.add(termQuery, BooleanClause.Occur.MUST);
		Query termQuery2  = new TermQuery(new Term("field","fox"));
		
		query.add(termQuery2, BooleanClause.Occur.MUST);
		TopDocs matches = searcher.search(query, 10);
		for(ScoreDoc doc : matches.scoreDocs){
			Document document = searcher.doc(doc.doc);
			System.out.println(document.getField("field").stringValue());
		}
		
		
		return matches.totalHits > 0 ? true : false;
		
	}
	
	//@Test
	public void testPrefixQuery() throws IOException{
		Term term = new Term("field","quick");
		PrefixQuery  query = new PrefixQuery(term);
		TopDocs matches = searcher.search(query, 10);
		if(matches != null && matches.scoreDocs.length > 0){
			for(ScoreDoc item : matches.scoreDocs){
				Document document = searcher.doc(item.doc);
				System.out.println(document.getField("field").stringValue());
			}
		}
		Assert.assertTrue(matches.totalHits > 0);
	}
	
	//@Test
	public void testBooleanQuery() throws Exception{
		Term term = new Term("title","villagers");
		BooleanQuery boolQuery = new BooleanQuery();
		boolQuery.add(new TermQuery(term), BooleanClause.Occur.MUST);
		
		BooleanQuery  secondQuery = new BooleanQuery();
		secondQuery.add(new TermQuery(new Term("desc","Five")),BooleanClause.Occur.MUST);
		secondQuery.add(new TermQuery(new Term("desc","knowledge")),BooleanClause.Occur.MUST_NOT);
		
		boolQuery.add(secondQuery,BooleanClause.Occur.MUST);
		QueryParser queryParser = new QueryParser(Version.LUCENE_4_9,"desc",new WhitespaceAnalyzer(Version.LUCENE_4_9));
		queryParser.setDefaultOperator(QueryParser.Operator.AND);
	
		Query  query = queryParser.parse("Five (NOT knowledge)");
		this.assertCondition(query, null, new String[]{"desc"});
	}
	
	//@Test
	public void testWildcardQuery() throws Exception{
		Query  query = new WildcardQuery(new Term("title","knowl*"));
		
		TopDocs matches = searcher.search(query, 10);
		
		Assert.assertTrue(matches.totalHits >  0);
		
	}
	
	//@Test 
	public void testNumricRangeQuery() throws Exception{
		NumericRangeQuery  rangeQuery = NumericRangeQuery.newIntRange("age", 15,30, true, true) ;
		assertCondition(rangeQuery);

	}
	
	//@Test 
	public void testTermRangeQuery() throws Exception{
//		TermRangeQuery  rangeQuery = new TermRangeQuery("content",new BytesRef("Ab"),new BytesRef("Am"),true,true);
//		assertCondition(rangeQuery);
//		CharTermAttribute  t2;
	}

	@Test 
	public void testMultiPhraseQuery() throws Exception{
		Query query = new MultiPhraseQuery();
		Set<Term> terms = new HashSet<Term>();
		terms.add(new Term("desc",""));
		terms.add(new Term("desc",""));
		query.extractTerms(terms);
		
		this.assertCondition(query, null, new String[]{"company"});
		//assertCondition(query,new Sort(new SortField("desc",SortField.Type.STRING),new SortField("age",SortField.Type.INT,true)),new String[]{"desc","age"});
	}
	
	//@Test 
	public void testTermQuery() throws Exception{
		Query  query = NumericRangeQuery.newIntRange("age", 17, 25, true, true); 
		Map<String,Analyzer> map = new HashMap<String,Analyzer>();
		map.put("company", new KeywordAnalyzer());
		
		
		PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new SimpleAnalyzer(Version.LUCENE_4_9),map);
		
		
		
		QueryParser queryParser = new QueryParser(Version.LUCENE_4_9,"desc",analyzer);
		query = queryParser.parse("company:Wuhan2");
		this.assertCondition(query, null, new String[]{"company"});
		//assertCondition(query,new Sort(new SortField("desc",SortField.Type.STRING),new SortField("age",SortField.Type.INT,true)),new String[]{"desc","age"});
	}
	
	
	
	
	protected void assertCondition(Query query) throws Exception{
		TopDocs matches = searcher.search(query, 50);
		if(matches.totalHits > 0){
			for(ScoreDoc doc : matches.scoreDocs){
				Document document = searcher.doc(doc.doc);
				System.out.println(document.getField("field").stringValue());
			}
		}
		
		Assert.assertTrue(matches.totalHits >  0);
	}
	
	protected void assertCondition(Query query,Sort sort,String[] fields) throws Exception{
		System.out.println(query);
		TopDocs matches = null;
		if(sort == null){
			matches = searcher.search(query, 50);
		}else{
			matches = searcher.search(query, 50,sort);
		}
		
		if(matches.totalHits > 0){
			for(ScoreDoc doc : matches.scoreDocs){
				Document document = searcher.doc(doc.doc);
				if(fields != null && fields.length > 0){
					for(String item : fields){
						System.out.println((document.getField(item) == null)? "" : document.getField(item).stringValue());
					}
					System.out.println("=========================================");
				}
			}
		}
		
		//Assert.assertTrue(matches.totalHits >  0);
	}
	
	public static void main(String[] args) {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
		QueryParser parser = new QueryParser(Version.LUCENE_4_9,"content",analyzer); 
		parser.setDefaultOperator(QueryParser.AND_OPERATOR);
		try {
			Query query = parser.parse("good morning");
			System.out.println(query.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
}
