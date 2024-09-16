window.addEventListener("load", () => {
    loadCartData();
});

var cartListElement;
var buyArr = [];
var totalValue;
var deliveryFee = 0;
const loadCartData = async () => {

    const response = await fetch("LoadCart");
    if (response.ok) {

        const data = await response.json();
        console.log(data);

        let columnNames = document.getElementById("columnName");
        let cartListParent = document.getElementById("tableRowParent");
        cartListElement = document.getElementById("cartListElement");

        let cartProductTotal = 0;
        cartListParent.innerHTML = "";

        cartListParent.appendChild(columnNames);

        if (data.success) {
            data.data.forEach((productObj) => {

                // console.log(productObj);

                let element = cartListElement.cloneNode(true);
                element.removeAttribute("id");
                element.querySelector(".tableProductName").innerHTML = productObj.name;

                element.querySelector(".tableRoductPrice").innerHTML = "Rs. " + productObj.unit_price;
                element.querySelector(".tableRoductPrice").setAttribute("id", "changeProductUnitPrice" + productObj.id);

                element.querySelector(".tableProductTotal").innerHTML = "Rs. " + productObj.unit_price;
                element.querySelector(".tableDisplayQty").setAttribute("id", "changeProductQty" + productObj.id);
                element.querySelector(".tableProductTotal").setAttribute("id", "changeProductPrice" + productObj.id);


                // decrease qty
                element.querySelector(".tableProductCountMinus").addEventListener("click", () => {

                    value = parseInt(document.getElementById("changeProductQty" + productObj.id).value);
                    if (value > 1) {
                        value -= 1;
                        document.getElementById("changeProductQty" + productObj.id).value = value;

                        document.getElementById("changeProductPrice" + productObj.id).innerHTML = "Rs. " + value * productObj.unit_price;

                        buyArr.forEach((obj) => {
                            if (obj.id == productObj.id) {
                                obj.qty = parseInt(obj.qty) - 1
                            }
                        });

                        // document.getElementById("cartProductTotal").innerHTML = "Rs. " + value * productObj.unit_price;

                        total();
                        finaltotal();

                    }
                });

                deliveryFee += productObj.delivery_fee;
                document.getElementById("cartProductDeliveryFee").innerHTML = "Rs. " + deliveryFee;



                buyArr.push({
                    id: productObj.id,
                    unitPrice: productObj.unit_price,
                    deliveryFee: productObj.delivery_fee
                });

                // increase qty
                element.querySelector(".tableProductCountPlus").addEventListener("click", () => {

                    let value = parseInt(document.getElementById("changeProductQty" + productObj.id).value);
                    value += 1;
                    document.getElementById("changeProductQty" + productObj.id).value = value;

                    document.getElementById("changeProductPrice" + productObj.id).innerHTML = "Rs. " + value * productObj.unit_price;

                    buyArr.forEach((obj) => {
                        if (obj.id == productObj.id) {
                            obj.qty = parseInt(obj.qty) + 1
                        }
                    });

                    total();
                    finaltotal();

                    // document.getElementById("cartProductTotal").innerHTML = "Rs. " + value * productObj.unit_price;

                });

                // delete
                element.querySelector(".checkBox").setAttribute("id", "cb" + productObj.id);
                element.querySelector(".checkBox").addEventListener("click", () => {

                    addToDeleteArr(productObj.id);

                });



                cartListParent.appendChild(element);

                cartProductTotal += productObj.unit_price;

            });
            document.getElementById("cartProductTotal").innerHTML = "Rs. " + cartProductTotal;
            finaltotal();
        }


    }
};

function total() {

    let tot = 0;
    buyArr.forEach((obj) => {

        let value = parseInt(document.getElementById("changeProductQty" + obj.id).value);
        tot += value * obj.unitPrice;

    });

    document.getElementById("cartProductTotal").innerHTML = "Rs. " + tot;

}

function finaltotal() {

    let tot = 0;
    buyArr.forEach((obj) => {

        let value = parseInt(document.getElementById("changeProductQty" + obj.id).value);
        tot += value * obj.unitPrice;
        tot += obj.deliveryFee;

    });

    document.getElementById("fulTotal").innerHTML = "Rs. " + tot;

}

var deleteArr = undefined;
const addToDeleteArr = (proId) => {

    if (deleteArr == undefined) {//no

        deleteArr = proId;
        document.getElementById("deleteContainer").innerHTML = `
            <button type="button" onclick="deleteFromCart(${proId})" class="flex-c-m stext-101 cl2 size-119 bg8 bor13 hov-btn3 p-lr-15 trans-04 pointer m-tb-10" id="deleteProduct">
                Delete Product
            </button>
        `;

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

    const response = await fetch("CheckSignIn", {
        method: "POST",
        body: JSON.stringify({
            address: document.getElementById("address").value,
            text: document.getElementById("otherText").value
        }),
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (response.ok) {

        const jsonData = await response.json();
        if (jsonData.data.isLoggedIn) {

            text = jsonData.data.text;
            address = jsonData.data.address;

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

        if (!text) {
            // wrong text
        } else if (!address) {
            //wrong address
        } else {

            // Payment completed. It can be a successful failure.
            payhere.onCompleted = function onCompleted(orderId) {
                console.log("Payment completed. OrderID:" + orderId);
                // Note: validate the payment and show success or failure page to the customer
                popup.success({
                    message: "Thank you, Payment completed!"
                });
                window.location = "index.html";
            };

            // Payment window closed
            payhere.onDismissed = function onDismissed() {
                // Note: Prompt user to pay again or show an error page
                console.log("Payment dismissed");
            };

            // Error occurred
            payhere.onError = function onError(error) {
                // Note: show an error page
                console.log("Error:" + error);
            };

        }




    } else {

    }
}