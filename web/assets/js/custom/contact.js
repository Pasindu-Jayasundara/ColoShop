window.addEventListener("load",()=>{
    loadCart()
})
async function sendMessage() {

    let titleM = document.getElementById("title").value
    let msgM = document.getElementById("msg").value

    if (titleM.trim() == "") {
        Notification().error({
            message: "Missing Title"
        })

    } else if (msgM.trim() == "") {

        Notification().error({
            message: "Missing Message"
        })

    } else {
        Notification().info({
            message: "Sending Message ..."
        })

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

                document.getElementById("title").value = ""
                document.getElementById("msg").value = ""
                
                Notification().success({
                    message: jsonData.data
                })
            } else {
                new Notification().error({
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