/*
 * @(#)Compare.java
 *
 * Copyright 2000-2002 Monrai Technologies, Inc. All Rights Reserved.
 * 
 * This software is the proprietary information of Monrai Technologies, Inc.
 * Use is subject to license terms.
 * 
 */

package com.monrai.cypher.util.text;


/**
 * Class <code>Compare</code>
 * 
 * @author Sherman Monroe
 * @version 1.0, 12/30/2002
 * @since Beta- December 2002
 */

public class Compare {

	// Get minimum of three values

	private static int minimum(int a, int b, int c) {
		int mi;

		mi = a;
		if (b < mi) {
			mi = b;
		}
		if (c < mi) {
			mi = c;
		}
		return mi;
	}

	// Compute Levenshtein distance

	/**
	 * Returns the Levenshtein distance of the two strings. The results are stored
	 * in an int[][], where the first index stores the number of substitutions, and the 
	 * second index stores the number of insertions needed to convert the src string into 
	 * the target string.
	 *
	 */

	public static int getEditDistance(String s, String t) {
		int d[][]; // matrix
		int n; // length of s
		int m; // length of t
		int i; // iterates through s
		int j; // iterates through t
		char s_i; // ith character of s
		char t_j; // jth character of t
		int cost; // cost

		// Step 1
		n = s.length();
		m = t.length();
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n+1][m+1];

		// Step 2
		for (i = 0; i <= n; i++) {
			d[i][0] = i;
		}

		for (j = 0; j <= m; j++) {
			d[0][j] = j;
		}

		// Step 3
		for (i = 1; i <= n; i++) {

			s_i = s.charAt (i - 1);

			// Step 4
			for (j = 1; j <= m; j++) {

				t_j = t.charAt (j - 1);

				// Step 5
				if (s_i == t_j) {
					cost = 0;
				}
				else {
					cost = 1;
				}

				// Step 6
				d[i][j] = minimum(d[i-1][j]+1, d[i][j-1]+1, d[i-1][j-1] + cost);

			}
		}

		// Step 7
		return d[n][m];
	}

} // Compare
