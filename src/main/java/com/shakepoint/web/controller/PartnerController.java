package com.shakepoint.web.controller;

import java.security.Principal;
import java.util.List;

import com.shakepoint.web.data.v1.dto.mvc.request.ProductOrderItemRequest;
import com.shakepoint.web.data.v1.dto.mvc.response.PartnerIndexContent;
import com.shakepoint.web.data.v1.dto.mvc.response.PerMachineValues;
import com.shakepoint.web.data.v1.dto.mvc.response.TotalIncomeValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.shakepoint.web.facade.PartnerFacade;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/partner")
public class PartnerController {

    @Autowired
    private PartnerFacade partnerFacade;

    @RequestMapping(value = {"/indexContent"}, method = RequestMethod.GET,
                    produces = "application/json")
    public @ResponseBody PartnerIndexContent indexContent(@RequestParam(value = "from", required = false) String from,
                                         @RequestParam(value = "to", required = false) String to,
                                         @AuthenticationPrincipal Principal principal) {
        return partnerFacade.getIndexContent(principal, from, to);
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public ModelAndView indexView(@AuthenticationPrincipal Principal principal) {
        return partnerFacade.getIndexView(principal);
    }

    @RequestMapping(value = {"/machines"}, method = RequestMethod.GET)
    public ModelAndView machinesView(@AuthenticationPrincipal Principal p,
                                     @RequestParam(value = "page_number", required = false, defaultValue = "1") int pageNumber) {
        return partnerFacade.getMachinesView(p, pageNumber);
    }

    @RequestMapping(value = {"/fails"}, method = RequestMethod.GET)
    public ModelAndView failsView(@AuthenticationPrincipal Principal p,
                                  @RequestParam(value = "page_number", required = false, defaultValue = "1") int pageNumber) {
        return partnerFacade.getFailsView(p, pageNumber);
    }

    @RequestMapping(value = {"/alerts"}, method = RequestMethod.GET)
    public ModelAndView alertsView(@AuthenticationPrincipal Principal p,
                                   @RequestParam(value = "page_number", required = false, defaultValue = "1") int pageNumber) {
        return partnerFacade.getAlertsView(p, pageNumber);
    }

    @RequestMapping(value = "/machine/{machineId}/", method = RequestMethod.GET)
    public ModelAndView getMachineProducts(@PathVariable(value = "machineId") String machineId,
                                           @RequestParam(value = "page_number", required = false, defaultValue = "1") int pageNumber,
                                           @AuthenticationPrincipal Principal p) {
        return partnerFacade.getMachineProductsView(machineId, p, pageNumber);
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public ModelAndView success(RedirectAttributes atts) {
        return new ModelAndView("shared/success");
    }

    @RequestMapping(value = "/machine/{machineId}/createOrder", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    String createOrder(@PathVariable(value = "machineId") String machineId,
                       @AuthenticationPrincipal Principal p,
                       RedirectAttributes atts,
                       @RequestBody List<ProductOrderItemRequest> request) {
        return partnerFacade.createPartnerOrder(p, machineId, request, atts);
    }

    @RequestMapping(value = "/machine/{machineId}/log", method = RequestMethod.GET)
    public ModelAndView getMachineLog(@PathVariable(value = "machineId") String machineId,
                                      @RequestParam(value = "page_number", required = false, defaultValue = "1") int pageNumber,
                                      @AuthenticationPrincipal Principal p) {
        return partnerFacade.getMachineFailsLogView(machineId, p, pageNumber);
    }
}
