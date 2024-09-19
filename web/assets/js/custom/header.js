var cartListElement;
const loadCart = async()=>{

    const response = await fetch("LoadCart");
    if(response.ok){

        const jsonData =await response.json();
        const data = jsonData.data;
        // console.log(data);

        let cartListParent = document.getElementById("cartSideBarContainer");
        cartListElement = document.getElementById("cartListElement");

        let cartProductTotal=0;
        cartListParent.innerHTML="";

        data.forEach((productObj)=>{

            // console.log(productObj);

            let element = cartListElement.cloneNode(true);
            element.removeAttribute("id");
            element.querySelector(".cartProductName").innerHTML=productObj.name;
            element.querySelector(".cartProductUnitPrice").innerHTML="Rs. "+productObj.unit_price;

            cartListParent.appendChild(element);

            cartProductTotal+=productObj.unit_price;

        });

        document.getElementById("cartProductTotal").innerHTML="Total: "+cartProductTotal;
    }
};
