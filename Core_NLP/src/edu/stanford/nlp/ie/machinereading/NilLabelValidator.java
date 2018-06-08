package edu.stanford.nlp.ie.machinereading;

import edu.stanford.nlp.ie.machinereading.structure.ExtractionObject;

import java.io.Serializable;

public class NilLabelValidator implements Serializable, LabelValidator {

  private static final long serialVersionUID = 1L;

  public boolean validLabel(String label, ExtractionObject object) {
    return true;
  }

  
}
