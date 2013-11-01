package viewer;

import java.util.ArrayList;
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
import entities.Links;
import entities.Word;

public class Viewer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
        Scanner console = new Scanner(System.in);
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
        while(true){
        	System.out.println("Which Word do you want to view? ");
        	String wordString = console.nextLine();
        	String[] inputs = wordString.split(" ");
        	//Word w = corpus.getWords().get(wordString);
        	HashSet<Word> wordList = new HashSet<Word>();
        	Iterator it = corpus.getWords().entrySet().iterator();
    	    while (it.hasNext()) {
    	        Map.Entry pairs = (Map.Entry)it.next();
    	        for(String input: inputs){
	    	        if(((String)pairs.getKey()).contains(input)){
	    	        	Word tmpWord = (Word)pairs.getValue();
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
	        	for(Links l: w.getLinkedFrom().values())System.out.print(l.getObject().getWord() + "-" + l.getObjectTag() +" , ");
	        	System.out.println();
	        	System.out.println();
    	    }
        }
        //NLP.printCompleteStatistics(corpus);
	}

}
