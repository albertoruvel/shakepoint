package com.shakepoint.web.core.repository.impl;

import com.shakepoint.web.core.repository.MachineRepository;
import com.shakepoint.web.core.repository.PurchaseRepository;
import com.shakepoint.web.data.dto.res.CpuQRCode;
import com.shakepoint.web.data.dto.res.MachineDTO;
import com.shakepoint.web.data.dto.res.rest.PurchaseCodeResponse;
import com.shakepoint.web.data.dto.res.rest.UserPurchaseResponse;
import com.shakepoint.web.data.entity.Purchase;
import com.shakepoint.web.data.entity.PurchaseQRCode;
import com.shakepoint.web.util.ShakeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public boolean validateQrCode(String qrCodeId) {
        PurchaseQRCode code = getQrCode(qrCodeId);
        Purchase purchase = getPurchase(code.getPurchaseId());
        return !code.isCashed() && purchase.getStatus() == Purchase.PurchaseStatus.PAID;
    }

    private static final String GET_PURCHASE = "select id, machine_id as machineId, product_id as productId, user_id as userId, total, purchase_date as purchaseDate, status from "
            + "purchase where id = ?";

    @Override
    public Purchase getPurchase(String purchaseId) {
        try {
            return (Purchase) em.createNativeQuery(GET_PURCHASE)
                    .setParameter(1, purchaseId).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get purchase", ex);
            return null;
        }
    }

    private static final String GET_QR_CODE_PURCHASE_ID = "select purchase_id from purchase_qrcode where id = ?";

    @Override
    public PurchaseQRCode getQrCode(String qrCode) {
        PurchaseQRCode code = null;
        //get purchase id
        String purchaseId = "";
        try {
            purchaseId = (String) em.createNativeQuery(GET_QR_CODE_PURCHASE_ID)
                    .setParameter(1, qrCode).getSingleResult();
            //get qr code
            code = getCode(purchaseId);
        } catch (Exception ex) {
            log.error("Could not find qr code: " + ex.getMessage());
        }

        return code;
    }

    private static final String GET_USER_PURCHASES = "select p.id, p.product_id as productid, p.machine_id as machineId, p.total, p.purchase_date as purchaseDate, p.status, pr.name as productName, pr.logo_url as productLogoUrl, "
            + "m.name machineName, qr.cashed, qr.qrCodeUrl from purchase p "
            + "inner join product pr on p.product_id = pr.id "
            + "inner join machine m on m.id = p.machine_id "
            + "inner join purchase_qrcode qr on qr.purchase_id = p.id "
            + "where p.user_id = ?";

    @Override
    public List<UserPurchaseResponse> getUserPurchases(String userId, int pageNumber) {
        try {
            return em.createNativeQuery(GET_USER_PURCHASES)
                    .setParameter(1, userId).getResultList();
        } catch (Exception ex) {
            log.error("Could not get user purchases", ex);
            return null;
        }
    }

    private static final String CREATE_QR_CODE = "insert into purchase_qrcode(id, creation_date, purchase_id, image_url, cashed) values(?, ?, ?, ?, ?)";

    @Override
    public void createQrCode(PurchaseQRCode code) {
        Object[] args = {
                code.getId(), code.getCreationDate(), code.getPurchaseId(), code.getImageUrl(), code.isCashed() ? 1 : 0
        };
        try {
            em.createNativeQuery(CREATE_QR_CODE)
                    .setParameter(1, code.getId())
                    .setParameter(2, code.getCreationDate())
                    .setParameter(3, code.getPurchaseId())
                    .setParameter(4, code.getImageUrl())
                    .setParameter(5, code.isCashed() ? 1 : 0).executeUpdate();
        } catch (Exception ex) {
            log.error("Could not add qr code", ex);
        }
    }

    private static final String GET_QR_CODE = "select id, purchase_id as purchaseId, creation_date as creationDate, image_url as imageUrl, cashed from purchase_qrcode where purchase_id = ?";

    @Override
    public PurchaseQRCode getCode(String purchaseId) {
        PurchaseQRCode code = null;
        Object[] args = {purchaseId};
        try {
            return (PurchaseQRCode) em.createNativeQuery(GET_QR_CODE)
                    .setParameter(1, purchaseId).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get qr code", ex);
            return code;
        }
    }

    private static final String CONFIRM_PURCHASE = "update purchase set status = ?, reference = ? where id = ?";

    @Override
    public void confirmPurchase(String purchaseId, String reference) {
        Object[] args = {Purchase.PurchaseStatus.PAID.getValue(), reference, purchaseId};
        try {
            em.createNativeQuery(CONFIRM_PURCHASE)
                    .setParameter(1, Purchase.PurchaseStatus.PAID.getValue())
                    .setParameter(2, reference)
                    .setParameter(3, purchaseId).executeUpdate();
        } catch (Exception ex) {
            log.error("Could not confirm purchase", ex);
        }
    }

    private static final String CREATE_PURCHASE = "insert into purchase(id, machine_id, product_id, user_id, total, purchase_date, status) values(?, ?, ?, ?, ?, ?, ?)";

    @Override
    public void createPurchase(Purchase purchase) {
        try {
            em.createNativeQuery(CREATE_PURCHASE)
                    .setParameter(1, purchase.getId())
                    .setParameter(2, purchase.getMachineId())
                    .setParameter(3, purchase.getProductId())
                    .setParameter(4, purchase.getUserId())
                    .setParameter(5, purchase.getTotal())
                    .setParameter(6, purchase.getPurchaseDate())
                    .setParameter(7, purchase.getStatus().getValue()).executeUpdate();
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
        List<MachineDTO> machines = machineRepository.getMachines(1);
        Double avg = 0.0;
        Object[] args = null;
        String format = "";
        for (MachineDTO machine : machines) {
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
            total = (Double) em.createNativeQuery(GET_TOTAL_INCOME)
                    .setParameter(1, format).getSingleResult();
            if (total == null)
                total = 0.0;
            values.add(total);
        }

        return values;
    }

    private static final String GET_ACTIVE_CODES = "select qr.id, qr.creation_date, qr.purchase_id, p.total, qr.cashed, qr.image_url, pr.name from purchase_qrcode qr "
            + "inner join purchase p on p.id = qr.purchase_id "
            + "inner join product pr on pr.id = p.product_id "
            + "inner join machine m on m.id = p.machine_id "
            + "inner join user u on p.user_id = u.id where u.id = ? and m.id = ? and p.status = 1 and qr.cashed = ?";

    @Override
    public List<PurchaseCodeResponse> getActiveCodes(String userId, String machineId, int pageNumber) {
        List<PurchaseCodeResponse> page = null;
        try {
            return em.createNativeQuery(GET_ACTIVE_CODES)
                    .setParameter(1, userId)
                    .setParameter(2, machineId)
                    .setParameter(3, PurchaseQRCode.QRCodeStatus.AUTHORIZED.getValue())
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get active codes", ex);
            return null;
        }
    }

    /**static class PurchaseRepositoryMappers {
        public static ParameterizedRowMapper<PurchaseQRCode> QR_MAPPER = new ParameterizedRowMapper<PurchaseQRCode>() {

            @Override
            public PurchaseQRCode mapRow(ResultSet rs, int arg1) throws SQLException {
                PurchaseQRCode qr = new PurchaseQRCode();
                qr.setId(rs.getString(ID));
                qr.setCashed(rs.getInt(CASHED) > 0);
                qr.setCreationDate(rs.getString(CREATION_DATE));
                qr.setImageUrl(rs.getString(IMAGE_URL));
                qr.setPurchaseId(rs.getString(PURCHASE_ID));
                return qr;
            }
        };


        public static ParameterizedRowMapper<Purchase> PURCHASE_MAPPER = new ParameterizedRowMapper<Purchase>() {

            @Override
            public Purchase mapRow(ResultSet rs, int arg1) throws SQLException {
                Purchase qr = new Purchase();
                qr.setId(rs.getString(ID));
                qr.setMachineId(rs.getString(MACHINE_ID));
                qr.setProductId(rs.getString(PRODUCT_ID));
                qr.setPurchaseDate(rs.getString(PURCHASE_DATE));
                qr.setStatus(Purchase.PurchaseStatus.get(rs.getInt(STATUS)));
                qr.setTotal(rs.getDouble(TOTAL));
                qr.setUserId(rs.getString(USER_ID));

                return qr;
            }
        };

        public static ParameterizedRowMapper<PurchaseCodeResponse> PURCHASE_QR_MAPPER = new ParameterizedRowMapper<PurchaseCodeResponse>() {

            @Override
            public PurchaseCodeResponse mapRow(ResultSet rs, int arg1) throws SQLException {
                PurchaseCodeResponse qr = new PurchaseCodeResponse();
                qr.setId(rs.getString(ID));
                qr.setCreationDate(rs.getString(CREATION_DATE));
                qr.setImageUrl(rs.getString(IMAGE_URL));
                qr.setPurchaseId(rs.getString(PURCHASE_ID));
                qr.setTotal(rs.getDouble(TOTAL));
                qr.setProductName(rs.getString(NAME));
                return qr;
            }

        };
        public static ParameterizedRowMapper<UserPurchaseResponse> USER_PURCHASE_MAPPER = new ParameterizedRowMapper<UserPurchaseResponse>() {

            @Override
            public UserPurchaseResponse mapRow(ResultSet rs, int arg1) throws SQLException {
                UserPurchaseResponse qr = new UserPurchaseResponse();
                qr.setPurchaseId(rs.getString(ID));
                qr.setCashed(rs.getInt(CASHED) > 0);
                qr.setMachineId(rs.getString(MACHINE_ID));
                qr.setMachineName(rs.getString(MACHINE_NAME));
                qr.setProductid(rs.getString(PRODUCT_ID));
                qr.setProductLogoUrl(rs.getString(LOGO_URL));
                qr.setProductName(rs.getString(PRODUCT_NAME));
                qr.setPurchaseDate(rs.getString(PURCHASE_DATE));
                qr.setStatus(rs.getInt(STATUS));
                qr.setTotal(rs.getDouble(TOTAL));
                qr.setQrCodeUrl(rs.getString(IMAGE_URL));
                return qr;
            }
        };

        public static ParameterizedRowMapper<CpuQRCode> QR_CODE_MAPPER = new ParameterizedRowMapper<CpuQRCode>() {

            @Override
            public CpuQRCode mapRow(ResultSet rs, int rowNum) throws SQLException {
                CpuQRCode code = new CpuQRCode();
                code.setCodeId(rs.getString(CODE_ID));
                code.setStatus(Purchase.PurchaseStatus.get(rs.getInt(STATUS)));
                code.setCodeUrl(rs.getString(IMAGE_URL));
                code.setProductId(rs.getString(PRODUCT_ID));
                code.setPurchaseDate(rs.getString(PURCHASE_DATE));
                code.setPurchaseId(rs.getString(PURCHASE_ID));
                return code;
            }
        };
    }**/

    private static final String UPDATE_QR_STATUS = "update purchase_qrcode set cashed = ? where id = ?";

    @Override
    public void updateQRStatus(String purchaseId, PurchaseQRCode.QRCodeStatus cashed) {
        try {
            em.createNativeQuery(UPDATE_QR_STATUS)
                    .setParameter(1, cashed.getValue())
                    .setParameter(2, purchaseId)
                    .executeUpdate();
        } catch (Exception ex) {
            log.error("Could not update qr code status", ex);
        }
    }


    private static final String GET_QR_CODES = "select c.id codeId, c.image_url as codeUrl, p.status, "
            + "p.id purchase_id as purchaseId, p.purchase_date as purchaseDate, p.product_id as productId "
            + "from purchase_qrcode c "
            + "inner join purchase p on p.id = c.purchase_id "
            + "where p.status = ? and p.machine_id = ? and c.cashed = ?";

    @Override
    public List<CpuQRCode> getQrCodes(PurchaseQRCode.QRCodeStatus status, String machineId) {
        List<Map<String, Object>> maps = null;
        List<CpuQRCode> codes = new ArrayList();
        try {
            return em.createNativeQuery(GET_QR_CODES)
                    .setParameter(1, status.getValue())
                    .setParameter(2, machineId)
                    .setParameter(3, PurchaseQRCode.QRCodeStatus.AUTHORIZED.getValue())
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get codes", ex);
            return codes;
        }
    }
}
