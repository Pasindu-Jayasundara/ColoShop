window.addEventListener("load", () => {
    loadProduct();
});

const loadCategories = async () => {
    try {
        const response = await fetch("loadCategories");
        if (response.ok) {
            const jsonData = await response.json();
            const categoryList = jsonData.categoryList;

            const element = document.querySelector(".category-name-li");
            const parentElement = document.getElementById("category-list");
            parentElement.innerHTML = "";

            let firstTime = true;

            categoryList.forEach(category => {
                const cloned = element.cloneNode(true);
                
                cloned.innerText = category.category;
                cloned.setAttribute("data-id", category.id);
                cloned.setAttribute("data-filter", "."+ category.category);
                if(firstTime){
                    cloned.classList.add("active","is-checked");
                    firstTime=false;
                }
                
                parentElement.appendChild(cloned);
            });
            
        } else {
            console.log("Error fetching categories:", response.statusText);
        }
    } catch (error) {
        console.error("Error loading categories:", error);
    }
};

const loadProduct = async()=>{
  
    const response = await fetch("loadProduct");
    if(response.ok){
                        console.log(response);

        const data = await response.json();
                console.log(data);

        
    }
    
};