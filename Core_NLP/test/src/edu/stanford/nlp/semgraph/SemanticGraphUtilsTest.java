package edu.stanford.nlp.semgraph;

import edu.stanford.nlp.ling.IndexedWord;
import junit.framework.TestCase;

import java.util.function.Function;

/**
 *
 * @author Sonal Gupta
 */
public class SemanticGraphUtilsTest extends TestCase {

  SemanticGraph graph;

  public void testCreateSemgrexPattern(){
    try{
    SemanticGraph graph = SemanticGraph.valueOf("[ate subj>Bill]");

    Function<IndexedWord, String> transformNode = o ->{
      return "{word: " + o.word().toLowerCase() + "; tag: " + o.tag() +"; ner: " + o.ner() + "}";
      };

    String pat = SemanticGraphUtils.semgrexFromGraphOrderedNodes(graph, null, null, transformNode);
    assertEquals("{word: ate; tag: null; ner: null}=ate  >subj=E1 {word: bill; tag: null; ner: null}=Bill", pat.trim());
    }catch(Exception e){
      e.printStackTrace();
    }
  }

}