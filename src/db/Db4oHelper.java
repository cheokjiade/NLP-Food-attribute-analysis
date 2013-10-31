package db;

import java.io.IOException;
import java.util.HashMap;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.CommonConfiguration;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ta.TransparentActivationSupport;
import com.db4o.ta.TransparentPersistenceSupport;

import edu.stanford.nlp.ling.Word;
import entities.CombinedWord;
import entities.Corpus;
import entities.Domain;
import entities.Links;

public class Db4oHelper {
	private static Db4oHelper singleton = null;
	private static ObjectContainer db = null;

	protected Db4oHelper(){
	}

	public static Db4oHelper getInstance(){
		if (singleton==null){
			singleton = new Db4oHelper();
		}
		return singleton;
	}

	public static ObjectContainer db() {

		try {
			if (db == null || db.ext().isClosed()) {
				db = Db4oEmbedded.openFile(dbConfig(), "corpus.db4o");
				//We first load the initial data from the database
				//ExercisesLoader.load(context, db);                                         
			}

			return db;

		} catch (Exception ie) {
			ie.printStackTrace();
			return null;
		}
	}
	
	/**
	    * Configure the behavior of the database
	    */

	    private static EmbeddedConfiguration dbConfig() throws IOException {
	           EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
	           
	           configuration.common().objectClass(Corpus.class).cascadeOnUpdate(true);
	           configuration.common().objectClass(Corpus.class).updateDepth(15);
	           configuration.common().objectClass(Domain.class).cascadeOnUpdate(true);
	           configuration.common().objectClass(CombinedWord.class).cascadeOnUpdate(true);
	           configuration.common().objectClass(CombinedWord.class).updateDepth(15);
	           configuration.common().objectClass(Word.class).cascadeOnUpdate(true);
	           configuration.common().objectClass(Word.class).updateDepth(15);
	           configuration.common().objectClass(Links.class).cascadeOnUpdate(true);
	           configuration.common().objectClass(Links.class).updateDepth(15);
	           configuration.common().objectClass(HashMap.class).cascadeOnUpdate(true);
	           
	           configuration.common().objectClass(Corpus.class).cascadeOnDelete(true);
	           configuration.common().add(new TransparentActivationSupport());
	           return configuration;
	    }
}
