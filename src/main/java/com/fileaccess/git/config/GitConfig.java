package com.fileaccess.git.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitConfig {

	@Bean
	public Git git() {
		
		Git git = null;
		try {
			Path p = Files.createDirectories(Paths.get("files"));
			File f = p.toFile();
			if(f.list().length > 0) {
				//git = Git.open("https://github.com/tusharbanne/documents", f);
			}
			else {
				git = Git.cloneRepository()
						  .setURI("https://github.com/tusharbanne/documents")
						  .setDirectory(f)
						  .call();
				
				git.pull().call();
			}
			
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return git;
	}
	
	
}
