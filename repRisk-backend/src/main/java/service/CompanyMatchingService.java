package service;

import java.io.IOException;
import java.util.Set;



public interface CompanyMatchingService {
	
	 Set<String> getCompanyInArticle(String xml,String csv) throws IOException, Exception;
}
