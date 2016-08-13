package net.k40s.nico;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.k40s.nico.Strings.*;

/**
 * Compiles and copies dart+ng2 source files to a particular folder.
 *
 * @author Nicolai Süper (nico@k40s.net)
 */
@Mojo(name = "run")
public class PubCompilerMojo extends AbstractMojo {

    /**
     * The dart project root.
     * <p>
     * This param is optional, the default value is "src/main/dart".
     */
    @Parameter
    private File projectRoot;

    /**
     * The output directory for the compiled dart project.
     * <p>
     * This param is optional too, the default value is "src/main/resources/static".
     */
    @Parameter
    private File targetDir;

    private Log log = getLog();

    /**
     * Determines the dart project dir, if not specified, and compiles your sources to the target dir, which is "src/main/resources/static" by default.
     *
     * @throws MojoExecutionException if something goes wrong.
     */
    public void execute() throws MojoExecutionException {

        if (targetDir == null) {
            targetDir = new File(DEFAULT_TARGET);
        }
        if (projectRoot == null) {
            projectRoot = CommonUtils.getDartBaseDir();
        }
        if (projectRoot == null) {
            log.error(CAN_T_FIND_PROJECT_ROOT);
            throw new MojoExecutionException(CAN_T_FIND_PROJECT_ROOT);
        }

        if (targetDir.isDirectory() && targetDir.listFiles() != null) {
            log.info("Cleaning up target directory \"" + targetDir.getAbsolutePath() + "\"");
            try {
                FileUtils.forceDelete(targetDir);
                FileUtils.forceMkdir(targetDir);
            } catch (IOException e) {
                throw new MojoExecutionException(ERROR_CLEANING_UP, e);
            }
        }

        CommonUtils.executeCommand(projectRoot, log, "pub", "build", "-o", targetDir.getAbsolutePath());

        File[] files = new File(targetDir.getAbsolutePath() + "/web").listFiles();
        List<File> fileArrayList = new ArrayList<>();
        if (files != null) {
            fileArrayList = Arrays.asList(files);
            for (File f : fileArrayList) {
                CommonUtils.executeCommand(targetDir, log, "mv", "web/" + f.getName(), targetDir.getAbsolutePath());
            }
            CommonUtils.executeCommand(targetDir, log, "rm", "-rf", "web/");
        } else {
            log.error(EMPTY_DIR + targetDir.getAbsolutePath());
            throw new MojoExecutionException(fileArrayList, EMPTY_DIR, UNABLE_TO_BUILD);
        }
    }
}