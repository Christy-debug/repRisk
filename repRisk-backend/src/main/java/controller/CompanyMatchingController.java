package controller;

import java.io.File;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import delegate.CompanyMatchingDelegate;
import service.Impl.CompanyMatchingServiceImpl;
import util.FilePath;
import util.ResponseBody;





public class CompanyMatchingController {
//	@Autowired
//	CompanyMatchingDelegate companyMatchingDelegate;
	
	


//	@Autowired
//	FilePath filePaths;

	
	public static void main(String[] args) {
		FilePath filePaths = new FilePath();
	
	 File xmlFile = new File(filePaths.article_xml_path);
	 String articlesPath=xmlFile.getAbsolutePath();
		  File csvFile = new File(filePaths.company_names_path);
		 String companyListPath=csvFile.getAbsolutePath();
		 CompanyMatchingDelegate companyMatchingDelegate = new CompanyMatchingDelegate();
		
		ResponseBody responseBody =new ResponseBody();
		Set<String> companyInArticle = companyMatchingDelegate.getCompaniesInArticle
				(articlesPath ,companyListPath);
		if(companyInArticle != null)
		{responseBody.setResponse(companyInArticle);
		responseBody.setResponseCode(200);
		responseBody.setResponseMessage("Success");
		System.out.println(responseBody);

		}
		}
			
	
	}

