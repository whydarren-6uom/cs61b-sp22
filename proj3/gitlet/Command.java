package gitlet;

import java.io.File;
import java.util.*;
import static gitlet.Utils.*;
import static gitlet.Files.*;

public class Command {
    static void init() {
        initDir();
        Objects initCommit = new Objects();
        writeObject(initCommit);
    }

    static void add(String file) {
        File newFile = join(file);
        Objects blob = new Objects(readContentsAsString(newFile), file);
        writeObject(blob);
    }

    static void commit(String msg) {
        Objects toStageFiles = readObject(INDEX, Objects.class);
        Objects stageRemove = readObject(INDEX_REMOVE, Objects.class);
        if (toStageFiles.index.isEmpty() && stageRemove.index.isEmpty()) {
            throw error("No changes added to the commit");
        }
        Objects currHead = getCurrCommit();
        currHead.updateIndex(toStageFiles, stageRemove);
        currHead.makeCommit(msg);

        Files.writeObject(currHead);

        toStageFiles.index.clear();
        stageRemove.index.clear();
        writeObject(INDEX_REMOVE, toStageFiles);
        writeObject(INDEX, toStageFiles);
    }

    static void rm(String file) {

    }

    static void log() {
        Objects curr = getCurrCommit();
        StringBuilder content = new StringBuilder();
        String currHead = getCurrHead();

        while (!curr.getParent().equals("")) {
            content.append("=== \n")
                    .append("commit ").append(currHead).append("\n")
                    .append("Date: ").append(curr.getTimestamp()).append("\n")
                    .append(curr.getMsg()).append("\n\n");
            currHead = curr.getParent();
            curr = readObject(getObjectsFile(curr.getParent()), Objects.class);
        }
        content.append("=== \n")
                .append("commit ").append(currHead).append("\n")
                .append("Date: ").append(curr.getTimestamp()).append("\n")
                .append(curr.getMsg()).append("\n\n");

        System.out.println(content);
    }

    static void globalLog() {
        List<String> allCommitHistory = plainFilenamesIn(BRANCHES);
        if (allCommitHistory == null) {
            return;
        }
        for (String commit : allCommitHistory) {
            Objects curr = getCommitGeneral(commit);
            StringBuilder content = new StringBuilder();
            String currHead = getCurrHead();

            while (!curr.getParent().equals("")) {
                content.append("=== \n")
                        .append("commit ").append(currHead).append("\n")
                        .append("Date: ").append(curr.getTimestamp()).append("\n")
                        .append(curr.getMsg()).append("\n\n");
                currHead = curr.getParent();
                curr = readObject(getObjectsFile(curr.getParent()), Objects.class);
            }

            content.append("=== \n")
                    .append("commit ").append(currHead).append("\n")
                    .append("Date: ").append(curr.getTimestamp()).append("\n")
                    .append(curr.getMsg()).append("\n\n");

            System.out.println(content);
        }
    }

    static void find(String commitmsg) {

    }

    static void status() {

    }

    static void checkoutPastFile(String sha1, String file) {
        String currHead = readContentsAsString(CURR_HEAD);

        List<String> pastCommits = pastCommits(currHead);
        File pastFile = null;

        if (pastCommits.contains(sha1)) {
            pastFile = getObjectsFile(sha1);
        }
        if (pastFile == null) {
            throw error("No such commit exists.");
        }
        Objects commit = readObject(pastFile, Objects.class);
        if (!commit.index.containsKey(file)) {
            throw error("File does not exist in that commit");
        }

        Index ver = commit.index.get(file);
        File newFileCWD = join(file);
        updateRepoFile(newFileCWD, ver.getSha1());
    }

    static void checkoutHeadFile(String file) {
        Objects commit = getCurrCommit();

        if (!commit.index.containsKey(file)) {
            throw error("File does not exist in that commit.");
        }
        String hash = commit.index.get(file).getSha1();
        File newFileCWD = join(file);
        Files.updateRepoFile(newFileCWD, hash);
    }

    static void checkoutBranch(String branch) {

    }

    static void branch(String branch) {

    }

    static void rmBranch(String branch) {

    }

    static void reset(String hash) {

    }

    static void mergeCheck(String branch) {

    }

    static void merge(String branch) {

    }

    static void mergeCommit(Objects commit, String givenBranch,
                            String givenHash)
    {

    }

    static void printMergeConf(LinkedList<String> fileList, Objects curr, Objects given) {

    }
}