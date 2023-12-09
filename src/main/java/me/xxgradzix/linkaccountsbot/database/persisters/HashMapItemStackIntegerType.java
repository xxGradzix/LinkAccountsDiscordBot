package me.xxgradzix.linkaccountsbot.database.persisters;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;

public class HashMapItemStackIntegerType extends StringType {

    private static final HashMapItemStackIntegerType instance = new HashMapItemStackIntegerType();

    private HashMapItemStackIntegerType() {
        super(SqlType.STRING, new Class<?>[]{HashMap.class});
    }

    public static HashMapItemStackIntegerType getSingleton() {
        return instance;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        if (javaObject != null && javaObject instanceof HashMap) {
            HashMap<?, ?> hashMap = (HashMap<?, ?>) javaObject;
            return encodeItemStackMap((HashMap<String, Integer>) hashMap);
        }
        return null;
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        if (sqlArg != null && sqlArg instanceof String) {
            String jsonString = (String) sqlArg;
            return decodeItemStackMap(jsonString);
        }
        return null;
    }
    private static String encodeItemStackMap(HashMap<String, Integer> itemStackMap) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream dataOutput = new ObjectOutputStream(outputStream);

            dataOutput.writeObject(itemStackMap);

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static HashMap<String, Integer> decodeItemStackMap(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            ObjectInputStream dataInput = new ObjectInputStream(inputStream);

            try {
                return (HashMap<String, Integer>) dataInput.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}