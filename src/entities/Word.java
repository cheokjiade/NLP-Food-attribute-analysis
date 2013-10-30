package entities;

import java.util.HashMap;
import java.util.Map;

public class Word {
	public String word;
	public int wordCount;
	public HashMap<String, Integer> tagsCount;
	public HashMap<String, Integer> domainCount;
	public HashMap<String,Links> linksTo;
	public HashMap<String, Links> linkedFrom;
	public Word(String word){
		this.word = word;
		wordCount=1;
		tagsCount = new HashMap<>();
		domainCount = new HashMap<>();
		linksTo = new HashMap<>();
		linkedFrom = new HashMap<>();
	}
}
