package controller;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import delegate.CompanyMatchingDelegate;
import exception.NotFoundException;
import service.Impl.CompanyMatchingServiceImpl;
import util.FilePath;
import util.ResponseBody;

public class CompanyMatchingController {
	
	public static void main(String[] args) throws NotFoundException {
		FilePath filePaths = new FilePath();
	
	 File xmlFile = new File(filePaths.article_xml_path);
	 String articlesPath=xmlFile.getAbsolutePath();
		  File csvFile = new File(filePaths.company_names_path);
		 String companyListPath=csvFile.getAbsolutePath();
		 CompanyMatchingDelegate companyMatchingDelegate = new CompanyMatchingDelegate();
		
		ResponseBody responseBody =new ResponseBody();
		Set<String> companyInArticle=new HashSet<>();
		try {
			companyInArticle = companyMatchingDelegate.getCompaniesInArticle
					(articlesPath ,companyListPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new NotFoundException("Failed to get the company names from article" ,e);
			
		}
		
		if(companyInArticle != null && !companyInArticle.isEmpty())
		{responseBody.setResponse(companyInArticle);
		responseBody.setResponseCode(200);
		responseBody.setResponseMessage("Company names listed successfully");
		System.out.println(responseBody.getResponseCode());
		System.out.println(responseBody.getResponseMessage());
		

		}
		else {
			
			responseBody.setResponseCode(404);
			responseBody.setResponseMessage("Unable to find company names in articles");
			System.out.println(responseBody.getResponseCode());
			System.out.println(responseBody.getResponseMessage());
		}
		}
			
	
	}

