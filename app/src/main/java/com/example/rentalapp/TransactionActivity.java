package com.example.rentalapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.midtrans.sdk.uikit.api.model.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.uikit.external.UiKitApi;
import com.midtrans.sdk.corekit.models.ItemDetails;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    private static final String CLIENT_KEY = "SB-Mid-client-nzpNijd88vdo-44";
    private static final String BASE_URL = "https://api.sandbox.midtrans.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        WebView webView;
        webView = findViewById(R.id.paymentWebView);
        webView.loadUrl("");

        initMidtransSDK(this);

        Button payButton = findViewById(R.id.pay_button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startPayment();
                webView.loadUrl("");
            }
        });
    }

    private void initMidtransSDK(Context context) {

        SdkUIFlowBuilder.init()
                .setContext(context)
                .setClientKey(CLIENT_KEY)
                .setMerchantBaseUrl(BASE_URL)
                .enableLog(true)
                .setColorTheme(new com.midtrans.sdk.corekit.core.themes.CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
                .buildSDK();

        if (MidtransSDK.getInstance() == null) {
            SdkUIFlowBuilder.init()
                    .setContext(context)
                    .setClientKey(CLIENT_KEY)
                    .setMerchantBaseUrl(BASE_URL)
                    .buildSDK();
        }
    }

    private void startPayment() {
        // Create transaction request
        TransactionRequest transactionRequest = new TransactionRequest("order-id-12345", 100000);

        // Set up item details
        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
        itemDetails.add(new ItemDetails("item-id-1", 1000.00, 1, "Sample Item"));
        transactionRequest.setItemDetails(itemDetails);

        // Set up customer details
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setFirstName("John");
        customerDetails.setLastName("Doe");
        customerDetails.setEmail("john.doe@example.com");
        customerDetails.setPhone("08123456789");
        transactionRequest.setCustomerDetails(customerDetails);

        // Set transaction request in SDK
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);

        // Start payment UI flow
        MidtransSDK.getInstance().startPaymentUiFlow(this);
    }
}
