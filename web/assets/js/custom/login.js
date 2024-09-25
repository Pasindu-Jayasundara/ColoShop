const login = async() => {

    const response = await fetch("UserLogin",{
        method:"POST",
        body:JSON.stringify({
            email:document.getElementById("email").value,
            password:document.getElementById("password").value
        }),
        headers:{
            "Content-Type":"application/json"
        }
    });

    const popup = Notification();

    if (response.ok) {

        const data = await response.json();
        if(data.success){
            window.location="my-account.html";
        }else{
            popup.error({
                message:data.data
            });
        }
    }else{
        popup.error({
            message:"Please Try Again Later"
        })

        console.log(response)
    }
};