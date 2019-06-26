$(function () {
	$("#jqGrid").jqGrid({
		url: baseURL + 'etl/job/list',
		datatype: "json",
		colModel: [
		           { label: '序号', name: 'id', index: 'ID', width: 120, key: true,hidden:true},
		           { label: '作业系统', name: 'etlSystem', index: 'ETL_System', width: 50},
		           { label: '作业名称', name: 'etlJob', index: 'ETL_Job', width: 60  },
		           { label: '服务器', name: 'etlServer', index: 'ETL_Server', width: 40,hidden:true },
		           { label: '描述', name: 'description', index: 'Description', width: 55 },
		           { label: '', name: 'frequency', index: 'Frequency', width: 80 ,hidden:true},
		           { label: '作业周期', name: 'jobtype', index: 'JobType', width: 50,formatter: function (value, options, row) {
		        	   if(value == "D"){
		        		   return  '<span class="label label-primary">日作业</span>'
		        	   } else if (value === "M") {
		        		   return  '<span class="label label-info">月作业</span>'
		        	   } else {};
		           }},
		           { label: '是否有效', name: 'enable', index: 'Enable', width: 60,formatter: function (value, options, row) {
		        	   if(value == "1"){
		        		   return  '<span class="label label-success">有效作业</span>'
		        	   } else if (value == "0") {
		        		   return  '<span class="label label-default">无效作业</span>'
		        	   } else {
		        		   return '<span class="label label-default">未知状态</span>'
		        	   };
		           }},
		           { label: '开始时间', name: 'lastStarttime', index: 'Last_StartTime', width: 100 },
		           { label: '结束时间', name: 'lastEndtime', index: 'Last_EndTime', width: 100 },
		           { label: '运行状态', name: 'lastJobstatus', index: 'Last_JobStatus', width: 60,formatter: function(value, options, row){

		        	   if(value == "Done"){
		        		   return  '<span class="label label-success">执行成功</span>'
		        	   } else if (value === "Pending") {
		        		   return  '<span class="label label-default">即将执行</span>'
		        	   } else if (value === "Ready") {
		        		   return   '<span class="label label-info">准备执行</span>'
		        	   } else if (value === "Running") {
		        		   return   '<span class="label label-primary">正在执行</span>'
		        	   } else if (value === "Failed") {
		        		   return   '<span class="label label-danger">运行失败</span>'
		        	   } else {};



		           }},
		           { label: '数据日期', name: 'lastTxdate', index: 'Last_TXDate', width: 60,formatter:"date",editable:true },
		           { label: '', name: 'lastFilecnt', index: 'Last_FileCnt', width: 80 ,hidden:true},
		           { label: '', name: 'lastCubestatus', index: 'Last_CubeStatus', width: 80 ,hidden:true},
		           { label: '', name: 'cubeflag', index: 'CubeFlag', width: 80,hidden:true },
		           { label: '', name: 'checkflag', index: 'CheckFlag', width: 80 ,hidden:true},
		           { label: '', name: 'autooff', index: 'AutoOff', width: 80 ,hidden:true},
		           { label: '', name: 'checkcalendar', index: 'CheckCalendar', width: 80 ,hidden:true},
		           { label: '', name: 'calendarbu', index: 'CalendarBU', width: 80 ,hidden:true},
		           { label: '', name: 'runningscript', index: 'RunningScript', width: 80,hidden:true },
		           { label: '会话ID', name: 'jobsessionid', index: 'JobSessionID', width: 50 ,hidden:true},
		           { label: '', name: 'expectedrecord', index: 'ExpectedRecord', width: 80,hidden:true },
		           { label: '', name: 'checklaststatus', index: 'CheckLastStatus', width: 80,hidden:true },
		           { label: '', name: 'timetrigger', index: 'TimeTrigger', width: 80,hidden:true },
		           { label: '', name: 'priority', index: 'Priority', width: 80 ,hidden:true},
		           // { label: '操作', name: 'id', index: 'ID', width: 120,key:true},
		           // { label: '操作', name: 'id', index: 'ID', width: 120,key:true,formatter:jobOpFormate },

		           ],
		           viewrecords: true,
		           height: 385,
		           rowNum: 10,
		           rowList : [10,30,50],
		           rownumbers: true, 
		           rownumWidth: 25, 
		           autowidth:true,
		           multiselect: true,
		           pager: "#jqGridPager",
		           jsonReader : {
		        	   root: "page.list",
		        	   page: "page.currPage",
		        	   total: "page.totalPage",
		        	   records: "page.totalCount"
		           },
		           prmNames : {
		        	   page:"page", 
		        	   rows:"limit", 
		        	   order: "order"
		           },
		           // cellEdit:true,

		           gridComplete:function(){
		        	   //隐藏grid底部滚动条
		        	   $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		           }
	});
	vm.initSelectJob();
	//初始化系统
	vm.loadsys();
	vm.loadstatus("etlStatusSelect");
	//初始化导入
	uploadScript();
	//初始化服务器
	vm.loadserver();
	//初始化执行脚本
	vm.loadDoScripts();
	//初始化依赖脚本列表
	vm.initDependJob();
});



var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			dep_etlSystem : null,
			dep_job : null,
			reqDenpsType: null,
			etlJob : null,
			etlSystem:null,
			lastTxDateStart : null,
			lastTxDateEnd : null,
			rerun_data_date:null,
			lastJobStatus:null
		},
		dependQ:{
			etlSystem:null,
			etlJob:null
		},
		showList: true,
		showQueryData:false,
		title: null,
		job: {},
		allsys:[],
		allServer:[],
		doScripts:[],
		allstatus:null,
		isChange:false,
		allDepend:{},
		triggerJob:null
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.job = {
					etlJob:"",
					runningscript:null
			};
			//初始化依赖脚本列表
			$("#jqGridDependJob").jqGrid('clearGridData');
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
			vm.title = "修改";
			vm.getInfo(id);
		},
		saveOrUpdate: function (event) {
			var url = vm.job.id == null ? "etl/job/save" : "etl/job/update";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				contentType: "application/json",
				data: JSON.stringify(vm.job),
				success: function(r){
					if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		openRerun:function(){
			var id = getSelectedRow();
			if (id == null){
				return;
			};
			vm.showQueryData = true;
			vm.title = "重跑";
			//vm.rerun();
		},
		//重跑单个任务
		rerun: function(){
			var id = getSelectedRow();
			if (id == null){return};
			var rowdata = $("#jqGrid").jqGrid('getRowData',id);
			var etlSystem = rowdata.etlSystem;
			var etlJob = rowdata.etlJob;
			if(vm.q.rerun_data_date ==null){
				alert("请选择数据重跑日期");
				return;
			} else {
				var lastTxDate = vm.q.rerun_data_date;
				var url = "etl/job/rerun";
				$.ajax({
					type: "GET",
					url: baseURL + url,
					contentType: "application/json",
					data: {'etlSystem':etlSystem,'etlJob':etlJob,'lastTxDate':lastTxDate},
					// data: 'etlSystem=' + etlSystem + '&etlJob=' + etlJob + '&lastTxDate=' + lastTxDate,
					success: function(r){
						vm.q.rerun_data_date =null;
						if(r.code === 0){
							alert(r.msg, function(index){
								vm.reload();
								vm.reBack();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			}
			return;
		},

		loadsys: function(){
			$.ajax({
				type: "GET",
				url: baseURL + "etl/sys/getsys",
				contentType: "application/json",
				success: function(data){
					if(data.code == 0){
						vm.allsys = data.allsys;
						window.setTimeout(function(){
							$('#etlSystemSelect').selectpicker('refresh');
							$('#etlSystemAddSelect').selectpicker('refresh');
							$('#dependEtlSystemid').selectpicker('refresh');
						},500); 
					}else{
						alert(data.msg);
					}
				}

			});
		},


		loadstatus: function(componentId){
			// alert("loadstatus 被调用");
			$.ajax({
				type: "GET",
				url: baseURL + "etl/job/getstatus",
				contentType: "application/json",
				success: function(data){
					if(data.code == 0){
						// alert(JSON.stringify(data));
						vm.allstatus = data.allstatus;
						window.setTimeout(function(){
							$('#etlStatusSelect').selectpicker('refresh');
						},500); 
					}else{
						alert(data.msg);
					}
				}

			});
		},


		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}

			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
					url: baseURL + "etl/job/delete",
					contentType: "application/json",
					data: JSON.stringify(etlSystems),
					success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(etlJob){
			$.get(baseURL + "etl/job/info/"+etlJob, function(r){
				vm.job = r.job;
				vm.reloadDependJobs();
				$('#etlServerAddSelect').selectpicker('val', vm.job.etlServer);
				$('#etlSystemAddSelect').selectpicker('val', vm.job.etlSystem);
				$('#publicScriptid').selectpicker('val', vm.job.publicScript);
				$('#frequencyid').selectpicker('val', vm.job.frequency);
				$('#enableid').selectpicker('val', vm.job.enable);
			});
		},

		dependency: function(){
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			var rowdata = $("#jqGrid").jqGrid('getRowData',id);
			vm.q.dep_etlSystem = rowdata.etlSystem;
			vm.q.dep_etlJob = rowdata.etlJob;
			vm.q.reqDenpsType = true;
			vm.reload();
		},
		reloadDependJobsSelect:function(){
			var page = $("#jqGridJob").jqGrid('getGridParam','page');
			$("#jqGridJob").jqGrid('setGridParam', {
				postData: {
					'etlSystem': vm.dependQ.etlSystem,
					'etlJob': vm.dependQ.etlJob,
					'enable':"1"
				},
				page: page
			}).trigger("reloadGrid");
		},
		reloadDependJobs:function(){
			var page = $("#jqGridDependJob").jqGrid('getGridParam','page');
			$("#jqGridDependJob").jqGrid('setGridParam', {
				postData: {
					'dep_etlSystem' : vm.job.etlSystem,
					'dep_etlJob' : vm.job.etlJob,
					'reqDenpsType' : true
				},
				url:baseURL + 'etl/job/list',
				page: page
			}).trigger("reloadGrid");
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam', {
				postData: {
					'dep_etlSystem': vm.q.dep_etlSystem,
					'dep_etlJob': vm.q.dep_etlJob,
					'reqDenpsType': vm.q.reqDenpsType,
					'etlJob': vm.q.etlJob,
					'etlSystem':vm.q.etlSystem,
					'lastTxDateStart':vm.q.lastTxDateStart,
					'lastTxDateEnd':vm.q.lastTxDateEnd,
					'lastJobStatus':vm.q.lastJobStatus
				},
				page: page
			}).trigger("reloadGrid");
			vm.q.reqDenpsType=null;
			vm.q.dep_etlSystem=null;
			vm.q.dep_etlJob=null;
			// vm.q.etlSystem=null;
			// vm.q.etlJob=null;
			// vm.q.lastTxDate=null;
		},
		reBack: function () {
			vm.showList = true;
			vm.showQueryData = false;
		},
		selectDependJobs:function(){
			layer.open({
				type: 1,
				offset: '50px',
				skin: 'layui-layer-molv',
				title: "选择依赖作业",
				area: ['1000px', '450px'],
				shade: 0,
				shadeClose: false,
				content: jQuery("#selectJobLayer"),
				btn: ['确定', '取消'],
				btn1: function (index) {
//					var node = dept_ztree.getSelectedNodes();
//					//选择上级部门
//					vm.role.deptId = node[0].deptId;
//					vm.role.deptName = node[0].name;
//					layer.close(index);
				}
			});
		},

		//初始化服务器
		loadserver:function(){
			$.ajax({
				type: "GET",
				url: baseURL + "etl/server/getService",
				contentType: "application/json",
				success: function(data){
					if(data.code == 0){
						vm.allServer = data.allServers;
						window.setTimeout(function(){
							$('#etlServerAddSelect').selectpicker('refresh');
						},500); 
					}else{
						alert(data.msg);
					}
				}

			});
		},
		//初始化执行脚本
		loadDoScripts:function(){
			$.ajax({
				type: "GET",
				url: baseURL + "etl/script/getScripts",
				contentType: "application/json",
				success: function(data){
					if(data.code == 0){
						vm.doScripts = data.doScripts;
						window.setTimeout(function(){
							$('#publicScriptid').selectpicker('refresh');
						},500); 
					}else{
						alert(data.msg);
					}
				}

			});
		},
		initSelectJob:function(){
			$("#jqGridJob").jqGrid({
				url: baseURL + 'etl/job/list',
				datatype: "json",
				colModel: [
				           { label: '序号', name: 'id', index: 'ID', width: 120, key: true,hidden:true},
				           { label: '作业系统', name: 'etlSystem', index: 'ETL_System'},
				           { label: '作业名称', name: 'etlJob', index: 'ETL_Job' },
				           { label: '服务器', name: 'etlServer', index: 'ETL_Server',hidden:true },
				           { label: '描述', name: 'description', index: 'Description' },
				           { label: '作业周期', name: 'jobtype', index: 'JobType', formatter: function (value, options, row) {
				        	   if(value == "D"){
				        		   return  '<span class="label label-primary">日作业</span>'
				        	   } else if (value === "M") {
				        		   return  '<span class="label label-info">月作业</span>'
				        	   } else {};
				           }},

				           { label: '作业周期隐藏', name: 'jobtypeHidde', hidden:true,formatter: function (value, options, row) {
				        	  return row["jobtype"];
				           }},
				           { label: '数据日期', name: 'lastTxdate', index: 'Last_TXDate', formatter:"date",editable:true },
				           { label: '依赖添加', name: 'id', index: 'ID',  width:80,formatter:function (value, options, row) {
				        	   return  '<a href="javacript:void(0);"  onclick="addDependJob('+"'"+value+"'"+');" style="text-decoration: underline;" >添加</a>';
				           } },
				           { label: '触发器添加', name: 'id', index: 'ID', width:90,formatter:function (value, options, row) {
				        	   return  '<a href="javacript:void(0);"  onclick="removeDependJob('+"'"+value+"'"+');" style="text-decoration: underline;" >添加</a>';
				           } },

				           ],
				           viewrecords: true,
				           height: 385,
				           rowNum: 1000,
				           /* rowList : [50,100,200],*/
				           rownumbers: true, 
				           rownumWidth: 25, 
				           width:950,
				           /*autowidth:true,*/
				           /*  multiselect: true,*/
				           pager: "#jqGridPagerJob",
				           jsonReader : {
				        	   root: "page.list",
				        	   page: "page.currPage",
				        	   total: "page.totalPage",
				        	   records: "page.totalCount"
				           },
				           prmNames : {
				        	   page:"page", 
				        	   rows:"limit", 
				        	   order: "order"
				           },
				           // cellEdit:true,

				           gridComplete:function(){
				        	   //隐藏grid底部滚动条
				        	   $("#jqGridJob").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
				           }
			});
		},
		initDependJob:function(){
			var data=null;
			var url="";
			$("#jqGridDependJob").jqGrid({
				url: url,
				postData:data,
				datatype: "json",
				colModel: [
				           { label: '序号', name: 'id', index: 'ID', width: 150, key: true,hidden:true},
				           { label: '作业系统', name: 'etlSystem', index: 'ETL_System', },
				           { label: '作业名称', name: 'etlJob', index: 'ETL_Job',   },
				           { label: '服务器', name: 'etlServer', index: 'ETL_Server', hidden:true },
				           { label: '描述', name: 'description', index: 'Description', },
				           { label: '作业周期', name: 'jobtype', index: 'JobType', formatter: function (value, options, row) {
				        	   if(value == "D"){
				        		   return  '<span class="label label-primary">日作业</span>'
				        	   } else if (value === "M") {
				        		   return  '<span class="label label-info">月作业</span>'
				        	   } else {};
				           }},
				           { label: '触发作业', name: 'jobTrigger', index: 'jobTrigger', formatter: function (value, options, row) {
				        	   return  '<span class="label label-primary">是</span>'
				           }},
				           ],
				           viewrecords: true,
				           height: 300,
				           rowNum: 10000,
				           rownumbers: true, 
				           rownumWidth: 25, 
				           width:950,
				           multiselect: true,
				           pager: "#jqGridPagerDependJob",
				           jsonReader : {
				        	   root: "page.list",
				        	   page: "page.currPage",
				        	   total: "page.totalPage",
				        	   records: "page.totalCount"
				           },
				           prmNames : {
				        	   page:"page", 
				        	   rows:"limit", 
				        	   order: "order"
				           },
				           gridComplete:function(){
				        	   //隐藏grid底部滚动条
				        	   $("#jqGridDependJob").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
				           }
			});
		}
	},


});

//导入脚本
var ajaxupload=null;
function uploadScript() {
	var URL=baseURL+"etl/script/upload";
	ajaxupload=new AjaxUpload('#upload', {
		action : URL, // url自己写
		name : 'file',
		autoSubmit : false,
		responseType : "json",
		onSubmit : function(file, extension) {
			//alert(file);
			if (!(extension && /^(hql|HQL)$/.test(extension.toLowerCase()))) {
				alert("脚本必须是hql类型");
				return false;
			}
			this.setData({"filepath":vm.script.filepath});
		},
		onComplete : function(file, m) {
			if(m.code==1){
				alert("脚本已存在");
			}else{
				vm.doSaveOrUpdate();
			}
		},
		onChange:function(file, extension){
			if (!(extension && /^(hql|HQL)$/.test(extension.toLowerCase()))) {
				alert("脚本必须是hql类型");
				return false;
			}
			vm.isChange=true;
			vm.job.etlJob=file;
			//vm.script.scripttype=extension;
		}
	});
}
//添加依赖作业
function addDependJob(id){
	if(vm.allDepend[id]==null || vm.allDepend[id]==undefined || vm.allDepend[id]==""){
		var rowdata = $("#jqGridJob").jqGrid('getRowData',id);
		rowdata["jobtype"]=rowdata["jobtypeHidde"]
		vm.allDepend[id]=rowdata;
		$("#jqGridDependJob").jqGrid('addRowData',id,rowdata,"last");
	}
	console.log(vm.allDepend);
}
//删除依赖作业
function removeDependJob(id){
	if(vm.allDepend[id]!=null && vm.allDepend[id]!=undefined && vm.allDepend[id]!=""){
		delete vm.allDepend[id];
	}
	console.log(vm.allDepend);
}
//添加触发作业
function addTriggerJob(id){
	vm.triggerJob=id;
	console.log(vm.triggerJob);
}


