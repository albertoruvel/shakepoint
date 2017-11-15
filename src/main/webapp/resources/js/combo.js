$(function(){
	var hasError = $("#hasError").val(); 
	ko.applyBindings(new ComboViewModel(hasError));
}); 

function ComboViewModel(hasError){
	var productId = $("#productId").val(); 
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var headers = {
		'X-CSRF-TOKEN': token,
	};
	//view model
	var self = this;
	self.hasError = ko.observable(false);
	self.showAllElements = ko.observable(false);  
	if(hasError == true){
		self.hasError(true); 
		self.showAllElements(false); 
	}else{
		self.hasError(false); 
		self.showAllElements(true);
		self.products = ko.observableArray([]);
		self.comboProducts = ko.observableArray([]); 
		self.combo = ko.observable({});
		self.comboId = ko.observable(productId);
		self.total = ko.computed(function(){
			var tot = 0; 
			$.each(self.comboProducts(), function(i, v){
				tot += v.price; 
			}); 
			return tot; 
		}); 
		
		self.saved = ko.computed(function(){
			
			return self.total() == 0 ? 0 : self.total() - self.combo().price; 
		}); 
		self.addProduct = function(product){
			updateCombo(product, true, headers, self); 
		}; 
		
		self.removeProduct = function(product){
			updateCombo(product, false, headers, self);
		};
		
		//get content 
		$.ajaxSetup({
			headers: headers
		});
		$.getJSON("get_content", function(json){
			//set combo 
			self.combo(json.combo);
			for(var i = 0; i < json.products.pageItems.length; i ++){
				//if the comboProducts array contains this product 
				var grep = $.grep(json.comboProducts.pageItems, function(e, k){
					if(e.id === json.products.pageItems[i])
						return e; 
				}); 
				if(! grep[0])self.products.push(json.products.pageItems[i]);
			}
			for(var i = 0; i < json.comboProducts.pageItems.length; i ++){
				self.comboProducts.push(json.comboProducts.pageItems[i]);
			}
			
		});
	}
}

function  updateCombo(product, add, headers, self){
	var value = null; 
	if(add == true)
		value = 1; 
	else
		value = 0;
	var url = '/admin/product/' + self.comboId() + '/update_combo?product_id=' + product.id + '&value=' + value; 
	$.ajax({
		url: url, 
		type: 'POST', 
		accept: 'application/json', 
		headers: headers, 
		success: function(response){
			switch(value){
				case 0:
					//delete product from comboProducts 
					self.comboProducts.remove(product); 
					//add to all products
					self.products.push(product); 
					break;
				case 1: 
					//delete product from products 
					self.products.remove(product); 
					//add to all products
					self.comboProducts.push(product);
					break; 
			}
		}, 
		error: function(request, status, error){
			alert('No se ha podido agregar el producto al paquete :('); 
		}
	}); 
}