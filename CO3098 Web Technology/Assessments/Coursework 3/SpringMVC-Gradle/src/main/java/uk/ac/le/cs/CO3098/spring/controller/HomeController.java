package uk.ac.le.cs.CO3098.spring.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.le.cs.CO3098.spring.domain.FolderDomain;
import uk.ac.le.cs.CO3098.spring.repository.FolderRepository;

@Controller
@RequestMapping(value = {"/home"})
public class HomeController {
	
	//RepositoryAutowires
	@Autowired 
	FolderRepository FolderRepo;
	
	
	//Use "/home/" to get to the Bookmarks Homepage
	@RequestMapping()
	public ModelAndView BookMarkHome() {
		Map<String, Object> model = new HashMap<String, Object>();	
		
		if(FolderRepo.count()==0) {
			FolderDomain root = new FolderDomain("/","","");
			FolderRepo.save(root);
		}
			
		model.put("text", FolderRepo.findByFolderNameAndPath("/","").convertToJSONString());
		return new ModelAndView("home", "model", model);
	}
	
	@RequestMapping(value= {"/viewOnMap"}, params= {"folderName", "path", "locationName"})
	public ModelAndView ViewOnMap(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path ,
			@RequestParam(value="locationName")String locationName) {
		Map<String, Object> model = new HashMap<String, Object>();	
		
		if(folderName.equals("/")) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName, "");
			model.put("location", folder.getLocationJSON(locationName));
			model.put("locationTitle", "'"+locationName+"'");
			
			return new ModelAndView("ViewOnMap", "model", model);
		}
		
		

		if (FolderRepo.countByFolderNameAndPath(folderName, path)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName, path);
			model.put("location", folder.getLocationJSON(locationName));
			model.put("locationTitle", "'"+locationName+"'");
			
			return new ModelAndView("ViewOnMap", "model", model);
		}else {
			return new ModelAndView("redirect:/home/");
		}
	}
	
	
	@RequestMapping(value= {"/viewOnMap"}, params= {"folderName", "locationName"})
	public ModelAndView ViewRootOnMap(@RequestParam(value="folderName")String folderName,
			@RequestParam(value="locationName")String locationName) {
		Map<String, Object> model = new HashMap<String, Object>();	
		
		if(folderName.equals("/")) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName, "");
			model.put("location", folder.getLocationJSON(locationName));
			model.put("locationTitle", "'"+locationName+"'");
			
			return new ModelAndView("ViewOnMap", "model", model);
		}else return new ModelAndView("redirect:/home/");

	}
	
	@RequestMapping(value= {"/chart"})
	public ModelAndView dummyChart() {
		
			return new ModelAndView("DummyChart");

	}

}
