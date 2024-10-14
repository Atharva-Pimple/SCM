const viewContactModal=document.getElementById("view_contact_modal");
const baseUrl="http://localhost:8081";

const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
    id: 'view_contact_modal',
    override: true
};

const contactModal=new Modal(viewContactModal, options, instanceOptions);

function openContactModal(){
    contactModal.show();
}

function closeContactModal(){
    contactModal.hide();
}

async function loadContactData(id){
    try{
        const data=await(
            await fetch(`${baseUrl}/api/contact/${id}`)
        ).json();
        openContactModal();
        document.getElementById("contact_name").innerHTML=data.name;
        let add=data.address;
        document.getElementById("contact_address").innerText= add==null? "NA":add;
        let web=data.websiteLink
        document.getElementById("contact_link").innerText= web==""? "NA":web;
        document.getElementById("contact_link").href= web==""? "#":web;
        let linkedIn=data.linkedInLink;
        document.getElementById("contact_linkedIn").innerText= linkedIn==""? "NA":linkedIn;
        document.getElementById("contact_linkedIn").href= linkedIn==""? "#":linkedIn;
        document.getElementById("contact_email").innerText=data.email;
        document.getElementById("contact_phone").innerText=data.phoneNumber;
        const imgEle=document.getElementById("contact_img");
        if(data.picture){
            imgEle.src=data.picture;
        }else{
            imgEle.src="https://img.freepik.com/free-vector/illustration-businessman_53876-5856.jpg?t=st=1726048458~exp=1726052058~hmac=45874c0a67da4ff1e42a09c362683d373e2c014638d76e1d1596e2a3db55ce69&w=740";
        }
        
    }catch(e){
        console.log("Error: ",e);
    }
}

// delete contact

async function deleteContact(id) {
    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!"
      }).then((result) => {
        if (result.isConfirmed) {
            const url=`${baseUrl}/user/contacts/delete/${id}`;
            window.location.replace(url);
          Swal.fire({
            title: "Deleted!",
            text: "Your file has been deleted.",
            icon: "success"
          });
        }
    });
}

