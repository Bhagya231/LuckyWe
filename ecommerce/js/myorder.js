import {
    getCustomerOrders,
    getCart,
    getWishlist
} from "./api.js";

const user =JSON.parse(localStorage.getItem("user"));
if (!user || user.role !== "CUSTOMER") {
    window.location.href = "./login.html";
}

const userId = user.id;
const loading = document.getElementById("loading");
const ordersContainer = document.getElementById("orders-container");
const emptyOrders = document.getElementById("empty-orders");
const errorMessage = document.getElementById("error-message");
const cartCount = document.getElementById("cart-count");
const wishlistCount = document.getElementById("wishlist-count");

async function loadOrders() {
    try {
        loading.classList.remove("d-none");
        ordersContainer.innerHTML = "";
        emptyOrders.classList.add("d-none");
        errorMessage.classList.add("d-none");
        const orders = await getCustomerOrders( user.email);
        console.log("Customer Orders:",orders);
        if (!orders || orders.length === 0) {
            emptyOrders.classList.remove("d-none");
            return;
        }
        for (let i = 0;i < orders.length;i++) {
            createOrder(orders[i],i);
        }
    }
    catch (error) {
        console.error("Orders error:",error);
        showError(error.message ||"Unable to load orders.");
    }
    finally {
        loading.classList.add("d-none");
    }
}



function createOrder(order,index) {
    const card = document.createElement("div");
    card.className = "card border-0 shadow-sm mb-4";
    const orderDate = formatDate(order.orderDate);
    const deliveryDate =formatDate(order.deliveryDate);
    card.innerHTML = `
        <div class="card-body">
            <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
                <div>
                    <h5 class="fw-bold mb-1">
                        Order Number
                    </h5>
                    <div class="fw-semibold">
                        ${order.orderNumber || "-"}
                    </div>
                    <small class="text-secondary">
                        ${orderDate}
                    </small>
                </div>
                <span class="badge bg-success">
                    Ordered
                </span>
            </div>
            <hr>
            <h6 class="fw-bold mb-3">
                Products
            </h6>
            <div id="order-items-${index}">
            </div>
            <p class="text-secondary mb-1">
                    subtotal
                </p>
                <p class="fw-semibold mb-0">
                    ₹${Number(order.cost || 0).toFixed(2)}
                </p>
            <hr>
            <div class="row g-3">
                <div class="col-12 col-md-6">
                    <p class="text-secondary mb-1">
                        Delivery Date
                    </p>
                    <p class="fw-semibold mb-0">
                        ${deliveryDate}
                    </p>
                </div>
                <div class="col-12 col-md-6">
                    <p class="text-secondary mb-1">
                        Delivery Address
                    </p>
                    <p class="fw-semibold mb-0">
                        ${order.address || "-"}
                    </p>
                </div>
                <div class="col-12 col-md-6">
                    <p class="text-secondary mb-1">
                        Discount
                    </p>
                    <p class="fw-semibold text-success mb-0">
                        ₹${Number(order.discount || 0).toFixed(2)}
                    </p>
                </div>
                <div class="col-12 col-md-6">
                    <p class="text-secondary mb-1">Total</p>
                    <p class="fw-bold text-primary fs-5 mb-0">
                        ₹${Number(order.cost - order.discount || 0).toFixed(2)}
                    </p>
                </div>
            </div>
        </div>
    `;
    ordersContainer.appendChild(card);
    displayOrderItems(order,document.getElementById(`order-items-${index}`));
}

function displayOrderItems(order,container) {
    const items = order.orderItems || [];
    if (items.length === 0) {
        container.innerHTML = `
            <p class="text-secondary">
                No product details available.
            </p>
        `;
        return;
    }

    for (let i = 0;i < items.length;i++) {
        const item = items[i];
        const div = document.createElement("div");
        div.className ="d-flex justify-content-between align-items-center mb-3";
        div.innerHTML = `
            <div>
                <strong>
                    ${item.productName || "Product"}
                </strong>
                <div class="text-secondary small">
                    Quantity: ${item.quantity || 0}
                </div>
            </div>
            <strong>
                ₹${Number(item.unitPrice || 0).toFixed(2)}
            </strong>
        `;
        container.appendChild(div);
    }
}

function formatDate(date) {
    if (!date) {
        return "-";
    }
    const value = new Date(date);
    return value.toLocaleDateString("en-IN",
        {
            day: "2-digit",
            month: "short",
            year: "numeric"
        }
    );
}
async function loadCartCount() {
    try {
        const cart = await getCart(userId);
        let count = 0;
        if (cart &&cart.cartItems) {
            for (let i = 0;i < cart.cartItems.length;i++) {
                count += Number(cart.cartItems[i].quantity);
            }
        }
        cartCount.textContent = count;
    }
    catch (error) {
        cartCount.textContent ="0";
    }
}
async function loadWishlistCount() {
    try {
        const wishlist = await getWishlist(userId);
        wishlistCount.textContent = wishlist.length;
    }
    catch (error) {
        wishlistCount.textContent = "0";
    }
}
document.getElementById("profileModal").addEventListener("show.bs.modal",function () {
            document.getElementById("profile-name").textContent =user.name || "-";
            document.getElementById("profile-email").textContent = user.email || "-";
            document.getElementById("profile-phone").textContent = user.phone || "-";
            document.getElementById( "profile-address").textContent =  user.address || "-";
            document.getElementById("profile-role").textContent =  user.role || "CUSTOMER";
        }
    );
document.getElementById("logout-btn").addEventListener("click",function () {
            localStorage.removeItem("user");
            localStorage.removeItem("checkoutItems");
            localStorage.removeItem("jwtToken");
            window.location.href ="./login.html";

             
        }
    );
function showError(message) {
    errorMessage.textContent =  message;
    errorMessage.classList.remove( "d-none");
}

document.addEventListener("DOMContentLoaded",function () {
        loadOrders();
        loadCartCount();
        loadWishlistCount();
    }
);
