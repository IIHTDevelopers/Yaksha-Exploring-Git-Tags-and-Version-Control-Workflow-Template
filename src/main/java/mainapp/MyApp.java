package mainapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyApp {

    // Method to check if the specified tags exist
    public static String areTagsPresent() {
        try {
            System.out.println("Checking if tags exists...");
            String tags = executeCommand("git tag").trim();

            // Check if all three tags are present
            if (tags.contains("v1.1.0") && tags.contains("v1.1.1") && tags.contains("v1.2.0-beta")) {
                return "true";
            } else {
                return "false";
            }
        } catch (Exception e) {
            System.out.println("Error in areTagsPresent method: " + e.getMessage());
            return "";
        }
    }

    // Method to check if the specified branches exist
    public static String areBranchesPresent() {
        try {
            System.out.println("Checking if branches exists...");
            String branches = executeCommand("git branch").trim();

            // Check if all three branches are present
            if (branches.contains("develop") && branches.contains("hotfix-payment-gateway") && branches.contains("discount")) {
                return "true";
            } else {
                return "false";
            }
        } catch (Exception e) {
            System.out.println("Error in areBranchesPresent method: " + e.getMessage());
            return "";
        }
    }

    // Method to check if the 'discount' branch was created from 'main'
    public static String wasBranchCreatedFromBaseBranch(String baseBranch, String branchName) {
        try {
            System.out.println("Checking if branch '" + branchName + "' was created from '" + baseBranch + "'...");

            // Execute 'git reflog' to get the reflog entries
            String reflog = executeCommand("git reflog").trim();

            // Create the regex pattern to match the 'checkout' entry indicating branch creation from the base branch
            String searchTerm = "checkout: moving from " + baseBranch + " to " + branchName;
            String regex = "checkout: moving from " + baseBranch + " to " + branchName;
            
            // Compile the regex pattern and match it against the reflog
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(reflog);

            // Check if the regular expression matches any line in the reflog
            if (matcher.find()) {
                // System.out.println(branchName + " was created from " + baseBranch);
                return "true";  // The branch was created from the base branch
            } else {
                System.out.println(branchName + " was not created from " + baseBranch);
                return "false";  // The branch was not created from the base branch
            }

        } catch (Exception e) {
            System.out.println("Error in wasBranchCreatedFromBaseBranch method: " + e.getMessage());
            return "";
        }
    }

    public static String isCommitPresentInBranch(String branchName, String commitMessage) {
        try {
            System.out.println("Checking if commit with message '" + commitMessage + "' is present in branch '" + branchName + "'...");

            // Execute 'git log' to get the commit history for the specified branch
            String logOutput = executeCommand("git log " + branchName + " --oneline").trim();
            System.out.println("Log Output for branch " + branchName + ":");

            // Create the regex pattern to match the commit message
            String regex = ".*" + Pattern.quote(commitMessage) + ".*";  // Escape special characters in commitMessage
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(logOutput);

            // Check if the commit message matches any line in the log
            if (matcher.find()) {
                return "true";  // Commit is found in the branch
            } else {
                return "false";  // Commit is not found in the branch
            }

        } catch (Exception e) {
            System.out.println("Error in isCommitPresentInBranch method: " + e.getMessage());
            return "";
        }
    }

    // Helper method to execute git commands
    private static String executeCommand(String command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(".")); // Ensure this is the correct directory where Git repo is located
        processBuilder.command("bash", "-c", command);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitVal = process.waitFor();
        if (exitVal == 0) {
            return output.toString();
        } else {
            System.out.println("Command failed with exit code: " + exitVal);
            throw new RuntimeException("Failed to execute command: " + command);
        }
    }

    // Main method to run the checks manually (can be used to test individually)
    public static void main(String[] args) {
        try {
            // Checking if the tags exist
            String tagsExist = areTagsPresent();
            if (tagsExist.equals("true")) {
                System.out.println("Tags v1.1.0, v1.1.1, and v1.2.0-beta exist.");
            } else {
                System.out.println("One or more tags do not exist.");
            }

            // Checking if the branches exist
            String branchesExist = areBranchesPresent();
            if (branchesExist.equals("true")) {
                System.out.println("Branches develop, hotfix-payment-gateway, and discount exist.");
            } else {
                System.out.println("One or more branches do not exist.");
            }

        } catch (Exception e) {
            System.out.println("Error in main method: " + e.getMessage());
        }
    }
}
