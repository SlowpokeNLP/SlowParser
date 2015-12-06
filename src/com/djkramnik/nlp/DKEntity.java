package com.djkramnik.nlp;

import java.util.*;

public class DKEntity {	
	public String identifier;
	public ArrayList<String> synonyms = new ArrayList<String>();
	public ArrayList<String> attributes = new ArrayList<String>();
	public ArrayList<DKEntity> belongs = new ArrayList<DKEntity>(); 
	public ArrayList<DKPrepPhrase> prepPhrases = new ArrayList<DKPrepPhrase>();
	
	public DKEntity(String identifier, String[] attributes, DKEntity[] belongs){
		this.identifier = identifier;
		if(attributes != null){
			this.attributes.addAll(Arrays.asList(attributes));
		}
		if(belongs != null){
			this.belongs.addAll(Arrays.asList(belongs));
		}
		this.synonyms.add(identifier);
	}
	public DKEntity(String identifier, String[] attributes){
		this(identifier, attributes, null);
	}
	public void addSynonym(String synonym, ArrayList<String> attributes){
		if(attributes != null){
			this.attributes.addAll(attributes);
		} 
		this.synonyms.add(synonym);
	}
	public void addBelongs(DKEntity entity){
		this.belongs.add(entity);
	}
	public String toString(){
		String belongStr = "", 
			s = attributes.toString() + " " + ParseUtil.capitalize(identifier) + " AKA " + synonyms.toString();
		DKPrepPhrase p;
		for(DKEntity entity : belongs){
			belongStr += entity.synonyms.get(0) + "\n";
		}
		if(prepPhrases.size() > 0){
			p = prepPhrases.get(0);
			s += " " + MiscUtil.prepositions.get(p.preposition) + "? " + p.toString();
		}
		return belongStr + s;
	}
	
	
}
