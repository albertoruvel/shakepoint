package com.shakepoint.web.core.repository.impl;

import com.shakepoint.web.core.machine.PurchaseStatus;
import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.PurchaseRepository;
import com.shakepoint.web.data.v1.entity.VendingMachine;
import com.shakepoint.web.data.v1.entity.Purchase;
import com.shakepoint.web.util.ShakeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.*;

public class PurchaseRepositoryImpl implements PurchaseRepository {

    @Autowired
    private MachineRepository machineRepository;

    @PersistenceContext
    private EntityManager em;

    private final Logger log = Logger.getLogger(getClass());

    public PurchaseRepositoryImpl() {
    }

    @Override
    public Purchase getPurchase(String purchaseId) {
        try {
            return (Purchase) em.createQuery("SELECT p FROM Purchase p WHERE p.id = :purchaseId")
                    .setParameter("purchaseId", purchaseId).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get purchase", ex);
            return null;
        }
    }


    private static final String GET_USER_PURCHASES = "select p.id, p.product_id as productid, p.machine_id as machineId, p.total, p.purchase_date as purchaseDate, p.status, pr.name as productName, pr.logo_url as productLogoUrl, "
            + "m.name machineName, qr.cashed, qr.qrCodeUrl from purchase p "
            + "inner join product pr on p.product_id = pr.id "
            + "inner join machine m on m.id = p.machine_id "
            + "inner join purchase_qrcode qr on qr.purchase_id = p.id "
            + "where p.user_id = ?";

    @Override
    public List<Purchase> getUserPurchases(String userId, int pageNumber) {
        try {
            return em.createQuery("SELECT p FROM Purchase p WHERE p.user.id = :id")
                    .setParameter("id", userId).getResultList();
        } catch (Exception ex) {
            log.error("Could not get user purchases", ex);
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void update(Purchase purchase) {
        em.merge(purchase);
    }

    public List<Purchase> getAvailablePurchasesForMachine(String productId, String machineId) {
        try {
            List<Purchase> list = em.createQuery("SELECT p FROM Purchase p WHERE p.machine.id = :machineId AND p.product.id = :productId AND p.status = :status")
                    .setParameter("machineId", machineId)
                    .setParameter("productId", productId)
                    .setParameter("status", PurchaseStatus.PRE_AUTH)
                    .getResultList();
            return list;
        } catch (Exception ex) {
            log.error("Could not get available purchase for machine", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public Integer getProductCountForDateRange(String id, String[] range, String machineId) {
        int counter = 0;
        Long value;

        //TODO: add purchase status to CASHED
        try{
            for (String date : range){
                value = (Long)em.createQuery("SELECT COUNT(p.id) FROM Purchase p WHERE p.machine.id = :machineId AND p.product.id = :productId AND p.purchaseDate LIKE :purchaseDate AND p.status <> :status")
                        .setParameter("machineId", machineId)
                        .setParameter("productId", id)
                        .setParameter("purchaseDate", "%" + date)
                        .setParameter("status", PurchaseStatus.PRE_AUTH)
                        .getSingleResult();
                counter += value;
            }
            return counter;
        }catch(Exception ex){
            log.error("Could get total products for machine and product", ex);
            return 0;
        }

    }

    private static final String GET_TODAY_TOTAL_PURCHASES = "select sum(total) from purchase where purchase_date like '%s";

    @Override
    public double getTodayTotalPurchases() {
        Date date = new Date();
        String dateString = ShakeUtils.SIMPLE_DATE_FORMAT.format(date);
        String sql = String.format(GET_TODAY_TOTAL_PURCHASES, dateString);
        sql += "%'";
        double total = 0;
        try {
            BigDecimal bigDecimal = (BigDecimal) em.createNativeQuery(sql).getSingleResult();
            if (bigDecimal == null) {
                return 0;
            }
            return bigDecimal.doubleValue();
        } catch (Exception ex) {
            log.error("Could not get today total purchases", ex);
            return 0;
        }
    }


    private static final String GET_PER_MACHINE_VALUES = "select avg(p.total) from purchase p inner join machine m on m.id = p.machine_id where p.machine_id = ? and p.purchase_date like '%s'";

    @Override
    public Map<String, List<Double>> getPerMachineValues(String[] range, List<VendingMachine> machines) {
        Map<String, List<Double>> map = new HashMap();
        List<Double> values = null;
        Double avg = 0.0;
        Object[] args = null;
        String format = "";
        for (VendingMachine machine : machines) {
            values = new ArrayList();
            for (String rangeValue : range) {
                format = String.format(GET_PER_MACHINE_VALUES, rangeValue + "%");
                try {
                    avg = (Double) em.createNativeQuery(format).setParameter(1, machine.getId()).getSingleResult();
                } catch (Exception ex) {
                    log.error("Could not get average value", ex);
                    avg = 0.0;
                }
                if (avg == null)
                    avg = 0.0;
                values.add(avg);
            }
            map.put(machine.getName(), values);
        }
        return map;
    }

    private static final String GET_PER_MACHINE_PRODUCT_COUNT_VALUES = "select count(p.total) from purchase p where p.machine_id = ? and p.purchase_date like '%s'";
    @Override
    public Map<String, List<Double>> getPerMachineProductsCountValues(String[] range, List<VendingMachine> machines) {
        Map<String, List<Double>> map = new HashMap();
        List<Double> values = null;
        Double avg = 0.0;
        Object[] args = null;
        String format = "";
        for (VendingMachine machine : machines) {
            values = new ArrayList();
            for (String rangeValue : range) {
                format = String.format(GET_PER_MACHINE_PRODUCT_COUNT_VALUES, rangeValue + "%");
                try {
                    avg = (Double) em.createNativeQuery(format).setParameter(1, machine.getId()).getSingleResult();
                } catch (Exception ex) {
                    log.error("Could not get average value", ex);
                    avg = 0.0;
                }
                if (avg == null)
                    avg = 0.0;
                values.add(avg);
            }
            map.put(machine.getName(), values);
        }
        return map;
    }

    private static final String GET_TOTAL_INCOME = "select sum(p.total) from purchase p where p.purchase_date like '%s'";

    @Override
    public List<Double> getTotalIncomeValues(String[] range) {
        List<Double> values = new ArrayList();
        String format = "";
        Double total = 0.0;
        for (String s : range) {
            format = String.format(GET_TOTAL_INCOME, s + "%");
            total = (Double) em.createNativeQuery(format).getSingleResult();
            if (total == null)
                total = 0.0;
            values.add(total);
        }

        return values;
    }

    private static final String GET_TOTAL_INCOME_PER_MACHINE = "select sum(p.total) from purchase p where p.purchase_date like '%s' and p.machine_id = 1";

    @Override
    public List<Double> getTotalIncomeValues(String[] range, String machineId) {
        List<Double> values = new ArrayList();
        String format = "";
        Double total = 0.0;
        for (String s : range) {
            format = String.format(GET_TOTAL_INCOME_PER_MACHINE, s + "%");
            total = (Double) em.createNativeQuery(format)
                    .setParameter(1, machineId)
                    .getSingleResult();
            if (total == null)
                total = 0.0;
            values.add(total);
        }

        return values;
    }

    @Override
    public List<Purchase> getAuthorizedPurchases(String userId, String machineId, int pageNumber) {
        try {
            return em.createQuery("SELECT p FROM Purchase p WHERE p.user.id = :id AND p.machine.id = :machineId AND p.status = :status")
                    .setParameter("id", userId)
                    .setParameter("machineId", machineId)
                    .setParameter("status", PurchaseStatus.AUTHORIZED)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get active codes", ex);
            return null;
        }
    }
}
