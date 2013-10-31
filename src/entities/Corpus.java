package entities;

import java.util.HashMap;
import java.util.Map;

public class Corpus {
	public Map<String,Word> words;
	public Map<String,Domain> domains;	//Word Name, Word Object
	//public Map<String, WordInformation> wordMap;
	//Text File Name, Text File Object
	//public Map<String, TextFile> textFileMap;
	
	//public Map<String, WordInformation> nounMap;
	
	public Corpus(){
		words = new HashMap<>();
		domains = new HashMap<>();
		//wordMap = new HashMap<String, WordInformation>();
		//nounMap  = new HashMap<String, WordInformation>();
		//textFileMap = new HashMap<String, TextFile>();
	}
}
