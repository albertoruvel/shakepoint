package com.shakepoint.web.facade;

import java.security.Principal;
import java.util.List;

import com.shakepoint.web.data.v1.dto.mvc.request.MachineProductOrderRequest;
import com.shakepoint.web.data.v1.dto.mvc.request.ProductOrderItemRequest;
import com.shakepoint.web.data.v1.dto.mvc.response.PartnerIndexContent;
import com.shakepoint.web.data.v1.dto.mvc.response.PerMachineValues;
import com.shakepoint.web.data.v1.dto.mvc.response.ProductLevelDescription;
import com.shakepoint.web.data.v1.dto.mvc.response.TotalIncomeValues;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface PartnerFacade {
	public ModelAndView getIndexView(Principal principal);
	public ModelAndView getAlertsView(Principal principal, int pageNumber); 
	public ModelAndView getMachinesView(Principal principal, int pageNumber); 
	public ModelAndView getFailsView(Principal principal, int pageNumber); 
	public ModelAndView getMachineFailsLogView(String machienId, Principal principal, int pageNumber);
	public ModelAndView getMachineProductsView(String machineId, Principal principal, int pageNumber);
	public String createPartnerOrder(Principal p, String machineId, List<ProductOrderItemRequest> request, RedirectAttributes atts);

	public PartnerIndexContent getIndexContent(Principal principal, String from, String to);
}
