$(function(){
	ko.applyBindings(new TechnicianMachinesViewModel()); 
}); 

function TechnicianMachinesViewModel(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var headers = {
		'X-CSRF-TOKEN': token,
	};
	
	var self = this;
	self.technicianName = ko.observable(); 
	self.technicianEmail = ko.observable();
	self.allMachines = ko.observableArray([]); 
	self.asignedMachines = ko.observableArray([]); 
	
	var technicianId = $("#tech_id").val();
	self.removeMachine = function(machine){
		updateTechnicianMachine(self, technicianId, machine, 0, headers); 
	}; 
	self.asignMachine = function(machine){
		updateTechnicianMachine(self, technicianId, machine, 1, headers); 
	}; 
	
	$.ajax({
		url: '/admin/get_technician_machines_content?technician_id=' + technicianId,
		type: 'GET',
		headers: headers, 
		accept: 'application/json', 
		success: function(content){
			bindContent(self, content); 
		}, 
		error: function(error){
			
		}
	}); 
}

function bindContent(self, content){
	self.technicianName(content.technician.name);
	self.technicianEmail(content.technician.email); 
	$.each(content.allMachines, function(i, v){
		self.allMachines.push(v);
	}); 
	
	$.each(content.asignedMachines, function(i, v){
		self.asignedMachines.push(v);
	}); 
}

function updateTechnicianMachine(self, techId, machine, option, headers){
	$.ajax({
		url: '/admin/update_technician_machine?machine_id=' + machine.id + "&tech_id=" + techId + '&option=' + option, 
		type: 'POST', 
		headers: headers,
		accept: 'application/json', 
		success: function(response){
			if(response === 'OK'){
				if(option == 0){
					//deleted successfuly
					self.asignedMachines.remove(machine); 
					self.allMachines.push(machine); 
				}else if(option == 1){
					self.asignedMachines.push(machine); 
					self.allMachines.remove(machine); 
				}
			}
		}, 
		error: function(error){
			if(error.responseText === 'OK'){
				if(option == 0){
					//deleted successfuly
					self.asignedMachines.remove(machine); 
					self.allMachines.push(machine); 
				}else if(option == 1){
					self.asignedMachines.push(machine); 
					self.allMachines.remove(machine); 
				}
			}
		}
	}); 
}