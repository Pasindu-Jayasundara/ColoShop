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
            console.log("f");

            window.location = "index.html";
        } else {
            console.log("g");

            document.getElementById("message").innerHTML = json.content;
        }
    } else {
        document.getElementById("message").innerHTML = "Please try agin later";
    }
};

