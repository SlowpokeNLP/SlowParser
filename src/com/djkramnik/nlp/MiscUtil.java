package com.djkramnik.nlp;

import java.util.*;

public class MiscUtil {
	
	public static Map<String, String> prepositions = new HashMap<String, String>();
	
	public static ArrayList<String> linkingVerbs = new ArrayList<String>(Arrays.asList(new String []{
		"am", "is", "are", "was", "were", "being", "been"	
	}));
	
	public static ArrayList<String> haveConjugates = new ArrayList<String>(Arrays.asList(new String[]{
		"has", "have", "had", "having"	
	}));
	
	public static ArrayList<String> nounTags = new ArrayList<String>(Arrays.asList(new String[]{
		"nn","nnp","nnps","vbg"
	}));
	
	public static void loadPrepositions(){
		prepositions.put("aboard", "where");
		prepositions.put("about", "where/when");
		prepositions.put("above", "where");
		prepositions.put("abreast", "where");
		prepositions.put("abroad", "where");
		prepositions.put("absent", "where");
		prepositions.put("across", "where");
		prepositions.put("adjacent", "where");
		prepositions.put("along", "where");
		prepositions.put("alongside", "where");
		prepositions.put("amid", "where");
		prepositions.put("among", "where");
		prepositions.put("apropos", "where");
		prepositions.put("around", "where/when");
		prepositions.put("astride", "where");
		prepositions.put("atop", "where");
		prepositions.put("at", "where/when");
		prepositions.put("bar", "what");
		prepositions.put("before", "where/when");
		prepositions.put("behind", "where");
		prepositions.put("below", "where");
		prepositions.put("beneath", "where");
		prepositions.put("beside", "where");
		prepositions.put("besides", "where");
		prepositions.put("between", "where");
		prepositions.put("beyond", "where");
		prepositions.put("but", "what");
		prepositions.put("by", "where/when");
		prepositions.put("circa", "when");
		prepositions.put("come", "where/when");
		prepositions.put("despite", "how");
		prepositions.put("down", "where");
		prepositions.put("during", "when");
		prepositions.put("except", "what");
		prepositions.put("for", "why,when");
		prepositions.put("from", "where/what");
		prepositions.put("in", "where/when");
		prepositions.put("inside", "where/when");
		prepositions.put("into", "where");
		prepositions.put("less", "what");
		prepositions.put("like", "how");
		prepositions.put("minus", "what");
		prepositions.put("near", "where/when");
		prepositions.put("notwithstanding", "how");
		prepositions.put("of", "what");
		prepositions.put("off", "where");
		prepositions.put("on", "where");
		prepositions.put("onto", "where");
		prepositions.put("opposite", "where/how");
		prepositions.put("out", "where");
		prepositions.put("outside", "where");
		prepositions.put("over", "where/how");
		prepositions.put("past", "where/when");
		prepositions.put("post", "when");
		prepositions.put("pro", "what");
		prepositions.put("re", "where");
		prepositions.put("sans", "what");
		prepositions.put("save", "what");
		prepositions.put("short", "what");
		prepositions.put("since", "when");
		prepositions.put("through", "where/how");
		prepositions.put("throughout", "where/how");
		prepositions.put("to", "where/how");
		prepositions.put("toward", "where/direction");
		prepositions.put("under", "where/when");
		prepositions.put("underneath", "where/when");
		prepositions.put("unlike", "how");
		prepositions.put("until", "where/when");
		prepositions.put("up", "where");
		prepositions.put("upon", "where");
		prepositions.put("upside", "where");
		prepositions.put("versus", "what");
		prepositions.put("via", "how");
		prepositions.put("vis-a-vis", "how");
		prepositions.put("with", "how/what");
		prepositions.put("within", "where/when");
		prepositions.put("without", "where/when");
		prepositions.put("worth", "what");
	}
	
	public static HashMap<String, Integer> countPerspective(String taggedSentence){
		HashMap<String, Integer> perspectives = new HashMap<String, Integer>();
		perspectives.put("first",0);
		perspectives.put("second",0);
		perspectives.put("third",0);
		
		String token, tag; 
		
		ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(taggedSentence.split("\\s+")));
		for(String s: tokens){
			token = s.substring(0, s.lastIndexOf('_')).toUpperCase();
			tag = s.substring(s.lastIndexOf('_') + 1);
			if(tag.contains("PRP")){
				switch(token){
					case "I":
					case "ME":
					case "MY":
					case "MINE":
					case "MYSELF":
					case "WE":
					case "US":
					case "OUR":
					case "OURS":
					case "OURSELVES":
						perspectives.put("first", perspectives.get("first").intValue() + 1);
						break;
					case "YOU":
					case "YOUR":
					case "YOURS":
					case "YOURSELF":
					case "YOURSELVES":
						perspectives.put("second", perspectives.get("second").intValue() + 1);
						break;
					case "HE": 
					case "SHE": 
					case "IT":
					case "HIS":
					case "HER":
					case "HERS": 
					case "ITS":
					case "HIMSELF":
					case "HERSELF": 
					case "ITSELF": 
						perspectives.put("third", perspectives.get("third").intValue() + 1);
						break;
				}
			}
		}

		return perspectives; 
	}
	
	public static Boolean toBe(String b){
		return linkingVerbs.contains(b.toLowerCase());
	}
	public static Boolean toHave(String s){
		return haveConjugates.contains(s.toLowerCase());
	}
	public static Boolean isNoun(String s){
		return nounTags.contains(s.toLowerCase());
	}
	public static Boolean isPreposition(String s){
		return prepositions.containsKey(s.toLowerCase());
	}
	//determining the attachment of the prepositional object is an important task 
	//that needs to take into account IRL to do it properly 
	//some prepositions are easier to determine than others though 
	//for the time being I am just looking at the most common role of the preposition as a guide 
	public static String determinePrepAttachment(String preposition){
		String prepType = prepositions.get(preposition.toLowerCase());
		return prepType.indexOf("what") == 0 ? "noun" : "verb";
	}
}
