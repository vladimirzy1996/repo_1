package by.bsu.main.project.service.impl;

import java.util.List;

public class PatternGenerator {
	public static String generate(List<String> request) {

		if (request != null) {
			StringBuilder response = new StringBuilder();
			int size = request.size();
			for (int i = 0; i < size; i++) {
				response.append("\\w*").append(request.get(i)).append("\\w*");
				if (i != size - 1) {
					response.append("\\s*");
				}
			}
			return response.toString();
		}
		return "";
	}

}
