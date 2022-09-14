package com.fileaccess.controller;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fileaccess.model.FileData;
import com.fileaccess.service.FileAccessService;

@RestController
public class FileAccessController {

	
	@Autowired
	private FileAccessService fileAccessService;
	
	@GetMapping("/allFiles")
	public List<FileData> getAllFilesData() throws RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException, IOException, NoHeadException, GitAPIException{
		return fileAccessService.getAllFileData();
	}
	
	
}
