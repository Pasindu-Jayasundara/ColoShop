window.addEventListener("load", () => {
    loadProduct();
});

var bigCategoryElement;
var isBigFirstTime = true;
const loadBigCategories = (categoryName) => {

    let parent = document.getElementById("category-big-container");
    bigCategoryElement = document.getElementById("bigCategoryElement");

    if (isBigFirstTime) {
        parent.innerHTML = "";
        isBigFirstTime = false;
    }

    let element = bigCategoryElement.cloneNode(true);
    element.querySelector(".bigCategoryCategory").innerHTML = categoryName;

    parent.appendChild(element);

};

var smallCategoryElement;
var isSmallFirstTime = true;
const loadSmallCategories = (categoryName) => {

    let parent = document.getElementById("category-small-container");
    smallCategoryElement = document.getElementById("smallCategoryElement");

    if (isSmallFirstTime) {
        parent.innerHTML = "";
        isSmallFirstTime = false;
    }

    let element = smallCategoryElement.cloneNode(true);
    element.innerHTML = categoryName;
    element.setAttribute("data-filter", "." + categoryName);

    parent.appendChild(element);

};

const arr = [];
var isProductFirstTime = true;
const productArr = [];
const loadProduct = async () => {

    const response = await fetch("LoadProduct?productCount=30");
    if (response.ok) {

        const data = await response.json();
        if (data.success) {

            const productData = data.data;

            let parent = document.getElementById("productContainer");
            let productElement = document.getElementById("productElement");

            for (var i = 1; i <= productData.length; i++) {

                let product = productData[i - 1];
                // console.log(product);

                productArr.push(product);

                if (!arr.includes(product.category.category)) {
                    loadBigCategories(product.category.category);
                    loadSmallCategories(product.category.category);

                    arr.push(product.category.category);
                }

                if (isProductFirstTime) {
                    parent.innerHTML = "";
                    isProductFirstTime = false;
                }

                //get product element
                let element = productElement.cloneNode(true);
                element.querySelector(".productName").innerHTML = product.name;
                element.querySelector(".productName").href = "product-detail.html?product="+product.id;
                element.querySelector(".productPrice").innerHTML = "Rs. " + product.unit_price;
                element.querySelector(".productElementATag").addEventListener("click", (e) => {
                    e.preventDefault();
                    loadQuickView(product.id);
                });
                element.querySelector(".productClick").addEventListener("click", (e) => {
                    window.location.href="product-detail.html?product="+product.id;
                });

                element.classList.replace("women", product.category.category);
                element.removeAttribute("style");
                element.querySelector(".addToWishlist").addEventListener("click", (e) => {
                    e.preventDefault();
                    addToWishlist(product.id)
                });

                parent.appendChild(element);

                // console.log(element)

            }

            //from main.js
            $('.js-show-modal1').on('click', function (e) {
                e.preventDefault();
                $('.js-modal1').addClass('show-modal1');
            });

            $('.js-hide-modal1').on('click', function () {
                $('.js-modal1').removeClass('show-modal1');
            });

            // [ Isotope ]*/
            var $topeContainer = $('.isotope-grid');
            var $filter = $('.filter-tope-group');

            // filter items on button click
            $filter.each(function () {
                $filter.on('click', 'button', function () {
                    var filterValue = $(this).attr('data-filter');
                    $topeContainer.isotope({ filter: filterValue });
                });

            });

            // init Isotope
            $(window).on('load', function () {
                var $grid = $topeContainer.each(function () {
                    $(this).isotope({
                        itemSelector: '.isotope-item',
                        layoutMode: 'fitRows',
                        percentPosition: true,
                        animationEngine: 'best-available',
                        masonry: {
                            columnWidth: '.isotope-item'
                        }
                    });
                });
            });

            var isotopeButton = $('.filter-tope-group button');

            $(isotopeButton).each(function () {
                $(this).on('click', function () {
                    for (var i = 0; i < isotopeButton.length; i++) {
                        $(isotopeButton[i]).removeClass('how-active1');
                    }

                    $(this).addClass('how-active1');
                });
            });

            document.getElementById("modelAddToCart").addEventListener("click", () => {
                addToCart();
            });

        } else {
            Notification().error({
                message: data.data
            })
        }
    } else {
        Notification().error({
            message: "Please Try Again Latter"
        })
        console.log(response)
    }

};

var addToCartProductId;
const loadQuickView = (productId) => {

    productArr.forEach((productObject) => {
        if (productObject.id == productId) {

            document.getElementById("modelProductName").innerHTML = productObject.name;
            document.getElementById("modelProductPrice").innerHTML = productObject.unit_price;
            document.getElementById("modelProductDesc").innerHTML = productObject.description;
            document.getElementById("modelProductSize").innerHTML = productObject.size.size;
            document.getElementById("modelProductColor").innerHTML = productObject.product_color.color;

            addToCartProductId = productId;

            return;
        }
    });

};

const addToCart = async () => {

    const data = {
        "productId": addToCartProductId
    }

    const response = await fetch("AddToCart", {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {
        // console.log(response);

        let data = await response.json();
        // console.log(data)
        if (data.success) {

            swal({
                title: "Success",
                text: data.data,
                icon: "success",
                button: "OK",
            });
        } else {
             Notification().error({
                message: data.data
            })
        }


    } else {
         Notification().error({
            message: "Please Try Again Latter"
        })
        console.log(response)
    }



};

// var cartListElement;
// const loadCart = async()=>{

//     const response = await fetch("LoadCart");
//     if(response.ok){

//         const jsonData =await response.json();
//         const data = jsonData.data;
//         // console.log(data);

//         let cartListParent = document.getElementById("cartSideBarContainer");
//         cartListElement = document.getElementById("cartListElement");

//         let cartProductTotal=0;
//         cartListParent.innerHTML="";

//         data.forEach((productObj)=>{

//             // console.log(productObj);

//             let element = cartListElement.cloneNode(true);
//             element.removeAttribute("id");
//             element.querySelector(".cartProductName").innerHTML=productObj.name;
//             element.querySelector(".cartProductUnitPrice").innerHTML="Rs. "+productObj.unit_price;

//             cartListParent.appendChild(element);

//             cartProductTotal+=productObj.unit_price;

//         });

//         document.getElementById("cartProductTotal").innerHTML="Total: "+cartProductTotal;
//     }
// };

const addToWishlist = async (productId) => {

    const response = await fetch("AddToWishlist",{
        method:"POST",
        body:JSON.stringify({
            pId:productId
        }),
        headers:{
            "Content-Type":"application/json"
        }
    });
    if (response.ok) {
        // console.log(response);

        let data = await response.json();
        // console.log(data)
        if (data.success) {

            new Notification().success({
                message: data.data
            })

        } else {
             Notification().error({
                message: data.data
            })
        }


    } else {
         Notification().error({
            message: "Please Try Again Latter"
        })
        console.log(response)
    }


};