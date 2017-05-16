package by.bsu.main.project.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.bsu.main.project.entity.SearchResult;
import by.bsu.main.project.service.SearchManager;

public class SimpleSearchManagerImpl implements SearchManager {

	private String currentLine;
	private int currentLineIndex;

	private List<SearchResult> searchResults;

	public SimpleSearchManagerImpl() {
		currentLineIndex = 0;
	}

	@Override
	public List<SearchResult> searchString(String searchingString, byte[] text) {
		searchResults = new ArrayList<SearchResult>();
		Pattern pattern = Pattern.compile(PatternGenerator.generate(divideLineIntoWords(searchingString)),
				Pattern.CASE_INSENSITIVE);
		StringTokenizer stringTokenizer = new StringTokenizer(new String(text), "\n");
		while (stringTokenizer.hasMoreTokens()) {
			currentLine = stringTokenizer.nextToken();
			Matcher matcher = pattern.matcher(currentLine);
			analyse(matcher);
			currentLineIndex++;
		}

		return searchResults;
	}

	private List<String> divideLineIntoWords(String line) {
		List<String> response = new ArrayList<String>();
		if (line != null) {
			StringTokenizer stringTokenizer = new StringTokenizer(line);
			while (stringTokenizer.hasMoreTokens()) {
				response.add(stringTokenizer.nextToken());
			}
		} else {
			System.err.println("Can't divide null line");
		}
		return response;
	}

	private void analyse(Matcher matcher) {
		while (matcher.find()) {
			SearchResult searchResult = new SearchResult();
			searchResult.setLineIndex(currentLineIndex);
			searchResult.setWord(matcher.group());
			searchResult.setLine(currentLine);
			searchResult.setLinePositionIndex(matcher.start());
			searchResults.add(searchResult);
		}
	}
}
