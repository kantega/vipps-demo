package no.kantega.vippsdemo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.bson.types.ObjectId;

/**
 * The product class reflects an object which can be ordered.
 * A product holds a name, a description and a price.
 */
@Document(collection = "products")
public class Product {

    @Id
    private ObjectId id;
    private float price;
    private String name;
    private String description;

    public Product(String name, String description, float price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public ObjectId getId() { return id; }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns the object as a JSON formatted string.
     */
    public String toString() {
        return String.format("{\"id\": \"%s\", \"name\": \"%s\", \"description\": \"%s\", \"price\": \"%s\"}",
                getId(),
                getName(),
                getDescription(),
                getPrice());
    }
}