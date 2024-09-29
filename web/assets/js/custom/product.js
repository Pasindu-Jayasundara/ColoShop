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

function resetFilter() {
    sortBy = "Default";
    color = ""
    category = ""
    brand = ""
    size = ""
    search = ""
    document.getElementById("searchText").value = ""
    loadProduct()
}

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
const loadFilterCategories = (categoryArr) => {

    // filter area
    const parentInFIlter = document.getElementById("categoryParent");
    filterCategoryElement = document.getElementById("categoryLi");

    parentInFIlter.innerHTML = "";
    let filterLiElement = filterCategoryElement.cloneNode(true);
    filterLiElement.addEventListener("click", (e) => {
        applyCategory(e, 0)
    });
    parentInFIlter.appendChild(filterLiElement);

    categoryArr.forEach(obj => {

        let filterLiElement = filterCategoryElement.cloneNode(true);
        filterLiElement.removeAttribute("id");
        filterLiElement.querySelector(".lia").innerHTML = obj.category;
        filterLiElement.querySelector(".lia").addEventListener("click", (e) => {
            applyCategory(e, obj.id)
        });
        filterLiElement.setAttribute("data-filter", "." + obj.category);

        parentInFIlter.appendChild(filterLiElement);
    })

};

var filterBrandElement;
const loadFilterBrands = (brandArr) => {

    // filter area
    const parent = document.getElementById("brandParent");
    filterBrandElement = document.getElementById("brandLi");

    // if (isBrandFilterFirstTime) {
    parent.innerHTML = "";
    let element = filterBrandElement.cloneNode(true);
    element.removeAttribute("id")
    element.addEventListener("click", (e) => {
        applyBrand(e, 0)
    });
    parent.appendChild(element);
    // isBrandFilterFirstTime = false;
    // }

    brandArr.forEach(obj => {

        let element = filterBrandElement.cloneNode(true);
        element.removeAttribute("id");
        element.querySelector(".lia").innerHTML = obj.brand;
        element.querySelector(".lia").addEventListener("click", (e) => {
            let id = obj.id
            applyBrand(e, id)
        });
        element.setAttribute("data-filter", "." + obj.brand);

        parent.appendChild(element);
    });

};

var filterSizeElement;
const loadFilterSizes = (sizeArr) => {

    // filter area
    const parent = document.getElementById("sizeParent");
    filterSizeElement = document.getElementById("sizeA");

    // if (isSizeFilterFirstTime) {
    parent.innerHTML = "";
    let element = filterSizeElement.cloneNode(true);
    element.addEventListener("click", (e) => {
        applySize(e, 0)
    });
    parent.appendChild(element);
    // isSizeFilterFirstTime = false;
    // }

    sizeArr.forEach((obj) => {

        let element = filterSizeElement.cloneNode(true);
        element.removeAttribute("id");
        element.innerHTML = obj.size;
        element.addEventListener("click", (e) => {
            applySize(e, obj.id)
        });
        element.setAttribute("data-filter", "." + obj.size);

        parent.appendChild(element);
    });

};

var filterColorElement;
const loadFilterColors = (colorArr) => {

    const parent = document.getElementById("colorParent");
    filterColorElement = document.getElementById("colorLi");

    parent.innerHTML = "";
    let element = filterColorElement.cloneNode(true);
    element.addEventListener("click", (e) => {
        applyColor(e, 0)
    });
    parent.appendChild(element);
    // isColorFilterFirstTime = false;

    // filter area
    colorArr.forEach(obj => {

        let element = filterColorElement.cloneNode(true);
        element.removeAttribute("id");
        element.querySelector(".colorC").style.color = obj.color
        element.querySelector(".lia").innerHTML = obj.color;
        element.querySelector(".lia").addEventListener("click", (e) => {
            applyColor(e, obj.id)
        });
        element.setAttribute("data-filter", "." + obj.color);

        parent.appendChild(element);
    });
};


var isFiltertFirstTime = true;
var productArr = [];
var productCount = 30;
var isFilterArrFirstTime = true;

var loadFrom = 0
var loadTo = 2
var allProductCount = 0;

var parent = document.getElementById("productContainer");
var productElement = document.getElementById("productElement");
parent.innerHTML=""
const loadProduct = async () => {

    Notification().info({
        message: "Applying Filters ..."
    })

    // console.log("LoadProduct?from=" + loadFrom + "&to=" + loadTo + "&color=" + color + "&brand=" + brand + "&category=" + category + "&size=" + size + "&sortBy=" + sortBy + "&search=" + search)
    const response = await fetch("LoadProduct?from=" + loadFrom + "&to=" + loadTo + "&color=" + color + "&brand=" + brand + "&category=" + category + "&size=" + size + "&sortBy=" + sortBy + "&search=" + search);
    if (response.ok) {

        const data = await response.json();
        if (data.success) {

            const productData = data.data.productList;
            // console.log(productData)
            allProductCount = data.data.allProductCount;
            parent.innerHTML = "";
            productArr = []

            if (isFilterArrFirstTime) {

                isFilterArrFirstTime = false;

                const categoryList = data.data.categoryList;
                categoryList.forEach(obj => {
                    let ob = {
                        id: obj.id,
                        category: obj.category
                    }
                    arr.push(ob)
                })

                const sizeList = data.data.sizeList;
                sizeList.forEach(obj => {
                    let ob = {
                        id: obj.id,
                        size: obj.size
                    }
                    arrSize.push(ob)
                })

                const colorList = data.data.colorList;
                colorList.forEach(obj => {
                    let ob = {
                        id: obj.id,
                        color: obj.color
                    }
                    arrColor.push(ob)
                })

                const brandList = data.data.brandList;
                brandList.forEach(obj => {
                    let ob = {
                        id: obj.id,
                        brand: obj.brand
                    }
                    arrBrand.push(ob)
                })
            }

            // load features to array
            for (let i = 1; i <= productData.length; i++) {

                let product = productData[i - 1];
                productArr.push(product);
            }

            //load for page
            loadProductCards()

            

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
        // console.log(response)
    }

};

function loadProductCards() {

    parent.innerHTML = ""
    if (productArr.length == 0) {

        parent.innerHTML = "No Product Found"
        parent.style.display = "flex"
        parent.style.JustifyContent = "center"

    } else {
        for (let x = 0; x < productArr.length; x++) {


            let product = productArr[x];

            //get product element
            let element = productElement.cloneNode(true);
            element.querySelector(".productName").innerHTML = product.name;
            element.querySelector(".productName").href = "product-detail.html?product=" + product.id;
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
            const trimmedPath = product.img1.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
            element.querySelector(".productImage").src = trimmedPath;

            parent.appendChild(element);

        }
    }


}

var addToCartProductId;
const loadQuickView = (productId) => {

    productArr.forEach((productObject) => {
        if (productObject.id == productId) {

            addToCartProductId = productId;

            document.getElementById("modelProductName").innerHTML = productObject.name;
            document.getElementById("modelProductPrice").innerHTML = productObject.unit_price;
            document.getElementById("modelProductDesc").innerHTML = productObject.description;
            document.getElementById("modelProductSize").innerHTML = productObject.size.size;
            document.getElementById("modelProductColor").innerHTML = productObject.product_color.color;

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

var cartData;
const addToCart = async () => {

    if (addToCartProductId) {

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
            cartData = data

            if (data.success) {
                // swal({
                //     title: "Success",
                //     text: data.data,
                //     icon: "success",
                //     button: "OK",
                // });
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

    } else {
        Notification().error({
            message: "Missing Id"
        })
        console.log(addToCartProductId)
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

var currentPage = 1;
var pgParent = document.getElementById("pgParent");
var pgPrevious = document.getElementById("pgPrevious");
var pgNext = document.getElementById("pgNext");
var pgChild = document.getElementById("pgChild");
var isNextFirstLoad = true;
var isPrevFirstLoad = true;

function pagination() {

    let availableProductCount = allProductCount;
    let productsPerPage = 10;

    let pages = Math.ceil(availableProductCount / productsPerPage);

    pgParent.innerHTML = "";

    if (currentPage > 1) {
        if (isPrevFirstLoad) {
            isPrevFirstLoad = false;
            pgPrevious.addEventListener("click", () => {

                currentPage--;

                loadFrom = (currentPage - 1) * productsPerPage;
                loadTo = loadFrom + productsPerPage;

                loadProduct();
            })
        }
        pgParent.appendChild(pgPrevious);
    }

    for (let i = 1; i <= pages; i++) {

        let element = pgChild.cloneNode(true);
        element.querySelector(".paginationNum").innerHTML = i;
        element.addEventListener("click", (event) => {
            currentPage = i;

            let paginationButtons = pgParent.querySelectorAll(".paginationNum");
            paginationButtons.forEach(button => {
                button.style.backgroundColor = "";
            });


            loadFrom = (currentPage - 1) * productsPerPage;
            loadTo = loadFrom + productsPerPage;

            loadProduct();
        });

        if (i === currentPage) {
            element.querySelector(".paginationNum").style.backgroundColor = "black";
            element.querySelector(".paginationNum").classList.replace("text-dark", "text-light")
        }

        pgParent.appendChild(element);
    }


    if (currentPage < pages) {
        if (isNextFirstLoad) {
            isNextFirstLoad = false;
            pgNext.addEventListener("click", () => {

                currentPage++;

                loadFrom = (currentPage - 1) * productsPerPage;
                loadTo = loadFrom + productsPerPage;

                loadProduct();
            })
        }
        pgParent.appendChild(pgNext);
    }
}

function showAlert() {
    // swal("", "success");
    if (cartData != null && cartData != undefined) {
        swal({
            title: cartData.success,
            text: cartData.data,
            icon: cartData.success ? "success" : "error",
            button: "OK",
        });
    }

}
