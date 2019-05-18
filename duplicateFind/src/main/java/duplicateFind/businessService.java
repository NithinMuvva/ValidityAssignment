package duplicateFind;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;

@Service
public class businessService {
	
	// To get the similar records from the file
	public Map<String,List<User>> getSimilarRecords(File file) throws IOException {
		HashMap<String, Boolean> userDetails =  new HashMap<String, Boolean>(); // To check for which user details are missing
		List<User> duplicates = new ArrayList<User>(); // duplicates List
		List<User> nonDuplicates = new ArrayList<User>(); // non duplicates list
		HashMap<String, List<User>> data  =  new HashMap<>();
		try{
		  ColumnPositionMappingStrategy<User> strategy = new ColumnPositionMappingStrategy<User>();
		  strategy.setType(User.class);
		  String[] columns = new String[] {"id","first_name","last_name","company","email","address1","address2","zip","city","state_long","state","phone"};
		  strategy.setColumnMapping(columns);
		  Reader reader = null; 
		  try { 
			  reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()));
		  } 
		  catch (FileNotFoundException e) { 
		      // TODO Auto-generated catch block 
		      e.printStackTrace(); 
		  } 
		  CsvToBean<User> csv = new CsvToBean<User>();
		  @SuppressWarnings("deprecation")
		  List<User> dataList = csv.parse(strategy, reader);
		  // checking each record for duplication
		  for( User user : dataList) {
			  userDetails.clear();
			  if(!user.getZip().isEmpty()) {
				userDetails.put("zip", true);
			  }
			  if(!user.getCity().isEmpty()) {
				userDetails.put("city", true);
			  }
			  if(!user.getState().isEmpty()) {
				userDetails.put("state", true);
			  }
			  if(!user.getAddress().trim().isEmpty()) {
				userDetails.put("address", true);
			  }
			  if(!user.getCompany().isEmpty()) {
				userDetails.put("company", true);
			  }
			  if(!user.getEmail().isEmpty()) {
				userDetails.put("email", true);
			  }
			  if(!user.getPhone().isEmpty()) {
				userDetails.put("phone", true);
			  }
			  int size = nonDuplicates.size();
			  Boolean found = false;
			  if(size == 0) {
				  // first User
				  nonDuplicates.add(user);
			  }else {
				  for( int i = 0; i < size ; i++) {
					  found = calculateSimilarity(user,nonDuplicates.get(i),userDetails);
					  if(found) {
						  duplicates.add(user);
						  break;
					  }
				  }
				  if(!found) {
					  nonDuplicates.add(user);
				  }
			  }
		  }
		  data.put("similarData",duplicates);
		  data.put("uniqueData",nonDuplicates);
	      return data;
		}catch(Exception e){
		     e.printStackTrace(); 
		     return null;
		}
	}
	public Boolean calculateSimilarity(User u1, User u2, Map<String, Boolean> userDetails) {
		try{
			double score = 0; // score to compare similarity between 2 records
			int count = 0;
			double fullName = caluclateDistance(u1.getFullName(),u2.getFullName());
			// if full name is similar, check for other attributes
			if(fullName < 0.4) {
				 if(userDetails.containsKey("company")) {
					 double company = caluclateDistance(u1.getCompany(),u2.getCompany());
					 count++;
					 if(company < 0.5 ) {
						 score += 25;
					 }
				 }
				 if(userDetails.containsKey("email")) {
					 double email = caluclateDistance(u1.getEmail(),u2.getEmail());
					 count++;
					 if(email <= 0.5) {
						 score += 25;
					 }
				 }
				 if(userDetails.containsKey("phone")) {
					 double maxLen = Math.max(u1.getPhone().length(), u2.getPhone().length());
					 double phone = LevenshteinDistance.getDefaultInstance().apply(u1.getPhone(), u2.getPhone())/maxLen;
					 count++;
					 if(phone < 0.5) {
						 score += 25;		 
					 }
				 }
				 if(userDetails.containsKey("address")) {
					 if(calculateAddress(u1,u2,userDetails)) {
						 score += 25;
					 }
					 count++;
				 }
				 score = score/count;
				 // if any two of the email , Phone , Company or address matches, They are similar
				 if(score >= 12.5) {
					 return true;
				 }
				 return false;
			}
			return false;
		}catch(Exception e){
			e.printStackTrace(); 	
		    return false;
		}
		
	}
	// Checks for address similarity between Users
	public Boolean calculateAddress(User u1 , User u2, Map<String, Boolean> userDetails) {
		try{
			boolean isAddressSimilar =  true;
			double zip = Integer.MAX_VALUE;
			double city = Integer.MAX_VALUE;
			double state = Integer.MAX_VALUE;
			double address = Integer.MAX_VALUE;
			if(userDetails.containsKey("zip")) {
				double maxLen = Math.max(u1.getZip().length(), u2.getZip().length());
				 zip = LevenshteinDistance.getDefaultInstance().apply(u1.getZip(), u2.getZip())/maxLen;
				if(zip > 0.5) {
					isAddressSimilar = false;
				}
			}
			if(userDetails.containsKey("city")) {
				city =caluclateDistance(u1.getCity(),u2.getCity());
				if(city > 0.5) {
					isAddressSimilar = false;
				}
			}
			if(userDetails.containsKey("state")) {
				state = caluclateDistance(u1.getState(),u2.getState());
				if(state > 0.5) {
					isAddressSimilar = false;
				}
			}
			// Zip, State, City should match before checking address for user
			if(isAddressSimilar) {
				address = caluclateDistance(u1.getAddress(),u2.getAddress());
				if(address > 0.5) {
					isAddressSimilar = false;
				}
			}
			return isAddressSimilar;
		}catch(Exception e){
			e.printStackTrace();
		    return false;
		}
	}
	// Calculates distance between two string inputs
	public Double caluclateDistance(String s1, String s2) {
		try{
		    if (s1 == null || s2 == null) {
			throw new IllegalArgumentException("Strings must not be null");
		    }
		    s1 = s1.toLowerCase().replaceAll("[\\W]|_\\-", " ");
		    s2 = s2.toLowerCase().replaceAll("[\\W]|_\\-", " ");
		    
		    StringTokenizer tokenizer = new StringTokenizer(s1);
		    StringTokenizer tokenizer2 = new StringTokenizer(s2);
		    String doubleMetaphone_s1 = ""; 
		    // Using double metaphone to generate encoded value for string
		    DoubleMetaphone metaPhone = new DoubleMetaphone();
		    while (tokenizer.hasMoreTokens()) {
		      doubleMetaphone_s1 += metaPhone.doubleMetaphone(tokenizer.nextToken()) + " "; 
		    }
		    doubleMetaphone_s1 = doubleMetaphone_s1.trim();
		    String doubleMetaphone_s2 = "";
		    while (tokenizer2.hasMoreTokens()) {
		      doubleMetaphone_s2 += metaPhone.doubleMetaphone(tokenizer2.nextToken()) + " "; 
		    }
		    double maxLen = Math.max(doubleMetaphone_s1.length(), doubleMetaphone_s2.length());
		    doubleMetaphone_s2 = doubleMetaphone_s2.trim();  
		    // Calculating Levenshtein distance for metaphonic encodings
		    double distance = LevenshteinDistance.getDefaultInstance().apply(doubleMetaphone_s1, doubleMetaphone_s2)/maxLen;
		    return distance;
		}catch(Exception e){
		     e.printStackTrace(); 
		     return null;
		}		
	} 
}
