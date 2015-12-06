package com.djkramnik.nlp;

public class DKPrepPhrase {
	public DKEntity objectOfPreposition; 
	public String preposition; 
	public DKPrepPhrase(DKEntity o, String p) {
		this.objectOfPreposition = o;
		this.preposition = p;
	}
	public String toString(){
		return preposition + " " + objectOfPreposition.toString();
	}
}
