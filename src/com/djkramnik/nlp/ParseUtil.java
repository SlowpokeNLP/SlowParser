package com.djkramnik.nlp;

public class ParseUtil {
	
	public static String[] extractSentences(String s){
		return s.split("(?<=[.?!])\\s+(?=[a-zA-Z])");
	}
	
	public static String[] extractTokens(String sentence){
		return sentence.split("\\s+");
	}
	
	public static String[] extract2Grams(String sentence){
		String [] oneGrams = extractTokens(sentence),
				twoGrams = new String [oneGrams.length - 1];
			
		for(int i = 0; i < oneGrams.length; i++){
			if(i + 1 < oneGrams.length){
				twoGrams[i] = capitalize(oneGrams[i]) + " " + capitalize(oneGrams[i+1]);
			}
		}
		
		return twoGrams; 
	}
	
	public static String capitalize(String s){
		return s.substring(0,1).toUpperCase() + s.substring(1);
	}
	
	public static String extractTag(String s, char delimiter){
		return s.substring(s.lastIndexOf(delimiter) + 1);
	}
	
	public static DKToken parseToken(String s, int index){
		return new DKToken(index, s.substring(0, s.lastIndexOf('_')), s.substring(s.lastIndexOf('_') + 1));		
	}
	
	public static String join (String[] s){
		String str = "";
		if(s.length >= 1){
			str += s[0];
			for(int i = 1; i < s.length; i++){
				str += ("," + s[i]);
			}
		}
		return str;
	}
	
}
