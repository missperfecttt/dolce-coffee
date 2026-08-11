const defaultMenuItems = [
    { id: "1", name: "Espresso Single", description: "Rich, dark shot of espresso.", price: 200.00, category: "Hot" },
    { id: "2", name: "Double Espresso", description: "Bold double shot of espresso.", price: 200.00, category: "Hot" },
    { id: "3", name: "Cappuccino", description: "Espresso with steamed milk foam.", price: 200.00, category: "Hot" },
    { id: "4", name: "Caffè Latte", description: "Smooth espresso with warm milk.", price: 200.00, category: "Hot" },
    { id: "5", name: "Iced Vanilla Latte", description: "Espresso with cold milk and vanilla.", price: 200.00, category: "Cold" },
    { id: "6", name: "Iced Caramel Macchiato", description: "Espresso, milk, and caramel drizzle.", price: 200.00, category: "Cold" },
    { id: "7", name: "Croissant", description: "Flaky, buttery fresh croissant.", price: 200.00, category: "Pastry" },
    { id: "8", name: "Chocolate Muffin", description: "Soft muffin filled with chocolate chunks.", price: 200.00, category: "Pastry" }
];

let allMenuItems = [...defaultMenuItems];
let cart = [];
let selectedOrderType = "Dine In";

const API_BASE_URL = "https://dolce-coffee-backend.onrender.com/api/v1";

const itemImages = {
    "1": "https://images.unsplash.com/photo-1510591509098-f4fdc6d0ff04?auto=format&fit=crop&w=500&q=80",
    "2": "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&w=500&q=80",
    "3": "https://images.unsplash.com/photo-1534778101976-62847782c213?auto=format&fit=crop&w=500&q=80",
    "4": "https://images.unsplash.com/photo-1570968915860-54d5c301fa9f?auto=format&fit=crop&w=500&q=80"
};

document.addEventListener("DOMContentLoaded", () => {
    renderMenu(allMenuItems);
    fetchMenuItems();
    setupCategoryTabs();
});

function fetchMenuItems() {
    fetch(`${API_BASE_URL}/menu`, { signal: AbortSignal.timeout(5000) })
        .then(res => {
            if (!res.ok) throw new Error("HTTP " + res.status);
            return res.json();
        })
        .then(data => {
            if (Array.isArray(data) && data.length > 0) {
                allMenuItems = data;
                renderMenu(data);
            }
        })
        .catch(err => {
            console.log("Using instant offline menu while backend wakes up:", err);
            renderMenu(allMenuItems);
        });
}

function renderMenu(items) {
    const container = document.getElementById("menu-container");
    if (!container) return;
    container.innerHTML = "";

    items.forEach(item => {
        const imageUrl = itemImages[item.id] || "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?auto=format&fit=crop&w=500&q=80";

        const card = document.createElement("div");
        card.className = "menu-card";
        card.innerHTML = `
            <img src="${imageUrl}" alt="${item.name}" class="card-img">
            <div class="card-body">
                <div>
                    <h3>${item.name}</h3>
                    <p>${item.description}</p>
                </div>
                <div style="display:flex; justify-content:space-between; align-items:center; margin-top: 15px;">
                    <span class="card-price">${item.price.toFixed(2)} ETB</span>
                    <button class="btn btn-primary" style="padding: 8px 18px; font-size: 0.9rem;" 
                        onclick="addToCart('${item.id}', '${item.name.replace(/'/g, "\\'")}', ${item.price})">+ Add to Order</button>
                </div>
            </div>
        `;
        container.appendChild(card);
    });
}

function toggleCart() {
    document.getElementById("cart-sidebar").classList.toggle("open");
    document.getElementById("cart-overlay").classList.toggle("open");
}

function setOrderType(type) {
    selectedOrderType = type;
    document.getElementById("selected-dining-type").innerText = type;
    
    document.getElementById("btn-dine-in").classList.toggle("active", type === 'Dine In');
    document.getElementById("btn-takeaway").classList.toggle("active", type === 'Takeaway');
}

function addToCart(id, name, price) {
    const existing = cart.find(item => item.id === id);
    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({ id, name, price, quantity: 1 });
    }
    updateCartUI();
    
    const cartSidebar = document.getElementById("cart-sidebar");
    if (!cartSidebar.classList.contains("open")) {
        toggleCart();
    }
}

function changeQty(id, delta) {
    const item = cart.find(i => i.id === id);
    if (!item) return;

    item.quantity += delta;
    if (item.quantity <= 0) {
        cart = cart.filter(i => i.id !== id);
    }
    updateCartUI();
}

function updateCartUI() {
    const container = document.getElementById("cart-items-container");
    container.innerHTML = "";

    let total = 0;
    let totalItems = 0;

    if (cart.length === 0) {
        container.innerHTML = `<div style="text-align:center; color: var(--text-muted); margin-top: 40px;">Your cart is empty. Pick items from the menu above!</div>`;
    } else {
        cart.forEach(item => {
            total += item.price * item.quantity;
            totalItems += item.quantity;

            const div = document.createElement("div");
            div.className = "cart-item";
            div.innerHTML = `
                <div>
                    <div class="cart-item-title">${item.name}</div>
                    <small style="color: var(--text-muted);">${item.price.toFixed(2)} ETB</small>
                </div>
                <div class="qty-controls">
                    <button class="qty-btn" onclick="changeQty('${item.id}', -1)">-</button>
                    <span style="font-weight: bold; min-width: 20px; text-align: center;">${item.quantity}</span>
                    <button class="qty-btn" onclick="changeQty('${item.id}', 1)">+</button>
                </div>
            `;
            container.appendChild(div);
        });
    }

    document.getElementById("cart-count").innerText = totalItems;
    document.getElementById("cart-total").innerText = `${total.toFixed(2)} ETB`;
}

function setupCategoryTabs() {
    document.querySelectorAll(".tab-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            document.querySelectorAll(".tab-btn").forEach(b => b.classList.remove("active"));
            btn.classList.add("active");
            const cat = btn.getAttribute("data-category");
            renderMenu(cat === "ALL" ? allMenuItems : allMenuItems.filter(i => i.category.toLowerCase() === cat.toLowerCase()));
        });
    });
}

function openConfirmationModal() {
    if (cart.length === 0) {
        alert("Your cart is empty! Please add some items from the menu first.");
        return;
    }

    const itemListContainer = document.getElementById("confirm-item-list");
    itemListContainer.innerHTML = "";
    let total = 0;

    cart.forEach(item => {
        const subtotal = item.price * item.quantity;
        total += subtotal;
        const li = document.createElement("li");
        li.innerText = `${item.name} x${item.quantity} - ${subtotal.toFixed(2)} ETB`;
        itemListContainer.appendChild(li);
    });

    document.getElementById("confirm-order-type").innerText = selectedOrderType;
    document.getElementById("confirm-total-amount").innerText = `${total.toFixed(2)} ETB`;

    const paymentContainer = document.getElementById("takeaway-payment-container");
    if (selectedOrderType === "Takeaway") {
        const depositAmount = total * 0.5;
        document.getElementById("deposit-amount").innerText = `${depositAmount.toFixed(2)} ETB`;
        paymentContainer.style.display = "block";
    } else {
        paymentContainer.style.display = "none";
    }

    document.getElementById("modal-step-confirm").style.display = "block";
    document.getElementById("modal-step-success").style.display = "none";
    document.getElementById("order-modal").classList.add("open");
}

function previewScreenshot(event) {
    const file = event.target.files[0];
    const preview = document.getElementById("screenshot-preview");
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            preview.style.display = "block";
        };
        reader.readAsDataURL(file);
    }
}

function selectPaymentMethod(method, accountNumber) {
    document.querySelectorAll('.payment-card').forEach(c => c.style.borderColor = 'var(--border-soft)');
    const selectedCard = document.getElementById(method === 'Telebirr' ? 'pay-card-telebirr' : 'pay-card-cbe');
    if (selectedCard) selectedCard.style.borderColor = 'var(--primary-lime)';
}

function copyAccount(number, btn) {
    if (navigator.clipboard) {
        navigator.clipboard.writeText(number).then(() => {
            const orig = btn.innerText;
            btn.innerText = "Copied!";
            setTimeout(() => btn.innerText = orig, 2000);
        }).catch(() => {});
    }
}

function submitOrder() {
    const screenshotInput = document.getElementById("payment-screenshot");
    if (screenshotInput && screenshotInput.files && screenshotInput.files.length > 0) {
        const file = screenshotInput.files[0];
        const reader = new FileReader();
        reader.onload = function(e) {
            sendOrderToBackend(e.target.result);
        };
        reader.readAsDataURL(file);
    } else {
        sendOrderToBackend(null);
    }
}

function sendOrderToBackend(paymentReceiptBase64) {
    const total = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const orderPayload = {
        orderType: selectedOrderType,
        totalAmount: total,
        advanceAmount: selectedOrderType === "Takeaway" ? total * 0.5 : 0,
        paymentReceipt: paymentReceiptBase64,
        items: cart
    };

    fetch(`${API_BASE_URL}/orders`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(orderPayload)
    })
    .then(res => {
        if (!res.ok) throw new Error("HTTP error " + res.status);
        return res.json();
    })
    .then(data => {
        showSuccessModal(data.orderId, data.orderType, data.estimatedTime || "10-15 mins");
    })
    .catch(err => {
        console.warn("Backend order error, generating local confirmation:", err);
        const fallbackOrderId = "DLC-" + Math.random().toString(36).substring(2, 7).toUpperCase();
        showSuccessModal(fallbackOrderId, selectedOrderType, "10-15 mins");
    });
}

function showSuccessModal(orderId, orderType, estimatedTime) {
    document.getElementById("modal-order-id").innerText = orderId;
    document.getElementById("modal-order-type").innerText = orderType;
    document.getElementById("modal-order-time").innerText = estimatedTime;

    document.getElementById("modal-step-confirm").style.display = "none";
    document.getElementById("modal-step-success").style.display = "block";

    cart = [];
    updateCartUI();
}

function closeOrderModal() {
    document.getElementById("order-modal").classList.remove("open");
}