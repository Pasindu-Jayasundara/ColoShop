window.addEventListener("load",()=>{
    loadCart()
})
async function sendMessage() {

    let titleM = document.getElementById("title").value
    let msgM = document.getElementById("txt").value

    if (titleM.trim() == "") {

        new Notification().error({
            message: "Missing Title"
        })

    } else if (msgM.trim() == "") {

        new Notification().error({
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
                new Notification().success({
                    message: jsonData.data
                })
            } else {
                new Notification().error({
                    message: jsonData.data
                })
            }
            console.log(jsonData);
        } else {
            new Notification().error({
                message: "Please Try Again Later"
            })
            console.error(response.statusText);
        }

    }

}