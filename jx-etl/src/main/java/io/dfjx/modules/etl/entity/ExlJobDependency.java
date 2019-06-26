package io.dfjx.modules.etl.entity;

public class ExlJobDependency {
    private String ETLSystem        ;
    private String ETLJob           ;
    private String DependencySystem ;
    private String DependencyJob    ;
    private String TriggerJobFlag   ;
    private String EnableFlag;

    public String getEnableFlag() {
        return EnableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        EnableFlag = enableFlag;
    }


    public String getETLSystem() {
        return ETLSystem;
    }

    public void setETLSystem(String ETLSystem) {
        this.ETLSystem = ETLSystem;
    }

    public String getETLJob() {
        return ETLJob;
    }

    public void setETLJob(String ETLJob) {
        this.ETLJob = ETLJob;
    }

    public String getDependencySystem() {
        return DependencySystem;
    }

    public void setDependencySystem(String dependencySystem) {
        DependencySystem = dependencySystem;
    }

    public String getDependencyJob() {
        return DependencyJob;
    }

    public void setDependencyJob(String dependencyJob) {
        DependencyJob = dependencyJob;
    }

    public String getTriggerJobFlag() {
        return TriggerJobFlag;
    }

    public void setTriggerJobFlag(String triggerJobFlag) {
        TriggerJobFlag = triggerJobFlag;
    }
}
