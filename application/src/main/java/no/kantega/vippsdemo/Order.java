package no.kantega.vippsdemo;

import java.util.List;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import org.bson.types.ObjectId;


@Document(collection = "orders")
public class Order {

    @Id
    private ObjectId id;
    private String userId;
    @NotNull
    private String status;
    @Nullable
    private String authToken;

    @DBRef
    private List<Product> orderProducts = new ArrayList<>();

    // for deserialisation
    public Order() {
        this.status = "created";
    }

    public Order(String userId){
        this.userId = userId;
        this.status = "created";
    }

    public String getUserId() {
        return userId;
    }

    public List<Product> getAllProducts(){
        return orderProducts;
    }

    public ObjectId getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTotalOrderPrice(){
        float sum = 0F;
        for (Product p : this.orderProducts) {
            sum += p.getPrice();
        }
        return sum;
    }

    public void addProduct(Product product){
        orderProducts.add(product);
    }

    public void removeProduct(Product product) {
        this.orderProducts.remove(product);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
