package io.dfjx.modules.etl.entity;

public class ExlJobConfig {
    private String EtlSystem  ;
    private String EtlJob     ;
    private String EtlServer  ;
    private String Description ;
    private String Frequency   ;
    private String ScriptFile  ;
    private String ScriptID    ;
    private String ScriptType  ;
    private String EnableFlag;

    public String getEnableFlag() {
        return EnableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        EnableFlag = enableFlag;
    }




    public String getEtlSystem() {
        return EtlSystem;
    }

    public void setEtlSystem(String etlSystem) {
        EtlSystem = etlSystem;
    }

    public String getEtlJob() {
        return EtlJob;
    }

    public void setEtlJob(String etlJob) {
        EtlJob = etlJob;
    }

    public String getEtlServer() {
        return EtlServer;
    }

    public void setEtlServer(String etlServer) {
        EtlServer = etlServer;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getFrequency() {
        return Frequency;
    }

    public void setFrequency(String frequency) {
        Frequency = frequency;
    }

    public String getScriptFile() {
        return ScriptFile;
    }

    public void setScriptFile(String scriptFile) {
        ScriptFile = scriptFile;
    }

    public String getScriptID() {
        return ScriptID;
    }

    public void setScriptID(String scriptID) {
        ScriptID = scriptID;
    }

    public String getScriptType() {
        return ScriptType;
    }

    public void setScriptType(String scriptType) {
        ScriptType = scriptType;
    }
}
