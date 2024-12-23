window.addEventListener("load", () => {

    let url = window.location.href;
    const splitArr = url.split("?");

    let productId = splitArr[1].split("=")[1];

    loadSingleProduct(productId);

});

var pId;

const loadSingleProduct = async (productId) => {

    let pid;
    if (productId.includes("#")) {
        pid = productId.split("#")[0];
    } else {
        pid = productId
    }
    const data = {
        "id": pid
    }

    const response = await fetch("LoadSingleProduct", {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {

        const jsonData = await response.json();
        const data = jsonData.data;

        pId = data.id;
        document.getElementById("productName").innerHTML = data.name;
        document.getElementById("productPrice").innerHTML = "Price : Rs. " + new Intl.NumberFormat(
            "en-US",
            {
                minimumFractionDigits: 2
            }
        ).format(data.unit_price);
        document.getElementById("productDesc").innerHTML = data.description;
        document.getElementById("productSize").innerHTML = "Size :" + data.size.size;
        document.getElementById("productColor").innerHTML = "Color :" + data.product_color.color;

        document.getElementById("descTab").innerHTML = data.description;
        document.getElementById("tabCategory").innerHTML = data.category.category;
        document.getElementById("tabBrand").innerHTML = data.brand.brand;
        document.getElementById("tabColor").innerHTML = data.product_color.color;
        document.getElementById("tabSize").innerHTML = data.size.size;

        const trimmedPath = data.img1.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
        const trimmedPath2 = data.img2.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
        const trimmedPath3 = data.img3.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");

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

        const images = document.querySelectorAll("#sl3dImg img");
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

        loadReview(productId);
        loadCart()
        loadSimilarProducts();

    }

};

const reviewParent = document.getElementById("reviewParent")
const reviewChild = document.getElementById("reviewChild")
const newReview = document.getElementById("newReview")
const loadReview = async (productId) => {

    let pid;
    if (productId.includes("#")) {
        pid = productId.split("#")[0];
    } else {
        pid = productId
    }

    reviewParent.innerHTML = ""

    const data = {
        "id": pid
    }

    const response = await fetch("LoadReview", {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {

        console.log("Review Loaded")
        let jsonData = await response.json();
        if (jsonData.success) {

            console.log(jsonData)
            let data = jsonData.data;


            if (data.length > 0) {

                data.forEach(obj => {

                    let element = reviewChild.cloneNode(true)
                    element.removeAttribute("id")
                    element.setAttribute("id", "reRow" + obj.id)

                    element.querySelector(".reviewName").innerHTML = obj.by
                    element.querySelector(".reviewDate").innerHTML = "At: " + obj.datetime
                    element.querySelector(".reviewDesc").innerHTML = obj.review
                    if (obj.isEditable) {
                        element.querySelector(".deleteReview").style.display = "block"
                        element.querySelector(".deleteReview").querySelector(".deleteBtn").addEventListener("click", () => {
                            deleteReview(obj.id)
                        })
                    }

                    reviewParent.appendChild(element)

                })
            }


            reviewParent.appendChild(newReview)

        }else{
            console.log(jsonData)
        }
    }

};

async function deleteReview(reviewId) {

    const data = {
        "id": reviewId
    }

    const response = await fetch("DeleteReview", {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {

        let jsonData = await response.json();
        if (jsonData.success) {

            Notification().success({
                message: data.data
            });
            reviewParent.removeChild(document.getElementById("reRow" + reviewId))

        } else {
            Notification().error({
                message: data.data
            });
        }
    }


}

var isProductFirstTime = true;
const productArr = [];

var parentS = document.getElementById("productContainer");
var productElementS = document.getElementById("productElement");
parentS.innerHTML=""
const loadSimilarProducts = async () => {

    const data = {
        "id": pId
    }

    const response = await fetch("loadSimilarProduct", {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {

        const data = await response.json();
        const productData = data.data;

        parentS.innerHTML = "";
        for (var i = 1; i <= productData.length; i++) {

            let product = productData[i - 1];

            productArr.push(product);

            if (isProductFirstTime) {
                parentS.innerHTML = "";
                isProductFirstTime = false;
            }

            //get product element
            let element = productElementS.cloneNode(true);
            element.querySelector(".productName").innerHTML = product.name;
            element.querySelector(".productName").href = "product-detail.html?product=" + product.id;
            element.querySelector(".productPrice").innerHTML = "Rs. " + new Intl.NumberFormat(
                "en-US",
                {
                    minimumFractionDigits: 2
                }
            ).format(product.unit_price);
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

            let trimmedPath = product.img1.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
            element.querySelector(".pImg").src = trimmedPath;

            element.querySelector(".productElementATag").addEventListener("click", (e) => {
                e.preventDefault();
                loadQuickView(product.id);
            });

            parentS.appendChild(element);

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
    }

};

const loadQuickView = (productId) => {

    productArr.forEach((productObject) => {
        if (productObject.id == productId) {

            document.getElementById("modelProductName").innerHTML = productObject.name;
            document.getElementById("modelProductPrice").innerHTML = "Rs. "+ new Intl.NumberFormat(
                "en-US",
                {
                    minimumFractionDigits: 2
                }
            ).format(productObject.unit_price);
            document.getElementById("modelProductDesc").innerHTML = productObject.description;
            document.getElementById("modelProductSize").innerHTML = productObject.size.size;
            document.getElementById("modelProductColor").innerHTML = productObject.product_color.color;

            const trimmedPath = productObject.img1.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
            const trimmedPath2 = productObject.img2.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
            const trimmedPath3 = productObject.img3.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");

            document.getElementById("img12").src = trimmedPath
            document.getElementById("img1a2").href = trimmedPath
            document.querySelector(".img1th2").setAttribute("data-thumb", trimmedPath)

            document.getElementById("img22").src = trimmedPath2
            document.getElementById("img2a2").href = trimmedPath2
            document.querySelector(".img2th2").setAttribute("data-thumb", trimmedPath2)

            document.getElementById("img32").src = trimmedPath3
            document.getElementById("img3a2").href = trimmedPath3
            document.querySelector(".img3th2").setAttribute("data-thumb", trimmedPath3)

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
        "productId": pId
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

async function addNewReview() {

    let review = document.getElementById("reviewtxt").value
    if (review.trim() != "") {

        let url = window.location.href;
        const splitArr = url.split("?");

        let pid = splitArr[1].split("=")[1];
        let productId
        if (pid.includes("#")) {
            productId = pid.split("#")[0];
        } else {
            productId = pid
        }

        const response = await fetch("AddNewReview", {
            method: "POST",
            body: JSON.stringify({
                txt: review,
                id: productId
            }),
            headers: {
                "Content-Type": "application/json"
            }
        });
        if (response.ok) {

            let data = await response.json();
            console.log("data")
            console.log(data)

            if (data.success) {

                Notification().success({
                    message: data.data
                })

                document.getElementById("reviewtxt").value = ""
                loadReview(productId)

            } else {
                if(data.data == "You Need To Buy This Product First" || data.data == "You Need To Order This Product First"){
                    Notification().info({
                        message: data.data
                    })
                }else{
                    Notification().error({
                        message: data.data
                    })
                }
            }


        } else {
            Notification().error({
                message: "Please Try Again Latter"
            })
            console.log(response)
        }

    } else {
        Notification().info({
            message: "Please Write Review First"
        });
    }
}