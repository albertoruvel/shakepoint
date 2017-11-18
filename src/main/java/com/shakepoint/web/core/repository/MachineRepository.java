package com.shakepoint.web.core.repository;


import com.shakepoint.web.data.dto.res.MachineDTO;
import com.shakepoint.web.data.dto.res.MachineProduct;
import com.shakepoint.web.data.dto.res.TechnicianMachine;
import com.shakepoint.web.data.entity.MachineProductModel;
import com.shakepoint.web.data.v1.entity.ShakepointMachine;

import java.util.List;

/**
 *
 * @author Alberto Rubalcaba
 */
public interface MachineRepository {
	public void updateMachineProductLevel(String machineId, String productId, int level); 
	public boolean containProduct(String machineId, String productId); 
    public void addMachine(ShakepointMachine machine);
    public List<ShakepointMachine> getMachines(int pageNumber);
    public ShakepointMachine getMachine(String machineId);
    public int getAlertedproducts(String machineId); 
    public List<ShakepointMachine> getMachineProducts(String machineId, int pageNumber);
    public void addTechnicianMachine(String technicianId, String machineId);
    public void deleteTechnicianMachine(String machineId);
    public void addMachineProduct(MachineProductModel mp);
    public MachineProduct getMachineProduct(String id);
    public void deleteMachineProduct(String id);
    public List<ShakepointMachine> getTechnicianAssignedMachines(String technicianId, int pageNumber);
    public List<ShakepointMachine> getAlertedMachines(String technicianId, int pageNumber);
    public List<ShakepointMachine> getFailedMachines(String technicianId, int pageNumber);
    public int getActiveMachines(); 
    public int getAlertedMachines();
    public int getAlertedMachines(String technicianId);
    public List<TechnicianMachine> getTechnicianMachines(String id, int pageNumber);
}
