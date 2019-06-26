$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'etl/joblog/list',
        datatype: "json",
        colModel: [
			{ label: '作业系统', name: 'etlSystem', index: 'ETL_System', width: 40},
			{ label: '作业名称', name: 'etlJob', index: 'ETL_Job', width: 40 },
			{ label: '会话ID', name: 'jobsessionid', index: 'JobSessionID', width: 40},
			{ label: '步骤ID', name: 'jobstepid', index: 'JobStepID', width: 80 ,hidden:true },
			{ label: '脚本名称', name: 'scriptfile', index: 'ScriptFile', width: 80,hidden:true  },
			{ label: '数据日期', name: 'txdate', index: 'TXDate', width: 40,formatter:"date"},
			{ label: '开始时间', name: 'starttime', index: 'StartTime', width: 80 },
			{ label: '结束时间', name: 'endtime', index: 'EndTime', width: 80 },
			{ label: '', name: 'returncode', index: 'ReturnCode', width: 80 ,hidden:true },
			{ label: '', name: 'seconds', index: 'Seconds', width: 80 ,hidden:true },
            { label: '操作', name: 'id', index: 'ID', width: 40, key: true,
                formatter: function(value, options, row){
                    return "<a href='/crm/user/"+value+"'><i class='fa fa-search'></i>&nbsp;&nbsp;查看详细日志</a>&nbsp;&nbsp;";
                }},
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
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });

    vm.loadsys("etlSystemSelect");
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			etlJob:null,
			etlSystem:null,
            data_date:null
			// lastTxDate:null
		},
		showList: true,
		title: null,
		jobLog: {},
        etlSystem:"",
        data_date:Date(),
		allsys:null,
        joblogdetail:null

	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.jobLog = {};
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
			var url = vm.jobLog.id == null ? "etl/joblog/save" : "etl/joblog/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.jobLog),
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
		//添加loadsys方法加载作业系统下拉框
		loadsys: function(componentId){
			// alert("loadsys被调用");

			$.ajax({
				type: "GET",
				url: baseURL + "etl/sys/getsys",
                contentType: "application/json",
                success: function(data){
                    if(data.code == 0){
						vm.allsys = data.allsys;
                    }else{
                        alert(data.msg);
                    }
                }

			});
		},

        //添加loadlog方法加载作业日志详情
        loadlog: function(){
            // alert("loadlog被调用");
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            vm.getInfo(id);
            alert(vm.jobLog.etlJob);
            // alert(JSON.stringify(vm.jobLog));
            // $.ajax({
            //     type: "GET",
            //     url: baseURL + "etl/joblog/loadlog",
            //     contentType: "application/json",
            //     data: JSON.stringify(vm.jobLog),
            //     success: function(data){
            //         if(data.code == 0){
            //             vm.joblogdetail = data.logresult;
            //         }else{
            //             alert(data.msg);
            //         }
            //     }
            //
            // });
        },

		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "etl/joblog/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
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
		getInfo: function(id){
			$.get(baseURL + "etl/joblog/info/"+id, function(r){
				// alert("===================="+JSON.stringify(r));
                vm.jobLog = r.jobLog;
                // alert(vm.jobLog.etlJob);
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'etlJob': vm.q.etlJob,'etlSystem':vm.q.etlSystem,'txdate':vm.q.data_date},
                page:page
            }).trigger("reloadGrid");
		}
	}
});