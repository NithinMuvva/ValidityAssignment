package duplicateFind;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Controller
public class businessControlller {
	
	@Autowired
	private businessService bService;
	@RequestMapping(value="/")
	public String index() {
		return "index";	
	}

	@RequestMapping(value="/getSimilarRecords",method=RequestMethod.POST,produces="application/json")
	public @ResponseBody Map<String,List<User>> getSimilarRecords(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException{
	    Map<String,List<User>> data = new HashMap<>();
	    String path = System.getProperty("user.dir");
	    File temp_file = new File(path +"/src/main/resources"+"/"+file.getOriginalFilename());
	    file.transferTo(temp_file);
	    try{
	    	data = bService.getSimilarRecords(temp_file);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return data;	
	}
	
}
