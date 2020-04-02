package io.dfjx.modules.etl.dto;

import java.util.List;

public class RerunMultiDto {

    String lastTxDate;

    String rerunjobids;

    public String getLastTxDate() {
        return lastTxDate;
    }

    public void setLastTxDate(String lastTxDate) {
        this.lastTxDate = lastTxDate;
    }

    public String getRerunjobids() {
        return rerunjobids;
    }

    public void setRerunjobids(String rerunjobids) {
        this.rerunjobids = rerunjobids;
    }
}
