package com.shakepoint.web.controller.rest;

import java.security.Principal;

import com.shakepoint.web.data.dto.cpu.QrCodeValidation;
import com.shakepoint.web.data.dto.cpu.QrCodeValidationRequest;
import com.shakepoint.web.data.dto.plc.PlcResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shakepoint.web.facade.CpuFacade;

@RequestMapping("/rest/cpu")
@RestController
public class RestCpuController {
	
	@Autowired
	private CpuFacade cpuFacade; 
	
	@RequestMapping(value="/validate_qr", method=RequestMethod.POST, 
			produces="application/json", consumes="application/json")
	public @ResponseBody
	QrCodeValidation validateQrCode(@AuthenticationPrincipal Principal principal,
									@RequestBody QrCodeValidationRequest request){
		return cpuFacade.validateQrCode(request, principal); 
	}
	
	@RequestMapping(value="/confirm_dispense", method=RequestMethod.POST, 
			consumes="application/json", produces="application/json")
	public @ResponseBody String confirmMachineDispense(@RequestParam(value="purchase_id", required=true)String purchaseId){
		return cpuFacade.confirmMachineDispense(purchaseId);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST, 
			produces="application/json", consumes="application/json")
	public @ResponseBody String updateMachineStatus(@RequestBody PlcResponse plcResponse){
		return cpuFacade.updateMachine(plcResponse); 
	}
	@RequestMapping(value="update_machine_status", method=RequestMethod.POST,
			produces="application/json")
	public String updateMachineStatus(@RequestParam(value="machine_id", required=true)String machineId,
									  @RequestParam(value="status", required=true)int status){
		return cpuFacade.updateMachineStatus(machineId, status);
	}

}
