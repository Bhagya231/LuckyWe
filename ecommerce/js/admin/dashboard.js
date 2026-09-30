import {
    getAllProducts,
    getAdminOrders
} from "../api.js";

const user = JSON.parse(localStorage.getItem("user"));

if (!user) {
    window.location.href = "login.html";
}

const productCount = document.getElementById("productCount");
const orderCount =  document.getElementById("orderCount");
const logoutBtn = document.getElementById("logoutBtn");
document.getElementById("adminName").textContent = user.name;
document.getElementById("adminEmail").textContent = user.email;
document.getElementById("adminRole").textContent = user.role;

async function loadDashboard() {
    try {
        const products = await getAllProducts();
        productCount.textContent = products.length;
        const orders = await getAdminOrders(user.email);
        orderCount.textContent = orders.length;
    }
    catch (error) {
        console.error("Dashboard error:",error );
    }

}

logoutBtn.addEventListener("click", function () {
        localStorage.removeItem("user");
        localStorage.removeItem("jwtToken");
        window.location.href = "login.html";
    }
);

loadDashboard();