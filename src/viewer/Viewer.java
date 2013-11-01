package viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
        	Word w = corpus.getWords().get(wordString);
        	System.out.print(w.getWord() + " links to : ");
        	for(Links l: w.getLinksTo().values())System.out.print(l.getSubject().getWord() + " , ");
        	System.out.println();
        	System.out.print(w.getWord() + " is linked from :");
        	for(Links l: w.getLinkedFrom().values())System.out.print(l.getObject().getWord() + " , ");
        	System.out.println();
        }
        //NLP.printCompleteStatistics(corpus);
	}

}
