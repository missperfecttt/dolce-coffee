package com.dolcecoffee.api.controller;

import com.dolcecoffee.api.model.OrderEntity;
import com.dolcecoffee.api.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

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

        String itemsJson = "[]";
        try {
            itemsJson = objectMapper.writeValueAsString(orderRequest.getItems());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        OrderEntity entity = new OrderEntity(
            orderId,
            orderType,
            orderRequest.getTotalAmount(),
            orderRequest.getAdvanceAmount(),
            orderRequest.getPaymentReceipt(),
            "RECEIVED",
            new Date().toString(),
            itemsJson
        );

        orderRepository.save(entity);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("orderType", orderType);
        response.put("status", "RECEIVED");
        response.put("estimatedTime", "10-15 mins");
        return response;
    }

    @GetMapping
    public List<Map<String, Object>> getAllOrders() {
        List<OrderEntity> entities = orderRepository.findAllByOrderByTimestampDesc();
        List<Map<String, Object>> result = new ArrayList<>();

        for (OrderEntity entity : entities) {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", entity.getOrderId());
            map.put("orderType", entity.getOrderType());
            map.put("totalAmount", entity.getTotalAmount());
            map.put("advanceAmount", entity.getAdvanceAmount());
            map.put("paymentReceipt", entity.getPaymentReceipt());
            map.put("status", entity.getStatus());
            map.put("timestamp", entity.getTimestamp());

            try {
                List<?> items = objectMapper.readValue(entity.getItemsJson(), List.class);
                map.put("items", items);
            } catch (Exception e) {
                map.put("items", Collections.emptyList());
            }

            result.add(map);
        }

        return result;
    }

    @PatchMapping("/{orderId}/status")
    public Map<String, Object> updateOrderStatus(@PathVariable String orderId, @RequestBody Map<String, String> body) {
        String newStatus = body.get("status");
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();
            order.setStatus(newStatus);
            orderRepository.save(order);

            Map<String, Object> map = new HashMap<>();
            map.put("orderId", order.getOrderId());
            map.put("status", order.getStatus());
            return map;
        }
        return Collections.singletonMap("error", "Order not found");
    }

    @DeleteMapping("/{orderId}")
    public Map<String, String> deleteOrder(@PathVariable String orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
            return Collections.singletonMap("message", "Order deleted successfully");
        }
        return Collections.singletonMap("error", "Order not found");
    }
}