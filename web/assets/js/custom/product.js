window.addEventListener("load", () => {
    loadProduct();
})

var sortBy = "Default";
var color = ""
var category = ""
var brand = ""
var size = ""
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

const arr = [];
const arrBrand = [];
const arrSize = [];
const arrColor = [];

var smallCategoryElement;
var isSmallFirstTime = true;
const loadSmallCategories = (categoryArr) => {

    // outside
    const parent = document.getElementById("category-small-container");
    smallCategoryElement = document.getElementById("smallCategoryElement");

    categoryArr.forEach(obj => {
        if (isSmallFirstTime) {
            parent.innerHTML = "";
            isSmallFirstTime = false;
        }

        let element = smallCategoryElement.cloneNode(true);
        element.innerHTML = obj.category;
        element.setAttribute("data-filter", "." + obj.category);

        parent.appendChild(element);

    })
    loadFilterCategories(categoryArr)

};

var filterCategoryElement;
var isFilterFirstTime = true;
const loadFilterCategories = (categoryArr) => {

    // filter area
    const parentInFIlter = document.getElementById("categoryParent");
    filterCategoryElement = document.getElementById("categoryLi");

    categoryArr.forEach(obj => {

        if (isFilterFirstTime) {
            parentInFIlter.innerHTML = "";
            let filterLiElement = filterCategoryElement.cloneNode(true);
            filterLiElement.addEventListener("click", (e) => {
                applyCategory(e, 0)
            });
            parentInFIlter.appendChild(filterLiElement);
            isFilterFirstTime = false;
        }

        let filterLiElement = filterCategoryElement.cloneNode(true);
        filterLiElement.removeAttribute("id");
        filterLiElement.querySelector(".lia").innerHTML = obj.category;
        filterLiElement.querySelector(".lia").addEventListener("load", (e) => {
            applyCategory(e, obj.id)
        });
        filterLiElement.setAttribute("data-filter", "." + obj.category);

        parentInFIlter.appendChild(filterLiElement);
    })

};

var filterBrandElement;
var isBrandFilterFirstTime = true;
const loadFilterBrands = (brandArr) => {

    // filter area
    const parent = document.getElementById("brandParent");
    filterBrandElement = document.getElementById("brandLi");

    brandArr.forEach(obj => {

        if (isBrandFilterFirstTime) {
            parent.innerHTML = "";
            let element = filterBrandElement.cloneNode(true);
            element.addEventListener("click", (e) => {
                applyBrand(e, 0)
            });
            parent.appendChild(element);
            isBrandFilterFirstTime = false;
        }

        let element = filterBrandElement.cloneNode(true);
        element.removeAttribute("id");
        element.querySelector(".lia").innerHTML = obj.brand;
        element.querySelector(".lia").addEventListener("load", (e) => {
            applyBrand(e, obj.id)
        });
        element.setAttribute("data-filter", "." + obj.brand);

        parent.appendChild(element);
    });

};

var filterSizeElement;
var isSizeFilterFirstTime = true;
const loadFilterSizes = (sizeArr) => {

    // filter area
    const parent = document.getElementById("sizeParent");
    filterSizeElement = document.getElementById("sizeA");

    sizeArr.forEach((obj) => {

        if (isSizeFilterFirstTime) {
            parent.innerHTML = "";
            let element = filterSizeElement.cloneNode(true);
            element.addEventListener("click", (e) => {
                applySize(e, 0)
            });
            parent.appendChild(element);
            isSizeFilterFirstTime = false;
        }

        let element = filterSizeElement.cloneNode(true);
        element.removeAttribute("id");
        element.innerHTML = obj.size;
        element.addEventListener("load", (e) => {
            applySize(e, obj.id)
        });
        element.setAttribute("data-filter", "." + obj.size);

        parent.appendChild(element);
    });

};

var filterColorElement;
var isColorFilterFirstTime = true;
const loadFilterColors = (colorArr) => {

    const parent = document.getElementById("colorParent");
    filterColorElement = document.getElementById("colorLi");

    // filter area
    colorArr.forEach(obj => {

        if (isColorFilterFirstTime) {
            parent.innerHTML = "";
            let element = filterColorElement.cloneNode(true);
            element.addEventListener("click", (e) => {
                applyColor(e, 0)
            });
            parent.appendChild(element);
            isColorFilterFirstTime = false;
        }

        let element = filterColorElement.cloneNode(true);
        element.removeAttribute("id");
        element.querySelector(".colorC").style.color = obj.color
        element.querySelector(".lia").innerHTML = obj.color;
        element.querySelector(".lia").addEventListener("load", (e) => {
            applyColor(e, obj.id)
        });
        element.setAttribute("data-filter", "." + obj.color);

        parent.appendChild(element);
    });
};

// const arr = [];
// const arrBrand = [];
// const arrSize = [];
// const arrColor = [];
var isFiltertFirstTime = true;
const productArr = [];
var productCount = 30;

var loadFrom = 0
var loadTo = 20

var parent = document.getElementById("productContainer");
var productElement = document.getElementById("productElement");
const loadProduct = async () => {

    Notification().info({
        message: "Applying Filters ..."
    })

    const response = await fetch("LoadProduct?productCount=" + productCount + "&color=" + color + "&brand=" + brand + "&category=" + category + "&size=" + size + "&sortBy=" + sortBy + "&search=" + search);
    if (response.ok) {

        const data = await response.json();
        if (data.success) {

            const productData = data.data;
            parent.innerHTML = "";

            // load features to array
            for (var i = 1; i <= productData.length; i++) {

                let product = productData[i - 1];
                productArr.push(product);

                // if (isProductFirstTime) {

                //category
                let categoryFound = false;
                arr.forEach(obj => {
                    if (obj.id == product.category.id) {
                        categoryFound = true;
                        return;
                    }
                })
                if (!categoryFound) {
                    let obj = {
                        id: product.category.id,
                        category: product.category.category
                    }
                    arr.push(obj);
                }

                //brand
                let brandFound = false;
                arrBrand.forEach(obj => {
                    if (obj.id == product.brand.id) {
                        brandFound = true;
                        return;
                    }
                })
                if (!brandFound) {
                    let obj = {
                        id: product.brand.id,
                        brand: product.brand.brand
                    }
                    arrBrand.push(obj);
                }

                //size
                let sizeFound = false;
                arrSize.forEach(obj => {
                    if (obj.id == product.size.id) {
                        sizeFound = true;
                        return;
                    }
                })
                if (!sizeFound) {
                    let obj = {
                        id: product.size.id,
                        size: product.size.size
                    }
                    arrSize.push(obj);
                }

                //color
                let colorFound = false;
                arrColor.forEach(obj => {
                    if (obj.id == product.product_color.id) {
                        colorFound = true;
                        return;
                    }
                })
                if (!colorFound) {
                    let obj = {
                        id: product.product_color.id,
                        color: product.product_color.color
                    }
                    arrColor.push(obj);
                }

                // parent.innerHTML = "";
                // isProductFirstTime = false;
                // }
            }

            //load for page
            loadProductCards()

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

            // load filters
            if (isFiltertFirstTime) {

                loadSmallCategories(arr);
                loadFilterBrands(arrBrand)
                loadFilterSizes(arrSize);
                loadFilterColors(arrColor);
                isFiltertFirstTime = false;
            }

            pagination()

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

// var isProductFirstTime = true;
function loadProductCards() {

    parent.innerHTML = ""
    if (productArr.length > loadFrom) {

        if (productArr.length < loadTo) {
            loadTo = productArr.length
        }
        for (let x = loadFrom; x < loadTo; x++) {


            let product = productArr[x];

            console.log(product)
            // console.log(product)
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
            element.querySelector(".productImage").src = product.img1;

            parent.appendChild(element);

        }
    }

}

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

var currentPage = 1
var pgParent = document.getElementById("pgParent")
var pgPrevious = document.getElementById("pgPrevious")
var pgNext = document.getElementById("pgNext")
var pgChild = document.getElementById("pgChild")
function pagination() {

    let pages = Math.ceil(productArr.length / 20);


    pgParent.innerHTML = ""
    pgParent.appendChild(pgPrevious)

    for (let i = 1; i <= pages; i++) {

        let element = pgChild.cloneNode(true)
        element.querySelector(".paginationNum").innerHTML = i;
        // element.href=window.location.href.split("?")[0]+"?page="+currentPage
        element.addEventListener("click", () => {
            currentPage = i
            element.style.backgroundColor = "black"

            loadFrom = (currentPage * 20) + 1
            loadTo = loadFrom + 20

            loadProductCards()
        })

        pgParent.appendChild(element)
    }
    pgParent.appendChild(pgNext)


}