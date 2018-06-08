package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.ling.HasWord;

import java.util.List;


/** Interface for supporting A* scoring.
 *
 *  @author Dan Klein
 */
public interface Scorer {

  public double oScore(Edge edge);

  public double iScore(Edge edge);

  public boolean oPossible(Hook hook);

  public boolean iPossible(Hook hook);

  public boolean parse(List<? extends HasWord> words);

}
