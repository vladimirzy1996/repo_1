package by.bsu.main.project.service.impl;

import java.util.ArrayList;
import java.util.List;

import by.bsu.main.project.entity.SearchResult;
import by.bsu.main.project.service.SearchManager;
import by.bsu.main.project.service.TextAnalyzer;

public class StemmerPorterImpl implements TextAnalyzer {

	private char[] stemWord;
	private int beginLength, /* offset into stemWord */
			resultLength, /* offset to end of stemmed word */
			j, k;
	private static final int STANDARD_LENGTH = 50;
	private SearchManager searchManager = new SimpleSearchManagerImpl();

	public StemmerPorterImpl() {
		stemWord = new char[STANDARD_LENGTH];
		beginLength = 0;
		resultLength = 0;
	}

	private void clear() {
		stemWord = new char[STANDARD_LENGTH];
		beginLength = 0;
		resultLength = 0;
	}

	public void add(char letter) {
		if (beginLength == stemWord.length) {
			char[] newStemWord = new char[beginLength + STANDARD_LENGTH];
			for (int c = 0; c < beginLength; c++) {
				newStemWord[c] = stemWord[c];
			}
			stemWord = newStemWord;
		}
		stemWord[beginLength++] = letter;
	}

	public void add(char[] word, int wordLength) {
		if (beginLength + wordLength >= stemWord.length) {
			char[] newStemWord = new char[beginLength + wordLength + STANDARD_LENGTH];
			for (int c = 0; c < beginLength; c++) {
				newStemWord[c] = stemWord[c];
			}
			stemWord = newStemWord;
		}
		for (int c = 0; c < wordLength; c++) {
			stemWord[beginLength++] = word[c];
		}
	}

	@Override
	public String toString() {
		return new String(stemWord, 0, resultLength);
	}

	private final boolean isConsonant(int i) {
		switch (stemWord[i]) {
		case 'a':
		case 'e':
		case 'i':
		case 'o':
		case 'u':
			return false;
		case 'y':
			return (i == 0) ? true : !isConsonant(i - 1);
		default:
			return true;
		}
	}

	private final int numberOfInterlaces() {
		int n = 0;
		int i = 0;
		while (true) {
			if (i > j) {
				return n;
			}
			if (!isConsonant(i)) {
				break;
			}
			i++;
		}
		i++;
		while (true) {
			while (true) {
				if (i > j) {
					return n;
				}
				if (isConsonant(i)) {
					break;
				}
				i++;
			}
			i++;
			n++;
			while (true) {
				if (i > j) {
					return n;
				}
				if (!isConsonant(i)) {
					break;
				}
				i++;
			}
			i++;
		}
	}

	private final boolean isContainsVowel() {
		for (int i = 0; i <= j; i++) {
			if (!isConsonant(i)) {
				return true;
			}
		}
		return false;
	}

	private final boolean isContainDoubleConsonant(int j) {
		if (j < 1) {
			return false;
		}
		if (stemWord[j] != stemWord[j - 1]) {
			return false;
		}
		return isConsonant(j);
	}

	private final boolean isConsonantVowelConsonant(int i) {
		if (i < 2 || !isConsonant(i) || isConsonant(i - 1) || !isConsonant(i - 2)) {
			return false;
		}
		{
			int ch = stemWord[i];
			if (ch == 'w' || ch == 'x' || ch == 'y') {
				return false;
			}
		}
		return true;
	}

	private final boolean isEndContain(String s) {
		int l = s.length();
		int o = k - l + 1;
		if (o < 0) {
			return false;
		}
		for (int i = 0; i < l; i++) {
			if (stemWord[o + i] != s.charAt(i)) {
				return false;
			}
		}
		j = k - l;
		return true;
	}

	private final void addToEnd(String s) {
		int l = s.length();
		int o = j + 1;
		for (int i = 0; i < l; i++) {
			stemWord[o + i] = s.charAt(i);
		}
		k = j + l;
	}

	private final void replace(String s) {
		if (numberOfInterlaces() > 0) {
			addToEnd(s);
		}
	}

	private final void step1() {
		if (stemWord[k] == 's') {
			if (isEndContain("sses")) {
				k -= 2;
			} else if (isEndContain("ies")) {
				addToEnd("i");
			} else if (stemWord[k - 1] != 's') {
				k--;
			}
		}
		if (isEndContain("eed")) {
			if (numberOfInterlaces() > 0) {
				k--;
			}
		} else if ((isEndContain("ed") || isEndContain("ing")) && isContainsVowel()) {
			k = j;
			if (isEndContain("at")) {
				addToEnd("ate");
			} else if (isEndContain("bl")) {
				addToEnd("ble");
			} else if (isEndContain("iz")) {
				addToEnd("ize");
			} else if (isContainDoubleConsonant(k)) {
				k--;
				{
					int ch = stemWord[k];
					if (ch == 'l' || ch == 's' || ch == 'z') {
						k++;
					}
				}
			} else if (numberOfInterlaces() == 1 && isConsonantVowelConsonant(k)) {
				addToEnd("e");
			}
		}
	}

	private final void step2() {
		if (isEndContain("y") && isContainsVowel()) {
			stemWord[k] = 'i';
		}
	}

	private final void step3() {
		if (k == 0) {
			return; /* For Bug 1 */
		}
		switch (stemWord[k - 1]) {
		case 'a':
			if (isEndContain("ational")) {
				replace("ate");
				break;
			}
			if (isEndContain("tional")) {
				replace("tion");
				break;
			}
			break;
		case 'c':
			if (isEndContain("enci")) {
				replace("ence");
				break;
			}
			if (isEndContain("anci")) {
				replace("ance");
				break;
			}
			break;
		case 'e':
			if (isEndContain("izer")) {
				replace("ize");
				break;
			}
			break;
		case 'l':
			if (isEndContain("bli")) {
				replace("ble");
				break;
			}
			if (isEndContain("alli")) {
				replace("al");
				break;
			}
			if (isEndContain("entli")) {
				replace("ent");
				break;
			}
			if (isEndContain("eli")) {
				replace("e");
				break;
			}
			if (isEndContain("ousli")) {
				replace("ous");
				break;
			}
			break;
		case 'o':
			if (isEndContain("ization")) {
				replace("ize");
				break;
			}
			if (isEndContain("ation")) {
				replace("ate");
				break;
			}
			if (isEndContain("ator")) {
				replace("ate");
				break;
			}
			break;
		case 's':
			if (isEndContain("alism")) {
				replace("al");
				break;
			}
			if (isEndContain("iveness")) {
				replace("ive");
				break;
			}
			if (isEndContain("fulness")) {
				replace("ful");
				break;
			}
			if (isEndContain("ousness")) {
				replace("ous");
				break;
			}
			break;
		case 't':
			if (isEndContain("aliti")) {
				replace("al");
				break;
			}
			if (isEndContain("iviti")) {
				replace("ive");
				break;
			}
			if (isEndContain("biliti")) {
				replace("ble");
				break;
			}
			break;
		case 'g':
			if (isEndContain("logi")) {
				replace("log");
				break;
			}
		}
	}

	private final void step4() {
		switch (stemWord[k]) {
		case 'e':
			if (isEndContain("icate")) {
				replace("ic");
				break;
			}
			if (isEndContain("ative")) {
				replace("");
				break;
			}
			if (isEndContain("alize")) {
				replace("al");
				break;
			}
			break;
		case 'i':
			if (isEndContain("iciti")) {
				replace("ic");
				break;
			}
			break;
		case 'l':
			if (isEndContain("ical")) {
				replace("ic");
				break;
			}
			if (isEndContain("ful")) {
				replace("");
				break;
			}
			break;
		case 's':
			if (isEndContain("ness")) {
				replace("");
				break;
			}
			break;
		}
	}

	private final void step5() {
		if (k == 0) {
			return; /* for Bug 1 */
		}
		switch (stemWord[k - 1]) {
		case 'a':
			if (isEndContain("al")) {
				break;
			}
			return;
		case 'c':
			if (isEndContain("ance")) {
				break;
			}
			if (isEndContain("ence")) {
				break;
			}
			return;
		case 'e':
			if (isEndContain("er")) {
				break;
			}
			return;
		case 'i':
			if (isEndContain("ic")) {
				break;
			}
			return;
		case 'l':
			if (isEndContain("able")) {
				break;
			}
			if (isEndContain("ible")) {
				break;
			}
			return;
		case 'n':
			if (isEndContain("ant")) {
				break;
			}
			if (isEndContain("ement")) {
				break;
			}
			if (isEndContain("ment")) {
				break;
			}
			/* element etc. not stripped before the numberOfInterlaces */
			if (isEndContain("ent")) {
				break;
			}
			return;
		case 'o':
			if (isEndContain("ion") && j >= 0 && (stemWord[j] == 's' || stemWord[j] == 't')) {
				break;
			}
			/* j >= 0 fixes Bug 2 */
			if (isEndContain("ou")) {
				break;
			}
			return;
		/* takes care of -ous */
		case 's':
			if (isEndContain("ism")) {
				break;
			}
			return;
		case 't':
			if (isEndContain("ate")) {
				break;
			}
			if (isEndContain("iti")) {
				break;
			}
			return;
		case 'u':
			if (isEndContain("ous")) {
				break;
			}
			return;
		case 'v':
			if (isEndContain("ive")) {
				break;
			}
			return;
		case 'z':
			if (isEndContain("ize")) {
				break;
			}
			return;
		default:
			return;
		}
		if (numberOfInterlaces() > 1) {
			k = j;
		}
	}

	private final void step6() {
		j = k;
		if (stemWord[k] == 'e') {
			int a = numberOfInterlaces();
			if (a > 1 || a == 1 && !isConsonantVowelConsonant(k - 1)) {
				k--;
			}
		}
		if (stemWord[k] == 'l' && isContainDoubleConsonant(k) && numberOfInterlaces() > 1) {
			k--;
		}
	}

	private String stem(String searchRequest) {
		add(searchRequest.toCharArray(), searchRequest.length());
		k = beginLength - 1;
		if (k > 1) {
			step1();
			step2();
			step3();
			step4();
			step5();
			step6();
		}
		resultLength = k + 1;
		beginLength = 0;
		String result = toString();
		clear();
		return result;
	}

	@Override
	public List<SearchResult> execute(String searchRequest, byte[] text) {
		List<String> request = RequestMaker.defineStringIntoWords(searchRequest);
		List<String> response = new ArrayList<String>();
		for (int i = 0; i < request.size(); i++) {
			response.add(stem(request.get(i)));
		}
		String result = RequestMaker.concatListOfString(response);
		return searchManager.searchString(result, text);
	}

}
