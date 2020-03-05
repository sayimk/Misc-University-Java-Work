package uk.ac.le.cs.CO3098.spring.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.ac.le.cs.CO3098.spring.domain.FolderDomain;

@Repository
public interface FolderRepository extends CrudRepository<FolderDomain, Integer> {
	
	// return type if multiple use List<> then findBy___(Pass in value)
	FolderDomain findByFolderNameAndPath(String folderName, String path);
	List<FolderDomain> findByParentFolder(String parentFolder);
	int countByFolderName(String folderName);
	int countByFolderNameAndPath(String folderName, String path);
}
