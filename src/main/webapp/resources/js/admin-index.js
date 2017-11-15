$(function(){
	ko.applyBindings(new AdminIndexViewModel()); 
}); 

function configDatepicker(){
	ko.bindingHandlers.datepicker = {
		    init: function(element, valueAccessor, allBindingsAccessor) {
		      //initialize datepicker with some optional options
		      var options = allBindingsAccessor().datepickerOptions || {};
		      $(element).datepicker(options);

		      //when a user changes the date, update the view model
		      ko.utils.registerEventHandler(element, "changeDate", function(event) {
		             var value = valueAccessor();
		             if (ko.isObservable(value)) {
		                 value(event.date);
		             }                
		      });
		    },
		    update: function(element, valueAccessor)   {
		        var widget = $(element).data("datepicker");
		         //when the view model is updated, update the widget
		        if (widget) {
		            widget.date = ko.utils.unwrapObservable(valueAccessor());
		            if (widget.date) {
		                widget.setValue();            
		            }
		        }
		    }
		};
	$('.input-sm').datepicker({
		/**format: {
			toDisplay: function (date, format, language) {
                var d = new Date(date);
                d.setDate(d.getDate() - 7);
                return d.toLocaleDateString();
            },
            toValue: function (date, format, language) {
                var d = new Date(date);
                d.setDate(d.getDate() + 7);
                return new Date(d).toLocaleDateString();
            }
		},**/
		autoclose: true, 
		clearBtn : true, 
		inmediateUpdates: true
	});
}

function AdminIndexViewModel(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var headers = {
		'X-CSRF-TOKEN': token,
	};
	
	//configures datepicker
	configDatepicker(); 
	
	var self = this;
	self.activeMachines = ko.observable(0); 
	self.alertedMachines = ko.observable(0); 
	self.todayTotal = ko.observable(0); 
	self.registeredTechnicians = ko.observable(0);
	//per machine analytics
	self.perMachineFromValue = ko.observable('');
	self.perMachineToValue = ko.observable('');
	self.totalIncomeFromValue = ko.observable('');
	self.totalIncomeToValue = ko.observable('');
	
	//total income
	self.updateTotalIncomeChart = function(){
		if(self.totalIncomeFromValue() === '' || self.totalIncomeToValue() === ''){
			alert("Debe de legir un rango de fechas");
			return; 
		}else{
			var from = new Date(self.totalIncomeFromValue()).toLocaleDateString();
			var to = new Date(self.totalIncomeToValue()).toLocaleDateString();
			getTotalIncomeChart(from, to, headers); 
		}
	}; 
	
	self.updatePerMachineChart = function(){
		if(self.perMachineFromValue() === '' || self.perMachineToValue() === ''){
			//empty values
			alert("Debe de elegir un rango de fechas");
			return; 
		}else{
			var from = new Date(self.perMachineFromValue()).toLocaleDateString();
			var to = new Date(self.perMachineToValue()).toLocaleDateString();
			getPerMachineChart(from, to, headers); 
		}
	}; 
	self.perMachineFromChanged = function(){
		if(self.perMachineToValue() === '')
			//does nothing, wait to choose 'to' input
			return; 
		else{
			getPerMachineChart(self.perMachineFromValue(), self.perMachineToValue(), headers);
		}
	};
	self.perMachineToChanged = function(){
		if(self.perMachineFromValue() === '')
			//does nothing, wait to choose 'from' input
			return;
		else{
			getPerMachineChart(self.perMachineFromValue(), self.perMachineToValue(), headers);
		}
	}; 
	$.ajax({
		url: '/admin/get_index_content',
		type: 'GET', 
		headers: headers, 
		accept: 'application/json', 
		success: function(response){
			 createPerMachineChart(response.perMachineValues);
			 createTotalIncomeChart(response.totalIncomeValues); 
		}, 
		error: function(error){
			
		}
	}); 
}

function getTotalIncomeChart(from, to, headers){
	$.ajax({
		url: '/admin/get_total_income_values?from=' + from + '&to=' + to, 
		type: 'GET', 
		headers: headers, 
		accept: 'application/json', 
		success: function(response){
			createTotalIncomeChart(response); 
		}, 
		error: function(error){
			
		}
	});
}

function getPerMachineChart(from, to, headers){
	$.ajax({
		url: '/admin/get_index_per_machine_values?from=' + from + '&to=' + to, 
		type: 'GET', 
		headers: headers, 
		accept: 'application/json', 
		success: function(response){
			createPerMachineChart(response); 
		}, 
		error: function(error){
			
		}
	}); 
}

function createTotalIncomeChart(totalIncomeObject){
	$("#totals").highcharts({
		chart: {
            type: 'column'
        },
        title: {
            text: 'Ingresos totales'
        },
        subtitle: {
            text: 'Por rango de fecha'
        },
        xAxis: {
            categories: totalIncomeObject.range,
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: 'MXN'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: getTotalIncomeValuesArray(totalIncomeObject)
		
	}); 
}

function getTotalIncomeValuesArray(totalIncomeObject){
	var array = new Array(); 
	var item = {
		name: 'Ingresos totales', 
		data: totalIncomeObject.values
	};
	array.push(item);
	return array; 
}

function createPerMachineChart(perMachineObject){
	$("#perMachineTotals").highcharts({
		title: {
			text: 'Ingresos por máquina', 
			x: -20 // center
		},
		subtitle: {
			text: 'Por rango de fecha', 
			x: -20
		},
		xAxis: {
			categories: perMachineObject.range,
		},
		
		yAxis: {
			title: {
				text: 'Ingresos en MXN'
			}, 
			plotLines: [{
	            value: 0,
	            width: 1,
	            color: '#808080'
	        }]
		}, 
		tooltip: {
	      	valueSuffix: 'MXN'
	    }, 
	    legend: {
	       	layout: 'vertical', 
	       	align: 'right', 
	       	verticalAlign: 'middle', 
	       	borderWidth: 0
	    },
	    series: getPerMachineValuesArray(perMachineObject.values)
		
	}); 
}

function getPerMachineValuesArray(values){
	var array = new Array(); 
	for(var key in values){
		var data = {
			name: key, 
			data: values[key] 
		}; 
		array.push(data); 
	}
	return array; 
}