package com.djkramnik.nlp;

import java.util.ArrayList;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagger {
	
	public MaxentTagger tagger;
	public POSTagger(String modelpath){
		tagger = new MaxentTagger(modelpath);
	}
	
	public String tagSentence(String sentence){
		return tagger.tagString(sentence);		
	}
	
	public ArrayList<String> getTags(String sentence){
		return getTags(ParseUtil.extractTokens(this.tagSentence(sentence)));
	}
	
	public ArrayList<String> getTags(String[] tokens){
		ArrayList<String> tags = new ArrayList<String>(); 
		for(int i = 0; i < tokens.length; i++){
			tags.add(ParseUtil.extractTag(tokens[i], '_'));
		}
		return tags;
	}
}
