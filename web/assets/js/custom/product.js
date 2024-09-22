window.addEventListener("load",()=>{
    loadProduct();
})

var sortBy = "Default";
var color = "Any"
var category = "Any"
var brand = "Any"
var size = "Any"
var search = ""
const applySort = (e, sortType) => {
    e.preventDefault();
    sortBy = sortType;

};
const applyColor = (e, colorID) => {
    e.preventDefault();
    color = colorID;

};
const applyCategory = (e, categoryID) => {
    e.preventDefault();
    category = categoryID;

};
const applyBrand = (e, brandID) => {
    e.preventDefault();
    brand = brandID;

};
const applySize = (e, sizeID) => {
    e.preventDefault();
    size = sizeID;

};

const searchText = (e) => {
    let txt = document.getElementById("searchText").value;
    if (txt.length > 5 && txt.trim() != "") {
        search = txt
    }

};

var smallCategoryElement;
var isSmallFirstTime = true;
const loadSmallCategories = (categoryName, cId) => {

    // outside
    const parent = document.getElementById("category-small-container");
    smallCategoryElement = document.getElementById("smallCategoryElement");

    if (isSmallFirstTime) {
        parent.innerHTML = "";
        isSmallFirstTime = false;
    }

    let element = smallCategoryElement.cloneNode(true);
    element.innerHTML = categoryName;
    element.setAttribute("data-filter", "." + categoryName);

    parent.appendChild(element);

    // filter area
    loadFilterCategories(categoryName, cId)
};

var filterCategoryElement;
var isFilterFirstTime = true;
const loadFilterCategories = (categoryName, cId) => {

    // filter area
    const parentInFIlter = document.getElementById("categoryParent");
    filterCategoryElement = document.getElementById("categoryLi");

    if (isFilterFirstTime) {
        parentInFIlter.innerHTML = "";
        filterCategoryElement.addEventListener("click", (e) => {
            applyCategory(event, 0)
        });
        parentInFIlter.innerHTML = filterCategoryElement;
        isFilterFirstTime = false;
    }

    let filterLiElement = filterCategoryElement.cloneNode(true);
    filterLiElement.removeAttribute("id");
    filterLiElement.querySelector(".lia").innerHTML = categoryName;
    filterLiElement.querySelector(".lia").addEventListener("load", (e, cId) => {
        applyCategory(event, cId)
    });
    filterLiElement.setAttribute("data-filter", "." + categoryName);

    parentInFIlter.appendChild(filterLiElement);
};

var filterBrandElement;
var isBrandFilterFirstTime = true;
const loadFilterBrands = (brandName, bId) => {

    // filter area
    const parent = document.getElementById("brandParent");
    filterBrandElement = document.getElementById("brandLi");

    if (isBrandFilterFirstTime) {
        parent.innerHTML = "";
        filterBrandElement.addEventListener("click", (e) => {
            applyBrand(event, 0)
        });
        parent.innerHTML = filterBrandElement;
        isBrandFilterFirstTime = false;
    }

    let element = filterBrandElement.cloneNode(true);
    element.removeAttribute("id");
    element.querySelector(".lia").innerHTML = brandName;
    element.querySelector(".lia").addEventListener("load", (e, bId) => {
        applyBrand(event, bId)
    });
    element.setAttribute("data-filter", "." + brandName);

    parent.appendChild(element);
};

var filterSizeElement;
var isSizeFilterFirstTime = true;
const loadFilterSizes = (sizeName, sId) => {

    // filter area
    const parent = document.getElementById("sizeParent");
    filterSizeElement = document.getElementById("sizeA");

    if (isSizeFilterFirstTime) {
        parent.innerHTML = "";
        filterSizeElement.addEventListener("click", (e) => {
            applySize(event, 0)
        });
        parent.innerHTML = filterSizeElement;
        isSizeFilterFirstTime = false;
    }

    let element = filterSizeElement.cloneNode(true);
    element.removeAttribute("id");
    element.innerHTML = sizeName;
    element.addEventListener("load", (e, sId) => {
        applySize(event, sId)
    });
    element.setAttribute("data-filter", "." + sizeName);

    parent.appendChild(element);
};

var filterColorElement;
var isColorFilterFirstTime = true;
const loadFilterColors = (colorName, cId) => {

    // filter area
    const parent = document.getElementById("brandParent");
    filterColorElement = document.getElementById("brandLi");

    if (isColorFilterFirstTime) {
        parent.innerHTML = "";
        filterColorElement.addEventListener("click", (e) => {
            applyColor(event, 0)
        });
        parent.innerHTML = filterColorElement;
        isColorFilterFirstTime = false;
    }

    let element = filterColorElement.cloneNode(true);
    element.removeAttribute("id");
    element.querySelector(".lia").innerHTML = colorName;
    element.querySelector(".lia").addEventListener("load", (e, cId) => {
        applyColor(event, cId)
    });
    element.setAttribute("data-filter", "." + colorName);

    parent.appendChild(element);
};

const arr = [];
var isProductFirstTime = true;
const productArr = [];
var productCount = 30;
const loadProduct = async () => {

    const response = await fetch("LoadProduct?productCount=" + productCount + "&color=" + color + "&brand=" + brand + "&category=" + category + "&size=" + size + "&sortBy=" + sortBy + "&search=" + search);
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

                //category
                if (!arr.includes(product.category.category)) {
                    loadSmallCategories(product.category.category, product.category.id);

                    arr.push(product.category.category);
                }

                //brand
                if (!arrBrand.includes(product.brand.brand)) {
                    loadFilterBrands(product.brand.brand,product.brand.id)
                    arrBrand.push(product.category.category);
                }

                //size
                if (!arrSize.includes(product.size.size)) {
                    loadFilterSizes(product.size.size, product.size.id);

                    arrSize.push(product.size.size);
                }

                //color
                if (!arrColor.includes(product.product_color.color)) {
                    loadSmallCategories(product.product_color.color, product.product_color.id);

                    arrColor.push(product.product_color.color);
                }


                if (isProductFirstTime) {
                    parent.innerHTML = "";
                    isProductFirstTime = false;
                }

                //get product element
                let element = productElement.cloneNode(true);
                element.querySelector(".productName").innerHTML = product.name;
                element.querySelector(".productPrice").innerHTML = "Rs. " + product.unit_price;
                element.querySelector(".productElementATag").addEventListener("click", (e) => {
                    e.preventDefault();
                    loadQuickView(product.id);
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
        // console.log(response);

        let data = await response.json();
        // console.log(data)
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