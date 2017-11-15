package com.shakepoint.web.facade;

import java.security.Principal;

import org.springframework.web.servlet.ModelAndView;

public interface TechnicianFacade {
	public ModelAndView getIndexView(Principal principal);
	public ModelAndView getAlertsView(Principal principal, int pageNumber); 
	public ModelAndView getMachinesView(Principal principal, int pageNumber); 
	public ModelAndView getFailsView(Principal principal, int pageNumber); 
	public ModelAndView getMachineFailsLogView(String machienId, Principal principal, int pageNumber);
	public ModelAndView getMachineProductsView(String machineId, Principal principal, int pageNumber); 
}
