package com.shakepoint.web.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shakepoint.web.facade.TechnicianFacade;

@Controller
@RequestMapping(value="/tech")
public class TechnicianController {
	
	@Autowired
	private TechnicianFacade techFacade;
	
	@RequestMapping(value={"/", "/index"}, method=RequestMethod.GET)
	public ModelAndView indexView(@AuthenticationPrincipal Principal p){
		return techFacade.getIndexView(p); 
	}
	
	@RequestMapping(value={"/machines"}, method=RequestMethod.GET)
	public ModelAndView machinesView(@AuthenticationPrincipal Principal p, 
									 @RequestParam(value="page_number", required=false, defaultValue="1")int pageNumber){
		return techFacade.getMachinesView(p, pageNumber); 
	}
	
	@RequestMapping(value={"/fails"}, method=RequestMethod.GET)
	public ModelAndView failsView(@AuthenticationPrincipal Principal p, 
								  @RequestParam(value="page_number", required=false, defaultValue="1")int pageNumber){
		return techFacade.getFailsView(p, pageNumber); 
	}
	
	@RequestMapping(value={"/alerts"}, method=RequestMethod.GET)
	public ModelAndView alertsView(@AuthenticationPrincipal Principal p, 
								   @RequestParam(value="page_number", required=false, defaultValue="1")int pageNumber){
		return techFacade.getAlertsView(p, pageNumber); 
	}
	
	@RequestMapping(value="/machine/{machineId}/", method=RequestMethod.GET)
	public ModelAndView getMachineProducts(@PathVariable(value="machineId")String machineId,
										   @RequestParam(value="page_number", required=false, defaultValue="1")int pageNumber, 
										   @AuthenticationPrincipal Principal p){
		return techFacade.getMachineProductsView(machineId, p, pageNumber); 
	}
	
	@RequestMapping(value="/machine/{machineId}/log", method=RequestMethod.GET)
	public ModelAndView getMachineLog(@PathVariable(value="machineId")String machineId,
									  @RequestParam(value="page_number", required=false, defaultValue="1")int pageNumber, 
									  @AuthenticationPrincipal Principal p){
		return techFacade.getMachineFailsLogView(machineId, p, pageNumber); 
	}
}
