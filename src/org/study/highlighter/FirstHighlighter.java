package org.study.highlighter;

import java.io.File;  
import java.io.IOException;  
import java.io.StringReader;  
  
import org.apache.lucene.analysis.TokenStream;  
import org.apache.lucene.analysis.core.SimpleAnalyzer;  
import org.apache.lucene.document.Document;  
import org.apache.lucene.index.DirectoryReader;  
import org.apache.lucene.index.IndexReader;  
import org.apache.lucene.index.Term;  
import org.apache.lucene.queryparser.classic.ParseException;  
import org.apache.lucene.queryparser.classic.QueryParser;  
import org.apache.lucene.search.IndexSearcher;  
import org.apache.lucene.search.Query;  
import org.apache.lucene.search.ScoreDoc;  
import org.apache.lucene.search.TermQuery;  
import org.apache.lucene.search.TopDocs;  
import org.apache.lucene.search.highlight.Highlighter;  
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;  
import org.apache.lucene.search.highlight.QueryScorer;  
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;  
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;  
import org.apache.lucene.search.highlight.TokenSources;  
import org.apache.lucene.store.Directory;  
import org.apache.lucene.store.SimpleFSDirectory;  
import org.apache.lucene.util.Version;  
import org.wltea.analyzer.lucene.IKAnalyzer;

public class FirstHighlighter {
	   // 高亮處理文本（以下内容纯属虚构）  
    private String text = "China has lots of people,most of them are very poor.China is very big.China become strong now,but the poor people is also poor than other controry";  
  
    // 原文高亮  
    public void highlighter() throws IOException, InvalidTokenOffsetsException {  
  
        TermQuery termQuery = new TermQuery(new Term("field", "china"));  
        TokenStream tokenStream = new SimpleAnalyzer(Version.LUCENE_4_9)  
                .tokenStream("field", new StringReader(text));  
  
        QueryScorer queryScorer = new QueryScorer(termQuery);  
        Highlighter highlighter = new Highlighter(queryScorer);  
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(queryScorer));  
        System.out.println(highlighter.getBestFragment(tokenStream, text));  
    }  
  
    // 使用CSS進行高亮顯示處理  
    public void highlighter_CSS(String searchText) throws ParseException,  
            IOException, InvalidTokenOffsetsException {  
  
        // 創建查詢  
        QueryParser queryParser = new QueryParser(Version.LUCENE_4_9, "field",  
                new SimpleAnalyzer(Version.LUCENE_4_9));  
        Query query = queryParser.parse(searchText);  
  
        // 自定义标注高亮文本标签  
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter(  
                "<span style=\"backgroud:red\">", "</span>");  
        // 语汇单元化  
        TokenStream tokenStream = new SimpleAnalyzer(Version.LUCENE_4_9)  
                .tokenStream("field", new StringReader(text));  
  
        // 創建QueryScoer  
        QueryScorer queryScorer = new QueryScorer(query, "field");  
  
        Highlighter highlighter = new Highlighter(htmlFormatter, queryScorer);  
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(queryScorer));  
  
        System.out.println(highlighter.getBestFragments(tokenStream, text, 4,  
                "..."));  
    }  
  
    // 高亮顯示搜索結果  
    public void highlighter_SR(String field, String searchText)  
            throws IOException, ParseException, InvalidTokenOffsetsException {  
  
        //本次示例为了简便直接使用之前实验建立的索引  
        Directory directory = new SimpleFSDirectory(new File("E:\\lucene\\index2"));  
        IndexReader reader = DirectoryReader.open(directory);// 读取目录  
        IndexSearcher searcher = new IndexSearcher(reader);// 初始化查询组件  
        QueryParser parser = new QueryParser(Version.LUCENE_4_9, field,  
                new IKAnalyzer());  
  
        Query query = parser.parse(searchText);  
  
        TopDocs td = searcher.search(query, 10000);// 获取匹配上元素的一个docid  
        ScoreDoc[] sd = td.scoreDocs;// 加载所有的Documnet文档  
  
        System.out.println("本次命中数据:" + sd.length);  
        QueryScorer scorer = new QueryScorer(query, "content");  
  
        Highlighter highlighter = new Highlighter(scorer);  
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));  
  
        for (ScoreDoc scoreDoc : sd) {  
            Document document = searcher.doc(scoreDoc.doc);  
            String content = document.get("content");  
//            TokenStream tokenStream = TokenSources.getAnyTokenStream(  
//                    search.getIndexReader(), scoreDoc.doc, "content", document,  
//                    new IKAnalyzer(true));  
            System.out.println(scoreDoc.score);
            System.out.println(highlighter  
                    .getBestFragment(new IKAnalyzer(),"content", content));  
        }  
    }  
    
    public static void main(String[] args) throws IOException, InvalidTokenOffsetsException, ParseException{
    	FirstHighlighter highlighterTest = new FirstHighlighter();  
//        highlighterTest.highlighter();  
//       
//        highlighterTest.highlighter_CSS("china");  
//        highlighterTest.highlighter_CSS("poor");  
        highlighterTest.highlighter_SR("content", "床前明月光");  
    }
}
