package com.shakepoint.web.facade;

import com.shakepoint.web.data.dto.cpu.QrCodeValidation;
import com.shakepoint.web.data.dto.cpu.QrCodeValidationRequest;
import com.shakepoint.web.data.dto.plc.PlcResponse;

import java.security.Principal;


public interface CpuFacade {
    public QrCodeValidation validateQrCode(QrCodeValidationRequest request, Principal p);

    public String confirmMachineDispense(String purchaseId);

    public String updateMachine(PlcResponse plcResponse);

    public String updateMachineStatus(String machineId, int status);
}
