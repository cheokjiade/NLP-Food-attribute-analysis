package entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.collections.ActivatableHashMap;
import com.db4o.ta.Activatable;

public class Word implements Activatable{
	private transient Activator _activator;
	private String word;
	private int wordCount;
	private ActivatableHashMap<String, Integer> tagsCount;
	private ActivatableHashMap<String, Integer> domainCount;
	private ActivatableHashMap<String,Links> linksTo;
	private ActivatableHashMap<String, Links> linkedFrom;
	public Word(String word){
		activate(ActivationPurpose.WRITE);
		this.word = word;
		wordCount=1;
		tagsCount = new ActivatableHashMap<>();
		domainCount = new ActivatableHashMap<>();
		linksTo = new ActivatableHashMap<>();
		linkedFrom = new ActivatableHashMap<>();
	}
	
	public String gethighestTagCount(){
		activate(ActivationPurpose.READ);
		ActivatableHashMap.Entry<String,Integer> highestPair=null;
		Iterator<Entry<String, Integer>> it = getTagsCount().entrySet().iterator();
	    while (it.hasNext()) {
	    	ActivatableHashMap.Entry<String,Integer> pairs = (Map.Entry)it.next();
	        if(highestPair==null||((Integer)pairs.getValue())>((Integer)highestPair.getValue()))
	        	highestPair=pairs;
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    if(highestPair==null) return null;
	    return highestPair.getKey();
	}
	public void addTag(String tag){
		activate(ActivationPurpose.WRITE);
		if(getTagsCount().containsKey(tag)){
			getTagsCount().put(tag, getTagsCount().get(tag).intValue()+1);
		}else{
			getTagsCount().put(tag, 1);
		}
	}
	
	public void addDomain(String domain){
		activate(ActivationPurpose.WRITE);
		if(getDomainCount().containsKey(domain)){
			getDomainCount().put(domain, getDomainCount().get(domain).intValue()+1);
		}else{
			getDomainCount().put(domain, 1);
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return word;
	}
	public void activate(ActivationPurpose purpose) {
        if(_activator != null) {
            _activator.activate(purpose);
        }
    }
    public void bind(Activator activator) {
        if (_activator == activator) {
            return;
        }
        if (activator != null && _activator != null) {
            throw new IllegalStateException();
        }
        _activator = activator;
    }
	/**
	 * @return the word
	 */
	public String getWord() {
		activate(ActivationPurpose.READ);
		return word;
	}
	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		activate(ActivationPurpose.WRITE);
		this.word = word;
	}
	/**
	 * @return the wordCount
	 */
	public int getWordCount() {
		activate(ActivationPurpose.READ);
		return wordCount;
	}
	/**
	 * @param wordCount the wordCount to set
	 */
	public void setWordCount(int wordCount) {
		activate(ActivationPurpose.WRITE);
		this.wordCount = wordCount;
	}
	/**
	 * @return the tagsCount
	 */
	public ActivatableHashMap<String, Integer> getTagsCount() {
		activate(ActivationPurpose.READ);
		return tagsCount;
	}
	/**
	 * @return the domainCount
	 */
	public ActivatableHashMap<String, Integer> getDomainCount() {
		activate(ActivationPurpose.READ);
		return domainCount;
	}
	/**
	 * @return the linksTo
	 */
	public ActivatableHashMap<String, Links> getLinksTo() {
		activate(ActivationPurpose.READ);
		return linksTo;
	}
	/**
	 * @return the linkedFrom
	 */
	public ActivatableHashMap<String, Links> getLinkedFrom() {
		activate(ActivationPurpose.READ);
		return linkedFrom;
	}
	
}
