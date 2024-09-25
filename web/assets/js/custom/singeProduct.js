window.addEventListener("load", () => {

    let url = window.location.href;
    const splitArr = url.split("?");

    let productId = splitArr[1].split("=")[1];

    loadSingleProduct(productId);

});

var pId;
const loadSingleProduct = async (productId) => {

    const data = {
        "id": productId
    }

    const response = await fetch("LoadSingleProduct", {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {

        console.log(response)

        const jsonData = await response.json();
        const data = jsonData.data;

        console.log(data)

        pId=data.id;
        document.getElementById("productName").innerHTML=data.name;
        document.getElementById("productPrice").innerHTML="Price : Rs. "+data.unit_price;
        document.getElementById("productDesc").innerHTML=data.description;
        document.getElementById("productSize").innerHTML="Size :"+data.size.size;
        document.getElementById("productColor").innerHTML="Color :"+data.product_color.color;

        document.getElementById("descTab").innerHTML=data.description;
        document.getElementById("tabCategory").innerHTML=data.category.category;
        document.getElementById("tabBrand").innerHTML=data.brand.brand;
        document.getElementById("tabColor").innerHTML=data.product_color.color;
        document.getElementById("tabSize").innerHTML=data.size.size;

        // loadReview(productId);
        loadSimilarProducts();
        
    }

};

const loadReview = async(productId)=>{

    const data = {
        "id":productId
    }

    const response = await fetch("loadSimilarProduct",{
        method:"POST",
        body:JSON.stringify(data),
        headers:{
            "Content-Type":"application/json"
        }
    });

    if (response.ok) {
        console.log(response);

        let jsonData = await response.json();
        let data = jsonData.data;
    }

};

var isProductFirstTime =true;
const productArr = [];
const loadSimilarProducts = async () => {

    console.log("similar product")
    const data = {
        "id":pId
    }

    const response = await fetch("loadSimilarProduct",{
        method:"POST",
        body:JSON.stringify(data),
        headers:{
            "Content-Type":"application/json"
        }
    });

    if (response.ok) {

        console.log(response)
        const data = await response.json();
        const productData = data.data;

        let parent = document.getElementById("productContainer");
        let productElement = document.getElementById("productElement");
        parent.innerHTML = "";
        for (var i = 1; i <= productData.length; i++) {

            let product = productData[i - 1];
            // console.log(product);

            productArr.push(product);
            
            if (isProductFirstTime) {
                parent.innerHTML = "";
                isProductFirstTime = false;
            }

            //get product element
            let element = productElement.cloneNode(true);
            element.querySelector(".productName").innerHTML = product.name;
            element.querySelector(".productPrice").innerHTML = "Rs. "+product.unit_price;
            element.querySelector(".productElementATag").addEventListener("click",(e)=>{
                e.preventDefault();
                loadQuickView(product.id);
            });

            element.classList.replace("women",product.category.category);
            element.removeAttribute("style");
            element.querySelector(".addToWishlist").addEventListener("click",(e)=>{
                e.preventDefault();
                addToWishlist(product.id)
            });

            parent.appendChild(element);

            // console.log(element)

        }

        //from main.js
        $('.js-show-modal1').on('click',function(e){
            e.preventDefault();
            $('.js-modal1').addClass('show-modal1');
        });
        
        $('.js-hide-modal1').on('click',function(){
            $('.js-modal1').removeClass('show-modal1');
        });

        // [ Isotope ]*/
        var $topeContainer = $('.isotope-grid');
        var $filter = $('.filter-tope-group');
    
        // filter items on button click
        $filter.each(function () {
            $filter.on('click', 'button', function () {
                var filterValue = $(this).attr('data-filter');
                $topeContainer.isotope({filter: filterValue});
            });
            
        });
    
        // init Isotope
        $(window).on('load', function () {
            var $grid = $topeContainer.each(function () {
                $(this).isotope({
                    itemSelector: '.isotope-item',
                    layoutMode: 'fitRows',
                    percentPosition: true,
                    animationEngine : 'best-available',
                    masonry: {
                        columnWidth: '.isotope-item'
                    }
                });
            });
        });
    
        var isotopeButton = $('.filter-tope-group button');
    
        $(isotopeButton).each(function(){
            $(this).on('click', function(){
                for(var i=0; i<isotopeButton.length; i++) {
                    $(isotopeButton[i]).removeClass('how-active1');
                }
    
                $(this).addClass('how-active1');
            });
        });

        document.getElementById("modelAddToCart").addEventListener("click",()=>{
            addToCart();
        });
    }

};

var addToCartProductId;
const loadQuickView=(productId)=>{

    productArr.forEach((productObject)=>{
        if(productObject.id==productId){

            document.getElementById("modelProductName").innerHTML=productObject.name;
            document.getElementById("modelProductPrice").innerHTML=productObject.unit_price;
            document.getElementById("modelProductDesc").innerHTML=productObject.description;
            document.getElementById("modelProductSize").innerHTML=productObject.size.size;
            document.getElementById("modelProductColor").innerHTML=productObject.product_color.color;

            addToCartProductId=productId;

            return;
        }
    });

};

const addToCart = async() => {

    const data = {
        "productId":addToCartProductId
    }

    const response = await fetch("AddToCart",{
        method:"POST",
        body:JSON.stringify(data),
        headers:{
            "Content-Type":"application/json"
        }
    });

    if (response.ok) {
        // console.log(response);

        let data = await response.json();
        // console.log(data)
        swal({
            title: "Success",
            text: data.data,
            icon: "success",
            button: "OK",
        });

    }


};

var cartListElement;
const loadCart2 = async()=>{

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

const addToWishlist = async (productId)=>{
    
    const response = await fetch("LoadCart");
    if(response.ok){

        const jsonData =await response.json();
        const data = jsonData.data;
    }
    
};