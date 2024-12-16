// Payment completed. It can be a successful failure.
payhere.onCompleted = function onCompleted(orderId) {
    console.log("Payment completed. OrderID:" + orderId);
    // Note: validate the payment and show success or failure page to the customer
    popup.success({
        message: "Order Placed, Thank You!"
    });
};

// Payment window closed
payhere.onDismissed = function onDismissed() {
    // Note: Prompt user to pay again or show an error page
    console.log("Payment dismissed");
    popup.info({
        message: "Payment Dismissed"
    });
};

// Error occurred
payhere.onError = function onError(error) {
    // Note: show an error page
    console.log("Error:" + error);
    popup.error({
        message: "Payment Error, Please Try Again Later!"
    });
};

window.addEventListener("load", () => {
    loadCartData();
});

const popup = Notification();

var cartListElement;
var buyArr = [];
var allProductArr = []
var totalValue;
var deliveryFee = 0;
var delElement
var cartListParent = document.getElementById("tableRowParent");
let columnNames = document.getElementById("columnName");
cartListElement = document.getElementById("cle");
cartListParent.innerHTML = "";

const loadCartData = async () => {

    cartListParent.innerHTML = "";

    const response = await fetch("LoadCart");
    if (response.ok) {

        const data = await response.json();
        console.log(data);

        // let cartProductTotal = 0;

        cartListParent.appendChild(columnNames);

        if (data.success) {

            let list = data.data.cartList
            list.forEach((productObj) => {

                let id;
                let name;
                let unitPrice;
                let img1;

                if(data.data.isLoggedIn){
                    id = productObj.product.id;
                    name = productObj.product.name;
                    unitPrice = productObj.product.unit_price;
                    img1 = productObj.product.img1;
                }else{
                    id = productObj.id;
                    name = productObj.name;
                    unitPrice = productObj.unit_price;
                    img1 = productObj.img1;
                }
                console.log(id)

                let element = cartListElement.cloneNode(true);
                element.removeAttribute("id");
                element.setAttribute("id", "cpr" + id);
                element.querySelector(".tableProductName").innerHTML = name;
                element.querySelector(".tableRoductPrice").innerHTML = "Rs. " + new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
                ).format(unitPrice);

                let trimmedPath = img1.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
                element.querySelector(".proimg").src = trimmedPath;

                element.querySelector(".deleteFromCart").addEventListener("click", () => {

                    if (delElement != document.getElementById("cpr" + id)) {
                        if (delElement != undefined) {
                            delElement.style.backgroundColor = "white"
                        }
                        delElement = document.getElementById("cpr" + id)
                        delElement.style.backgroundColor = "grey"

                    } else {
                        if (delElement != undefined) {
                            if (delElement.style.backgroundColor == "grey") {
                                delElement.style.backgroundColor = "white"
                            } else {
                                delElement.style.backgroundColor = "grey"
                            }
                        }
                    }
                    addToDeleteArr(id);
                });

                element.querySelector(".tableRoductPrice").setAttribute("id", "changeProductUnitPrice" + id);
                element.querySelector(".tableProductTotal").innerHTML = "Rs. " + new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
                ).format(unitPrice);
                element.querySelector(".tableDisplayQty").setAttribute("id", "changeProductQty" + id);
                element.querySelector(".tableProductTotal").setAttribute("id", "changeProductPrice" + id);


                // decrease qty
                element.querySelector(".tableProductCountMinus").addEventListener("click", () => {

                    let foundInBuyArr = buyArr.find(buyObj => buyObj.id === id);
                    if (foundInBuyArr) {
                        if (foundInBuyArr.qty > 1) {

                            foundInBuyArr.qty = parseInt(foundInBuyArr.qty) - 1;

                            let value = foundInBuyArr.qty;
                            document.getElementById("changeProductQty" + id).value = value;

                            document.getElementById("changeProductPrice" + id).innerHTML = "Rs. " + new Intl.NumberFormat(
                                "en-US",
                                {
                                    minimumFractionDigits: 2
                                }
                            ).format(value * unitPrice);

                            updateTable()

                            let foundInAllProducts = allProductArr.find(obj => obj.id === id);
                            if (foundInAllProducts) {
                                foundInAllProducts.qty = foundInBuyArr.qty;
                            }
                        }

                    } else {

                        let foundInAllProducts = allProductArr.find(obj => obj.id === id);
                        if (foundInAllProducts) {
                            if (foundInAllProducts.qty > 1) {
                                foundInAllProducts.qty = parseInt(foundInAllProducts.qty) - 1;

                                let value = foundInAllProducts.qty;
                                document.getElementById("changeProductQty" + id).value = value;

                                document.getElementById("changeProductPrice" + id).innerHTML = "Rs. " + new Intl.NumberFormat(
                                    "en-US",
                                    {
                                        minimumFractionDigits: 2
                                    }
                                ).format(value * unitPrice);
                            }
                        }

                    }

                });

                if(data.data.isLoggedIn){
                    allProductArr.push({
                        id: productObj.product.id,
                        unitPrice: productObj.product.unit_price,
                        deliveryFee: productObj.product.delivery_fee,
                        qty: 1,
                        name: productObj.product.name
                    });
                }else{
                    allProductArr.push({
                        id: productObj.id,
                        unitPrice: productObj.unit_price,
                        deliveryFee: productObj.delivery_fee,
                        qty: 1,
                        name: productObj.name
                    });
                }

                //increse qty
                element.querySelector(".tableProductCountPlus").addEventListener("click", () => {

                    let foundInBuyArr = buyArr.find(buyObj => buyObj.id === id);
                    if (foundInBuyArr) {

                        foundInBuyArr.qty = parseInt(foundInBuyArr.qty) + 1;

                        let value = foundInBuyArr.qty;
                        document.getElementById("changeProductQty" + id).value = value;

                        document.getElementById("changeProductPrice" + id).innerHTML = "Rs. " + new Intl.NumberFormat(
                            "en-US",
                            {
                                minimumFractionDigits: 2
                            }
                        ).format(value * unitPrice);

                        updateTable();

                        let foundInAllProducts = allProductArr.find(obj => obj.id === id);
                        if (foundInAllProducts) {
                            foundInAllProducts.qty = foundInBuyArr.qty;
                        }
                    } else {

                        let foundInAllProducts = allProductArr.find(obj => obj.id === id);
                        if (foundInAllProducts) {
                            foundInAllProducts.qty = parseInt(foundInAllProducts.qty) + 1;

                            let value = foundInAllProducts.qty;
                            document.getElementById("changeProductQty" + id).value = value;


                            document.getElementById("changeProductPrice" + id).innerHTML = "Rs. " + new Intl.NumberFormat(
                                "en-US",
                                {
                                    minimumFractionDigits: 2
                                }
                            ).format(value * unitPrice);
                        }

                    }

                });


                // purchase
                element.querySelector(".checkBox").setAttribute("id", "cb" + id);
                element.querySelector(".checkBox").addEventListener("click", (event) => {

                    if (event.target.checked) {
                        if (!buyArr.some(item => item.id === id)) {

                            let foundObj = allProductArr.find(obj => obj.id === id);

                            if (foundObj) {
                                console.log("c : " + JSON.stringify(foundObj))
                                buyArr.push(foundObj);
                                updateTable()
                            }
                        }
                    } else {
                        buyArr = buyArr.filter(item => item.id !== id);

                        updateTable()
                    }

                    console.log(buyArr);
                });


                cartListParent.appendChild(element);

            });
        }

        loadCart()

    }
};

//table
var body = document.getElementById("pTBody")
var tr = document.getElementById("ptTr")

function updateTable() {

    body.innerHTML = ""

    let subtotal = 0.0
    let deliveryFee = 0.0
    let total = 0.0

    buyArr.forEach(item => {

        let element = tr.cloneNode(true)
        element.removeAttribute("id")
        element.setAttribute("id", "tr" + item.id)
        element.querySelector(".ptrname").innerHTML = item.name
        element.querySelector(".ptrqty").innerHTML = item.qty
        element.querySelector(".ptrprice").innerHTML = new Intl.NumberFormat(
            "en-US",
            {
                minimumFractionDigits: 2
            }
        ).format(item.qty * item.unitPrice)

        element.querySelector(".ptrdf").innerHTML = new Intl.NumberFormat(
            "en-US",
            {
                minimumFractionDigits: 2
            }
        ).format(item.deliveryFee)

        body.appendChild(element)

        subtotal += item.qty * item.unitPrice
        deliveryFee += item.deliveryFee

        console.log(item.qty)
    })
    total = subtotal + deliveryFee

    document.querySelector(".cartProductTotal").innerHTML = "Rs. " + new Intl.NumberFormat(
        "en-US",
        {
            minimumFractionDigits: 2
        }
    ).format(total)

}


var deleteArr = undefined;
var prevEl
const addToDeleteArr = (proId) => {

    if (deleteArr == undefined) {//no

        deleteArr = proId;
        document.getElementById("deleteContainer").innerHTML = `
            <button type="button" onclick="deleteFromCart(${proId})" class="flex-c-m stext-101 cl2 size-119 bg8 bor13 hov-btn3 p-lr-15 trans-04 pointer m-tb-10" id="deleteProduct">
                Delete Product
            </button>
        `;

        prevEl = document.getElementById("cpr" + proId)

    } else if (deleteArr != undefined && prevEl != document.getElementById("cpr" + proId)) {//no

        deleteArr = proId;
        document.getElementById("deleteContainer").innerHTML = `
            <button type="button" onclick="deleteFromCart(${proId})" class="flex-c-m stext-101 cl2 size-119 bg8 bor13 hov-btn3 p-lr-15 trans-04 pointer m-tb-10" id="deleteProduct">
                Delete Product
            </button>
        `;

        prevEl = document.getElementById("cpr" + proId)

    } else {
        deleteArr = undefined;
        document.getElementById("deleteContainer").innerHTML = "";
    }
};

const deleteFromCart = async (productId) => {

    if (deleteArr != undefined) {

        let obj = {
            id: productId
        };

        const response = await fetch("DeleteFromCart", {
            method: "POST",
            body: JSON.stringify(obj),
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {


            buyArr.forEach(obj => {
                if (obj.id == productId) {
                    deliveryFee -= obj.deliveryFee;
                    document.getElementById("cartProductDeliveryFee").innerHTML = "Rs. " + deliveryFee;
                }
            });

            loadCartData()
            loadCart()

            const jsonData = await response.json();
            if (jsonData.success) {

                popup.success({
                    message: jsonData.data
                });

                window.location.reload();

            } else {
                popup.error({
                    message: "Please Try Again Later"
                });
            }
            console.log(jsonData)

        } else {
            popup.error({
                message: "Please Try Again Later"
            });
        }
    }

};

var isLoggedIn = false
const checkSignIn = async () => {

    const response = await fetch("CheckSignIn");
    if (response.ok) {

        const jsonData = await response.json();
        if (jsonData.success) {
            isLoggedIn = true;

            procedToCheckout()
        } else {
            popup.info({
                message: "Please Login First"
            });
        }
    } else {
        popup.info({
            message: "Please Try Again Later"
        });
    }

};

async function procedToCheckout() {
    if (isLoggedIn) {

        let txt = document.getElementById("otherText").value;
        let addr = document.getElementById("address").value;

        if (addr.trim() == "") {
            //wrong address
            popup.info({
                message: "Please Fill The Address"
            });
        } else if (buyArr.length == 0) {
            //wrong address
            popup.info({
                message: "Please Select What You Want To Buy"
            });
        } else {

            const obj = {
                address: addr,
                array: buyArr,
                text: txt
            }
            const response = await fetch("Checkout", {
                method: "POST",
                body: JSON.stringify(obj),
                headers: {
                    "Content-Type": "application/json"
                }
            });

            if (response.ok) {

                const jsonData = await response.json();
                if (jsonData.success) {

                    let paymentJson = jsonData.data
                    payhere.startPayment(paymentJson);
                } else {
                    popup.error({
                        message: jsonData.data
                    });
                }

            } else {
                popup.error({
                    message: "Please Try Again Later"
                });
            }
        }

    }
}