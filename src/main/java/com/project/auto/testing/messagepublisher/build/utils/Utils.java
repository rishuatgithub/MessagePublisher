package com.project.auto.testing.messagepublisher.build.utils;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class Utils {

    // returns a json object from a string message
    public JSONObject getJsonFromString(String message){
        JSONObject jsonObject = new JSONObject(message);
        return jsonObject;
    }

    // parse the string schema to avro schema
    public Schema getAvroSchema(String schema){
        Schema.Parser schemaParser = new Schema.Parser();
        Schema payloadSchema = schemaParser.parse(schema);

        return payloadSchema;
    }

    // convert JSON payload to AVRO file format
    public byte[] convertJsontoAvro(String message, String schema) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(message.getBytes());
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        Schema schema1 = getAvroSchema(schema);

        Decoder decoder = DecoderFactory.get().jsonDecoder(schema1,dataInputStream);

        DatumReader<Object> reader = new GenericDatumReader<Object>(schema1);
        Object datum = reader.read(null,decoder);

        GenericDatumWriter<Object> writer = new GenericDatumWriter<Object>(schema1);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        writer.write(datum,encoder);

        encoder.flush();

        return outputStream.toByteArray();
    }

}
