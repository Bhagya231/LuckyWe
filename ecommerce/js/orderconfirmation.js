import { getCustomerOrder } from "./api.js";


const user = JSON.parse(localStorage.getItem("user"));

if (!user) {

    window.location.href = "./login.html";
}


const params = new URLSearchParams(window.location.search);

const orderId = params.get("orderId");

if (!orderId) {
    window.location.href = "./product.html";
}

console.log("Order ID:", orderId);

async function loadOrder() {
    try {

        const order = await getCustomerOrder(user.email, orderId);

        console.log("Order:", order);

        displayOrder(order);

    } catch (error) {

        console.error("Error loading order:", error);

        alert("Unable to load order.");

        window.location.href = "./product.html";
    }
}

function displayOrder(order) {

    document.getElementById("customer-name").textContent = order.customerName || "";
    document.getElementById("customer-email").textContent = order.customerEmail || "";
    document.getElementById("customer-phone").textContent = order.phone || "";
    document.getElementById("customer-address").textContent = order.address || "";
    document.getElementById("order-number").textContent = order.orderId || "N/A";
    document.getElementById("order-date").textContent = order.orderDate || "N/A";
    document.getElementById("delivery-date").textContent = order.deliveryDate || "N/A";


    const orderedProducts = document.getElementById("ordered-products");
    orderedProducts.innerHTML = "";
    const items = order.orderItems || [];
    items.forEach(item => {
        const productName = item.productName || "Product";
        const quantity = Number(item.quantity) || 0;
        const price = Number(item.unitPrice || item.price) || 0;

        const itemTotal = Number(item.itemTotal) || price * quantity;

        const div = document.createElement("div");

        div.className = "d-flex justify-content-between align-items-center mb-3";
        div.innerHTML = `
            <div>
                <div class="fw-semibold">
                    ${productName}
                </div>
                <small class="text-secondary">
                    ₹${price.toFixed(2)} × ${quantity}
                </small>
            </div>
            <div class="fw-semibold">
                ₹${itemTotal.toFixed(2)}
            </div>
        `;
        orderedProducts.appendChild(div);
    });

    const cost = Number(order.cost) || 0;
    const discount = Number(order.discount) || 0;

    const total = cost - discount;

    document.getElementById("subtotal").textContent = `₹${cost.toFixed(2)}`;
    document.getElementById("discount").textContent = `₹${discount.toFixed(2)}`;
    document.getElementById("total").textContent = `₹${total.toFixed(2)}`;

    setTimeout(() => {

            window.location.href = "./product.html";

        }, 5000);
}

loadOrder();

