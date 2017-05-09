package by.bsu.main.project.controllers;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import by.bsu.main.project.entity.Algorithm;
import by.bsu.main.project.entity.SearchScreenControllerConstants;
import by.bsu.main.project.service.TextAnalyzer;
import by.bsu.main.project.service.impl.EmptyAnalyzerImpl;

@Controller
@RequestMapping(SearchScreenControllerConstants.URL_MAIN)
public class SearchScreenController {
	private Logger logger = LogManager.getLogger(SearchScreenController.class);

	private TextAnalyzer textAnalyzer = new EmptyAnalyzerImpl();
	private byte[] text;

	@GetMapping
	public String initSearchPageScreen() {
		return SearchScreenControllerConstants.PAGE_SEARCH;
	}

	@PostMapping(value = SearchScreenControllerConstants.URL_RESULTS)
	public ModelAndView search(@RequestParam(SearchScreenControllerConstants.ATTR_SEARCH) String searchRequest) {
		ModelAndView modelAndView = new ModelAndView();
		if (textAnalyzer instanceof EmptyAnalyzerImpl || text == null) {
			modelAndView.setViewName(SearchScreenControllerConstants.PAGE_SEARCH);
		} else {
			modelAndView.addObject(SearchScreenControllerConstants.ATTR_RESULTS_LIST,
					textAnalyzer.execute(searchRequest, text));
			modelAndView.addObject(SearchScreenControllerConstants.ATTR_SEARCH_REQUEST, searchRequest);
			modelAndView.setViewName(SearchScreenControllerConstants.PAGE_SEARCH_RESULT);
		}
		return modelAndView;
	}

	@PostMapping(value = SearchScreenControllerConstants.URL_INIT_ANALYZER)
	public void initTextAnalyzer(@RequestParam(SearchScreenControllerConstants.ATTR_ALGORITHM) String algorithm) {
		if (algorithm != null && !algorithm.isEmpty()) {
			textAnalyzer = Algorithm.valueOf(algorithm).getValue();
		}
	}

	@PostMapping(value = SearchScreenControllerConstants.URL_UPLOAD)
	public void doImport(@RequestParam(SearchScreenControllerConstants.ATTR_FILE) MultipartFile file) {
		try {
			text = file.getBytes();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

	}

}
