package org.study.query;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IkAnalyzerDemo {

	   public static void main(String[] args) throws IOException {  
	        String text="中华人民共和国";  
	        //创建分词对象  
	        Analyzer analyzer = new IKAnalyzer();       
	        StringReader reader = new StringReader(text);  
	        //分词  
	        TokenStream tokenStream = analyzer.tokenStream("content", reader);  
	        CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);  
	        tokenStream.reset();
	        //遍历分词数据  
	        while(tokenStream.incrementToken()){  
	            System.out.print(termAttribute.toString()+"|");  
	        }  
	        tokenStream.close();
	        reader.close();  
//	        System.out.println();  
//		   Dictionary.initial(DefaultConfig.getInstance());
//		   Dictionary dictionary = Dictionary.getSingleton();
////		   
////		   Collection<String> words = new ArrayList<String>();
////		   words.add("民人");
////		   dictionary.addWords(words);
//	        StringReader re = new StringReader("基于java语言开发的轻量级的中文分词工具包,中华人民共和国");
//	        IKSegmenter ik = new IKSegmenter(re,false);
//	        Lexeme lex = null;
//	        while((lex=ik.next())!=null){
//	        	System.out.print(lex.getLexemeText()+"|");
//	        }
	    }  
	
}
