package me.xxgradzix.linkaccountsbot.database.persisters;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class LongSetPersister extends StringType {

    private static final LongSetPersister instance = new LongSetPersister();

    public static LongSetPersister getSingleton() {
        return instance;
    }


    protected LongSetPersister() {
        super(SqlType.STRING, new Class<?>[]{HashSet.class});
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        if (javaObject instanceof Set) {
            Set<?> ids = (Set<?>) javaObject;
            return longSetToBase64((HashSet<Long>) ids);
        }
        return null;
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        if (sqlArg instanceof String) {
            String jsonString = (String) sqlArg;
            try {
                return longSetFromBase64(jsonString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
    public static String longSetToBase64(Set<Long> ids) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream dataOutput = new ObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(ids.size());

            // Save every element in the list
            for(Long id : ids) {
                dataOutput.writeLong(id);
            }

            dataOutput.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static Set<Long> longSetFromBase64(String data) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        ObjectInputStream dataInput = new ObjectInputStream(inputStream);
        Set<Long> ids = new HashSet<>();

        int idsLength = dataInput.readInt();
        // Read the serialized inventory
        for (int i = 0; i < idsLength; i++) {
            ids.add(dataInput.readLong());
        }

        dataInput.close();
        return ids;
    }
}