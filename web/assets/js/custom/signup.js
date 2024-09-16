const signup = async() => {

    const user_dto = {
        first_name: document.getElementById("first_name").value,
        last_name: document.getElementById("last_name").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        re_type_password: document.getElementById("re_type_password").value
    };

    const response = await fetch("UserRegister", {
        method: "POST",
        body: JSON.stringify(user_dto),
        headers: {
            "Content-Type": "application/json"
        }
    });
    
    if (response.ok) {

        const data = await response.json();
        console.log(data);
        if (data.success) {
           window.location = "verify-account.html";
        } else {
            Notification().error({
                message:data.message
            })
        }
    }else{
        Notification().error({
            message:"Please Try Again Later"
        })

        console.log(response)
    }
};

