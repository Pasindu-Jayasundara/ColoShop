window.addEventListener("load", () => {

    loadData()

})

var colorArr;
var brandArr;
var categoryArr;
var sizeArr;
async function loadData() {

    const response = await fetch("LoadAdminData");
    if (response.ok) {

        const jsonData = await response.json();
        if (jsonData.success) {
            const data = jsonData.data;

            const sellerCount = data.sellerCount;
            const buyerCount = data.buyerCount;
            const productCount = data.productCount;
            const userName = data.userName;
            const messageArr = data.messageArr;
            const colorArr = data.colorArr;
            const brandArr = data.brandArr;
            const categoryArr = data.categoryArr;
            const sizeArr = data.sizeArr;

            document.getElementById("sellerCount").innerHTML = sellerCount;
            document.getElementById("buyerCount").innerHTML = buyerCount;
            document.getElementById("productCount").innerHTML = productCount;
            document.getElementById("userName").innerHTML = userName;

            let messageTableBody = document.getElementById("tableBody");
            let messageRow = document.getElementById("msgRow");

            messageTableBody.innerHTML = "";

            let count = 1;
            messageArr.forEach(message => {

                let element = messageRow.cloneNode(true);

                element.removeAttribute("id");
                element.querySelector(".msgCount").innerHTML = count;
                element.querySelector(".mssageTitle").innerHTML = message.title;
                element.querySelector(".mssageText").innerHTML = message.message;
                element.querySelector(".mssageDatetime").innerHTML = message.datetime;
                element.querySelector(".mssageStatus").innerHTML = message.message_status.status;
                element.querySelector(".msgBtn").setAttribute("data-bs-whatever", message.title);
                element.querySelector(".msgBtn").setAttribute("msg-id", message.id);
                element.querySelector(".msgBtn").setAttribute("id", message.id);
                messageTableBody.appendChild(element);

                count++;
            });

        } else {
            Notification().error({
                message: jsonData.data
            })
        }


    } else {
        Notification().error({
            message: "Please Try Again Later"
        })
        console.log(response)
    }

}

function loadSelectOptionTable() {

    let option = document.getElementById("featureList").value;
    if (option == 1) {//color
        loadTable(colorArr, color)
    } else if (option == 2) {//size
        loadTable(sizeArr, size)
    } else if (option == 3) {//category
        loadTable(categoryArr, category)
    } else if (option == 4) {//brand
        loadTable(brandArr, brand)
    } else {
        Notification().error({
            message: "Invalid Selection"
        })
    }

}

function loadTable(array, receiveName) {

    let tableBody = document.getElementById("manageTableBody");
    let row = document.getElementById("manageRow");

    tableBody.innerHTML = "";

    let count = 1;
    array.forEach(arrElement => {

        let element = row.cloneNode(true);

        element.removeAttribute("id");
        element.querySelector(".msgCount").innerHTML = count;
        element.querySelector(".avaliableOption").innerHTML = arrElement.receiveName;

        element.querySelector(".msgBtn").addEventListener("click", () => {
            remove(arrElement.id, receiveName)
        });
        tableBody.appendChild(element);

        count++;
    });
}

async function remove(elementid, ofWhat) {

    let isConfirmed = confirm("Are You Sure You Want to Remove Value ?")
    if (isConfirmed) {

        const response = await fetch("Remove", {
            method: "POST",
            body: {
                id: elementid,
                of: ofWhat
            },
            headers: {
                "Content-Type": "application/json"
            }
        });

        const popup = Notification();

        if (response.ok) {

            const data = await response.json();
            if (data.success) {

                popup.success({
                    message: "Removing Success"
                })

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

    }

}

var msgId;
async function sendReply() {

    const response = await fetch("ReplyToUserMessage", {
        method: "POST",
        body: {
            id: msgId,
            text: document.getElementById("replyText").value
        },
        headers: {
            "Content-Type": "application/json"
        }
    });

    const popup = Notification();

    if (response.ok) {

        const data = await response.json();
        if (data.success) {

            popup.success({
                message: "Reply Send Successfully"
            })

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

}

function openModel() {
    const exampleModal = document.getElementById('exampleModal')
    if (exampleModal) {
        exampleModal.addEventListener('show.bs.modal', event => {
            // Button that triggered the modal
            const button = event.relatedTarget
            // Extract info from data-bs-* attributes
            const recipient = button.getAttribute('data-bs-whatever')
            msgId = button.getAttribute('id')
            // If necessary, you could initiate an Ajax request here
            // and then do the updating in a callback.

            // Update the modal's content.
            const modalTitle = exampleModal.querySelector('.modal-title')
            const modalBodyInput = exampleModal.querySelector('.modal-body input')

            modalTitle.innerHTML = `Reply message To :<br><br> <span class="fs-5" style="color:grey;">${recipient}</span>`
        })
    }
}

async function sendNews() {

    const response = await fetch("NewsLetter", {
        method: "POST",
        body: {
            text: document.getElementById("exampleFormControlTextarea1").value
        },
        headers: {
            "Content-Type": "application/json"
        }
    });

    const popup = Notification();

    if (response.ok) {

        const data = await response.json();
        if (data.success) {

            popup.success({
                message: "Reply Send Successfully"
            })

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

}

async function logOut() {
    const response = await fetch("LogOut");

    const popup = Notification();

    if (response.ok) {

        const data = await response.json();
        if (data.success) {

            window.location = "admin-sign-in.html";

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
}

async function saveOption() {

    let of = document.getElementById("addNewFeatureList").value
    let newOption = document.getElementById("newOption").value



    const popup = Notification();

    if (of.trim() == "") {
        popup.error({
            message: "Missing Type"
        });
    } else if (newOption.trim() == "") {
        popup.error({
            message: "Missing New option"
        });
    } else {

        let typeText;
        if (of == 1) {
            typeText = "color"
        } else if (of == 2) {
            typeText = "size"
        } else if (of == 3) {
            typeText = "category"
        } else if (of == 4) {
            typeText = "brand"
        }else{
            popup.error({
                message: "Invalid Type"
            });
            return;
        }

        const response = await fetch("AddNewOption", {
            method: "POST",
            body: {
                type: typeText,
                option: newOption
            },
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {

            const data = await response.json();
            if (data.success) {

                popup.success({
                    message: "Reply Send Successfully"
                })

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

    }
}