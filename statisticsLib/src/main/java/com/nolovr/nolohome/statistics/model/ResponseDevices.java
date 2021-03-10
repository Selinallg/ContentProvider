package com.nolovr.nolohome.statistics.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 */

public class ResponseDevices implements Serializable{


    private static final long serialVersionUID = 164014288890603751L;
    /**
     * responseCode : 0000
     * vendorInfo : [{"channelCode":"10001","channelKey":"1","channelName":"1058","createdate":1532424676000,"id":"1","sorts":1,"status":0}]
     */

    private String responseCode;
    private List<VendorInfoBean> vendorInfo;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public List<VendorInfoBean> getVendorInfo() {
        return vendorInfo;
    }

    public void setVendorInfo(List<VendorInfoBean> vendorInfo) {
        this.vendorInfo = vendorInfo;
    }

    public static class VendorInfoBean implements Serializable{
        private static final long serialVersionUID = -5656518094151227478L;
        /**
         * channelCode : 10001
         * channelKey : 1
         * channelName : 1058
         * createdate : 1532424676000
         * id : 1
         * sorts : 1
         * status : 0
         */

        private String channelCode;
        private String channelKey;
        private String channelName;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getStrategy() {
            return strategy;
        }

        public void setStrategy(int strategy) {
            this.strategy = strategy;
        }

        private String address;
        private int strategy;


        private long createdate;
        private String id;
        private int sorts;
        private int status;

        public String getChannelCode() {
            return channelCode;
        }

        public void setChannelCode(String channelCode) {
            this.channelCode = channelCode;
        }

        public String getChannelKey() {
            return channelKey;
        }

        public void setChannelKey(String channelKey) {
            this.channelKey = channelKey;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public long getCreatedate() {
            return createdate;
        }

        public void setCreatedate(long createdate) {
            this.createdate = createdate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getSorts() {
            return sorts;
        }

        public void setSorts(int sorts) {
            this.sorts = sorts;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "VendorInfoBean{" +
                    "channelCode='" + channelCode + '\'' +
                    ", channelKey='" + channelKey + '\'' +
                    ", channelName='" + channelName + '\'' +
                    ", address='" + address + '\'' +
                    ", strategy=" + strategy +
                    ", createdate=" + createdate +
                    ", id='" + id + '\'' +
                    ", sorts=" + sorts +
                    ", status=" + status +
                    '}';
        }
    }
}
