package com.djkramnik.nlp;

public class DKInfinitivePhrase { 

	public String infinitive; 
	public DKPhrase target;
	public DKInfinitivePhrase(DKPhrase t, String infinitive) {
		this.target = t;
		this.infinitive = infinitive;
	}
	public String toString(){
		return "to " + this.infinitive; 
	}
}
