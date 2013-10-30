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
	
	public Links(Word subject, Word object){
		this.subject = subject;
		this.object = object;
	}
}
