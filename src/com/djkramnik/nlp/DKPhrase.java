package com.djkramnik.nlp;
import java.util.*;

public class DKPhrase {
	public String type;
	public ArrayList<DKPhrase> children = new ArrayList<DKPhrase>();
	public ArrayList<DKToken> tokens = new ArrayList<DKToken>();
	
	public DKPhrase (String type, DKToken token) {
		this(type, token, null);
	}
	
	public DKPhrase(String type, DKToken token, ArrayList<DKPhrase> children) {
		this.type = type;
		this.tokens.add(token);
		if (children != null) {
			this.children.addAll(children);
		}
	}
	
	public void addToPhrase(DKToken t) {
		this.tokens.add(t);
	}
	
	public String toString() {
		return this.tokens.get(0).token + ":" + this.type + "_" + this.tokens.size();
	}
} 
