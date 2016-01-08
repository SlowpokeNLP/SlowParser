package com.djkramnik.nlp;

import java.util.*; 

public class DKEvent {
	
	public ArrayList<DKToken> auxiliaries = new ArrayList<DKToken>();
	public ArrayList<DKPrepPhrase> prepPhrases = new ArrayList<DKPrepPhrase>();
	public ArrayList<String> attributes = new ArrayList<String>();
	public DKToken token = null;
	public String identifier = null; 
	public DKEntity actor = null; 
	public DKEntity actedOn = null;
	
	
	public DKEvent (DKToken token, DKEntity actor) {
		this.token = token;
		this.actor = actor;
		this.identifier = token.token;
	}
	public DKEvent (DKToken token, DKEntity actor, String [] attributes) {
		this(token,actor);
		if(attributes != null){
			this.attributes.addAll(Arrays.asList(attributes));
		}
	}
	public DKEvent cheapCopy (DKToken token) {
		return new DKEvent(token, this.actor);
	}
	
	public String toString(){
		String s = actor.identifier + " " + attributes.toString() + " " + identifier;
		DKPrepPhrase p;
		if(actedOn != null){
			s += " " + actedOn.identifier;
		}
		if(prepPhrases.size() > 0){
			p = prepPhrases.get(0);
			s += " " + MiscUtil.prepositions.get(p.preposition) + "? " + p.toString();
		}
		return s;
	}
}
