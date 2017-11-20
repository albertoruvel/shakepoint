package com.shakepoint.web.facade.impl;

import java.security.Principal;
import java.util.List;

import com.shakepoint.web.core.repository.FailRepository;
import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.v1.dto.mvc.response.Technician;
import com.shakepoint.web.data.v1.entity.ShakepointMachine;
import com.shakepoint.web.data.v1.entity.ShakepointUser;
import com.shakepoint.web.util.TransformationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import com.shakepoint.web.facade.TechnicianFacade;

public class TechnicianFacadeImpl implements TechnicianFacade{
	
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private MachineRepository machineRepository;
	
	@Autowired
	private FailRepository failRepository;
    

	@Override
	public ModelAndView getIndexView(Principal principal) {
		//get principal id 
		String id = userRepository.getUserId(principal.getName()); 
		//get user info 
		ShakepointUser user = userRepository.getTechnician(id);
		Technician technicianDto = TransformationUtils.createTechnician(user);
		//add to the model 
		ModelAndView mav = new ModelAndView("/tech/index");
		mav.addObject("user", technicianDto);
		//get number of alerted machines by technician
		int alertedMachines = machineRepository.getAlertedMachines(id);
		String lastSignin = userRepository.getLastSignin(id); 
		mav.addObject("lastSignin", lastSignin);
		mav.addObject("alertedMachines", alertedMachines);
		return mav;  
	}

	//todo: convert to dto
	@Override
	public ModelAndView getAlertsView(Principal principal, int pageNumber) {
		ModelAndView mav = new ModelAndView("/tech/alerts");
		final String id = userRepository.getUserId(principal.getName()); 
		List<ShakepointMachine> machines = machineRepository.getAlertedMachines(id, pageNumber);
		if(machines != null){
			mav.addObject("machines", machines);
		}
		return mav;
	}
	@Override
	public ModelAndView getMachinesView(Principal principal, int pageNumber) {
		ModelAndView mav = new ModelAndView("/tech/machines");
		//get technician id 
		String id = userRepository.getUserId(principal.getName()); 
		//get technician machines 
		List<ShakepointMachine> machines = machineRepository.getTechnicianMachines(id, pageNumber);
		//todo: convert to dto here....
		//add machines  
		mav.addObject("machines", machines);
		return mav;
	}

	//todo: convert to dto
	public ModelAndView getFailsView(Principal principal, int pageNumber) {
		ModelAndView mav = new ModelAndView("/tech/fails");
		String id = userRepository.getUserId(principal.getName()); 
		List<ShakepointMachine> machines = machineRepository.getFailedMachines(id, pageNumber);
		
			mav.addObject("machines", machines);
		return mav;
	}

	@Override
	public ModelAndView getMachineFailsLogView(String machienId, Principal principal, int pageNumber) {
		// TODO Auto-generated method stub
		ModelAndView mav =  new ModelAndView("/tech/log");
		return mav; 
	}

	public ModelAndView getMachineProductsView(String machineId, Principal principal, int pageNumber) {
		ModelAndView mav =  new ModelAndView("/tech/products");
		return mav; 
	}
}
