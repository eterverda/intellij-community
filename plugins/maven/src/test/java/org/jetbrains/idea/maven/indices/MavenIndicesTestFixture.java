package org.jetbrains.idea.maven.indices;

import com.intellij.openapi.project.Project;
import com.intellij.util.ArrayUtil;
import org.jetbrains.idea.maven.core.MavenCore;

import java.io.File;

public class MavenIndicesTestFixture {
  private File myDir;
  private Project myProject;
  private String myLocalRepoDir;
  private String[] myExtraRepoDirs;

  private MavenCustomRepositoryTestFixture myRepositoryFixture;
  private MavenProjectIndicesManager myIndicesManager;

  public MavenIndicesTestFixture(File dir, Project project) {
    this(dir, project, "local1", "local2");
  }

  public MavenIndicesTestFixture(File dir, Project project, String localRepoDir, String... extraRepoDirs) {
    myDir = dir;
    myProject = project;
    myLocalRepoDir = localRepoDir;
    myExtraRepoDirs = extraRepoDirs;
  }

  public void setUp() throws Exception {
    myRepositoryFixture = new MavenCustomRepositoryTestFixture(myDir, ArrayUtil.append(myExtraRepoDirs, myLocalRepoDir));
    myRepositoryFixture.setUp();

    for (String each : myExtraRepoDirs) {
      myRepositoryFixture.copy(each, myLocalRepoDir);
    }

    MavenCore.getInstance(myProject).getState().setLocalRepository(myRepositoryFixture.getTestDataPath(myLocalRepoDir));

    getIndicesManager().setTestIndexDir(new File(myDir, "MavenIndices"));
    myIndicesManager = MavenProjectIndicesManager.getInstance(myProject);
    myIndicesManager.doInit();
  }

  public void tearDown() throws Exception {
    getIndicesManager().doShutdown();
  }

  public MavenIndicesManager getIndicesManager() {
    return MavenIndicesManager.getInstance();
  }

  public MavenProjectIndicesManager getProjectIndicesManager() {
    return myIndicesManager;
  }

  public MavenCustomRepositoryTestFixture getRepositoryFixture() {
    return myRepositoryFixture;
  }
}
