/*
Navicat MySQL Data Transfer

Source Server         : 政务资源中心-mysql
Source Server Version : 50627
Source Host           : 172.26.52.179:3506
Source Database       : etldb

Target Server Type    : MYSQL
Target Server Version : 50627
File Encoding         : 65001

Date: 2019-04-01 10:55:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `datacalendar`
-- ----------------------------
DROP TABLE IF EXISTS `datacalendar`;
CREATE TABLE `datacalendar` (
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `CalendarYear` int(11) NOT NULL,
  `SeqNum` int(11) NOT NULL,
  `CalendarMonth` int(11) NOT NULL,
  `CalendarDay` int(11) NOT NULL,
  `CheckFlag` char(1) DEFAULT NULL,
  PRIMARY KEY (`ETL_System`,`ETL_Job`,`CalendarYear`,`CalendarMonth`,`CalendarDay`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of datacalendar
-- ----------------------------

-- ----------------------------
-- Table structure for `datacalendaryear`
-- ----------------------------
DROP TABLE IF EXISTS `datacalendaryear`;
CREATE TABLE `datacalendaryear` (
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `CalendarYear` int(11) NOT NULL,
  PRIMARY KEY (`ETL_System`,`ETL_Job`,`CalendarYear`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of datacalendaryear
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_event`
-- ----------------------------
DROP TABLE IF EXISTS `etl_event`;
CREATE TABLE `etl_event` (
  `EventID` varchar(30) NOT NULL,
  `ETL_Server` varchar(10) NOT NULL,
  `EventStatus` char(1) NOT NULL,
  `Severity` char(1) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `LogTime` char(19) NOT NULL,
  `CloseTime` char(19) DEFAULT NULL,
  PRIMARY KEY (`EventID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_event
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_bak`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_bak`;
CREATE TABLE `etl_job_bak` (
  `ID` int(32) NOT NULL AUTO_INCREMENT,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `ETL_Server` varchar(10) DEFAULT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Frequency` varchar(30) DEFAULT NULL,
  `JobType` char(1) DEFAULT NULL,
  `Enable` char(1) DEFAULT NULL,
  `Last_StartTime` char(19) DEFAULT NULL,
  `Last_EndTime` char(19) DEFAULT NULL,
  `Last_JobStatus` varchar(20) DEFAULT NULL,
  `Last_TXDate` date DEFAULT NULL,
  `Last_FileCnt` int(11) DEFAULT NULL,
  `Last_CubeStatus` char(20) DEFAULT NULL,
  `CubeFlag` char(1) DEFAULT NULL,
  `CheckFlag` char(1) DEFAULT NULL,
  `AutoOff` char(1) DEFAULT NULL,
  `CheckCalendar` char(1) DEFAULT NULL,
  `CalendarBU` varchar(15) DEFAULT NULL,
  `RunningScript` varchar(50) DEFAULT NULL,
  `JobSessionID` int(11) DEFAULT NULL,
  `ExpectedRecord` int(11) DEFAULT NULL,
  `CheckLastStatus` char(1) DEFAULT NULL,
  `TimeTrigger` char(1) DEFAULT NULL,
  `Priority` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ETL_System` (`ETL_System`,`ETL_Job`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_bak
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_concurrent`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_concurrent`;
CREATE TABLE `etl_job_concurrent` (
  `ConcurrentName` varchar(50) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Concurrent` int(11) NOT NULL,
  PRIMARY KEY (`ConcurrentName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_concurrent
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_concurrentchild`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_concurrentchild`;
CREATE TABLE `etl_job_concurrentchild` (
  `ConcurrentName` varchar(50) NOT NULL,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Enable` char(1) NOT NULL,
  PRIMARY KEY (`ConcurrentName`,`ETL_System`,`ETL_Job`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_concurrentchild
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_dependency`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_dependency`;
CREATE TABLE `etl_job_dependency` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `Dependency_System` char(3) NOT NULL,
  `Dependency_Job` varchar(80) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Enable` char(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ETL_System` (`ETL_System`,`ETL_Job`,`Dependency_System`,`Dependency_Job`)
) ENGINE=InnoDB AUTO_INCREMENT=198 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_dependency
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_dependency_bak`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_dependency_bak`;
CREATE TABLE `etl_job_dependency_bak` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `Dependency_System` char(3) NOT NULL,
  `Dependency_Job` varchar(80) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Enable` char(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ETL_System` (`ETL_System`,`ETL_Job`,`Dependency_System`,`Dependency_Job`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_dependency_bak
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_group`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_group`;
CREATE TABLE `etl_job_group` (
  `GroupName` varchar(50) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `ETL_System` char(3) DEFAULT NULL,
  `ETL_Job` varchar(80) DEFAULT NULL,
  `AutoOnChild` char(1) DEFAULT NULL,
  PRIMARY KEY (`GroupName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_group
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_groupchild`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_groupchild`;
CREATE TABLE `etl_job_groupchild` (
  `GroupName` varchar(50) NOT NULL,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Enable` char(1) DEFAULT NULL,
  `CheckFlag` char(1) DEFAULT NULL,
  `TXDate` date DEFAULT NULL,
  `TurnOnFlag` char(1) DEFAULT NULL,
  PRIMARY KEY (`GroupName`,`ETL_System`,`ETL_Job`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_groupchild
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_log`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_log`;
CREATE TABLE `etl_job_log` (
  `ID` int(32) NOT NULL AUTO_INCREMENT,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `JobSessionID` int(11) NOT NULL,
  `JobStepID` char(4) NOT NULL,
  `ScriptFile` varchar(50) NOT NULL,
  `TXDate` date NOT NULL,
  `StartTime` char(19) DEFAULT NULL,
  `EndTime` char(19) DEFAULT NULL,
  `ReturnCode` int(11) DEFAULT NULL,
  `Seconds` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ETL_System` (`ETL_System`,`ETL_Job`,`JobSessionID`,`JobStepID`)
) ENGINE=InnoDB AUTO_INCREMENT=634 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_log_bak`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_log_bak`;
CREATE TABLE `etl_job_log_bak` (
  `ID` int(32) NOT NULL AUTO_INCREMENT,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `JobSessionID` int(11) NOT NULL,
  `JobStepID` char(4) NOT NULL,
  `ScriptFile` varchar(50) NOT NULL,
  `TXDate` date NOT NULL,
  `StartTime` char(19) DEFAULT NULL,
  `EndTime` char(19) DEFAULT NULL,
  `ReturnCode` int(11) DEFAULT NULL,
  `Seconds` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ETL_System` (`ETL_System`,`ETL_Job`,`JobSessionID`,`JobStepID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_log_bak
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_queue`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_queue`;
CREATE TABLE `etl_job_queue` (
  `ETL_Server` varchar(10) NOT NULL,
  `SeqID` int(11) DEFAULT NULL,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `TXDate` date NOT NULL,
  `RequestTime` varchar(19) NOT NULL,
  PRIMARY KEY (`ETL_Server`,`ETL_System`,`ETL_Job`,`RequestTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_queue
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_source`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_source`;
CREATE TABLE `etl_job_source` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Source` varchar(80) NOT NULL,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `Conv_File_Head` varchar(80) NOT NULL,
  `AutoFilter` char(1) DEFAULT NULL,
  `Alert` char(1) DEFAULT NULL,
  `BeforeHour` int(11) DEFAULT NULL,
  `BeforeMin` int(11) DEFAULT NULL,
  `OffsetDay` int(11) DEFAULT NULL,
  `LastCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Source` (`Source`)
) ENGINE=InnoDB AUTO_INCREMENT=393 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_source
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_source_bak`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_source_bak`;
CREATE TABLE `etl_job_source_bak` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Source` varchar(80) NOT NULL,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `Conv_File_Head` varchar(80) NOT NULL,
  `AutoFilter` char(1) DEFAULT NULL,
  `Alert` char(1) DEFAULT NULL,
  `BeforeHour` int(11) DEFAULT NULL,
  `BeforeMin` int(11) DEFAULT NULL,
  `OffsetDay` int(11) DEFAULT NULL,
  `LastCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Source` (`Source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_source_bak
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_status`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_status`;
CREATE TABLE `etl_job_status` (
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `JobSessionID` int(11) NOT NULL,
  `TXDate` date NOT NULL,
  `StartTime` char(19) DEFAULT NULL,
  `EndTime` char(19) DEFAULT NULL,
  `JobStatus` varchar(20) DEFAULT NULL,
  `FileCnt` int(11) DEFAULT NULL,
  `CubeStatus` varchar(20) DEFAULT NULL,
  `ExpectedRecord` int(11) DEFAULT NULL,
  PRIMARY KEY (`ETL_System`,`ETL_Job`,`JobSessionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_status
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_status_type`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_status_type`;
CREATE TABLE `etl_job_status_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STATUS` varchar(50) NOT NULL,
  `STATUS_DESC` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `STATUS` (`STATUS`),
  UNIQUE KEY `STATUS_DESC` (`STATUS_DESC`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_status_type
-- ----------------------------
INSERT INTO `etl_job_status_type` VALUES ('1', 'Ready', '准备执行(Ready)');
INSERT INTO `etl_job_status_type` VALUES ('2', 'Pending', '即将执行(Pending)');
INSERT INTO `etl_job_status_type` VALUES ('3', 'Running', '正在执行(Running)');
INSERT INTO `etl_job_status_type` VALUES ('4', 'Done', '执行成功(Done)');
INSERT INTO `etl_job_status_type` VALUES ('5', 'Failed', '执行失败(Failed)');

-- ----------------------------
-- Table structure for `etl_job_step`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_step`;
CREATE TABLE `etl_job_step` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `JobStepID` char(4) NOT NULL,
  `ScriptID` int(11) NOT NULL,
  `ScriptFile` varchar(100) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Enable` char(1) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ETL_System` (`ETL_System`,`ETL_Job`,`JobStepID`)
) ENGINE=InnoDB AUTO_INCREMENT=393 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_step
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_step_bak`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_step_bak`;
CREATE TABLE `etl_job_step_bak` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `JobStepID` char(4) NOT NULL,
  `ScriptID` int(11) NOT NULL,
  `ScriptFile` varchar(100) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Enable` char(1) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ETL_System` (`ETL_System`,`ETL_Job`,`JobStepID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_step_bak
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_stream`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_stream`;
CREATE TABLE `etl_job_stream` (
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `Stream_System` char(3) NOT NULL,
  `Stream_Job` varchar(80) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Enable` char(1) DEFAULT NULL,
  PRIMARY KEY (`ETL_System`,`ETL_Job`,`Stream_System`,`Stream_Job`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_stream
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_timewindow`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_timewindow`;
CREATE TABLE `etl_job_timewindow` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `Allow` char(1) DEFAULT NULL,
  `BeginHour` int(11) DEFAULT NULL,
  `EndHour` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ETL_System` (`ETL_System`,`ETL_Job`)
) ENGINE=InnoDB AUTO_INCREMENT=183 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_timewindow
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_timewindow_bak`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_timewindow_bak`;
CREATE TABLE `etl_job_timewindow_bak` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `Allow` char(1) DEFAULT NULL,
  `BeginHour` int(11) DEFAULT NULL,
  `EndHour` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ETL_System` (`ETL_System`,`ETL_Job`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_timewindow_bak
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_job_trace`
-- ----------------------------
DROP TABLE IF EXISTS `etl_job_trace`;
CREATE TABLE `etl_job_trace` (
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `TXDate` date NOT NULL,
  `JobStatus` varchar(20) DEFAULT NULL,
  `StartTime` char(19) DEFAULT NULL,
  `EndTime` char(19) DEFAULT NULL,
  PRIMARY KEY (`ETL_System`,`ETL_Job`,`TXDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_job_trace
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_received_file`
-- ----------------------------
DROP TABLE IF EXISTS `etl_received_file`;
CREATE TABLE `etl_received_file` (
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `ReceivedFile` varchar(100) NOT NULL,
  `JobSessionID` int(11) NOT NULL,
  `FileSize` decimal(18,0) DEFAULT NULL,
  `ExpectedRecord` int(11) DEFAULT NULL,
  `ArrivalTime` char(19) DEFAULT NULL,
  `ReceivedTime` char(19) DEFAULT NULL,
  `Location` varchar(128) DEFAULT NULL,
  `Status` char(1) DEFAULT NULL,
  PRIMARY KEY (`ETL_System`,`ETL_Job`,`ReceivedFile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_received_file
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_relatedjob`
-- ----------------------------
DROP TABLE IF EXISTS `etl_relatedjob`;
CREATE TABLE `etl_relatedjob` (
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `RelatedSystem` char(3) NOT NULL,
  `RelatedJob` varchar(80) NOT NULL,
  `CheckMode` char(1) DEFAULT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Enable` char(1) DEFAULT NULL,
  PRIMARY KEY (`ETL_System`,`ETL_Job`,`RelatedSystem`,`RelatedJob`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_relatedjob
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_script`
-- ----------------------------
DROP TABLE IF EXISTS `etl_script`;
CREATE TABLE `etl_script` (
  `ScriptID` int(11) NOT NULL AUTO_INCREMENT,
  `ETL_Server` varchar(10) DEFAULT NULL,
  `FilePath` varchar(50) NOT NULL,
  `FileName` varchar(50) NOT NULL,
  `ScriptType` varchar(10) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Username` varchar(20) DEFAULT NULL,
  `ShareFlag` char(1) DEFAULT NULL,
  `Enable` char(1) DEFAULT NULL,
  PRIMARY KEY (`ScriptID`),
  UNIQUE KEY `ETL_Server` (`ETL_Server`,`FilePath`,`FileName`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_script
-- ----------------------------

INSERT INTO `etl_script` VALUES ('91', 's149', '/app', 'csum_executor_test.py', 'py', 'hql运行公共作业python模板', 'admin', '1', '1');
INSERT INTO `etl_script` VALUES ('92', 's149', '/app', 'exechive2.sh', 'sh', 'hql运行公共作业shell模板', 'admin', '1', '1');
INSERT INTO `etl_script` VALUES ('93', 's149', '/app', 'trigger_python_test.py', 'py', 'python触发空作业模板', 'admin', '1', '1');
INSERT INTO `etl_script` VALUES ('94', 's149', '/app', 'dqc-run.sh', 'sh', '数据质量检查程序', 'admin', '1', '1');




-- ----------------------------
-- Table structure for `etl_script_log`
-- ----------------------------
DROP TABLE IF EXISTS `etl_script_log`;
CREATE TABLE `etl_script_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ScriptID` int(11) DEFAULT NULL,
  `ScriptVersion` varchar(20) DEFAULT NULL,
  `ETL_Server` varchar(10) DEFAULT NULL,
  `FilePath` varchar(50) DEFAULT NULL,
  `FileName` varchar(50) DEFAULT NULL,
  `ScriptType` varchar(10) DEFAULT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Actions` varchar(20) DEFAULT NULL,
  `Message` varchar(200) DEFAULT NULL,
  `Author` varchar(20) DEFAULT NULL,
  `LogDate` datetime DEFAULT NULL,
  `VersionFile` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_script_log
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_server`
-- ----------------------------
DROP TABLE IF EXISTS `etl_server`;
CREATE TABLE `etl_server` (
  `ID` int(32) NOT NULL AUTO_INCREMENT,
  `ETL_Server` varchar(10) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `IPAddress` varchar(15) DEFAULT NULL,
  `AgentPort` int(11) DEFAULT NULL,
  `LiveCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ETL_Server` (`ETL_Server`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_server
-- ----------------------------
INSERT INTO `etl_server` VALUES ('1', 's149', 'etl服务器', '192.166.162.149', '6346', '1');



-- ----------------------------
-- Table structure for `etl_sys`
-- ----------------------------
DROP TABLE IF EXISTS `etl_sys`;
CREATE TABLE `etl_sys` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ETL_System` char(3) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `DataKeepPeriod` int(11) DEFAULT NULL,
  `LogKeepPeriod` int(11) DEFAULT NULL,
  `RecordKeepPeriod` int(11) DEFAULT NULL,
  `Priority` int(11) DEFAULT NULL,
  `Concurrent` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ETL_System` (`ETL_System`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_sys
-- ----------------------------
INSERT INTO `etl_sys` VALUES ('1', 'SUB', '主题库任务', '3', '30', '30', '10', '3');
INSERT INTO `etl_sys` VALUES ('2', 'CSM', '数据集市任务', '3', '30', '30', '10', '3');
INSERT INTO `etl_sys` VALUES ('3', 'HIS', '历史库任务', '3', '30', '30', '10', '3');
INSERT INTO `etl_sys` VALUES ('4', 'BAC', '基础库任务', '3', '30', '30', '10', '3');
INSERT INTO `etl_sys` VALUES ('5', 'INX', '指标库任务', '3', '30', '30', '10', '3');
INSERT INTO `etl_sys` VALUES ('6', 'CLN', '清除数据', '3', '30', '30', '10', '3');
INSERT INTO `etl_sys` VALUES ('11', 'TST', 'for test', '30', '30', '30', '0', '10');
INSERT INTO `etl_sys` VALUES ('12', 'MAT', '集市库', null, '10', null, '10', '3');
INSERT INTO `etl_sys` VALUES ('13', 'TMP', '临时数据区', null, '10', null, '10', '10');
INSERT INTO `etl_sys` VALUES ('14', 'DQC', '数据质量检查区', null, '10', null, '10', '10');



-- ----------------------------
-- Table structure for `etl_timetrigger`
-- ----------------------------
DROP TABLE IF EXISTS `etl_timetrigger`;
CREATE TABLE `etl_timetrigger` (
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `TriggerType` char(1) DEFAULT NULL,
  `StartHour` int(11) DEFAULT NULL,
  `StartMin` int(11) DEFAULT NULL,
  `OffsetDay` int(11) DEFAULT NULL,
  `LastRunDate` int(11) DEFAULT NULL,
  `LastRunTime` int(11) DEFAULT NULL,
  PRIMARY KEY (`ETL_System`,`ETL_Job`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_timetrigger
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_timetrigger_calendar`
-- ----------------------------
DROP TABLE IF EXISTS `etl_timetrigger_calendar`;
CREATE TABLE `etl_timetrigger_calendar` (
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `Seq` int(11) NOT NULL,
  `YearNum` int(11) DEFAULT NULL,
  `MonthNum` int(11) DEFAULT NULL,
  `DayNum` int(11) DEFAULT NULL,
  PRIMARY KEY (`ETL_System`,`ETL_Job`,`Seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_timetrigger_calendar
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_timetrigger_monthly`
-- ----------------------------
DROP TABLE IF EXISTS `etl_timetrigger_monthly`;
CREATE TABLE `etl_timetrigger_monthly` (
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `Timing` char(31) NOT NULL,
  `EndOfMonth` char(1) NOT NULL,
  PRIMARY KEY (`ETL_System`,`ETL_Job`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_timetrigger_monthly
-- ----------------------------

-- ----------------------------
-- Table structure for `etl_timetrigger_weekly`
-- ----------------------------
DROP TABLE IF EXISTS `etl_timetrigger_weekly`;
CREATE TABLE `etl_timetrigger_weekly` (
  `ETL_System` char(3) NOT NULL,
  `ETL_Job` varchar(80) NOT NULL,
  `Timing` char(7) NOT NULL,
  PRIMARY KEY (`ETL_System`,`ETL_Job`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etl_timetrigger_weekly
-- ----------------------------

-- ----------------------------
-- Table structure for `qrtz_blob_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for `qrtz_calendars`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for `qrtz_cron_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
INSERT INTO `qrtz_cron_triggers` VALUES ('RenrenScheduler', 'TASK_1', 'DEFAULT', '0 0/30 * * * ?', 'Asia/Shanghai');
INSERT INTO `qrtz_cron_triggers` VALUES ('RenrenScheduler', 'TASK_2', 'DEFAULT', '0 0/30 * * * ?', 'Asia/Shanghai');

-- ----------------------------
-- Table structure for `qrtz_fired_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for `qrtz_job_details`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
INSERT INTO `qrtz_job_details` VALUES ('RenrenScheduler', 'TASK_1', 'DEFAULT', null, 'io.dfjx.modules.job.utils.ScheduleJob', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372002C696F2E64666A782E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200084C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C000E63726F6E45787072657373696F6E71007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C000A6D6574686F644E616D6571007E00094C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B7870740008746573745461736B7372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000158BAF593307874000E3020302F3330202A202A202A203F7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B020000787000000000000000017400047465737474000464666A7874000FE69C89E58F82E695B0E6B58BE8AF95737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0013000000007800);
INSERT INTO `qrtz_job_details` VALUES ('RenrenScheduler', 'TASK_2', 'DEFAULT', null, 'io.dfjx.modules.job.utils.ScheduleJob', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372002C696F2E64666A782E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200084C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C000E63726F6E45787072657373696F6E71007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C000A6D6574686F644E616D6571007E00094C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B7870740008746573745461736B7372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000158C377C4607874000E3020302F3330202A202A202A203F7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B0200007870000000000000000274000574657374327074000FE697A0E58F82E695B0E6B58BE8AF95737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0013000000017800);

-- ----------------------------
-- Table structure for `qrtz_locks`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('RenrenScheduler', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('RenrenScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for `qrtz_paused_trigger_grps`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for `qrtz_scheduler_state`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
INSERT INTO `qrtz_scheduler_state` VALUES ('RenrenScheduler', 's1491553668692310', '1554087321379', '15000');

-- ----------------------------
-- Table structure for `qrtz_simple_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for `qrtz_simprop_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for `qrtz_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
INSERT INTO `qrtz_triggers` VALUES ('RenrenScheduler', 'TASK_1', 'DEFAULT', 'TASK_1', 'DEFAULT', null, '1554087600000', '1554085800000', '5', 'WAITING', 'CRON', '1532351965000', '0', null, '2', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372002C696F2E64666A782E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200084C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C000E63726F6E45787072657373696F6E71007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C000A6D6574686F644E616D6571007E00094C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B7870740008746573745461736B7372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000158BAF593307874000E3020302F3330202A202A202A203F7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B020000787000000000000000017400047465737474000464666A7874000FE69C89E58F82E695B0E6B58BE8AF95737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0013000000007800);
INSERT INTO `qrtz_triggers` VALUES ('RenrenScheduler', 'TASK_2', 'DEFAULT', 'TASK_2', 'DEFAULT', null, '1532352600000', '-1', '5', 'PAUSED', 'CRON', '1532351965000', '0', null, '2', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000D4A4F425F504152414D5F4B45597372002C696F2E64666A782E6D6F64756C65732E6A6F622E656E746974792E5363686564756C654A6F62456E7469747900000000000000010200084C00086265616E4E616D657400124C6A6176612F6C616E672F537472696E673B4C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C000E63726F6E45787072657373696F6E71007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C000A6D6574686F644E616D6571007E00094C0006706172616D7371007E00094C000672656D61726B71007E00094C00067374617475737400134C6A6176612F6C616E672F496E74656765723B7870740008746573745461736B7372000E6A6176612E7574696C2E44617465686A81014B5974190300007870770800000158C377C4607874000E3020302F3330202A202A202A203F7372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B0200007870000000000000000274000574657374327074000FE697A0E58F82E695B0E6B58BE8AF95737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C75657871007E0013000000017800);

-- ----------------------------
-- Table structure for `r_cluster`
-- ----------------------------
DROP TABLE IF EXISTS `r_cluster`;
CREATE TABLE `r_cluster` (
  `ID_CLUSTER` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `BASE_PORT` varchar(255) DEFAULT NULL,
  `SOCKETS_BUFFER_SIZE` varchar(255) DEFAULT NULL,
  `SOCKETS_FLUSH_INTERVAL` varchar(255) DEFAULT NULL,
  `SOCKETS_COMPRESSED` tinyint(1) DEFAULT NULL,
  `DYNAMIC_CLUSTER` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_CLUSTER`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_cluster
-- ----------------------------

-- ----------------------------
-- Table structure for `r_cluster_slave`
-- ----------------------------
DROP TABLE IF EXISTS `r_cluster_slave`;
CREATE TABLE `r_cluster_slave` (
  `ID_CLUSTER_SLAVE` bigint(20) NOT NULL,
  `ID_CLUSTER` int(11) DEFAULT NULL,
  `ID_SLAVE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_CLUSTER_SLAVE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_cluster_slave
-- ----------------------------

-- ----------------------------
-- Table structure for `r_condition`
-- ----------------------------
DROP TABLE IF EXISTS `r_condition`;
CREATE TABLE `r_condition` (
  `ID_CONDITION` bigint(20) NOT NULL,
  `ID_CONDITION_PARENT` int(11) DEFAULT NULL,
  `NEGATED` tinyint(1) DEFAULT NULL,
  `OPERATOR` varchar(255) DEFAULT NULL,
  `LEFT_NAME` varchar(255) DEFAULT NULL,
  `CONDITION_FUNCTION` varchar(255) DEFAULT NULL,
  `RIGHT_NAME` varchar(255) DEFAULT NULL,
  `ID_VALUE_RIGHT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_CONDITION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_condition
-- ----------------------------

-- ----------------------------
-- Table structure for `r_database`
-- ----------------------------
DROP TABLE IF EXISTS `r_database`;
CREATE TABLE `r_database` (
  `ID_DATABASE` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `ID_DATABASE_TYPE` int(11) DEFAULT NULL,
  `ID_DATABASE_CONTYPE` int(11) DEFAULT NULL,
  `HOST_NAME` varchar(255) DEFAULT NULL,
  `DATABASE_NAME` mediumtext,
  `PORT` int(11) DEFAULT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `SERVERNAME` varchar(255) DEFAULT NULL,
  `DATA_TBS` varchar(255) DEFAULT NULL,
  `INDEX_TBS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_DATABASE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_database
-- ----------------------------

-- ----------------------------
-- Table structure for `r_database_attribute`
-- ----------------------------
DROP TABLE IF EXISTS `r_database_attribute`;
CREATE TABLE `r_database_attribute` (
  `ID_DATABASE_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_DATABASE` int(11) DEFAULT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `VALUE_STR` mediumtext,
  PRIMARY KEY (`ID_DATABASE_ATTRIBUTE`),
  UNIQUE KEY `IDX_RDAT` (`ID_DATABASE`,`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_database_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for `r_database_contype`
-- ----------------------------
DROP TABLE IF EXISTS `r_database_contype`;
CREATE TABLE `r_database_contype` (
  `ID_DATABASE_CONTYPE` bigint(20) NOT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_DATABASE_CONTYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_database_contype
-- ----------------------------
INSERT INTO `r_database_contype` VALUES ('1', 'Native', 'Native (JDBC)');
INSERT INTO `r_database_contype` VALUES ('2', 'ODBC', 'ODBC');
INSERT INTO `r_database_contype` VALUES ('3', 'OCI', 'OCI');
INSERT INTO `r_database_contype` VALUES ('4', 'Plugin', 'Plugin specific access method');
INSERT INTO `r_database_contype` VALUES ('5', 'JNDI', 'JNDI');
INSERT INTO `r_database_contype` VALUES ('6', ',', 'Custom');

-- ----------------------------
-- Table structure for `r_database_type`
-- ----------------------------
DROP TABLE IF EXISTS `r_database_type`;
CREATE TABLE `r_database_type` (
  `ID_DATABASE_TYPE` bigint(20) NOT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_DATABASE_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_database_type
-- ----------------------------
INSERT INTO `r_database_type` VALUES ('1', 'INGRES', 'Ingres');
INSERT INTO `r_database_type` VALUES ('2', 'MARIADB', 'MariaDB');
INSERT INTO `r_database_type` VALUES ('3', 'INTERBASE', 'Borland Interbase');
INSERT INTO `r_database_type` VALUES ('4', 'INFOBRIGHT', 'Infobright');
INSERT INTO `r_database_type` VALUES ('5', 'ORACLE', 'Oracle');
INSERT INTO `r_database_type` VALUES ('6', 'EXTENDB', 'ExtenDB');
INSERT INTO `r_database_type` VALUES ('7', 'MSACCESS', 'MS Access');
INSERT INTO `r_database_type` VALUES ('8', 'SYBASE', 'Sybase');
INSERT INTO `r_database_type` VALUES ('9', 'PALO', 'Palo MOLAP Server');
INSERT INTO `r_database_type` VALUES ('10', 'INFORMIX', 'Informix');
INSERT INTO `r_database_type` VALUES ('11', 'LucidDB', 'LucidDB');
INSERT INTO `r_database_type` VALUES ('12', 'TERADATA', 'Teradata');
INSERT INTO `r_database_type` VALUES ('13', 'UNIVERSE', 'UniVerse database');
INSERT INTO `r_database_type` VALUES ('14', 'IMPALA', 'Impala');
INSERT INTO `r_database_type` VALUES ('15', 'MONETDB', 'MonetDB');
INSERT INTO `r_database_type` VALUES ('16', 'IMPALASIMBA', 'Cloudera Impala');
INSERT INTO `r_database_type` VALUES ('17', 'CACHE', 'Intersystems Cache');
INSERT INTO `r_database_type` VALUES ('18', 'MSSQL', 'MS SQL Server');
INSERT INTO `r_database_type` VALUES ('19', 'GREENPLUM', 'Greenplum');
INSERT INTO `r_database_type` VALUES ('20', 'GENERIC', 'Generic database');
INSERT INTO `r_database_type` VALUES ('21', 'SQLITE', 'SQLite');
INSERT INTO `r_database_type` VALUES ('22', 'REMEDY-AR-SYSTEM', 'Remedy Action Request System');
INSERT INTO `r_database_type` VALUES ('23', 'MONDRIAN', 'Native Mondrian');
INSERT INTO `r_database_type` VALUES ('24', 'NETEZZA', 'Netezza');
INSERT INTO `r_database_type` VALUES ('25', 'KettleThin', 'Pentaho Data Services');
INSERT INTO `r_database_type` VALUES ('26', 'VERTICA5', 'Vertica 5+');
INSERT INTO `r_database_type` VALUES ('27', 'POSTGRESQL', 'PostgreSQL');
INSERT INTO `r_database_type` VALUES ('28', 'EXASOL4', 'Exasol 4');
INSERT INTO `r_database_type` VALUES ('29', 'HIVE', 'Hadoop Hive');
INSERT INTO `r_database_type` VALUES ('30', 'HYPERSONIC', 'Hypersonic');
INSERT INTO `r_database_type` VALUES ('31', 'AS/400', 'AS/400');
INSERT INTO `r_database_type` VALUES ('32', 'ORACLERDB', 'Oracle RDB');
INSERT INTO `r_database_type` VALUES ('33', 'DBASE', 'dBase III, IV or 5');
INSERT INTO `r_database_type` VALUES ('34', 'HIVE2', 'Hadoop Hive 2');
INSERT INTO `r_database_type` VALUES ('35', 'KINGBASEES', 'KingbaseES');
INSERT INTO `r_database_type` VALUES ('36', 'SAPR3', 'SAP ERP System');
INSERT INTO `r_database_type` VALUES ('37', 'SQLBASE', 'Gupta SQL Base');
INSERT INTO `r_database_type` VALUES ('38', 'DERBY', 'Apache Derby');
INSERT INTO `r_database_type` VALUES ('39', 'VERTICA', 'Vertica');
INSERT INTO `r_database_type` VALUES ('40', 'INFINIDB', 'Calpont InfiniDB');
INSERT INTO `r_database_type` VALUES ('41', 'MYSQL', 'MySQL');
INSERT INTO `r_database_type` VALUES ('42', 'MSSQLNATIVE', 'MS SQL Server (Native)');
INSERT INTO `r_database_type` VALUES ('43', 'H2', 'H2');
INSERT INTO `r_database_type` VALUES ('44', 'SAPDB', 'MaxDB (SAP DB)');
INSERT INTO `r_database_type` VALUES ('45', 'SPARKSIMBA', 'SparkSQL');
INSERT INTO `r_database_type` VALUES ('46', 'VECTORWISE', 'Ingres VectorWise');
INSERT INTO `r_database_type` VALUES ('47', 'DB2', 'IBM DB2');
INSERT INTO `r_database_type` VALUES ('48', 'NEOVIEW', 'Neoview');
INSERT INTO `r_database_type` VALUES ('49', 'SYBASEIQ', 'SybaseIQ');
INSERT INTO `r_database_type` VALUES ('50', 'REDSHIFT', 'Redshift');
INSERT INTO `r_database_type` VALUES ('51', 'FIREBIRD', 'Firebird SQL');
INSERT INTO `r_database_type` VALUES ('52', 'OpenERPDatabaseMeta', 'OpenERP Server');

-- ----------------------------
-- Table structure for `r_dependency`
-- ----------------------------
DROP TABLE IF EXISTS `r_dependency`;
CREATE TABLE `r_dependency` (
  `ID_DEPENDENCY` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_DATABASE` int(11) DEFAULT NULL,
  `TABLE_NAME` varchar(255) DEFAULT NULL,
  `FIELD_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_DEPENDENCY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_dependency
-- ----------------------------

-- ----------------------------
-- Table structure for `r_directory`
-- ----------------------------
DROP TABLE IF EXISTS `r_directory`;
CREATE TABLE `r_directory` (
  `ID_DIRECTORY` bigint(20) NOT NULL,
  `ID_DIRECTORY_PARENT` int(11) DEFAULT NULL,
  `DIRECTORY_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_DIRECTORY`),
  UNIQUE KEY `IDX_RDIR` (`ID_DIRECTORY_PARENT`,`DIRECTORY_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_directory
-- ----------------------------

-- ----------------------------
-- Table structure for `r_element`
-- ----------------------------
DROP TABLE IF EXISTS `r_element`;
CREATE TABLE `r_element` (
  `ID_ELEMENT` bigint(20) NOT NULL,
  `ID_ELEMENT_TYPE` int(11) DEFAULT NULL,
  `NAME` mediumtext,
  PRIMARY KEY (`ID_ELEMENT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_element
-- ----------------------------

-- ----------------------------
-- Table structure for `r_element_attribute`
-- ----------------------------
DROP TABLE IF EXISTS `r_element_attribute`;
CREATE TABLE `r_element_attribute` (
  `ID_ELEMENT_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_ELEMENT` int(11) DEFAULT NULL,
  `ID_ELEMENT_ATTRIBUTE_PARENT` int(11) DEFAULT NULL,
  `ATTR_KEY` varchar(255) DEFAULT NULL,
  `ATTR_VALUE` mediumtext,
  PRIMARY KEY (`ID_ELEMENT_ATTRIBUTE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_element_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for `r_element_type`
-- ----------------------------
DROP TABLE IF EXISTS `r_element_type`;
CREATE TABLE `r_element_type` (
  `ID_ELEMENT_TYPE` bigint(20) NOT NULL,
  `ID_NAMESPACE` int(11) DEFAULT NULL,
  `NAME` mediumtext,
  `DESCRIPTION` mediumtext,
  PRIMARY KEY (`ID_ELEMENT_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_element_type
-- ----------------------------

-- ----------------------------
-- Table structure for `r_job`
-- ----------------------------
DROP TABLE IF EXISTS `r_job`;
CREATE TABLE `r_job` (
  `ID_JOB` bigint(20) NOT NULL,
  `ID_DIRECTORY` int(11) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` mediumtext,
  `EXTENDED_DESCRIPTION` mediumtext,
  `JOB_VERSION` varchar(255) DEFAULT NULL,
  `JOB_STATUS` int(11) DEFAULT NULL,
  `ID_DATABASE_LOG` int(11) DEFAULT NULL,
  `TABLE_NAME_LOG` varchar(255) DEFAULT NULL,
  `CREATED_USER` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_USER` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  `USE_BATCH_ID` tinyint(1) DEFAULT NULL,
  `PASS_BATCH_ID` tinyint(1) DEFAULT NULL,
  `USE_LOGFIELD` tinyint(1) DEFAULT NULL,
  `SHARED_FILE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_JOB`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_job
-- ----------------------------

-- ----------------------------
-- Table structure for `r_jobentry`
-- ----------------------------
DROP TABLE IF EXISTS `r_jobentry`;
CREATE TABLE `r_jobentry` (
  `ID_JOBENTRY` bigint(20) NOT NULL,
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_JOBENTRY_TYPE` int(11) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` mediumtext,
  PRIMARY KEY (`ID_JOBENTRY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_jobentry
-- ----------------------------

-- ----------------------------
-- Table structure for `r_jobentry_attribute`
-- ----------------------------
DROP TABLE IF EXISTS `r_jobentry_attribute`;
CREATE TABLE `r_jobentry_attribute` (
  `ID_JOBENTRY_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_JOBENTRY` int(11) DEFAULT NULL,
  `NR` int(11) DEFAULT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `VALUE_NUM` double DEFAULT NULL,
  `VALUE_STR` mediumtext,
  PRIMARY KEY (`ID_JOBENTRY_ATTRIBUTE`),
  UNIQUE KEY `IDX_RJEA` (`ID_JOBENTRY_ATTRIBUTE`,`CODE`,`NR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_jobentry_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for `r_jobentry_copy`
-- ----------------------------
DROP TABLE IF EXISTS `r_jobentry_copy`;
CREATE TABLE `r_jobentry_copy` (
  `ID_JOBENTRY_COPY` bigint(20) NOT NULL,
  `ID_JOBENTRY` int(11) DEFAULT NULL,
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_JOBENTRY_TYPE` int(11) DEFAULT NULL,
  `NR` int(11) DEFAULT NULL,
  `GUI_LOCATION_X` int(11) DEFAULT NULL,
  `GUI_LOCATION_Y` int(11) DEFAULT NULL,
  `GUI_DRAW` tinyint(1) DEFAULT NULL,
  `PARALLEL` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_JOBENTRY_COPY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_jobentry_copy
-- ----------------------------

-- ----------------------------
-- Table structure for `r_jobentry_database`
-- ----------------------------
DROP TABLE IF EXISTS `r_jobentry_database`;
CREATE TABLE `r_jobentry_database` (
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_JOBENTRY` int(11) DEFAULT NULL,
  `ID_DATABASE` int(11) DEFAULT NULL,
  KEY `IDX_RJD1` (`ID_JOB`),
  KEY `IDX_RJD2` (`ID_DATABASE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_jobentry_database
-- ----------------------------

-- ----------------------------
-- Table structure for `r_jobentry_type`
-- ----------------------------
DROP TABLE IF EXISTS `r_jobentry_type`;
CREATE TABLE `r_jobentry_type` (
  `ID_JOBENTRY_TYPE` bigint(20) NOT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_JOBENTRY_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_jobentry_type
-- ----------------------------
INSERT INTO `r_jobentry_type` VALUES ('1', 'WEBSERVICE_AVAILABLE', '检查web服务是否可用');
INSERT INTO `r_jobentry_type` VALUES ('2', 'MYSQL_BULK_FILE', '从 Mysql 批量导出到文件');
INSERT INTO `r_jobentry_type` VALUES ('3', 'COPY_MOVE_RESULT_FILENAMES', '复制/移动结果文件');
INSERT INTO `r_jobentry_type` VALUES ('4', 'XSD_VALIDATOR', 'XSD Validator');
INSERT INTO `r_jobentry_type` VALUES ('5', 'SPECIAL', '特殊作业项');
INSERT INTO `r_jobentry_type` VALUES ('6', 'FILE_COMPARE', '比较文件');
INSERT INTO `r_jobentry_type` VALUES ('7', 'CREATE_FOLDER', '创建一个目录');
INSERT INTO `r_jobentry_type` VALUES ('8', 'MAIL_VALIDATOR', '邮件验证');
INSERT INTO `r_jobentry_type` VALUES ('9', 'PALO_CUBE_DELETE', 'Palo Cube Delete');
INSERT INTO `r_jobentry_type` VALUES ('10', 'HadoopJobExecutorPlugin', 'Hadoop Job Executor ');
INSERT INTO `r_jobentry_type` VALUES ('11', 'MAIL', '发送邮件');
INSERT INTO `r_jobentry_type` VALUES ('12', 'TRUNCATE_TABLES', '清空表');
INSERT INTO `r_jobentry_type` VALUES ('13', 'MSGBOX_INFO', '显示消息对话框');
INSERT INTO `r_jobentry_type` VALUES ('14', 'WAIT_FOR_SQL', '等待SQL');
INSERT INTO `r_jobentry_type` VALUES ('15', 'FTPS_GET', 'FTPS 下载');
INSERT INTO `r_jobentry_type` VALUES ('16', 'FTP_DELETE', 'FTP 删除');
INSERT INTO `r_jobentry_type` VALUES ('17', 'COLUMNS_EXIST', '检查列是否存在');
INSERT INTO `r_jobentry_type` VALUES ('18', 'CHECK_FILES_LOCKED', '检查文件是否被锁');
INSERT INTO `r_jobentry_type` VALUES ('19', 'UNZIP', '解压缩文件');
INSERT INTO `r_jobentry_type` VALUES ('20', 'JOB', '作业');
INSERT INTO `r_jobentry_type` VALUES ('21', 'DELETE_FILE', '删除一个文件');
INSERT INTO `r_jobentry_type` VALUES ('22', 'SHELL', 'Shell');
INSERT INTO `r_jobentry_type` VALUES ('23', 'ABORT', '中止作业');
INSERT INTO `r_jobentry_type` VALUES ('24', 'HiveJobExecutorPlugin', 'Amazon Hive Job Executor');
INSERT INTO `r_jobentry_type` VALUES ('25', 'XML_WELL_FORMED', 'Check if XML file is well formed');
INSERT INTO `r_jobentry_type` VALUES ('26', 'SFTP', 'SFTP 下载');
INSERT INTO `r_jobentry_type` VALUES ('27', 'HTTP', 'HTTP');
INSERT INTO `r_jobentry_type` VALUES ('28', 'FTP_PUT', 'FTP 上传');
INSERT INTO `r_jobentry_type` VALUES ('29', 'SQL', 'SQL');
INSERT INTO `r_jobentry_type` VALUES ('30', 'DataRefineryBuildModel', 'Build Model');
INSERT INTO `r_jobentry_type` VALUES ('31', 'WRITE_TO_FILE', '写入文件');
INSERT INTO `r_jobentry_type` VALUES ('32', 'PGP_VERIFY_FILES', '用PGP验证文件签名');
INSERT INTO `r_jobentry_type` VALUES ('33', 'DOS_UNIX_CONVERTER', 'DOS和UNIX之间的文本转换');
INSERT INTO `r_jobentry_type` VALUES ('34', 'PGP_DECRYPT_FILES', '用PGP解密文件');
INSERT INTO `r_jobentry_type` VALUES ('35', 'TALEND_JOB_EXEC', 'Talend 作业执行');
INSERT INTO `r_jobentry_type` VALUES ('36', 'EVAL', '使用 JavaScript 脚本验证');
INSERT INTO `r_jobentry_type` VALUES ('37', 'DELAY', '等待');
INSERT INTO `r_jobentry_type` VALUES ('38', 'HL7MLLPAcknowledge', 'HL7 MLLP Acknowledge');
INSERT INTO `r_jobentry_type` VALUES ('39', 'PALO_CUBE_CREATE', 'Palo Cube Create');
INSERT INTO `r_jobentry_type` VALUES ('40', 'FTP', 'FTP 下载');
INSERT INTO `r_jobentry_type` VALUES ('41', 'OozieJobExecutor', 'Oozie Job Executor');
INSERT INTO `r_jobentry_type` VALUES ('42', 'FOLDERS_COMPARE', '比较目录');
INSERT INTO `r_jobentry_type` VALUES ('43', 'ZIP_FILE', 'Zip 压缩文件');
INSERT INTO `r_jobentry_type` VALUES ('44', 'GET_POP', 'POP 收信');
INSERT INTO `r_jobentry_type` VALUES ('45', 'TRANS', '转换');
INSERT INTO `r_jobentry_type` VALUES ('46', 'SEND_NAGIOS_PASSIVE_CHECK', '发送Nagios 被动检查');
INSERT INTO `r_jobentry_type` VALUES ('47', 'SET_VARIABLES', '设置变量');
INSERT INTO `r_jobentry_type` VALUES ('48', 'MS_ACCESS_BULK_LOAD', 'MS Access Bulk Load');
INSERT INTO `r_jobentry_type` VALUES ('49', 'HadoopPigScriptExecutorPlugin', 'Pig Script Executor');
INSERT INTO `r_jobentry_type` VALUES ('50', 'DummyJob', 'Example Job');
INSERT INTO `r_jobentry_type` VALUES ('51', 'COPY_FILES', '复制文件');
INSERT INTO `r_jobentry_type` VALUES ('52', 'EVAL_FILES_METRICS', '计算文件大小或个数');
INSERT INTO `r_jobentry_type` VALUES ('53', 'SqoopImport', 'Sqoop Import');
INSERT INTO `r_jobentry_type` VALUES ('54', 'PING', 'Ping 一台主机');
INSERT INTO `r_jobentry_type` VALUES ('55', 'ADD_RESULT_FILENAMES', '添加文件到结果文件中');
INSERT INTO `r_jobentry_type` VALUES ('56', 'DELETE_FOLDERS', '删除目录');
INSERT INTO `r_jobentry_type` VALUES ('57', 'CHECK_DB_CONNECTIONS', '检查数据库连接');
INSERT INTO `r_jobentry_type` VALUES ('58', 'FILE_EXISTS', '检查一个文件是否存在');
INSERT INTO `r_jobentry_type` VALUES ('59', 'EVAL_TABLE_CONTENT', '计算表中的记录数');
INSERT INTO `r_jobentry_type` VALUES ('60', 'FILES_EXIST', '检查多个文件是否存在');
INSERT INTO `r_jobentry_type` VALUES ('61', 'SFTPPUT', 'SFTP 上传');
INSERT INTO `r_jobentry_type` VALUES ('62', 'FTPS_PUT', 'FTPS 上传');
INSERT INTO `r_jobentry_type` VALUES ('63', 'SqoopExport', 'Sqoop Export');
INSERT INTO `r_jobentry_type` VALUES ('64', 'DELETE_RESULT_FILENAMES', '从结果文件中删除文件');
INSERT INTO `r_jobentry_type` VALUES ('65', 'DELETE_FILES', '删除多个文件');
INSERT INTO `r_jobentry_type` VALUES ('66', 'HadoopCopyFilesPlugin', 'Hadoop Copy Files');
INSERT INTO `r_jobentry_type` VALUES ('67', 'PGP_ENCRYPT_FILES', '用PGP加密文件');
INSERT INTO `r_jobentry_type` VALUES ('68', 'WRITE_TO_LOG', '写日志');
INSERT INTO `r_jobentry_type` VALUES ('69', 'SUCCESS', '成功');
INSERT INTO `r_jobentry_type` VALUES ('70', 'WAIT_FOR_FILE', '等待文件');
INSERT INTO `r_jobentry_type` VALUES ('71', 'MSSQL_BULK_LOAD', 'SQLServer 批量加载');
INSERT INTO `r_jobentry_type` VALUES ('72', 'TELNET', '远程登录一台主机');
INSERT INTO `r_jobentry_type` VALUES ('73', 'MOVE_FILES', '移动文件');
INSERT INTO `r_jobentry_type` VALUES ('74', 'XSLT', 'XSL Transformation');
INSERT INTO `r_jobentry_type` VALUES ('75', 'EMRJobExecutorPlugin', 'Amazon EMR Job Executor');
INSERT INTO `r_jobentry_type` VALUES ('76', 'DTD_VALIDATOR', 'DTD Validator');
INSERT INTO `r_jobentry_type` VALUES ('77', 'IncETLJobPlugin', '增量抽取作业配置');
INSERT INTO `r_jobentry_type` VALUES ('78', 'SparkSubmit', 'Spark Submit');
INSERT INTO `r_jobentry_type` VALUES ('79', 'HadoopTransJobExecutorPlugin', 'Pentaho MapReduce');
INSERT INTO `r_jobentry_type` VALUES ('80', 'DATASOURCE_PUBLISH', 'Publish Model');
INSERT INTO `r_jobentry_type` VALUES ('81', 'HL7MLLPInput', 'HL7 MLLP Input');
INSERT INTO `r_jobentry_type` VALUES ('82', 'FOLDER_IS_EMPTY', '检查目录是否为空');
INSERT INTO `r_jobentry_type` VALUES ('83', 'SIMPLE_EVAL', '检验字段的值');
INSERT INTO `r_jobentry_type` VALUES ('84', 'EXPORT_REPOSITORY', '导出资源库到XML文件');
INSERT INTO `r_jobentry_type` VALUES ('85', 'TABLE_EXISTS', '检查表是否存在');
INSERT INTO `r_jobentry_type` VALUES ('86', 'SYSLOG', '用 Syslog 发送信息');
INSERT INTO `r_jobentry_type` VALUES ('87', 'MYSQL_BULK_LOAD', 'Mysql 批量加载');
INSERT INTO `r_jobentry_type` VALUES ('88', 'CREATE_FILE', '创建文件');
INSERT INTO `r_jobentry_type` VALUES ('89', 'SNMP_TRAP', '发送 SNMP 自陷');
INSERT INTO `r_jobentry_type` VALUES ('90', 'CONNECTED_TO_REPOSITORY', '检查是否连接到资源库');

-- ----------------------------
-- Table structure for `r_job_attribute`
-- ----------------------------
DROP TABLE IF EXISTS `r_job_attribute`;
CREATE TABLE `r_job_attribute` (
  `ID_JOB_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_JOB` int(11) DEFAULT NULL,
  `NR` int(11) DEFAULT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `VALUE_NUM` bigint(20) DEFAULT NULL,
  `VALUE_STR` mediumtext,
  PRIMARY KEY (`ID_JOB_ATTRIBUTE`),
  UNIQUE KEY `IDX_JATT` (`ID_JOB`,`CODE`,`NR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_job_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for `r_job_hop`
-- ----------------------------
DROP TABLE IF EXISTS `r_job_hop`;
CREATE TABLE `r_job_hop` (
  `ID_JOB_HOP` bigint(20) NOT NULL,
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_JOBENTRY_COPY_FROM` int(11) DEFAULT NULL,
  `ID_JOBENTRY_COPY_TO` int(11) DEFAULT NULL,
  `ENABLED` tinyint(1) DEFAULT NULL,
  `EVALUATION` tinyint(1) DEFAULT NULL,
  `UNCONDITIONAL` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_JOB_HOP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_job_hop
-- ----------------------------

-- ----------------------------
-- Table structure for `r_job_lock`
-- ----------------------------
DROP TABLE IF EXISTS `r_job_lock`;
CREATE TABLE `r_job_lock` (
  `ID_JOB_LOCK` bigint(20) NOT NULL,
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_USER` int(11) DEFAULT NULL,
  `LOCK_MESSAGE` mediumtext,
  `LOCK_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID_JOB_LOCK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_job_lock
-- ----------------------------

-- ----------------------------
-- Table structure for `r_job_note`
-- ----------------------------
DROP TABLE IF EXISTS `r_job_note`;
CREATE TABLE `r_job_note` (
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_NOTE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_job_note
-- ----------------------------

-- ----------------------------
-- Table structure for `r_log`
-- ----------------------------
DROP TABLE IF EXISTS `r_log`;
CREATE TABLE `r_log` (
  `ID_LOG` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `ID_LOGLEVEL` int(11) DEFAULT NULL,
  `LOGTYPE` varchar(255) DEFAULT NULL,
  `FILENAME` varchar(255) DEFAULT NULL,
  `FILEEXTENTION` varchar(255) DEFAULT NULL,
  `ADD_DATE` tinyint(1) DEFAULT NULL,
  `ADD_TIME` tinyint(1) DEFAULT NULL,
  `ID_DATABASE_LOG` int(11) DEFAULT NULL,
  `TABLE_NAME_LOG` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_LOG`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_log
-- ----------------------------

-- ----------------------------
-- Table structure for `r_loglevel`
-- ----------------------------
DROP TABLE IF EXISTS `r_loglevel`;
CREATE TABLE `r_loglevel` (
  `ID_LOGLEVEL` bigint(20) NOT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_LOGLEVEL`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_loglevel
-- ----------------------------
INSERT INTO `r_loglevel` VALUES ('1', 'Error', '错误日志');
INSERT INTO `r_loglevel` VALUES ('2', 'Minimal', '最小日志');
INSERT INTO `r_loglevel` VALUES ('3', 'Basic', '基本日志');
INSERT INTO `r_loglevel` VALUES ('4', 'Detailed', '详细日志');
INSERT INTO `r_loglevel` VALUES ('5', 'Debug', '调试');
INSERT INTO `r_loglevel` VALUES ('6', 'Rowlevel', '行级日志(非常详细)');

-- ----------------------------
-- Table structure for `r_namespace`
-- ----------------------------
DROP TABLE IF EXISTS `r_namespace`;
CREATE TABLE `r_namespace` (
  `ID_NAMESPACE` bigint(20) NOT NULL,
  `NAME` mediumtext,
  PRIMARY KEY (`ID_NAMESPACE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_namespace
-- ----------------------------

-- ----------------------------
-- Table structure for `r_note`
-- ----------------------------
DROP TABLE IF EXISTS `r_note`;
CREATE TABLE `r_note` (
  `ID_NOTE` bigint(20) NOT NULL,
  `VALUE_STR` mediumtext,
  `GUI_LOCATION_X` int(11) DEFAULT NULL,
  `GUI_LOCATION_Y` int(11) DEFAULT NULL,
  `GUI_LOCATION_WIDTH` int(11) DEFAULT NULL,
  `GUI_LOCATION_HEIGHT` int(11) DEFAULT NULL,
  `FONT_NAME` mediumtext,
  `FONT_SIZE` int(11) DEFAULT NULL,
  `FONT_BOLD` tinyint(1) DEFAULT NULL,
  `FONT_ITALIC` tinyint(1) DEFAULT NULL,
  `FONT_COLOR_RED` int(11) DEFAULT NULL,
  `FONT_COLOR_GREEN` int(11) DEFAULT NULL,
  `FONT_COLOR_BLUE` int(11) DEFAULT NULL,
  `FONT_BACK_GROUND_COLOR_RED` int(11) DEFAULT NULL,
  `FONT_BACK_GROUND_COLOR_GREEN` int(11) DEFAULT NULL,
  `FONT_BACK_GROUND_COLOR_BLUE` int(11) DEFAULT NULL,
  `FONT_BORDER_COLOR_RED` int(11) DEFAULT NULL,
  `FONT_BORDER_COLOR_GREEN` int(11) DEFAULT NULL,
  `FONT_BORDER_COLOR_BLUE` int(11) DEFAULT NULL,
  `DRAW_SHADOW` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_NOTE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_note
-- ----------------------------

-- ----------------------------
-- Table structure for `r_partition`
-- ----------------------------
DROP TABLE IF EXISTS `r_partition`;
CREATE TABLE `r_partition` (
  `ID_PARTITION` bigint(20) NOT NULL,
  `ID_PARTITION_SCHEMA` int(11) DEFAULT NULL,
  `PARTITION_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_PARTITION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_partition
-- ----------------------------

-- ----------------------------
-- Table structure for `r_partition_schema`
-- ----------------------------
DROP TABLE IF EXISTS `r_partition_schema`;
CREATE TABLE `r_partition_schema` (
  `ID_PARTITION_SCHEMA` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DYNAMIC_DEFINITION` tinyint(1) DEFAULT NULL,
  `PARTITIONS_PER_SLAVE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_PARTITION_SCHEMA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_partition_schema
-- ----------------------------

-- ----------------------------
-- Table structure for `r_repository_log`
-- ----------------------------
DROP TABLE IF EXISTS `r_repository_log`;
CREATE TABLE `r_repository_log` (
  `ID_REPOSITORY_LOG` bigint(20) NOT NULL,
  `REP_VERSION` varchar(255) DEFAULT NULL,
  `LOG_DATE` datetime DEFAULT NULL,
  `LOG_USER` varchar(255) DEFAULT NULL,
  `OPERATION_DESC` mediumtext,
  PRIMARY KEY (`ID_REPOSITORY_LOG`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_repository_log
-- ----------------------------
INSERT INTO `r_repository_log` VALUES ('1', '5.0', '2018-09-21 12:13:13', 'admin', 'Creation of the Kettle repository');
INSERT INTO `r_repository_log` VALUES ('2', '5.0', '2018-09-21 14:19:43', 'admin', 'Upgrade of the Kettle repository');

-- ----------------------------
-- Table structure for `r_slave`
-- ----------------------------
DROP TABLE IF EXISTS `r_slave`;
CREATE TABLE `r_slave` (
  `ID_SLAVE` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `HOST_NAME` varchar(255) DEFAULT NULL,
  `PORT` varchar(255) DEFAULT NULL,
  `WEB_APP_NAME` varchar(255) DEFAULT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `PROXY_HOST_NAME` varchar(255) DEFAULT NULL,
  `PROXY_PORT` varchar(255) DEFAULT NULL,
  `NON_PROXY_HOSTS` varchar(255) DEFAULT NULL,
  `MASTER` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_SLAVE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_slave
-- ----------------------------

-- ----------------------------
-- Table structure for `r_step`
-- ----------------------------
DROP TABLE IF EXISTS `r_step`;
CREATE TABLE `r_step` (
  `ID_STEP` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` mediumtext,
  `ID_STEP_TYPE` int(11) DEFAULT NULL,
  `DISTRIBUTE` tinyint(1) DEFAULT NULL,
  `COPIES` int(11) DEFAULT NULL,
  `GUI_LOCATION_X` int(11) DEFAULT NULL,
  `GUI_LOCATION_Y` int(11) DEFAULT NULL,
  `GUI_DRAW` tinyint(1) DEFAULT NULL,
  `COPIES_STRING` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_STEP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_step
-- ----------------------------

-- ----------------------------
-- Table structure for `r_step_attribute`
-- ----------------------------
DROP TABLE IF EXISTS `r_step_attribute`;
CREATE TABLE `r_step_attribute` (
  `ID_STEP_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_STEP` int(11) DEFAULT NULL,
  `NR` int(11) DEFAULT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `VALUE_NUM` bigint(20) DEFAULT NULL,
  `VALUE_STR` mediumtext,
  PRIMARY KEY (`ID_STEP_ATTRIBUTE`),
  UNIQUE KEY `IDX_RSAT` (`ID_STEP`,`CODE`,`NR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_step_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for `r_step_database`
-- ----------------------------
DROP TABLE IF EXISTS `r_step_database`;
CREATE TABLE `r_step_database` (
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_STEP` int(11) DEFAULT NULL,
  `ID_DATABASE` int(11) DEFAULT NULL,
  KEY `IDX_RSD1` (`ID_TRANSFORMATION`),
  KEY `IDX_RSD2` (`ID_DATABASE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_step_database
-- ----------------------------

-- ----------------------------
-- Table structure for `r_step_type`
-- ----------------------------
DROP TABLE IF EXISTS `r_step_type`;
CREATE TABLE `r_step_type` (
  `ID_STEP_TYPE` bigint(20) NOT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `HELPTEXT` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID_STEP_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_step_type
-- ----------------------------
INSERT INTO `r_step_type` VALUES ('1', 'Delete', '删除', '基于关键字删除记录');
INSERT INTO `r_step_type` VALUES ('2', 'WriteToLog', '写日志', 'Write data to log');
INSERT INTO `r_step_type` VALUES ('3', 'SyslogMessage', '发送信息至Syslog', 'Send message to Syslog server');
INSERT INTO `r_step_type` VALUES ('4', 'TextFileOutput', '文本文件输出', '写记录到一个文本文件.');
INSERT INTO `r_step_type` VALUES ('5', 'MultiwayMergeJoin', 'Multiway Merge Join', 'Multiway Merge Join');
INSERT INTO `r_step_type` VALUES ('6', 'ParquetOutput', 'Parquet Output', 'Writes data to a Parquet file according to a mapping.');
INSERT INTO `r_step_type` VALUES ('7', 'LDAPOutput', 'LDAP 输出', 'Perform Insert, upsert, update, add or delete operations on records based on their DN (Distinguished  Name).');
INSERT INTO `r_step_type` VALUES ('8', 'SAPINPUT', 'SAP Input', 'Read data from SAP ERP, optionally with parameters');
INSERT INTO `r_step_type` VALUES ('9', 'AccessInput', 'Access 输入', 'Read data from a Microsoft Access file');
INSERT INTO `r_step_type` VALUES ('10', 'JoinRows', '记录关联 (笛卡尔输出)', '这个步骤的输出是输入流的笛卡尔的结果.{0} 输出结果的记录数是输入流记录之间的乘积.');
INSERT INTO `r_step_type` VALUES ('11', 'JsonInput', 'JSON Input', 'Extract relevant portions out of JSON structures (file or incoming field) and output rows');
INSERT INTO `r_step_type` VALUES ('12', 'TableOutput', '表输出', '写信息到一个数据库表');
INSERT INTO `r_step_type` VALUES ('13', 'MySQLBulkLoader', 'MySQL 批量加载', 'MySQL bulk loader step, loading data over a named pipe (not available on MS Windows)');
INSERT INTO `r_step_type` VALUES ('14', 'SingleThreader', 'Single Threader', 'Executes a transformation snippet in a single thread.  You need a standard mapping or a transformation with an Injector step where data from the parent transformation will arive in blocks.');
INSERT INTO `r_step_type` VALUES ('15', 'HL7Input', 'HL7 Input', 'Reads and parses HL7 messages and outputs a series of values from the messages');
INSERT INTO `r_step_type` VALUES ('16', 'RandomCCNumberGenerator', '生成随机的信用卡号', 'Generate random valide (luhn check) credit card numbers');
INSERT INTO `r_step_type` VALUES ('17', 'RegexEval', '正则表达式', 'Regular expression Evaluation\nThis step uses a regular expression to evaluate a field. It can also extract new fields out of an existing field with capturing groups.');
INSERT INTO `r_step_type` VALUES ('18', 'PaloDimOutput', 'Palo Dim Output', 'Writes data to defined Palo Dimension');
INSERT INTO `r_step_type` VALUES ('19', 'S3FileOutputPlugin', 'S3 File Output', 'Create files in an S3 location');
INSERT INTO `r_step_type` VALUES ('20', 'IfNull', '替换NULL值', 'Sets a field value to a constant if it is null.');
INSERT INTO `r_step_type` VALUES ('21', 'ColumnExists', '检查表里的列是否存在', 'Check if a column exists in a table on a specified connection.');
INSERT INTO `r_step_type` VALUES ('22', 'FileLocked', '检查文件是否已被锁定', 'Check if a file is locked by another process');
INSERT INTO `r_step_type` VALUES ('23', 'SocketWriter', 'Socket 写', 'Socket writer.  A socket server that can send rows of data to a socket reader.');
INSERT INTO `r_step_type` VALUES ('24', 'ExcelInput', 'Excel输入', '从一个微软的Excel文件里读取数据. 兼容Excel 95, 97 and 2000.');
INSERT INTO `r_step_type` VALUES ('25', 'TeraFast', 'Teradata Fastload 批量加载', 'The Teradata Fastload Bulk loader');
INSERT INTO `r_step_type` VALUES ('26', 'DataGrid', '自定义常量数据', 'Enter rows of static data in a grid, usually for testing, reference or demo purpose');
INSERT INTO `r_step_type` VALUES ('27', 'KafkaProducerOutput', 'Kafka Producer', 'Produce messages to a Kafka topic');
INSERT INTO `r_step_type` VALUES ('28', 'DetectLastRow', '识别流的最后一行', 'Last row will be marked');
INSERT INTO `r_step_type` VALUES ('29', 'TypeExitEdi2XmlStep', 'Edi to XML', 'Converts Edi text to generic XML');
INSERT INTO `r_step_type` VALUES ('30', 'SortRows', '排序记录', '基于字段值把记录排序(升序或降序)');
INSERT INTO `r_step_type` VALUES ('31', 'FieldsChangeSequence', '根据字段值来改变序列', 'Add sequence depending of fields value change.\nEach time value of at least one field change, PDI will reset sequence. ');
INSERT INTO `r_step_type` VALUES ('32', 'RowsFromResult', '从结果获取记录', '这个允许你从同一个任务的前一个条目里读取记录.');
INSERT INTO `r_step_type` VALUES ('33', 'AvroInputNew', 'Avro Input', 'Reads data from Avro file');
INSERT INTO `r_step_type` VALUES ('34', 'JobExecutor', '执行作业', 'This step executes a Pentaho Data Integration job, sets parameters and passes rows.');
INSERT INTO `r_step_type` VALUES ('35', 'BlockUntilStepsFinish', '阻塞数据直到步骤都完成', 'Block this step until selected steps finish.');
INSERT INTO `r_step_type` VALUES ('36', 'AutoDoc', '自动文档输出', 'This step automatically generates documentation based on input in the form of a list of transformations and jobs');
INSERT INTO `r_step_type` VALUES ('37', 'SFTPPut', 'SFTP Put', 'Upload a file or a stream file to remote host via SFTP');
INSERT INTO `r_step_type` VALUES ('38', 'ProcessFiles', '处理文件', 'Process one file per row (copy or move or delete).\nThis step only accept filename in input.');
INSERT INTO `r_step_type` VALUES ('39', 'RuleAccumulator', 'Rules Accumulator', 'Rules Accumulator Step');
INSERT INTO `r_step_type` VALUES ('40', 'DimensionLookup', '维度查询/更新', '在一个数据仓库里更新一个渐变维 {0} 或者在这个维里查询信息.');
INSERT INTO `r_step_type` VALUES ('41', 'HBaseRowDecoder', 'HBase Row Decoder', 'Decodes an incoming key and HBase result object according to a mapping ');
INSERT INTO `r_step_type` VALUES ('42', 'TypeExitGoogleAnalyticsInputStep', 'Google Analytics', 'Fetches data from google analytics account');
INSERT INTO `r_step_type` VALUES ('43', 'PaloCellInput', 'Palo Cell Input', 'Reads data from a defined Palo Cube ');
INSERT INTO `r_step_type` VALUES ('44', 'Append', '追加流', 'Append 2 streams in an ordered way');
INSERT INTO `r_step_type` VALUES ('45', 'ZipFile', 'Zip 文件', 'Zip a file.\nFilename will be extracted from incoming stream.');
INSERT INTO `r_step_type` VALUES ('46', 'IncETLOutputPlugin', '增量抽取作业更新', '增量抽取更新');
INSERT INTO `r_step_type` VALUES ('47', 'StringCut', '剪切字符串', 'Strings cut (substring).');
INSERT INTO `r_step_type` VALUES ('48', 'MergeRows', '合并记录', '合并两个数据流, 并根据某个关键字排序.  这两个数据流被比较，以标识相等的、变更的、删除的和新建的记录.');
INSERT INTO `r_step_type` VALUES ('49', 'Validator', '数据检验', 'Validates passing data based on a set of rules');
INSERT INTO `r_step_type` VALUES ('50', 'SSTableOutput', 'SSTable Output', 'Writes to a filesystem directory as a Cassandra SSTable');
INSERT INTO `r_step_type` VALUES ('51', 'MemoryGroupBy', '在内存中分组', 'Builds aggregates in a group by fashion.\nThis step doesn\'t require sorted input.');
INSERT INTO `r_step_type` VALUES ('52', 'SystemInfo', '获取系统信息', '获取系统信息，例如时间、日期.');
INSERT INTO `r_step_type` VALUES ('53', 'Denormaliser', '列转行', 'Denormalises rows by looking up key-value pairs and by assigning them to new fields in the输出 rows.{0}This method aggregates and needs the输入 rows to be sorted on the grouping fields');
INSERT INTO `r_step_type` VALUES ('54', 'ExcelOutput', 'Excel输出', 'Stores records into an Excel (XLS) document with formatting information.');
INSERT INTO `r_step_type` VALUES ('55', 'OlapInput', 'OLAP 输入', 'Execute and retrieve data using an MDX query against any XML/A OLAP datasource using olap4j');
INSERT INTO `r_step_type` VALUES ('56', 'Delay', '延迟行', 'Output each input row after a delay');
INSERT INTO `r_step_type` VALUES ('57', 'NullIf', '设置值为NULL', '如果一个字段值等于某个固定值，那么把这个字段值设置成null');
INSERT INTO `r_step_type` VALUES ('58', 'SalesforceInput', 'Salesforce Input', 'Extract data from Salesforce');
INSERT INTO `r_step_type` VALUES ('59', 'CallEndpointStep', 'Call endpoint', 'Call an endpoint of the Pentaho Server.');
INSERT INTO `r_step_type` VALUES ('60', 'SocketReader', 'Socket 读', 'Socket reader.  A socket client that connects to a server (Socket Writer step).');
INSERT INTO `r_step_type` VALUES ('61', 'CombinationLookup', '联合查询/更新', '更新数据仓库里的一个junk维 {0} 可选的, 科研查询维里的信息.{1}junk维的主键是所有的字段.');
INSERT INTO `r_step_type` VALUES ('62', 'FilesToResult', '复制文件到结果', 'This step allows you to set filenames in the result of this transformation.\nSubsequent job entries can then use this information.');
INSERT INTO `r_step_type` VALUES ('63', 'GPBulkLoader', 'Greenplum Bulk Loader', 'Greenplum Bulk Loader');
INSERT INTO `r_step_type` VALUES ('64', 'VerticaBulkLoader', 'Vertica Bulk Loader', 'Bulk load data into a Vertica database table');
INSERT INTO `r_step_type` VALUES ('65', 'SalesforceDelete', 'Salesforce Delete', 'Delete records in Salesforce module.');
INSERT INTO `r_step_type` VALUES ('66', 'HTTP', 'HTTP client', 'Call a web service over HTTP by supplying a base URL by allowing parameters to be set dynamically');
INSERT INTO `r_step_type` VALUES ('67', 'XSDValidator', 'XSD Validator', 'Validate XML source (files or streams) against XML Schema Definition.');
INSERT INTO `r_step_type` VALUES ('68', 'SetValueConstant', '将字段值设置为常量', 'Set value of a field to a constant');
INSERT INTO `r_step_type` VALUES ('69', 'ScriptValueMod', 'JavaScript代码', 'This is a modified plugin for the Scripting Values with improved interface and performance.\nWritten & donated to open source by Martin Lange, Proconis : http://www.proconis.de');
INSERT INTO `r_step_type` VALUES ('70', 'CassandraInput', 'Cassandra Input', 'Reads data from a Cassandra table');
INSERT INTO `r_step_type` VALUES ('71', 'SwitchCase', 'Switch / Case', 'Switch a row to a certain target step based on the case value in a field.');
INSERT INTO `r_step_type` VALUES ('72', 'DBLookup', '数据库查询', '使用字段值在数据库里查询值');
INSERT INTO `r_step_type` VALUES ('73', 'FuzzyMatch', '模糊匹配', 'Finding approximate matches to a string using matching algorithms.\nRead a field from a main stream and output approximative value from lookup stream.');
INSERT INTO `r_step_type` VALUES ('74', 'TeraDataBulkLoader', 'Teradata TPT Bulk Loader', 'Teradata TPT bulkloader, using tbuild command');
INSERT INTO `r_step_type` VALUES ('75', 'TextFileInput', '文本文件输入', '从一个文本文件（几种格式）里读取数据{0}这些数据可以被传递到下一个步骤里...');
INSERT INTO `r_step_type` VALUES ('76', 'SalesforceInsert', 'Salesforce Insert', 'Insert records in Salesforce module.');
INSERT INTO `r_step_type` VALUES ('77', 'CouchDbInput', 'CouchDb Input', 'Reads from a Couch DB view');
INSERT INTO `r_step_type` VALUES ('78', 'ShapeFileReader', 'ESRI Shapefile Reader', 'Reads shape file data from an ESRI shape file and linked DBF file');
INSERT INTO `r_step_type` VALUES ('79', 'RssInput', 'RSS 输入', 'Read RSS feeds');
INSERT INTO `r_step_type` VALUES ('80', 'Unique', '去除重复记录', '去除重复的记录行，保持记录唯一{0}这个仅仅基于一个已经排好序的输入.{1}如果输入没有排序, 仅仅两个连续的记录行被正确处理.');
INSERT INTO `r_step_type` VALUES ('81', 'InfobrightOutput', 'Infobright 批量加载', 'Load data to an Infobright database table');
INSERT INTO `r_step_type` VALUES ('82', 'UserDefinedJavaClass', 'Java 代码', 'This step allows you to program a step using Java code');
INSERT INTO `r_step_type` VALUES ('83', 'OraBulkLoader', 'Oracle 批量加载', 'Use Oracle Bulk Loader to load data');
INSERT INTO `r_step_type` VALUES ('84', 'StepsMetrics', '转换步骤信息统计', 'Return metrics for one or several steps');
INSERT INTO `r_step_type` VALUES ('85', 'GetTableNames', '获取表名', 'Get table names from database connection and send them to the next step');
INSERT INTO `r_step_type` VALUES ('86', 'SetSessionVariableStep', 'Set session variables', 'Set session variables in the current user session.');
INSERT INTO `r_step_type` VALUES ('87', 'MappingInput', '映射输入规范', '指定一个映射的字段输入');
INSERT INTO `r_step_type` VALUES ('88', 'Abort', '中止', 'Abort a transformation');
INSERT INTO `r_step_type` VALUES ('89', 'PropertyOutput', '配置文件输出', 'Write data to properties file');
INSERT INTO `r_step_type` VALUES ('90', 'JsonOutput', 'JSON Output', 'Create JSON block and output it in a field or a file.');
INSERT INTO `r_step_type` VALUES ('91', 'ESOutputPlugin', 'ES输出', '输出数据到ElasticSearch');
INSERT INTO `r_step_type` VALUES ('92', 'TableExists', '检查表是否存在', 'Check if a table exists on a specified connection');
INSERT INTO `r_step_type` VALUES ('93', 'DetectEmptyStream', '检测空流', 'This step will output one empty row if input stream is empty\n(ie when input stream does not contain any row)');
INSERT INTO `r_step_type` VALUES ('94', 'GPLoad', 'Greenplum Load', 'Greenplum Load');
INSERT INTO `r_step_type` VALUES ('95', 'PaloDimInput', 'Palo Dim Input', 'Reads data from a defined Palo Dimension');
INSERT INTO `r_step_type` VALUES ('96', 'Normaliser', '行转列', 'De-normalised information can be normalised using this step type.');
INSERT INTO `r_step_type` VALUES ('97', 'SelectValues', '字段选择', '选择或移除记录里的字。{0}此外，可以设置字段的元数据: 类型, 长度和精度.');
INSERT INTO `r_step_type` VALUES ('98', 'XMLInputStream', 'XML Input Stream (StAX)', 'This step is capable of processing very large and complex XML files very fast.');
INSERT INTO `r_step_type` VALUES ('99', 'Flattener', '行扁平化', 'Flattens consequetive rows based on the order in which they appear in the输入 stream');
INSERT INTO `r_step_type` VALUES ('100', 'InsertUpdate', '插入 / 更新', '基于关键字更新或插入记录到数据库.');
INSERT INTO `r_step_type` VALUES ('101', 'OrcParquetOutputPlugin', 'OrcParquet Output', '输出orc或者parquet文件到hdfs，可以创建hive表');
INSERT INTO `r_step_type` VALUES ('102', 'MondrianInput', 'Mondrian 输入', 'Execute and retrieve data using an MDX query against a Pentaho Analyses OLAP server (Mondrian)');
INSERT INTO `r_step_type` VALUES ('103', 'SQLFileOutput', 'SQL 文件输出', 'Output SQL INSERT statements to file');
INSERT INTO `r_step_type` VALUES ('104', 'ReplaceString', '字符串替换', 'Replace all occurences a word in a string with another word.');
INSERT INTO `r_step_type` VALUES ('105', 'CubeInput', 'Cube 文件输入', '从一个cube读取记录.');
INSERT INTO `r_step_type` VALUES ('106', 'WebServiceLookup', 'Web 服务查询', '使用 Web 服务查询信息');
INSERT INTO `r_step_type` VALUES ('107', 'TableCompare', '比较表', 'Compares 2 tables and gives back a list of differences');
INSERT INTO `r_step_type` VALUES ('108', 'MailValidator', '检验邮件地址', 'Check if an email address is valid.');
INSERT INTO `r_step_type` VALUES ('109', 'OpenERPObjectOutputImport', 'OpenERP Object Output', 'Writes data into OpenERP objects using the object import procedure');
INSERT INTO `r_step_type` VALUES ('110', 'RowGenerator', '生成记录', '产生一些空记录或相等的行.');
INSERT INTO `r_step_type` VALUES ('111', 'DBJoin', '数据库连接', '使用数据流里的值作为参数执行一个数据库查询');
INSERT INTO `r_step_type` VALUES ('112', 'RuleExecutor', 'Rules Executor', 'Rules Executor Step');
INSERT INTO `r_step_type` VALUES ('113', 'MergeJoin', '记录集连接', 'Joins two streams on a given key and outputs a joined set. The input streams must be sorted on the join key');
INSERT INTO `r_step_type` VALUES ('114', 'OpenERPObjectInput', 'OpenERP Object Input', 'Reads data from OpenERP objects');
INSERT INTO `r_step_type` VALUES ('115', 'HBaseInput', 'HBase Input', 'Reads data from a HBase table according to a mapping ');
INSERT INTO `r_step_type` VALUES ('116', 'SalesforceUpdate', 'Salesforce Update', 'Update records in Salesforce module.');
INSERT INTO `r_step_type` VALUES ('117', 'XMLJoin', 'XML Join', 'Joins a stream of XML-Tags into a target XML string');
INSERT INTO `r_step_type` VALUES ('118', 'GetVariable', '获取变量', 'Determine the values of certain (environment or Kettle) variables and put them in field values.');
INSERT INTO `r_step_type` VALUES ('119', 'DBProc', '调用DB存储过程', '通过调用数据库存储过程获得返回值.');
INSERT INTO `r_step_type` VALUES ('120', 'RssOutput', 'RSS 输出', 'Read RSS stream.');
INSERT INTO `r_step_type` VALUES ('121', 'S3CSVINPUT', 'S3 CSV Input', 'Is capable of reading CSV data stored on Amazon S3 in parallel');
INSERT INTO `r_step_type` VALUES ('122', 'AvroOutput', 'Avro Output', 'Writes data to an Avro file according to a mapping');
INSERT INTO `r_step_type` VALUES ('123', 'SimpleMapping', 'Simple Mapping (sub-transformation)', 'Run a mapping (sub-transformation), use MappingInput and MappingOutput to specify the fields interface.  This is the simplified version only allowing one input and one output data set.');
INSERT INTO `r_step_type` VALUES ('124', 'OpenERPObjectDelete', 'OpenERP Object Delete', 'Deletes OpenERP objects');
INSERT INTO `r_step_type` VALUES ('125', 'LDAPInput', 'LDAP 输入', 'Read data from LDAP host');
INSERT INTO `r_step_type` VALUES ('126', 'XBaseInput', 'XBase输入', '从一个XBase类型的文件(DBF)读取记录');
INSERT INTO `r_step_type` VALUES ('127', 'CheckSum', '增加校验列', 'Add a checksum column for each input row');
INSERT INTO `r_step_type` VALUES ('128', 'ParallelGzipCsvInput', 'GZIP CSV Input', 'Parallel GZIP CSV file input reader');
INSERT INTO `r_step_type` VALUES ('129', 'SortedMerge', '排序合并', 'Sorted Merge');
INSERT INTO `r_step_type` VALUES ('130', 'ChangeFileEncoding', '改变文件编码', 'Change file encoding and create a new file');
INSERT INTO `r_step_type` VALUES ('131', 'LoadFileInput', '文件内容加载至内存', 'Load file content in memory');
INSERT INTO `r_step_type` VALUES ('132', 'Janino', '利用Janino计算Java表达式', 'Calculate the result of a Java Expression using Janino');
INSERT INTO `r_step_type` VALUES ('133', 'getXMLData', 'Get data from XML', 'Get data from XML file by using XPath.\n This step also allows you to parse XML defined in a previous field.');
INSERT INTO `r_step_type` VALUES ('134', 'LDIFInput', 'LDIF 输入', 'Read data from LDIF files');
INSERT INTO `r_step_type` VALUES ('135', 'PGBulkLoader', 'PostgreSQL 批量加载', 'PostgreSQL Bulk Loader');
INSERT INTO `r_step_type` VALUES ('136', 'RecordsFromStream', 'Get records from stream', 'This step allows you to read records from a Kafka consumer.');
INSERT INTO `r_step_type` VALUES ('137', 'CubeOutput', 'Cube输出', '把数据写入一个cube');
INSERT INTO `r_step_type` VALUES ('138', 'SymmetricCryptoTrans', '对称加密', 'Encrypt or decrypt a string using symmetric encryption.\nAvailable algorithms are DES, AES, TripleDES.');
INSERT INTO `r_step_type` VALUES ('139', 'AddXML', 'Add XML', 'Encode several fields into an XML fragment');
INSERT INTO `r_step_type` VALUES ('140', 'TableInput', '表输入', '从数据库表里读取信息.');
INSERT INTO `r_step_type` VALUES ('141', 'HadoopEnterPlugin', 'MapReduce Input', 'Enter a Hadoop Mapper or Reducer transformation');
INSERT INTO `r_step_type` VALUES ('142', 'SASInput', 'SAS 输入', 'This step reads files in sas7bdat (SAS) native format');
INSERT INTO `r_step_type` VALUES ('143', 'AccessOutput', 'Access 输出', 'Stores records into an MS-Access database table.');
INSERT INTO `r_step_type` VALUES ('144', 'MetaInject', 'ETL Metadata Injection', 'ETL元数据注入');
INSERT INTO `r_step_type` VALUES ('145', 'ElasticSearchBulk', 'ElasticSearch Bulk Insert', 'Performs bulk inserts into ElasticSearch');
INSERT INTO `r_step_type` VALUES ('146', 'GetFileNames', '获取文件名', 'Get file names from the operating system and send them to the next step.');
INSERT INTO `r_step_type` VALUES ('147', 'StringOperations', '字符串操作', 'Apply certain operations like trimming, padding and others to string value.');
INSERT INTO `r_step_type` VALUES ('148', 'SetVariable', '设置变量', 'Set environment variables based on a single input row.');
INSERT INTO `r_step_type` VALUES ('149', 'RandomValue', '生成随机数', 'Generate random value');
INSERT INTO `r_step_type` VALUES ('150', 'GetSessionVariableStep', 'Get session variables', 'Get session variables from the current user session.');
INSERT INTO `r_step_type` VALUES ('151', 'FieldSplitter', '拆分字段', '当你想把一个字段拆分成多个时，使用这个类型.');
INSERT INTO `r_step_type` VALUES ('152', 'BlockingStep', '阻塞数据', 'This step blocks until all incoming rows have been processed.  Subsequent steps only recieve the last input row to this step.');
INSERT INTO `r_step_type` VALUES ('153', 'CsvInput', 'CSV文件输入', 'Simple CSV file input');
INSERT INTO `r_step_type` VALUES ('154', 'PropertyInput', '配置文件输入', 'Read data (key, value) from properties files.');
INSERT INTO `r_step_type` VALUES ('155', 'SynchronizeAfterMerge', '数据同步', 'This step perform insert/update/delete in one go based on the value of a field. ');
INSERT INTO `r_step_type` VALUES ('156', 'StreamLookup', '流查询', '从转换中的其它流里查询值.');
INSERT INTO `r_step_type` VALUES ('157', 'Dummy', '空操作 (什么也不做)', '这个步骤类型什么都不作.{0} 当你想测试或拆分数据流的时候有用.');
INSERT INTO `r_step_type` VALUES ('158', 'PGPEncryptStream', 'PGP Encrypt stream', 'Encrypt data stream with PGP');
INSERT INTO `r_step_type` VALUES ('159', 'GetFilesRowsCount', '获取文件行数', 'Returns rows count for text files.');
INSERT INTO `r_step_type` VALUES ('160', 'SetValueField', '设置字段值', 'Set value of a field with another value field');
INSERT INTO `r_step_type` VALUES ('161', 'PGPDecryptStream', 'PGP Decrypt stream', 'Decrypt data stream with PGP');
INSERT INTO `r_step_type` VALUES ('162', 'Mapping', '映射 (子转换)', '运行一个映射 (子转换), 使用MappingInput和MappingOutput来指定接口的字段');
INSERT INTO `r_step_type` VALUES ('163', 'DynamicSQLRow', '执行Dynamic SQL', 'Execute dynamic SQL statement build in a previous field');
INSERT INTO `r_step_type` VALUES ('164', 'KafkaConsumerInput', 'Kafka Consumer', 'Consume messages from a Kafka topic');
INSERT INTO `r_step_type` VALUES ('165', 'Update', '更新', '基于关键字更新记录到数据库');
INSERT INTO `r_step_type` VALUES ('166', 'UniqueRowsByHashSet', '唯一行 (哈希值)', 'Remove double rows and leave only unique occurrences by using a HashSet.');
INSERT INTO `r_step_type` VALUES ('167', 'Formula', '公式', '使用 Pentaho 的公式库来计算公式');
INSERT INTO `r_step_type` VALUES ('168', 'WebServiceAvailable', '检查web服务是否可用', 'Check if a webservice is available');
INSERT INTO `r_step_type` VALUES ('169', 'ExecProcess', '启动一个进程', 'Execute a process and return the result');
INSERT INTO `r_step_type` VALUES ('170', 'Injector', '记录注射', 'Injector step to allow to inject rows into the transformation through the java API');
INSERT INTO `r_step_type` VALUES ('171', 'SampleRows', '样本行', 'Filter rows based on the line number.');
INSERT INTO `r_step_type` VALUES ('172', 'DummyStep', 'Example Step', 'This is a plugin example step');
INSERT INTO `r_step_type` VALUES ('173', 'GetSlaveSequence', 'Get ID from slave server', 'Retrieves unique IDs in blocks from a slave server.  The referenced sequence needs to be configured on the slave server in the XML configuration file.');
INSERT INTO `r_step_type` VALUES ('174', 'HadoopFileInputPlugin', 'Hadoop File Input', 'Process files from an HDFS location');
INSERT INTO `r_step_type` VALUES ('175', 'MappingOutput', '映射输出规范', '指定一个映射的字段输出');
INSERT INTO `r_step_type` VALUES ('176', 'FileExists', '检查文件是否存在', 'Check if a file exists');
INSERT INTO `r_step_type` VALUES ('177', 'Script', 'Script', 'Calculate values by scripting in Ruby, Python, Groovy, JavaScript, ... (JSR-223)');
INSERT INTO `r_step_type` VALUES ('178', 'CreateSharedDimensions', 'Shared Dimension', 'Create shared dimensions for use with Streamlined Data Refinery.');
INSERT INTO `r_step_type` VALUES ('179', 'GroupBy', '分组', '以分组的形式创建聚合.{0}这个仅仅在一个已经排好序的输入有效.{1}如果输入没有排序, 仅仅两个连续的记录行被正确处理.');
INSERT INTO `r_step_type` VALUES ('180', 'GetRepositoryNames', '获取资源库配置', 'Lists detailed information about transformations and/or jobs in a repository');
INSERT INTO `r_step_type` VALUES ('181', 'PaloCellOutput', 'Palo Cell Output', 'Writes data to a defined Palo Cube');
INSERT INTO `r_step_type` VALUES ('182', 'ClosureGenerator', 'Closure Generator', 'This step allows you to generates a closure table using parent-child relationships.');
INSERT INTO `r_step_type` VALUES ('183', 'Sequence', '增加序列', '从序列获取下一个值');
INSERT INTO `r_step_type` VALUES ('184', 'FilterRows', '过滤记录', '使用简单的相等来过滤记录');
INSERT INTO `r_step_type` VALUES ('185', 'VectorWiseBulkLoader', 'Ingres VectorWise 批量加载', 'This step interfaces with the Ingres VectorWise Bulk Loader \"COPY TABLE\" command.');
INSERT INTO `r_step_type` VALUES ('186', 'ConcatFields', 'Concat Fields', 'Concat fields together into a new field (similar to the Text File Output step)');
INSERT INTO `r_step_type` VALUES ('187', 'TypeExitExcelWriterStep', 'Microsoft Excel 输出', 'Writes or appends data to an Excel file');
INSERT INTO `r_step_type` VALUES ('188', 'OldTextFileInput', 'Old Text file input', '从一个文本文件（几种格式）里读取数据{0}这些数据可以被传递到下一个步骤里...');
INSERT INTO `r_step_type` VALUES ('189', 'AnalyticQuery', '分析查询', 'Execute analytic queries over a sorted dataset (LEAD/LAG/FIRST/LAST)');
INSERT INTO `r_step_type` VALUES ('190', 'FixedInput', '固定宽度文件输入', 'Fixed file input');
INSERT INTO `r_step_type` VALUES ('191', 'ParquetInput', 'Parquet Input', 'Reads data from a Parquet file.');
INSERT INTO `r_step_type` VALUES ('192', 'PrioritizeStreams', '数据流优先级排序', 'Prioritize streams in an order way.');
INSERT INTO `r_step_type` VALUES ('193', 'CassandraOutput', 'Cassandra Output', 'Writes to a Cassandra table');
INSERT INTO `r_step_type` VALUES ('194', 'ValueMapper', '值映射', 'Maps values of a certain field from one value to another');
INSERT INTO `r_step_type` VALUES ('195', 'Constant', '增加常量', '给记录增加一到多个常量');
INSERT INTO `r_step_type` VALUES ('196', 'HadoopFileOutputPlugin', 'Hadoop File Output', 'Create files in an HDFS location ');
INSERT INTO `r_step_type` VALUES ('197', 'ExecSQLRow', '执行SQL脚本(字段流替换)', 'Execute SQL script extracted from a field\ncreated in a previous step.');
INSERT INTO `r_step_type` VALUES ('198', 'JavaFilter', '根据Java代码过滤记录', 'Filter rows using java code');
INSERT INTO `r_step_type` VALUES ('199', 'XMLOutput', 'XML Output', 'Write data to an XML file');
INSERT INTO `r_step_type` VALUES ('200', 'ReservoirSampling', '数据采样', '[Transform] Samples a fixed number of rows from the incoming stream');
INSERT INTO `r_step_type` VALUES ('201', 'SalesforceUpsert', 'Salesforce Upsert', 'Insert or update records in Salesforce module.');
INSERT INTO `r_step_type` VALUES ('202', 'RowsToResult', '复制记录到结果', '使用这个步骤把记录写到正在执行的任务.{0}信息将会被传递给同一个任务里的下一个条目.');
INSERT INTO `r_step_type` VALUES ('203', 'MonetDBAgileMart', 'MonetDB Agile Mart', 'Load data into MonetDB for Agile BI use cases');
INSERT INTO `r_step_type` VALUES ('204', 'ExecSQL', '执行SQL脚本', '执行一个SQL脚本, 另外，可以使用输入的记录作为参数');
INSERT INTO `r_step_type` VALUES ('205', 'HBaseOutput', 'HBase Output', 'Writes data to an HBase table according to a mapping');
INSERT INTO `r_step_type` VALUES ('206', 'Rest', 'REST Client', 'Consume RESTfull services.\nREpresentational State Transfer (REST) is a key design idiom that embraces a stateless client-server\narchitecture in which the web services are viewed as resources and can be identified by their URLs');
INSERT INTO `r_step_type` VALUES ('207', 'FieldMetadataAnnotation', 'Annotate Stream', 'Add more details to describe data for published models used by the Streamlined Data Refinery.');
INSERT INTO `r_step_type` VALUES ('208', 'LucidDBStreamingLoader', 'LucidDB Streaming Loader', 'Load data into LucidDB by using Remote Rows UDX.');
INSERT INTO `r_step_type` VALUES ('209', 'TransExecutor', 'Transformation Executor', 'This step executes a Pentaho Data Integration transformation, sets parameters and passes rows.');
INSERT INTO `r_step_type` VALUES ('210', 'StepMetastructure', '流的元数据', 'This is a step to read the metadata of the incoming stream.');
INSERT INTO `r_step_type` VALUES ('211', 'Calculator', '计算器', '通过执行简单的计算创建一个新字段');
INSERT INTO `r_step_type` VALUES ('212', 'SplitFieldToRows3', '列拆分为多行', 'Splits a single string field by delimiter and creates a new row for each split term');
INSERT INTO `r_step_type` VALUES ('213', 'MongoDbOutput', 'MongoDB Output', 'Writes to a Mongo DB collection');
INSERT INTO `r_step_type` VALUES ('214', 'YamlInput', 'Yaml 输入', 'Read YAML source (file or stream) parse them and convert them to rows and writes these to one or more output. ');
INSERT INTO `r_step_type` VALUES ('215', 'FilesFromResult', '从结果获取文件', 'This step allows you to read filenames used or generated in a previous entry in a job.');
INSERT INTO `r_step_type` VALUES ('216', 'UnivariateStats', '单变量统计', 'This step computes some simple stats based on a single input field');
INSERT INTO `r_step_type` VALUES ('217', 'SSH', '运行SSH命令', 'Run SSH commands and returns result.');
INSERT INTO `r_step_type` VALUES ('218', 'XSLT', 'XSL Transformation', 'Make an XSL Transformation');
INSERT INTO `r_step_type` VALUES ('219', 'MailInput', '邮件信息输入', 'Read POP3/IMAP server and retrieve messages');
INSERT INTO `r_step_type` VALUES ('220', 'CloneRow', '克隆行', 'Clone a row as many times as needed');
INSERT INTO `r_step_type` VALUES ('221', 'HTTPPOST', 'HTTP Post', 'Call a web service request over HTTP by supplying a base URL by allowing parameters to be set dynamically');
INSERT INTO `r_step_type` VALUES ('222', 'HadoopExitPlugin', 'MapReduce Output', 'Exit a Hadoop Mapper or Reducer transformation ');
INSERT INTO `r_step_type` VALUES ('223', 'TableAgileMart', 'Table Agile Mart', 'Load data into a table for Agile BI use cases');
INSERT INTO `r_step_type` VALUES ('224', 'GetSubFolders', '获取子目录名', 'Read a parent folder and return all subfolders');
INSERT INTO `r_step_type` VALUES ('225', 'MongoDbInput', 'MongoDB Input', 'Reads from a Mongo DB collection');
INSERT INTO `r_step_type` VALUES ('226', 'NumberRange', '数值范围', 'Create ranges based on numeric field');
INSERT INTO `r_step_type` VALUES ('227', 'CreditCardValidator', '检验信用卡号码是否有效', 'The Credit card validator step will help you tell:\n(1) if a credit card number is valid (uses LUHN10 (MOD-10) algorithm)\n(2) which credit card vendor handles that number\n(VISA, MasterCard, Diners Club, EnRoute, American Express (AMEX),...)');
INSERT INTO `r_step_type` VALUES ('228', 'MonetDBBulkLoader', 'MonetDB 批量加载', 'Load data into MonetDB by using their bulk load command in streaming mode.');
INSERT INTO `r_step_type` VALUES ('229', 'Mail', '发送邮件', 'Send eMail.');
INSERT INTO `r_step_type` VALUES ('230', 'SecretKeyGenerator', '生成密钥', 'Generate secret key for algorithms such as DES, AES, TripleDES.');
INSERT INTO `r_step_type` VALUES ('231', 'PentahoReportingOutput', 'Pentaho 报表输出', 'Executes an existing report (PRPT)');

-- ----------------------------
-- Table structure for `r_transformation`
-- ----------------------------
DROP TABLE IF EXISTS `r_transformation`;
CREATE TABLE `r_transformation` (
  `ID_TRANSFORMATION` bigint(20) NOT NULL,
  `ID_DIRECTORY` int(11) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` mediumtext,
  `EXTENDED_DESCRIPTION` mediumtext,
  `TRANS_VERSION` varchar(255) DEFAULT NULL,
  `TRANS_STATUS` int(11) DEFAULT NULL,
  `ID_STEP_READ` int(11) DEFAULT NULL,
  `ID_STEP_WRITE` int(11) DEFAULT NULL,
  `ID_STEP_INPUT` int(11) DEFAULT NULL,
  `ID_STEP_OUTPUT` int(11) DEFAULT NULL,
  `ID_STEP_UPDATE` int(11) DEFAULT NULL,
  `ID_DATABASE_LOG` int(11) DEFAULT NULL,
  `TABLE_NAME_LOG` varchar(255) DEFAULT NULL,
  `USE_BATCHID` tinyint(1) DEFAULT NULL,
  `USE_LOGFIELD` tinyint(1) DEFAULT NULL,
  `ID_DATABASE_MAXDATE` int(11) DEFAULT NULL,
  `TABLE_NAME_MAXDATE` varchar(255) DEFAULT NULL,
  `FIELD_NAME_MAXDATE` varchar(255) DEFAULT NULL,
  `OFFSET_MAXDATE` double DEFAULT NULL,
  `DIFF_MAXDATE` double DEFAULT NULL,
  `CREATED_USER` varchar(255) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_USER` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  `SIZE_ROWSET` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_TRANSFORMATION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_transformation
-- ----------------------------

-- ----------------------------
-- Table structure for `r_trans_attribute`
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_attribute`;
CREATE TABLE `r_trans_attribute` (
  `ID_TRANS_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `NR` int(11) DEFAULT NULL,
  `CODE` varchar(255) DEFAULT NULL,
  `VALUE_NUM` bigint(20) DEFAULT NULL,
  `VALUE_STR` mediumtext,
  PRIMARY KEY (`ID_TRANS_ATTRIBUTE`),
  UNIQUE KEY `IDX_TATT` (`ID_TRANSFORMATION`,`CODE`,`NR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_trans_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for `r_trans_cluster`
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_cluster`;
CREATE TABLE `r_trans_cluster` (
  `ID_TRANS_CLUSTER` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_CLUSTER` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_CLUSTER`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_trans_cluster
-- ----------------------------

-- ----------------------------
-- Table structure for `r_trans_hop`
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_hop`;
CREATE TABLE `r_trans_hop` (
  `ID_TRANS_HOP` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_STEP_FROM` int(11) DEFAULT NULL,
  `ID_STEP_TO` int(11) DEFAULT NULL,
  `ENABLED` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_HOP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_trans_hop
-- ----------------------------

-- ----------------------------
-- Table structure for `r_trans_lock`
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_lock`;
CREATE TABLE `r_trans_lock` (
  `ID_TRANS_LOCK` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_USER` int(11) DEFAULT NULL,
  `LOCK_MESSAGE` mediumtext,
  `LOCK_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_LOCK`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_trans_lock
-- ----------------------------

-- ----------------------------
-- Table structure for `r_trans_note`
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_note`;
CREATE TABLE `r_trans_note` (
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_NOTE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_trans_note
-- ----------------------------

-- ----------------------------
-- Table structure for `r_trans_partition_schema`
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_partition_schema`;
CREATE TABLE `r_trans_partition_schema` (
  `ID_TRANS_PARTITION_SCHEMA` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_PARTITION_SCHEMA` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_PARTITION_SCHEMA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_trans_partition_schema
-- ----------------------------

-- ----------------------------
-- Table structure for `r_trans_slave`
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_slave`;
CREATE TABLE `r_trans_slave` (
  `ID_TRANS_SLAVE` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_SLAVE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_SLAVE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_trans_slave
-- ----------------------------

-- ----------------------------
-- Table structure for `r_trans_step_condition`
-- ----------------------------
DROP TABLE IF EXISTS `r_trans_step_condition`;
CREATE TABLE `r_trans_step_condition` (
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_STEP` int(11) DEFAULT NULL,
  `ID_CONDITION` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_trans_step_condition
-- ----------------------------

-- ----------------------------
-- Table structure for `r_user`
-- ----------------------------
DROP TABLE IF EXISTS `r_user`;
CREATE TABLE `r_user` (
  `ID_USER` bigint(20) NOT NULL,
  `LOGIN` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ENABLED` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_USER`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_user
-- ----------------------------
INSERT INTO `r_user` VALUES ('1', 'admin', '2be98afc86aa7f2e4cb79ce71da9fa6d4', 'Administrator', 'User manager', '1');
INSERT INTO `r_user` VALUES ('2', 'guest', '2be98afc86aa7f2e4cb79ce77cb97bcce', 'Guest account', 'Read-only guest account', '1');

-- ----------------------------
-- Table structure for `r_value`
-- ----------------------------
DROP TABLE IF EXISTS `r_value`;
CREATE TABLE `r_value` (
  `ID_VALUE` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `VALUE_TYPE` varchar(255) DEFAULT NULL,
  `VALUE_STR` varchar(255) DEFAULT NULL,
  `IS_NULL` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_VALUE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_value
-- ----------------------------

-- ----------------------------
-- Table structure for `r_version`
-- ----------------------------
DROP TABLE IF EXISTS `r_version`;
CREATE TABLE `r_version` (
  `ID_VERSION` bigint(20) NOT NULL,
  `MAJOR_VERSION` int(11) DEFAULT NULL,
  `MINOR_VERSION` int(11) DEFAULT NULL,
  `UPGRADE_DATE` datetime DEFAULT NULL,
  `IS_UPGRADE` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_VERSION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_version
-- ----------------------------
INSERT INTO `r_version` VALUES ('1', '5', '0', '2018-09-21 12:13:13', '0');
INSERT INTO `r_version` VALUES ('2', '5', '0', '2018-09-21 14:19:43', '1');

-- ----------------------------
-- Table structure for `schedule_job`
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'spring bean名称',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(100) DEFAULT NULL COMMENT 'cron表达式',
  `status` tinyint(4) DEFAULT NULL COMMENT '任务状态  0：正常  1：暂停',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='定时任务';

-- ----------------------------
-- Records of schedule_job
-- ----------------------------
INSERT INTO `schedule_job` VALUES ('1', 'testTask', 'test', 'dfjx', '0 0/30 * * * ?', '0', '有参数测试', '2016-12-01 23:16:46');
INSERT INTO `schedule_job` VALUES ('2', 'testTask', 'test2', null, '0 0/30 * * * ?', '1', '无参数测试', '2016-12-03 14:55:56');

-- ----------------------------
-- Table structure for `schedule_job_log`
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job_log`;
CREATE TABLE `schedule_job_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `job_id` bigint(20) NOT NULL COMMENT '任务id',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'spring bean名称',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数',
  `status` tinyint(4) NOT NULL COMMENT '任务状态    0：成功    1：失败',
  `error` varchar(2000) DEFAULT NULL COMMENT '失败信息',
  `times` int(11) NOT NULL COMMENT '耗时(单位：毫秒)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`log_id`),
  KEY `job_id` (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11356 DEFAULT CHARSET=utf8 COMMENT='定时任务日志';

-- ----------------------------
-- Records of schedule_job_log
-- ----------------------------


-- ----------------------------
-- Table structure for `sys_config`
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `param_key` (`param_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='系统配置信息表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES ('1', 'CLOUD_STORAGE_CONFIG_KEY', '{\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"aliyunDomain\":\"\",\"aliyunEndPoint\":\"\",\"aliyunPrefix\":\"\",\"qcloudBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuBucketName\":\"ios-app\",\"qiniuDomain\":\"http://7xqbwh.dl1.z0.glb.clouddn.com\",\"qiniuPrefix\":\"upload\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"type\":1}', '0', '云存储配置信息');

-- ----------------------------
-- Table structure for `sys_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级部门ID，一级部门为0',
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='部门管理';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '0', '北京东方金信科技有限公司', '0', '0');
INSERT INTO `sys_dept` VALUES ('2', '1', '海南分公司', '1', '0');
INSERT INTO `sys_dept` VALUES ('3', '1', '北京分公司', '2', '0');
INSERT INTO `sys_dept` VALUES ('4', '3', '技术部', '0', '0');
INSERT INTO `sys_dept` VALUES ('5', '3', '销售部', '1', '0');
INSERT INTO `sys_dept` VALUES ('6', '0', null, '0', '-1');

-- ----------------------------
-- Table structure for `sys_dict`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '字典名称',
  `type` varchar(100) NOT NULL COMMENT '字典类型',
  `code` varchar(100) NOT NULL COMMENT '字典码',
  `value` varchar(1000) NOT NULL COMMENT '字典值',
  `order_num` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标记  -1：已删除  0：正常',
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='数据字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('1', '性别', 'sex', '0', '女', '0', null, '0');
INSERT INTO `sys_dict` VALUES ('2', '性别', 'sex', '1', '男', '1', null, '0');
INSERT INTO `sys_dict` VALUES ('3', '性别', 'sex', '2', '未知', '3', null, '0');

-- ----------------------------
-- Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 COMMENT='系统日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '系统管理', null, null, '0', 'fa fa-cog', '11');
INSERT INTO `sys_menu` VALUES ('2', '1', '管理员管理', 'modules/sys/user.html', null, '1', 'fa fa-user', '1');
INSERT INTO `sys_menu` VALUES ('3', '1', '角色管理', 'modules/sys/role.html', null, '1', 'fa fa-user-secret', '2');
INSERT INTO `sys_menu` VALUES ('4', '1', '菜单管理', 'modules/sys/menu.html', null, '1', 'fa fa-th-list', '3');
INSERT INTO `sys_menu` VALUES ('5', '1', 'SQL监控', 'druid/sql.html', null, '1', 'fa fa-bug', '4');
INSERT INTO `sys_menu` VALUES ('6', '1', '定时任务', 'modules/job/schedule.html', null, '1', 'fa fa-tasks', '5');
INSERT INTO `sys_menu` VALUES ('7', '6', '查看', null, 'sys:schedule:list,sys:schedule:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('8', '6', '新增', null, 'sys:schedule:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('9', '6', '修改', null, 'sys:schedule:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('10', '6', '删除', null, 'sys:schedule:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('11', '6', '暂停', null, 'sys:schedule:pause', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('12', '6', '恢复', null, 'sys:schedule:resume', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('13', '6', '立即执行', null, 'sys:schedule:run', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('14', '6', '日志列表', null, 'sys:schedule:log', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('15', '2', '查看', null, 'sys:user:list,sys:user:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('16', '2', '新增', null, 'sys:user:save,sys:role:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('17', '2', '修改', null, 'sys:user:update,sys:role:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('18', '2', '删除', null, 'sys:user:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('19', '3', '查看', null, 'sys:role:list,sys:role:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('20', '3', '新增', null, 'sys:role:save,sys:menu:perms', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('21', '3', '修改', null, 'sys:role:update,sys:menu:perms', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('22', '3', '删除', null, 'sys:role:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('23', '4', '查看', null, 'sys:menu:list,sys:menu:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('24', '4', '新增', null, 'sys:menu:save,sys:menu:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('25', '4', '修改', null, 'sys:menu:update,sys:menu:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('26', '4', '删除', null, 'sys:menu:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('27', '1', '参数管理', 'modules/sys/config.html', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', '1', 'fa fa-sun-o', '6');
INSERT INTO `sys_menu` VALUES ('29', '1', '系统日志', 'modules/sys/log.html', 'sys:log:list', '1', 'fa fa-file-text-o', '7');
INSERT INTO `sys_menu` VALUES ('30', '1', '文件上传', 'modules/oss/oss.html', 'sys:oss:all', '1', 'fa fa-file-image-o', '6');
INSERT INTO `sys_menu` VALUES ('31', '1', '部门管理', 'modules/sys/dept.html', null, '1', 'fa fa-file-code-o', '1');
INSERT INTO `sys_menu` VALUES ('32', '31', '查看', null, 'sys:dept:list,sys:dept:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('33', '31', '新增', null, 'sys:dept:save,sys:dept:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('34', '31', '修改', null, 'sys:dept:update,sys:dept:select', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('35', '31', '删除', null, 'sys:dept:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('36', '1', '字典管理', 'modules/sys/dict.html', null, '1', 'fa fa-bookmark-o', '6');
INSERT INTO `sys_menu` VALUES ('37', '36', '查看', null, 'sys:dict:list,sys:dict:info', '2', null, '6');
INSERT INTO `sys_menu` VALUES ('38', '36', '新增', null, 'sys:dict:save', '2', null, '6');
INSERT INTO `sys_menu` VALUES ('39', '36', '修改', null, 'sys:dict:update', '2', null, '6');
INSERT INTO `sys_menu` VALUES ('40', '36', '删除', null, 'sys:dict:delete', '2', null, '6');
INSERT INTO `sys_menu` VALUES ('42', '0', 'ETL服务器配置', 'modules/etl/server.html', null, '1', 'fa fa-server', '4');
INSERT INTO `sys_menu` VALUES ('43', '42', '查看', null, 'etl:server:list,etl:server:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('44', '42', '新增', null, 'etl:server:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('45', '42', '修改', null, 'etl:server:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('46', '42', '删除', null, 'etl:server:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('47', '0', '作业系统配置', 'modules/etl/sys.html', null, '1', 'fa fa-cogs', '5');
INSERT INTO `sys_menu` VALUES ('48', '47', '查看', null, 'etl:sys:list,etl:sys:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('49', '47', '新增', null, 'etl:sys:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('50', '47', '修改', null, 'etl:sys:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('51', '47', '删除', null, 'etl:sys:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('52', '0', '作业模板配置', 'modules/etl/script.html', null, '1', 'fa fa-file-code-o', '7');
INSERT INTO `sys_menu` VALUES ('53', '52', '查看', null, 'etl:script:list,etl:script:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('54', '52', '新增', null, 'etl:script:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('55', '52', '修改', null, 'etl:script:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('56', '52', '删除', null, 'etl:script:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('57', '0', 'ETL任务管理', 'modules/etl/job.html', null, '1', 'fa fa-th-large', '8');
INSERT INTO `sys_menu` VALUES ('58', '57', '查看', null, 'etl:job:list,etl:job:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('59', '57', '新增', null, 'etl:job:save', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('60', '57', '修改', null, 'etl:job:update', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('61', '57', '删除', null, 'etl:job:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('62', '0', 'ETL任务日志', 'modules/etl/joblog.html', null, '1', 'fa fa-camera', '10');
INSERT INTO `sys_menu` VALUES ('63', '62', '查看', null, 'etl:joblog:list,etl:joblog:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('64', '62', '删除', null, 'etl:joblog:delete', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('65', '0', '仪表盘', 'main.html', null, '1', 'fa fa-pie-chart', '0');
INSERT INTO `sys_menu` VALUES ('66', '52', '脚本上传', null, 'etl:script:upload', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('67', '42', '服务器列表', null, 'etl:server:getService', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('68', '52', '脚本键值对', null, 'etl:script:getScripts', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('69', '62', '查看日志详情', null, 'etl:joblog:loadlog', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('70', '62', '下载日志详情', null, 'etl:joblog:logdload', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('71', '0', 'ETL任务监控', 'modules/etl/jobrerun.html', null, '1', 'fa fa-tv', '9');
INSERT INTO `sys_menu` VALUES ('72', '71', '查看', null, 'etl:job:list,etl:job:info', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('73', '71', '重跑', null, 'etl:job:rerunmulti', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('74', '57', '血缘影响分析', null, 'etl:job:analysis', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('75', '71', '批量更改作业有效性', null, 'etl:job:upbatchenable', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('76', '71', '批量作业状态更新', null, 'etl:job:upbatchstatus', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('77', '71', '批量更新数据日期', null, 'etl:job:upbatchtxdate', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('78', '57', '配置上传', null, 'etl:job:upload', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('79', '57', '校验配置', null, 'etl:job:batchconfig', '2', null, '0');
INSERT INTO `sys_menu` VALUES ('80', '57', '导出配置', null, 'etl:job:expconfig,etl:job:dloadconfig', '2', null, '0');

-- ----------------------------
-- Table structure for `sys_oss`
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件上传';

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '访问者', null, '3', '2018-07-30 11:29:04');
INSERT INTO `sys_role` VALUES ('2', '调度管理员', '调度管理员', '4', '2018-07-30 11:39:49');
INSERT INTO `sys_role` VALUES ('3', '超级管理员', null, '1', '2018-07-30 11:40:39');

-- ----------------------------
-- Table structure for `sys_role_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='角色与部门对应关系';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES ('19', '2', '4');
INSERT INTO `sys_role_dept` VALUES ('21', '1', '3');
INSERT INTO `sys_role_dept` VALUES ('27', '3', '1');
INSERT INTO `sys_role_dept` VALUES ('28', '3', '2');
INSERT INTO `sys_role_dept` VALUES ('29', '3', '3');
INSERT INTO `sys_role_dept` VALUES ('30', '3', '4');
INSERT INTO `sys_role_dept` VALUES ('31', '3', '5');

-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=663 DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('497', '2', '42');
INSERT INTO `sys_role_menu` VALUES ('498', '2', '43');
INSERT INTO `sys_role_menu` VALUES ('499', '2', '44');
INSERT INTO `sys_role_menu` VALUES ('500', '2', '45');
INSERT INTO `sys_role_menu` VALUES ('501', '2', '46');
INSERT INTO `sys_role_menu` VALUES ('502', '2', '47');
INSERT INTO `sys_role_menu` VALUES ('503', '2', '48');
INSERT INTO `sys_role_menu` VALUES ('504', '2', '49');
INSERT INTO `sys_role_menu` VALUES ('505', '2', '50');
INSERT INTO `sys_role_menu` VALUES ('506', '2', '51');
INSERT INTO `sys_role_menu` VALUES ('507', '2', '52');
INSERT INTO `sys_role_menu` VALUES ('508', '2', '53');
INSERT INTO `sys_role_menu` VALUES ('509', '2', '54');
INSERT INTO `sys_role_menu` VALUES ('510', '2', '55');
INSERT INTO `sys_role_menu` VALUES ('511', '2', '56');
INSERT INTO `sys_role_menu` VALUES ('512', '2', '66');
INSERT INTO `sys_role_menu` VALUES ('513', '2', '68');
INSERT INTO `sys_role_menu` VALUES ('514', '2', '57');
INSERT INTO `sys_role_menu` VALUES ('515', '2', '58');
INSERT INTO `sys_role_menu` VALUES ('516', '2', '59');
INSERT INTO `sys_role_menu` VALUES ('517', '2', '60');
INSERT INTO `sys_role_menu` VALUES ('518', '2', '61');
INSERT INTO `sys_role_menu` VALUES ('519', '2', '62');
INSERT INTO `sys_role_menu` VALUES ('520', '2', '63');
INSERT INTO `sys_role_menu` VALUES ('521', '2', '64');
INSERT INTO `sys_role_menu` VALUES ('522', '2', '69');
INSERT INTO `sys_role_menu` VALUES ('523', '2', '70');
INSERT INTO `sys_role_menu` VALUES ('524', '2', '71');
INSERT INTO `sys_role_menu` VALUES ('525', '2', '72');
INSERT INTO `sys_role_menu` VALUES ('526', '2', '73');
INSERT INTO `sys_role_menu` VALUES ('539', '1', '42');
INSERT INTO `sys_role_menu` VALUES ('540', '1', '43');
INSERT INTO `sys_role_menu` VALUES ('541', '1', '47');
INSERT INTO `sys_role_menu` VALUES ('542', '1', '48');
INSERT INTO `sys_role_menu` VALUES ('543', '1', '52');
INSERT INTO `sys_role_menu` VALUES ('544', '1', '53');
INSERT INTO `sys_role_menu` VALUES ('545', '1', '57');
INSERT INTO `sys_role_menu` VALUES ('546', '1', '58');
INSERT INTO `sys_role_menu` VALUES ('547', '1', '62');
INSERT INTO `sys_role_menu` VALUES ('548', '1', '63');
INSERT INTO `sys_role_menu` VALUES ('549', '1', '69');
INSERT INTO `sys_role_menu` VALUES ('550', '1', '71');
INSERT INTO `sys_role_menu` VALUES ('551', '1', '72');
INSERT INTO `sys_role_menu` VALUES ('605', '3', '1');
INSERT INTO `sys_role_menu` VALUES ('606', '3', '2');
INSERT INTO `sys_role_menu` VALUES ('607', '3', '15');
INSERT INTO `sys_role_menu` VALUES ('608', '3', '17');
INSERT INTO `sys_role_menu` VALUES ('609', '3', '3');
INSERT INTO `sys_role_menu` VALUES ('610', '3', '19');
INSERT INTO `sys_role_menu` VALUES ('611', '3', '20');
INSERT INTO `sys_role_menu` VALUES ('612', '3', '21');
INSERT INTO `sys_role_menu` VALUES ('613', '3', '22');
INSERT INTO `sys_role_menu` VALUES ('614', '3', '4');
INSERT INTO `sys_role_menu` VALUES ('615', '3', '23');
INSERT INTO `sys_role_menu` VALUES ('616', '3', '24');
INSERT INTO `sys_role_menu` VALUES ('617', '3', '25');
INSERT INTO `sys_role_menu` VALUES ('618', '3', '26');
INSERT INTO `sys_role_menu` VALUES ('619', '3', '31');
INSERT INTO `sys_role_menu` VALUES ('620', '3', '32');
INSERT INTO `sys_role_menu` VALUES ('621', '3', '33');
INSERT INTO `sys_role_menu` VALUES ('622', '3', '34');
INSERT INTO `sys_role_menu` VALUES ('623', '3', '35');
INSERT INTO `sys_role_menu` VALUES ('624', '3', '42');
INSERT INTO `sys_role_menu` VALUES ('625', '3', '43');
INSERT INTO `sys_role_menu` VALUES ('626', '3', '44');
INSERT INTO `sys_role_menu` VALUES ('627', '3', '45');
INSERT INTO `sys_role_menu` VALUES ('628', '3', '46');
INSERT INTO `sys_role_menu` VALUES ('629', '3', '67');
INSERT INTO `sys_role_menu` VALUES ('630', '3', '47');
INSERT INTO `sys_role_menu` VALUES ('631', '3', '48');
INSERT INTO `sys_role_menu` VALUES ('632', '3', '49');
INSERT INTO `sys_role_menu` VALUES ('633', '3', '50');
INSERT INTO `sys_role_menu` VALUES ('634', '3', '51');
INSERT INTO `sys_role_menu` VALUES ('635', '3', '52');
INSERT INTO `sys_role_menu` VALUES ('636', '3', '53');
INSERT INTO `sys_role_menu` VALUES ('637', '3', '54');
INSERT INTO `sys_role_menu` VALUES ('638', '3', '55');
INSERT INTO `sys_role_menu` VALUES ('639', '3', '56');
INSERT INTO `sys_role_menu` VALUES ('640', '3', '66');
INSERT INTO `sys_role_menu` VALUES ('641', '3', '68');
INSERT INTO `sys_role_menu` VALUES ('642', '3', '57');
INSERT INTO `sys_role_menu` VALUES ('643', '3', '58');
INSERT INTO `sys_role_menu` VALUES ('644', '3', '59');
INSERT INTO `sys_role_menu` VALUES ('645', '3', '60');
INSERT INTO `sys_role_menu` VALUES ('646', '3', '61');
INSERT INTO `sys_role_menu` VALUES ('647', '3', '74');
INSERT INTO `sys_role_menu` VALUES ('648', '3', '78');
INSERT INTO `sys_role_menu` VALUES ('649', '3', '79');
INSERT INTO `sys_role_menu` VALUES ('650', '3', '80');
INSERT INTO `sys_role_menu` VALUES ('651', '3', '62');
INSERT INTO `sys_role_menu` VALUES ('652', '3', '63');
INSERT INTO `sys_role_menu` VALUES ('653', '3', '64');
INSERT INTO `sys_role_menu` VALUES ('654', '3', '69');
INSERT INTO `sys_role_menu` VALUES ('655', '3', '70');
INSERT INTO `sys_role_menu` VALUES ('656', '3', '65');
INSERT INTO `sys_role_menu` VALUES ('657', '3', '71');
INSERT INTO `sys_role_menu` VALUES ('658', '3', '72');
INSERT INTO `sys_role_menu` VALUES ('659', '3', '73');
INSERT INTO `sys_role_menu` VALUES ('660', '3', '75');
INSERT INTO `sys_role_menu` VALUES ('661', '3', '76');
INSERT INTO `sys_role_menu` VALUES ('662', '3', '77');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `user_real_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', 'e1153123d7d180ceeb820d577ff119876678732a68eef4e6ffc0b1f06a01f91b', 'YzcmCZNvbXocrsz9dm8e', 'root@dfjx.io', '13612345678', '1', '1', '2016-11-11 11:11:11', '系统管理员');
INSERT INTO `sys_user` VALUES ('2', 'lwq', '6ce99541eb6a850447247c7c73178600fcbdaf5882751b26d18a29078ca365bd', '7juutNNDCbDnj4Xw5idh', 'lwq@126.com', '18611061948', '1', '2', '2018-07-24 14:24:03', null);
INSERT INTO `sys_user` VALUES ('3', 'lwq2', '94c62a971dc04f866aab0283da765733b77f373f1dedd67974265860ce7be18e', '2Y241X49aQFWfGME2Ehz', 'lwq@qq.com', '18611071039', '1', '4', '2018-07-24 14:32:06', null);
INSERT INTO `sys_user` VALUES ('4', 'develop', 'eb8e6d8756249997fbc023114236c8622a6567b3d46ded17ad3f36ef3ff49b95', '8GRgIELkq5KQw1JDHCXO', null, null, '1', '1', '2018-11-28 15:11:42', null);
INSERT INTO `sys_user` VALUES ('5', 'luxuebao', 'd75df22a6bb3aee09c494d9446ce55cad4818cc209882b56e28186ebf8ee7feb', 'byvZp4NNxfzQIdgQKQ9a', null, null, '1', '1', '2018-11-28 15:13:07', null);
INSERT INTO `sys_user` VALUES ('6', 'huangyangguang', 'f67a5ab838b017f7c2ddff24081e5647f7cf10469a0fa25d70a0ef27f81fcec5', 'cXHzc8gJ1mNkf3H9nY02', null, 'test', '1', '1', '2018-11-30 15:33:25', null);
INSERT INTO `sys_user` VALUES ('7', 'niewei', 'e7b853a35aafb5f7a7e30373dd00a8e013b979cfc359cfa3ab736c18f03c4066', '3cdQsdLO4vTGbRJXAkkL', null, null, '1', '1', '2018-11-30 18:18:04', null);

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('6', '2', '1');
INSERT INTO `sys_user_role` VALUES ('7', '3', '2');
INSERT INTO `sys_user_role` VALUES ('8', '4', '1');
INSERT INTO `sys_user_role` VALUES ('9', '5', '1');
INSERT INTO `sys_user_role` VALUES ('10', '6', '1');
INSERT INTO `sys_user_role` VALUES ('11', '7', '1');
INSERT INTO `sys_user_role` VALUES ('12', '1', '3');

-- ----------------------------
-- Table structure for `w900_package`
-- ----------------------------
DROP TABLE IF EXISTS `w900_package`;
CREATE TABLE `w900_package` (
  `pkg_id` varchar(32) DEFAULT NULL,
  `pkg_name` varchar(100) DEFAULT NULL,
  `pkg_file` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of w900_package
-- ----------------------------
INSERT INTO `w900_package` VALUES ('0ebcc38e23f2418eb3cb79e030dddd72', '公民身份号码识别', 'idcardRule.zip');
