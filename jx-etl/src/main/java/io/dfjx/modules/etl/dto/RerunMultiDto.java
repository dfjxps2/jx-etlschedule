package io.dfjx.modules.etl.dto;

import java.util.List;

public class RerunMultiDto {

    Integer lastTxDate;

    String rerunjobids;

    public Integer getLastTxDate() {
        return lastTxDate;
    }

    public void setLastTxDate(Integer lastTxDate) {
        this.lastTxDate = lastTxDate;
    }

    public String getRerunjobids() {
        return rerunjobids;
    }

    public void setRerunjobids(String rerunjobids) {
        this.rerunjobids = rerunjobids;
    }
}
