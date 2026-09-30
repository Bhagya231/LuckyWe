import {
    getWishlist,
    removeFromWishlist,
    moveWishlistToCart
} from "./api.js";

const user = JSON.parse(localStorage.getItem("user"));

if (!user || user.role !== "CUSTOMER") {
    window.location.href = "login.html";
}

let wishlist = [];
const wishlistContainer = document.getElementById("wishlist-container");
const emptyWishlist = document.getElementById("empty-wishlist");
const message = document.getElementById("message");

async function loadWishlist() {
    try {
        wishlist = await getWishlist(user.id);
        displayWishlist();
    }
    catch (error) {
        console.error("Wishlist loading error:",error);
        showMessage(error.message,"danger");
    }
}

function displayWishlist() {
    wishlistContainer.innerHTML = "";
    if (wishlist.length === 0) {
        emptyWishlist.classList.remove("d-none");
        return;
    }
    emptyWishlist.classList.add("d-none");
    wishlist.forEach(function (item) {
        wishlistContainer.innerHTML += `
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card h-100 border-0 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title fw-bold">
                            ${item.productName}
                        </h5>
                        <p class="text-secondary mb-2">
                            ${item.category}
                        </p>
                        <h5 class="text-primary fw-bold">
                            ₹${Number(item.price).toFixed(2)}
                        </h5>
                        <div class="d-flex gap-2 mt-3">
                            <button
                                class="btn btn-primary move-cart-btn"
                                data-product-id="${item.productId}">
                                Move to Cart
                            </button>
                            <button
                                class="btn btn-outline-danger remove-btn"
                                data-product-id="${item.productId}">
                                Remove
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    });
}

async function removeProduct(productId) {
    try {
        await removeFromWishlist(user.id, productId);
        wishlist = wishlist.filter(function (item) {
                return item.productId !== productId;
            });
        displayWishlist();
        showMessage( "Product removed from wishlist.", "success");
    }
    catch (error) {
        console.error("Remove wishlist error:",error);
        showMessage(error.message,"danger");
    }
}
async function moveToCart(productId) {
    try {
        await moveWishlistToCart(user.id,productId);
        wishlist = wishlist.filter(function (item) {
                return item.productId !== productId;
            });
        displayWishlist();
        showMessage("Product moved to cart.","success");
    }
    catch (error) {
        console.error("Move to cart error:",error);
        showMessage(error.message,"danger");
    }
}

wishlistContainer.addEventListener("click",function (event) {
        const productId = event.target.getAttribute("data-product-id");
        if (!productId) {
            return;
        }
        if (event.target.classList.contains("remove-btn")) {
            removeProduct(productId);
        }
        if (event.target.classList.contains("move-cart-btn")) {
            moveToCart(productId);
        }
    }
);

document.getElementById("logout-btn").addEventListener("click",function () {
            localStorage.removeItem("user");
             localStorage.removeItem("jwtToken");
            window.location.href ="./login.html";
           
    
    

        }
    );
function showMessage(text, type) {
    message.textContent = text;
    message.className = `alert alert-${type}`;
    setTimeout(function () {
        message.classList.add("d-none");
    }, 3000);
}

loadWishlist();
