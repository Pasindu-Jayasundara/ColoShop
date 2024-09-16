window.addEventListener("load",()=>{

    loadData()

})

async function addNewProduct() {
    const formData = new FormData();

    formData.append("name", document.getElementById("inputEmail4").value);
    formData.append("unit_price", document.getElementById("inputAddress").value);
    formData.append("delivery_fee", document.getElementById("inputAddress2").value);
    formData.append("size", document.getElementById("size").value);
    formData.append("color", document.getElementById("color").value);
    formData.append("category", document.getElementById("category").value);
    formData.append("description", document.getElementById("inputPassword4").value);
    formData.append("brand", document.getElementById("brand").value);
    formData.append("img1", document.getElementById("img1").files[0]);
    formData.append("img2", document.getElementById("img2").files[0]);
    formData.append("img3", document.getElementById("img3").files[0]);

        const response = await fetch("AddNewProduct", {
            method: "POST",
            body: formData 
        });

        if (response.ok) {
            const jsonData = await response.json();
            console.log(jsonData);
        } else {
            console.error("Failed to submit the form", response.statusText);
        }
}

async function loadData() {
    
    const response = await fetch("LoadFeatures");
    if(response.ok){
        
        // const jsonData = await response.json();
        if (response.ok) {

            console.log(response)
            const json = await response.json();

            console.log(json)

            const categoryList = json.categoryList;
            const colorList = json.colorList;
            const sizeList = json.sizeList;
            const brandList = json.brandList;

            loadSelectOptions("category", categoryList, ["category"]);
            loadSelectOptions("size", sizeList,["size"]);
            loadSelectOptions("color", colorList, ["color"]);
            loadSelectOptions("brand", brandList, ["brand"]);
        } else {
            console.log("Error");
        }

    }

}

function loadSelectOptions(selectTagId, list, propertyArray) {
    const selectTag = document.getElementById(selectTagId);

    for(var i = 0; i<list.length;i++){
        let optionTag = document.createElement("option");
        optionTag.value = list[i].id;
        optionTag.innerHTML = list[i][propertyArray[0]];
        selectTag.appendChild(optionTag);
    }
    // list.forEach((item) => {
    //     let optionTag = document.createElement("option");
    //     optionTag.value = item.id;
    //     optionTag.innerHTML = item[propertyArray[0]];
    //     selectTag.appendChild(optionTag);
    // });
}
