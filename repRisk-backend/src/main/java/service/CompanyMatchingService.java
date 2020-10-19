package service;

import java.util.Set;



public interface CompanyMatchingService {
	
	 Set<String> getCompanyInArticle(String xml,String csv);
}
