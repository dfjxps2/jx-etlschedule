$(function () {

	vm.loadsys("etlSystemSelect");
	vm.loadstatus("etlStatusSelect");
    vm.loadstatus("updateBatchStatusSelect");
	setTimeout(refreshjoblist,vm.refresh_freq);
	function refreshjoblist() {
		if(vm.refresh_auto)
			vm.query();
		console.log(new Date().toString());
		setTimeout(refreshjoblist,vm.refresh_freq);
    }
});



var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			dep_etlSystem : null,
			dep_job : null,
			reqDenpsType: null,
            reqAllDeps:null,
			etlJob : null,
			etlSystem:null,
			lastTxDateStart : null,
			lastTxDateEnd : null,
			rerun_data_date: null,
			lastJobStatus: null,
			newJobStatus: null,
            newJobTxdate: null,
            newEnableFlag: null,
		},
		schedule:{
			jobId:null,
			beanName:'jobReturnTask',
			methodName:'rerunMulti',
			remark:null,
			cronExpression:'',
			params:{},
		},
		triggerJob:{
			etlJob:null,
			lastTxDate:-1
		},
		dependQ:{},
		showList: true,
		showQueryData:false,
		showTaskData:false,
        showUpdateBatchStatus: false,
        showUpdateBatchJobTxDate: false,
        showUpdateBatchEnable: false,
		title: null,
		job: {},
		allsys:[],
		allstatus:null,
		refresh_freq:20000,
		refresh_auto:true,
		dataPage: {},
		multipleSelection:[],
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
		query: function (init) {
			if (this.q.lastTxDateStart > this.q.lastTxDateEnd) {
				this.$alert('结束时间不能小于开始时间', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}
			if (init) {
				this.initPage();
			}
			var postData = {
				'dep_etlSystem': this.q.dep_etlSystem,
				'dep_etlJob': this.q.dep_etlJob,
				'reqDenpsType': this.q.reqDenpsType,
				'reqAllDeps': this.q.reqAllDeps,
				'etlJob': this.q.etlJob,
				'etlSystem': this.q.etlSystem,
				'lastTxDateStart': this.q.lastTxDateStart,
				'lastTxDateEnd': this.q.lastTxDateEnd,
				'lastJobStatus': this.q.lastJobStatus,
				'page':this.dataPage.currPage,
				'limit':this.dataPage.pageSize,
			}
				var url = "etl/job/list";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				dataType:'json',
				data: postData,
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
			vm.job = {};
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
			if(vm.multipleSelection.length > 1){
				vm.$alert("只能选择一条记录", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var id = vm.multipleSelection[0].id;
			vm.showList = false;
			vm.title = "修改";

			vm.getInfo(id)
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
						vm.reBack();
						vm.query();
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
		},
		openTask:function(){
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			if(vm.multipleSelection.length > 1){
				vm.$alert('最多选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			vm.schedule = {
				jobId:null,
				beanName:'jobReturnTask',
				methodName:'rerunMulti',
				remark:null,
				cronExpression:'',
				params:{},
			}
			vm.triggerJob = {
				etlJob:null,
				lastTxDate:-1
			},
			$.get(baseURL + "etl/job/checktrigger/"+vm.multipleSelection[0].id, function(r){
				if (r.data.scheduleJob) {
					vm.schedule = r.data.scheduleJob
					vm.triggerJob.lastTxDate = r.data.lastTxDate
					vm.triggerJob.etlJob = r.data.etlJob
				}

				vm.showTaskData = true;
				vm.title = "定时重跑作业";
			});
			//vm.rerun();
		},
		openRerun:function(){
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var rowKey = vm.multipleSelection.map(x=>{return x.id})

			vm.showQueryData = true;
			vm.title = "重跑作业";
			//vm.rerun();
		},
		setTrigger(ids){
			$.ajax({
				type: "POST",
				url: baseURL + "etl/job/settrigger",
				contentType: "application/json",
				data: JSON.stringify(ids),
				success: function(r){

				}
			});
		},
		removeTrigger(ids){
			$.ajax({
				type: "POST",
				url: baseURL + "etl/job/removetrigger",
				contentType: "application/json",
				data: JSON.stringify(ids),
				success: function(r){

				}
			});
		},
		taskmulti:function(){
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			if(vm.schedule.cronExpression.length == 0){
				vm.$alert('请选择数填写cron表达式', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}
			var ids = vm.multipleSelection.map(x=>{return x.id})
			var param = {
				'rerunjobids':ids.join(','),
				'lastTxDate':vm.triggerJob.lastTxDate
			}
			vm.schedule.params = JSON.stringify(param);
			var url = vm.schedule.jobId == null ? "sys/schedule/save" : "sys/schedule/update";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				contentType: "application/json",
				data: JSON.stringify(vm.schedule),
				success: function(r){
					if(r.code === 0){
						vm.reBack();
						vm.query();
						vm.setTrigger(ids);
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
		},
        rerunmulti:function(){
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var ids = vm.multipleSelection.map(x=>{return x.id})
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
                    data: {'rerunjobids':ids.join(','),'lastTxDate':lastTxDate},
                    success: function(r){
                        vm.q.rerun_data_date =null;
                        if(r.code === 0){
							vm.reBack();
							vm.query();
							vm.$message({
								message: r.msg,
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
            }
            return;
		},
		deleteTigger(){
			vm.$confirm('确定要删除定时器?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				$.ajax({
					type: "POST",
					url: baseURL + "sys/schedule/delete",
					contentType: "application/json",
					data: JSON.stringify([vm.schedule.jobId]),
					success: function(r){
						if(r.code == 0){
							vm.reBack();
							vm.query();
							vm.removeTrigger([vm.triggerJob.etlJob]);
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
			}).catch(() => {
			});
		},
		loadsys: function(componentId){
			$.ajax({
				type: "GET",
				url: baseURL + "etl/sys/getsys",
				contentType: "application/json",
				success: function(data){
					if(data.code == 0){
						vm.allsys = data.allsys;
						window.setTimeout(function(){
							$('#etlSystemSelect').selectpicker('refresh');
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


		loadstatus: function(componentId){
			$.ajax({
				type: "GET",
				url: baseURL + "etl/job/getstatus",
				contentType: "application/json",
				success: function(data){
					if(data.code == 0){
						vm.allstatus = data.allstatus;
						window.setTimeout(function(){
							$('#etlStatusSelect').selectpicker('refresh');
                            $('#updateBatchStatusSelect').selectpicker('refresh');
							// $(componentId).selectpicker('refresh');
						},2000);
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


		del: function (event) {
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}

			var etlSystems = vm.multipleSelection.map(x=>{return x.id})
			vm.$confirm('确定要删除选中的记录?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				$.ajax({
					type: "POST",
					url: baseURL + "etl/job/delete",
					contentType: "application/json",
					data: JSON.stringify(etlSystems),
					success: function(r){
						if(r.code == 0){
							vm.$message({
								message: '操作成功',
								type: 'success'
							});
							vm.query(true);
						}else{
							vm.$alert(r.msg, '系统提示', {
								confirmButtonText: '确定',
								callback: action => {
								}
							});
						}
					}
				});
			}).catch(() => {
			});
		},
		getInfo: function(etlJob){
			$.get(baseURL + "etl/job/info/"+etlJob, function(r){
				vm.job = r.job;
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
		reBack: function () {
			vm.showList = true;
			vm.showQueryData = false;
			vm.showTaskData = false;
		},

        openUpdateStatus: function () {

			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var rowKey = vm.multipleSelection.map(x=>{return x.id})
            vm.showUpdateBatchStatus = true;
            vm.title = "更新作业状态";
        },

        reBack2: function () {
            vm.showList = true;
            vm.showUpdateBatchStatus = false;
        },

        updateBatchStatus: function () {
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var ids = vm.multipleSelection.map(x=>{return x.id})
            if(vm.q.newJobStatus ==null){
				vm.$alert('请选择作业状态', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
                return;
            }
            $.ajax({
                type: "GET",
                url: baseURL + "etl/job/upbatchstatus",
                contentType: "application/json",
                data: {'upids':ids.join(','),'newJobStatus':vm.q.newJobStatus},
                success: function(r){
                    vm.q.newJobStatus =null;
                    if(r.code === 0){
						vm.reBack2();
						vm.query();
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
        },

        openUpdateTxDate:function () {
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var rowKey = vm.multipleSelection.map(x=>{return x.id})
            vm.showUpdateBatchJobTxDate = true;
            vm.title = "更新作业数据日期";
        },

        reBack3: function () {
            vm.showList = true;
            vm.showUpdateBatchJobTxDate = false;
        },

        updateBatchJobTxDate: function () {
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var ids = vm.multipleSelection.map(x=>{return x.id})
            if(vm.q.newJobTxdate ==null){
				vm.$alert('请选择更新数据日期', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
                return;
            }
            $.ajax({
                type: "GET",
                url: baseURL + "etl/job/upbatchtxdate",
                contentType: "application/json",
                data: {'upids':ids.join(','),'newJobTxdate':vm.q.newJobTxdate},
                success: function(r){
                    vm.q.newJobTxdate =null;
                    if(r.code === 0){
						vm.reBack3();
						vm.query();
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
        },

        openUpdateEnable:function () {
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var rowKey = vm.multipleSelection.map(x=>{return x.id})
            vm.showUpdateBatchEnable = true;
            vm.title = "更新作业有效性";
        },

        reBack4: function () {
            vm.showList = true;
            vm.showUpdateBatchEnable = false;
        },

        updateBatchEnable: function () {
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var ids = vm.multipleSelection.map(x=>{return x.id})
            if(vm.q.newEnableFlag ==null){
				vm.$alert('请选择作业状态', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
                return;
            }
            $.ajax({
                type: "GET",
                url: baseURL + "etl/job/upbatchenable",
                contentType: "application/json",
                data: {'upids':ids.join(','),'newEnableFlag':vm.q.newEnableFlag},
                success: function(r){
                    vm.q.newEnableFlag =null;
                    if(r.code === 0){
						vm.reBack4();
						vm.query();
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
        },

	},


});


