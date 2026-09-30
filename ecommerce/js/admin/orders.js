import { 
    getAdminOrders, 
    getOrderByOrderNumber 
} from "../api.js";

const user = JSON.parse(localStorage.getItem("user"));

if (!user) {
    window.location.href = "../login.html";
}

if (user.role !== "ADMIN") {
    alert("Access denied");
    window.location.href = "../login.html";
}

const ordersTableBody = document.getElementById("ordersTableBody");
const ordersSection = document.getElementById("ordersSection");
const orderDetails = document.getElementById("orderDetails");
const orderInfo = document.getElementById("orderInfo");
const orderItemsBody = document.getElementById("orderItemsBody");
const orderTotal = document.getElementById("orderTotal");
const message = document.getElementById("message");
const backBtn = document.getElementById("backBtn");
const logoutBtn = document.getElementById("logoutBtn");

async function loadOrders() {
    try {
        const orders = await getAdminOrders(user.email);
        displayOrders(orders);
    }
    catch (error) {
        console.error(error);
        showMessage(  error.message, "danger" );
    }
}
function displayOrders(orders) {
    ordersTableBody.innerHTML = " ";
    if (orders.length === 0) {
        ordersTableBody.innerHTML = `
            <tr>
                <td colspan="7" class="text-center">
                    No orders found
                </td>
            </tr>
        `;
        return;
    }
    orders.forEach(function (order) {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${order.orderNumber}</td>
            <td>${order.customerName}</td>
            <td>${order.customerEmail}</td>
            <td>${order.orderDate}</td>
            <td>${order.deliveryDate}</td>
            <td> ₹${order.cost}</td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="viewOrder('${order.orderNumber}')"> View </button>
            </td>
        `;
        ordersTableBody.appendChild(row);
    });

}
window.viewOrder = async function (orderNumber) {
    try {
        const order =  await getOrderByOrderNumber(user.email, orderNumber);
        console.log(order);
        displayOrderDetails(order);
    }
    catch (error) {
        console.error(error);
        showMessage( error.message, "danger");
    }
};
function displayOrderDetails(order) {
    ordersSection.classList.add("d-none");
    orderDetails.classList.remove("d-none");
    backBtn.classList.remove("d-none");
    orderInfo.innerHTML = `
        <p><strong>Order Number:</strong>${order.orderNumber}</p>
        <p><strong>Customer:</strong>${order.customerName}</p>
        <p><strong>Email:</strong>${order.customerEmail}</p>
        <p><strong>Phone:</strong>${order.phone}</p>
        <p><strong>Address:</strong>${order.address}</p>
        <p><strong>Order Date:</strong>${order.orderDate}</p>
        <p><strong>Delivery Date:</strong>${order.deliveryDate}</p>
    `;
    orderItemsBody.innerHTML = "";
    order.orderItems.forEach(function (item) {
        console.log("item: ",item)
        const itemTotal = Number(item.unitPrice) * Number(item.quantity);
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${item.productName}</td>
            <td>${item.quantity}</td>
            <td>₹${Number(item.unitPrice).toFixed(2)}</td>
            <td>₹${itemTotal.toFixed(2)}</td>
        `;
        orderItemsBody.appendChild(row);
    });
    orderTotal.innerHTML = `<h5>Total: ₹${Number(order.cost).toFixed(2)}</h5>`;
}

backBtn.addEventListener("click", function () {
    orderDetails.classList.add("d-none");
    ordersSection.classList.remove("d-none");
    backBtn.classList.add("d-none");
    message.classList.add("d-none");
});
function showMessage(text, type) {
    message.textContent = text;
    message.className = `alert alert-${type}`;

}

logoutBtn.addEventListener("click", function () {
    localStorage.removeItem("user");
    localStorage.removeItem("jwtToken");
    window.location.href = "login.html";
});

loadOrders();