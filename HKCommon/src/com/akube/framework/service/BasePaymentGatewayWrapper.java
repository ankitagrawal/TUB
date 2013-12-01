package com.akube.framework.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import net.sourceforge.stripes.util.CryptoUtil;

/**
 * <p>
 * The BasePaymentGateway wrapper encapsulates the encoding and decoding logic of transaction params to be forwarded to
 * an intermediate page which constructs the request to a specific gateway based on the paramters passed.
 * </p>
 * <p>
 * The rather odd looking generic declaration on this class is called a self-bounding generic type. The declaration
 * allows methods in this class like {@link #addParameter(String, Object)} to return the appropriate type when accessed
 * through subclasses. I.e. {@code ConcretePaymentGateway.addParameter(String,Object...)} will return a reference of
 * type ConcretePaymentGateway instead of BasePaymentGatewayWrapper.
 * </p>
 * <p/> Author: Kani Thanks to Tim Fenell (Stripes) for the generics fu Date: Dec 30, 2008
 */
public abstract class BasePaymentGatewayWrapper<T extends BasePaymentGatewayWrapper<T>> implements PaymentGatewayWrapper {
    public static final String  TRANSACTION_DATA_PARAM = "tdp";

    private Map<String, Object> parameters             = new HashMap<String, Object>();

    /**
     * @param name
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public T addParameter(String name, Object value) {
        this.parameters.put(name, value);
        return (T) this;
    }

    /**
     * <p>
     * Provides access to the Map of parameters that has been accumulated so far for this resolution. The reference
     * returned is to the internal parameters map! As such any changed made to the Map will be reflected in the
     * Resolution, and any subsequent calls to addParameter(s) will be reflected in the Map.
     * </p>
     * 
     * @return the Map of parameters for the resolution
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    public static String encodeTransactionDataParam(Double amount, String gatewayOrderId, Long orderId, String checksum, String paymentMethod, Long billingAddressId) {
        return new TransactionData(amount, gatewayOrderId, orderId, checksum, paymentMethod, billingAddressId).getEncodedString();
    }

    public static TransactionData decodeTransactionDataParam(String encodedString) {
        TransactionData data = new TransactionData();
        data.decode(encodedString);
        return data;
    }
    public static class TransactionData {
        private Double             amount;
        private String             gatewayOrderId;
        private Long               orderId;
        private String               checksum;
        private String             paymentMethod;
        private Long               billingAddressId;

        public static NumberFormat decimalFormat = NumberFormat.getNumberInstance();

        static {
            decimalFormat.setMinimumFractionDigits(2);
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setGroupingUsed(false);
        }

        public TransactionData() {
        }

        public TransactionData(Double amount, String gatewayOrderId, Long orderId, String checksum, String paymentMethod, Long billingAddressId) {
            this.amount = amount;
            this.gatewayOrderId = gatewayOrderId;
            this.orderId = orderId;
            this.checksum = checksum;
            this.paymentMethod = paymentMethod;
            this.billingAddressId = billingAddressId;
        }

        public Double getAmount() {
            return amount;
        }

        public String getGatewayOrderId() {
            return gatewayOrderId;
        }

        public Long getOrderId() {
            return orderId;
        }

         public String getEncodedString(){
             return CryptoUtil.encrypt(decimalFormat.format(amount) + "," + gatewayOrderId + "," + orderId + "," + checksum + "," + paymentMethod + "," + billingAddressId);
         }

        public String getChecksum() {
            return checksum;
        }

        public void setChecksum(String checksum) {
            this.checksum = checksum;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public Long getBillingAddressId() {
            return billingAddressId;
        }

        public void setBillingAddressId(Long billingAddressId) {
            this.billingAddressId = billingAddressId;
        }

        public void decode(String encodedString) {
            try {
                encodedString = URLDecoder.decode(encodedString, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
            encodedString = CryptoUtil.decrypt(encodedString);
            StringTokenizer tokenizer = new StringTokenizer(encodedString, ",");
            amount = Double.parseDouble(tokenizer.nextToken());
            gatewayOrderId = tokenizer.nextToken();
            orderId = Long.parseLong(tokenizer.nextToken());
            checksum = tokenizer.nextToken();
            paymentMethod = tokenizer.nextToken();
            if (paymentMethod.equals("null")){
               paymentMethod = null;
            }
            
            String billingAddress = tokenizer.nextToken();
            if (!billingAddress.equals("null"))
                billingAddressId = Long.parseLong(billingAddress);
        }

    }

}
