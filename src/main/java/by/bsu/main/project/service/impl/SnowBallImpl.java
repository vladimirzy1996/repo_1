package by.bsu.main.project.service.impl;

import java.util.ArrayList;
import java.util.List;

import by.bsu.main.project.entity.SearchResult;
import by.bsu.main.project.service.SearchManager;
import by.bsu.main.project.service.TextAnalyzer;
import by.bsu.main.project.service.impl.logic.dependency.EnglishSnowBallStemmer;
import by.bsu.main.project.service.impl.logic.dependency.SnowballStemmer;

public class SnowBallImpl implements TextAnalyzer {
	private SnowballStemmer snowballStemmer = new EnglishSnowBallStemmer();
	private SearchManager searchManager = new BitapSearchManager();

	@Override
	public List<SearchResult> execute(String searchRequest, byte[] text) {
		List<String> request = RequestMaker.defineStringIntoWords(searchRequest);
		List<String> response = new ArrayList<>();
		for (int i = 0; i < request.size(); i++) {
			snowballStemmer.setCurrent(request.get(i));
			snowballStemmer.stem();
			String result = snowballStemmer.getCurrent();
			response.add(result);
		}
		String result = RequestMaker.concatListOfString(response);
		if (!result.isEmpty()) {
			List<SearchResult> searchResult = searchManager.searchString(result, text);
			return searchResult;
		} else {
			return new ArrayList<SearchResult>();
		}
	}
}
