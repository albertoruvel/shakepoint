$(document).ready(function () {
    $("#create-order").on('click', function(){
        var token = $("meta[name='_csrf']").attr("content");
    	var header = $("meta[name='_csrf_header']").attr("content");
    	var headers = {
    		'X-CSRF-TOKEN': token,
    	};
        var machineId = $('#machineId').val();
        //get all inputs with class product-quantity
        var inputs = $('.product-quantity');
        var array = new Array();
        $.each(inputs, function(index, value){
                var id = $(value).attr('id');
                var quantity = $(value).val();
                if (quantity == 0)
                    return;
                array.push({
                    productId : id,
                    quantity : quantity
                });
            });
        if (array.length == 0){
            alert('Debes de seleccionar al menos un producto para realizar una orden');
            return;
        }
        //create an ajax request
        $.ajax({
            url: "/partner/machine/" + machineId + '/createOrder',
            contentType: 'application/json',
            accept: 'test/plan',
            type: 'POST',
            headers: headers,
            data: JSON.stringify(array),
            success: function(response){
                //send redirect
                alert('Se ha creado tu solicitud, recibirás un correo de confirmación.');
                window.location.href = "/partner/"
            }
        });
    });
});