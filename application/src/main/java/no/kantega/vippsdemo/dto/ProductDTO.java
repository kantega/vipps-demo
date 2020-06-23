package no.kantega.vippsdemo.dto;

public class ProductDTO {

    private String name;
    private int quantity;

    // For deserialization
    public ProductDTO() {
        this.name = "";
        this.quantity = 0;
    }

    public ProductDTO(String name, int amount) {
        this.name = name;
        this.quantity = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
