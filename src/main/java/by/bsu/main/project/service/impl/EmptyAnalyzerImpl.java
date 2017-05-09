package by.bsu.main.project.service.impl;

import java.util.List;

import by.bsu.main.project.entity.SearchResult;
import by.bsu.main.project.service.TextAnalyzer;

public class EmptyAnalyzerImpl implements TextAnalyzer {

	@Override
	public List<SearchResult> execute(String searchRequest, byte[] text) {
		// TODO Auto-generated method stub
		return null;
	}

}
