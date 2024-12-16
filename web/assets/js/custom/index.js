window.addEventListener("load", () => {
    loadProduct();
});

var bigCategoryElement =document.getElementById("bigCategoryElement");
var isBigFirstTime = true;
var parentC = document.getElementById("category-big-container");
parentC.innerHTML=""

const loadBigCategories = (categoryName) => {

    if (isBigFirstTime) {
        parentC.innerHTML = "";
        isBigFirstTime = false;
    }

    let element = bigCategoryElement.cloneNode(true);
    element.querySelector(".bigCategoryCategory").innerHTML = categoryName;

    parentC.appendChild(element);

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

var parent = document.getElementById("productContainer");
var productElement = document.getElementById("productElement");
parent.innerHTML=""
const loadProduct = async () => {

    const response = await fetch("LoadProduct?from=0&to=8");
    if (response.ok) {

        const data = await response.json();
        if (data.success) {

            const productData = data.data.productList;
            const categoryList = data.data.categoryList;

            categoryList.forEach(obj=>{

                if (!arr.includes(obj.category)) {
                    loadBigCategories(obj.category);
                    loadSmallCategories(obj.category);
    
                    arr.push(obj.category);
                }
            })

            for (var i = 1; i <= productData.length; i++) {

                let product = productData[i - 1];

                productArr.push(product);

                

                if (isProductFirstTime) {
                    parent.innerHTML = "";
                    isProductFirstTime = false;
                }

                //get product element
                let element = productElement.cloneNode(true);
                element.querySelector(".productName").innerHTML = product.name;
                element.querySelector(".productName").href = "product-detail.html?product=" + product.id;
                element.querySelector(".productPrice").innerHTML = "Rs. " + new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
                ).format(product.unit_price);

                let trimmedPath = product.img1.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
                element.querySelector(".pImg").src = trimmedPath;

                element.querySelector(".productElementATag").addEventListener("click", (e) => {
                    e.preventDefault();
                    loadQuickView(product.id);
                });
                element.querySelector(".productClick").addEventListener("click", (e) => {
                    window.location.href = "product-detail.html?product=" + product.id;
                });

                element.classList.replace("women", product.category.category);
                element.removeAttribute("style");
                element.querySelector(".addToWishlist").addEventListener("click", (e) => {
                    e.preventDefault();
                    let id = product.id
                    addToWishlist(id)
                });

                parent.appendChild(element);


            }

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

            loadCart()

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
            document.getElementById("modelProductPrice").innerHTML = "Rs. "+new Intl.NumberFormat(
                "en-US",
                {
                    minimumFractionDigits: 2
                }
            ).format(productObject.unit_price);
            document.getElementById("modelProductDesc").innerHTML = productObject.description;
            document.getElementById("modelProductSize").innerHTML = productObject.size.size;
            document.getElementById("modelProductColor").innerHTML = productObject.product_color.color;

            addToCartProductId = productId;

            const trimmedPath = productObject.img1.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
            const trimmedPath2 = productObject.img2.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
            const trimmedPath3 = productObject.img3.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");

            document.getElementById("img1").src = trimmedPath
            document.getElementById("img1a").href = trimmedPath
            document.querySelector(".img1th").setAttribute("data-thumb", trimmedPath)

            document.getElementById("img2").src = trimmedPath2
            document.getElementById("img2a").href = trimmedPath2
            document.querySelector(".img2th").setAttribute("data-thumb", trimmedPath2)

            document.getElementById("img3").src = trimmedPath3
            document.getElementById("img3a").href = trimmedPath3
            document.querySelector(".img3th").setAttribute("data-thumb", trimmedPath3)

            document.getElementById("quickViewAddToWishlist").addEventListener("click", () => {
                addToWishlist(productId)
            })

            const images = document.querySelectorAll("#modelSlickImg img");
            const newSrcs = [
                trimmedPath,
                trimmedPath2,
                trimmedPath3
            ];
            images.forEach((img, index) => {
                if (newSrcs[index]) {
                    img.src = newSrcs[index];
                }
            });


            //from main.js
            $('.js-show-modal1').on('click', function (e) {
                e.preventDefault();
                $('.js-modal1').addClass('show-modal1');
            });

            $('.js-hide-modal1').on('click', function () {
                $('.js-modal1').removeClass('show-modal1');
            });

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

        let data = await response.json();
        if (data.success) {

            loadCart()
            Notification().success({
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

const addToWishlist = async (productId) => {

    const response = await fetch("AddToWishlist", {
        method: "POST",
        body: JSON.stringify({
            pId: productId
        }),
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (response.ok) {

        let data = await response.json();
        if (data.success) {

            Notification().success({
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