async function addtoNewsL() {

    let emailAdd = document.getElementById("nEmail").value
    if (emailAdd.trim() == "") {
        Notification().error({
            message: "Missing Email Address"
        })
    }else{

        const data = {
            "email":emailAdd
        }
    
        const response = await fetch("RegisterToNewsletter",{
            method:"POST",
            body:JSON.stringify(data),
            headers:{
                "Content-Type":"application/json"
            }
        });
    
        if (response.ok) {
    
            let data = await response.json();
            if(data.success){

                Notification().success({
                    message: data.data
                })

            }else{
                Notification().error({
                    message: data.data
                })
            }

    
        }else{
            Notification().error({
                message: "Please Try Again Latter"
            })
            console.log(response)
        }
    

    }

}