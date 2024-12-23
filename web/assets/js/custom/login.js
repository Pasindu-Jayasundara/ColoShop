const popup = Notification();

// window.onload = () => {
//     checkLogin()
// }

// function checkLogin() {
//     let user = sessionStorage.getItem('user')
//     if (user!=null) {

//         if (document.referrer.includes("/wishlist.html")) {
//             window.location = "wishlist.html";
//         } else if (document.referrer.includes("/shoping-cart.html")) {
//             window.location = "shoping-cart.html";
//         } else if (document.referrer.includes("/contact.html")) {
//             window.location = "contact.html";
//         } else {
//             window.location = "my-account.html";
//         }
//     }
// }

const login = async () => {

    console.log("Login");

    const response = await fetch("UserLogin", {
        method: "POST",
        body: JSON.stringify({
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        }),
        headers: {
            "Content-Type": "application/json"
        }
    });


    if (response.ok) {

        const data = await response.json();
        if (data.success) {
            if (data.data == "Login Success") {
                if (sessionStorage.getItem("user") == null) {
                    sessionStorage.setItem("user", JSON.stringify(data.data));
                }
            }

            let re = sessionStorage.getItem("redirect")
            if(re == 'true'){
                sessionStorage.setItem("redirect",false)
                if (document.referrer.includes("/wishlist.html")) {
                    window.location = "wishlist.html";
                } else if (document.referrer.includes("/shoping-cart.html")) {
                    window.location = "shoping-cart.html";
                } else if (document.referrer.includes("/contact.html")) {
                    window.location = "contact.html";
                } 
            }else {
                window.location = "my-account.html";
            }
            
        } else {
            popup.error({
                message: data.data
            });
        }
    } else {
        popup.error({
            message: "Please Try Again Later"
        })

        console.log(response)
    }
};