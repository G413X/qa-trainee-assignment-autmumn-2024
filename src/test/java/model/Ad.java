package model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)  // Игнорировать неизвестные поля
public class Ad {
    private Integer sellerId;

    // Геттеры и сеттеры
    public int getSellerId() { return sellerId; }

    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
}