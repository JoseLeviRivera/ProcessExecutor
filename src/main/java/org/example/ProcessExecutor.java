package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class ProcessExecutor {
    private static final Logger log = Logger.getLogger(ProcessExecutor.class.getName());


    /**
     * Executes an external program with optional arguments and returns its output in real time.
     *
     * @param executable Path to the executable or script.
     * @param args       Optional arguments.
     * @return Result of the execution.
     * @throws Exception If an error occurs during execution.
     */
    public static ProcessResult execute(String executable, String... args) throws Exception {
        List<String> command = new ArrayList<>();
        command.add(executable);
        command.addAll(Arrays.asList(args));

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true); // Redirect stderr to stdout
        Process process = builder.start();

        StringBuilder outputBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        // Create threads to read output and errors in real time
        Thread outputThread = new Thread(() -> readStream(process.getInputStream(), System.out, outputBuilder));
        Thread errorThread = new Thread(() -> readStream(process.getErrorStream(), System.err, errorBuilder));

        outputThread.start();
        errorThread.start();

        // Wait for process to finish
        int exitCode = process.waitFor();

        // Ensure all output is captured
        outputThread.join();
        errorThread.join();

        return new ProcessResult(outputBuilder.toString().trim(), errorBuilder.toString().trim(), exitCode);
    }

    /**
     * Reads an InputStream line by line and prints it to the provided PrintStream (stdout or stderr).
     *
     * @param inputStream InputStream to read from.
     * @param printStream PrintStream where the output should be displayed.
     * @param outputBuilder StringBuilder to store the output.
     */
    private static void readStream(java.io.InputStream inputStream, PrintStream printStream, StringBuilder outputBuilder) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                printStream.println(line); // Print in real time
                outputBuilder.append(line).append("\n"); // Store output
            }
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }

    /**
         * Container class for the process result.
         */
        public record ProcessResult(String output, String errorOutput, int exitCode) {
    }
}


