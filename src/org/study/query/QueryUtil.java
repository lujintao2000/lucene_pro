package org.study.query;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class QueryUtil {
	private String filePath = "E:\\lucene\\index2";
	private IndexSearcher searcher = null;
	private static final QueryUtil queryUtil = new QueryUtil();
	
	public static QueryUtil getInstance(){
		return queryUtil;
	}
	
	private QueryUtil(){
		IndexReader indexReader;
		try {
			indexReader = DirectoryReader.open(FSDirectory.open(new File(filePath)));
			searcher = new IndexSearcher(indexReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 打印查询结果
	 * @param query
	 */
	public void displayResult(Query query,int count){
		try {
			TopDocs result = this.searcher.search(query, count);
			for(ScoreDoc item : result.scoreDocs){
				Document document = searcher.doc(item.doc);
				System.out.println(document.toString());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
