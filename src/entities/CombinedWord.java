package entities;

import java.util.ArrayList;
import java.util.HashMap;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.collections.ActivatableArrayList;
import com.db4o.collections.ActivatableHashMap;
import com.db4o.ta.Activatable;

public class CombinedWord extends Word implements Activatable{
	private transient Activator _activator;
	private ActivatableArrayList<Word> wordParts;
	//public HashMap<String, Integer> domainCount;
	public CombinedWord(String combinedWord){
		super(combinedWord);
		activate(ActivationPurpose.WRITE);
		wordParts = new ActivatableArrayList<Word>();
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
//	public void addDomain(String domain){
//		if(domainCount.containsKey(domain)){
//			domainCount.put(domain, domainCount.get(domain).intValue()+1);
//		}else{
//			domainCount.put(domain, 1);
//		}
//	}


	/**
	 * @return the wordParts
	 */
	public ActivatableArrayList<Word> getWordParts() {
		activate(ActivationPurpose.READ);
		return wordParts;
	}
}
