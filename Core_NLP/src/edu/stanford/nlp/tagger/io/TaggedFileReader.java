package edu.stanford.nlp.tagger.io;

import edu.stanford.nlp.ling.TaggedWord;

import java.util.Iterator;
import java.util.List;

public interface TaggedFileReader extends Iterable<List<TaggedWord>>, Iterator<List<TaggedWord>> {

  String filename();

}
