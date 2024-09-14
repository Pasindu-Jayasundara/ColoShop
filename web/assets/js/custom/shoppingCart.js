window.addEventListener("load",()=>{
    loadCartData();
});

const checkSignIn = async()=>{

    const response = await fetch("LoadCart");
    if(response.ok){

        const jsonData =await response.json();
        const data = jsonData.data;
    }

};

var cartListElement;
const loadCartData = async()=>{

    const response = await fetch("LoadCart");
    if(response.ok){

        const jsonData =await response.json();
        const data = jsonData.data;
        // console.log(data);

        let cartListParent = document.getElementById("tableRowParent");
        cartListElement = document.getElementById("cartListElement");

        let cartProductTotal=0;
        cartListParent.innerHTML="";

        data.forEach((productObj)=>{

            // console.log(productObj);

            let element = cartListElement.cloneNode(true);
            element.removeAttribute("id");
            element.querySelector(".tableProductName").innerHTML=productObj.name;
            element.querySelector(".tableRoductPrice").innerHTML="Rs. "+productObj.unit_price;
            element.querySelector(".tableRoductPrice").setAttribute("id","changeProductUnitPrice"+productObj.id);
            element.querySelector(".tableProductTotal").innerHTML="Rs. "+productObj.unit_price;
            element.querySelector(".tableDisplayQty").setAttribute("id","changeProductQty"+productObj.id);
            element.querySelector(".tableProductTotal").setAttribute("id","changeProductPrice"+productObj.id);
            element.querySelector(".tableProductCountMinus").addEventListener("click",()=>{
                changeProductQty("changeProductQty"+productObj.id,"changeProductPrice"+productObj.id,"changeProductUnitPrice"+productObj.id)
            });
            element.querySelector(".tableProductCountPlus").addEventListener("click",()=>{
                changeProductQty("changeProductQty"+productObj.id,"changeProductPrice"+productObj.id,"changeProductUnitPrice"+productObj.id)
            });
            

            cartListParent.appendChild(element);

            cartProductTotal+=productObj.unit_price;

        });

        document.getElementById("cartProductTotal").innerHTML="Total: "+cartProductTotal;
    }
};

const changeProductQty = (elementId,productTotalElement,unitPrice)=>{

    let qty = parseInt(document.getElementById(elementId).value);
    let newPrice = unitPrice*qty;


    document.getElementById(productTotalElement).innerHTML="Rs. "+newPrice;


};