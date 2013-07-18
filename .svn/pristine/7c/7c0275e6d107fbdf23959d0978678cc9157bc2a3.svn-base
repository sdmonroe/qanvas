/*
 * @(#)Substituter.java
 *
 * Copyright 2000-2002 Monrai Technologies, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Monrai Technologies, Inc.  
 * Use is subject to license terms.
 * 
 */

package com.monrai.cypher.util.text;
        
import java.util.StringTokenizer;


/**
 * Class <code>Substituter</code>.
 *
 * 
 * @author  Sherman Monroe
 * @version 1.0, 04/26/2002
 * @since  PreAlpha- November 2000
 */


public class Substituter {

	/**
	 * A straightforward generalization of the <code>String.replace(char)
       * </code> method where the arguments are strings instead of characters.
	 *
	 * @param to		the <code>String</code> to replace
	 * @param from		the value that replaces <code>x</code>
	 * @param context_field_value		the text in which <code>x</code> appears
	 * @return 			returns <code>context_field_value</code> after the replacement
	 *				has been made
	 */

	public static String replace(String from, String to, String context) {
		if (from == null || from.length() <= 0) return context;
		if (to == null || to.length() <= 0) to = "";
		if (context.indexOf(from) < 0) return context;
		if (from.equals(to)) return context;
		return replace(from, to, context, new String[0]);
	}


	/**
	 * A straightforward generalization of the <code>String.replace(char)
       * </code> method where the arguments are strings instead of characters.
	 *
	 * @param to		the <code>String</code> to replace
	 * @param from		the value that replaces <code>x</code>
	 * @param skip		the <code>String</code> to skip over during replacement;
	 *				For the string "THESE OFFICE SUPPLY ARE OFF LIMIT", replace 
	 *				only the token "OFF" with "ON", call <code>replace("OFF",
	 *				"ON", "OFFICE", context_field_value);</code>
	 * @return 			returns <code>context_field_value</code> after the replacement
	 *				has been made
	 */

	public static String replace(String from, String to, String skip, String context) {
		if (from == null || from.length() <= 0) return context;
		if (to == null || to.length() <= 0) to = "";
		if (context.indexOf(from) < 0) return context;
		if (from.equals(to)) return context;
		return replace(from, to, context, new String[]{skip});
	}

	/**
	 * A straightforward generalization of the <code>String.replace(char)
       * </code> method where the arguments are strings instead of characters.
	 * @param skip		contains <code>String</code>s to skip over during replacement;
	 *				For the string "THESE OFFICE SUPPLY ARE OFF LIMIT", replace 
	 *				only the token "OFF" with "ON", call <code>replace("OFF",
	 *				"ON","OFFICE", context_field_value);</code>
	 * @param x			the <code>String</code> to replace
	 * @param y			the value that replaces <code>x</code>
	 * @param context_field_value		the text in which <code>x</code> appears
	 *
	 * @return 			returns <code>context_field_value</code> after the replacement
	 *				has been made
	 */

	public static String replace(String from, String to, String context, String[] skip) {
		if (from == null || from.length() <= 0) return context;
		if (to == null || to.length() <= 0) to = "";		
		if (context.indexOf(from) < 0) return context;
		if (from.equals(to)) return context;
		//System.out.println("SUBSTITUTER.REPLACE() FROM: " + from + " TO: " + to);
		
		
		StringBuffer sb = new StringBuffer(context);
		boolean skipOn = (skip != null && skip.length > 0);
		int index = -1;
		int start = 0;
		try {
			if (from.length() > 0) {
				int len = from.length();
				while((index = sb.indexOf( from, start )) >= 0){
					if (index > sb.length()) {
						//System.out.println("SUBSTITUER.REPLACE(): '" + from + "' '" + to + "' '" + context + "'" + index);
						return (sb.toString());
					}
					String head = sb.substring(0, index);
					String tail = (index + len < sb.length()) ? sb.substring(index + len) : "";

					String mid = to;
					if(skipOn){
						String check;
						for(int i = 0; i < skip.length; i++){
							check = skip[i];
							if(index == sb.indexOf( check, start )){
								mid = from;
								break;
							}
						}
					}

					sb = new StringBuffer(head);
					sb.append(mid);
					sb.append(tail);
					
					start = head.length() + mid.length(); 
				}
			}
		}
		catch (Exception e) {
			System.out.println("SUBSTITUTER.REPLACE() Strings: '" + from + "' '" + to + "' '" + sb.toString() + "'" + index);
			e.printStackTrace();
		}
		return sb.toString();
	}


	// the overloaded replace() method
	// replaces an entire array of substitutions
	// in the rule string

	public static String replace(String[][] subst, String input) {
		input = input.trim();
		input = " "+input+" ";
		for (int i = 0; i < subst.length; i++) input = replace(subst[i][0], subst[i][1], input);
		input = input.trim();
		return input;
	} // replace (2 args)

	public static String capitalizeSentence(String s) {
		SentenceTokenizer st = new SentenceTokenizer(s, " ");
		StringBuffer sb = new StringBuffer();
		while(st.hasMoreTokens()){
			sb.append(capitalize(st.nextToken()));
			if(st.hasMoreTokens()) sb.append(" ");
		}
		return sb.toString();
	}
	public static String capitalize(String s) {
		s = s.trim();
		if (s.length() == 1) s = s.toUpperCase();
		else if (s.length() > 1) {
			s = s.substring(0,1).toUpperCase() +
			s.substring(1, s.length()).toLowerCase();
		}
		return s;
	}

	public static String formal(String s) {
		StringTokenizer st = new StringTokenizer(s, " ");
		String ss = "";  
		try {
			int n = st.countTokens();
			for (int i = 0; i < n; i++) {
				String sent = st.nextToken();
				sent = sent.substring(0,1).toUpperCase() +
				sent.substring(1).toLowerCase();
				ss += sent + " ";
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		ss = ss.trim();
		return ss;
	}


	public static String deformalize(String s){
		SentenceTokenizer st = new SentenceTokenizer(s, " ");
		String words = "";
		String hold = "";
		for(int i = 0; st.countTokens() > 0; i++){
			hold = st.nextToken();
			if(
				hold.equals( hold.toUpperCase() ) ||
				hold.equals( Substituter.capitalize(hold) ) && i == 0
			){
				hold = hold.toLowerCase();
			}
			words += hold;
			if(st.countTokens() > 0) words += " ";
		}
		return words;
	}

	public static String oneSpace(String s){
		while(s.indexOf("  ") >= 0) replace("  "," ",s);
		while(s.indexOf("	") >= 0) replace("	"," ",s);
		return s;
	}
	
	public static String noSpace(String s){
	    while(s != null && s.indexOf(" ") >= 0) s = Substituter.replace(" ", "", s);
	    return s;
	}
	
	
	public static String remove(String token, String s){
	    return Substituter.replace(token, "", s);
	}


	/**
		 * @param sentence
		 * @return
		 */
		public static String removeTabsCharacters(String sentence) {
			sentence = replace("\t", " ", sentence);
	//					token = Substituter.replace("\n", " ", token);
			sentence = replace("\r", " ", sentence);
			sentence = replace("\f", " ", sentence);
			return sentence;
		}


	/**
		 * @param sentence
		 * @return
		 */
		public static String removeTabsAndEOLCharacters(String sentence) {
			sentence = replace("\t", "", sentence);
	//					token = Substituter.replace("\n", " ", token);
			sentence = replace("\n", "", sentence);
			sentence = replace("\r", "", sentence);
			sentence = replace("\f", "", sentence);
			return sentence;
		}


	/**
	 * @param rule
	 * @return
	 */
	public static String formatFileName(String input) {
		if(input.length() > 100) input = input.substring(0, 100) + "_x";
		input = replace(".", "_", input);
		input = replace("?", "_", input);
		input = replace("!", "_", input);
	
		input = replace(",", "_", input);
		input = replace(";", "_", input);
		input = replace(":", "_", input);
		input = replace("\"", "_", input);
		//rule = Substituter.replace("\'", " \' ", contractions, rule);
		input = replace("(", "_", input);
		input = replace(")", "_", input);
		input = replace("{", "_", input);
		input = replace("}", "_", input);
		input = replace("<", "_", input);
		input = replace(">", "_", input);
		input = replace("/", "_", input);
		input = replace("\\", "_", input);
		input = replace("-", "_", input);
		input = replace("%", "_", input);
		input = replace("&", "_", input);
		input = replace("$", "_", input);
		input = replace("#", "_", input);
		input = replace("@", "_", input);
		input = replace("+", "_", input);
		input = replace("*", "_", input);
		return input;
	}

}// class Substituter
