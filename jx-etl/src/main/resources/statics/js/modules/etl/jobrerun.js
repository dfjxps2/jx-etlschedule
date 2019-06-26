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
		           { label: '作业周期', name: 'frequency', index: 'Frequency', width: 80 ,formatter: function (value, options, row) {
                           if(value == "0"){
                               return  '<span class="label label-primary">日作业</span>'
                           } else {
                               return  '<span class="label label-info">月作业</span>'
                           };
                       }},
		           { label: '作业类型', name: 'jobtype', index: 'JobType', width: 50,hidden:true,},
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
	vm.loadsys("etlSystemSelect");
	vm.loadstatus("etlStatusSelect");
    vm.loadstatus("updateBatchStatusSelect");
	setTimeout(refreshjoblist,vm.refresh_freq);
	function refreshjoblist() {
		if(vm.refresh_auto)
			vm.reload();
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
		dependQ:{},
		showList: true,
		showQueryData:false,
        showUpdateBatchStatus: false,
        showUpdateBatchJobTxDate: false,
        showUpdateBatchEnable: false,
		title: null,
		job: {},
		allsys:[],
		allstatus:null,
		refresh_freq:20000,
		refresh_auto:true,
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.job = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
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
            var grid = $("#jqGrid");
            var rowKey = grid.getGridParam("selrow");
            if(!rowKey){
                alert("请选择一条或多条记录");
                return ;
            }

			vm.showQueryData = true;
			vm.title = "重跑作业";
			//vm.rerun();
		},
		// //检查重跑单个任务还是批量重跑
		// checkRerun:function(){
         //    var grid = $("#jqGrid");
         //    var rowKey = grid.getGridParam("selrow");
         //    if(!rowKey){
         //        alert("请选择一条或多条记录");
         //        return ;
         //    }
         //    var selectedIDs = grid.getGridParam("selarrrow");
         //    if(selectedIDs.length === 1){
         //    	vm.rerun();
		// 	} else if (selectedIDs.length > 1){
         //    	vm.rerunmulti();
		// 	} else {
         //    	return;
		// 	}
		// },
		// //重跑单个任务
		// rerun: function(){
         //    alert("进入 rerun-js方法");
		// 	var id = getSelectedRow();
		// 	if (id == null){return};
		// 	var rowdata = $("#jqGrid").jqGrid('getRowData',id);
		// 	var etlSystem = rowdata.etlSystem;
		// 	var etlJob = rowdata.etlJob;
		// 	if(vm.q.rerun_data_date ==null){
		// 		alert("请选择数据重跑日期");
		// 		return;
		// 	} else {
		// 		var lastTxDate = vm.q.rerun_data_date;
		// 		var url = "etl/job/rerun";
		// 		$.ajax({
		// 			type: "GET",
		// 			url: baseURL + url,
		// 			contentType: "application/json",
		// 			data: {'etlSystem':etlSystem,'etlJob':etlJob,'lastTxDate':lastTxDate},
		// 			// data: 'etlSystem=' + etlSystem + '&etlJob=' + etlJob + '&lastTxDate=' + lastTxDate,
		// 			success: function(r){
		// 				vm.q.rerun_data_date =null;
		// 				if(r.code === 0){
		// 					alert(r.msg, function(index){
		// 						vm.reload();
		// 						vm.reBack();
		// 					});
		// 				}else{
		// 					alert(r.msg);
		// 				}
		// 			}
		// 		});
		// 	}
		// 	return;
		// },
        rerunmulti:function(){
            var ids = getSelectedRows();
            if (ids == null){return};
            if(vm.q.rerun_data_date ==null){
                alert("请选择数据重跑日期");
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
                            $('#updateBatchStatusSelect').selectpicker('refresh');
							// $(componentId).selectpicker('refresh');
						},2000);
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
			});
		},



		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam', {
				postData: {
					'dep_etlSystem': vm.q.dep_etlSystem,
					'dep_etlJob': vm.q.dep_etlJob,
					'reqDenpsType': vm.q.reqDenpsType,
					'reqAllDeps': vm.q.reqAllDeps,
					'etlJob': vm.q.etlJob,
					'etlSystem':vm.q.etlSystem,
					'lastTxDateStart': vm.q.lastTxDateStart,
					'lastTxDateEnd': vm.q.lastTxDateEnd,
					'lastJobStatus': vm.q.lastJobStatus
				},
				page: page
			}).trigger("reloadGrid");
			vm.q.reqDenpsType=null;
			vm.q.dep_etlSystem=null;
			vm.q.dep_etlJob=null;
		},
		reBack: function () {
			vm.showList = true;
			vm.showQueryData = false;
		},

        openUpdateStatus: function () {

            var grid = $("#jqGrid");
            var rowKey = grid.getGridParam("selrow");
            if(!rowKey){
                alert("请选择一条或多条记录");
                return ;
            }
            vm.showUpdateBatchStatus = true;
            vm.title = "更新作业状态";
        },

        reBack2: function () {
            vm.showList = true;
            vm.showUpdateBatchStatus = false;
        },

        updateBatchStatus: function () {
            var ids = getSelectedRows();
            if (ids == null){return};
            if(vm.q.newJobStatus ==null){
                alert("请选择作业状态");
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
                        alert(r.msg, function(index){
                            vm.reload();
                            vm.reBack2();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },

        openUpdateTxDate:function () {
            var grid = $("#jqGrid");
            var rowKey = grid.getGridParam("selrow");
            if(!rowKey){
                alert("请选择一条或多条记录");
                return ;
            }

            vm.showUpdateBatchJobTxDate = true;
            vm.title = "更新作业数据日期";
        },

        reBack3: function () {
            vm.showList = true;
            vm.showUpdateBatchJobTxDate = false;
        },

        updateBatchJobTxDate: function () {
            var ids = getSelectedRows();
            if (ids == null){return};
            if(vm.q.newJobTxdate ==null){
                alert("请选择更新数据日期");
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
                        alert(r.msg, function(index){
                            vm.reload();
                            vm.reBack3();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },

        openUpdateEnable:function () {
            var grid = $("#jqGrid");
            var rowKey = grid.getGridParam("selrow");
            if(!rowKey){
                alert("请选择一条或多条记录");
                return ;
            }
            vm.showUpdateBatchEnable = true;
            vm.title = "更新作业有效性";
        },

        reBack4: function () {
            vm.showList = true;
            vm.showUpdateBatchEnable = false;
        },

        updateBatchEnable: function () {
            var ids = getSelectedRows();
            if (ids == null){return};
            if(vm.q.newEnableFlag ==null){
                alert("请选择作业状态");
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
                        alert(r.msg, function(index){
                            vm.reload();
                            vm.reBack4();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },

	},


});


