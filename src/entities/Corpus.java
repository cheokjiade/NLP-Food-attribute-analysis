package entities;

import java.util.HashMap;
import java.util.Map;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.collections.ActivatableHashMap;
import com.db4o.ta.Activatable;

public class Corpus implements Activatable{
	private transient Activator _activator;
	private ActivatableHashMap<String,Word> words;
	private ActivatableHashMap<String,Domain> domains;	//Word Name, Word Object
	//public Map<String, WordInformation> wordMap;
	//Text File Name, Text File Object
	//public Map<String, TextFile> textFileMap;
	
	//public Map<String, WordInformation> nounMap;
	
	public Corpus(){
		activate(ActivationPurpose.WRITE);
		words = new ActivatableHashMap<>();
		domains = new ActivatableHashMap<>();
		//wordMap = new HashMap<String, WordInformation>();
		//nounMap  = new HashMap<String, WordInformation>();
		//textFileMap = new HashMap<String, TextFile>();
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
	 * @return the words
	 */
	public ActivatableHashMap<String, Word> getWords() {
		activate(ActivationPurpose.READ);
		return words;
	}

	/**
	 * @return the domains
	 */
	public ActivatableHashMap<String, Domain> getDomains() {
		activate(ActivationPurpose.READ);
		return domains;
	}
}
