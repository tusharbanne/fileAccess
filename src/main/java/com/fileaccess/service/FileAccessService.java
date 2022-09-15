package com.fileaccess.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fileaccess.helper.LogFollowCommand;
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

	private Path foundFile;

	public List<FileData> getAllFileData() throws RevisionSyntaxException, AmbiguousObjectException,
			IncorrectObjectTypeException, IOException, NoHeadException, GitAPIException {
		String url = RequestConstructHelper.createPath(baseUrl, owner, repo, branch, depth);

		GitTreeData gitTreeData = gitRestUtil.getAllFiles(url);
		List<GitTree> gitTrees = gitTreeData.getTree().stream().filter(t -> t.type.equals("blob"))
				.collect(Collectors.toList());
		List<FileData> fileList = new ArrayList<>();

		Repository repository = git.getRepository();
		File f = Paths.get("files/dev").toFile();
		/*
		 * RevCommit commits =
		 * git.log().add(null).addPath("MongoDBSetup/EmployeeManagement/pom.xml").call()
		 * .iterator().next(); RevCommit commits1 = git.log().addPath(
		 * "MongoDBSetup/EmployeeManagement/src/main/java/com/employee").call().iterator
		 * ().next();
		 * 
		 * LogFollowCommand command = new LogFollowCommand(repository,
		 * "MongoDBSetup/EmployeeManagement/pom.xml"); command.call();
		 * 
		 * RevWalk revWalk = new RevWalk(repository); revWalk.markStart(
		 * revWalk.parseCommit( repository.resolve( Constants.HEAD ) ) );
		 * revWalk.setTreeFilter(PathFilter.create(
		 * "MongoDBSetup/EmployeeManagement/pom.xml")); revWalk.sort(
		 * RevSort.COMMIT_TIME_DESC ); revWalk.sort( RevSort.REVERSE, true ); RevCommit
		 * commit = revWalk.next();
		 * 
		 * Git git = Git.init().setDirectory(new File("/newFold")).call();
		 * git.remoteSetUrl().setRemoteName("https://github.com/tusharbanne");
		 * System.out.println("Created repository: " +
		 * git.getRepository().getDirectory()); File myFile = new
		 * File(git.getRepository().getDirectory().getParent(), "testfile"); if
		 * (!myFile.createNewFile()) { throw new IOException("Could not create file " +
		 * myFile); }
		 * 
		 * // run the add-call git.add().addFilepattern("testfile").call();
		 * 
		 * git.commit().setMessage("Initial commit").call();
		 * System.out.println("Committed file " + myFile + " to repository at " +
		 * git.getRepository().getDirectory()); // Create a few branches for testing for
		 * (int i = 0; i < 10; i++) {
		 * git.checkout().setCreateBranch(true).setName("new-branch" + i).call(); } //
		 * List all branches List<Ref> call = git.branchList().call(); for (Ref ref :
		 * call) { System.out.println("Branch: " + ref + " " + ref.getName() + " " +
		 * ref.getObjectId().getName()); }
		 * 
		 * // Create a few new files for (int i = 0; i < 10; i++) { File fs = new
		 * File(git.getRepository().getDirectory().getParent(), "testfile" + i);
		 * fs.createNewFile(); if (i % 2 == 0) { git.add().addFilepattern("testfile" +
		 * i).call(); } }
		 * 
		 * Spliterator<RevCommit> revComits =
		 * git.log().addPath(f.getAbsolutePath()).call().spliterator();
		 * 
		 * Stream<RevCommit> s = StreamSupport.stream(revComits, false); s.forEach(x->
		 * x.getAuthorIdent());
		 * 
		 * 
		 * ObjectId lastCommitId = repository.resolve(Constants.HEAD); RevWalk revWalk1
		 * = new RevWalk(repository); RevCommit lastCommit =
		 * revWalk1.parseCommit(lastCommitId); PersonIdent authorIdent1 =
		 * lastCommit.getAuthorIdent(); PersonIdent committerIdent1 =
		 * lastCommit.getCommitterIdent(); Date date = new Date(0);
		 */

		gitTrees.forEach(gt -> {
			FileData fileData = new FileData();
			if (gt.getPath().contains("dev")) {
				fileData.setDev(true);
				fileData.setName(gt.getPath().substring(gt.getPath().lastIndexOf('/') + 1));
				// Repository repo = git.
			}
			if (gt.getPath().contains("si")) {
				fileData.setSi(true);
				fileData.setName(gt.getPath().substring(gt.getPath().lastIndexOf('/') + 1));
			}
			if (gt.getPath().contains("prod")) {
				fileData.setProd(true);
				fileData.setName(gt.getPath().substring(gt.getPath().lastIndexOf('/') + 1));
			}
			fileList.add(fileData);
		});

		return fileList;
	}

	public Resource getFileData(String env, String fileName) throws IOException {
		String path = RequestConstructHelper.createPath("files", "/",env);
		Files.list(Paths.get(path)).forEach(file -> {
			if (file.getFileName().toString().startsWith(fileName)) {
				foundFile = file;
				return;
			}
		});

		if (foundFile != null) {
			return new UrlResource(foundFile.toUri());
		}

		return null;
	}

}
