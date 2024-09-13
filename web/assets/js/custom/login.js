const login = async() => {

    const response = await fetch("UserLogin");
    if (response.ok) {

        const data = await response.json();
        if(data.success){
            
        }else{
            alert(dara.message);
        }
    }
};