package com.myproject.test.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Logger {

	
	private ConsoleWriter consoleWriter;
	private Filewritter fileWriter;
	public ConsoleWriter getConsoleWriter() {
		return consoleWriter;
	}
	// its not very flexible
	@Autowired
	public void setConsoleWriter(ConsoleWriter consoleWriter) {
		this.consoleWriter = consoleWriter;
	}
	public Filewritter getFileWriter() {
		return fileWriter;
	}
	
	@Autowired
	public void setFileWriter(Filewritter fileWriter) {
		this.fileWriter = fileWriter;
	}
	
	public void writeFile(String text){
		fileWriter.write(text);
	}
	
	public void writeConsole(String text){
		consoleWriter.write(text);
	}
	
}
