package by.bsu.main.project.service.impl;

import java.util.ArrayList;
import java.util.List;

import by.bsu.main.project.entity.SearchResult;
import by.bsu.main.project.service.SearchManager;
import by.bsu.main.project.service.TextAnalyzer;
import smile.nlp.stemmer.LancasterStemmer;

public class LancasterImpl implements TextAnalyzer {

	private LancasterStemmer lancasterStemmer = new LancasterStemmer();
	private SearchManager searchManager = new SimpleSearchManagerImpl();

	@Override
	public List<SearchResult> execute(String searchRequest, byte[] text) {
		List<String> request = RequestMaker.defineStringIntoWords(searchRequest);
		List<String> response = new ArrayList<String>();
		for (int i = 0; i < request.size(); i++) {
			String stemResult = lancasterStemmer.stem(request.get(i));
			response.add(stemResult);
		}
		String stemResult = RequestMaker.concatListOfString(response);
		if (!stemResult.isEmpty()) {
			return searchManager.searchString(stemResult, text);
		}
		return new ArrayList<SearchResult>();
	}

}
