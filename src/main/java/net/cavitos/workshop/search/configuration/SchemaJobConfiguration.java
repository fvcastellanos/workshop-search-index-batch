package net.cavitos.workshop.search.configuration;

import net.cavitos.workshop.search.schema.listener.JobCompletionNotificationListener;
import net.cavitos.workshop.search.schema.procesor.SchemaProcessor;
import net.cavitos.workshop.search.schema.reader.SchemaItemReader;
import net.cavitos.workshop.search.schema.writer.SchemaWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.typesense.model.CollectionSchema;

@Configuration
@EnableBatchProcessing
public class SchemaJobConfiguration {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public JsonItemReader<CollectionSchema> collectionSchemaJsonItemReader() {

        final var builder = new JsonItemReaderBuilder<CollectionSchema>();

        return builder.maxItemCount(50)
                .name("collectionSchemaReader")
                .jsonObjectReader(new JacksonJsonObjectReader<>(CollectionSchema.class))
                .resource(new ClassPathResource("schema/collection-schema.json"))
                .build();
    }

    @Bean
    public Step typesenseSchemaConfigurationStep(final JsonItemReader<CollectionSchema> collectionSchemaJsonItemReader,
                                                 final SchemaProcessor schemaProcessor,
                                                 final SchemaWriter schemaWriter) {

        return stepBuilderFactory.get("typesenseSchemaConfigurationStep")
                .<CollectionSchema, CollectionSchema> chunk(10)
                .reader(collectionSchemaJsonItemReader)
                .processor(schemaProcessor)
                .writer(schemaWriter)
                .build();
    }

    @Bean
    public Job typesenseSchemaConfigrationJob(final JobCompletionNotificationListener jobCompletionNotificationListener,
                                              final Step typesenseSchemaConfigurationStep) {

        return jobBuilderFactory.get("typesenseSchemaConfigrationJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .flow(typesenseSchemaConfigurationStep)
                .end().build();
    }

}
