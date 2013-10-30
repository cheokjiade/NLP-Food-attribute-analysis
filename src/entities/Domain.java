package entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Domain {
	public String domain;
	public HashMap<String, Word> words;
	public ArrayList<String> tags;
	public Domain(String domain){
		this.domain=domain;
		words = new HashMap<>();
		tags = new ArrayList<>();
	}
}
