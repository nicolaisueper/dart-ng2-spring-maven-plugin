package net.k40s.nico;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static net.k40s.nico.Strings.CAN_T_FIND_PROJECT_ROOT;
import static net.k40s.nico.Strings.MULTIPLE_PUBSPECS;

/**
 * Common methods I use in this plugin.
 *
 * @author Nicolai Süper (nico@k40s.net)
 */
class CommonUtils {

    /**
     * Executes a given command on the host system and prints it's output with the program name prefixed to System.out.
     *
     * @param execDir working dir
     * @param log     logger
     * @param args    cmdstring.split(" ");
     * @throws MojoExecutionException
     */
    static void executeCommand(File execDir, Log log, String... args) throws MojoExecutionException {
        ProcessBuilder b = new ProcessBuilder(args);
        b.directory(execDir);
        b.redirectErrorStream(true);
        String cmd = "";
        for (String s : args) {
            cmd += s + " ";
        }
        System.out.println("[EXEC] \"" + cmd.trim() + "\"");
        try {
            Scanner scanner = new Scanner(b.start().getInputStream()).useDelimiter(System.getProperty("line.separator"));
            while (scanner.hasNext()) {
                System.out.println(args[0] + "> " + scanner.next());
            }
            scanner.close();
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }

    /**
     * Searches for one dart project in the default location.
     *
     * @return dart base dir
     * @throws MojoExecutionException if no ore more than one pubspec was found
     */
    static File getDartBaseDir() throws MojoExecutionException {
        File dartBaseDir = new File("src/main/dart");
        if (!(dartBaseDir.exists() || dartBaseDir.isDirectory())) {
            throw new MojoExecutionException(CAN_T_FIND_PROJECT_ROOT);
        }
        Searchy searchy = new Searchy();
        searchy.searchInDirectory(dartBaseDir, "pubspec.yaml");
        if (Searchy.results.size() < 1) {
            throw new MojoExecutionException(CAN_T_FIND_PROJECT_ROOT);
        } else if (Searchy.results.size() > 1) {
            throw new MojoExecutionException(MULTIPLE_PUBSPECS);
        }
        return new File(Searchy.results.get(0)).getParentFile();
    }


}

/**
 * Searches for elements in a given base dir
 */
class Searchy {
    private String query;
    private List<String> results;

    public Searchy() {
    }

    public Searchy(File baseDir, String query) {
        this.query = query;
        this.searchInDirectory(baseDir, r);
    }

    void searchInDirectory(File baseDir, String q) {
        results = new ArrayList<>();
        query = q;
        search(baseDir);
    }

    @SuppressWarnings("ConstantConditions")
    private void search(File file) {
        if (file.isDirectory() && file.canRead()) {
            for (File f : file.listFiles()) {
                if (f.isDirectory()) {
                    search(f);
                } else {
                    if (query.equals(f.getName())) {
                        results.add(f.getAbsoluteFile().toString());
                    }
                }
            }
        }
    }
}