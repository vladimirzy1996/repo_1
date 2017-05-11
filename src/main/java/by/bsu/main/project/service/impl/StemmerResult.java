package by.bsu.main.project.service.impl;

import by.bsu.main.project.service.impl.logic.dependency.EnglishSnowBallStemmer;
import smile.nlp.stemmer.LancasterStemmer;

public class StemmerResult {
	private String porter;
	private long porterTime;
	private String snowball;
	private long snowballTime;
	private String lancaster;
	private long lancasterTime;

	public void setPorter(String porter) {
		StemmerPorterImpl stemmerPorter = new StemmerPorterImpl();
		porterTime = System.nanoTime();
		this.porter = stemmerPorter.stem(porter);
		porterTime = System.nanoTime() - porterTime;
	}

	public void setSnowball(String snowball) {
		snowballTime = System.nanoTime();
		EnglishSnowBallStemmer snowBall = new EnglishSnowBallStemmer();
		snowBall.setCurrent(snowball);
		snowBall.stem();
		this.snowball = snowBall.getCurrent();
		snowballTime = System.nanoTime() - snowballTime;
	}

	public void setLancaster(String lancaster) {
		lancasterTime = System.nanoTime();
		LancasterStemmer lancasterStemmer = new LancasterStemmer();
		this.lancaster = lancasterStemmer.stem(lancaster);
		lancasterTime = System.nanoTime() - lancasterTime;

	}

	public boolean check() {
		return !(porter.equalsIgnoreCase(lancaster) && porter.equalsIgnoreCase(snowball));

	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder("");
		strBuilder.append("Porter: ").append(porter).append(" Time: ").append(porterTime).append(" ")
				.append("Snowball: ").append(snowball).append(" Time: ").append(snowballTime).append(" ")
				.append("Lancaster: ").append(lancaster).append(" Time: ").append(lancasterTime).append('\n');
		return strBuilder.toString();
	}

}
