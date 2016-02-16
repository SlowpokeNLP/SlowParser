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
	public void addAttribute (String attribute) {
		attributes.add(attribute);
	}
	public String toString(){
		String associates = "", 
			s = "",
			qualities = "";
		DKPrepPhrase p;
		for (DKEntity m : members) {
			associates += " and " + m.identifier;
		}
		if (prepPhrases.size() > 0) {
			p = prepPhrases.get(0);
			s += " " + MiscUtil.prepositions.get(p.preposition) + "? " + p.toString();
		}
		if (attributes.size() > 0) {
			for (String a : attributes){
				qualities += (a + ", ");
			}
		}
		return qualities + identifier + associates + s;
	}
	public Boolean isGroup (){
		return !this.members.isEmpty();
	}
	
	
}
