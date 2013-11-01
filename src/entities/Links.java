package entities;

import java.util.HashMap;
import java.util.Map;

import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.collections.ActivatableHashMap;
import com.db4o.ta.Activatable;

public class Links implements Activatable{
	private transient Activator _activator;
	public Word subject;
	public String subjectTag;
	public Word object;
	public String objectTag;
	public int linkCount;
	//the number of times it occurs in a domain
	public ActivatableHashMap<String, Integer> domainCount;
	
	public Links(Word subject, String subjectTag, Word object, String objectTag){
		activate(ActivationPurpose.WRITE);
		this.subject = subject;
		this.object = object;
		this.subjectTag = subjectTag;
		this.objectTag = objectTag;
		linkCount =1;
		domainCount= new ActivatableHashMap<>();
		
	}
	public void addDomain(String domain){
		activate(ActivationPurpose.READ);
		if(domainCount.containsKey(domain)){
			domainCount.put(domain, domainCount.get(domain).intValue()+1);
		}else{
			domainCount.put(domain, 1);
		}
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
	 * @return the subject
	 */
	public Word getSubject() {
		activate(ActivationPurpose.READ);
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(Word subject) {
		activate(ActivationPurpose.WRITE);
		this.subject = subject;
	}
	/**
	 * @return the subjectTag
	 */
	public String getSubjectTag() {
		activate(ActivationPurpose.READ);
		return subjectTag;
	}
	/**
	 * @param subjectTag the subjectTag to set
	 */
	public void setSubjectTag(String subjectTag) {
		activate(ActivationPurpose.WRITE);
		this.subjectTag = subjectTag;
	}
	/**
	 * @return the object
	 */
	public Word getObject() {
		activate(ActivationPurpose.READ);
		return object;
	}
	/**
	 * @param object the object to set
	 */
	public void setObject(Word object) {
		activate(ActivationPurpose.WRITE);
		this.object = object;
	}
	/**
	 * @return the objectTag
	 */
	public String getObjectTag() {
		activate(ActivationPurpose.READ);
		return objectTag;
	}
	/**
	 * @param objectTag the objectTag to set
	 */
	public void setObjectTag(String objectTag) {
		activate(ActivationPurpose.WRITE);
		this.objectTag = objectTag;
	}
	/**
	 * @return the linkCount
	 */
	public int getLinkCount() {
		activate(ActivationPurpose.READ);
		return linkCount;
	}
	/**
	 * @param linkCount the linkCount to set
	 */
	public void setLinkCount(int linkCount) {
		activate(ActivationPurpose.WRITE);
		this.linkCount = linkCount;
	}
	/**
	 * @return the domainCount
	 */
	public ActivatableHashMap<String, Integer> getDomainCount() {
		activate(ActivationPurpose.READ);
		return domainCount;
	}
}
