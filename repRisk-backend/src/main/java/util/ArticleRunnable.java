package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ArticleRunnable implements Callable<Set<String>> {

	private Set<String> companyNameList;
	private File xmlFile;


	public ArticleRunnable(Set<String> companyNameList,File xmlFile) {
		this.companyNameList = companyNameList;
		this.xmlFile= xmlFile; 
		
	}


	@Override
	public Set<String> call() throws Exception {
		return getCompaniesInArticles(this.xmlFile);
		
	}

/**
 * 
 * Get all Companies present in all  XML
 * @param xmlFile
 * @return Set of companies present in  XML
 */
	private Set<String> getCompaniesInArticles(File xmlFile) {
		Set<String> companyInArticle = new HashSet<>();
		try {
			
			// an instance of factory that gives a document builder
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// an instance of builder to parse the specified xml file
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(this.xmlFile);
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("text");
			// nodeList is not iterable, so we are using for loop

			for (int itr = 0; itr < nodeList.getLength(); itr++) {
				Node node = nodeList.item(itr);
				
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					//split string to get line by line data
					String[] articleString = eElement.getTextContent().split("\n");
					//iterate over all lines in an xml article
					for (String article : articleString) {
						//iterate for all companynames
					for (String company : companyNameList) {
                         //check if companyname already added in the list
							if (!companyInArticle.contains(company)) {
								if (article.contains(company))
								{
								companyInArticle.add(company);
									
								}

							}
						}
					}
				}

		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return companyInArticle;
		
	}

}
