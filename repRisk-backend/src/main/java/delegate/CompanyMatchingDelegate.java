package delegate;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import service.CompanyMatchingService;
import service.Impl.CompanyMatchingServiceImpl;

public class CompanyMatchingDelegate {
	@Autowired
	CompanyMatchingService companyMatchingService ;

	public Set<String>	getCompaniesInArticle(String xml,String csv) throws Exception{
		CompanyMatchingServiceImpl companyMatchingService = new CompanyMatchingServiceImpl();
		return companyMatchingService.getCompanyInArticle(xml, csv);
	}
}
