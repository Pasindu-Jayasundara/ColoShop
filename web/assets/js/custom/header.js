const cartListElement = document.getElementById("cartListElement");
const loadCart = async () => {

    const response = await fetch("LoadCart");
    if (response.ok) {

        const jsonData = await response.json();
        const data = jsonData.data;
        // console.log(data);

        let cartListParent = document.getElementById("cartSideBarContainer");

        let cartProductTotal = 0;
        cartListParent.innerHTML = "";

        data.forEach((productObj) => {

            // console.log(productObj);

            let element = cartListElement.cloneNode(true);
            element.removeAttribute("id");
            element.querySelector(".cartProductName").innerHTML = productObj.name;
            element.querySelector(".cartProductUnitPrice").innerHTML = "Rs. " + productObj.unit_price;

            const trimmedPath = productObj.img1.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
            element.querySelector("#cartImg").src = trimmedPath;

            cartListParent.appendChild(element);

            cartProductTotal += productObj.unit_price;

        });

        document.getElementById("cartProductTotal").innerHTML = "Total: " + cartProductTotal;
        document.querySelectorAll(".cartProductCount").forEach(el=>{
            el.setAttribute("data-notify", data.length);
        })
    }
};
