package util;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CountryNames {



	     final static Map<String, String> cityMap = new TreeMap<String, String>   (String.CASE_INSENSITIVE_ORDER);
	
/**
 * Check whether given string is a City name
 * @param country
 * @return return true if the string is a countryname
 */
	     public boolean getCityname(String country){
	    	if(cityMap==null || cityMap.isEmpty())
	    		getCityNames();
	     String countryFound = cityMap.get(country);
	     if(countryFound==null){
	             return true;
	     }
	     return false ;
	     }
		
		/**
		 * Generate a map with City name and code  
		 */

		     public static void getCityNames() {
	     String[] countryCodes = Locale.getISOCountries();
	    
	    for (String countryCode : countryCodes) {

	         Locale obj = new Locale("", countryCode);
	         cityMap.put(obj.getDisplayCountry(),countryCode);
	        
	        
	     }
	    cityMap.put("USA", "USA");
	    cityMap.put("America", "USA");
	    cityMap.put("South Africa", "SA");
	    cityMap.put("London", "LD");
        cityMap.put("California", "CL");
        cityMap.put("Texas", "TX");
        cityMap.put("Montreal", "ML");
        cityMap.put("New York", "NY");
        cityMap.put("Los Angeles", "LA");
        cityMap.put("England", "EG");
        cityMap.put("EUR", "EUR");
        cityMap.put("Pennsylvania", "PV");
        cityMap.put("Alabama", "AB");
        cityMap.put("Louisiana", "LS");
        cityMap.put("Beijing", "BJ");
        cityMap.put("New Jersey", "NY");
        cityMap.put("Argentina", "AG");
        cityMap.put("Lisbon", "LN");
        cityMap.put("Zurich", "ZH");
        
        
		     
		     }
		     
		   
		     }

