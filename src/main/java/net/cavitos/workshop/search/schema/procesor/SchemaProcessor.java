package net.cavitos.workshop.search.schema.procesor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.typesense.api.Client;
import org.typesense.model.CollectionResponse;
import org.typesense.model.CollectionSchema;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SchemaProcessor implements ItemProcessor<CollectionSchema, CollectionSchema> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaProcessor.class);

    private final Client client;
    private final List<String> collections;

    @Autowired
    public SchemaProcessor(final Client client) throws Exception {

        this.client = client;
        this.collections = buildCollectionsNameList();
    }

    @Override
    public CollectionSchema process(final CollectionSchema collectionSchema) throws Exception {

        final var collectionName = collectionSchema.getName();
        if (collectionNameExists(collectionSchema.getName())) {

            LOGGER.info("Collection: {} already exists, not necessary to process", collectionName);
            return null;
        }

        LOGGER.info("Collection: {} doesn't exists, will be processed", collectionName);
        return collectionSchema;
    }

    private List<String> buildCollectionsNameList() throws Exception {

        try {
            return Arrays.stream(client.collections().retrieve())
                    .map(CollectionResponse::getName)
                    .collect(Collectors.toList());

        } catch (Exception exception) {

            LOGGER.error("Unable to retrieve typesense collection list - ", exception);
            throw exception;
        }
    }

    private boolean collectionNameExists(final String name) {

        return collections.stream()
                .anyMatch(collectionName -> collectionName.equalsIgnoreCase(name));
    }
}
