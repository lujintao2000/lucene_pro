package org.study.query;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.study.query.analyzer.SynonymAnalyzer;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IndexCreator {
	private String filePath = "E:\\lucene\\index2";
	private IndexWriter writer = null;
	
	public IndexCreator(){
		this(new WhitespaceAnalyzer(Version.LUCENE_4_9));
	}
	
	public IndexCreator(Analyzer analyzer){
		Directory dir;
		try {
			dir = FSDirectory.open(new File(filePath));
		    writer = new IndexWriter(dir,new IndexWriterConfig(Version.LUCENE_4_9,analyzer));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void addDocument(Document doc) throws Exception{

		writer.addDocument(doc);
	}
	
	public void close(){
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		IndexCreator creator = new IndexCreator(new  IKAnalyzer());
		try {
			Document doc = new Document();
			IndexableField field = new TextField( "content","这次中央全面深化改革领导小组会议，就有关主要负责同志亲力亲为抓改革落实听取了汇报，汇报的人员既有全面深化改革专项小组组长、重大专项改革领导小组组长，也有地区和部门主要负责同志.床前明月光，疑是地上霜。举头望明月，低头思故乡。", Field.Store.YES);
			doc.add(field);
			creator.addDocument(doc);
			
	        //创建分词对象  
//	        Analyzer analyzer = new SynonymAnalyzer();       
//	        StringReader reader = new StringReader("Nanjing Animal");  
//	        //分词  
//	        TokenStream tokenStream = analyzer.tokenStream("description", reader);  
//	        CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);  
//	        PositionIncrementAttribute positionAttribute = tokenStream.getAttribute(PositionIncrementAttribute.class); 
//	        tokenStream.reset();
//	        //遍历分词数据  
//	        while(tokenStream.incrementToken()){  
//	            System.out.print(termAttribute.toString() + "_" + positionAttribute.getPositionIncrement() +"|");  
//	        }  
//			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			creator.close();
		}
//		QueryParser queryParser = new QueryParser(Version.LUCENE_4_9,"description",new StandardAnalyzer(Version.LUCENE_4_9));
//		try {
//			System.out.println(queryParser.parse("Nanjing zoo"));
//			QueryUtil.getInstance().displayResult(queryParser.parse("nanjing AND zoo"), 10);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
	}
}
