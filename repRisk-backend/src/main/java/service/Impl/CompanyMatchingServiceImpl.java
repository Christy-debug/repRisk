package service.Impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import service.CompanyMatchingService;
import util.ArticleRunnable;
import util.CountryNames;
import util.FilePath;

@Configurable
@Service
public class CompanyMatchingServiceImpl implements CompanyMatchingService {

	Set<Future<Set<String>>> ListOfCompanyInArticle = new HashSet<>();

	Set<String> companyNameSet = new HashSet<>();

	public Set<String> getCompanyInArticle(String xml, String csv) throws Exception {

		Set<String> companyList = getCompanyNamesFromCsv(csv);
		if (null != companyList && !companyList.isEmpty()) {
			companyNameSet = callingThreads(companyList, xml);
		}
		// ll companies present in articles are written to the file in the given file
		// path
		try {
			FilePath filePath = new FilePath();
			Files.write(Paths.get(filePath.companies_in_article_path), companyNameSet);

		} catch (IOException e) {
			throw e;
			// System.out.println("Unable to write out names");
		}
		return companyNameSet;

	}

	/**
	 * Calling multiple threads to get the list of companies present in XML files
	 * 
	 * @param companyList
	 * @param xml
	 * @return list of companies in all articles
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */

	private Set<String> callingThreads(Set<String> companyList, String xml)
			throws InterruptedException, ExecutionException {

		final int MAX_T = FilePath.MAX_T; // Thread count
		ExecutorService executor = Executors.newFixedThreadPool(MAX_T);
		File folder = new File(xml);

		File[] files = folder.listFiles();
		// iterate over all xml files
		for (File xmlFile : files) {
			Callable<Set<String>> callable = new ArticleRunnable(companyList, xmlFile);

			Future<Set<String>> future = executor.submit(callable);

			ListOfCompanyInArticle.add(future);
		}
		// iterate over all thread result and add to the Set companyNameSet
		for (Future<Set<String>> s : ListOfCompanyInArticle) {
			try {
				companyNameSet.addAll(s.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw e;
			} catch (ExecutionException e) {
				e.printStackTrace();
				throw e;
			}
		}
		// pool shutdown
		executor.shutdown();
		return companyNameSet;
	}

	/**
	 * Get all company names from CSV file
	 * 
	 * @param csv
	 * @return list of all companies from csv file (including abbrevation name)
	 * @throws Exception
	 */
	private static Set<String> getCompanyNamesFromCsv(String csv) throws Exception {
		Set<String> companyNamesList = new HashSet<>();
		try {

			FileReader filereader = new FileReader(csv);
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			List<String[]> allData = csvReader.readAll();

			for (String[] data : allData) {
				String string = new String(data[0]);
				string = string.replaceAll("(?i)(formerly known as|also known as|please refer to|formerly)", "");
				string = string.trim();

				string = formatQuotedName(string);

				string = getDataInBracket(string);

				String[] names_list = string.split(";", 5);
				for (String name : names_list) {
					name = name.trim();
					if (name.isEmpty() || isNumeric(name) || isValid(name)) {
						continue;
					} else {
						companyNamesList.add(" " + name.trim() + " ");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return companyNamesList;
	}

	static Pattern numeric_pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

	/**
	 * To check whether a string is numeric or lenght less than 3
	 * 
	 * @param strNum
	 * @return false if string is numeric
	 */
	static boolean isNumeric(String strNum) {
		if (strNum == null || strNum.length() < 3) {
			return true;
		} else {
			return numeric_pattern.matcher(strNum).matches();

		}
	}

	static Pattern extra_names_pattern = Pattern.compile("\\(([^)]*)\\)$");
	static CountryNames countryNamesObj = new CountryNames();

	/**
	 * Format the given string to get the data given in () at the end
	 * 
	 * @param string
	 * @return string with all data in () append to the string after ";"
	 */
	static String getDataInBracket(String string) {
		String extra_names = new String();
		Matcher matcher = extra_names_pattern.matcher(string);
		if (matcher.find()) {
			if (countryNamesObj.getCityname(matcher.group(1)))
				extra_names = extra_names + ";" + matcher.group(1);

			string = string.replaceAll("\\(([^)]*)\\)$", "");
			string = string.trim();
			string = getDataInBracket(string);

		}

		return string + extra_names;
	}

	static Pattern quoted_names_pattern = Pattern.compile("\"(([^\"])*)\"");

	/**
	 * format the string which is given in between "" and avoid common company
	 * abbrevations like "OOO","ZAO"
	 * 
	 * @param string
	 * @return
	 */
	static String formatQuotedName(String string) {
		String[] extra_names = new String[2];
		Matcher matcher = quoted_names_pattern.matcher(string);
		if (matcher.find()) {
			string = matcher.group(1);
			extra_names = string.split(";");
			return extra_names[0];
		} else
			return string;
	}

	/**
	 * Check whether company name is valid
	 * 
	 * @param s
	 * @return return if string is invalid
	 */
	public static boolean isValid(String s) {
		if (s.equalsIgnoreCase("The") || s.equalsIgnoreCase("Ltd") || s.equalsIgnoreCase("Private")
				|| s.equalsIgnoreCase("New") || s.equalsIgnoreCase("Group") || s.equalsIgnoreCase("Development")
				|| s.equalsIgnoreCase("Engineering")) {
			return true;
		} else
			return false;
	}

}
