package syntactictagging;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.parser.lexparser.TreebankLangParserParams;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.WhitespaceTokenizer;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.util.Function;
import edu.stanford.nlp.util.Generics;
import edu.stanford.nlp.util.Timing;

public class SyntacticTagging {

	public static final String TAGGED_XML_FILES_FOLDER = "xmldocstagged";
	public static void main(String[] args) {
		createSyntacticTrees("It/PRP 's/POS late/NN at/IN night/NN and/CC you/PRP are/VBP feeling/VBG a/DT little/JJ hungry/NN for/IN a/DT something/NN seriously/RB lemak/NN -LRB-/IN rich/JJ and/CC savoury/JJ -RRB-/NN because/IN you/PRP have/VBP been/VBN trying/VBG to/TO go/VB on/IN a/DT salad/NN and/CC fruit/NN diet/NN for/IN the/DT whole/JJ day/NN ./.\nWhat/WP do/VBP you/PRP do/VBP ?/.\nWell/RB ,/, you/PRP could/MD turn/VB on/IN the/DT computer/NN ,/, log/VB onto/IN ieatishootipost/NN and/CC check/VB out/IN the/DT After/JJ 10/CD label/NN ./.\nThen/RB a/DT big/JJ smile/NN will/MD come/VB across/IN your/PRP$ face/NN when/WRB you/PRP discover/VB this/DT blog/NN ./.\nCos/NNP salvation/NNP is/VBZ at/IN hand/NN !/.\nThere/EX is/VBZ a/DT Nasi/NNP Lemak/NNP place/NN dishing/VBG out/IN Nasi/NNP Lemak/NNP with/IN a/DT seriously/RB shiok/JJ curry/NN till/NN 2/CD am/VBP !/.\nI/PRP really/RB liked/VBD Hainanese/NNP Style/NNP Curry/NNP Chicken/NNP Wings/NNP ./.\nThe/DT curry/NN gravy/NN is/VBZ darn/JJ shiok/NN and/CC sure/VB to/TO satisfy/VB your/PRP$ umami/NN -LRB-/NN savoury/NN -RRB-/NN craving/NN ./.\nThis/DT place/NN is/VBZ also/RB famous/JJ for/IN its/PRP$ Black/JJ Chicken/NNP Wings/NNP ./.\nThese/DT are/VBP fried/JJ chicken/NN wings/NNS coated/VBN with/IN a/DT sweet/JJ and/CC savoury/JJ ,/, Kecap/NNP Manis/NNS based/VBD sauce/NN ./.\nThe/DT sauce/NN is/VBZ very/RB shiok/JJ but/CC I/PRP found/VBD the/DT chicken/NN wings/NNS a/DT bit/NN overcooked/VBN that/IN night/NN so/IN it/PRP was/VBD a/DT bit/NN too/RB dry/JJ ./.\nThe/DT rice/NN was/VBD fragrant/JJ ,/, but/CC not/RB the/DT best/JJS I/PRP have/VBP tasted/VBN ./.\nSambal/NNP chilli/NN was/VBD the/DT sweeter/NN version/NN which/WRB I/PRP always/RB prefer/VB with/IN my/PRP$ Nasi/NNP Lemak/NNP ./.\nForummers/NNS have/VBP raved/VBN about/IN the/DT Petai/NNP -LRB-/NNP Topmost/NNP pic/NN -RRB-/NN but/CC I/PRP do/VBP n't/RB think/VB I/PRP have/VBP developed/VBN a/DT taste/NN for/IN it/PRP ./.\nI/PRP find/VB it/PRP too/RB bitter/JJ for/IN my/PRP$ palate/NN ./.\nAll/PDT the/DT curry/NN dishes/NNS here/RB are/VBP very/RB very/RB good/JJ ./.\nAside/RB from/IN the/DT curry/NN chicken/NN ,/, the/DT brinjal/NN and/CC the/DT Sayur/NNP Lodeh/NNP -LRB-/NNP Mixed/NNP Veg/NNP Curry/NNP -RRB-/NNP were/VBD excellent/JJ ./.\nMy/PRP$ only/RB other/JJ complaint/NN is/VBZ that/IN they/PRP do/VBP n't/RB use/VB a/DT Bain-Marie/NN to/TO keep/VB the/DT food/NN warm/JJ ,/, so/RB only/RB the/DT rice/NN is/VBZ warm/JJ ./.\nIt/PRP would/MD have/VB been/VBN great/JJ if/IN the/DT curries/NNS were/VBD not/RB cold/JJ ./.\nGood/JJ place/NN to/TO satisfy/VB your/PRP$ Nasi/NNP Lemak/NNP craving/NN with/IN some/DT really/RB excellent/JJ curries/NNS ./.\nWould/MD have/VB been/VBN perfect/JJ if/IN the/DT food/NN was/VBD warm/JJ and/CC the/DT fried/JJ stuff/NN were/VBD not/RB over/RB fried/VBD ./.\n");
	}
	
	public static void createSyntacticTreesFromXML(){
		File[] files = new File(TAGGED_XML_FILES_FOLDER).listFiles();
		System.out.println("File Count: " + files.length);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        
		for(File f:files){
			if(f.getName().endsWith(".xml")){
				try {
					System.out.println(f.getName());
					dBuilder = dbFactory.newDocumentBuilder();
					org.w3c.dom.Document doc = dBuilder.parse(f);
		            doc.getDocumentElement().normalize();
		            Node textNode = doc.getElementsByTagName("text").item(0).getFirstChild();
		            String posTagged = textNode.getNodeValue();
		            posTagged = posTagged.replaceAll("/. ", "/.\n");
		            
		            
				}catch(Exception e){
					
				}
			}
		}
	}
	
	public static void createSyntacticTrees(String s){
		Options o = new Options();
		//o.setOption(new String[]{"-tokenized","-tagSeparator","/","-outputFormat", "penn"}, 0);
		LexicalizedParser lp = LexicalizedParser.loadModel(o,"-outputFormat", "penn");
		
		List<List<? extends HasWord>> document;
		TokenizerFactory<? extends HasWord> tokenizerFactory = WhitespaceTokenizer.factory();
		TreebankLanguagePack tlp = o.tlpParams.treebankLanguagePack();
		DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(new StringReader(s));
		documentPreprocessor.setTokenizerFactory(tokenizerFactory);
	    documentPreprocessor.setSentenceFinalPuncWords(tlp.sentenceFinalPunctuationWords());
	    documentPreprocessor.setSentenceDelimiter("\n");
	    documentPreprocessor.setTagDelimiter("/");
	    document = Generics.newArrayList();
	    for(List<HasWord>a:documentPreprocessor)document.add(a);
	    for(List<? extends HasWord> sentence:document){
	    	//each sentence is 1 tree. Use this tree for everything
	    	Tree tree = lp.parse(sentence);
	    	ArrayList<Tree> btmBranchList = new ArrayList<Tree>();
	    	Tree tmpBtmBranch = null;
	    	tree.pennPrint();
	    	
	    	TreePrint tp = new TreePrint("typedDependenciesCollapsed");
	    	GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

	          GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
	          System.out.println(gs.typedDependenciesCollapsed());
	          System.out.println();
	          tp.printTree(tree);
	          System.out.println("----------");

//	    	for(Tree leaf:tree){
//	    		if(leaf.i) {
//	    			//tmpBtmBranch = leaf.parent();
//	    			//btmBranchList.add(leaf.parent());
//	    			leaf.pennPrint();
//	    		}
////	    		if(tmpBtmBranch != leaf.parent()){
////	    			//tmpBtmBranch = leaf.parent();
////	    			//btmBranchList.add(leaf.parent());
////	    			leaf.pennPrint();
////	    		}
//	    	}
	    	//printing a tree just for printing.
//	    	TreePrint tp = new TreePrint("penn");
//	    	StringWriter sw = new StringWriter();
//	    	 PrintWriter pw = new PrintWriter(sw);
//	    	 tp.printTree(tree,pw);
//	    	 StringBuffer sb = sw.getBuffer();
//	    	 System.out.println(sb.toString());
//	    	Tree tree = lp.parse(sentence);

	    	 
	    	 //this for loop prints all NP subtrees
//	    	for (Tree subtree: tree)
//	        {
//
//	          if(subtree.label().value().equals("NP"))
//	          {
//	            System.out.println(subtree);
//	          }
//	        }
//
//	    	for(Label l: tree.yield())System.out.println(l.value());
//	    	for(Word w :tree.yieldWords())System.out.println(w);

	    	//tree.it
	    }
		
	}
//	private static Options op = new Options();
//	
//	public static String outputFormat = "penn";
//	public static String outputFormatOptions = "";
//	public static TreePrint treePrint(TreebankLangParserParams tlpParams) {
//	    TreebankLanguagePack tlp = tlpParams.treebankLanguagePack();
//	    return new TreePrint(outputFormat, outputFormatOptions, tlp, tlpParams.headFinder());
//	  }
	
	
}
