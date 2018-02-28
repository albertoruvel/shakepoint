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
        log.info("Creating PayWorks service client");
        client = new Retrofit.Builder()
                .baseUrl(configuration.getServerUrl())
                .build().create(PayWorksClient.class);
    }

    public PaymentDetails authorizePayment(String cardNumber, String cardExpDate, String cvv, double amount){
        log.info("Authorizing payment");

        try{
            Response<ResponseBody> response = client.authorizePayment(currentMode, amount, commandTransaction, user, merchantId, password, cardNumber, cardExpDate, cvv, "manual", "EN")
                    .execute();
            Headers headers = response.headers();
            PaymentDetails details = getDetailsFromResponse(headers);
            return details;
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

        return new PaymentDetails(authCode, requestDate, responseDate, merchantId, reference, payworksResult, message);
    }
}
