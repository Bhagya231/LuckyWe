import {
    getAllProducts,
    getProducts,
    addProduct,
    getProductById,
    updateProduct,
    deleteProduct
} from "../api.js";

const user = JSON.parse(localStorage.getItem("user"));

if (!user) {
    window.location.href = "login.html";
}

if (user.role !== "ADMIN") {

    alert("Access denied");

    window.location.href = "../login.html";

}

const productTableBody = document.getElementById("productTableBody");
const productFormContainer = document.getElementById("productFormContainer");
const productForm = document.getElementById("productForm");
const addProductBtn = document.getElementById("addProductBtn");
const cancelBtn = document.getElementById("cancelBtn");
const saveBtn = document.getElementById("saveBtn");
const formTitle = document.getElementById("formTitle");
const message = document.getElementById("message");
const logoutBtn = document.getElementById("logoutBtn");

let editingProductId = null;

async function loadProducts() {
    try {
        const products = await getAllProducts();
        displayProducts(products);
    }
    catch (error) {
        console.error(error);
        showMessage(error.message, "danger");
    }
}

function displayProducts(products) {
    productTableBody.innerHTML = "";
    if (products.length === 0) {
        productTableBody.innerHTML = `
            <tr>
                <td colspan="6" class="text-center">
                    No products found
                </td>
            </tr>
        `;
        return;
    }
    products.forEach(function (product) {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${product.name}</td>
            <td>${product.category}</td>
            <td>₹${product.price}</td>
            <td>${product.stock}</td>
            <td>${product.description}</td>
            <td>
                <button class="btn btn-sm btn-warning me-1"  onclick="editProduct('${product.id}')">Edit</button>
                <button class="btn btn-sm btn-danger" onclick="removeProduct('${product.id}')"> Delete</button>
            </td>
        `;
        productTableBody.appendChild(row);
    });
}

addProductBtn.addEventListener("click", function () {
    editingProductId = null;
    formTitle.textContent = "Add Product";
    saveBtn.textContent = "Add Product";
    productForm.reset();
    productFormContainer.classList.remove("d-none");
    message.classList.add("d-none");
});
cancelBtn.addEventListener("click", function () {
    productForm.reset();
    editingProductId = null;
    productFormContainer.classList.add("d-none");
});
productForm.addEventListener("submit", async function (event) {
    event.preventDefault();
    const product = {
        name: document.getElementById("name").value,
        category: document.getElementById("category").value,
        price: Number(document.getElementById("price").value),
        stock: Number(document.getElementById("stock").value),
        description: document.getElementById("description").value
    };
    try {
        if (editingProductId === null) { 
            await addProduct(user.email, product);
            showMessage("Product added successfully","success");
        }
        else {
            await updateProduct(user.email,editingProductId,product);
            showMessage("Product updated successfully", "success");
        }
        productForm.reset();
        editingProductId = null;
        productFormContainer.classList.add("d-none");
        loadProducts();
    }
    catch (error) {
        console.error(error);
        showMessage(error.message,"danger");
    }
});
window.editProduct = async function (productId) {
    try {
        const product = await getProductById(productId);
        document.getElementById("name").value = product.name;
        document.getElementById("category").value = product.category;
        document.getElementById("price").value = product.price;
        document.getElementById("stock").value = product.stock;
        document.getElementById("description").value = product.description;
        editingProductId = productId;
        formTitle.textContent = "Edit Product";
        saveBtn.textContent = "Update Product";
        productFormContainer.classList.remove("d-none");
        productFormContainer.scrollIntoView({behavior: "smooth"});
    }
    catch (error) {
        console.error(error);
        showMessage( error.message, "danger");
    }
};
window.removeProduct = async function (productId) {
    const confirmDelete = confirm("Are you sure you want to delete this product?");
    if (!confirmDelete) {
        return;
    }
    try {
        await deleteProduct(user.email,productId);
        showMessage("Product deleted successfully","success");
        loadProducts();
    }
    catch (error) {
        console.error(error);
        showMessage( error.message, "danger");
    }
};
function showMessage(text, type) {
    message.textContent = text;
    message.className =`alert alert-${type}`;
}

logoutBtn.addEventListener("click", function () {
    localStorage.removeItem("user");
    localStorage.removeItem("jwtToken");
    window.location.href = "login.html";
});

loadProducts();
