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
	public void addTag(String tag){
		if(tagsCount.containsKey(tag)){
			tagsCount.put(tag, tagsCount.get(tag).intValue()+1);
		}else{
			tagsCount.put(tag, 1);
		}
	}
	
	public void addDomain(String domain){
		if(domainCount.containsKey(domain)){
			domainCount.put(domain, domainCount.get(domain).intValue()+1);
		}else{
			domainCount.put(domain, 1);
		}
	}
}
