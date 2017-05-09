package by.bsu.main.project.entity;

import by.bsu.main.project.service.TextAnalyzer;
import by.bsu.main.project.service.impl.LancasterImpl;
import by.bsu.main.project.service.impl.SnowBallImpl;
import by.bsu.main.project.service.impl.StemmerPorterImpl;

public enum Algorithm {
	DEFAULT(null), PORTER(new StemmerPorterImpl()), LANCASTER(new LancasterImpl()), SNOWBALL(new SnowBallImpl());

	private TextAnalyzer textAnalyzer = null;

	Algorithm(TextAnalyzer textAnalyzer) {
		this.textAnalyzer = textAnalyzer;
	}

	public TextAnalyzer getValue() {
		return textAnalyzer;
	}

}
