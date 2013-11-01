package entities;

import java.util.ArrayList;
import java.util.HashMap;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.collections.ActivatableArrayList;
import com.db4o.collections.ActivatableHashMap;
import com.db4o.collections.ActivatableHashSet;
import com.db4o.ta.Activatable;

public class Domain implements Activatable{
	private transient Activator _activator;
	private String domain;
	private ActivatableHashMap<String, Word> words;
	private ActivatableHashSet<String> tags;
	public Domain(String domain){
		activate(ActivationPurpose.WRITE);
		this.domain=domain;
		words = new ActivatableHashMap<>();
		tags = new ActivatableHashSet<>();
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
	 * @return the domain
	 */
	public String getDomain() {
		activate(ActivationPurpose.READ);
		return domain;
	}
	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		activate(ActivationPurpose.WRITE);
		this.domain = domain;
	}
	/**
	 * @return the words
	 */
	public ActivatableHashMap<String, Word> getWords() {
		activate(ActivationPurpose.READ);
		return words;
	}
	/**
	 * @return the tags
	 */
	public ActivatableHashSet<String> getTags() {
		activate(ActivationPurpose.READ);
		return tags;
	}
}
