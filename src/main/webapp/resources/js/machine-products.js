$(function(){
	ko.applyBindings(new MachineProductsViewModel());
}); 

function MachineProductsViewModel(){
	//get token and header 
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var headers = {
			'X-CSRF-TOKEN': token,
		}; 
	//get machineId 
	var machineId = $("#machine_id").val(); 
	
	var self = this; 
	self.hasAlertedProducts = ko.observable(false); 
	self.alertedProducts = ko.observable(0);
	self.allProducts = ko.observableArray([]); 
	self.machineProducts = ko.observableArray([]); 
	self.machineName = ko.observable(); 
	self.machineDescription = ko.observable();
	self.technicianCard = ko.observable(false);
	self.technicianName = ko.observable(); 
	self.technicianEmail = ko.observable();
	self.technicianId = ko.observable(); 
	self.showAllTechnicians = ko.observable(false); 
	self.technicians = ko.observableArray([]); 
	self.selectedTechnician = ko.observable(); 
	self.maxSlots = ko.observable(6); 
	self.minSlots = ko.observable(1); 
	
	self.hasAllProducts = ko.observable(false);
	self.deleteTechnician = function(){
		//delete current technician
		$.ajax({
			url: '/admin/update_technician_machine?machine_id=' + machineId + '&tech_id=' + self.technicianId() + '&option=0', 
			type: 'POST', 
			accept: 'application/json',
			headers: headers,
			success: function(response){
				//the machine has been removed from technician assigned-machines
				self.technicianCard(false); 
				self.showAllTechnicians(true); 
			}, 
			error: function(error){
				if(error.responseText === 'OK'){
					self.technicianCard(false); 
					self.showAllTechnicians(true); 
				}
			}
		}); 
	}; 
	
	self.addNewTechnician = function(){
		//create a new ajax request 
		$.ajax({
			url: '/admin/update_technician_machine?machine_id=' + machineId + '&tech_id=' + self.selectedTechnician().id  + '&option=1', 
			type: 'POST', 
			accept: 'application/json',
			headers: headers,
			success: function(machineProduct){
				//The product has been added to the machine 
				self.showAllTechnicians(false); 
				self.technicianCard(true); 
				self.technicianId(self.selectedTechnician().id);
				self.technicianName(self.selectedTechnician().name);
				self.technicianEmail(self.selectedTechnician().email);
			}, 
			error: function(error){
				if(error.responseText === 'OK'){
					self.showAllTechnicians(false); 
					self.technicianCard(true); 
					self.technicianId(self.selectedTechnician().id);
					self.technicianName(self.selectedTechnician().name);
					self.technicianEmail(self.selectedTechnician().email);
				}
			}
		});
	}; 
	
	self.addProduct = function(product){
		//slot number
		var slotNumber = $("input#" + product.id).val(); 
		if(product.type != 1){
			if(!slotNumber || slotNumber <= 0){
				alert('Se debe de especificar un slot para el producto'); 
				return;
			}
		}else slotNumber = -1; 
		$.ajax({
			url: '/admin/add_machine_product?machine_id=' + machineId + '&product_id=' + product.id  + '&slot_number=' + slotNumber, 
			type: 'POST', 
			accept: 'application/json',
			headers: headers,
			success: function(machineProduct){
				//The product has been added to the machine 
				if(machineProduct.id != null){
					self.machineProducts.push(machineProduct);
					self.allProducts.remove(product); 
				}else{
					alert(machineProduct.message); 
				}
			}, 
			error: function(error){
				
			}
		}); 
	};
	
	self.deleteProduct = function(machineProduct){
		$.ajax({
			url: '/admin/delete_machine_product?mp_id=' + machineProduct.id, 
			type: 'POST', 
			accept: 'application/json',
			headers: headers,
			success: function(product){
				//The product has been added to the machine 
				self.allProducts.push(product);
				self.machineProducts.remove(machineProduct); 
			}, 
			error: function(error){
				
			}
		}); 
	};
	//get content 
	$.ajax({
		url: '/admin/get_machine_products_content?machine_id=' + machineId, 
		type: 'GET', 
		accept: 'application/json',
		headers: headers,
		success: function(response){
			//bind response to view 
			bindContent(self, response);
		}, 
		error: function(error){
			
		}
	}); 
}

function bindContent(self, response){
	//bind machine data 
	self.machineName(response.machine.name); 
	self.machineDescription(response.machine.description);
	if(response.alertedProducts > 0){
		self.hasAlertedProducts(true);
		self.alertedProducts(response.alertedProducts); 
	}
	if(response.technician){
		//show the technician card 
		self.technicianCard(true);
		self.showAllTechnicians(false); 
		self.technicianName(response.technician.name); 
		self.technicianEmail(response.technician.email);
		self.technicianId(response.technician.id); 
	}else{
		//show the select with technicians 
		self.technicianCard(false); 
		self.showAllTechnicians(true);
	}
	//add all products 
	$.each(response.products, function(i, v){
		//add the new product if is not present in the machine
		var product = $.grep(response.machineProducts, function(element, index){
			if(element.productId === v.id)return element; 
		}); 
		if(! product[0]){
			//the product is not on the machine
			//add the current product 
			self.allProducts.push(v); 
		}
			
	}); 
	//add machine products 
	$.each(response.machineProducts, function(i, v){
		//add the new product if is not present in the machine
		//add the current product
        			self.machineProducts.push(v);
		/**var product = $.grep(response.products, function(element, index){
			if(element.productId === v.id)return element; 
		}); 
		if(! product[0]){
			//the product is not on the machine

		}**/
			
	});
	//add technicians 
	$.each(response.technicians, function(i, v){
		//add the technician 
		self.technicians.push(v); 
	}); 
}