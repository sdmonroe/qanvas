/*
 * @(#)SentenceTokenzier.java
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
 * Class <code>SentenceTokenizer</code> generalizes StringTokenizer by remembering the 
 * remaining tokens in the string after each call to <code>nextToken()</code>. 
 * The call remainder() retrievs the remaining tokens as a string.
 * 
 * @author  Sherman Monroe
 * @version 1.0, 04/26/2002
 * @since  PreAlpha- November 2000
 */

public class SentenceTokenizer extends StringTokenizer implements java.io.Serializable {

	protected String r; // remainder

	protected String w; // current word

	protected String delims;

	public String remainder() {return r;}

	public String currToken() {return w;}


	public SentenceTokenizer(String s) {
		this(s, " ");
	}

	public SentenceTokenizer(String s, String d) {
		super(s, d);
		delims = d;
		r = s;
	}
	
	/**
	 * Returns a <code>String[]</code> containing the remaining tokens. Caution: this method iterates through the tokens, causing
	 * the <code>countToken()</code> method to return 0.
	 * 
	 * @return	an array containing the remaining tokens
	 */
	public String[] toArray(){
//		Vector v = new Vector();
		String[] a = new String[countTokens()];
		for(int i = 0; i < a.length; i++) a[i] = nextToken();
//		try{
//			return (String[]) v.toArray(new String[0]);
//		}
//		catch(ArrayStoreException asex){
//			asex.printStackTrace();
//		}
//		catch(NullPointerException npex){
//			npex.printStackTrace();
//		}
		return a;
	}

	public synchronized String nextToken() {
		if (hasMoreTokens()) {
			w = super.nextToken();
			r = (w.length() < r.length()) ? r.substring(w.length()+1, r.length()) : "";
			for (int i = 0; i < delims.length(); i++){
				while (r.length() > 0 && r.charAt(0) == delims.charAt(i)) r = r.substring(1, r.length());
			}
			return w;
		}
		else return null;
	}

} // class SentenceTokenzier