package entities;

import java.util.ArrayList;
import java.util.HashMap;

public class CombinedWord extends Word{
	public ArrayList<Word> wordParts;
	public HashMap<String, Integer> domainCount;
	public CombinedWord(String combinedWord){
		super(combinedWord);
		wordParts = new ArrayList<Word>();
		domainCount = new HashMap<>();
	}
	
	public void addDomain(String domain){
		if(domainCount.containsKey(domain)){
			domainCount.put(domain, domainCount.get(domain).intValue()+1);
		}else{
			domainCount.put(domain, 1);
		}
	}
}
