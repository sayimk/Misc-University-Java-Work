package uk.ac.le.cs.CO3098.spring.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;



@Entity
public class FolderDomain {
		
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;
	
    @Column
	private String folderName;
	
    @Column (name = "parent_folder")
	private String parentFolder;
    
    @Column (name = "path")
	private String path;
    
    @Column (name = "Read_Only")
	private Boolean readOnly;
    
    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<FolderDomain> childFolders = new ArrayList<FolderDomain>();
	
    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<LinksDomain> links = new ArrayList<LinksDomain>();
    
    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<TextDomain> textFiles = new ArrayList<TextDomain>();
    
    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private List<LocationDomain> locations = new ArrayList<LocationDomain>();
    
	public FolderDomain(){
		folderName = "";
		parentFolder = "";
		path="";
		readOnly = false;
	}
	
	public FolderDomain(String folderName, String parentFolder, String path) {
		this.folderName = folderName;
		this.parentFolder = parentFolder;
		this.path = path;
		readOnly = false;
	}
	
	public String getParentPathForChild() {
		
		return path+"|"+folderName;
	}
	
	public long getId() {
		return id;
	}
	
	public void setReadOnly(boolean TrueOrFalse) {
		readOnly = TrueOrFalse;
	}
	
	public boolean getReadOnly() {
		return readOnly;
	}
	
	public void toggleReadOnly() {
		if(readOnly) {
			readOnly = false;
		}else {
			readOnly = true;
		}
	}
	
	public FolderDomain(String folderName) {
		this.folderName = folderName;
		this.parentFolder = "/";
		path="/";
	}
	
	public String getFolderName() {
		return folderName;
	}
	
	public String getParentFolder() {
		return parentFolder;
	}
	
	public boolean addChildFolder(FolderDomain childFolderName) {
		childFolders.add(childFolderName);
		return true;
	}
	
	public String getURLEncodedPath() {
		String output="";
		
		for(int i=0; i<path.length(); i++) {
			if(path.charAt(i)=='|') {
				output = output+"%7C";
			}else
				output = output+path.charAt(i);
		}
		
		return output;
	}
	
	//NOTE: parentPath MUST be in this format root|Sub1|Sub2 for Parsing
	public boolean addChildFolder(String childFolderName, String parentPath) {

		List<String> parentPathList = parseParentPath(parentPath);
		String Sublist = "";
		FolderDomain newFolder;
		
		if (readOnly)
			return false;
		
		if((parentPathList.get(0).equals(folderName))&&(parentPathList.size()==1)) {
			System.out.println("foldername is "+parentPath);

			
			if(folderName.equals("/"))
				newFolder = new FolderDomain(childFolderName, folderName, ("/|"+childFolderName));
			else
				newFolder = new FolderDomain(childFolderName, folderName, (path+"|"+childFolderName));
			
			System.out.println("foldername is "+parentPath);
			addChildFolder(newFolder);
		}else if ((parentPathList.size()>1)){

			for (int i = 1; i<parentPathList.size(); i++) {
				if (i!=1) {
					Sublist = Sublist+"|";
				}
				
				Sublist = Sublist+parentPathList.get(i);
			}
			
			
			if (doesChildFolderExist(parentPathList.get(1))) {				
				getSpecificSubFolder(parentPathList.get(1)).addChildFolder(childFolderName, Sublist);
			}
		}
		
		return true;
	}
	
	private List<String> parseParentPath(String parentPath){
		
		String folderName="";
		List<String> parentList = new ArrayList<String>();
		
		for (int i= 0; i<parentPath.length(); i++) {
			
			if(parentPath.charAt(i)=='|') {
				parentList.add(folderName);
				System.out.println(i+folderName);
				folderName = "";
			}else {
				folderName = folderName+parentPath.charAt(i);
			}
			if(i==(parentPath.length()-1)) {
				parentList.add(folderName);
			}
		}
		
		return parentList;
	}
	
	public void removeChildFolder(String folderName) {
		
		boolean deleted = false;
		String childFolderName= "";
		
		for(int i=0; i < childFolders.size(); i++) {
			
			childFolderName = childFolders.get(i).getFolderName();
			
			if ((!deleted) && (childFolderName.equals(folderName))) {
				childFolders.remove(i);
				deleted = true;
			}
		}
	}
	
	public void setParentFolder(String folderName) {
		this.parentFolder = folderName;
	}
	
	public List<FolderDomain> getAllSubFolders(){
		return childFolders;
	}
	
	public boolean doesChildFolderExist(String folderName) {
		
		boolean found = false;
		String childFolderName= "";
		
		for(int i=0; i < childFolders.size(); i++) {
			
			childFolderName = childFolders.get(i).getFolderName();
			
			if ((!found) && (childFolderName.equals(folderName))) {
				found = true;
			}
		}
		return found;
	}
	
	public FolderDomain getSpecificSubFolder(String folderName) {
		
		String childFolderName= "";

		for(int i=0; i < childFolders.size(); i++) {
			
			childFolderName = childFolders.get(i).getFolderName();
			
			if (childFolderName.equals(folderName)) {
				return childFolders.get(i);
			}
		}
		
		return null;
	}
	
	public boolean deleteSubFolder(String FolderPath) {
		
		List<String> parentPathList = parseParentPath(FolderPath);
		
		if (readOnly)
			return false;
		
		if (parentPathList.size()==2) {
			if(parentPathList.get(0).equals(folderName)) {
				if(doesChildFolderExist(parentPathList.get(1))) {
					System.out.println("Deleting folder");
					childFolders.remove(getSpecificSubFolder(parentPathList.get(1)));
					return true;
				}
			}
		}else if (doesChildFolderExist(parentPathList.get(1))) {
			String Sublist = "";
			System.out.println("Going recusive");
			for (int i = 1; i<parentPathList.size(); i++) {
				if (i!=1) {
					Sublist  = Sublist+"|";
				}
				Sublist = Sublist+parentPathList.get(i);
			}
			System.out.println(Sublist);
			getSpecificSubFolder(parentPathList.get(1)).deleteSubFolder(Sublist);
		}
		return false;
	}
	
	public int countdirectFolders() {
		return childFolders.size();
	}
	
	public int countIndirectFolders() {

		int size =0;
		
		for (int i=0; i<childFolders.size(); i++) {
			if(childFolders.get(i).hasSubFolders())
				size = size+childFolders.get(i).countSubFolders();
		}
		
		
		return size;
	}
	
	public int countSubFolders() {
		
		int size =0;
		
		for(int i=0; i<childFolders.size(); i++) {
			size = size +1;
			if(childFolders.get(i).hasSubFolders()) {
				size = size+childFolders.get(i).countSubFolders();
			}
		}
		
		return size;
	}
	
	
	public boolean hasSubFolders() {
		return (!childFolders.isEmpty());
	}
	
	public String  convertToJSONString() {
		
		String begin = "[{text: ";
		String end = "}]";
		String addChild =", nodes: ";
		String output = begin+"\""+folderName+"\", selectable: true";
		String pathPair = ", path: \""+path+"\"";
		
		if (!childFolders.isEmpty()) {
			output = output+addChild;
			
			for(int i =0; i<childFolders.size(); i++) {
				
				if (childFolders.size()==1) {
					output = output+childFolders.get(i).convertToJSONString();

				}else if(i == 0) {
					output = output+childFolders.get(i).JSONFirstChildString();
				}else if(i!=(childFolders.size()-1)) {
					output = output+childFolders.get(i).JSONChildString();
				}else if (i==(childFolders.size()-1)) {
					output = output+childFolders.get(i).JSONEndChildString();
				}
				
				
				if(i!=(childFolders.size()-1)) {
					output = output+", ";
				}
			}
		}
		output = output+pathPair;
		output =output+end;
		return output;
	}
	
	private String JSONFirstChildString() {
		String begin = "[{text: ";
		String childEnd = "}";
		String addChild =", nodes: ";
		String output;
		String pathPair = ", path: \""+path+"\"";

			output = begin+"\""+folderName+"\", selectable: true";
		
		if (!childFolders.isEmpty()) {
			output = output+addChild;
			
			for(int i =0; i<childFolders.size(); i++) {
				
				if (childFolders.size()==1) {
					output = output+childFolders.get(i).convertToJSONString();

				}else if(i == 0) {
					output = output+childFolders.get(i).JSONFirstChildString();
				}else if(i!=(childFolders.size()-1)) {
					output = output+childFolders.get(i).JSONChildString();
				}else if (i==(childFolders.size()-1)) {
					output = output+childFolders.get(i).JSONEndChildString();
				}
				
				
				if(i!=(childFolders.size()-1)) {
					output = output+", ";
				}
			}
		}
		
		output = output+pathPair;
		output = output+childEnd;
		return output;
	}
	
	private String JSONChildString() {
		String begin = "{text: ";
		String childEnd = "}";
		String addChild =", nodes: ";
		String output;
		String pathPair = ", path: \""+path+"\"";

			output = begin+"\""+folderName+"\", selectable: true";
		
		if (!childFolders.isEmpty()) {
			output = output+addChild;
			
			for(int i =0; i<childFolders.size(); i++) {
				
				if (childFolders.size()==1) {
					output = output+childFolders.get(i).convertToJSONString();

				}else if(i == 0) {
					output = output+childFolders.get(i).JSONFirstChildString();
				}else if(i!=(childFolders.size()-1)) {
					output = output+childFolders.get(i).JSONChildString();
				}else if (i==(childFolders.size()-1)) {
					output = output+childFolders.get(i).JSONEndChildString();
				}
				
				
				if(i!=(childFolders.size()-1)) {
					output = output+", ";
				}
			}
		}
		
		output = output+pathPair;
		output = output+childEnd;
		return output;
	}
	
	private String JSONEndChildString() {
		String begin = "{text: ";
		String childEnd = "}]";
		String addChild =", nodes: ";
		String output;
		String pathPair = ", path: \""+path+"\"";

			output = begin+"\""+folderName+"\", selectable: true";
		
		if (!childFolders.isEmpty()) {
			output = output+addChild;
			
			for(int i =0; i<childFolders.size(); i++) {
				
				if (childFolders.size()==1) {
					output = output+childFolders.get(i).convertToJSONString();

				}else if(i == 0) {
					output = output+childFolders.get(i).JSONFirstChildString();
				}else if(i!=(childFolders.size()-1)) {
					output = output+childFolders.get(i).JSONChildString();
				}else if (i==(childFolders.size()-1)) {
					output = output+childFolders.get(i).JSONEndChildString();
				}
				
				
				if(i!=(childFolders.size()-1)) {
					output = output+", ";
				}
			}
		}
		output = output+pathPair;
		output = output+childEnd;
		return output;
	}
	
	//Links Methods
	
	public boolean addLink(LinksDomain newLink) {
		
		if (readOnly)
			return false;
		
		for (int i = 0; i<links.size(); i++) {
			if (newLink.getLinkName().equals(links.get(i).getLinkName()))
				return false;
		}
		
		links.add(newLink);
		return true;
	}
	
	public boolean deleteLink(String linkName) {
		
		if (readOnly)
			return false;
		
		int foundLinkID=-1;
		for (int i = 0; i<links.size(); i++) {
			if (linkName.equals(links.get(i).getLinkName()))
				foundLinkID = i;
		}
		
		if (foundLinkID==-1) {
			return false;
		}else {
			links.remove(foundLinkID);
			return true;
		}
			
	}
	
	public boolean editLinkURL(String LinkName, String NewLink) {
		
		int foundLinkID=-1;
		
		if (readOnly)
			return false;
		
		for (int i = 0; i<links.size(); i++) {
			if (LinkName.equals(links.get(i).getLinkName()))
				foundLinkID = i;
		}
		
		if (foundLinkID==-1) {
			return false;
		}else {
			links.get(foundLinkID).setLinkURL(NewLink);
			return true;
		}
	}

	public List<LinksDomain> getLinks() {
		return links;
	}
	
	public boolean addTextFile(TextDomain newText) {
	
		if (readOnly)
			return false;
		
	for (int i = 0; i<textFiles.size(); i++) {
		if (newText.getFileName().equals(textFiles.get(i).getFileName()))
				return false;
		}
		
		textFiles.add(newText);
		return true;
	}
	
	public boolean deleteTextFile(String fileName) {
		
		int foundTextID=-1;
		
		if (readOnly)
			return false;
		
		for (int i = 0; i<textFiles.size(); i++) {
			if (fileName.equals(textFiles.get(i).getFileName()))
				foundTextID = i;
		}
		
		if (foundTextID==-1) {
			return false;
		}else {
			textFiles.remove(foundTextID);
			return true;
		}
			
	}
	
	public boolean editTextContents(String fileName, String newContent) {
		
		int foundTextID=-1;
		
		if (readOnly)
			return false;
		
		for (int i = 0; i<textFiles.size(); i++) {
			if (fileName.equals(textFiles.get(i).getFileName()))
				foundTextID = i;
		}
		
		if (foundTextID==-1) {
			return false;
		}else {
			textFiles.get(foundTextID).setFileContents(newContent);
			return true;
		}
	}

	public List<TextDomain> getTextFiles() {
		return textFiles;
	}
	
	public String findTextContents(String fileName) {
		
		int foundTextID=-1;
		for (int i = 0; i<textFiles.size(); i++) {
			if (fileName.equals(textFiles.get(i).getFileName()))
				foundTextID = i;
		}
		
		if (foundTextID==-1) {
			return "";
		}else {
			return textFiles.get(foundTextID).getFileContents();
		}
	}

	public String getLinkURL(String LinkName) {
		
		int foundLinkID=-1;
		for (int i = 0; i<links.size(); i++) {
			if (LinkName.equals(links.get(i).getLinkName()))
				foundLinkID = i;
		}
		
		if (foundLinkID==-1) {
			return "";
		}else {
			return links.get(foundLinkID).getLinkUrl();
		}
	}
	
	public boolean addLocation(LocationDomain newLocation) {
		
		if (readOnly)
			return false;
		
		for (int i = 0; i<locations.size(); i++) {
			if (newLocation.getLocationName().equals(locations.get(i).getLocationName()))
					return false;
			}
			
			locations.add(newLocation);
			return true;
		}
	
	public boolean editLocation(String locationName, String newLatitude, String newLongitude) {
		
		int foundLocationID=-1;
		
		if (readOnly)
			return false;
		
		for (int i = 0; i<locations.size(); i++) {
			if (locationName.equals(locations.get(i).getLocationName()))
				foundLocationID = i;
		}
				
		if (foundLocationID==-1) {
			return false;
		}else {
			locations.get(foundLocationID).setLatitude(newLatitude);
			locations.get(foundLocationID).setLongitude(newLongitude);
			return true;
		}
	}
	
	public boolean deleteLocation(String locationName) {
		
		int foundLocationID=-1;
		
		if (readOnly)
			return false;
		
		for (int i = 0; i<locations.size(); i++) {
			if (locationName.equals(locations.get(i).getLocationName()))
				foundLocationID = i;
		}
		
		if (foundLocationID==-1) {
			return false;
		}else {
			locations.remove(foundLocationID);
			return true;
		}
			
	}
	
	
	public String getLocationJSON(String locationName) {
		
		int foundLocationID=-1;
		for (int i = 0; i<locations.size(); i++) {
			if (locationName.equals(locations.get(i).getLocationName()))
				foundLocationID = i;
		}
		
		if (foundLocationID==-1) {
			return "";
		}else {
			return locations.get(foundLocationID).getLatLngJSON();
			
		}
			
	}
	
	public String fetchLocationLatitude(String locationName) {
		
		int foundLocationID=-1;
		for (int i = 0; i<locations.size(); i++) {
			if (locationName.equals(locations.get(i).getLocationName()))
				foundLocationID = i;
		}
		
		if (foundLocationID==-1) {
			return "";
		}else {
			return locations.get(foundLocationID).getLatitude();
		}
	}
	
	
	public String fetchLocationLongitude(String locationName) {
		
		int foundLocationID=-1;
		for (int i = 0; i<locations.size(); i++) {
			if (locationName.equals(locations.get(i).getLocationName()))
				foundLocationID = i;
		}
		
		if (foundLocationID==-1) {
			return "";
		}else {
			return locations.get(foundLocationID).getLongitude();
		}
	}
	
	public List<LocationDomain> getAllLocations() {
		return locations;
	}
	
	public String generateFolderStructureArray() {
		
		String output ="[[";
		int subFolders = childFolders.size();
		int totalLinks = links.size();
		int totalFiles = textFiles.size();
		int totalLocations = locations.size();
		//int totalIndirectFolders = countIndirectFolders();
		output = output +"\"Folder\", \"Parent\", \"Contents (size)\"], [";
		output = output +"\""+folderName+"\", ";
		output = output +"\""+parentFolder+"\", ";
		output = output +(subFolders+totalLinks+totalFiles+totalLocations)+"]";
		
		if(childFolders.size()!=0)
			output = output + ", ";
		
		for(int i=0; i<childFolders.size(); i++) {
			output = output + childFolders.get(i).subFolderStructureArray();
			if(i!=(childFolders.size()-1))
				output=output +", ";
		}
		System.out.println(output+"]");		
		return (output+"]");
	}
	
	public String subFolderStructureArray() {
		
		String output ="[";
		int subFolders = childFolders.size();
		int totalLinks = links.size();
		int totalFiles = textFiles.size();
		int totalLocations = locations.size();
		//int totalIndirectFolders = countIndirectFolders();
		
		output = output +"\""+folderName+"\", ";
		output = output +"\""+parentFolder+"\", ";
		output = output +(subFolders+totalLinks+totalFiles+totalLocations);
		output= output+"]";
		
		if(childFolders.size()!=0)
			output = output + ", ";
		
		for(int i=0; i<childFolders.size(); i++) {
			output = output + childFolders.get(i).subFolderStructureArray();
			if(i!=(childFolders.size()-1))
				output=output +", ";
		}
		
		return (output);
	}

}
