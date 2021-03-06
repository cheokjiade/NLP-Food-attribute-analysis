package postagging;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

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





import syntactictagging.SyntacticTagging;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.trees.*;
import entities.CombinedWord;
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
		//getAllAdj();
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

		
		//db.Db4oHelper.getInstance().db().close();

		for(File f:files){
			if(f.getName().endsWith(".xml")){
				try {
					Corpus corpus;
					//List<Word> wordList = db.Db4oHelper.getInstance().db().query(Word.class);
					List<Corpus> corpusList = db.Db4oHelper.getInstance().db().query(Corpus.class);

					if(corpusList.size()==0){
						corpus = new Corpus();
						db.Db4oHelper.getInstance().db().store(corpus);
					}else{
						corpus = corpusList.get(0);
					}
					System.out.println(f.getName());
					dBuilder = dbFactory.newDocumentBuilder();
					org.w3c.dom.Document doc = dBuilder.parse(f);
					doc.getDocumentElement().normalize();
					Node textNode = doc.getElementsByTagName("text").item(0).getFirstChild();
					//List<Node> tagNodes = 
					String posTagged = textNode.getNodeValue();
					String[] tags = posTagged.split(" ");
					ArrayList<entities.Word> multiWordBuffer = new ArrayList<entities.Word>();

					//if domain already exist in corpus, do nothing, else add domain to corpus
					if(corpus.getDomains().containsKey(f.getName()));
					else {
						//creating new domain objects
						entities.Domain d = new entities.Domain(f.getName());
						
						//extracting out tags
						NodeList nl = doc.getElementsByTagName("tag");
						for(int i=0;i<nl.getLength();i++){
							for(String tag:nl.item(i).getFirstChild().getNodeValue().split(" "))
								d.getTags().add(tag);
						}
						d.setSource(doc.getElementsByTagName("source").item(0).getFirstChild().getNodeValue());
						//add domain objects to corpus. Key == name of corpus, d == object that has been created
						corpus.getDomains().put(f.getName(), d);
						db.Db4oHelper.getInstance().db().store(d);
						for (String tag : tags){
							String[] parts=tag.split("/");

							if(corpus.getWords().containsKey(parts[0].toLowerCase())){
								entities.Word w = corpus.getWords().get(parts[0].toLowerCase());
								//WordInformation wi = corpus.wordMap.get(parts[0].toLowerCase());
								w.setWordCount(w.getWordCount()+1);
								w.addTag(parts[1]);
								w.addDomain(f.getName());
								db.Db4oHelper.getInstance().db().store(w);
								//Add NN to multiword
								if(parts[1].startsWith("NN")){
									//While the multiword has not ended, add it to list
									multiWordBuffer.add(w);
								}else{
									//else assemble the multi word and put it into corpus
									if(multiWordBuffer.size()>1){
										String combinedWord = "";
										//Join the combined words buffer
										for(entities.Word tmpWord:multiWordBuffer)
											combinedWord+=tmpWord.getWord()+ " ";
										//Remove Trailing whitespace
										combinedWord = combinedWord.substring(0, combinedWord.length()-1);
										//if the corpus does not contain the word
										if(!corpus.getWords().containsKey(combinedWord)){
											//Create a combined word entity and fill it up
											CombinedWord cw = new CombinedWord(combinedWord);
											cw.addTag("NN");
											cw.addDomain(f.getName());
											for(entities.Word tmpWord:multiWordBuffer)
												cw.getWordParts().add(tmpWord);
											corpus.getWords().put(combinedWord, cw);
											db.Db4oHelper.getInstance().db().store(cw);
										}else{
											//update the exisiting combinedword entity if it already exists
											CombinedWord cw = (CombinedWord) corpus.getWords().get(combinedWord);
											cw.setWordCount(cw.getWordCount()+1);
											cw.addTag("NN");
											cw.addDomain(f.getName());
											db.Db4oHelper.getInstance().db().store(cw);
										}
									}
									//clear buffer in prep for next multi word
									multiWordBuffer.clear();
									//put words into domain object
									d.getWords().put(parts[0].toLowerCase(), w);
									db.Db4oHelper.getInstance().db().commit();
								}

							}else{
								entities.Word w = new entities.Word(parts[0].toLowerCase());
								w.getTagsCount().put(parts[1], 1);
								w.addTag(parts[1]);
								w.addDomain(f.getName());
								
								corpus.getWords().put(parts[0].toLowerCase(), w);
								db.Db4oHelper.getInstance().db().store(w);
								//put words into domain object
								d.getWords().put(parts[0].toLowerCase(), w);
								if(parts[1].startsWith("NN")){
									//While the multiword has not ended, add it to list
									multiWordBuffer.add(w);
								}else{
									//else assemble the multi word and put it into corpus
									if(multiWordBuffer.size()>1){
										String combinedWord = "";
										//Join the combined words buffer
										for(entities.Word tmpWord:multiWordBuffer)
											combinedWord+=tmpWord.getWord()+ " ";
										//Remove Trailing whitespace
										combinedWord = combinedWord.substring(0, combinedWord.length());
										//if the corpus does not contain the word
										if(!corpus.getWords().containsKey(combinedWord)){
											//Create a combined word entity and fill it up
											CombinedWord cw = new CombinedWord(combinedWord);
											cw.addTag("NN");
											cw.addDomain(f.getName());
											for(entities.Word tmpWord:multiWordBuffer)
												cw.getWordParts().add(tmpWord);
											corpus.getWords().put(combinedWord, cw);
											db.Db4oHelper.getInstance().db().store(cw);
										}else{
											//update the exisiting combinedword entity if it already exists
											CombinedWord cw = (CombinedWord) corpus.getWords().get(combinedWord);

											cw.setWordCount(cw.getWordCount()+1);
											cw.addTag("NN");
											cw.addDomain(f.getName());
											db.Db4oHelper.getInstance().db().store(cw);
										}
									}
									//clear buffer in prep for next multi word
									multiWordBuffer.clear();
								}
							}
						}
					}
					db.Db4oHelper.getInstance().db().close();
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		//db.Db4oHelper.getInstance().db().store(corpus);

		db.Db4oHelper.getInstance().db().close();
		SyntacticTagging.createSyntacticTreesFromXML();

		//dp.
	}

	public static void printCompleteStatistics(Corpus corpus){
		System.out.println("Total Number of Domains: " + corpus.getDomains().size());
		System.out.println("Total Number of Unique Words = " + corpus.getWords().size());
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
			//        	Iterator it = wi.getTagsCount().entrySet().iterator();
			//        	while(it.hasNext()){
			//        		Map.Entry pairs = (Map.Entry)it.next();
			//        		System.out.println((String)pairs.getKey() + " : " + (Integer)pairs.getValue());
			//        		it.remove();
			//        	}
			System.out.println();
		}
		System.out.println();
		System.out.println();
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


	public static void getAllAdj(){
		TreeSet<String> adjSet = new TreeSet<String>();
		File[] files = new File(TAGGED_XML_FILES_FOLDER).listFiles();
		System.out.println("File Count: " + files.length);
		DocumentPreprocessor dp = new DocumentPreprocessor(new StringReader(""));

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		
		//db.Db4oHelper.getInstance().db().close();

		for(File f:files){
			if(f.getName().endsWith(".xml")){
				try {
					System.out.println(f.getName());
					dBuilder = dbFactory.newDocumentBuilder();
					org.w3c.dom.Document doc = dBuilder.parse(f);
					doc.getDocumentElement().normalize();
					Node textNode = doc.getElementsByTagName("text").item(0).getFirstChild();
					//List<Node> tagNodes = 
					String posTagged = textNode.getNodeValue();
					String[] tags = posTagged.split(" ");
					for(String tag:tags){
						if(tag.split("/")[1].startsWith("JJ")){
							adjSet.add(tag.split("/")[0]);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		for(String s : adjSet){
			System.out.println(s);
		}
	}
}