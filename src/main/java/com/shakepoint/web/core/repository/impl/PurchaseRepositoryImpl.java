package com.shakepoint.web.core.repository.impl;

import com.shakepoint.web.core.machine.PurchaseStatus;
import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.PurchaseRepository;
import com.shakepoint.web.data.v1.dto.rest.response.PurchaseCodeResponse;
import com.shakepoint.web.data.dto.res.rest.UserPurchaseResponse;
import com.shakepoint.web.data.v1.dto.rest.response.PurchaseQRCode;
import com.shakepoint.web.data.v1.entity.ShakepointMachine;
import com.shakepoint.web.data.v1.entity.ShakepointPurchase;
import com.shakepoint.web.data.v1.entity.ShakepointPurchaseQRCode;
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
    public ShakepointPurchase getPurchase(String purchaseId) {
        try {
            return (ShakepointPurchase)em.createQuery("SELECT p FROM Purchase p WHERE p.id = :purchaseId")
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
    public List<ShakepointPurchase> getUserPurchases(String userId, int pageNumber) {
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
    public void update(ShakepointPurchase purchase) {
        em.merge(purchase);
    }

    private static final String CREATE_QR_CODE = "insert into purchase_qrcode(id, creation_date, purchase_id, image_url, cashed) values(?, ?, ?, ?, ?)";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void createQrCode(ShakepointPurchaseQRCode code) {
        try {
            em.persist(code);
        } catch (Exception ex) {
            log.error("Could not add qr code", ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void confirmPurchase(String purchaseId, String reference) {
        try {
            em.createQuery("UPDATE Purchase p SET p.status = :status, p.reference = :ref WHERE p.id = :id")
                    .setParameter("status", PurchaseStatus.AUTHORIZED.getValue())
                    .setParameter("ref", reference)
                    .setParameter("id", purchaseId)
                    .executeUpdate();
        } catch (Exception ex) {
            log.error("Could not confirm purchase", ex);
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void createPurchase(ShakepointPurchase purchase) {
        try {
            em.persist(purchase);
        } catch (Exception ex) {
            log.error("Could not create purchase: " + ex.getMessage());
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
            if(bigDecimal == null){
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
    public Map<String, List<Double>> getPerMachineValues(String[] range) {
        Map<String, List<Double>> map = new HashMap();
        List<Double> values = null;

        //get all machines
        List<ShakepointMachine> machines = machineRepository.getMachines(1);
        Double avg = 0.0;
        Object[] args = null;
        String format = "";
        for (ShakepointMachine machine : machines) {
            values = new ArrayList();
            for (String rangeValue : range) {
                //get the current range total purchases total average
                args = new Object[]{machine.getId()};
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

    @Override
    public List<ShakepointPurchaseQRCode> getActiveCodes(String userId, String machineId, int pageNumber) {
        try {
            return em.createQuery("SELECT q FROM Code WHERE q.purchase.user.id = :userId AND q.purchase.machine.id = :machineId AND q.purchase.status = 1 AND q.cashed = 1")
                    .setParameter("userId", userId)
                    .setParameter("machineId", machineId)
                    .getResultList() ;
        } catch (Exception ex) {
            log.error("Could not get active codes", ex);
            return null;
        }
    }
}
