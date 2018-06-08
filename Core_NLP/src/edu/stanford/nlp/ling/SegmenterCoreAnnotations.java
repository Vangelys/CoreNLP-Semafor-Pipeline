package edu.stanford.nlp.ling;

import edu.stanford.nlp.util.ErasureUtils;

import java.util.List;

public class SegmenterCoreAnnotations {

  private SegmenterCoreAnnotations() { } // only static members

  public static class CharactersAnnotation implements CoreAnnotation<List<CoreLabel>> {
    @Override
    public Class<List<CoreLabel>> getType() {
      return ErasureUtils.uncheckedCast(List.class);
    }
  }

  public static class XMLCharAnnotation implements CoreAnnotation<String> {
    @Override
    public Class<String> getType() {
      return String.class;
    }
  }

}
