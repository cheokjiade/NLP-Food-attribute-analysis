package entities;

import java.util.HashMap;
import java.util.Map;

public class WordInformation {
	public String word;
	public int corpusFreqency;
	public Map<String, Integer> tagFrequency;
	public Map<String, Integer> tagInFileFrequency;
	public Map<String,WordInTextInformation> wordInTextInformation;
	
	public WordInformation(String word){
		this.word = word;
		corpusFreqency = 1;
		tagFrequency = new HashMap<String, Integer>();
		tagInFileFrequency = new HashMap<String, Integer>();
	}
}
