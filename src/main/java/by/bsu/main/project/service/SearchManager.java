package by.bsu.main.project.service;

import java.util.List;

import by.bsu.main.project.entity.SearchResult;

public interface SearchManager {
	List<SearchResult> searchString(String searchingString, byte[] text);
}
