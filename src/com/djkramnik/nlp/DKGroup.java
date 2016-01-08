package com.djkramnik.nlp;

import java.util.*;
public class DKGroup extends DKEntity {

	public ArrayList<DKEntity> members = new ArrayList<DKEntity>();
	
	public DKGroup(String identifier, String[] attributes) {
		this(identifier, attributes, null);
	}
	
	public DKGroup(String identifier, String[] attributes, DKEntity [] members){
		super(identifier, attributes);
		if(members != null){
			this.members.addAll(Arrays.asList(members));
		}
	}
	

}
