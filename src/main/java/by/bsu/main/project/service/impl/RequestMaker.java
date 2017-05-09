package by.bsu.main.project.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RequestMaker {
	public static List<String> defineStringIntoWords(String request) {
		StringTokenizer tokenizer = new StringTokenizer(request);
		List<String> result = new ArrayList<String>();
		while (tokenizer.hasMoreTokens()) {
			result.add(tokenizer.nextToken());
		}
		return result;
	}

	public static String concatListOfString(List<String> request) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < request.size(); i++) {
			if (i != request.size() - 1) {
				stringBuilder.append(request.get(i)).append(" ");
			} else {
				stringBuilder.append(request.get(i));
			}
		}
		return stringBuilder.toString();
	}
}
