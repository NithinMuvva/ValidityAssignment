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
	
	public Map<String,List<User>> getSimilarRecords(File file) throws IOException {
		HashMap<String, Boolean> check =  new HashMap<String, Boolean>();
		List<User> duplicates = new ArrayList<User>();
		List<User> nonDuplicates = new ArrayList<User>();
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
		  List<User> list = csv.parse(strategy, reader);
		  
		  for( User myUser : list) {
			  check.clear();
			  if(myUser.getId().equals("47")) {
				  System.out.println("sa");
			  }
			  if(!myUser.getZip().isEmpty()) {
				check.put("zip", true);
			  }
			  if(!myUser.getCity().isEmpty()) {
				check.put("city", true);
			  }
			  if(!myUser.getState().isEmpty()) {
				check.put("state", true);
			  }
			  if(!myUser.getAddress().trim().isEmpty()) {
				check.put("address", true);
			  }
			  if(!myUser.getCompany().isEmpty()) {
				check.put("company", true);
			  }
			  if(!myUser.getEmail().isEmpty()) {
				check.put("email", true);
			  }
			  if(!myUser.getPhone().isEmpty()) {
				check.put("phone", true);
			  }
			  int size = nonDuplicates.size();
			  Boolean found = false;
			  if(size == 0) {
				  nonDuplicates.add(myUser);
			  }else {
				  for( int i = 0; i < size ; i++) {
					  found = calculateSimilarity(myUser,nonDuplicates.get(i),check);
					  if(found) {
						  duplicates.add(myUser);
						  break;
					  }
				  }
				  if(!found) {
					  nonDuplicates.add(myUser);
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
	public Boolean calculateSimilarity(User u1, User u2, Map<String, Boolean> check) {
		try{
			double score = 0;
			int c = 0;
			double fullName = calDist(u1.getFullName(),u2.getFullName());
			if(fullName < 0.4) {
				 if(check.containsKey("company")) {
					 double company = calDist(u1.getCompany(),u2.getCompany());
					 c++;
					 if(company < 0.5 ) {
						 score += 25;
					 }
				 }
				 if(check.containsKey("email")) {
					 double email = calDist(u1.getEmail(),u2.getEmail());
					 c++;
					 if(email <= 0.5) {
						 score += 25;
					 }
				 }
				 if(check.containsKey("phone")) {
					 double maxLen = Math.max(u1.getPhone().length(), u2.getPhone().length());
					 double phone = LevenshteinDistance.getDefaultInstance().apply(u1.getPhone(), u2.getPhone())/maxLen;
					 c++;
					 if(phone < 0.5) {
						 score += 25;		 
					 }
				 }
				 if(check.containsKey("address")) {
					 if(calAddress(u1,u2,check)) {
						 score += 25;
					 }
					 c++;
				 }
				 score = score/c;
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
	public Boolean calAddress(User u1 , User u2, Map<String, Boolean> check) {
		try{
			boolean adrsSimilar =  true;
			double zip = Integer.MAX_VALUE;
			double city = Integer.MAX_VALUE;
			double state = Integer.MAX_VALUE;
			double address = Integer.MAX_VALUE;
			if(check.containsKey("zip")) {
				double maxLen = Math.max(u1.getZip().length(), u2.getZip().length());
				 zip = LevenshteinDistance.getDefaultInstance().apply(u1.getZip(), u2.getZip())/maxLen;
				if(zip > 0.5) {
					adrsSimilar = false;
				}
			}
			if(check.containsKey("city")) {
				city =calDist(u1.getCity(),u2.getCity());
				if(city > 0.5) {
					adrsSimilar = false;
				}
			}
			if(check.containsKey("state")) {
				state = calDist(u1.getState(),u2.getState());
				if(state > 0.5) {
					adrsSimilar = false;
				}
			}
			if(adrsSimilar) {
				address = calDist(u1.getAddress(),u2.getAddress());
				if(address > 0.5) {
					adrsSimilar = false;
				}
			}
			return adrsSimilar;
		}catch(Exception e){
			e.printStackTrace();
		    return false;
		}
	}
	public Double calDist(String s1, String s2) {
		try{
		    if (s1 == null || s2 == null) {
			throw new IllegalArgumentException("Strings must not be null");
		    }
		    s1 = s1.toLowerCase().replaceAll("[\\W]|_\\-", " ");
		    s2 = s2.toLowerCase().replaceAll("[\\W]|_\\-", " ");
		    
		    StringTokenizer tokenizer = new StringTokenizer(s1);
		    StringTokenizer tokenizer2 = new StringTokenizer(s2);
		    String s1dm = ""; 
		    DoubleMetaphone m = new DoubleMetaphone();
		    while (tokenizer.hasMoreTokens()) {
		      s1dm += m.doubleMetaphone(tokenizer.nextToken()) + " "; 
		    }
		    s1dm = s1dm.trim();
		    String s2dm = "";
		    while (tokenizer2.hasMoreTokens()) {
		      s2dm += m.doubleMetaphone(tokenizer2.nextToken()) + " "; 
		    }
		    double maxLen = Math.max(s1dm.length(), s2dm.length());
		    s2dm = s2dm.trim();  
		    double distance = LevenshteinDistance.getDefaultInstance().apply(s1dm, s2dm)/maxLen;
		    return distance;
		}catch(Exception e){
		     e.printStackTrace(); 
		     return null;
		}		
	} 
}
