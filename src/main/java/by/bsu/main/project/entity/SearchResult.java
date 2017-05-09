package by.bsu.main.project.entity;

public class SearchResult {
	private String word;
	private String line;
	private int lineIndex;
	private int linePositionIndex;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}

	public int getLinePositionIndex() {
		return linePositionIndex;
	}

	public void setLinePositionIndex(int linePositionIndex) {
		this.linePositionIndex = linePositionIndex;
	}

}
