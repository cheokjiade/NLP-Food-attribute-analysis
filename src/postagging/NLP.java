package postagging;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;































import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//public class NLP {
//	public static void main(String[] args){
//		Properties props = new Properties();
//	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
//	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//		MaxentTagger tagger = new MaxentTagger("models/left3words-wsj-0-18.tagger");
//	}
//}
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.trees.*;
import entities.Corpus;
import entities.WordInformation;

public class NLP {

	/** Convert Penn Treebank parse trees in a file to typed dependencies
	 *  (collapsed).  It does it two different ways for pedagogical reasons.
	 *  This is for English, but is easy to generalize. 
	 *  Usage: java TypedDependenciesDemo filename
	 */
	public static final String UNTAGGED_XML_FILES_FOLDER = "xmldocs";
	public static final String TAGGED_XML_FILES_FOLDER = "xmldocstagged";
	public static final String FINAL_TRAINED_TAGGER = "nlpTrain";
	public static void main(String[] args) {
		printStatistics();
		//tagAllXMLFiles();
//		if (args.length != 1) {
//			System.err.println("usage: java TypedDependenciesDemo filename");
//			return;
//		}
		//Properties prop = new Properties();
		//prop.load(new FileInputStream("POS 5\\WL_2.tagger.props"));
		//MaxentTagger mt = new MaxentTagger("POS 5\\WL_2.tagger",prop,true);
		//mt.tagString(toTag);
		//mt.tokenizeText(new BufferedReader(new FileReader(args[1])));
	}

	public static void synteticParsing(String[] args){
		// The fancy footwork here is to keep -TMP functional tags if present.
		// If not available, you could just use the no-argument constructor.
		Treebank tb = new DiskTreebank(new TreeReaderFactory() {
			public TreeReader newTreeReader(Reader in) {
				return new PennTreeReader(in, new LabeledScoredTreeFactory(),
						new NPTmpRetainingTreeNormalizer());
			}});
		tb.loadPath(args[0]);
		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		TreePrint tp = new TreePrint("typedDependenciesCollapsed");

		for (Tree t : tb) {
			GrammaticalStructure gs = gsf.newGrammaticalStructure(t);
			System.out.println(gs.typedDependenciesCollapsed());
			System.out.println();
			tp.printTree(t);
			System.out.println("----------");
		}

	}
	
	public static void defaultTagging(){
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream("");
			String everything = IOUtils.toString(inputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public static void trainOnCorrectedFiles(){
		MaxentTagger mt = new MaxentTagger();
		//mt.r
	}
	
	public static void printStatistics(){
		List<String> results = new ArrayList<String>();
		File[] files = new File(TAGGED_XML_FILES_FOLDER).listFiles();
		System.out.println("File Count: " + files.length);
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(FINAL_TRAINED_TAGGER+".tagger.props"));
			
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try{
			MaxentTagger mt = new MaxentTagger(FINAL_TRAINED_TAGGER+".tagger",prop,true);
			//return;
		}catch(Exception e){
			
		}
		
		DocumentPreprocessor dp = new DocumentPreprocessor(new StringReader(""));
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Corpus corpus = new Corpus();
        for(File f:files){
			if(f.getName().endsWith(".xml")){
				try {
					System.out.println(f.getName());
					dBuilder = dbFactory.newDocumentBuilder();
					org.w3c.dom.Document doc = dBuilder.parse(f);
		            doc.getDocumentElement().normalize();
		            Node textNode = doc.getElementsByTagName("text").item(0).getFirstChild();
		            String posTagged = textNode.getNodeValue();
		            String[] tags = posTagged.split(" ");
		            ArrayList<Word> multiWordBuffer = new ArrayList<Word>();
		            for (String tag : tags){
		            	String[] parts=tag.split("/");
		            	
		            	if(corpus.words.containsKey(parts[0].toLowerCase())){
		            		entities.Word w = corpus.words.get(parts[0].toLowerCase());
		            		//WordInformation wi = corpus.wordMap.get(parts[0].toLowerCase());
		            		w.wordCount++;
		            		if(w.tagsCount.containsKey(parts[1])){
		            			w.tagsCount.put(parts[1], w.tagsCount.get(parts[1]).intValue()+1);
		            		}else{
		            			w.tagsCount.put(parts[1], 1);
		            		}
		            	}else{
		            		entities.Word w = new entities.Word(parts[0].toLowerCase());
		            		w.tagsCount.put(parts[1], 1);
		            		//WordInformation wi = new WordInformation(parts[0].toLowerCase());
		            		//wi.tagFrequency.put(parts[1], 1);
		            		corpus.words.put(parts[0].toLowerCase(), w);
		            	}
		            }
				}catch (Exception e){
					e.printStackTrace();
				}
			}
        }
        System.out.println("Total Number of Unique Words = " + corpus.wordMap.size());
        ArrayList<WordInformation > alwi = new ArrayList<>(corpus.wordMap.values());
        Comparator<WordInformation> c = new Comparator<WordInformation>() {
			
			@Override
			public int compare(WordInformation o1, WordInformation o2) {
				if((o2.tagFrequency.containsKey("NN")||(o2.tagFrequency.containsKey("NNP"))&&(o1.tagFrequency.containsKey("NN")||(o1.tagFrequency.containsKey("NNP"))))){
					int o2int = (o2.tagFrequency.containsKey("NN")?o2.tagFrequency.get("NN"):0) + (o2.tagFrequency.containsKey("NNP")?o2.tagFrequency.get("NNP"):0);
					int o1int = (o1.tagFrequency.containsKey("NN")?o1.tagFrequency.get("NN"):0) + (o1.tagFrequency.containsKey("NNP")?o1.tagFrequency.get("NNP"):0);
					return o2int - o1int;
					//return o2.tagFrequency - o1.corpusFreqency ;
				}else if(o2.tagFrequency.containsKey("NN")||(o2.tagFrequency.containsKey("NNP"))){
					return 1;
				}else if(o1.tagFrequency.containsKey("NN")||(o1.tagFrequency.containsKey("NNP"))){
					return -1;
				}return 0;
				
			}
		};
		Collections.sort(alwi,c);
        for(WordInformation wi : alwi){
        	System.out.println(wi.word + " appeared " + wi.corpusFreqency);
        	Iterator it = wi.tagFrequency.entrySet().iterator();
        	while(it.hasNext()){
        		Map.Entry pairs = (Map.Entry)it.next();
        		System.out.println((String)pairs.getKey() + " : " + (Integer)pairs.getValue());
        		it.remove();
        	}
        	System.out.println();
        }
        System.out.println();
        System.out.println();
		//dp.
	}
	
	public static void tagAllXMLFiles(){
		List<String> results = new ArrayList<String>();
		File[] files = new File(UNTAGGED_XML_FILES_FOLDER).listFiles();
		System.out.println("File Count: " + files.length);
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(FINAL_TRAINED_TAGGER+".tagger.props"));
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		MaxentTagger mt = new MaxentTagger(FINAL_TRAINED_TAGGER+".tagger",prop,true);
		
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        
		FileInputStream inputStream;
		
		for(File f:files){
			if(f.getName().endsWith(".xml")){
				try {
					dBuilder = dbFactory.newDocumentBuilder();
					org.w3c.dom.Document doc = dBuilder.parse(f);
		            doc.getDocumentElement().normalize();
		            Node textNode = doc.getElementsByTagName("text").item(0).getFirstChild();
		            String posUntagged = textNode.getNodeValue();
		            
		            textNode.setNodeValue(mt.tagString(posUntagged));


		            doc.getDocumentElement().normalize();
		            TransformerFactory transformerFactory = TransformerFactory.newInstance();
		            Transformer transformer = transformerFactory.newTransformer();
		            DOMSource source = new DOMSource(doc);
		            StreamResult result = new StreamResult(new File(TAGGED_XML_FILES_FOLDER+"\\"+f.getName()));
		            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		            transformer.transform(source, result);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (TransformerConfigurationException e) {
					e.printStackTrace();
				} catch (TransformerException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				}
			}
			
		}

	}
	
	
}