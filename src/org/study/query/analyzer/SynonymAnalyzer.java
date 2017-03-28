package org.study.query.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.Version;

public class SynonymAnalyzer extends Analyzer{
	private  Version version = Version.LUCENE_4_9;
	
	public SynonymAnalyzer(Version version){
		this.version = version;
	}
	
	@Override
	protected TokenStreamComponents createComponents(String s, Reader reader) {
		 org.apache.lucene.analysis.Tokenizer source = new StandardTokenizer(version, reader);
	     TokenStream tok = new StandardFilter(version, source);
	     tok = new LowerCaseFilter(version, tok);
		 return new TokenStreamComponents(source,new SynonymFilter(tok));
	}
	
	private class SynonymFilter extends TokenFilter{
		private Stack<String> stack = new Stack();
		private CharTermAttribute termAttr;
		private PositionIncrementAttribute positionIncrAttr;
		//存放同义词数据
		private Map<String,List<String>> synonymMap = new HashMap<String,List<String>>();
		private AttributeSource.State current;
		
		public SynonymFilter(TokenStream input){
			super(input);
			this.initSynonym();
			this.termAttr = this.addAttribute(CharTermAttribute.class);
			this.positionIncrAttr = this.addAttribute(PositionIncrementAttribute.class);
		}
		
		private void initSynonym(){
			List<String> list = new ArrayList<String>();
			list.add("fast");
			
			this.synonymMap.put("quick", list);
			List<String> list2 = new ArrayList<String>();
			list2.add("zoo");
			this.synonymMap.put("animal", list2);
		}
		
		@Override
		public boolean incrementToken() throws IOException {
			if(this.stack.size() > 0){
				this.restoreState(current);
				String synonym = this.stack.pop();
				//清空
				this.termAttr.setEmpty();
				this.termAttr.append(synonym);
				this.positionIncrAttr.setPositionIncrement(0);
				return true;
			}
			if(this.input.incrementToken()){
				if(this.addSynonymToStack(this.termAttr.toString())){
					current = this.captureState();
				}
				return true;
			}else{
				return false;
			}
		}
		
		/**
		 * 获取一个词语的所有同义词，如果存在，就将它们压入栈中
		 * @param word 要检索的词语
		 * @return  匹配的同义词的个数
		 */
		private boolean addSynonymToStack(String word){
			List<String> synonymList = this.getSynonyms(word.toLowerCase());
			if(synonymList.size() > 0){
				for(String item : synonymList){
					stack.push(item);
				}
			}
			
			return synonymList.size() > 0;
		}
		
		/**
		 * 获取一个词语所有的同义词
		 * @param word
		 * @return 匹配的同义词列表;如果没有匹配的同义词，就返回空集合
		 */
		private List<String> getSynonyms(String word){
			List<String> list = new ArrayList<String>();
			if(this.synonymMap.containsKey(word)){
				list = this.synonymMap.get(word);
			}
			return list;
		}
	}
}
