package com.shakepoint.web.core.repository;


import com.shakepoint.web.data.v1.entity.ShakepointMachine;
import com.shakepoint.web.data.v1.entity.ShakepointMachineProductStatus;
import com.shakepoint.web.data.v1.entity.ShakepointProduct;

import java.util.List;

/**
 *
 * @author Alberto Rubalcaba
 */
public interface MachineRepository {
	public boolean containProduct(String machineId, String productId); 
    public void addMachine(ShakepointMachine machine);
    public List<ShakepointMachine> getMachines(int pageNumber);
    public ShakepointMachine getMachine(String machineId);
    public int getAlertedproducts(String machineId);
    public void updateMachine(ShakepointMachine machine);
    public void addMachineProduct(ShakepointMachineProductStatus mp);
    public ShakepointMachineProductStatus getMachineProduct(String id);
    public List<ShakepointMachineProductStatus> getMachineProducts(String machineId);
    public void deleteMachineProduct(String id);
    public List<ShakepointMachine> getAlertedMachines(String technicianId, int pageNumber);
    public List<ShakepointMachine> getFailedMachines(String technicianId, int pageNumber);
    public int getActiveMachines(); 
    public int getAlertedMachines();
    public int getAlertedMachines(String technicianId);
    public List<ShakepointMachine> getTechnicianMachines(String id, int pageNumber);

    public boolean isMachineAlerted(String id);

    public List<ShakepointMachine> searchByName(String machineName);
}
