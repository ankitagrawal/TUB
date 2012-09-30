package com.hk.util.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 9/21/12
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class HKFileWriter {
    private static Logger logger = LoggerFactory.getLogger(HKFileWriter.class);

    public static File getFileStream(String path, String fileName, String extension){
        final File file = new File(String.format("%s%s.%s",path, fileName, extension));
        try{
            file.getParentFile().mkdirs();
            file.createNewFile();
        }catch(IOException ie){
            logger.error(String.format("Unable to create file stream for the given file %s ", file.getName()), ie);
        }
        return file;
    }
    /**
     * WARNING: It is caller's responsiblity to clean up resources
     * @param file
     * @return
     */
    public static Writer getFileWriter(File file){
        Writer output = null;
        try{
            output = new BufferedWriter(new FileWriter(file));
        }catch (IOException ie){
            logger.error(String.format("Unable to create file stream for the given file %s ", file.getName()), ie);
        }
        return output;
    }

    public static void writeToStream(Writer writer, String message){
        try{
            writer.write(message);
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }catch (IOException ie){
            logger.error("Unable to write to log", ie);
        }
    }

    public static void close(Writer writer){
        try{
            writer.flush();
            writer.close();
        }catch (IOException ie){
            logger.error("Unable to close Stream", ie);
        }
    }
}
