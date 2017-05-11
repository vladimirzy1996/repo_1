package by.bsu.main.project.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import by.bsu.main.project.entity.SearchResult;
import by.bsu.main.project.service.SearchManager;

public class BitapSearchManager implements SearchManager {

	@Override
	public List<SearchResult> searchString(String searchingString, byte[] text) {
		StringTokenizer doc = new StringTokenizer(new String(text), "\n");
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		int i = 0;
		while (doc.hasMoreTokens()) {
			int length = searchingString.length();
			String nextToken = doc.nextToken();
			List<Integer> indexes = find(nextToken, searchingString, length / 3);
			for (Integer index : indexes) {
				if (index >= 0) {
					SearchResult searchResult = new SearchResult();
					searchResult.setWord(getWordInLineByIndex(nextToken, index));
					searchResult.setLine(nextToken);
					searchResult.setLinePositionIndex(getLinePositionIndex(nextToken, index));
					searchResult.setLineIndex(i);
					searchResults.add(searchResult);
				}
				i++;
			}
		}
		return searchResults;
	}

	private String getWordInLineByIndex(String line, int index) {
		StringBuilder strBuild = new StringBuilder();
		int i = index + 1;
		while (!Character.isWhitespace(line.charAt(i))) {
			strBuild.append(line.charAt(i));
			i++;
		}
		return strBuild.toString();
	}

	private int getLinePositionIndex(String line, int index) {
		StringTokenizer strTok = new StringTokenizer(line);
		int length = 0;
		int result = 0;
		while (length < index) {
			if (strTok.hasMoreTokens()) {
				length += strTok.nextToken().length();
				result++;
			}
		}
		return result;

	}
	// http://en.wikipedia.org/wiki/Bitap_algorithm

	/**
	 * Return the list of indexes where the pattern was found.
	 *
	 * The indexes are not exacts because of the addition and deletion : Example
	 * : the text "aa bb cc" with the pattern "bb" and k=1 will match "
	 * b","b","bb","b ". and only the index of the first result " b" is added to
	 * the list even if "bb" have q lower error rate.
	 *
	 * @param doc
	 * @param pattern
	 * @param k
	 * @return
	 */
	public static List<Integer> find(String doc, String pattern, int k) {

		// Range of the alphabet
		// 128 is enough if we stay in the ASCII range (0-127)
		int alphabetRange = 128;
		int firstMatchedText = -1;
		// Indexes where the pattern was found
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		long[] r = new long[k + 1];

		long[] patternMask = new long[alphabetRange];
		for (int i = 0; i <= k; i++) {
			r[i] = 1;
		}

		// Example : The mask for the letter 'e' and the pattern "hello" is
		// 11101 (0 means this letter is at this place in the pattern)
		for (int i = 0; i < pattern.length(); ++i) {
			patternMask[pattern.charAt(i)] |= 1 << i;
		}
		int i = 0;

		while (i < doc.length()) {

			long old = 0;
			long nextOld = 0;

			for (int d = 0; d <= k; ++d) {
				// Three operations of the Levenshtein distance
				long sub = (old | (r[d] & patternMask[doc.charAt(i)])) << 1;
				long ins = old | ((r[d] & patternMask[doc.charAt(i)]) << 1);
				long del = (nextOld | (r[d] & patternMask[doc.charAt(i)])) << 1;
				old = r[d];
				r[d] = sub | ins | del | 1;
				nextOld = r[d];
			}
			// When r[k] is full of zeros, it means we matched the pattern
			// (modulo k errors)
			if (0 < (r[k] & (1 << pattern.length()))) {
				// The pattern "aaa" for the document "bbaaavv" with k=2
				// will slide from "bba","baa","aaa","aav","avv"
				// Because we allow two errors !
				// This test keep only the first one and skip all the others.
				// (We can't skip by increasing i otherwise the r[d] will be
				// wrong)
				if ((firstMatchedText == -1) || (i - firstMatchedText > pattern.length())) {
					firstMatchedText = i;
					indexes.add(firstMatchedText - pattern.length() + 1);
				}
			}
			i++;
		}

		return indexes;
	}
}
