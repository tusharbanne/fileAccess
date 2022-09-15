package com.fileaccess.controller;

import java.io.IOException;
import java.util.List;

import javax.websocket.server.PathParam;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

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
	
	@GetMapping(value = "/downloadFile")
	public ResponseEntity<?> getFileData(@PathParam("env") String env, @PathParam("fileName") String fileName) throws RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException, IOException, NoHeadException, GitAPIException{
		Resource resource = null;
        try {
            resource = fileAccessService.getFileData(env, fileName);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
         
        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
         
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

		return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);  
	}
	
}
