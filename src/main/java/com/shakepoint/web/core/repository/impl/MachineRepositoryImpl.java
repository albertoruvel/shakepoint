/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.core.repository.impl;

import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.ProductRepository;
import com.shakepoint.web.data.v1.entity.ShakepointMachine;
import com.shakepoint.web.data.v1.entity.ShakepointMachineProductStatus;
import com.shakepoint.web.data.v1.entity.ShakepointProduct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

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

    //TODO: create dto with this...
    private static final String GET_TECHNICIAN_ALERTED_MACHINES = "select m.id, m.name, m.description, m.location, m.slots, m.technician_id as technicianId, "
            + "(select count(*) from machine_product mp where mp.machine_id = m.id) productsCount "
            + "from machine m inner join machine_product mp on mp.machine_id = m.id "
            + "where m.technician_id = ? and mp.available_percentage < 30 limit and m.has_error = 1";

    @Override
    public List<ShakepointMachine> getAlertedMachines(String technicianId, int pageNumber) {
        List<ShakepointMachine> page = null;
        try {
            page = entityManager.createNativeQuery("HECTO????")
                    .setParameter(1, technicianId).getResultList();
            return page;
        } catch (Exception ex) {
            log.error("Could not get technician machines with errors", ex);
            return null;
        }
    }


    //TODO: create dto for this...
    private static final String GET_TECHNICIAN_FAILED_MACHINES = "select m.id, m.name, m.description, m.location, m.slots, m.technician_id as technicianId, "
            + "(select count(*) from machine_product mp where mp.machine_id = m.id) productsCount "
            + "from machine m where m.technician_id = ? and m.has_error = 1";

    @Override
    public List<ShakepointMachine> getFailedMachines(String technicianId, int pageNumber) {
        List<ShakepointMachine> page = null;
        try {
            page = entityManager.createNativeQuery("...............")
                    .setParameter(1, technicianId).getResultList();
            return page;
        } catch (Exception ex) {
            log.error("Could not get technician machines with with errors: " + ex.getMessage());
        }
        return page;
    }


    @Override
    public List<ShakepointMachine> getTechnicianMachines(String id, int pageNumber) {
        try {
            return entityManager.createQuery("SELECT m FROM Machine m WHERE m.technician.id = :id")
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get technician machines" + ex);
            return null;
        }
    }

    @Override
    public boolean isMachineAlerted(String id) {
        try{
            BigInteger integer = (BigInteger)entityManager.createNativeQuery("SELECT count(*) FROM machine_product WHERE machine_id = ? AND available_percentage < 30")
                    .setParameter(1, id)
                    .getSingleResult();
            return integer.intValue() > 0;
        }catch(Exception ex){
            return false;
        }

    }

    private static final String GET_ALERTED_MACHINES_COUNT = "select count(*) from machine m "
            + "inner join machine_product mp on mp.machine_id = m.id "
            + "where mp.available_percentage < 30";

    @Override
    public int getAlertedMachines() {
        try {
            BigInteger count = (BigInteger) entityManager.createNativeQuery(GET_ALERTED_MACHINES_COUNT)
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
            BigInteger count = (BigInteger) entityManager.createNativeQuery(GET_ACTIVE_MACHINES).getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get active machines count", ex);
            return 0;
        }
    }


    private static final String DELETE_MACHINE_PRODUCT = "delete from machine_product where id = ?";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

    @Override
    public ShakepointMachineProductStatus getMachineProduct(String id) {
        try {
            ShakepointMachineProductStatus res = (ShakepointMachineProductStatus) entityManager.createQuery("SELECT s FROM MachineProductStatus s WHERE s.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();
            return res;
        } catch (Exception ex) {
            log.error("Could not get machine product response", ex);
            return null;
        }
    }

    @Override
    public List<ShakepointMachineProductStatus> getMachineProducts(String machineId) {
        try{
            return entityManager.createQuery("SELECT s FROM MachineProductStatus s WHERE s.machine.id = :id")
                    .setParameter("id", machineId)
                    .getResultList();
        }catch(Exception ex){
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void addMachineProduct(ShakepointMachineProductStatus mp) {
        try {
            entityManager.persist(mp);
        } catch (Exception ex) {
            log.error("Could not add machine product", ex);
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

    @Override
    public List<ShakepointMachine> getMachines(int pageNumber) {
        List<ShakepointMachine> page = null;

        try {
            page = entityManager.createQuery("SELECT m FROM Machine m")
                    .getResultList();
            return page;
        } catch (Exception ex) {
            log.error("Could not get machines", ex);
            return page;
        }
    }

    @Override
    public ShakepointMachine getMachine(String machineId) {
        try {
            return (ShakepointMachine)entityManager.createQuery("SELECT m FROM Machine m WHERE m.id = :id")
                    .setParameter("id", machineId).getSingleResult();
        } catch (Exception ex) {
            //not found
            log.error("Error", ex);
            return null;
        }
    }


    private static final String GET_ALERTED_PRODUCTS = "select count(*) from product p inner join "
            + "machine_product mp on mp.product_id = p.id where mp.machine_id = ? and mp.available_percentage < 30";

    @Override
    public int getAlertedproducts(String machineId) {
        try {
            BigInteger count = (BigInteger) entityManager.createNativeQuery(GET_ALERTED_PRODUCTS)
                    .setParameter(1, machineId)
                    .getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get alerted products count", ex);
            return 0;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateMachine(ShakepointMachine machine) {
        try{
            entityManager.merge(machine);
        }catch(Exception ex){
            log.error("Error trying to update machine", ex);
        }
    }

    private static final String GET_ALERTED_MACHINES_COUNT_BY_TECHNICIAN = "select count(*) from machine m "
            + "inner join machine_product mp on mp.machine_id = m.id "
            + "where mp.available_percentage < 30 and m.technician_id = ?";

    @Override
    public int getAlertedMachines(String technicianId) {

        try {
            BigInteger count = (BigInteger) entityManager.createNativeQuery(GET_ALERTED_MACHINES_COUNT_BY_TECHNICIAN)
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
            BigInteger count = (BigInteger) entityManager.createNativeQuery(CONTAINS_PRODUCT)
                    .setParameter(1, machineId)
                    .setParameter(2, productId)
                    .getSingleResult();
            return count.intValue() > 0;
        } catch (Exception ex) {
            log.error("Could not get machine_product count", ex);
            return false;
        }
    }


}
