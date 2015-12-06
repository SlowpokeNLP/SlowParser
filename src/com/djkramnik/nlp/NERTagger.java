package com.djkramnik.nlp;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.ling.CoreLabel;
import java.util.*;

public class NERTagger {
  
  public AbstractSequenceClassifier<CoreLabel> classifier;
  
  public NERTagger(String serializedClassifier) throws Exception {
	  classifier = CRFClassifier.getClassifier(serializedClassifier);
  }
  
  public String labelEntity(String candidate){
	  return classifier.classifyToString(candidate);
  }
  
  public ArrayList<String> getLabels(String[] ngrams){
	  ArrayList<String> entities = new ArrayList<String>();
	  for(int i=0; i < ngrams.length; i++){
		  entities.add(labelEntity(ngrams[i]));
	  }
	  return entities; 
  }

}
