package uk.ac.le.cs.CO3098.spring.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.le.cs.CO3098.spring.domain.CountFolderDTO;
import uk.ac.le.cs.CO3098.spring.domain.FolderDomain;
import uk.ac.le.cs.CO3098.spring.domain.LinksDomain;
import uk.ac.le.cs.CO3098.spring.domain.LocationDomain;
import uk.ac.le.cs.CO3098.spring.domain.TextDomain;
import uk.ac.le.cs.CO3098.spring.repository.FolderRepository;

@Controller
@RequestMapping(value = {"/service"})
public class RESTServiceController {
	
	//RepositoryAutowires
	@Autowired 
	FolderRepository FolderRepo;

	@RequestMapping(value = {"/listFolders"})
	public ModelAndView listAllFolders() {
		//Use to return multiple values to the JSP
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<FolderDomain> allFolders = (List<FolderDomain>) FolderRepo.findAll();
		model.put("folderList", allFolders);
		
		return new ModelAndView("ListAllFolders", "model", model);
	}
	
	/*URL Signiture for create parent
		GET /service/create?folder=Documents
		change to return bool
	 */
	@RequestMapping(value = {"/create"}, params = {"folder"})
	@ResponseBody
	public Boolean createParentFolder(@RequestParam(value = "folder") String parentFolderName) {
		
		try {
		if(parentFolderName.equals("/"))
			return false;
			
		FolderDomain parentFolder = new FolderDomain(parentFolderName);
		
		FolderRepo.save(parentFolder);
		
		return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
		
	
	/*
	 	GET /service/create?folder=Books&parent=Documents
		GET /service/create?folder=Fiction&parent=Documents|Books
		GET /service/create?folder=Fiction&parent=Documents%7CBooks

	 */
	@RequestMapping(value = {"/create"}, params = {"folder", "parent"})
	@ResponseBody
	public Boolean createChildFolder(@RequestParam(value = "folder") String FolderName, @RequestParam(value = "parent") String parentFolderPath) {			

		FolderDomain parentFolder = null;
		
		if(FolderName.equals("/"))
			return false;

		try {
		if(parentFolderPath.contains("|")) {
			String parentFolderName="";
			Boolean foundSeparator = false;
						
			for(int i=0; (/*(!foundSeparator)&&*/(i<parentFolderPath.length())); i++) {
				if (!(parentFolderPath.charAt(i)=='|')) {
					parentFolderName = parentFolderName + parentFolderPath.charAt(i);
				}else parentFolderName="";// {
				//	foundSeparator = true;
			//	}
			}

			parentFolder = FolderRepo.findByFolderNameAndPath(parentFolderName,parentFolderPath);
			//Parse the first parent folder and call addChild on it
		}else {
			parentFolder = FolderRepo.findByFolderNameAndPath(parentFolderPath,"/");
		}
		
		if(parentFolderPath.equals("/"))
			parentFolder = FolderRepo.findByFolderNameAndPath(parentFolderPath,"");


		parentFolder.addChildFolder(FolderName, parentFolder.getFolderName());

		FolderRepo.save(parentFolder);
		
		return true;
		
		}catch(Exception e) {
			e.printStackTrace();
			return false;
			
		}
	}
	
	
	///service/delete?folder=Documents|Books
	
	@RequestMapping(value= {"/delete"}, params = {"folder"})
	@ResponseBody
	public Boolean DeleteFolder(@RequestParam(value="folder")String FolderPath) {
		
		FolderDomain parentFolder = null;
		
		if (FolderPath.equals("/"))
			return false;
		
		try {
		
			if(FolderPath.contains("|")) {

				parentFolder = FolderRepo.findByFolderNameAndPath("/","");
				parentFolder.deleteSubFolder(FolderPath);
			
				FolderRepo.save(parentFolder);

			}else if(FolderRepo.countByFolderName(FolderPath)>0) {
				FolderRepo.delete((int) FolderRepo.findByFolderNameAndPath(FolderPath, "").getId());
			};
		
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			
			return false;
}
	}
	
	
	@RequestMapping(value= {"/structure"}, params = {"folder"})
	@ResponseBody
	public FolderDomain outputJSONTreeStructure(@RequestParam(value="folder")String folderName) {
		
		if(folderName.contains("|")) {
			String ChildFolderName="";
						
			for(int i=0; (i<folderName.length()); i++) {
				if (!(folderName.charAt(i)=='|')) {
					ChildFolderName = ChildFolderName + folderName.charAt(i);
				}else {
					ChildFolderName = "";
				}
			}			
			return FolderRepo.findByFolderNameAndPath(ChildFolderName, folderName);

		}else {
			return FolderRepo.findByFolderNameAndPath(folderName,"/");

		}
	}
	
	
	@RequestMapping(value= {"/treeViewStructure"})
	@ResponseBody
	public String ManualTreeViewStructure() {
		
		System.out.println("Fetching text data from service ===========");
		
		return "text: \"Node 1\"";
	}
	
	
	
	
	@RequestMapping(value = {"count"}, params = {"folder"})
	@ResponseBody
	public CountFolderDTO countFolders(@RequestParam(value="folder")String folderPath) {
		CountFolderDTO response = null;
		
		if(folderPath.contains("|")) {
			String ChildFolderName="";
						
			for(int i=0; (i<folderPath.length()); i++) {
				if (!(folderPath.charAt(i)=='|')) {
					ChildFolderName = ChildFolderName + folderPath.charAt(i);
				}else {
					ChildFolderName = "";
				}
			}			
			
			//call count on childs
			response = new CountFolderDTO(FolderRepo.findByFolderNameAndPath(ChildFolderName,folderPath).countdirectFolders(),
					FolderRepo.findByFolderNameAndPath(ChildFolderName,folderPath).countIndirectFolders());
			return response;

		}else {
			
			//call counts methods on parent
			response = new CountFolderDTO(FolderRepo.findByFolderNameAndPath(folderPath,"/").countdirectFolders(),
					FolderRepo.findByFolderNameAndPath(folderPath,"/").countIndirectFolders());
			return response;

		}
		
	}
	
	//GET
	//services/createStructure?tree=[Books[Fiction[Romance|Horror]|NonFiction]]&root=Documents]
	//doesn't like http://localhost:8080/service/createStructure?tree=%5BBooks%5BFiction%5BRomance%7CHorror%7Caction%5D%7CNonFiction%7CData%5Btech%5D%5D%5D&root=home
	
	
	@RequestMapping(value= {"createStructure"}, params = {"tree", "root"})
	@ResponseBody
	public Boolean createStructure(@RequestParam(value="tree")String tree, @RequestParam(value="root")String root) {
		
	
		List<FolderDomain> stack = new ArrayList<FolderDomain>();
		String workingFolder="";
		String parentName="";
		List<String> childFolder = new ArrayList<String>();
		FolderDomain temp = FolderRepo.findByFolderNameAndPath(root, "/");

	//	System.out.println(tree);
		
	//	System.out.println("this is the char at 14" + tree.charAt(14));
		
		
		for(int i= 0; i<tree.length(); i++) {
		//	System.out.println("current index is = "+i);
	//		System.out.println("tree Length is = " +tree.length());
	//		System.out.println("The char is = "+tree.charAt(i));
			
			
			if((tree.charAt(i)!='[')&&(tree.charAt(i)!=']')&&(tree.charAt(i)!='|'))
				workingFolder = workingFolder + tree.charAt(i);
			
			if(tree.charAt(i)=='[') {
				
				
				if(!workingFolder.isEmpty()) {
					
					if (stack.isEmpty()) {
					
						parentName = root;
					}else
						parentName = stack.get((stack.size()-1)).getFolderName();
					
					
		//			System.out.println(parentName);
					stack.add(new FolderDomain(workingFolder,parentName, parentName));
		//			System.out.println("added New Folder");
		//			System.out.println(stack.size());
		//			System.out.println(workingFolder);
					workingFolder = "";
				}
				
			}
			
			if((tree.charAt(i)=='|')&&(!workingFolder.equals(""))) {
				
				
				if (stack.isEmpty())
					parentName = root;
				else if (childFolder.isEmpty())
					parentName = stack.get((stack.size()-1)).getFolderName();
				else {
					parentName = stack.get((stack.size()-1)).getParentFolder();
				}
			
		//		System.out.println(parentName);
		//		System.out.println(workingFolder);
				stack.add(new FolderDomain(workingFolder,parentName, parentName));
				childFolder.add(workingFolder);
				workingFolder = "";
		//		System.out.println(stack.size());
			
			}
			
			
			if((tree.charAt(i)==']')&& (i!=(tree.length()-1))) {
			
				int parentReference =-1;
				
				if(!childFolder.isEmpty()) {
					
		//			System.out.println("Line 275");
					stack.add(new FolderDomain(workingFolder,stack.get(stack.size()-1).getParentFolder(),stack.get(stack.size()-1).getParentPathForChild()));
					
		//			System.out.println("Parent for "+ workingFolder +"is " + stack.get(stack.size()-1).getParentFolder());

					childFolder.add(workingFolder);
		//			System.out.println("current Working folder is "+workingFolder);
					
					for(int j=0; j<childFolder.size(); j++) {
						
			//			System.out.println("childFolder at " +j +" = "+ childFolder.get(j));
						
						for (int x = 0; x< stack.size(); x++) {
							
							if(stack.get(x).getFolderName().equals(childFolder.get(j))) {
								
								if(stack.get(x).getParentFolder().equals(root)) {
								
									temp.addChildFolder(stack.get(x));
									
								}else if(parentReference==-1) {
									stack.get(x-1).addChildFolder(stack.get(x));
									parentReference = x-1;
									}else {
										stack.get(parentReference).addChildFolder(stack.get(x));

									}
							}
							
						}
						
						
					}
					
					parentReference = -1;
					
					
					for(int j=0; j<childFolder.size(); j++) {
						int deleteReference =-1;
						
						for (int x = 0; x< stack.size(); x++) {
							if(stack.get(x).getFolderName().equals(childFolder.get(j))) {
								deleteReference = x;
							}
							
						}
						stack.remove(deleteReference);
						
						deleteReference = -1;
					}
					
					
		//			System.out.println("ChildFolder size is " +childFolder.size());
					childFolder.clear();
		//			System.out.println("ChildFolder size after clear is " +childFolder.size());

				}else {
					
		//			System.out.println("line 318");
					stack.add(new FolderDomain(workingFolder,stack.get(stack.size()-1).getFolderName(),stack.get(stack.size()-1).getFolderName()));
		//			System.out.println(stack.get(stack.size()-1).getParentFolder());
		//			System.out.println(workingFolder);
					
				}
				
		//		System.out.println("current Stack size = " + stack.size());
		//		System.out.println(stack.get(0).getFolderName());
				
				
				if(stack.size()!=1) {
				if (stack.get(stack.size()-1).getParentFolder().equals(stack.get(stack.size()-2).getFolderName())) {
					stack.get(stack.size()-2).addChildFolder(stack.get(stack.size()-1));
					stack.remove(stack.size()-1);
				}
				}
				workingFolder = "";
				
			}
			
			if((tree.charAt(i)==']')&& (i==(tree.length()-1))) {
			
			
			temp.addChildFolder(stack.get(stack.size()-1));
			
			
			}
			
			
		}
		
		FolderRepo.save(temp);
		return true;

	}
	
	@RequestMapping(value= {"addNewLink"}, params= {"folderName", "path" ,"linkName", "linkUrl"})
	@ResponseBody
	public Boolean AddLinks(@RequestParam(value="folderName")String folderName, @RequestParam(value="linkUrl")String linkUrl, @RequestParam(value="linkName")String linkName, @RequestParam(value="path")String path) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);
			LinksDomain newLink = new LinksDomain(linkName,linkUrl);
			
			if(folder.addLink(newLink)) {
				FolderRepo.save(folder);
				return true;
			}else return false;
		}else 
			return false;
	}
	
	@RequestMapping(value= {"editLink"}, params= {"folderName","linkName", "path", "linkUrl"})
	@ResponseBody
	public Boolean editLinks(@RequestParam(value="folderName")String folderName, @RequestParam(value="linkUrl")String linkUrl, @RequestParam(value="linkName")String linkName, @RequestParam(value="path")String path) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);
			
			if(folder.editLinkURL(linkName, linkUrl)) {
				FolderRepo.save(folder);
				return true;
			}else return false;
		}else 
			return false;
	}
	
	@RequestMapping(value= {"deleteLink"}, params= {"folderName","linkName","path"})
	@ResponseBody
	public Boolean deleteLinks(@RequestParam(value="folderName")String folderName, @RequestParam(value="linkName")String linkName, @RequestParam(value="path")String path) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);
			
			if(folder.deleteLink(linkName)) {
				FolderRepo.save(folder);
				return true;
			}else return false;
		}else 
			return false;
	}
	
	
	
	
	@RequestMapping(value= {"getLinksTable"}, params= {"folderName","path"})
	@ResponseBody
	public String getLinksTable(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path) {
		
		String output = "<table class=\"table-hover\"><col width=\"200\"><col width\"400\"><tr><th>Link Name:</th><th>Link Address:</th></tr>";
		String tableEnd = "</table>";
		String trBegin = "<tr>";
		String trEnd = "</tr>";
		String tdBegin = "<td>";
		String tdEnd = "</td>";
		
		String URL;
		String Name;
		
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);
			for(int i=0; i<folder.getLinks().size(); i++) {
				
				URL = folder.getLinks().get(i).getLinkUrl();
				Name = folder.getLinks().get(i).getLinkName();
				
				output = output + trBegin + tdBegin+ Name+tdEnd+tdBegin+"<a href=\"http://"+URL+"\">"+URL+"</a>"+tdEnd+trEnd;
			}
		}
		output = output+tableEnd;
		return output;
	}
	
	@RequestMapping(value= {"getTextTable"}, params= {"folderName", "path"})
	@ResponseBody
	public String getTextTable(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path) {
		
		String output = "<table class=\"table-hover\"><col width=\"150\"><col width\"500\"><tr><th>Filename:</th><th>File Contents:</th></tr>";
		String tableEnd = "</table>";
		String trBegin = "<tr>";
		String trEnd = "</tr>";
		String tdBegin = "<td>";
		String tdEnd = "</td>";
		
		String contents;
		String fileName;
		
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName, path);
			for(int i=0; i<folder.getTextFiles().size(); i++) {
				
				fileName = folder.getTextFiles().get(i).getFileName();
				contents = folder.getTextFiles().get(i).getFileContents();
				
				output = output + trBegin + tdBegin+ fileName+tdEnd+tdBegin+contents+tdEnd+trEnd;
			}
		}
		output = output+tableEnd;
		return output;
	}
	
	
	@RequestMapping(value= {"addNewTextFile"}, params= {"folderName", "path" ,"fileName", "fileContents"})
	@ResponseBody
	public Boolean AddTextFile(@RequestParam(value="folderName")String folderName,@RequestParam(value="path")String path ,@RequestParam(value="fileName")String fileName, @RequestParam(value="fileContents")String fileContents) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);
			TextDomain newText = new TextDomain(fileName,fileContents);
			
			if(folder.addTextFile(newText)) {
				FolderRepo.save(folder);
				return true;
			}else return false;
		}else 
			return false;
	}
	
	@RequestMapping(value= {"editTextFile"}, params= {"folderName","path","fileName", "fileContents"})
	@ResponseBody
	public Boolean editTextFile(@RequestParam(value="folderName")String folderName,@RequestParam(value="path")String path ,@RequestParam(value="fileName")String fileName, @RequestParam(value="fileContents")String fileContents) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);
			
			if(folder.editTextContents(fileName, fileContents)) {
				FolderRepo.save(folder);
				return true;
			}else return false;
		}else 
			return false;
	}
	
	@RequestMapping(value= {"deleteTextFile"}, params= {"folderName","FileName","path"})
	@ResponseBody
	public Boolean deleteTextFile(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path ,@RequestParam(value="FileName")String FileName) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);
			
			if(folder.deleteTextFile(FileName)) {
				FolderRepo.save(folder);
				return true;
			}else return false;
		}else 
			return false;
	}
	
	@RequestMapping(value= {"ajaxGetFileContent"}, params= {"folderName","FileName", "path"})
	@ResponseBody
	public String ajaxGetFileContents(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path ,@RequestParam(value="FileName")String FileName) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);
			return folder.findTextContents(FileName);
		
		}else 
			return "";
	}
	
	@RequestMapping(value= {"ajaxGetURL"}, params= {"folderName","path","LinkName"})
	@ResponseBody
	public String ajaxGetLink(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path ,@RequestParam(value="LinkName")String linkName) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName, path);
			return folder.getLinkURL(linkName);
		
		}else 
			return "";
	}
	
	
	@RequestMapping(value= {"getLocationTable"}, params= {"folderName", "path"})
	@ResponseBody
	public String getLocationTable(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path) {
		
		String output = "<table class=\"table-hover\"><col width=\"200\"><col width=\"100\"><col width=\"100\"><tr><th>Location Name:</th><th>Latitude:</th><th>Longitude:</th></tr>";
		String tableEnd = "</table>";
		String trBegin = "<tr>";
		String trEnd = "</tr>";
		String tdBegin = "<td>";
		String tdEnd = "</td>";
		
		String latitude;
		String longitude;
		String locationName;
		String viewButton;
		
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName, path);
			for(int i=0; i<folder.getAllLocations().size(); i++) {
				
				locationName = folder.getAllLocations().get(i).getLocationName();
				latitude = folder.getAllLocations().get(i).getLatitude();
				longitude = folder.getAllLocations().get(i).getLongitude();
								
				
				if(folderName.equals("/"))
					viewButton = "<button type=\"button\" onclick=\"window.location.href='/home/viewOnMap?locationName="+locationName+"&folderName="+folderName+"'\"class=\"btn btn-default btn-xs\">View</button>";
				else
					viewButton = "<button type=\"button\" onclick=\"window.location.href='/home/viewOnMap?locationName="+locationName+"&path="+folder.getURLEncodedPath()+"&folderName="+folderName+"'\"class=\"btn btn-default btn-xs\">View</button>";
				
				output = output + trBegin + tdBegin+ locationName+tdEnd+tdBegin+latitude+tdEnd+tdBegin+longitude+tdEnd+tdBegin+viewButton+tdEnd+trEnd;
			}
		}
		output = output+tableEnd;
		return output;
	}
	
	@RequestMapping(value= {"addNewLocation"}, params= {"folderName", "path" ,"locationName", "lat", "lng"})
	@ResponseBody
	public Boolean AddLocation(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path ,
			@RequestParam(value="locationName")String locationName, @RequestParam(value="lat")String latitude,
			@RequestParam(value="lng")String longitude) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);
			LocationDomain newLocation = new LocationDomain(locationName, latitude, longitude);
			
			if(folder.addLocation(newLocation)) {
				FolderRepo.save(folder);
				return true;
			}else return false;
		}else 
			return false;
	}
	
	@RequestMapping(value= {"deleteLocation"}, params= {"folderName", "path" ,"locationName"})
	@ResponseBody
	public Boolean AddLocation(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path ,
			@RequestParam(value="locationName")String locationName) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);			
			if(folder.deleteLocation(locationName)) {
				FolderRepo.save(folder);
				return true;
			}else return false;
		}else 
			return false;
	}
	
	@RequestMapping(value= {"editLocation"}, params= {"folderName", "path" ,"locationName", "lat", "lng"})
	@ResponseBody
	public Boolean EditLocation(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path ,
			@RequestParam(value="locationName")String locationName, @RequestParam(value="lat")String latitude,
			@RequestParam(value="lng")String longitude) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);
			
			if(folder.editLocation(locationName, latitude, longitude)) {
				FolderRepo.save(folder);
				return true;
			}else return false;
		}else 
			return false;
	}
	
	@RequestMapping(value= {"getLocationLat"}, params= {"folderName", "path" ,"locationName"})
	@ResponseBody
	public String getLat(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path ,
			@RequestParam(value="locationName")String locationName) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);
			return folder.fetchLocationLatitude(locationName);
			
		}else 
			return "";
	}
	
	@RequestMapping(value= {"getLocationLng"}, params= {"folderName", "path" ,"locationName"})
	@ResponseBody
	public String getLng(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path ,
			@RequestParam(value="locationName")String locationName) {
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName,path);
			return folder.fetchLocationLongitude(locationName);
			
		}else 
			return "";
	}
	
	@RequestMapping(value= {"ajaxGetReadOnly"}, params= {"folderName","path"})
	@ResponseBody
	public String ajaxGetReadOnly(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path) {
			
		FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName, path);

			if (folder.getReadOnly()) 
				return "Yes";
			else return "No";
	
	}
	
	//returns if it was toggled
	@RequestMapping(value= {"ajaxToggle"}, params= {"folderName","path"})
	@ResponseBody
	public Boolean ajaxToggleReadOnly(@RequestParam(value="folderName")String folderName, @RequestParam(value="path")String path) {
			
		if(FolderRepo.countByFolderName(folderName)>0) {
			FolderDomain folder = FolderRepo.findByFolderNameAndPath(folderName, path);
			folder.toggleReadOnly();
			FolderRepo.save(folder);
			return true;
		
		}else 
			return false;
	}
	
	@RequestMapping(value= {"ajaxCountFolderStructure"})//, produces = "application/json")
	@ResponseBody
	public String ajaxCountFolderStructure() {
			
			FolderDomain folder = FolderRepo.findByFolderNameAndPath("/", "");
			return folder.generateFolderStructureArray();
	}
}
