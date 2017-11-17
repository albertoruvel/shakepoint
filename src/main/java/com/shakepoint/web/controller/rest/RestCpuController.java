package com.shakepoint.web.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shakepoint.web.facade.CpuFacade;

@RequestMapping("/rest/cpu")
@RestController
public class RestCpuController {

    @Autowired
    private CpuFacade cpuFacade;

    //TODO: THIS CLASS WILL CONTAIN ALL OPERATIONS TO COMMUNICATE WITH MACHINE EXENDER (WILL DISCUSS LATER ABOUT THIS)

}
