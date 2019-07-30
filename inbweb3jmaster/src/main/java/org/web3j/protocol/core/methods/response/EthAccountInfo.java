package org.web3j.protocol.core.methods.response;

import android.util.JsonReader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;

import org.json.JSONObject;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;

/**
 * db_getString.
 */
public class EthAccountInfo extends Response<Object> {

    public Object getAccountInfo() throws TransactionException {
        if(getResult() == null){
            throw new TransactionException(getError().getMessage());
        }
        return getResult();
    }

    public static class ResponseDeserialiser extends JsonDeserializer<AccountInfo> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public AccountInfo deserialize(JsonParser jsonParser,
                                       DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, AccountInfo.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
