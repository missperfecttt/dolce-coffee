package com.dolcecoffee.api.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class OrderController {

    private final List<Map<String, Object>> orderList = new ArrayList<>();

    public static class CartItemRequest {
        private String id;
        private String name;
        private int quantity;
        private double price;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }

    public static class OrderRequest {
        private String orderType;
        private List<CartItemRequest> items;
        private double totalAmount;
        private double advanceAmount;
        private String paymentReceipt;

        public String getOrderType() { return orderType; }
        public void setOrderType(String orderType) { this.orderType = orderType; }
        public List<CartItemRequest> getItems() { return items; }
        public void setItems(List<CartItemRequest> items) { this.items = items; }
        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
        public double getAdvanceAmount() { return advanceAmount; }
        public void setAdvanceAmount(double advanceAmount) { this.advanceAmount = advanceAmount; }
        public String getPaymentReceipt() { return paymentReceipt; }
        public void setPaymentReceipt(String paymentReceipt) { this.paymentReceipt = paymentReceipt; }
    }

    @PostMapping
    public Map<String, Object> createOrder(@RequestBody OrderRequest orderRequest) {
        String orderId = "DLC-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        String orderType = orderRequest.getOrderType() != null ? orderRequest.getOrderType() : "Dine In";

        Map<String, Object> newOrder = new HashMap<>();
        newOrder.put("orderId", orderId);
        newOrder.put("orderType", orderType);
        newOrder.put("totalAmount", orderRequest.getTotalAmount());
        newOrder.put("advanceAmount", orderRequest.getAdvanceAmount());
        newOrder.put("paymentReceipt", orderRequest.getPaymentReceipt());
        newOrder.put("items", orderRequest.getItems());
        newOrder.put("status", "RECEIVED");
        newOrder.put("timestamp", new Date().toString());

        orderList.add(0, newOrder);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("orderType", orderType);
        response.put("status", "RECEIVED");
        response.put("estimatedTime", "10-15 mins");
        return response;
    }

    @GetMapping
    public List<Map<String, Object>> getAllOrders() {
        return orderList;
    }

    @PatchMapping("/{orderId}/status")
    public Map<String, Object> updateOrderStatus(@PathVariable String orderId, @RequestBody Map<String, String> body) {
        String newStatus = body.get("status");
        for (Map<String, Object> order : orderList) {
            if (order.get("orderId").equals(orderId)) {
                order.put("status", newStatus);
                return order;
            }
        }
        return Collections.singletonMap("error", "Order not found");
    }

    @DeleteMapping("/{orderId}")
    public Map<String, String> deleteOrder(@PathVariable String orderId) {
        boolean removed = orderList.removeIf(order -> order.get("orderId").equals(orderId));
        if (removed) {
            return Collections.singletonMap("message", "Order deleted successfully");
        }
        return Collections.singletonMap("error", "Order not found");
    }
}