package com.djkramnik.nlp;

public class DKToken {
	int index;
	public String token;
	public String tag;
	
	public DKToken(int index, String token, String tag){
		this.index = index;
		this.token = token;
		this.tag = tag;
	}
	
	public String toString(){
		return this.index + ": " + this.token + "_" + this.tag;
	}
}
