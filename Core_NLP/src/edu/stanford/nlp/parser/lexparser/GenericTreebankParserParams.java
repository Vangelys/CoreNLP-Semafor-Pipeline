package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.trees.*;

import java.util.List;

public class GenericTreebankParserParams extends AbstractTreebankParserParams {

  /**
   * 
   */
  private static final long serialVersionUID = -617650500538652513L;

  protected GenericTreebankParserParams(TreebankLanguagePack tlp) {
    super(tlp);
    // TODO Auto-generated constructor stub
  }

  @Override
  public TreeTransformer collinizer() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public TreeTransformer collinizerEvalb() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void display() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public HeadFinder headFinder() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public HeadFinder typedDependencyHeadFinder() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MemoryTreebank memoryTreebank() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int setOptionFlag(String[] args, int i) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String[] sisterSplitters() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Tree transformTree(Tree t, Tree root) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<? extends HasWord> defaultTestSentence() {
    // TODO Auto-generated method stub
    return null;
  }

  public DiskTreebank diskTreebank() {
    // TODO Auto-generated method stub
    return null;
  }

  public TreeReaderFactory treeReaderFactory() {
    // TODO Auto-generated method stub
    return null;
  }

}
