package org.study.query;

import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.Version;

public class AnalyzerDemo {

	private static final String[] examples = {};
	
	
	
	public static void displayTokensWithFullDetails(Analyzer analyzer,String text) throws Exception{
		TokenStream stream = analyzer.tokenStream("content", new StringReader(text));
		CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
		
		PositionIncrementAttribute posIncr = stream.addAttribute(PositionIncrementAttribute.class);
		OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);
		TypeAttribute type = stream.addAttribute(TypeAttribute.class);
		
		int position = 0;
		stream.reset();
		while(stream.incrementToken()){
			int increment = posIncr.getPositionIncrement();
			position += increment;
			System.out.println("position: " + position);
			
			System.out.println(term.toString() + " from-to:" + offset.startOffset() + "-" + offset.endOffset() + "  type:" + type.type());
		}
		
	}
	
	public static void main(String[] args){
		try {
			AnalyzerDemo.displayTokensWithFullDetails(new StandardAnalyzer(Version.LUCENE_31), "I'll e-mail you at xyz@example.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
}
