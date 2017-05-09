package by.bsu.main.project.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import by.bsu.main.project.entity.SearchResult;
import by.bsu.main.project.service.SearchManager;

public class SimpleSearchManagerImpl implements SearchManager {

	private String currentLine;
	private int currentLineIndex;
	private List<String> wordsList;
	private List<SearchResult> searchResults;

	public SimpleSearchManagerImpl() {
		wordsList = new ArrayList<String>();
		currentLineIndex = 0;
	}

	@Override
	public List<SearchResult> searchString(String searchingString, byte[] text) {
		searchResults = new ArrayList<SearchResult>();
		StringTokenizer stringTokenizer = new StringTokenizer(new String(text), "\n");
		while (stringTokenizer.hasMoreTokens()) {
			currentLine = stringTokenizer.nextToken();
			currentLineIndex++;
			divideLineIntoWords(currentLine);
			analyse(searchingString);
			wordsList.clear();
		}

		return searchResults;
	}

	private void divideLineIntoWords(String line) {
		if (line != null) {
			StringTokenizer stringTokenizer = new StringTokenizer(line);
			while (stringTokenizer.hasMoreTokens()) {
				wordsList.add(stringTokenizer.nextToken());
			}
		} else {
			System.err.println("Can't divide null line");
		}
	}

	private void analyse(String searchingString) {
		for (int i = 0; i < wordsList.size(); i++) {
			if (wordsList.get(i).toLowerCase().startsWith(searchingString.toLowerCase())) {
				SearchResult searchResult = new SearchResult();
				searchResult.setWord(wordsList.get(i));
				searchResult.setLineIndex(currentLineIndex);
				searchResult.setLinePositionIndex(i);
				searchResult.setLine(currentLine);
				searchResults.add(searchResult);

			}
		}
	}
}
