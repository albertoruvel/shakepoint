$(document).ready(function(){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var headers = {
    	'X-CSRF-TOKEN': token,
    };
    //get per machine values
    $.ajax({
        url: '/partner/indexContent',
        type: 'GET',
         headers: headers,
         accept: 'application/json',
         success: function(response){
            $('#last-signin').val(response.lastSignin);
            $('#alerted-machines').val(response.alertedMachines);
            $('#user-name').val(response.partner.name);
            createPerMachineChart(response.perMachineValues);
            createMachinesCharts(response.range, response.machines);
         }
    });
});

function createMachinesCharts(range, machines){
    $('#machines-data-container').html('');
    for (var i = 0; i < machines.length; i ++){
        var div = $('<div>').attr('id', machines[i].machineId);
        $(div).appendTo('#machines-data-container');
        Highcharts.chart(machines[i].machineId, {
            chart: {
                type: 'bar'
            },
            title: {
                text: machines[i].machineName
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                categories: machines.products,
                title: {
                    text: null
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Total de productos vendidos',
                    align: 'high'
                },
                labels: {
                    overflow: 'justify'
                }
            },
            tooltip: {
                valueSuffix: ' unidades'
            },
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: true
                    }
                }
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'top',
                x: -40,
                y: 80,
                floating: true,
                borderWidth: 1,
                backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
                shadow: true
            },
            credits: {
                enabled: false
            },
            series: [{
                name: range[0] + ' - ' + range[range.length - 1],
                data: machines[i].data
            }]
        });
    }
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