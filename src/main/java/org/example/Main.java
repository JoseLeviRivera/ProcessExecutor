package org.example;

public class Main {
    public static void main(String[] args) {
        try {
            // Path to Python interpreter
            String pythonExecutable = "C:\\Users\\jrivera\\PycharmProjects\\BackUpCli\\.venv\\Scripts\\python.exe";
            // Path to Python script
            String scriptPath = "C:\\Users\\jrivera\\PycharmProjects\\BackUpCli\\main.py";
            // Argument to pass (directory path)
            String argument = "C:/Users/jrivera/Downloads";

            // Execute Python script
            ProcessExecutor.ProcessResult result = ProcessExecutor.execute(pythonExecutable, scriptPath, argument);

            // Print the output
            System.out.println("Output:\n" + result.getOutput());
            System.err.println("Errors:\n" + result.getErrorOutput());
            System.out.println("Exit Code: " + result.getExitCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}