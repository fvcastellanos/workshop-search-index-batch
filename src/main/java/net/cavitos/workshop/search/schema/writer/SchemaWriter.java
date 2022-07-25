package net.cavitos.workshop.search.schema.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.typesense.api.Client;
import org.typesense.model.CollectionSchema;

import java.util.List;
@Component
public class SchemaWriter implements ItemWriter<CollectionSchema> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaWriter.class);

    private final Client client;

    private final RetryTemplate retryTemplate;

    @Autowired
    public SchemaWriter(final Client client,
                        final RetryTemplate retryTemplate) {

        this.client = client;
        this.retryTemplate = retryTemplate;
    }

    @Override
    public void write(final List<? extends CollectionSchema> collectionSchemas) throws Exception {

        for (final var collectionSchema : collectionSchemas) {

            try {

                LOGGER.info("Creating typesense collection: {}", collectionSchema.getName());

                retryTemplate.execute((retryContext) ->
                        {
                            try {
                                return client.collections().create(collectionSchema);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
            } catch (Exception exception) {

                LOGGER.error("Unable to create typesense collection: {} - ", collectionSchema.getName(), exception);
                throw exception;
            }
        }
    }
}
