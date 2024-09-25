window.addEventListener("load", () => {

    loadData()

})

async function addNewProduct() {
    const formData = new FormData();

    formData.append("name", document.getElementById("inputEmail4").value);
    formData.append("unit_price", document.getElementById("inputAddress").value);
    formData.append("delivery_fee", document.getElementById("inputAddress2").value);
    formData.append("size", document.getElementById("size").value);
    formData.append("color", document.getElementById("color").value);
    formData.append("category", document.getElementById("category").value);
    formData.append("description", document.getElementById("inputPassword4").value);
    formData.append("brand", document.getElementById("brand").value);
    formData.append("img1", document.getElementById("img1").files[0]);
    formData.append("img2", document.getElementById("img2").files[0]);
    formData.append("img3", document.getElementById("img3").files[0]);

    const response = await fetch("AddNewProduct", {
        method: "POST",
        body: formData
    });

    if (response.ok) {
        const jsonData = await response.json();
        if (jsonData.success) {
            Notification().success({
                message: jsonData.data
            })
        } else {
            Notification().error({
                message: jsonData.data
            })
        }
        console.log(jsonData);
    } else {
        Notification().error({
            message: "Faild to Add Product"
        })
        console.error("Failed to submit the form", response.statusText);
    }
}

var categoryList;
var colorList;
var sizeList;
var brandList;
async function loadData() {

    const response = await fetch("LoadFeatures");
    if (response.ok) {

        const json = await response.json();

        if (json.success) {

            categoryList = json.data.categoryList;
            colorList = json.data.colorList;
            sizeList = json.data.sizeList;
            brandList = json.data.brandList;
            const userName = json.data.userName;
            const isBuyer = json.data.isBuyer;
            const isSeller = json.data.isSeller;

            let productList;
            if (json.data.productList != null || json.data.productList != undefined) {
                productList = json.data.productList;
                loadMyProducts(productList)
            }

            if (productList != null || productList != undefined) {
                const msgToSellerList = json.data.msgToSellerList;
                loadMessages(msgToSellerList)
            }

            document.getElementById("userName").innerHTML = userName;

            const orderList = json.data.orderList;
            if (isBuyer) {
                document.getElementById("accountType").innerHTML = "Buyer";
                document.getElementById("oppositeAccountType").innerHTML = "Seller";

                document.getElementById("addN").style.display = "none";
                document.getElementById("myP").style.display = "none";
                document.getElementById("msgS").style.display = "none";

                loadPurchasedOrders(orderList)

            } else if (isSeller) {

                document.getElementById("myOrders").style.display = "none";
                document.getElementById("accountType").innerHTML = "Seller";
                document.getElementById("oppositeAccountType").innerHTML = "Buyer";

                loadReceivedOrders(orderList)

            }

            loadSelectOptions("category", categoryList, ["category"]);
            loadSelectOptions("size", sizeList, ["size"]);
            loadSelectOptions("color", colorList, ["color"]);
            loadSelectOptions("brand", brandList, ["brand"]);
        } else {
            Notification().error({
                message: json.data
            })
        }

    } else {
        Notification().error({
            message: "Please Try Again Later"
        })
        // window.location.href = "sign-in.html"

        console.log(response)
    }

}

var messageId;
function openModel(msgId) {
    const exampleModal = document.getElementById('exampleModal')
    if (exampleModal) {
        exampleModal.addEventListener('show.bs.modal', event => {
            // Button that triggered the modal
            const button = event.relatedTarget
            // Extract info from data-bs-* attributes
            const recipient = button.getAttribute('data-bs-whatever')
            messageId = msgId;
            // If necessary, you could initiate an Ajax request here
            // and then do the updating in a callback.

            // Update the modal's content.
            const modalTitle = exampleModal.querySelector('.modal-title')
            const modalBodyInput = exampleModal.querySelector('.modal-body input')

            modalTitle.innerHTML = `Reply message To :<br><br> <span class="fs-5" style="color:grey;">${recipient}</span>`
        })
    }
}

async function sendReply() {

    let reply = document.getElementById("reply").value;
    if (reply.trim() != "") {

        const response = await fetch("ReplyToBuyerMessage", {
            method: "POST",
            body: JSON.stringify({
                id: messageId,
                text: reply
            }),
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const jsonData = await response.json();
            if (jsonData.success) {
                Notification().success({
                    message: jsonData.data
                })
            } else {
                Notification().error({
                    message: jsonData.data
                })
            }
            console.log(jsonData);
        } else {
            Notification().error({
                message: "Faild to Add Product"
            })
            console.error("Failed to submit the form", response.statusText);
        }
    }
}

async function changeOrderStatus(orderId) {
    const response = await fetch("UpdateProductStatus", {
        method: "POST",
        body: JSON.stringify({
            id: orderId
        }),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {
        const jsonData = await response.json();
        if (jsonData.success) {

            document.getElementById("r" + orderId).innerHTML = jsonData.data.status

            Notification().success({
                message: jsonData.data.message
            })
        } else {
            Notification().error({
                message: jsonData.data
            })
        }
        console.log(jsonData);
    } else {
        Notification().error({
            message: "Please Try Again Later"
        })
        console.error(response.statusText);
    }
}

async function changeAccountType() {

    let confirmStatus = confirm("Are You Sure You Want to Change Account Type ?")
    if (confirmStatus) {

        const response = await fetch("BecomeUser");

        if (response.ok) {
            const jsonData = await response.json();
            if (jsonData.success) {
                window.location.reload()
            } else {
                Notification().error({
                    message: jsonData.data
                })
            }
            console.log(jsonData);
        } else {
            Notification().error({
                message: "Please Try Again Later"
            })
            console.error(response);
        }

    }

}

function loadPurchasedOrders(orderList) {

    let tableBody = document.getElementById("oTableBody");
    let row = document.getElementById("oTableRow");

    tableBody.innerHTML = "";

    orderList.forEach(item => {

        let element = row.cloneNode(true);

        element.removeAttribute("id");
        element.querySelector(".oId").innerHTML = item.id;
        element.querySelector(".oProduct").innerHTML = item.product.name;
        element.querySelector(".oQty").innerHTML = item.qty;
        element.querySelector(".oPaid").innerHTML = item.order.total_amount;
        element.querySelector(".oAddress").innerHTML = item.order.address;
        element.querySelector(".oOtherText").innerHTML = item.order.text;
        element.querySelector(".oStatus").innerHTML = item.order.order_status.status;

        tableBody.appendChild(element);

    });

}

function loadReceivedOrders(orderList) {

    let tableBody = document.getElementById("rTableBody");
    let row = document.getElementById("rTableRow");

    tableBody.innerHTML = "";

    orderList.forEach(item => {

        let element = row.cloneNode(true);

        element.removeAttribute("id");
        element.querySelector(".rId").innerHTML = item.id;
        element.querySelector(".rProduct").innerHTML = item.product.name;
        element.querySelector(".rQty").innerHTML = item.qty;
        element.querySelector(".rPaid").innerHTML = item.order.total_amount;
        element.querySelector(".rAddress").innerHTML = item.order.address;
        element.querySelector(".rOtherText").innerHTML = item.order.text;
        element.querySelector(".rStatus").innerHTML = item.order.order_status.status;
        element.querySelector(".rStatus").setAttribute("id", "r" + item.id);
        element.querySelector(".rStatusBth").addEventListener("click", () => {
            changeOrderStatus(item.id)
        })

        tableBody.appendChild(element);

    });

    document.getElementById("pendingOrderCount").innerHTML = orderList.length
}

function loadMessages(msgList) {

    let messageTableBody = document.getElementById("mTableBody");
    let messageRow = document.getElementById("mTableRow");

    messageTableBody.innerHTML = "";

    let count = 1;
    msgList.forEach(message => {

        let element = messageRow.cloneNode(true);

        element.removeAttribute("id");
        element.querySelector(".msgCount").innerHTML = count;
        element.querySelector(".mssageText").innerHTML = message.message;
        element.querySelector(".mssageDatetime").innerHTML = message.datetime;
        element.querySelector(".mssageStatus").innerHTML = message.message_status.status;
        element.querySelector(".mssageProduct").innerHTML = message.product.name;
        element.querySelector(".msgBtn").setAttribute("data-bs-whatever", message.message);
        element.querySelector(".msgBtn").setAttribute("id", message.id);
        element.querySelector(".msgBtn").addEventListener("click", () => {
            openModel(message.id)
        })
        messageTableBody.appendChild(element);

        count++;
    });
    document.getElementById("messageCount").innerHTML = msgList.length

}

var productId;
var pname;
var price;
var deliveryFee;
var desc;
var size;
var color;
var brand;
var category;

var isFirstTime = true
function openProductModel(pId) {
    const exampleModal = document.getElementById('exampleModal2')
    if (exampleModal) {
        exampleModal.addEventListener('show.bs.modal', event => {
            // Button that triggered the modal
            const button = event.relatedTarget
            // Extract info from data-bs-* attributes
            const recipient = button.getAttribute('data-bs-whatever')
            productId = pId;

            pname = button.getAttribute('name')
            price = button.getAttribute('price')
            deliveryFee = button.getAttribute('deliveryFee')
            desc = button.getAttribute('desc')
            size = button.getAttribute('size')
            color = button.getAttribute('color')
            brand = button.getAttribute('brand')
            category = button.getAttribute('category')

            // If necessary, you could initiate an Ajax request here
            // and then do the updating in a callback.

            // Update the modal's content.
            const modalTitle = exampleModal.querySelector('.modal-title')

            exampleModal.querySelector('#updatePName').value = pname
            exampleModal.querySelector('#updatePrice').value = price
            exampleModal.querySelector('#updateDF').value = deliveryFee
            exampleModal.querySelector('#updateDESC').value = desc

            if(isFirstTime){
                loadSelectOptions("updatecategory", categoryList, ["category"]);
                loadSelectOptions("updatesize", sizeList, ["size"]);
                loadSelectOptions("updatecolor", colorList, ["color"]);
                loadSelectOptions("updatebrand", brandList, ["brand"]);

                isFirstTime=false
            }

            exampleModal.querySelector('#updatesize').selectedIndex = size
            exampleModal.querySelector('#updatecolor').selectedIndex = color
            exampleModal.querySelector('#updatecategory').selectedIndex = category
            exampleModal.querySelector('#updatebrand').selectedIndex = brand

            modalTitle.innerHTML = `Update Product :<br><br> <span class="fs-5" style="color:grey;">${recipient}</span>`
        })
    }
}

function loadMyProducts(productList) {

    let tableBody = document.getElementById("pTableBody");
    let tableRow = document.getElementById("pTableRow");

    tableBody.innerHTML = "";

    let count = 1;
    productList.forEach(product => {

        console.log(product)

        let row = tableRow.cloneNode(true);

        row.removeAttribute("id");
        row.querySelector(".pCount").innerHTML = count;
        row.querySelector(".pName").innerHTML = product.name;
        row.querySelector(".pDesc").innerHTML = product.description;
        row.querySelector(".pAdded").innerHTML = product.added;
        row.querySelector(".pUnitPrice").innerHTML = product.unit_price;
        row.querySelector(".pColor").innerHTML = product.product_color.color;
        row.querySelector(".pSize").innerHTML = product.size.size;
        row.querySelector(".pCategory").innerHTML = product.category.category;
        row.querySelector(".pBrand").innerHTML = product.brand.brand;
        row.querySelector(".pSoldCount").innerHTML = product.sold_count;
        row.querySelector(".pButton").setAttribute("data-bs-whatever", product.name);
        row.querySelector(".pButton").addEventListener("click", () => {
            openProductModel(product.id)
        });
        row.querySelector(".pButton").setAttribute("id", product.id);
        row.querySelector(".pButton").setAttribute("name", product.name);
        row.querySelector(".pButton").setAttribute("price", product.unit_price);
        row.querySelector(".pButton").setAttribute("deliveryFee", product.delivery_fee);
        row.querySelector(".pButton").setAttribute("desc", product.description);
        row.querySelector(".pButton").setAttribute("size", product.size.id);
        row.querySelector(".pButton").setAttribute("color", product.product_color.id);
        row.querySelector(".pButton").setAttribute("brand", product.brand.id);
        row.querySelector(".pButton").setAttribute("category", product.category.id);

        tableBody.appendChild(row);

        count++;

    });

    document.getElementById("totalProductCount").innerHTML = productList.length

}

function loadSelectOptions(selectTagId, list, propertyArray) {
    const selectTag = document.getElementById(selectTagId);

    for (var i = 0; i < list.length; i++) {
        let optionTag = document.createElement("option");
        optionTag.value = list[i].id;
        optionTag.innerHTML = list[i][propertyArray[0]];
        selectTag.appendChild(optionTag);
    }
}

async function updateProduct() {
    const formData = new FormData();

    formData.append("name", document.getElementById("inputEmail4").value);
    formData.append("unit_price", document.getElementById("inputAddress").value);
    formData.append("delivery_fee", document.getElementById("inputAddress2").value);
    formData.append("size", document.getElementById("size").value);
    formData.append("color", document.getElementById("color").value);
    formData.append("category", document.getElementById("category").value);
    formData.append("description", document.getElementById("inputPassword4").value);
    formData.append("brand", document.getElementById("brand").value);
    formData.append("img1", document.getElementById("img1").files[0]);
    formData.append("img2", document.getElementById("img2").files[0]);
    formData.append("img3", document.getElementById("img3").files[0]);

    const response = await fetch("AddNewProduct", {
        method: "POST",
        body: formData
    });

    if (response.ok) {
        const jsonData = await response.json();
        if (jsonData.success) {
            Notification().success({
                message: jsonData.data
            })
        } else {
            Notification().error({
                message: jsonData.data
            })
        }
        console.log(jsonData);
    } else {
        Notification().error({
            message: "Faild to Add Product"
        })
        console.error("Failed to submit the form", response.statusText);
    }
}