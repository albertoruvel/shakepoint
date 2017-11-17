package com.shakepoint.web.facade.impl;

import com.shakepoint.web.core.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.shakepoint.web.facade.CpuFacade;

public class CpuFacadeImpl implements CpuFacade{
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	

}
