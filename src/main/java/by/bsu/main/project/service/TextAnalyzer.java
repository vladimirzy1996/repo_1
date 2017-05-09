package by.bsu.main.project.service;

import java.util.List;

import by.bsu.main.project.entity.SearchResult;

public interface TextAnalyzer {
	List<SearchResult> execute(String searchRequest, byte[] text);
}
