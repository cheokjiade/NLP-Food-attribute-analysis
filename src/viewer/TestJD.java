package viewer;

import java.util.ArrayList;
import java.util.HashSet;

import entities.Domain;

public class TestJD {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> stringList = new ArrayList<String>();
		String a = "their";
		String b = "first";
		stringList.add(a);
		stringList.add(b);
		HashSet<Domain> d = Viewer.findCommonDomains(stringList);
		for(Domain domain :d)System.out.println(domain.getSource());

	}

}
