package org.gdd.sage.http.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

public class SageStatisticsDeserializer extends JsonDeserializer<SageStatistics> {
    @Override
    public SageStatistics deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        double exportTime = node.get("export").asDouble();
        double importTime = node.get("import").asDouble();
        SageStatistics sageStatistics = new SageStatistics(exportTime, importTime);
        ArrayNode cardsNode = (ArrayNode) node.get("cardinalities");
        for(JsonNode cardNode: cardsNode) {
            JsonNode tripleNode = cardNode.get("triple");
            sageStatistics.addTriple(
                    tripleNode.get("subject").asText(),
                    tripleNode.get("predicate").asText(),
                    tripleNode.get("object").asText(),
                    cardNode.get("cardinality").asInt());
        }
        return sageStatistics;
    }
}
