console.log("this is script file");

const paymentStart = () => {
    console.log("Payment Started");
    let amount = $("#payment_field").val();
    console.log(amount);

    if(amount=="" || amount==null){
        alert("Amount required");
        return;
    } 

    // we will use ajax to send request to server to create order - hquery

    $.ajax(
 
        {
            url:'/create_order',
            data:JSON.stringify({amount:amount,info:'order_request'}),
            contentType:'application/json',
            type:'POST',
            dataType:'json',
            success:function(response){

                // invoked when success
                console.log(response)
                if(response.status == "created"){
                    //  open payment form
                    let options = {
                        key : "rzp_test_tVBRqSf0PDIupW",
                        amount : response.amount,
                        currency : "INR",
                        name : "Testing",
                        description : "test",
                        order_id : response.id,
                        "handler" : function(response){
                            console.log(response.razorpay_payment_id);
        					console.log(response.razorpay_order_id);
       						console.log(response.razorpay_signature)

                            updatePaymentOnServer(response.razorpay_order_id, response.razorpay_payment_id, "Paid");
                            
                        },
                        prefill:{
                            name : "",
                            email : "",
                            contact : ""
                        },
                        notes : {
                            address : "Vrushabh Gundecha"
                        },
                        theme : {
                            color : "#3399cc"
                        }
                    };

                    //  initiate a payment
                    let rzp = new Razorpay(options);

                    rzp.on("payment.failed", function(response){
                        console.log(response.error.code);
                        console.log(response.error.description);
                        console.log(response.error.source);
                        console.log(response.error.step);
                        console.log(response.error.reason);
                        console.log(response.error.metadata.order_id);
                        console.log(response.error.metadata.payment_id);
                        alert("Payment Failed");
                    });

                    rzp.open();
                    
                }
            },
            error:function(error){
                
                // invoked when error
                console.log(error)
                alert("Something went wrong !")
            }

        }

    )

};

function updatePaymentOnServer(order_id,payment_id, status){

    $.ajax(
        {
            url:'/update_server',
            data:JSON.stringify({order_id : order_id, payment_id : payment_id, status : status}),
            contentType:'application/json',
            type:'POST',
            dataType:'json',
            success:function(response){
                alert("Congrats, Payment Successfull");
            },
            error:function(error){
                alert("Payment Successful but, not update on server!!"); 
            }
        }
    );


};



