package com.ibm.verifydemo;

public class QuoteOrPolicy {
        private String type;
        private String quote_name;
        private String quote_desc;
        private int quote_image;

        public QuoteOrPolicy(String type, String quote_name, String quote_desc, int quote_image) {
            this.quote_name = quote_name;
            this.quote_desc = quote_desc;
            this.quote_image = quote_image;
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
        public String getQuote_name() {
            return quote_name;
        }

        public void setQuote_name(String quote_name) {
            this.quote_name = quote_name;
        }

        public String getQuote_desc() {
            return quote_desc;
        }

        public void setQuote_desc(String quote_desc) {
            this.quote_desc = quote_desc;
        }

        public int getQuote_image() {
            return quote_image;
        }

        public void setQuote_image(int quote_image) {
            this.quote_image = quote_image;
        }

}
