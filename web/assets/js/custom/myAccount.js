window.addEventListener("load", () => {

    loadData()

})

function clearFormFields() {
    document.getElementById("inputEmail4").value = "";
    document.getElementById("inputAddress").value = "";
    document.getElementById("inputAddress2").value = "";
    document.getElementById("size").value = "";
    document.getElementById("color").value = "";
    document.getElementById("category").value = "";
    document.getElementById("inputPassword4").value = "";
    document.getElementById("brand").value = "";
    document.getElementById("img1").value = "";
    document.getElementById("img2").value = "";
    document.getElementById("img3").value = "";
}

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
            clearFormFields()
            document.getElementById("totalProductCount").innerHTML = parseInt(document.getElementById("totalProductCount").innerHTML) + 1

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

var switchbtn

var categoryList;
var colorList;
var sizeList;
var brandList;
var msgToSellerList;
var orderList
async function loadData() {

    const response = await fetch("LoadFeatures");
    if (response.ok) {

        const json = await response.json();
        console.log(json)

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
                msgToSellerList = json.data.msgToSellerList;
                loadMessages(msgToSellerList)
            }

            document.getElementById("userName").innerHTML = userName;

            console.log(json.data)
            orderList = json.data.orderList;
            if (isBuyer) {
                document.getElementById("accountType").innerHTML = "Buyer";
                document.getElementById("oppositeAccountType").innerHTML = "Seller";

                document.getElementById("addN").style.display = "none";
                document.getElementById("myP").style.display = "none";
                document.getElementById("msgS").style.display = "none";
                document.getElementById("or").style.display = "none";
                document.getElementById("totalProducts").style.display = "none";
                document.getElementById("msg").style.display = "none";

                loadPurchasedOrders(orderList)
                console.log("1: "+orderList)

            } else if (isSeller) {

                document.getElementById("myOrders").style.display = "none";
                document.getElementById("accountType").innerHTML = "Seller";
                document.getElementById("oppositeAccountType").innerHTML = "Buyer";

                loadReceivedOrders(orderList)

                console.log("2 :"+orderList)
            }

            switchbtn = document.getElementById("switch").innerHTML

            loadSelectOptions("category", categoryList, ["category"]);
            loadSelectOptions("size", sizeList, ["size"]);
            loadSelectOptions("color", colorList, ["color"]);
            loadSelectOptions("brand", brandList, ["brand"]);

            document.getElementById("msgS").classList.add("visible-false")
        } else {
            Notification().error({
                message: json.data
            })
            if(json.data=="Please logIn First"){
                sessionStorage.removeItem("user")
                window.location.href = "sign-in.html"
            }
        }

    } else {
        Notification().error({
            message: "Please Try Again Later"
        })
        window.location.href = "sign-in.html"

        console.log(response.body)
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

                removeMsgTableRow()
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
    }else{
        document.getElementById("reply").style.border = "1px solid red"
    }
}

function removeMsgTableRow(){
    msgToSellerList = msgToSellerList.filter(msg => msg.id !== messageId);
    document.getElementById("messageCount").innerHTML = msgToSellerList.length
    loadMessages(msgToSellerList)
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
            if (jsonData.data.status == "Delivered") {
                document.getElementById("pendingOrderCount").innerHTML = parseInt(document.getElementById("pendingOrderCount").innerHTML) - 1
            }

            Notification().success({
                message: jsonData.data.message
            })

            setTimeout(() => {
               orderList = orderList.filter(order => order.orders.id !== orderId); 
                loadReceivedOrders(orderList)
            }, 3000);
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
                logOut()
            } else if (!jsonData.success && jsonData.data == "Not Delivered Products Avaliable") {
                Notification().error({
                    message: jsonData.data
                })
                document.getElementById("switch").innerHTML = switchbtn
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

let otableBody = document.getElementById("oTableBody");
let orow = document.getElementById("oTableRow");

function loadPurchasedOrders(orderList) {

    otableBody.innerHTML = "";

    let previousOrderIdArr = [];
    let element;

    orderList.forEach(item => {

        let isNew = true;
        previousOrderIdArr.forEach(id => {

            if (id == item.orders.id) {

                // item
                let ulTag = document.getElementById("ul" + id)
                let ulLiTag = document.getElementById("li" + id)

                let newLiTag = ulLiTag.cloneNode(true);
                newLiTag.innerHTML = item.product.name

                ulTag.appendChild(newLiTag)

                // qty
                let qtyulTag = document.getElementById("qtyul" + id)
                let qtyulLiTag = document.getElementById("qtyli" + id)

                let qtynewLiTag = qtyulLiTag.cloneNode(true);
                qtynewLiTag.innerHTML = item.qty

                qtyulTag.appendChild(qtynewLiTag)


                isNew = false
                return;
            }
        });

        if (isNew) {
            element = orow.cloneNode(true);

            element.removeAttribute("id");
            element.querySelector(".oId").innerHTML = item.id;
            element.querySelector(".oPaid").innerHTML = item.orders.total_amount;
            element.querySelector(".oAddress").innerHTML = item.orders.address;
            element.querySelector(".oOtherText").innerHTML = item.orders.text;
            element.querySelector(".oStatus").innerHTML = item.orders.order_status.status;

            //qty
            element.querySelector(".rOtyLi").innerHTML = item.qty;
            element.querySelector(".rOtyLi").setAttribute("id", "qtyli" + item.orders.id)
            element.querySelector(".rOtyParent").setAttribute("id", "qtyul" + item.orders.id)

            //item
            element.querySelector(".rItemsLi").innerHTML = item.product.name;
            element.querySelector(".rItemsLi").setAttribute("id", "li" + item.orders.id)
            element.querySelector(".rItemsParent").setAttribute("id", "ul" + item.orders.id)

            otableBody.appendChild(element);

            previousOrderIdArr.push(item.orders.id);
        }
    });
    document.getElementById("pendingOrderCount").innerHTML = orderList.length

}

var rtableBody = document.getElementById("rTableBody");
var row = document.getElementById("rTableRow");
rtableBody.innerHTML = "";

function loadReceivedOrders(orderList) {

    rtableBody.innerHTML = "";
    let previousOrderIdArr = [];
    let element;

    orderList.forEach(arrElement => {

        let isNew = true;

        previousOrderIdArr.forEach(id => {

            if (id == arrElement.orders.id) {

                // item
                let ulTag = document.getElementById("ul" + id)
                let ulLiTag = document.getElementById("li" + id)

                let newLiTag = ulLiTag.cloneNode(true);
                newLiTag.innerHTML = arrElement.product.name

                ulTag.appendChild(newLiTag)

                // qty
                let qtyulTag = document.getElementById("qtyul" + id)
                let qtyulLiTag = document.getElementById("qtyli" + id)

                let qtynewLiTag = qtyulLiTag.cloneNode(true);
                qtynewLiTag.innerHTML = arrElement.qty

                qtyulTag.appendChild(qtynewLiTag)


                isNew = false
                return;
            }
        });
        if (isNew) {

            element = row.cloneNode(true);

            let txt;
            if (arrElement.orders.text == undefined) {
                txt = "-"
            } else {
                txt = arrElement.orders.text
            }
            element.removeAttribute("id");
            element.querySelector(".rId").innerHTML = arrElement.orders.id;
            element.querySelector(".rPaid").innerHTML = arrElement.orders.total_amount;
            element.querySelector(".rAddress").innerHTML = arrElement.orders.address;
            element.querySelector(".rOtherText").innerHTML = txt;
            element.querySelector(".rStatusBth").innerHTML = arrElement.orders.order_status.status;
            element.querySelector(".rStatusBth").setAttribute("id", "r" + arrElement.orders.id);
            element.querySelector(".rStatusBth").addEventListener("click", () => {
                changeOrderStatus(arrElement.orders.id)
                // alert(arrElement.orders.id)
                // console.log(arrElement.orders.id)
            })

            //qty
            element.querySelector(".rOtyLi").innerHTML = arrElement.qty;
            element.querySelector(".rOtyLi").setAttribute("id", "qtyli" + arrElement.orders.id)
            element.querySelector(".rOtyParent").setAttribute("id", "qtyul" + arrElement.orders.id)

            //item
            element.querySelector(".rItemsLi").innerHTML = arrElement.product.name;
            element.querySelector(".rItemsLi").setAttribute("id", "li" + arrElement.orders.id)
            element.querySelector(".rItemsParent").setAttribute("id", "ul" + arrElement.orders.id)
            previousOrderIdArr.push(arrElement.orders.id);

            rtableBody.appendChild(element);
        }
    });

    document.getElementById("pendingOrderCount").innerHTML = orderList.length
}


let messageTableBody = document.getElementById("mTableBody");
let messageRow = document.getElementById("mTableRow");
messageTableBody.innerHTML = "";

function loadMessages(msgList) {

    messageTableBody.innerHTML = "";

    let count = 1;
    msgList.forEach(message => {

        console.log(message)
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

            if (isFirstTime) {
                loadSelectOptions("updatecategory", categoryList, ["category"]);
                loadSelectOptions("updatesize", sizeList, ["size"]);
                loadSelectOptions("updatecolor", colorList, ["color"]);
                loadSelectOptions("updatebrand", brandList, ["brand"]);

                isFirstTime = false
            }

            exampleModal.querySelector('#updatesize').selectedIndex = size
            exampleModal.querySelector('#updatecolor').selectedIndex = color
            exampleModal.querySelector('#updatecategory').selectedIndex = category
            exampleModal.querySelector('#updatebrand').selectedIndex = brand

            modalTitle.innerHTML = `Update Product :<br><br> <span class="fs-5" style="color:grey;">${recipient}</span>`
        })
    }
}

var tableBody = document.getElementById("pTableBody");
var tableRow = document.getElementById("pTableRow");

function loadMyProducts(productList) {

    tableBody.innerHTML = "";

    let count = 1;
    productList.forEach(product => {

        let row = tableRow.cloneNode(true);

        row.setAttribute("id", "row" + product.id);
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

    let name = document.getElementById("updatePName").value
    let unit_price = document.getElementById("updatePrice").value
    let delivery_fee = document.getElementById("updateDF").value
    let size = document.getElementById("updateDESC").value
    let color = document.getElementById("updatesize").value
    let category = document.getElementById("updatecolor").value
    let description = document.getElementById("updatecategory").value
    let brand = document.getElementById("updatebrand").value

    if (name.trim() == ""
        || unit_price.trim() == ""
        || delivery_fee.trim() == ""
        || size.trim() == ""
        || color.trim() == ""
        || category.trim() == ""
        || brand.trim() == ""
        || description.trim() == ""
    ) {

        Notification().error({
            message: "Missing Data"
        })

    } else {

        const data = {
            name: document.getElementById("updatePName").value,
            unit_price: document.getElementById("updatePrice").value,
            delivery_fee: document.getElementById("updateDF").value,
            size: document.getElementById("updatesize").value,
            color: document.getElementById("updatecolor").value,
            category: document.getElementById("updatecategory").value,
            description: document.getElementById("updateDESC").value,
            brand: document.getElementById("updatebrand").value,
            id: productId
        }

        console.log(data)
        const response = await fetch("UpdateProduct", {
            method: "POST",
            body: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const jsonData = await response.json();
            if (jsonData.success) {

                loadData()

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
                message: "Please Try Again Later"
            })
            console.error(response);
        }
    }
}

async function deleteProduct() {

    const response = await fetch("DeleteProduct", {
        method: "POST",
        body: JSON.stringify({
            id: productId
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
            document.getElementById("totalProductCount").innerHTML = parseInt(document.getElementById("totalProductCount").innerHTML) - 1

            tableBody.removeChild(document.getElementById("row" + productId))
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

async function logOut() {
    const response = await fetch("LogOut");

    const popup = Notification();

    if (response.ok) {

        const data = await response.json();
        if (data.success) {

            sessionStorage.removeItem("user");

            window.location = "sign-in.html";

        } else {
            popup.error({
                message: data.data
            });
        }
    } else {
        popup.error({
            message: "Please Try Again Later"
        })

        console.log(response)
    }
}