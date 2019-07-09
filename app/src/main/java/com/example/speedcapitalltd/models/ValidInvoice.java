package com.example.speedcapitalltd.models;

    public class ValidInvoice {
        private String merchantId;
        private String mechSupCode;
        private String lPONumber;
        private String amount;
        private String maturityDate;
        private String invoiceDate;
        private String recordId;
        private String merchantLogo;

        public ValidInvoice() {
        }
        /**
         * Instantiates a new Valid invoice.
         *
         * @param merchantId   the merchant id
         * @param mechSupCode  the mech sup code
         * @param lPONumber    the lpo number
         * @param amount       the amount
         * @param maturityDate the maturity date
         * @param invoiceDate  the invoice date
         * @param recordId     the record id
         */
        public ValidInvoice(String merchantId, String mechSupCode, String lPONumber, String amount, String maturityDate, String invoiceDate, String recordId, String merchantLogo) {
            this.merchantId = merchantId;
            this.mechSupCode = mechSupCode;
            this.lPONumber = lPONumber;
            this.amount = amount;
            this.maturityDate = maturityDate;
            this.invoiceDate = invoiceDate;
            this.recordId = recordId;
            this.merchantLogo = merchantLogo;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getMechSupCode() {
            return mechSupCode;
        }

        public void setMechSupCode(String mechSupCode) {
            this.mechSupCode = mechSupCode;
        }

        public String getlPONumber() {
            return lPONumber;
        }

        public void setlPONumber(String lPONumber) {
            this.lPONumber = lPONumber;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getMaturityDate() {
            return maturityDate;
        }

        public void setMaturityDate(String maturityDate) {
            this.maturityDate = maturityDate;
        }

        public String getInvoiceDate() {
            return invoiceDate;
        }

        public void setInvoiceDate(String invoiceDate) {
            this.invoiceDate = invoiceDate;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getMerchantLogo() {
            return merchantLogo;
        }

        public void setMerchantLogo(String merchantLogo) {
            this.merchantLogo = merchantLogo;
        }
    }