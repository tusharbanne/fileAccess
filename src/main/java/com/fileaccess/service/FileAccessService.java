package com.fileaccess.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Spliterator;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.ServiceUnavailableException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fileaccess.helper.RequestConstructHelper;
import com.fileaccess.model.FileData;
import com.fileaccess.model.GitTree;
import com.fileaccess.model.GitTreeData;
import com.fileaccess.rest.GitRestUtil;

@Service
public class FileAccessService {

	@Autowired
	private Git git;
	
	@Autowired
	private GitRestUtil gitRestUtil;
	
	@Value("${git.url}")
	private String baseUrl;
	
	@Value("${git.repo}")
	private String repo;
	
	@Value("${git.owner}")
	private String owner;
	
	@Value("${git.branch}")
	private String branch;
	
	@Value("${git.depth}")
	private String depth;
	
	public List<FileData> getAllFileData() throws RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException, IOException, NoHeadException, GitAPIException{
		String url = RequestConstructHelper.getRepoUrl(baseUrl, owner, repo, branch, depth);

		GitTreeData gitTreeData = gitRestUtil.getAllFiles(url);
		List<GitTree> gitTrees = gitTreeData.getTree().stream().filter(t -> t.type.equals("blob")).collect(Collectors.toList());
		List<FileData> fileList = new ArrayList<>();
		
		
		Repository repository = git.getRepository();
		File f = Paths.get("/files/dev/").toFile();
		RevCommit commits = git.log().addPath("D:/Code_downloads/fileaccess/files/dev/Doc2.csv").call().iterator().next();
	
		RevWalk revWalk = new RevWalk( repository );
		revWalk.markStart( revWalk.parseCommit( repository.resolve( Constants.HEAD ) ) );
		revWalk.setTreeFilter(PathFilter.create( "D:/Code_downloads/fileaccess/files/dev/Doc2.csv" ) );
		revWalk.sort( RevSort.COMMIT_TIME_DESC );
		revWalk.sort( RevSort.REVERSE, true );
		RevCommit commit = revWalk.next();
		
		
		/*
		 * Spliterator<RevCommit> revComits =
		 * git.log().addPath(f.getAbsolutePath()).call().spliterator();
		 * 
		 * Stream<RevCommit> s = StreamSupport.stream(revComits, false); s.forEach(x->
		 * x.getAuthorIdent());
		 */
		
		ObjectId lastCommitId = repository.resolve(Constants.HEAD);
		RevWalk revWalk1 = new RevWalk(repository);
		RevCommit lastCommit = revWalk1.parseCommit(lastCommitId);
		PersonIdent authorIdent1 = lastCommit.getAuthorIdent();
		PersonIdent committerIdent1 = lastCommit.getCommitterIdent();
		Date date = new Date(0);
		
		
		gitTrees.forEach(gt -> {
			FileData fileData = new FileData();
			if(gt.getPath().contains("dev")) {
				fileData.setDev(true);
				fileData.setName(gt.getPath().substring(gt.getPath().lastIndexOf('/')+1));
				//Repository repo = git.
			}
			if(gt.getPath().contains("si")) {
				fileData.setSi(true);
				fileData.setName(gt.getPath().substring(gt.getPath().lastIndexOf('/')+1));
			}
			if(gt.getPath().contains("prod")) {
				fileData.setProd(true);
				fileData.setName(gt.getPath().substring(gt.getPath().lastIndexOf('/')+1));
			}
			fileList.add(fileData);
		});
		
		return fileList;
	}
	
}
