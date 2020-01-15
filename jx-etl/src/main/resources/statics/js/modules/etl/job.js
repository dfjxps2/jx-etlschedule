$(function () {

	//初始化系统下拉列表
	vm.loadsys();
	//状态下拉列表
	vm.loadstatus("etlStatusSelect");
	//初始化导入
	uploadScript();
	//初始化服务器
	vm.loadserver();
	//初始化执行脚本
	vm.loadDoScripts();
	//初始化月列表
	vm.loaddate();

	new AjaxUpload('#uploadxls', {
		action: baseURL + "etl/job/upload",
		name: 'file',
		autoSubmit:true,
		responseType:"json",
		onSubmit:function(file, extension){
			if (!(extension && /^(xls|xlsx)$/.test(extension.toLowerCase()))){
				vm.$alert("只支持 xls/xlsx 格式的文件！", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}
			// if(file.size>1*1024*1024){
			// 	alert("超出允许文件大小!");
			// 	return false;
			// }
		},
		onComplete : function(file, r){
			if(r.code == 0){
				vm.$alert(r.msg, '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				// vm.reload();
			}else{
				vm.$alert(r.msg, '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
			}
		}
	});
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
			lastJobStatus:null,
			reqAllDeps:null
		},
		dependQ:{
			etlSystem:null,
			etlJob:null
		},
		showList: true,
		title: null,
		showQueryData:false,
		job: {preEtlJob:null},
		allsys:[],
		allServer:[],
		doScripts:[],
		allstatus:null,
		isChange:false,
		allDepend:{},
		joblogdetail:null,
		frequencyD:'',
		frequencyM:'',
		alldate:[],
		dataPage: {},
		dependJobsPage: {},
		allDependPage: {},
		multipleSelection:[],
		adMultipleSelection:[],
		selectJobLayer:false,
		showRadio:'0'
	},
	mounted(){
		this.query(true);
	},
	methods: {
		initPage(){
			this.dataPage = {
				list: [],
				currPage: 1,
				pageSize: 10,
				totalCount: 0
			}
		},
		initDjPage(){
			this.dependJobsPage = {
				list: [],
				currPage: 1,
				pageSize: 10,
				totalCount: 0
			}
		},
		initDependPage(){
			this.allDependPage = {
				list: [],
				currPage: 1,
				pageSize: 10,
				totalCount: 0
			}
		},
		query: function (init) {
			if (init) {
				this.initPage();
			}
			var param = {
				'dep_etlSystem': this.q.dep_etlSystem,
				'dep_etlJob': this.q.dep_etlJob,
				'reqDenpsType': this.q.reqDenpsType,
				'etlJob': this.q.etlJob,
				'etlSystem':this.q.etlSystem,
				'reqAllDeps': this.q.reqAllDeps,
				'lastTxDateStart':this.q.lastTxDateStart,
				'lastTxDateEnd':this.q.lastTxDateEnd,
				'lastJobStatus':this.q.lastJobStatus,
				'page':this.dataPage.currPage,
				'limit':this.dataPage.pageSize,
			}
			var url = "etl/job/list";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				dataType:'json',
				data: param,
				success: function(r){
					if(r.code === 0){
						vm.dataPage = r.page;
					}
				}
			});
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.job = {
				etlJob:"",
				runningscript:null,
				allDependSave:[],
				preEtlJob:null,
				lastJobstatus:'Ready',
			};
			vm.frequencyD = '';
			vm.frequencyM = '';
			vm.initDependPage();
			vm.isChange=false;
			/*vm.doScripts=[];*/
			//清空依赖作业列表
			$("#jqGridDependJob").jqGrid('clearGridData');
			//初始化新增作业状态
			// vm.job.lastJobstatus = 'Ready';
		},
		update: function (event) {
			if(vm.multipleSelection.length == 0){
				vm.$alert("请选择一条记录", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var id = vm.multipleSelection[0].id;
			vm.showList = false;
			vm.title = "修改";
			vm.initDependPage();
			vm.frequencyD = '';
			vm.frequencyM = '';
			vm.getInfo(id);
		},
		saveOrUpdate: function (event) {
			vm.job.allDependSave=[];
			vm.allDependPage.list.forEach(function (item) {
				vm.job.allDependSave.push(item.id)
				//获取触发作业
				if(item.isTriggerJob != null && item.isTriggerJob != '' &&  item.isTriggerJob != undefined ){
					vm.job.triggerJob=item.id;
				}
			})
			//校验保存数据
			if(!vm.verifyParams()){
				return ;
			}
			//是否有上传文件
			if(vm.isChange==true){
				if(vm.fileIsExists()){
					vm.$alert("脚本已经存在", '系统提示', {
						confirmButtonText: '确定',
						callback: action => {
						}
					});
				}
				ajaxupload.submit();
			}else if(vm.job.id != null  && vm.isChange==false){
				vm.doSaveOrUpdate();
			}else if(vm.job.id == null  && vm.isChange==false){
				vm.doSaveOrUpdate();
			}
		},
		fileIsExists:function(){
			var isExists=false;
			$.ajax({
				type: "get",
				url: baseURL + "etl/job/fileIsExists/"+vm.job.etlJobAllNm,
				async: false,
				success: function(r){
					isExists=(r.code==1?true:false);
				}
			});
			return isExists;
		},
		verifyParams:function(){
			if(isBlank(vm.job.etlServer)){
				vm.$alert("服务器名称不能为空", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}else if(isBlank(vm.job.etlSystem)){
				vm.$alert("系统名称不能为空", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}else if(isBlank(vm.job.etlJob)){
				vm.$alert("作业不能为空", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}else if(isBlank(vm.frequencyD)){
				vm.$alert("周期类型不能为空", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}else if(isBlank(vm.job.publicScript)){
				vm.$alert("公共脚本不能为空", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}else if(isBlank(vm.job.enable)){
				vm.$alert("是否生效不能为空", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}
			else if(isBlank(vm.job.lastJobstatus)){
				vm.$alert("作业状态不能为空", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}
			else if(!isBlank(vm.job.allDependSave) && isBlank(vm.job.triggerJob)){
				vm.$alert("触发作业不能为空", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}

			//验证job
			var isExists=true;
			$.ajax({
				type: "POST",
				url: baseURL +'etl/job/jobIsExists',
				contentType: "application/json",
				data:JSON.stringify(vm.job),
				async: false,
				success: function(r){
					isExists=(r.code==1?false:true);
				}
			});
			if(!isExists){
				vm.$alert("作业已经存在", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
			}
			return isExists;
		},
		doSaveOrUpdate:function(){
			if (vm.frequencyD == '0') {
				vm.job.frequency = vm.frequencyD;
			} else {
				vm.job.frequency = vm.frequencyM;
			}
			var url = vm.job.id == null ? "etl/job/save" : "etl/job/update";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				contentType: "application/json",
				data: JSON.stringify(vm.job),
				success: function(r){
					if(r.code === 0){
						vm.$message({
							message: '操作成功',
							type: 'success'
						});
						vm.reBack();
						vm.query();
						vm.isChange==false
					}else{
						vm.$alert(r.msg, '系统提示', {
							confirmButtonText: '确定',
							callback: action => {
							}
						});
					}
				}
			});
		},
		expconfig:function(){
			$.ajax({
				type: "GET",
				url: baseURL + "etl/job/expconfig",
				contentType: "application/json",
				success: function(data){
					if(data.code === 0){
						location.href = baseURL+"etl/job/dloadconfig?cfgName="+data.msg ;
					}else{
						vm.$alert('作业配置导出失败', '系统提示', {
							confirmButtonText: '确定',
							callback: action => {
							}
						});
					}
				}
			});
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
						},1000);
					}else{
						vm.$alert(r.msg, '系统提示', {
							confirmButtonText: '确定',
							callback: action => {
							}
						});
					}
				}

			});
		},
		//初始化查询列表状态,新增job状态,修改job状态
		loadstatus: function(){
			// alert("loadstatus 被调用");
			$.ajax({
				type: "GET",
				url: baseURL + "etl/job/getstatus",
				contentType: "application/json",
				success: function(data){
					if(data.code == 0){
						vm.allstatus = data.allstatus;
						window.setTimeout(function(){
							$('#etlStatusSelect').selectpicker('refresh');
							$('#etlStatusAddSelectId').selectpicker('refresh');
						},1000);
					}else{
						vm.$alert(r.msg, '系统提示', {
							confirmButtonText: '确定',
							callback: action => {
							}
						});
					}
				}

			});
		},
		loaddate:function(){
			var ret = [{'value':'-1','text':'月末'}];
			for(var i = 1;i<=28;i++){
				ret.push({'value':i + '', 'text':i+'号'});
			}
			vm.alldate = ret;
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
							if ($("#jqGrid").getGridParam("reccount") == ids.length) {
								$("#jqGrid").jqGrid('setGridParam',{
									page:1
								})
							}
							$("#jqGrid").trigger("reloadGrid");
							vm.$message({
								message: '操作成功',
								type: 'success'
							});
						}else{
							vm.$alert(r.msg, '系统提示', {
								confirmButtonText: '确定',
								callback: action => {
								}
							});
						}
					}
				});
			});
		},
		getInfo: function(etlJob){
			$.get(baseURL + "etl/job/info/"+etlJob, function(r){
				vm.job = r.job;
				console.log(vm.job)
				vm.reloadDependJobs();
				$('#etlServerAddSelect').selectpicker('val', vm.job.etlServer);
				$('#etlSystemAddSelect').selectpicker('val', vm.job.etlSystem);
				$('#publicScriptid').selectpicker('val', vm.job.publicScript);
				$('#frequencyid').selectpicker('val', vm.job.frequency == '0' ? '0' : '-1');

				if(vm.job.frequency != '0'){
					$("#freqbox1").show();
					vm.frequencyM = vm.job.frequency
					vm.frequencyD = '-1'
					console.log('vm.job.frequency2',vm.job.frequency2)
				}else{
					vm.frequencyD = '0'
					$("#freqbox1").hide();
				}
				$('#enableid').selectpicker('val', vm.job.enable);
				$('#etlStatusAddSelectId').prop('disabled', false);
				$('#etlStatusAddSelectId').selectpicker('refresh');
				$('#etlStatusAddSelectId').selectpicker('val', vm.job.lastJobstatus);
				vm.job.preEtlJob=vm.job.etlJob;
			});
		},

		dependency: function(){
			if(vm.multipleSelection.length == 0){
				vm.$alert("请选择一条记录", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			if(vm.multipleSelection.length  > 1){
				vm.$alert("只能选择一条记录", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var rowdata = vm.multipleSelection[0]
			vm.q.dep_etlSystem = rowdata.etlSystem;
			vm.q.dep_etlJob = rowdata.etlJob;
			vm.q.reqDenpsType = true;
			vm.q.reqAllDeps = false;

			vm.query(true);
		},
		alldependency: function(){
			if(vm.multipleSelection.length == 0){
				vm.$alert("请选择一条记录", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			if(vm.multipleSelection.length  > 1){
				vm.$alert("只能选择一条记录", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var rowdata = vm.multipleSelection[0]
			vm.q.dep_etlSystem = rowdata.etlSystem;
			vm.q.dep_etlJob = rowdata.etlJob;
			vm.q.reqDenpsType = true;
			vm.q.reqAllDeps = true;

			vm.query(true);
			vm.q.reqDenpsType=null;
			vm.q.dep_etlSystem=null;
			vm.q.dep_etlJob=null;
			vm.q.reqAllDeps = null;
		},
		reloadDependJobsSelect:function(init){
			if (init) {
				this.initDjPage();
			}
			var param = {
				'etlSystem': vm.dependQ.etlSystem,
				'etlJob': vm.dependQ.etlJob,
				'enable':"1",
				'etlJobNow':vm.job.etlJob,
				'page':vm.dependJobsPage.currPage,
				'limit':vm.dependJobsPage.pageSize,
			}
			var url = "etl/job/list";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				dataType:'json',
				data: param,
				success: function(r){
					if(r.code === 0){
						vm.dependJobsPage = r.page;
					}
				}
			});
		},
		reloadDependJobs:function(){
			var param = {
				'dep_etlSystem' : vm.job.etlSystem,
				'dep_etlJob' : vm.job.etlJob,
				'reqDenpsType' : true,
				'page':vm.allDependPage.currPage,
				'limit':vm.allDependPage.pageSize,
			}
			var url = "etl/job/list";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				dataType:'json',
				data: param,
				success: function(r){
					if(r.code === 0){
						vm.allDependPage = r.page;
					}
				}
			});
		},
		handleSizeChange(val) {
			vm.dataPage.pageSize = val
			vm.dataPage.currPage = 1;
			vm.query();
		},
		handleCurrentChange(val) {
			vm.dataPage.currPage = val;
			vm.query();
		},
		handleSelectionChange(val) {
			vm.multipleSelection = val;
		},
		colIndex(row, column, cellValue, index) {
			return (vm.dataPage.currPage - 1) * vm.dataPage.pageSize + index + 1
		},
		adHandleSizeChange(val) {
			vm.allDependPage.pageSize = val
			vm.allDependPage.currPage = 1;
			vm.reloadDependJobs();
		},
		adHandleCurrentChange(val) {
			vm.allDependPage.currPage = val;
			vm.reloadDependJobs();
		},
		adHandleSelectionChange(val) {
			vm.adMultipleSelection = val;
		},
		adColIndex(row, column, cellValue, index) {
			return (vm.allDependPage.currPage - 1) * vm.allDependPage.pageSize + index + 1
		},
		djHandleSizeChange(val) {
			vm.dependJobsPage.pageSize = val
			vm.dependJobsPage.currPage = 1;
			vm.reloadDependJobsSelect();
		},
		djHandleCurrentChange(val) {
			vm.dependJobsPage.currPage = val;
			vm.reloadDependJobsSelect();
		},
		djColIndex(row, column, cellValue, index) {
			return (vm.dependJobsPage.currPage - 1) * vm.dependJobsPage.pageSize + index + 1
		},
		reBack: function () {
			vm.showList = true;
		},
		selectDependJobs:function(){
			vm.selectJobLayer = true;
			//初始化弹出框job
			vm.reloadDependJobsSelect(true);
			vm.dependQ={
				etlSystem:null,
				etlJob:null
			};

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
						},1000);
					}else{
						vm.$alert(data.msg, '系统提示', {
							confirmButtonText: '确定',
							callback: action => {
							}
						});;
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
						},1000);
					}else{
						vm.$alert(data.msg, '系统提示', {
							confirmButtonText: '确定',
							callback: action => {
							}
						});
					}
				}

			});
		},
		//血缘影响分析
		analysis:function(){
			if(vm.multipleSelection.length == 0){
				vm.$alert("请选择一条记录", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			if(vm.multipleSelection.length  > 1){
				vm.$alert("只能选择一条记录", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			vm.showRadio = '0'
			var rowdata = vm.multipleSelection[0]
			var querydata = {
				limit:10000,
				'dep_etlSystem': rowdata.etlSystem,
				'dep_etlJob': rowdata.etlJob,
				'reqDenpsType': true,
				'etlJob': vm.q.etlJob,
				'etlSystem':vm.q.etlSystem,
				'reqAllDeps': true,
				'lastTxDateStart':vm.q.lastTxDateStart,
				'lastTxDateEnd':vm.q.lastTxDateEnd,
				'lastJobStatus':vm.q.lastJobStatus
			};
			$.ajax({
				type: "GET",
				url: baseURL + 'etl/job/analysis',
				contentType: "application/json",
				data:querydata,
				success: function(data){
					if(data.code != 0){
						vm.$alert(data.msg, '系统提示', {
							confirmButtonText: '确定',
							callback: action => {
							}
						});
						return;
					}
					$(".grid-btn").parent().hide();
					$("#analysisLayer").show();
					topoChart(data.data, querydata);
				}
			});
		},
		//打开重跑
		openRerun:function(){
			vm.showQueryData = true;
			vm.title = "重跑作业 [" + _esys + "." + _ejob + "]";
			var d = lastDate();
			vm.q.rerun_data_date = d;
			$("#rerundateid").find("input").val(d);
		},
		//重跑单个任务
		rerun: function(){
			var jobid = _jobid;
			if(vm.q.rerun_data_date ==null){
				vm.$alert('请选择数据重跑日期', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			} else {
				var lastTxDate = vm.q.rerun_data_date;
				var url = "etl/job/rerunmulti";
				$.ajax({
					type: "GET",
					url: baseURL + url,
					contentType: "application/json",
					data: {'rerunjobids':jobid,'lastTxDate':lastTxDate},
					// data: 'etlSystem=' + etlSystem + '&etlJob=' + etlJob + '&lastTxDate=' + lastTxDate,
					success: function(r){
						vm.q.rerun_data_date =null;
						if(r.code === 0){
							vm.$alert(r.msg, '系统提示', {
								confirmButtonText: '确定',
								callback: action => {
									vm.reBack();
									clearTimeout(_tfn);
									setTimeout(auto_refresh, 2000);
								}
							});
						}else{
							vm.$alert(r.msg, '系统提示', {
								confirmButtonText: '确定',
								callback: action => {
								}
							});
						}
					}
				});
			}
			return;
		},
		reBack2: function () {
			vm.showQueryData = false;
		},
		back:function(){
			drawChart = drawChart1;
			is_auto = false;
			clearTimeout(_tfn);
			clearState();
			$("#analysisLayer").hide();
			$(".grid-btn").parent().show();
			window.dispatchEvent(new Event('resize'));
		},
		//添加loadlog方法加载作业日志详情
		loadlog: function(){
			$.ajax({
				type: "GET",
				url: baseURL + "etl/joblog/loadLast",
				contentType: "application/json",
				data: {'etlSystem':_esys,'etlJob':_ejob},
				success: function(data){
					$('#myModal').modal('show');
					if(data.code == 0){
						$("#joblogdetail").html(data.logresult);
						// vm.joblogdetail = data.logresult;
					}else{
						$("#joblogdetail").html("该日志不存在或已被清理");
					}
				}

			});
		},
		//添加日志下载
		logdload:function(){
			console.info('--- logdload  ---')
			$.ajax({
				type: "GET",
				url: baseURL + "etl/joblog/loadLast",
				contentType: "application/json",
				data: {'etlSystem':_esys,'etlJob':_ejob},
				success: function(data){
					if(data.code == 0){
						location.href = baseURL+"etl/joblog/downloadLast?etlSystem="+_esys+"&etlJob=" + _ejob;
					}else{
						vm.$alert(data.msg, '系统提示', {
							confirmButtonText: '确定',
							callback: action => {
							}
						});
					}
				}

			});
		},
		//关闭日志
		clearlog: function(){
			vm.joblogdetail = null;
		},

		batchconfig: function () {
			$.ajax({
				type: "GET",
				url: baseURL + 'etl/job/batchconfig',
				contentType: "application/json",

				success: function(data){
					if(data.code === 0){
						vm.$alert(data.msg, '系统提示', {
							confirmButtonText: '确定',
							callback: action => {
							}
						});
						vm.reload();
					} else {
						vm.$alert(data.msg, '系统提示', {
							confirmButtonText: '确定',
							callback: action => {
							}
						});
						return;
					}


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
		cache:false,
		onSubmit : function(file, extension) {
			if (!(extension && /^(hql|HQL)$/.test(extension.toLowerCase()))) {
				vm.$alert("脚本必须是hql类型", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}
			this.setData({"uppercase":"1"});
		},
		onComplete : function(file, m) {
			if(m.code==1){
				vm.$alert("脚本已存在", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
			}else{
				vm.doSaveOrUpdate();
			}
		},
		onChange:function(file, extension){
			if (!(extension && /^(hql|HQL)$/.test(extension.toLowerCase()))) {
				vm.$alert("脚本必须是hql类型", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}
			var lastIndex=file.lastIndexOf(".");
			var etlJob=file.substr(0,lastIndex).toLocaleUpperCase();
			vm.job.etlJob=etlJob;
			vm.job.etlJobAllNm=file;
			vm.job.preEtlJob=etlJob;
			setDoScript(file);
			vm.isChange=true;
		}
	});
}

function setDoScript(){
	if(vm.job.preEtlJob!=vm.job.etlJob){
		vm.job.etlJobAllNm=null;
		vm.isChange=false;
		vm.job.etlJob=vm.job.etlJob.toLocaleUpperCase();
	}
	if (!vm.job.publicScript) {
		return;
	}
	var script = vm.doScripts.find(x=>{if(x.code == vm.job.publicScript) return x}).name
	// var script = $("#publicScriptid").find("option:selected").text();
	console.log('script=',script)
	var index = script.lastIndexOf(".");
	var end = script.substring(index, script.length);
	vm.job.runningscript = vm.job.etlJob.toLocaleLowerCase() + "0100" + end;
	if (isBlank(script)  || isBlank(vm.job.etlJob)) {
		vm.job.runningscript = '';
	}
}
//添加依赖作业
function addDependJob(row){
	var item = vm.allDependPage.list.find(x=>{if(x.id == row.id)return x});
	if (!item) {
		vm.allDependPage.list.push(row)
		vm.allDependPage.totalCount = vm.allDependPage.totalCount + 1;
	}
}
function hadDepend(id) {
	var item = vm.allDependPage.list.find(x=>{if(id == x.id)return x});
	console.log('item',item)
	if (item) {
		return true;
	} else {
		return false;
	}

}
//删除依赖作业
function removeDependJob(){
	if(vm.adMultipleSelection.length == 0){
		vm.$alert('请选择一条记录', '系统提示', {
			confirmButtonText: '确定',
			callback: action => {
			}
		});
		return ;
	}
	vm.adMultipleSelection.forEach(function (item) {
		console.log(item)
		for (var index = 0; index<vm.allDependPage.list.length; index ++){
			console.log(vm.allDependPage.list[index])
			if (vm.allDependPage.list[index].id == item.id) {
				vm.allDependPage.list.splice(index, 1);
				if (item.isTriggerJob == '1') {
					vm.job.triggerJob=null;
				}
				break;
			}
		}

	})
}
//添加触发作业
function addTriggerJob(){
	if(vm.adMultipleSelection.length == 0){
		vm.$alert("请选择一条记录", '系统提示', {
			confirmButtonText: '确定',
			callback: action => {
			}
		});
		return ;
	}
	if(vm.adMultipleSelection.length > 1){
		vm.$alert("只能选择一条记录", '系统提示', {
			confirmButtonText: '确定',
			callback: action => {
			}
		});
		return ;
	}
	var id = vm.adMultipleSelection[0].id;
	vm.allDependPage.list.forEach(function (item) {
		if (item.id == id) {
			item.isTriggerJob = 1;
		} else {
			item.isTriggerJob = 0;
		}
	})
	vm.job.triggerJob=id;
}
//血缘影响分析图
var reqData = [],allData = {}, myChart = null, _option = {}, _ejob = '', _esys = '', _jobid='', _queryPrm = {}, _box = {}, is_auto = true, _ts = 20000, _tfn = 0, _nMap = {}, _nGroup = {}, _lv = 0;
function topoChart(data, prm){
	reqData = data;
	_queryPrm = prm;
	is_auto = true;
	var uploadedDataURL = baseURL + "/statics/js/modules/etl/job-analysis/data-1479697763933-ByhDrJlGx.json";
	_box = initSize(data);
	$.get(uploadedDataURL, function (geoJson) {
		echarts.registerMap('wuhan', geoJson);

		drawChart(data, _box);
		setTimeout(auto_refresh, _ts);//添加定时
	});
}
function auto_refresh(){
	$.ajax({
		type: "GET",
		url: baseURL + 'etl/job/analysis',
		contentType: "application/json",
		data:_queryPrm,
		success: function(data){
			if(data.code != 0){
				return;
			}
			console.log('time elsape...');
			reqData = data.data;
			drawChart(reqData, _box);
		},complete:function(){
			if(is_auto){
				_tfn = setTimeout(auto_refresh, _ts);
			}
		}
	});
}
//设置画布大小
function initSize(data){
	console.info('initSize')
	var w = $("#analysisLayer").width(), h = $("#analysisLayer").height() - 40, lr = 0, lc = 0, lr0 = 2, xx = 160, yy = 100, yy0 = 80;
	var sta = 0, end = 0, i = 0;
	for(var n in data) {
		if(data[n].length > 0)
			lr++;
		else
			lr0++;
		lc = data[n].length > lc ? data[n].length : lc;
		if(i==0)
			end = n;
		else
			sta = n;
		i++;
	}
	if(lr>=4){
		h = h + yy * (lr - 5) + lr0 * yy0;
	}
	if(lc>6){
		w = w + xx * (lc - 5);
	}
	$("#analysisChart").width(w).height(h);
	return {w:w,h:h, sta:parseInt(sta), end:parseInt(end)};
}
function initData1(maps){
	//xy=[112.25, 30.25], xx = -0.2, yy=0.5
	var nodes = [], lines = [], xy = [110, 100], yy = 100, xx = 120, a = 0, b = 0;
	var imgURL = baseURL + "/statics/js/modules/etl/job-analysis/";
	var lvmap = ["0","-1","1"];
	for(var j in maps){
		var data = maps[j];
		if(data.length == 0)
			continue;
		if(_lv>0 && lvmap.indexOf(j)==-1)
			break;
		var y0 = xy[1] + (a++)*yy;
		b = 0;
		_nMap[j] = 0;
		data.forEach(function(o){
			if(hasNode(o, nodes))
				return true;
			_nMap[j]++;
			o.Status = checkStatus(o);
			var node = {
				"name": o.dependencySystem + '.' + o.dependencyJob
				,"value":[xy[0] + (b++)*xx,y0,20]
				,symbol: 'image://' + imgURL + 'svr_' + o.Status + '.png'
				,symbolSize:40
				,itemStyle:{}
				,info:o
			};
			nodes.push(node);
		});
	}
	for(var j in maps){
		if(_lv>0 && lvmap.indexOf(j)==-1)
			break;
		maps[j].forEach(function(o){
			var n1 = o.dependencySystem + '.' + o.dependencyJob, n2 = o.etlSystem + '.' + o.etlJob;
			if(hasLine(lines, n1, n2))
				return true;
			var line = {
				"fromName":n1,"toName":n2
				,"coords":getCoords(n1,n2, nodes, xy)
				,lineStyle:{}
			};
			lines.push(line);
		});
	}
	return {
		nodes:nodes,
		lines:lines
	};
}
function initData2(maps){
	var width = $("#analysisChart").width(), height = $("#analysisChart").height();
	var nodes = [], lines = [],ids = {}, mapc = 0, xy = [100, 0], yy = 100, xx = 160, a = 0, b = 0;
	var imgURL = baseURL + "/statics/js/modules/etl/job-analysis/";
	var lvmap = ["0","-1","1"];
	for(var j in maps){
		var data = maps[j];
		if(data.length == 0)
			continue;
		if(_lv>0 && lvmap.indexOf(j)==-1)
			break;
		var x0 = ++a;
		b = 1;
		_nMap[j] = 0;
		data.forEach(function(o){
			if(hasNode(o, nodes))
				return true;
			_nMap[j]++;
			o.Status = checkStatus(o);
			var node = {
				"name": o.dependencySystem + '.' + o.dependencyJob
				,"value":[x0, b++,20]
				,symbol: 'image://' + imgURL + 'svr_' + o.Status + '.png'
				,symbolSize:40
				,itemStyle:{}
				,info:o
			};
			nodes.push(node);
			ids[o.id] = j;
		});
		if(_nMap[j]>0)
			mapc++;
	}
	//计算各节点位置
	nodes.forEach(function(o){
		var i = o.info.id, pos = o.value, cnt = _nMap[ids[i]], step = height / (cnt + 1);
		var x0 = xy[0] + (mapc - pos[0])*xx;
		var y0 = xy[1] + pos[1]*step;
		o.value = [x0, y0];
	});

	for(var j in maps){
		if(_lv>0 && lvmap.indexOf(j)==-1)
			break;
		maps[j].forEach(function(o){
			var n1 = o.dependencySystem + '.' + o.dependencyJob, n2 = o.etlSystem + '.' + o.etlJob;
			if(hasLine(lines, n1, n2))
				return true;
			var cds = getCoords(n1,n2, nodes, xy);
			//横坐标不等时拐点
			if(cds[0][0] != cds[1][0]){
				var xm = (cds[0][0] + cds[1][0]) / 2;
				cds = [cds[0],[xm, cds[0][1]],[xm, cds[1][1]],cds[1]];
			}
			var line = {
				"fromName":n1,"toName":n2
				,"coords":cds
				,lineStyle:{}
			};
			lines.push(line);
		});
	}
	return {
		nodes:nodes,
		lines:lines
	};
}
function setLayer(lv){
	_lv = lv;
	drawChart(reqData, _box);
}
function hasNode(o, arr){
	for(var i = 0; i < arr.length; i++){
		if(arr[i].info.id == o.id)
			return true;
	}
	return false;
}
function hasLine(arr, n1, n2){
	for(var i = 0; i < arr.length; i++){
		if(arr[i].fromName == n1 && arr[i].toName == n2)
			return true;
	}
	return false;
}
function getCoords(n1,n2, nodes, df){
	var crds = [df, df], l = nodes.length;
	for(var i = 0; i < l; i++){
		var n = nodes[i];
		if(n.name == n1){
			crds[0] = [n.value[0], n.value[1]];
			break;
		}
	}
	for(var i = 0; i < l; i++){
		var n = nodes[i];
		if(n.name == n2){
			crds[1] = [n.value[0], n.value[1]];
			break;
		}
	}
	return crds;
}
function checkStatus(o){
	var s = o.lastJobstatus;
	var d1 = lastDate();
	var d2 = o.lastTxdate;
	if(d1 != d2)
		s = 'Stop';
	return s;
}
function lastDate(){
	var dd = new Date();
	dd.setTime(dd.getTime()-24*60*60*1000);
	return dd.getFullYear() + '-' + fix0(dd.getMonth()+1) + '-' + fix0(dd.getDate());
}
function fix0(n){
	var s = '0' + n;
	return s.substr(s.length - 2, 2);
}
function drawMenu(myChart){
	var style_ul = "padding:0px;margin:0px;border: 1px solid #ccc;background-color: #fff;position: absolute;left: 0px;top: 0px;z-index: 2;display: none;";
	var style_li = "list-style:none;padding: 5px; cursor: pointer; padding: 5px 20px;margin:0px;";
	var style_li_hover = style_li + "background-color: #00A0E9; color: #fff;";
	var menubox = null;
	if($("#echartboxMenu").size() == 0){
		menubox = $("<div id='echartboxMenu' class='echartboxMenu' style='" + style_ul + "'><ul style='margin:0px;padding:0px;'></ul></div>")
			.appendTo($(document.body));
		$(document).click(function() {
			menubox.hide();
		});
	}else{
		menubox = $("#echartboxMenu");
	}

	myChart.getDom().oncontextmenu = menubox[0].oncontextmenu = function(){
		return false;
	}
	myChart.on("mousedown",function(e){
		if(e.event.event.button===2 && e.componentSubType == 'scatter'){
			_ejob = e.data.info.dependencyJob;
			_esys = e.data.info.dependencySystem;
			_jobid = e.data.info.id;
			showMenu([
				{
					"name": "重跑",
					"fn": function() {
						vm.openRerun();
					}
				},
				{
					"name":"查看日志",
					"fn":function(){
						vm.loadlog();
					}
				},
				{
					"name":"下载日志",
					"fn":function(){
						vm.logdload();
					}
				}
			]);
		}
	});

	var showMenu = function(menus){
		var menulistbox = $("ul", menubox).empty();
		$(menus).each(function(i, item) {
			var li = $("<li style='" + style_li + "'>" + item.name + "</li>")
				.mouseenter(function() {
					$(this).attr("style", style_li_hover);
				})
				.mouseleave(function() {
					$(this).attr("style", style_li);
				})
				.click(function() {
					item["fn"].call(this);
					menubox.hide();
				});
			menulistbox.append(li);
		});
		menubox.css({
			"left": event.x,
			"top": event.y
		}).show();
	}
}
function initOpt1(){
	return {
		backgroundColor: '#074883',
		color:['#074883','#020933'],
		title: [{
			text: '任务调度图',
			top: 0,
			left:10,
			textStyle: {
				color: '#fff'
			}
		}],
		toolbox:{
			left:150,
			itemSize:30,
			feature: {
				myLayout2:{
					show:true,
					title:'树型排列',
					icon:'image://'+baseURL+'/statics/js/modules/etl/job-analysis/treeico.png',
					onclick:function(){
						drawChart = drawChart2;
						drawChart(reqData, _box);
					}
				},
			}
		},
		tooltip:{
			trigger:'item',
			formatter:function(o){
				if(o.data == null || o.data.info == null)
					return o.name;
				var x = o.data.info;
				var str = x.dependencyJob + '<br />系统名称：' + x.dependencySystem + '<br />描述：' + x.description + '<br />任务状态：'+ x.lastJobstatus
					+ '<br />当前数据日期：'+ x.lastTxdate
				return str;
			}
		},
		legend: {
			show: false,
			orient: 'vertical',
			top: 'bottom',
			left: 'right',
			data: ['任务', '进度'],
			textStyle: {
				color: '#fff'
			}
		},
		grid: {
			width:'100%',
			height:'100%',
			left:0,
			right:0,
			bottom:0,
			top:0,
		},
		xAxis: {
			show: false,
		},
		geo: {
			map: 'wuhan1',
			label: {
				emphasis: {
					show: false
				}
			},
			roam: true,
			itemStyle: {
				normal: {
					color:'rgba(255,255,255,0)',
					areaColor:'rgba(255,255,255,0)',
					borderColor:'rgba(255,255,255,0)'
				},
				emphasis: {
					color:'rgba(22,22,2,0)',
					areaColor:'rgba(22,22,2,0)',
					borderColor:'rgba(22,22,2,0)'
				}
			}
		},
		series: [
			{
				name: '任务',
				type: 'scatter',
				coordinateSystem: 'geo',
				zlevel: 2,
				rippleEffect: {
					brushType: 'stroke',
					period:7,
					scale:26
				},
				label: {
					normal:{
						show:true,
						position:'bottom',
						formatter:function(o){
							if(o.name.length <= 12)
								return o.name;
							var s = '';
							for(var i = 0; i < o.name.length; i++){
								s+= o.name.charAt(i);
								if(i%12==0&&i>0)
									s+= '\n';
							}
							return s;
						},
						color:'white',
					},
					emphasis: {
						show: true,
						formatter: '{b}'
					}
				},
				symbolSize: 20,
				showEffectOn: 'render',
				itemStyle: {
					normal: {
						color: '#46bee9'
					}
				},
				data: allData.nodes
			},
			{
				name: '进度',
				type: 'lines',
				coordinateSystem: 'geo',
				zlevel: 1,
				large: true,
				effect: {
					show: true,
					period: 4,
					//constantSpeed: 30,
					//symbol: 'arrow',
					color: '#64f2ff',
					symbolSize: 4,
					trailLength: 0.5,
				},
				lineStyle: {
					normal: {
						color:'#13aae6',
						width: 1,
						opacity: 0.6,
						curveness: 0.1
					}
				},
				data: allData.lines
			}
		]
	};
}
function initOpt2(){
	return {
		backgroundColor: '#074883',
		color:['#074883','#020933'],
		title: [{
			text: '任务调度图',
			top: 0,
			left:10,
			textStyle: {
				color: '#fff'
			}
		}],
		toolbox:{
			left:150,
			itemSize:30,
			feature: {
				myLayout1:{
					show:true,
					title:'分层排列',
					icon:'image://'+baseURL+'/statics/js/modules/etl/job-analysis/netico.png',
					onclick:function(){
						drawChart = drawChart1;
						drawChart(reqData, _box);
					}
				},
			}
		},
		tooltip:{
			trigger:'item',
			formatter:function(o){
				if(o.data == null || o.data.info == null)
					return o.name;
				var x = o.data.info;
				var str = x.dependencyJob + '<br />系统名称：' + x.dependencySystem + '<br />描述：' + x.description + '<br />任务状态：'+ x.lastJobstatus
					+ '<br />当前数据日期：'+ x.lastTxdate
				return str;
			}
		},
		legend: {
			show: false,
			orient: 'vertical',
			top: 'bottom',
			left: 'right',
			data: ['任务', '进度'],
			textStyle: {
				color: '#fff'
			}
		},
		grid: {
			width:'100%',
			height:'100%',
			left:0,
			right:0,
			bottom:0,
			top:0,
		},
		xAxis: {
			show: false,
		},
		geo: {
			map: 'wuhan1',
			label: {
				emphasis: {
					show: false
				}
			},
			roam: true,
			itemStyle: {
				normal: {
					color:'rgba(255,255,255,0)',
					areaColor:'rgba(255,255,255,0)',
					borderColor:'rgba(255,255,255,0)'
				},
				emphasis: {
					color:'rgba(22,22,2,0)',
					areaColor:'rgba(22,22,2,0)',
					borderColor:'rgba(22,22,2,0)'
				}
			}
		},
		series: [
			{
				name: '任务',
				type: 'scatter',
				coordinateSystem: 'geo',
				zlevel: 2,
				rippleEffect: {
					brushType: 'stroke',
					period:7,
					scale:26
				},
				label: {
					normal:{
						show:true,
						position:'bottom',
						formatter:function(o){
							if(o.name.length <= 12)
								return o.name;
							return o.name.substr(0, 12) + '\n' + o.name.substring(12);
						},
						color:'white',
					},
					emphasis: {
						show: true,
						formatter: '{b}'
					}
				},
				symbolSize: 20,
				showEffectOn: 'render',
				itemStyle: {
					normal: {
						color: '#46bee9'
					}
				},
				data: allData.nodes
			},
			{
				name: '进度',
				type: 'lines',
				coordinateSystem: 'geo',
				zlevel: 1,
				large: false,
				polyline:true,
				effect: {
					show: true,
					period: 4,
					//constantSpeed: 30,
					//symbol: 'arrow',
					color: '#64f2ff',
					symbolSize: 4,
					trailLength: 0.5,
				},
				lineStyle: {
					normal: {
						color:'#13aae6',
						width: 1,
						opacity: 0.6,
						curveness: 0
					}
				},
				data: allData.lines
			}
		]
	};
}
function drawChart1(data, box){
	if(myChart != null){
		myChart.dispose();
		$("#analysisChart").html('');
	}
	myChart = echarts.init($('#analysisChart')[0]);
	allData = initData1(data);
	var bgObj = setBg(data);
	_option = initOpt1();
	appendBg(_option, bgObj, box);

	myChart.setOption(_option);
	drawMenu(myChart);
	if(allData.nodes.length > 4){
		focusNode(myChart,_option);
	}
}
function drawChart2(data, box){
	if(myChart != null){
		myChart.dispose();
		$("#analysisChart").html('');
	}
	myChart = echarts.init($('#analysisChart')[0]);

	allData = initData2(data);
	//var bgObj = setBg(data);
	_option = initOpt2();
	//appendBg(_option, bgObj, box);

	myChart.setOption(_option);
	drawMenu(myChart);
	if(allData.nodes.length > 4){
		focusNode(myChart,_option);
	}
}
var drawChart = drawChart1;
function setBg(data){
	var aStyle = [{color:'#f00'},{color:'#ff0'}];//#020933
	var ret = {min:0,max:0,s:[]},m = [], q = -1, curr = '', pre = '', h2 = 60, yy = 100, yy0 = 40;
	for(var n in data){
		if(data[n].length > 0 && (_nMap[n] && _nMap[n]>0)){
			h2 += yy;
			curr = data[n][0].dependencySystem;
			if(curr != pre){
				m[++q] = {
					'name':curr,
					'min':pre ? m[q-1].max : 0,
					'max':h2
				};
				pre = curr;
			}else{
				m[q].max = h2;
			}
		}
	}
	ret.max = h2;
	for(var n = 0; n < m.length; n++){
		var x = m[n];
		ret.s.push({
			name: x.name + n,
			xname: x.name,
			type: 'line',
			animation: false,
			tooltip:{show:false},
			areaStyle: aStyle[n%2],
			lineStyle: {
				normal: {
					width: 1
				}
			},
			markArea: {
				data: [
					[{
						yAxis: x.min + 1
					}, {
						yAxis: x.max
					}]
				]
			}
		});
	}
	return ret;
}
function appendBg(option, ret, box){
	var bl = box.h / ret.max;
	option.yAxis = {
		min: ret.min,
		max: box.h,
		inverse:true,
		//splitNumber: 10,
		show: false,
		axisLabel:{inside:true}
	};
	ret.s.forEach(function(o){
		option.series.push(o);
		var t1 = parseInt(((o.markArea.data[0][1].yAxis - o.markArea.data[0][0].yAxis) / 2 + o.markArea.data[0][0].yAxis));
		var txt = {
			text: o.xname,
			left:10,
			top:t1,
			textStyle:{
				color:'#e1e1e1',
				fontSize:10,
				fontStyle:'italic'
			}
		};
		option.title.push(txt);
	});
}
function focusNode(myChart, option){
	myChart.on("mouseover",function(prm){
		var name = prm.name;
		if(prm.componentSubType != "scatter")
			return;
		var gp = findGroup(name, option);
		setGroup(option, true, gp);
		myChart.setOption(option);

		_nGroup[name].groups.forEach(function(o){
			myChart.dispatchAction({
				type: 'highlight',
				name: o
			});
		});
	});
	myChart.on("mouseout",function(prm){
		var name = prm.name;
		if(prm.componentSubType != "scatter")
			return;
		setGroup(option, false, null);
		myChart.setOption(option);

		_nGroup[name].groups.forEach(function(o){
			myChart.dispatchAction({
				type: 'downplay',
				name: o
			});
		});
	});
}
function findGroup(name,option){
	if(_nGroup[name])
		return _nGroup[name];
	_nGroup[name] = {nodes:[],lines:[],groups:[]};
	var arr = _nGroup[name].groups;
	option.series[1].data.forEach(function(o, i){
		if(o.fromName == name){
			arr.push(o.toName);
			_nGroup[name].lines.push(i);
		}
		if(o.toName == name){
			arr.push(o.fromName);
			_nGroup[name].lines.push(i);
		}
	});
	option.series[0].data.forEach(function(o, i){
		if(o.name == name)
			_nGroup[name].nodes.push(i);
		else if(arr.indexOf(o.name)>-1)
			_nGroup[name].nodes.push(i);
	});
	return _nGroup[name];
}
function setGroup(option, ishide,g){
	var v1 = ishide ? 0.2 : 1;
	option.series[0].data.forEach(function(o){
		o.itemStyle.opacity = v1;
	});
	if(ishide){
		option.series[1].data.forEach(function(o){
			o.coords1 = o.coords;
			o.coords = [o.coords[0],o.coords[0]];
		});
	}else{
		option.series[1].data.forEach(function(o){
			o.coords = o.coords1;
		});
	}

	if(!g) return;
	g.nodes.forEach(function(i){
		option.series[0].data[i].itemStyle.opacity = 1;
	});
	g.lines.forEach(function(i){
		option.series[1].data[i].coords = option.series[1].data[i].coords1;
	});
}
function clearState(){
	_ejob = '';
	_esys = '';
	_jobid='';
	_box = {};
	_nMap = {};
	_nGroup = {};
}

