/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.core.repository.impl;

import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.ProductRepository;
import com.shakepoint.web.data.dto.res.MachineDTO;
import com.shakepoint.web.data.dto.res.MachineProduct;
import com.shakepoint.web.data.dto.res.ProductDTO;
import com.shakepoint.web.data.dto.res.TechnicianMachine;
import com.shakepoint.web.data.entity.MachineProductModel;
import com.shakepoint.web.data.v1.entity.ShakepointMachine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alberto Rubalcaba
 */
public class MachineRepositoryImpl implements MachineRepository {

    private final Logger log = Logger.getLogger(getClass());

    @Autowired
    private ProductRepository productsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public MachineRepositoryImpl() {

    }

    private static final String GET_TECHNICIAN_ALERTED_MACHINES = "select m.id, m.name, m.description, m.location, m.slots, m.technician_id as technicianId, "
            + "(select count(*) from machine_product mp where mp.machine_id = m.id) productsCount "
            + "from machine m inner join machine_product mp on mp.machine_id = m.id "
            + "where m.technician_id = ? and mp.available_percentage < 30 limit and m.has_error = 1";

    @Override
    public List<MachineDTO> getAlertedMachines(String technicianId, int pageNumber) {
        List<MachineDTO> page = null;
        try {
            page = entityManager.createNativeQuery(GET_TECHNICIAN_ALERTED_MACHINES)
                    .setParameter(1, technicianId).getResultList();
            return page;
        } catch (Exception ex) {
            log.error("Could not get technician machines with errors", ex);
            return null;
        }
    }


    private static final String GET_TECHNICIAN_FAILED_MACHINES = "select m.id, m.name, m.description, m.location, m.slots, m.technician_id as technicianId, "
            + "(select count(*) from machine_product mp where mp.machine_id = m.id) productsCount "
            + "from machine m where m.technician_id = ? and m.has_error = 1";

    @Override
    public List<MachineDTO> getFailedMachines(String technicianId, int pageNumber) {
        List<MachineDTO> page = null;
        try {
            page = entityManager.createNativeQuery(GET_TECHNICIAN_FAILED_MACHINES)
                    .setParameter(1, technicianId).getResultList();
            return page;
        } catch (Exception ex) {
            log.error("Could not get technician machines with with errors: " + ex.getMessage());
        }
        return page;
    }

    private static final String GET_TECHNICIAN_MACHINES_PAGE = "select m.id, m.name, m.description, m.slots, "
            + "(select count(*) from machine_product mp where mp.machine_id = m.id) products, "
            + "(select count(*) from machine_product mp where mp.machine_id = m.id and mp.available_percentage < 30) alerted"
            + "from machine m where m.technician_id = ? limit %d,%d";

    @Override
    public List<TechnicianMachine> getTechnicianMachines(String id, int pageNumber) {
        List<TechnicianMachine> page = null;
        try {
            page = entityManager.createNativeQuery(GET_TECHNICIAN_MACHINES_PAGE)
                    .setParameter(1, id).getResultList();
            return page;
        } catch (Exception ex) {
            log.error("Could not get technician machines" + ex);
            return null;
        }
    }

    private static final String GET_ALERTED_MACHINES_COUNT = "select count(*) from machine m "
            + "inner join machine_product mp on mp.machine_id = m.id "
            + "where mp.available_percentage < 30";

    @Override
    public int getAlertedMachines() {
        try {
            Long count = (Long) entityManager.createNativeQuery(GET_ALERTED_MACHINES_COUNT)
                    .getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get alerted machines count", ex);
            return 0;
        }
    }

    private static final String GET_ACTIVE_MACHINES = "select count(*) from machine where active = 1";

    @Override
    public int getActiveMachines() {
        try {
            Long count = (Long) entityManager.createNativeQuery(GET_ACTIVE_MACHINES).getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get active machines count", ex);
            return 0;
        }
    }

    private static final String GET_TECHNICIAN_MACHINES = "select m.id, m.name, m.description, m.location, m.slots, m.technician_id as technicianId, "
            + "(select count(*) from machine_product mp where mp.machine_id = m.id) productsCount "
            + "from machine m where m.technician_id = ?";

    @Override
    public List<MachineDTO> getTechnicianAsignedMachines(String technicianId, int pageNumber) {
        List<MachineDTO> page = null;
        try {
            page = entityManager.createNativeQuery(GET_TECHNICIAN_MACHINES)
                    .setParameter(1, technicianId).getResultList();
            return page;
        } catch (Exception ex) {
            log.error("Could not get technician machines", ex);
            return null;
        }
    }

    private static final String DELETE_MACHINE_PRODUCT = "delete from machine_product where id = ?";

    @Override
    public void deleteMachineProduct(String id) {
        //delete
        try {
            entityManager.createNativeQuery(DELETE_MACHINE_PRODUCT)
                    .setParameter(1, id).executeUpdate();
        } catch (Exception ex) {
            log.error("Could not delete machine product", ex);
        }
    }

    private static final String GET_MACHINE_PRODUCT = "select mp.id, mp.product_id as productId, mp.machine_id as machineId, "
            + "mp.slot_number as slotNumber, p.name as productName, p.price as productPrice, p.logo_url as productLogoUrl from machine_product mp "
            + "inner join product p on mp.product_id = p.id where mp.id = ?";

    @Override
    public MachineProduct getMachineProduct(String id) {
        try {
            MachineProduct res = (MachineProduct) entityManager.createNativeQuery(GET_MACHINE_PRODUCT)
                    .setParameter(1, id)
                    .getSingleResult();
            return res;
        } catch (Exception ex) {
            log.error("Could not get machine product response", ex);
            return null;
        }
    }

    private static final String ADD_MACHINE_PRODUCT = "insert into machine_product(id, machine_id, product_id, updated_by, available_percentage, slot_number) "
            + "values(?, ?, ?, ?, ?, ?)";

    @Override
    public void addMachineProduct(MachineProductModel mp) {
        try {
            entityManager.createNativeQuery(ADD_MACHINE_PRODUCT)
                    .setParameter(1, mp.getId())
                    .setParameter(2, mp.getMachineId())
                    .setParameter(3, mp.getProductId())
                    .setParameter(4, mp.getUpdatedBy())
                    .setParameter(5, mp.getAvailablePercentage())
                    .setParameter(6, mp.getSlotNumber())
                    .executeUpdate();
        } catch (Exception ex) {
            log.error("Could not add machine product", ex);
        }
    }

    private static final String GET_MACHINE_PRODUCTS = "select mp.id, mp.product_id as productId, mp.machine_id as machineId, "
            + "mp.slot_number as slotNumber, p.name as productName, p.price as productPrice, p.logo_url as productLogoUrl from machine_product mp "
            + "inner join product p on mp.product_id = p.id where mp.machine_id = ? ";

    @Override
    public List<MachineProduct> getMachineProducts(String machineId, int pageNumber) {
        List<MachineProduct> page = null;
        try {
            page = entityManager.createNativeQuery(GET_MACHINE_PRODUCTS)
                    .setParameter(1, machineId)
                    .getResultList();
            return page;
        } catch (Exception ex) {
            log.error("Could not get machine products", ex);
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void addMachine(ShakepointMachine machine) {
        try {
            entityManager.persist(machine);
        } catch (Exception ex) {
            log.error("Could not add machine expender", ex);
        }
    }

    private static final String GET_MACHINES = "select m.id, m.name, m.description, m.location, m.slots, m.technician_id as technicianId, "
            + "(select count(*) from machine_product mp where mp.machine_id = m.id) productsCount "
            + "from machine m ";

    @Override
    public List<MachineDTO> getMachines(int pageNumber) {
        List<MachineDTO> page = null;

        try {
            page = entityManager.createNativeQuery(GET_MACHINES)
                    .getResultList();
            return page;
        } catch (Exception ex) {
            log.error("Could not get machines", ex);
            return page;
        }
    }

    private static final String GET_MACHINE = "select m.id, m.name, m.description, m.location, m.slots, m.technicianId , "
            + "(select count(*) from machine_product mp where mp.machine_id = m.id) productsCount"
            + " from machine m "
            + "where m.id = ?";

    @Override
    public MachineDTO getMachine(String machineId) {
        MachineDTO dto = null;
        Object[] args = {machineId};

        try {
            dto = (MachineDTO) entityManager.createNativeQuery(GET_MACHINE)
                    .setParameter(1, machineId).getSingleResult();
            if (dto == null) {
                //machine has no products
                return null;
            }
            //get products 
            if (dto.getProductsCount() > 0) {
                //get them 
                List<ProductDTO> page = productsRepository.getMachineProductsDTO(machineId, 1);
                dto.setProducts(page);
            }
        } catch (Exception ex) {
            //not found
            log.error("Error", ex);
        }
        return dto;
    }


    private static final String GET_ALERTED_PRODUCTS = "select count(*) from product p inner join "
            + "machine_product mp on mp.product_id = p.id where mp.machine_id = ? and mp.available_percentage < 30";

    @Override
    public int getAlertedproducts(String machineId) {
        try {
            Long count = (Long) entityManager.createNativeQuery(GET_ALERTED_PRODUCTS)
                    .getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get alerted products count", ex);
            return 0;
        }
    }


    //private static final String ADD_TECHNICIAN_MACHINE = "insert into technician_machine(id, asigned_date, machine_id, user_id) values(?, ?, ?, ?)";
    private static final String ADD_TECHNICIAN_MACHINE = "update machine set technician_id = ? where id = ?";

    @Override
    public void addTechnicianMachine(String technicianId, String machineId) {
        try {
            entityManager.createNativeQuery(ADD_TECHNICIAN_MACHINE)
                    .setParameter(1, machineId)
                    .setParameter(2, technicianId)
                    .executeUpdate();
        } catch (Exception ex) {
            log.error("Could not add new technician machine", ex);
        }

    }


    private static final String DELETE_TECHNICIAN_MACHINE = "update machine set technician_id = null where id = ?";

    @Override
    public void deleteTechnicianMachine(String machineId) {
        Object[] args = {
                machineId
        };
        try {
            entityManager.createNativeQuery(DELETE_TECHNICIAN_MACHINE)
                    .setParameter(1, machineId);
        } catch (Exception ex) {
            log.error("Could not delete technician machine", ex);
        }

    }

    private static final String GET_ALERTED_MACHINES_COUNT_BY_TECHNICIAN = "select count(*) from machine m "
            + "inner join machine_product mp on mp.machine_id = m.id "
            + "where mp.available_percentage < 30 and m.technician_id = ?";

    @Override
    public int getAlertedMachines(String technicianId) {

        try {
            Long count = (Long) entityManager.createNativeQuery(GET_ALERTED_MACHINES_COUNT_BY_TECHNICIAN)
                    .setParameter(1, technicianId)
                    .getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get alerted machines by technician: " + ex.getMessage());
            return 0;
        }
    }

    private static final String CONTAINS_PRODUCT = "select count(*) from machine_product where machine_id = ? and product_id = ?";

    @Override
    public boolean containProduct(String machineId, String productId) {
        try {
            Long count = (Long) entityManager.createNativeQuery(CONTAINS_PRODUCT)
                    .setParameter(1, machineId)
                    .setParameter(2, productId)
                    .getSingleResult();
            return count > 0;
        } catch (Exception ex) {
            log.error("Could not get machine_product count", ex);
            return false;
        }
    }

    private static final String UPDATE_MACHINE_PRODUCT_LEVEL = "update machine_product set available_percentage = ? where machine_id = ? and product_id = ?";

    @Override
    public void updateMachineProductLevel(String machineId, String productId, int level) {
        try {
            entityManager.createNativeQuery(UPDATE_MACHINE_PRODUCT_LEVEL)
                    .setParameter(1, level)
                    .setParameter(2, machineId)
                    .setParameter(3, productId);
        } catch (Exception ex) {
            log.error("Could not update availability", ex);
        }

    }

}
