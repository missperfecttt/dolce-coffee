package com.dolcecoffee.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private String orderId;

    private String orderType;

    private double totalAmount;

    private double advanceAmount;

    @Column(columnDefinition = "TEXT")
    private String paymentReceipt;

    private String status;

    private String timestamp;

    @Column(columnDefinition = "TEXT")
    private String itemsJson;

    public OrderEntity() {}

    public OrderEntity(String orderId, String orderType, double totalAmount, double advanceAmount, String paymentReceipt, String status, String timestamp, String itemsJson) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.totalAmount = totalAmount;
        this.advanceAmount = advanceAmount;
        this.paymentReceipt = paymentReceipt;
        this.status = status;
        this.timestamp = timestamp;
        this.itemsJson = itemsJson;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getAdvanceAmount() { return advanceAmount; }
    public void setAdvanceAmount(double advanceAmount) { this.advanceAmount = advanceAmount; }

    public String getPaymentReceipt() { return paymentReceipt; }
    public void setPaymentReceipt(String paymentReceipt) { this.paymentReceipt = paymentReceipt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getItemsJson() { return itemsJson; }
    public void setItemsJson(String itemsJson) { this.itemsJson = itemsJson; }
}
