window.addEventListener("load", () => {

    loadWishlist()

})

const arr = [];
var isProductFirstTime = true;
const productArr = [];
const loadWishlist = async () => {

    const response = await fetch("LoadWishlist");
    if (response.ok) {

        const data = await response.json();
        if (data.success) {

            const productData = data.data;

            let parent = document.getElementById("productContainer");
            let productElement = document.getElementById("productElement");
                    parent.innerHTML = "";

            for (var i = 1; i <= productData.length; i++) {

                let product = productData[i - 1];
                console.log(product);

                productArr.push(product);

                if (!arr.includes(product.product.category.category)) {
                    // loadBigCategories(product.product.category.category);
                    // loadSmallCategories(product.product.category.category);

                    arr.push(product.product.category.category);
                }

                // if (isProductFirstTime) {
                //     parent.innerHTML = "";
                //     isProductFirstTime = false;
                // }

                //get product element
                let element = productElement.cloneNode(true);
                element.querySelector(".productName").innerHTML = product.product.name;
                element.querySelector(".productPrice").innerHTML = "Rs. " + product.product.unit_price;

                const trimmedPath = product.product.img1.replace("F:\\pasindu\\Git\\project\\ColoShop\\web\\", "");
                element.querySelector(".pimg").src = trimmedPath;
                element.querySelector(".productElementATag").addEventListener("click", (e) => {
                    e.preventDefault();
                    window.location.href="product-detail.html?product="+product.product.id
                });

                element.classList.replace("women", product.product.category.category);
                element.removeAttribute("style");
                element.querySelector(".addToWishlist").addEventListener("click", (e) => {
                    e.preventDefault();
                    removeFromWishlist(product.product.id)
                });

                element.querySelector(".productName").addEventListener("click", (e) => {
                    window.location.href = "product-detail.html?product=" + product.product.id;
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

            loadCart()

        } else {
            Notification().error({
                message: data.data
            })
            window.history.back()
        }
    } else {
        new Notification().error({
            message: "Please Try Again Latter"
        })
        console.log(response)
    }

};

const removeFromWishlist = async (productId) => {

    const response = await fetch("DeleteFromWishList", {
        method: "POST",
        body: JSON.stringify({
            id: productId
        }),
        headers: {
            "Content-Type": "application/json"
        }
    });
    if (response.ok) {
        // console.log("response");
        // console.log(await response.text());

        let data = await response.json();
        if (data.success) {

            Notification().success({
                message: data.data
            })

            loadWishlist()
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