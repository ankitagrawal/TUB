package com.hk.api.client.utils;


import com.hk.api.client.constants.EnumHKAPIErrorCode;
import com.hk.api.client.constants.HKAPIOperationStatus;
import com.hk.api.client.dto.HKAPIBaseDto;
import com.hk.api.client.exception.HKAPIErrorResponseException;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 2:26 PM
 */
public class HKAPIBaseUtils {

    private static String PREFERRED_ENCODING="UTF-8";
    private static String PREFERRED_DIGEST_ALGORITHM="MD5";

    public static String md5Hash(String src,String salt, int hashIterations){
        //to-do write program to handle salt and multiple iterations
        String digestString="";
        try{
            MessageDigest md = MessageDigest.getInstance(PREFERRED_DIGEST_ALGORITHM);
            byte[] srcBytes = src.getBytes(PREFERRED_ENCODING);
            byte[] base64Digest=Base64.encodeBase64(md.digest(srcBytes));

            digestString=new String(base64Digest, PREFERRED_ENCODING);
        }catch (NoSuchAlgorithmException e){
            //no need to do this -MD5 exists
        }catch (UnsupportedEncodingException e){

        }
        return digestString;
       //return new Md5Hash(src, salt, hashIterations).toBase64();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static <T> T fromJSON(Class<T> clazz, String json){
        try{
            JsonFactory jsonFactory=new JsonFactory();
            jsonFactory.setCodec(new ObjectMapper());
            JsonParser  jsonParser=jsonFactory.createJsonParser(json);
            return jsonParser.readValueAs(clazz);

        }catch (JsonParseException e){

        }catch (IOException e){

        }
        return null;
    }

    public static String toJSON(Object o){
        try{
            JsonFactory jsonFactory=new JsonFactory();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JsonGenerator jsonGenerator=jsonFactory.createJsonGenerator(baos);
            jsonGenerator.writeObject(o);
            jsonGenerator.close();
            return baos.toString();

        }catch (JsonParseException e){

        }catch (IOException e){

        }
        return null;
    }

    public static void checkForErrors(HKAPIBaseDto baseDto){
        if(baseDto.getStatus().equals(HKAPIOperationStatus.ERROR)){
            if(baseDto.getErrorCode()==0){
                if(HKAPIBaseUtils.isEmpty(baseDto.getMessage())){
                    throw new HKAPIErrorResponseException(EnumHKAPIErrorCode.getMessageFromErrorCode(baseDto.getErrorCode()));
                }else{
                    throw new HKAPIErrorResponseException(baseDto.getMessage());
                }
            }
        }
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
