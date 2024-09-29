const cle = document.getElementById("cartListElement");
let cartListParentH = document.getElementById("cartSideBarContainer");
cartListParentH.innerHTML=""

const loadCart = async () => {

    const response = await fetch("LoadCart");
    if (response.ok) {

        const jsonData = await response.json();
        if (jsonData.success) {

            const data = jsonData.data;

            let cartProductTotal = 0;
            cartListParentH.innerHTML = "";

            let isLoggedIn = data.isLoggedIn

            data.cartList.forEach((productObj) => {

                let element = cle.cloneNode(true);
                element.removeAttribute("id");


                let trimmedPath;
                if (!isLoggedIn) {

                    trimmedPath = productObj.img1.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");

                    element.querySelector(".cartProductName").innerHTML = productObj.name;
                    element.querySelector(".cartProductUnitPrice").innerHTML = "Rs. " + productObj.unit_price;

                    cartProductTotal += productObj.unit_price;

                } else if (isLoggedIn) {

                    trimmedPath = productObj.product.img1.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");

                    element.querySelector(".cartProductName").innerHTML = productObj.product.name;
                    element.querySelector(".cartProductUnitPrice").innerHTML = "Rs. " + productObj.product.unit_price;

                    cartProductTotal += productObj.product.unit_price;

                }

                element.querySelector("#cartImg").src = trimmedPath;

                cartListParentH.appendChild(element);


            });

            document.getElementById("cartProductTotal").innerHTML = "Total: " + cartProductTotal;
            document.querySelectorAll(".cartProductCount").forEach(el => {

                el.removeAttribute("data-notify")
                el.setAttribute("data-notify", data.cartList.length);
            })

            if (data.length == 0) {
                document.getElementById("cartProductTotal").innerHTML = "Total: 00.00";
            }
        } 

    } else {
        console.log(response)
    }
};
