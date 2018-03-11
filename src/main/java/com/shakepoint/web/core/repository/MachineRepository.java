package com.shakepoint.web.core.repository;


import com.shakepoint.web.data.v1.entity.VendingMachine;
import com.shakepoint.web.data.v1.entity.VendingMachineProductStatus;

import java.util.List;

/**
 *
 * @author Alberto Rubalcaba
 */
public interface MachineRepository {
	public boolean containProduct(String machineId, String productId); 
    public void addMachine(VendingMachine machine);
    public List<VendingMachine> getMachines(int pageNumber);
    public VendingMachine getMachine(String machineId);
    public int getAlertedproducts(String machineId);
    public void updateMachine(VendingMachine machine);
    public void addMachineProduct(VendingMachineProductStatus mp);
    public VendingMachineProductStatus getMachineProduct(String id);
    public List<VendingMachineProductStatus> getMachineProducts(String machineId);
    public void deleteMachineProduct(String id);
    public List<VendingMachine> getAlertedMachines(String technicianId, int pageNumber);
    public List<VendingMachine> getFailedMachines(String technicianId, int pageNumber);
    public int getActiveMachines(); 
    public int getAlertedMachines();
    public int getAlertedMachines(String technicianId);
    public List<VendingMachine> getTechnicianMachines(String id, int pageNumber);

    public boolean isMachineAlerted(String id);

    public List<VendingMachine> searchByName(String machineName);
}
