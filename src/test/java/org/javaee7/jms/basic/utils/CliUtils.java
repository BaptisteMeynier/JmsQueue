package org.javaee7.jms.basic.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.scriptsupport.CLI;
import org.jboss.as.protocol.StreamUtils;

public class CliUtils {

	private CliUtils() {
		
	}
	
	  public static int processCliFile(String host, File file) {
	        CLI cli = CLI.newInstance();
	        try {
	            cli.connect(host, 9990, null, null);
	            CommandContext commandContext = cli.getCommandContext();
	            BufferedReader reader = null;
	            try {
	                reader = new BufferedReader(new FileReader(file));
	                String line = reader.readLine();
	                while (commandContext.getExitCode() == 0 && !commandContext.isTerminated() && line != null) {
	                    commandContext.handleSafe(line.trim());
	                    line = reader.readLine();
	                }
	                return commandContext.getExitCode();
	            } catch (Throwable e) {
	                throw new IllegalStateException("Failed to process file '" + file.getAbsolutePath() + "'", e);
	            } finally {
	                StreamUtils.safeClose(reader);
	            }
	        } finally {
	            cli.disconnect();
	        }
	    }

	
}
