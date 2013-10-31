package entities;

import java.util.HashMap;
import java.util.Map;

public class Links {
	public Word subject;
	public String subjectTag;
	public Word object;
	public String objectTag;
	public int linkCount;
	//the number of times it occurs in a domain
	public HashMap<String, Integer> domainCount;
	
	public Links(Word subject, String subjectTag, Word object, String objectTag){
		this.subject = subject;
		this.object = object;
		this.subjectTag = subjectTag;
		this.objectTag = objectTag;
		linkCount =1;
		domainCount= new HashMap<>();
	}
	public void addDomain(String domain){
		if(domainCount.containsKey(domain)){
			domainCount.put(domain, domainCount.get(domain).intValue()+1);
		}else{
			domainCount.put(domain, 1);
		}
	}
}
