package com.djkramnik.nlp;

import java.util.*;

public class DKEntity {	
	public String identifier;
	public ArrayList<String> attributes = new ArrayList<String>();
	public ArrayList<DKEntity> belongs = new ArrayList<DKEntity>(); 
	public ArrayList<DKEntity> synonyms = new ArrayList<DKEntity>();
	public ArrayList<DKEntity> members = new ArrayList<DKEntity>();
	public ArrayList<DKPrepPhrase> prepPhrases = new ArrayList<DKPrepPhrase>();
	
	public DKEntity(String identifier, String[] attributes){
		this.identifier = identifier;
		if(attributes != null){
			this.attributes.addAll(Arrays.asList(attributes));
		}
	}
	public void addSynonym(String synonym, ArrayList<String> attributes){
		String [] entityAttributes = new String[attributes.size()];
		attributes.toArray(entityAttributes);
		this.synonyms.add(new DKEntity(synonym, entityAttributes));
	}
	public void addBelongs(DKEntity entity){
		this.belongs.add(entity);
	}
	public String toString(){
		String associates = "";
		for (DKEntity m : members) {
			associates += " and " + m.identifier;
		}
		return identifier + associates;
	}
	public Boolean isGroup (){
		return !this.members.isEmpty();
	}
	
	
}
