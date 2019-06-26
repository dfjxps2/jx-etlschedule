package io.dfjx.modules.etl.vo;

import com.baomidou.mybatisplus.annotations.TableId;

import java.io.Serializable;
import java.util.Date;

public class EtlJobEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String etlSystem;

    private String etlJob;

    private String etlServer;

    private String description;

//    private String frequency;

    private String jobtype;

    private String enable;

    private String lastStarttime;

    private String lastEndtime;

    private String lastJobstatus;

    private Date lastTxdate;




//    private Integer lastFilecnt;

//    private String lastCubestatus;

//    private String cubeflag;

//    private String checkflag;

//    private String autooff;

//    private String checkcalendar;

//    private String calendarbu;
    
//    private String runningscript;

    private Integer jobsessionid;

//    private Integer expectedrecord;

//    private String checklaststatus;

//    private String timetrigger;

    private Integer priority;


    public String getEtlSystem() {
        return etlSystem;
    }

    public String getEtlJob() {
        return etlJob;
    }

    public String getEtlServer() {
        return etlServer;
    }

    public String getDescription() {
        return description;
    }

    public String getJobtype() {
        return jobtype;
    }

    public String getEnable() {
        return enable;
    }

    public String getLastStarttime() {
        return lastStarttime;
    }

    public String getLastEndtime() {
        return lastEndtime;
    }

    public String getLastJobstatus() {
        return lastJobstatus;
    }

    public Date getLastTxdate() {
        return lastTxdate;
    }

    public Integer getJobsessionid() {
        return jobsessionid;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setEtlJob(String etlJob) {
        this.etlJob = etlJob;
    }

    public void setEtlServer(String etlServer) {
        this.etlServer = etlServer;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public void setLastStarttime(String lastStarttime) {
        this.lastStarttime = lastStarttime;
    }

    public void setLastEndtime(String lastEndtime) {
        this.lastEndtime = lastEndtime;
    }

    public void setLastJobstatus(String lastJobstatus) {
        this.lastJobstatus = lastJobstatus;
    }

    public void setLastTxdate(Date lastTxdate) {
        this.lastTxdate = lastTxdate;
    }

    public void setJobsessionid(Integer jobsessionid) {
        this.jobsessionid = jobsessionid;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
