/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Alberto Rubalcaba
 */
@Controller
public class DefaultController {
    
    /**
     * Get index view
     * @return 
     */
    @RequestMapping(value="/", method=RequestMethod.GET)
    public ModelAndView indexView(){
        return new ModelAndView("index"); 
    }
    
    /**
     * Get sign in view
     * @return 
     */
    @RequestMapping(value="/signin", method=RequestMethod.GET)
    public ModelAndView signinView(){
        return new ModelAndView("signin"); 
    }
}
