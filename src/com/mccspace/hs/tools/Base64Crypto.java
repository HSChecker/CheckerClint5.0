package com.mccspace.hs.tools;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import org.apache.commons.codec.binary.Base64;

import java.io.*;

/**
 * Base64Crypto��
 * Git to�� http://hs.mccspace.com:3000/Qing_ning/CheckerServer5.0/
 *
 * @TIME 2020/6/4 14:50
 * @AUTHOR ��˶~
 */

public class Base64Crypto {

    /**
     * <p>���ļ�ת��base64 �ַ���</p>
     *
     * @param path �ļ�·��
     */
    public static String encodeBase64File(String path){
        try {
            File file = new File(path);
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return new BASE64Encoder().encode(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>��base64�ַ����뱣���ļ�</p>
     */
    public static void decoderBase64File(String base64Code, String targetPath){
        try {
            byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
            FileOutputStream out = new FileOutputStream(targetPath);
            out.write(buffer);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>��base64�ַ������ı��ļ�</p>
     */
    public static void toFile(String base64Code, String targetPath){
        try {
            byte[] buffer = base64Code.getBytes();
            FileOutputStream out = new FileOutputStream(targetPath);
            out.write(buffer);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
