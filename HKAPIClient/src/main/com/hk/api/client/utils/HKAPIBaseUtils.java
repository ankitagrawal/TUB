package com.hk.api.client.utils;


import com.hk.api.client.constants.EnumHKAPIErrorCode;
import com.hk.api.client.constants.HKAPIOperationStatus;
import com.hk.api.client.dto.HKAPIBaseDto;
import com.hk.api.client.exception.HKAPIErrorResponseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.codehaus.jackson.*;

import java.io.*;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 2:26 PM
 */
public class HKAPIBaseUtils {

    public static String md5Hash(String src,String salt, int hashIterations){
       return new Md5Hash(src, salt, hashIterations).toBase64();
    }

    public static <T> T fromJSON(Class<T> clazz, String json){
        try{
            JsonFactory jsonFactory=new JsonFactory();
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
                if(StringUtils.isEmpty(baseDto.getMessage())){
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
