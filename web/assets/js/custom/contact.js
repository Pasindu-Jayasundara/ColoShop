async function sendMessage() {

    let titleM = document.getElementById("title").value
    let msgM = document.getElementById("txt").value

    if (titleM.trim() == "") {

        Notification().error({
            message: "Missing Title"
        })

    } else if (msgM.trim() == "") {

        Notification().error({
            message: "Missing Message"
        })

    } else {

        const response = await fetch("UserContactMessage", {
            method: "POST",
            body: JSON.stringify({
                msg: msgM,
                title: titleM
            }),
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const jsonData = await response.json();
            if (jsonData.success) {
                Notification().success({
                    message: jsonData.data
                })
            } else {
                Notification().error({
                    message: jsonData.data
                })
            }
            console.log(jsonData);
        } else {
            Notification().error({
                message: "Please Try Again Later"
            })
            console.error(response.statusText);
        }

    }

}