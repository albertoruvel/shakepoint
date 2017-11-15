package com.shakepoint.web.core.cpu;

import com.shakepoint.web.data.dto.cpu.CpuRequest;

public interface CpuServiceManager {
	public void sendPreAuthorizedPurchase(CpuRequest cpuRequest);
}
