let currentTheme=getTheme();
document.addEventListener('DOMContentLoaded',()=>{
    changeTheme();
})


function changeTheme(){
    changePageTheme(currentTheme,currentTheme)

    const changeThemeButton=document.querySelector('#theme_change_button')
    // change theme
    changeThemeButton.addEventListener("click",()=>{
        let oldTheme=currentTheme;
        if(currentTheme==="light"){
            currentTheme="dark";
        }else{
            currentTheme="light";
        }
        changePageTheme(currentTheme,oldTheme);
    });

}

// set theme to local storage
function setTheme(theme){
    localStorage.setItem("theme",theme);
}

// get theme from local storage
function getTheme(){
    let theme=localStorage.getItem("theme");
    return theme? theme:"light";
}

function changePageTheme(theme,oldTheme){
    setTheme(theme);
    document.querySelector('html').classList.remove(oldTheme);
    document.querySelector('html').classList.add(theme);

    document
        .querySelector('#theme_change_button')
        .querySelector('span').textContent= currentTheme=="light"? "Dark":"Light";

}