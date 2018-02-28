package com.shakepoint.web.core.shop;

import com.shakepoint.web.data.v1.dto.rest.response.PaymentDetails;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.io.IOException;

public class PayWorksClientService {

    @Value("${com.shakepoint.web.admin.banorte.user}")
    private String user;

    @Value("${com.shakepoint.web.admin.banorte.password}")
    private String password;

    @Value("${com.shakepoint.web.admin.banorte.merchant}")
    private String merchantId;

    @Value("${com.shakepoint.web.admin.banorte.terminal}")
    private String terminalId;

    @Value("${com.shakepoint.web.admin.banorte.currentMode}")
    private String currentMode;

    @Value("${com.shakepoint.web.admin.banorte.commandTransaction}")
    private String commandTransaction;

    @Autowired
    private RetrofitConfiguration configuration;

    private final Logger log = Logger.getLogger(getClass());

    private PayWorksClient client;

    @PostConstruct
    public void init() {
        log.info(String.format("Creating PayWorks service client for %s", configuration.getServerUrl()));
        client = new Retrofit.Builder()
                .baseUrl(configuration.getServerUrl())
                .build().create(PayWorksClient.class);

        logProperties();
    }

    private void logProperties() {
        log.info(String.format("usuario banorte: %s", user));
        log.info(String.format("Password banorte: %s", password));
        log.info(String.format("Numero de afiliacion: %s", merchantId));
        log.info(String.format("Terminal: %s", terminalId));
        log.info(String.format("Comando: %s", commandTransaction));
    }

    public PaymentDetails authorizePayment(String cardNumber, String cardExpDate, String cvv, double amount){
        log.info("Payment authorization in progress");

        try{
            Response<ResponseBody> response = client.authorizePayment(currentMode, amount, commandTransaction, user, merchantId, password, cardNumber, cardExpDate, cvv, "manual", "ES", terminalId)
                    .execute();
            Headers headers = response.headers();
            if (response.errorBody() != null){
                log.error(response.errorBody().string());
                return null;
            }else{
                log.info("Got headers");
                PaymentDetails details = getDetailsFromResponse(headers);
                return details;
            }

        }catch(IOException ex){
            log.error("Could not complete payment :(", ex);
            return null;
        }
    }

    private PaymentDetails getDetailsFromResponse(Headers headers) {
        final String authCode = headers.get("CODIGO_AUT");
        final String requestDate = headers.get("FECHA_REQ_CTE");
        final String responseDate = headers.get("FECHA_RSP_CTE");
        final String merchantId = headers.get("ID_AFILIACION");
        final String reference = headers.get("REFERENCIA");
        final String payworksResult = headers.get("RESULTADO_PAYW");
        final String message = headers.get("TEXTO");
        log.info(authCode);
        log.info(requestDate);
        log.info(responseDate);
        log.info(merchantId);
        log.info(reference);
        log.info(payworksResult);
        log.info(message);

        return new PaymentDetails(authCode, requestDate, responseDate, merchantId, reference, payworksResult, message);
    }
}
