package org.study.query;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

public class PhraseQueryDemo {

	
	public static void main(String[] args){
		try {
			
			QueryParser queryParser = new QueryParser(Version.LUCENE_4_9,"title",new StandardAnalyzer(Version.LUCENE_4_9));
			//queryParser.setPhraseSlop(5);
			Query query = queryParser.parse("\"This is some Phrase \"~5");
			System.out.println(query.toString("title"));
			queryParser.setDefaultOperator(Operator.AND);
			Query query2 = queryParser.parse("you  me");
			
			System.out.println(query2.toString("title"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
