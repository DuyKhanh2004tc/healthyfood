package model;

/**
 * @author ASUS
 */
public class OrderStatus {
    private int id;
    private String statusName;
    private String description;

    public OrderStatus() {
    }

    public OrderStatus(int id, String statusName, String description) {
        this.id = id;
        this.statusName = statusName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}