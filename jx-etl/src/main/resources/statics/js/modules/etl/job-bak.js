$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'etl/job/list',
        datatype: "json",
        colModel: [			
			{ label: '系统名称', name: 'etlSystem', index: 'ETL_System', width: 50, key: true },
			{ label: '作业名称', name: 'etlJob', index: 'ETL_Job', width: 80 },
			{ label: '服务器名称', name: 'etlServer', index: 'ETL_Server', width: 80 },
			{ label: '描述', name: 'description', index: 'Description', width: 80 },
			{ label: '周期类型', name: 'jobtype', index: 'JobType', width: 80 },
			{ label: '是否生效', name: 'enable', index: 'Enable', width: 80 },
			{ label: '开始时间', name: 'lastStarttime', index: 'Last_StartTime', width: 80 },
			{ label: '结束时间', name: 'lastEndtime', index: 'Last_EndTime', width: 80 },
			{ label: '运行状态', name: 'lastJobstatus', index: 'Last_JobStatus', width: 80 },
			{ label: '数据日期', name: 'lastTxdate', index: 'Last_TXDate', width: 80 },
			{ label: '会话ID', name: 'jobsessionid', index: 'JobSessionID', width: 80 },
			{ label: '优先级', name: 'priority', index: 'Priority', width: 80 }
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		job: {}
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
			var etlSystem = getSelectedRow();
			if(etlSystem == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(etlSystem)
		},
		saveOrUpdate: function (event) {
			var url = vm.job.etlSystem == null ? "etl/job/save" : "etl/job/update";
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
		del: function (event) {
			var etlSystems = getSelectedRows();
			if(etlSystems == null){
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
		getInfo: function(etlSystem){
			$.get(baseURL + "etl/job/info/"+etlSystem, function(r){
                vm.job = r.job;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});