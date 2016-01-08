package com.djkramnik.nlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader; 
import java.util.*;

public class Driver {
	
	public static String parseMode = ""; 
	public static ArrayList<String> entityAttributes = new ArrayList<String>();
	public static ArrayList<String> eventAttributes = new ArrayList<String>();
	public static ArrayList<DKEntity> dkEntities = new ArrayList<DKEntity>();
	public static ArrayList<DKEvent> dkEvents = new ArrayList<DKEvent>();
	public static ArrayList<DKGroup> dkGroups = new ArrayList<DKGroup>();
	public static ArrayList<DKPhrase> dkPhrases = new ArrayList<DKPhrase>();
	public static DKEntity cachedEntity = null;
	public static Boolean linkingFlag = false;
	public static int linkedIndex = 0;
	public static Boolean andFlag = false;
	public static Boolean havingFlag = false;
	public static Boolean eventFlag = false;
	public static Boolean prepositionFlag = false;
	public static String lastPreposition = "";
	
	public static void main(String[] args) throws Exception {
		/*
		String sourceText = readFile("./sources/hotgirl.txt"), 
				sentence, tag, tag2;
		String [] sentences = ParseUtil.extractSentences(sourceText),
				tokens, twoGrams;
		*/
		
		String sentence, tag, tag2;
		String [] tokens, twoGrams;
				POSTagger tagger = new POSTagger("models/english-bidirectional-distsim.tagger");
		NERTagger classifier = new NERTagger("classifiers/english.all.3class.distsim.crf.ser.gz");	
		
		ArrayList<String> oneGramEntities, twoGramEntities, posTags; 
		ArrayList<DKToken> dkTokens = new ArrayList<DKToken>(); 
		
		//sentence = "A crazy thing happened to your Montreal Canadiens last night and they need some counselling.";
		//sentence = "A crazy thing happened to Obama in downtown Washington last night.";
		//sentence = "Barney is a kindly old man.";
		//sentence = "The hungry spoilt child was a monster and ate the cake with his hands.";
		//sentence = "The man with the golden gun fired at us";
		//sentence = "My girlfriend peacefully slept.";
		//sentence = "She was my sister.";
		//sentence = "A crazy thing happened to me last night and I need to get this off my chest.";
		sentence = "Arash and Poya saw the mysterious light in the sky and ran in terror.";
		
		tokens = ParseUtil.extractTokens(sentence);
		twoGrams = ParseUtil.extract2Grams(sentence);
		oneGramEntities = classifier.getLabels(tokens);
		twoGramEntities = classifier.getLabels(twoGrams);
		posTags = tagger.getTags(sentence);
		
		for(int i=0; i < tokens.length - 1; i++){
			if (!(tag = ParseUtil.extractTag(oneGramEntities.get(i),'/')).equals("O")){
				tag2 = ParseUtil.extractTag(twoGramEntities.get(i),'/'); 
				if(!(tag.equals(tag2)) && !(tag2.equals("O"))){
					dkTokens.add(new DKToken(dkTokens.size(), tokens[i] + " " + tokens[i+1], tag2));
					i = i + 1; 
				} else {
					dkTokens.add(new DKToken(dkTokens.size(), tokens[i], tag));
				}
			} else if (MiscUtil.isNoun(posTags.get(i)) && posTags.get(i).equals(posTags.get(i+1))){
				dkTokens.add(new DKToken(dkTokens.size(), tokens[i] + " " + tokens[i+1], posTags.get(i)));
				i = i + 1;
			}
			else {
				dkTokens.add(new DKToken(dkTokens.size(), tokens[i], posTags.get(i)));
			}	
		}

		//need to solve for the times this is wrong 
		if(!(tag = ParseUtil.extractTag(oneGramEntities.get(tokens.length - 1),'/')).equals("O")){
			dkTokens.add(new DKToken(dkTokens.size(), tokens[tokens.length - 1], tag));
		} else {
			dkTokens.add(new DKToken(dkTokens.size(), tokens[tokens.length - 1], posTags.get(tokens.length - 1)));
		}
		
		for(DKToken t : dkTokens){
			System.out.println(t);
		}
		
		MiscUtil.loadPrepositions();
		slowParse(dkTokens);
		
		System.out.println("\n*********************");
		for(DKEntity e : dkEntities){
			System.out.println(e);
		}
		for(DKEvent e : dkEvents){
			System.out.println(e);
		}
		
	}
	
	public static void slowParse(ArrayList<DKToken> dkTokens){
		//there are 32 POS tags 
		//and essentially for each tag we want a different callback
		//and we want the callback to sometimes vary based on the current parse-mode 
		//so there are many different combinations possible 
		for(DKToken t : dkTokens){
			switch(t.tag){
				case "CC":
					if(t.token.toLowerCase().equals("and")) {
						andFlag = true;
					}
					break;
				case "PRP$":
					havingFlag = true;
					createEntity(t.token);
				case "DT":
					break;
				case "IN":
					if(MiscUtil.isPreposition(t.token)) {
						prepositionFlag = true;
						lastPreposition = t.token;
					}
					break;
				case "PERSON":
				case "ORGANIZATION":
				case "LOCATION":
				case "NN":
				case "NNS":
				case "NNP":
				case "NNPS":
				case "VBG":
				case "PRP":
					if (prepositionFlag) {
						//determine prep attachment at this point using IRL or some hocus pocus 
						//if its a noun attachment basically the object here is an adjectival modifier 
						//if it is verb attachment take the question it is answering (where/why/when/how) and together with the object attach to the last event 
						//if neither a last noun or verb exists we are dealing with a subordinate clause 
						String attachment = MiscUtil.determinePrepAttachment(lastPreposition);
						if (attachment.indexOf("noun") == 0) {
							//noun attachment 
							if(dkEntities.size() > 0){
								dkEntities.get(dkEntities.size() - 1).prepPhrases.add(new DKPrepPhrase(createStandAloneEntity(t.token), lastPreposition)); 	
							} else if (dkEvents.size() > 0) {
								dkEvents.get(dkEvents.size() - 1).prepPhrases.add(new DKPrepPhrase(createStandAloneEntity(t.token), lastPreposition)); 
							}
						} else {
							//verb attachment
							if(dkEvents.size() > 0){
								dkEvents.get(dkEvents.size() - 1).prepPhrases.add(new DKPrepPhrase(createStandAloneEntity(t.token), lastPreposition)); 
							} else if (dkEntities.size() > 0) {
								dkEntities.get(dkEntities.size() - 1).prepPhrases.add(new DKPrepPhrase(createStandAloneEntity(t.token), lastPreposition)); 
							}
						} 						
					} else if (!linkingFlag) {					
						
						if (andFlag) {
							andFlag = false;
							if (!dkEntities.isEmpty()) {
								dkEntities.get(dkEntities.size() - 1).members.add(createStandAloneEntity(t.token));
							} else {
								createEntity(t.token);
							}
						} else {
							createEntity(t.token);	
						}
						//cachedEntity = dkEntities.get(dkEntities.size() - 1);
						if (eventFlag) {
							eventFlag = false;
							dkEvents.get(dkEvents.size() - 1).actedOn = dkEntities.get(dkEntities.size() - 1); 
						}
					} else {
						linkingFlag = false;
						if (!dkEntities.isEmpty()) {
							
							//should be the entity prior to the linker instead 
							dkEntities.get(linkedIndex).addSynonym(t.token, entityAttributes);
							
							//don't forget to empty cache!! 
							entityAttributes.clear();
						}
					}
					if (havingFlag) {
						havingFlag = false;
						if (dkEntities.size() > 1) {
							dkEntities.get(dkEntities.size() - 1).addBelongs(dkEntities.get(dkEntities.size() - 2));
							dkEntities.get(dkEntities.size() - 2).addBelongs(dkEntities.get(dkEntities.size() - 1));
						}
					}

					break;
				case "CD":
				case "JJ":
				case "JJR":
				case "JJS":
					cacheEntityAttribute(t.token);
					break;
				case "RB":
				case "RBR":
				case "RBS":
					cacheEventAttribute(t.token);
					break;
				case "VB":
				case "VBD":
				case "VBZ":
				case "VBP":
				case "VBN":
					if(MiscUtil.toBe(t.token)){
						linkingFlag = true;
						//cache the index of the linker entity  
						linkedIndex = dkEntities.size() - 1; 
					} else if (MiscUtil.toHave(t.token)){
						havingFlag = true;
					} else {
						if (dkEntities.size() > 0) {
							
							createEvent(t);
							if (andFlag) {
								andFlag = false;
								if (dkEvents.size() > 1) {
									dkEvents.get(dkEvents.size() - 1).actor = dkEvents.get(dkEvents.size() - 2).actor;
								}
							}
							eventFlag = true;
							
						}
						if(linkingFlag){
							linkingFlag = false;
							//add the cached linker to auxiliaries 
						}
					}
					break;
				default:
					break;
			}
		}
	}
	
	public static DKEntity createStandAloneEntity (String entity){
		String[] attributes = new String[entityAttributes.size()];
		entityAttributes.toArray(attributes);
		entityAttributes.clear();
		return new DKEntity(entity, attributes);
	}
	
	public static void createEntity(String entity){
		String[] attributes = new String[entityAttributes.size()];
		entityAttributes.toArray(attributes);
		dkEntities.add(new DKEntity(entity, attributes));
		entityAttributes.clear();
	}
	
	public static void createEvent (DKToken t) {
		String [] adverbs = new String[eventAttributes.size()];
		eventAttributes.toArray(adverbs);
		DKEvent dkEvent = new DKEvent(t, dkEntities.get(dkEntities.size() - 1), adverbs);
		dkEvents.add(dkEvent);	
		eventAttributes.clear();
	}
	
	public static void cacheEntityAttribute(String attribute){
		entityAttributes.add(attribute);
	}
	public static void cacheEventAttribute(String attribute){
		eventAttributes.add(attribute);
	}
	public static String readFile(String filePath) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		StringBuilder sb; 
		String line; 
		
		try {
		    sb = new StringBuilder();
		    line = br.readLine();

		    while (line != null) {
		        sb.append(line.replace("...", ""));
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		} finally {
		    br.close();
		}
		return sb.toString(); 
	}
	
}
