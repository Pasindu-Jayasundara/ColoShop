const verifyAccount = async() => {

    const dto = {
        token: document.getElementById("verification").value
    };

    const response = await fetch(
            "VerifyNewUser",
            {
                method: "POST",
                body: JSON.stringify(dto),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        console.log(response);
        const json = await response.json();
        console.log(json);

        if (json.success) {
            window.location = "index.html";
        } else {
            Notification().error({
                message:data.data
            })
        }
    } else {
        Notification().error({
            message:"Please Try Again Later"
        })

        console.log(response)
    }
};

