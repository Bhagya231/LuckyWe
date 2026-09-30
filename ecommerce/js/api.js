import axios from "https://cdn.jsdelivr.net/npm/axios@1.12.2/+esm";

const BASE_URL = "http://localhost:8080";

const api = axios.create({
    baseURL: BASE_URL
});



api.interceptors.request.use((config) => {

    const token = localStorage.getItem("jwtToken");
    console.log("token    " + token);

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});




export async function register(request) {

    const response = await api.post(
        "/auth/register",
        request
    );

    return response.data;
}


export async function login(request) {

    const response = await api.post(
        "/auth/login",
        request
    );

    return response.data;
}


export async function getProducts(
    offset = 0,
    limit = 20,
    search,
    category,
    minPrice,
    maxPrice
) {

    const response = await api.get("/products", {

        params: {
            offset: offset,
            limit: limit,
            search: search,
            category: category,
            minPrice: minPrice,
            maxPrice: maxPrice
        }

    });

    return response.data;
}

export async function getAllProducts() {

    const response = await api.get("/products/getAllProducts");

    return response.data;
}


export async function getProductById(id) {

    const response = await api.get(`/products/${id}`);

    return response.data;
}

export async function addProduct(email, request) {

    const response = await api.post(
        "/products/add",
        request,
        {
            params: {
                email: email
            }
        }
    );

    return response.data;
}


export async function updateProduct(email, id, request) {

    const response = await api.put(
        `/products/update/${id}`,
        request,
        {
            params: {
                email: email
            }
        }
    );

    return response.data;
}


export async function deleteProduct(email, id) {

    const response = await api.delete(
        `/products/delete/${id}`,
        {
            params: {
                email: email
            }
        }
    );

    return response.data;
}



export async function getCart(userId) {

    const response = await api.get(
        `/cart/${userId}`
    );

    return response.data;
}


export async function addToCart(
    userId,
    productId,
    quantity
) {

    const response = await api.post(
        `/cart/${userId}`,
        {
            productId: productId,
            quantity: quantity
        }
    );

    return response.data;
}


export async function increaseQuantity(
    userId,
    productId
) {

    const response = await api.put(
        `/cart/${userId}/increase/${productId}`
    );

    return response.data;
}


export async function decreaseQuantity( userId, productId) {

    const response = await api.put(
        `/cart/${userId}/decrease/${productId}`
    );

    return response.data;
}


export async function removeFromCart(
    userId,
    productId
) {

    const response = await api.delete(
        `/cart/${userId}/remove/${productId}`
    );

    return response.data;
}


export async function clearCart(userId) {

    const response = await api.delete(
        `/cart/${userId}/clear`
    );

    return response.data;
}



export async function getCoupons() {

    const response = await api.get("/coupons");

    return response.data;
}


export async function applyCoupon(email, request) {

    const response = await api.post(
        "/coupons/apply",
        request,
        {
            params: {
                email: email
            }
        }
    );

    return response.data;
}


export async function addToWishlist(userId, productId) {

    const response = await api.post(
        "/wishlist/add",
        null,
        {
            params: {
                userId: userId,
                productId: productId
            }
        }
    );

    return response.data;
}


export async function getWishlist(userId) {

    const response = await api.get(
        "/wishlist/view",
        {
            params: {
                userId: userId
            }
        }
    );

    return response.data;
}


export async function removeFromWishlist(
    userId,
    productId
) {

    const response = await api.delete(
        `/wishlist/${productId}`,
        {
            params: {
                userId: userId
            }
        }
    );

    return response.data;
}


export async function moveWishlistToCart(
    userId,
    productId
) {

    const response = await api.post(
        `/wishlist/${productId}/movetocart`,
        null,
        {
            params: {
                userId: userId
            }
        }
    );

    return response.data;
}



export async function placeOrder(email, request,couponId) {

    const response = await api.post(
        "/orders",
        request,
        {
            params: {
                email: email,
                couponId:couponId
            }
        }
    );

    return response.data;
}

export async function getOrderByOrderNumber(email, orderNumber) {

    const response = await api.get(
        `/orders/admin/${orderNumber}`,
        {
            params: {
                email: email
            }
        }
    );

    return response.data;
}

export async function getCustomerOrders(email) {

    const response = await api.get(
        "/orders/customer",
        {
            params: {
                email: email
            }
        }
    );

    return response.data;
}


export async function getCustomerOrder(
    email,
    orderId
) {

    const response = await api.get(
        `/orders/customer/${orderId}`,
        {
            params: {
                email: email
            }
        }
    );

    return response.data;
}


export async function getAdminOrders(email) {

    const response = await api.get(
        "/orders/admin",
        {
            params: {
                email: email
            }
        }
    );

    return response.data;
}