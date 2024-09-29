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

const loadCartData = async () => {

    const response = await fetch("LoadCart");
    if (response.ok) {

        const data = await response.json();
        console.log(data);

        let columnNames = document.getElementById("columnName");
        let cartListParent = document.getElementById("tableRowParent");
        cartListElement = document.getElementById("cle");

        let cartProductTotal = 0;
        cartListParent.innerHTML = "";

        cartListParent.appendChild(columnNames);

        if (data.success) {
            let list = data.data.cartList
            list.forEach((productObj) => {

                // console.log(productObj);

                let element = cartListElement.cloneNode(true);
                element.removeAttribute("id");
                element.setAttribute("id", "cpr" + productObj.product.id);
                element.querySelector(".tableProductName").innerHTML = productObj.product.name;
                element.querySelector(".tableRoductPrice").innerHTML = "Rs. " + new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
                ).format(productObj.product.unit_price);

                let trimmedPath = productObj.product.img1.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
                element.querySelector(".proimg").src = trimmedPath;

                element.querySelector(".deleteFromCart").addEventListener("click", () => {

                    if (delElement != document.getElementById("cpr" + productObj.product.id)) {
                        if (delElement != undefined) {
                            delElement.style.backgroundColor = "white"
                        }
                        delElement = document.getElementById("cpr" + productObj.product.id)
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
                    addToDeleteArr(productObj.product.id);
                });

                element.querySelector(".tableRoductPrice").setAttribute("id", "changeProductUnitPrice" + productObj.product.id);
                element.querySelector(".tableProductTotal").innerHTML = "Rs. " + new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
                ).format(productObj.product.unit_price);
                element.querySelector(".tableDisplayQty").setAttribute("id", "changeProductQty" + productObj.product.id);
                element.querySelector(".tableProductTotal").setAttribute("id", "changeProductPrice" + productObj.product.id);


                // decrease qty
                element.querySelector(".tableProductCountMinus").addEventListener("click", () => {

                    value = parseInt(document.getElementById("changeProductQty" + productObj.product.id).value);
                    if (value > 1) {
                        value -= 1;
                        document.getElementById("changeProductQty" + productObj.product.id).value = value;

                        document.getElementById("changeProductPrice" + productObj.product.id).innerHTML = "Rs. " + new Intl.NumberFormat(
                            "en-US",
                            {
                                minimumFractionDigits: 2
                            }
                        ).format(value * productObj.product.unit_price);


                        // Update the qty in allProductArr
                        let foundInAllProducts = allProductArr.find(obj => obj.id === productObj.product.id);
                        if (foundInAllProducts) {
                            foundInAllProducts.qty = parseInt(foundInAllProducts.qty) - 1;
                        }

                        // Update the qty in buyArr if the product already exists
                        let foundInBuyArr = buyArr.find(buyObj => buyObj.id === productObj.product.id);
                        if (foundInBuyArr) {
                            foundInBuyArr.qty = parseInt(foundInBuyArr.qty) - 1;

                            updateTable()
                        }

                        // document.getElementById("cartProductTotal").innerHTML = "Rs. " + value * productObj.unit_price;

                        // total();
                        // finaltotal();

                    }
                });

                // deliveryFee += productObj.product.delivery_fee;
                // document.getElementById("cartProductDeliveryFee").innerHTML = "Rs. " + deliveryFee;



                // buyArr.push({
                allProductArr.push({
                    id: productObj.product.id,
                    unitPrice: productObj.product.unit_price,
                    deliveryFee: productObj.product.delivery_fee,
                    qty: 1,
                    name: productObj.product.name
                });

                // increase qty
                element.querySelector(".tableProductCountPlus").addEventListener("click", () => {

                    let value = parseInt(document.getElementById("changeProductQty" + productObj.product.id).value);
                    value += 1;
                    document.getElementById("changeProductQty" + productObj.product.id).value = value;

                    document.getElementById("changeProductPrice" + productObj.product.id).innerHTML = "Rs. " + new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }
                    ).format(value * productObj.product.unit_price);

                    // Update the qty in allProductArr
                    let foundInAllProducts = allProductArr.find(obj => obj.id === productObj.product.id);
                    if (foundInAllProducts) {
                        foundInAllProducts.qty = parseInt(foundInAllProducts.qty) + 1;
                    }

                    // Update the qty in buyArr if the product already exists
                    let foundInBuyArr = buyArr.find(buyObj => buyObj.id === productObj.product.id);
                    if (foundInBuyArr) {
                        foundInBuyArr.qty = parseInt(foundInBuyArr.qty) + 1;

                        updateTable()
                    }




                    // total();
                    // finaltotal();

                    // document.getElementById("cartProductTotal").innerHTML = "Rs. " + value * productObj.unit_price;

                });

                // purchase
                element.querySelector(".checkBox").setAttribute("id", "cb" + productObj.product.id);
                element.querySelector(".checkBox").addEventListener("click", (event) => {

                    if (event.target.checked) {
                        if (!buyArr.some(item => item.id === productObj.product.id)) {

                            let foundObj = allProductArr.find(obj => obj.id === productObj.product.id);

                            if (foundObj) {

                                buyArr.push(foundObj);
                                updateTable()
                            }
                        }
                    } else {
                        buyArr = buyArr.filter(item => item.id !== productObj.product.id);

                        updateTable()
                    }

                    console.log(buyArr);
                });


                cartListParent.appendChild(element);

                // cartProductTotal += productObj.product.unit_price;

            });
            // document.getElementById("cartProductTotal").innerHTML = "Rs. " + cartProductTotal;
            // finaltotal();
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
    })
    total = subtotal + deliveryFee

    document.querySelector(".cartProductTotal").innerHTML = "Rs. "+new Intl.NumberFormat(
        "en-US",
        {
            minimumFractionDigits: 2
        }
    ).format(total)

}

// function total() {
//     console.log("total")
//     let tot = 0;
//     buyArr.forEach((obj) => {

//         let value = parseInt(document.getElementById("changeProductQty" + obj.id).value);
//         tot += value * obj.unitPrice;

//     });

//     document.getElementById("cartProductTotal").innerHTML = "Rs. " + tot;

// }

// function finaltotal() {

//     let tot = 0;
//     buyArr.forEach((obj) => {

//         let value = parseInt(document.getElementById("changeProductQty" + obj.id).value);
//         tot += value * obj.unitPrice;
//         tot += obj.deliveryFee;

//     });

//     document.getElementById("fulTotal").innerHTML = "Rs. " + tot;

// }

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


            const jsonData = await response.json();
            console.log(jsonData)

        }
    }

};


var text = "";
var address = "";
const checkSignIn = async () => {

    const response = await fetch("CheckSignIn");
    if (response.ok) {

        const jsonData = await response.json();
        if (jsonData.data.isSignedIn) {
            return true;
        } else {
            return false;
        }
        const data = jsonData.data;
    }

};

async function procedToCheckout() {
    const isLoggedIn = await checkSignIn();
    if (isLoggedIn) {

        if (address.trim() == "") {
            //wrong address
            popup.info({
                message: "Please Fill The Address"
            });
        } else {

            // Payment completed. It can be a successful failure.
            payhere.onCompleted = function onCompleted(orderId) {
                console.log("Payment completed. OrderID:" + orderId);
                // Note: validate the payment and show success or failure page to the customer
                popup.success({
                    message: "Order Placed, Thank You!"
                });
                // window.location = "index.html";
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

            let paymentJson = jsonData.data 
            payhere.startPayment(paymentJson);

        }




    } else {

        popup.info({
            message: "Please Login First"
        });

    }
}