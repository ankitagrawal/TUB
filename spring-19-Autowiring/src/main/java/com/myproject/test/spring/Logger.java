package com.myproject.test.spring;

public class Logger {

	
	private ConsoleWriter consoleWriter;
	private Filewritter fileWriter;
	public ConsoleWriter getConsoleWriter() {
		return consoleWriter;
	}
	public void setConsoleWriter(ConsoleWriter consoleWriter) {
		this.consoleWriter = consoleWriter;
	}
	public Filewritter getFileWriter() {
		return fileWriter;
	}
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
