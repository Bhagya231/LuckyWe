import { placeOrder } from "./api.js";

const user = JSON.parse(localStorage.getItem("user"));

if (!user || user.role !== "CUSTOMER") {
    window.location.href = "./login.html";
}

const urlParams = new URLSearchParams(window.location.search);

const discount = Number(urlParams.get("discount")) || 0;

const couponId = urlParams.get("couponId");

console.log("Discount:", discount);
console.log("Coupon ID:", couponId);

console.log("Logged-in user:", user);

const selectedItems = JSON.parse(localStorage.getItem("checkoutItems")) || [];



const nameInput = document.getElementById("name");
const emailInput = document.getElementById("email");
const phoneInput = document.getElementById("phone");
const addressInput = document.getElementById("address");

const checkoutItems =
    document.getElementById("checkout-items");

const subtotalElement =
    document.getElementById("subtotal");

const discountElement =
    document.getElementById("discount");

const totalElement =
    document.getElementById("total");

const placeOrderButton =
    document.getElementById("place-order-btn");

const errorMessage =
    document.getElementById("error-message");

const successMessage =
    document.getElementById("success-message");


nameInput.value = user.name || "";
emailInput.value = user.email || "";


if (selectedItems.length === 0) {

    showError("Your cart is empty.");

    placeOrderButton.disabled = true;

} else {

    displayCheckoutItems();
}


function displayCheckoutItems() {

    checkoutItems.innerHTML = "";

    let subtotal = 0;

    selectedItems.forEach(item => {

        const price = Number(item.price) || 0;
        const quantity = Number(item.quantity) || 1;

        const itemTotal = price * quantity;

        subtotal += itemTotal;

        const div = document.createElement("div");

        div.className =
            "d-flex justify-content-between mb-3";

        div.innerHTML = `
            <div>
                <div class="fw-semibold">
                    ${item.productName || item.name}
                </div>

                <small class="text-secondary">
                    ₹${price.toFixed(2)} × ${quantity}
                </small>
            </div>

            <div class="fw-semibold">
                ₹${itemTotal.toFixed(2)}
            </div>
        `;

        checkoutItems.appendChild(div);
    });

    calculateTotal(subtotal);
}


function calculateTotal(subtotal) {

    const total = subtotal - discount;

    subtotalElement.textContent = `₹${subtotal.toFixed(2)}`;

    discountElement.textContent = `₹${discount.toFixed(2)}`;

    totalElement.textContent =
        `₹${total.toFixed(2)}`;
}


placeOrderButton.addEventListener(
    "click",
    async function () {

        hideMessages();

        const name = nameInput.value.trim();
        const email = emailInput.value.trim();
        const phone = phoneInput.value.trim();
        const address = addressInput.value.trim();


        if (!name) {
            showError("Please enter your name.");
            return;
        }

        if (!email) {
            showError(
                "User email not found. Please login again."
            );
            return;
        }

        if (!/^[0-9]{10}$/.test(phone)) {
            showError(
                "Please enter a valid 10 digit phone number."
            );
            return;
        }

        if (!address) {
            showError(
                "Please enter your delivery address."
            );
            return;
        }

        if (selectedItems.length === 0) {
            showError("Your cart is empty.");
            return;
        }


        const orderRequest = {

            address: address,

            orderItems: selectedItems.map(item => ({

                productId:
                    item.productId || item.id,

                quantity:
                    Number(item.quantity)

            }))
        };


        console.log("Email sent to backend:",email);

        console.log(
            "Order request:",
            orderRequest
        );

        console.log(
            "Checkout discount:",
            discount
        );


        placeOrderButton.disabled = true;
        placeOrderButton.textContent =
            "Placing Order...";


        try {

            const response =
                await placeOrder(
                    email,
                    orderRequest,
                    couponId
                );

            console.log(
                "Order response:",
                response
            );


            localStorage.removeItem("checkoutItems");
            localStorage.removeItem("checkoutDiscount");


            window.location.href =
                `orderconfirmation.html?orderId=${response.orderId}`;


        } catch (error) {

            console.error(
                "Place order error:",
                error
            );

            showError(
                error.message ||
                "Failed to place order."
            );

            placeOrderButton.disabled = false;

            placeOrderButton.textContent =
                "Place Order";
        }
    }
);


function showError(message) {

    errorMessage.textContent = message;

    errorMessage.classList.remove("d-none");
}


function showSuccess(message) {

    successMessage.textContent = message;

    successMessage.classList.remove("d-none");
}


function hideMessages() {

    errorMessage.classList.add("d-none");

    successMessage.classList.add("d-none");
}