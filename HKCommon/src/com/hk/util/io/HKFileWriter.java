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

    /**
     * WARNING: It is caller's responsiblity to clean up resources
     * @param fileName
     * @param extension
     * @return
     */
    public static Writer getFileStream(String fileName, String extension){
        Writer output = null;
        File file = new File(String.format("%s.%s", fileName, extension));
        try{
            output = new BufferedWriter(new FileWriter(file));
        }catch (IOException ie){
            logger.error(String.format("Unable to create file stream for the given file %s ", fileName), ie);
        }
        return output;
    }

    public static void writeToStream(Writer writer, String message){
        try{
            writer.write(message);
            writer.flush();
        }catch (IOException ie){
            logger.error("Unable to write to log", ie);
        }
    }

    public static void close(Writer writer){
        try{
            writer.close();
        }catch (IOException ie){
            logger.error("Unable to close Stream", ie);
        }
    }
}
