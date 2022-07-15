package net.cavitos.workshop.search.schema.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.typesense.model.CollectionSchema;

@Getter
@ToString
@AllArgsConstructor
public class SchemaInformation {

    private String name;
    private CollectionSchema collectionSchema;
}
