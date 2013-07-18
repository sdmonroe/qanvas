/*
 * @(#)TextParser.java
 *
 * Copyright 2000-2002 Monrai Technologies, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Monrai Technologies, Inc.  
 * Use is subject to license terms.
 * 
 */

package com.monrai.cypher.util.text;

/**
 * Class <code>TextParser</code>.
 * 
 * @author  Sherman Monroe
 * @version 1.0, 05/08/2002
 * @since  Beta 1.0
 */


public class TextParser {


	public static int indexOfEndDelimiter(String startDelimit, String[] endDelimits, String context) {
		int index = -1;
		for(int i = 0; i < endDelimits.length; i++){
			index = indexOfEndDelimiter(startDelimit, endDelimits[i], context);
			if(index >= 0) break;
		}
		return index;
	}

	/**
 	 * Returns the index of the end delimiter of the first 
	 * start delimiter in <code>context_field_value</code>. Supports 
	 * nested delimiters (e.g. "This [[represents,illustrates], is] nesting" ).
	 *
	 * @param stag		the start delimiter
	 * @param etag		the end delimiter
	 * @param context_field_value		the text the delimiters appears in
	 * @return			the index of the end delimiter which belongs to this
	 *				start delimiter
	 *
	 */

	// Retrives the index of the corresponding end tag for 'partOfSpeech' in its context_field_value
	// TIMESTAMP: 01.15.2001 16:29 
	public static int indexOfEndDelimiter(String startDelimit, String endDelimit, String context) {
		if(context.indexOf(endDelimit) < 0) return -1;
		int count = -1;
		boolean loop = true;
		String peep ="", scanned = context;
		int speep, start, end, extracted_string_len = 0;
		//System.out.println("TEXTPARSER.INDEX_OF_END_DELIMIT(): context_field_value: " + Substituter.replace("\n", "", context_field_value)+"\nendDelimit: "+endDelimit);
		start = scanned.indexOf(startDelimit);
		if(start >= 0) {
			scanned = scanned.substring(start + startDelimit.length());
			extracted_string_len = start + startDelimit.length();
			while(loop == true){
				loop = false;
				speep = -1;
				end = scanned.indexOf(endDelimit);
				if(end >= 0) {
					count = end + extracted_string_len;
					peep = scanned.substring(0, end);
					speep = peep.indexOf(startDelimit);
					if(speep >= 0){
						String before_end = scanned.substring(0, scanned.indexOf(endDelimit));
						String after_end = scanned.substring(scanned.indexOf(endDelimit) + endDelimit.length());
						String before_last_start = before_end.substring(0, before_end.lastIndexOf(startDelimit));
						scanned = before_last_start + after_end;
						extracted_string_len += (before_end.length() - before_last_start.length()) + endDelimit.length();
						loop = true;
					}
				}
			}
				//System.out.println("scanned: " + Substituter.replace("\n", "", scanned)+"\nendDelimit: "+endDelimit);
		}
			//System.out.println("TEXTPARSER.INDEX_OF_END_DELIMIT(): COUNT: "+count);
		return count;
	}

}// class TextParser
