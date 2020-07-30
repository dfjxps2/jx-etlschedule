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
	// var index = script.lastIndexOf(".");
	// var end = script.substring(index, script.length);
	// vm.job.runningscript = vm.job.etlJob.toLocaleLowerCase() + "0100" + end;
	vm.job.runningscript = script;
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




