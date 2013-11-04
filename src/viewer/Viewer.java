package viewer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import postagging.NLP;
import entities.Corpus;
import entities.Domain;
import entities.Links;
import entities.Word;

public class Viewer {
	public static final int MAX_DEPTH = 5;
	public static void listAllWords(){
		Corpus corpus;
        //List<Word> wordList = db.Db4oHelper.getInstance().db().query(Word.class);
		 //List<Links> linksList = db.Db4oHelper.getInstance().db().query(Links.class);
        List<Corpus> corpusList = db.Db4oHelper.getInstance().db().query(Corpus.class);
        
        if(corpusList.size()==0){
        	corpus = new Corpus();
        }else{
        	corpus = corpusList.get(0);
        }
        System.out.println(corpus.getDomains().size());
        
        ArrayList<entities.Word > alwi = new ArrayList<>(corpus.getWords().values());
		Comparator<entities.Word> c = new Comparator<entities.Word>() {

			@Override
			public int compare(entities.Word o1, entities.Word o2) {
				if((o2.getTagsCount().containsKey("NN")||(o2.getTagsCount().containsKey("NNP"))&&(o1.getTagsCount().containsKey("NN")||(o1.getTagsCount().containsKey("NNP"))))){
					int o2int = (o2.getTagsCount().containsKey("NN")?o2.getTagsCount().get("NN"):0) + (o2.getTagsCount().containsKey("NNP")?o2.getTagsCount().get("NNP"):0);
					int o1int = (o1.getTagsCount().containsKey("NN")?o1.getTagsCount().get("NN"):0) + (o1.getTagsCount().containsKey("NNP")?o1.getTagsCount().get("NNP"):0);
					return o2int - o1int;
					//return o2.tagFrequency - o1.corpusFreqency ;
				}else if(o2.getTagsCount().containsKey("NN")||(o2.getTagsCount().containsKey("NNP"))){
					return 1;
				}else if(o1.getTagsCount().containsKey("NN")||(o1.getTagsCount().containsKey("NNP"))){
					return -1;
				}return 0;

			}
		};
		Collections.sort(alwi,c);
		for(entities.Word wi : alwi){
			System.out.println(wi.getWord() + " appeared " + wi.getTagsCount() + "\nIn"+ wi.getDomainCount().size() + " unique domains " + wi.getDomainCount());
			System.out.println("Number of links to: " + wi.getLinksTo().size() + " " + wi.getLinksTo());
			System.out.println("Number of links from: " + wi.getLinkedFrom().size() + " " + wi.getLinkedFrom());
			System.out.println();
		}
	}

	public static HashSet<Word> searchByKeyWords(String inputString){
		String[] inputs = inputString.split(" ");
    	//Word w = corpus.getWords().get(wordString);
    	HashSet<Word> wordList = new HashSet<Word>();
    	Corpus corpus;
    	List<Corpus> corpusList = db.Db4oHelper.getInstance().db().query(Corpus.class);
        
        if(corpusList.size()==0){
        	corpus = new Corpus();
        }else{
        	corpus = corpusList.get(0);
        }
    	
    	Iterator it = corpus.getWords().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        for(String input: inputs){
    	        if(((String)pairs.getKey()).contains(input)){
    	        	Word tmpWord = (Word)pairs.getValue();
    	        	//wordList.add(tmpWord);
    	        	if(tmpWord.getLinksTo().size()+tmpWord.getLinkedFrom().size()>0){
    	        		wordList.add(tmpWord);
    	        	}
    	        	
    	        }
	        }
	        it.remove(); // avoids a ConcurrentModificationException
	    }
    	
    	//if (w==null) continue;
	    if(wordList.size()==0)return null;
	    HashSet<Word> adjSet = new HashSet<>();
    	    for(Word w:wordList){
    	    	
        	System.out.print(w.getWord() + " links to : ");
        	for(Links l: w.getLinksTo().values())System.out.print(l.getSubject().getWord() + "-" + l.getSubjectTag() +" , ");
        	System.out.print(w.getWord() + " is linked from :");
        	System.out.println();
        	ArrayList<List<Word>> wordChainList = new ArrayList<List<Word>>();
        	findAdjChain( w,MAX_DEPTH,wordChainList);
        	
        	for(List<Word> tmpWordList : wordChainList){
        		for(Word tmpWord :tmpWordList){
        			String tmpTag = tmpWord.gethighestTagCount();
        			if(tmpTag!=null && tmpTag.startsWith("JJ"))adjSet.add(tmpWord);
        		}
        	}
        	for(Links l: w.getLinkedFrom().values())System.out.print(l.getObject().getWord() + "-" + l.getObjectTag() +" , ");
        	System.out.println();
        	System.out.println();
	    }
    	    db.Db4oHelper.getInstance().db().close();
    	    return adjSet;
	}
	
	public static HashSet<Domain> searchByAdjs(String inputString, String selectedAdj){
		String selectedCheckboxAdj = selectedAdj;
		String[] inputs = inputString.split(" ");
		
		HashSet<Word> wordList = new HashSet<Word>();
		HashSet<Domain> domainSet = new HashSet<Domain>();
		HashSet<String> domainNameSet =null;
		
		Corpus corpus;
    	List<Corpus> corpusList = db.Db4oHelper.getInstance().db().query(Corpus.class);
    	
    	if(corpusList.size()==0){
        	corpus = new Corpus();
        }else{
        	corpus = corpusList.get(0);
        }
    	
//    	Iterator it = corpus.getDomains().entrySet().iterator();
//    	while (it.hasNext()) {
//    		 Map.Entry pairs = (Map.Entry)it.next();
//    		 if(((String)pairs.getKey()).contains(selectedAdj)){
//    			 Word tmpWord = (Word)pairs.getValue();
//    			 System.out.println("Source:  " + tmpWord);
//    		 }
//    	}
    	
    	Iterator it = corpus.getWords().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        for(String input: inputs){
    	        if(((String)pairs.getKey()).contains(input)){
    	        	Word tmpWord = (Word)pairs.getValue();
    	        	//wordList.add(tmpWord);
    	        	if(tmpWord.getLinksTo().size()+tmpWord.getLinkedFrom().size()>0){
    	        		wordList.add(tmpWord);
    	        		
    	        		if(tmpWord.getLinksTo().values().contains(selectedCheckboxAdj)){
    	        			domainNameSet = new HashSet<String>(tmpWord.getDomainCount().keySet());
    	        			for(String domainNameString:domainNameSet){
    	        				 domainSet.add(corpus.getDomains().get(domainNameString));
    	        			}
    	        		}
    	        	}
    	        	
    	        }
	        }
	        it.remove(); // avoids a ConcurrentModificationException
	    }
    	
    	//if (w==null) continue;
	    //if(wordList.size()==0)return null;
//	    HashSet<Word> adjSet = new HashSet<>();
//    	    for(Word w:wordList){
//    	    	
//        	System.out.print(w.getWord() + " links to : ");
//        	for(Links l: w.getLinksTo().values())System.out.print(l.getSubject().getWord() + "-" + l.getSubjectTag() +" , ");
//        	System.out.print(w.getWord() + " is linked from :");
//        	System.out.println();
//        	ArrayList<List<Word>> wordChainList = new ArrayList<List<Word>>();
//        	findAdjChain( w,MAX_DEPTH,wordChainList);
//        	
//        	for(List<Word> tmpWordList : wordChainList){
//        		for(Word tmpWord :tmpWordList){
//        			String tmpTag = tmpWord.gethighestTagCount();
//        			if(tmpTag!=null && tmpTag.startsWith("JJ"))adjSet.add(tmpWord);
//        		}
//        	}
//        	for(Links l: w.getLinkedFrom().values())System.out.print(l.getObject().getWord() + "-" + l.getObjectTag() +" , ");
//        	System.out.println();
//        	System.out.println();
//	    }
//    	    db.Db4oHelper.getInstance().db().close();
	    db.Db4oHelper.getInstance().db().close();    
		return domainSet;
		
	}
	
	public static HashSet<Domain> findCommonDomains(Collection<String> wordList){
		HashSet<Domain> domainSet = new HashSet<Domain>();
		HashSet<String> domainNameSet =null;
		Iterator<String> wIter = wordList.iterator();
		Corpus corpus;
		List<Corpus> corpusList = db.Db4oHelper.getInstance().db().query(Corpus.class);

		if(corpusList.size()==0){
			corpus = new Corpus();
		}else{
			corpus = corpusList.get(0);
		}

		while(wIter.hasNext()){
			if(domainNameSet==null){
				String w = wIter.next();
				domainNameSet = new HashSet<String>(corpus.getWords().get(w).getDomainCount().keySet());
			}
			else{
				domainNameSet.retainAll(corpus.getWords().get(wIter.next()).getDomainCount().keySet());
			}
		}
		System.out.println(domainNameSet);
		if(domainNameSet!=null){
			for(String domainNameString:domainNameSet){
				
				
				if (domainSet.contains(corpus.getDomains().get(domainNameString))){
					
				}
				else{
					domainSet.add(corpus.getDomains().get(domainNameString));
					System.out.println("Returning to JFrame :" + domainSet);
				}
				
			}
		}
		//db.Db4oHelper.getInstance().db().close();        
		return domainSet;
	}
		 
	
	
	public static void main(String[] args) {
		listAllWords();
		Scanner console = new Scanner(System.in);
        while(true){
        	db.Db4oHelper.getInstance().db().close();
        	System.out.println("Which Word do you want to view? ");
        	String wordString = console.nextLine();
        	String[] inputs = wordString.split(" ");
        	//Word w = corpus.getWords().get(wordString);
        	HashSet<Word> wordList = new HashSet<Word>();
        	Corpus corpus;
        	List<Corpus> corpusList = db.Db4oHelper.getInstance().db().query(Corpus.class);
            
            if(corpusList.size()==0){
            	corpus = new Corpus();
            }else{
            	corpus = corpusList.get(0);
            }
        	
        	Iterator it = corpus.getWords().entrySet().iterator();
    	    while (it.hasNext()) {
    	        Map.Entry pairs = (Map.Entry)it.next();
    	        for(String input: inputs){
	    	        if(((String)pairs.getKey()).contains(input)){
	    	        	Word tmpWord = (Word)pairs.getValue();
	    	        	//wordList.add(tmpWord);
	    	        	if(tmpWord.getLinksTo().size()+tmpWord.getLinkedFrom().size()>0){
	    	        		wordList.add(tmpWord);
	    	        	}
	    	        	
	    	        }
    	        }
    	        it.remove(); // avoids a ConcurrentModificationException
    	    }
        	
        	//if (w==null) continue;
    	    if(wordList.size()==0)continue;
	    	    for(Word w:wordList){
	    	    	
	        	System.out.print(w.getWord() + " links to : ");
	        	for(Links l: w.getLinksTo().values())System.out.print(l.getSubject().getWord() + "-" + l.getSubjectTag() +" , ");
	        	System.out.println();
	        	System.out.print(w.getWord() + " is linked from :");
	        	System.out.println();
	        	ArrayList<List<Word>> wordChainList = new ArrayList<List<Word>>();
	        	findAdjChain( w,MAX_DEPTH,wordChainList);
	        	for(List<Word> wordChain: wordChainList)System.out.println(wordChain);
	        	for(Links l: w.getLinkedFrom().values())System.out.print(l.getObject().getWord() + "-" + l.getObjectTag() +" , ");
	        	System.out.println();
	        	System.out.println();
    	    }
	    	    db.Db4oHelper.getInstance().db().close();
        }
        //NLP.printCompleteStatistics(corpus);
	}
	
	public static void findAdjChain(Word w,int levelsLeft, ArrayList<List<Word>> wordChainList){
		if(levelsLeft==0)return;
		if(levelsLeft==MAX_DEPTH){
			List newWordChain = new ArrayList<Word>();
			newWordChain.add(w);
			wordChainList.add(newWordChain);
		}else{
			wordChainList.get((wordChainList.size()-1)).add(w);
		}
		for(Links l: w.getLinkedFrom().values()){
    		if(l.getObjectTag().startsWith("JJ")||l.getObjectTag().startsWith("RB")){
    			System.out.println(multiplyString(" ",(MAX_DEPTH-levelsLeft)) + l.getObject().getWord() + "-" +l.getObjectTag() + " ");
    			findAdjChain(l.getObject(),levelsLeft-1,wordChainList);
    		}
    	}
	}
	public static String multiplyString(String s,int i){
		   String result="";
		   for(;i>0;i--){
		       result += s;   //for appending strings
		   }
		return result;
		}

}
