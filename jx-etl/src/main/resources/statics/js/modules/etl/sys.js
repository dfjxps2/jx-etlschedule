$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'etl/sys/list',
        datatype: "json",
        colModel: [
            { label: '作业系统ID', name: 'id', index: 'ID', width: 50, key: true, hidden:true},
			{ label: '作业系统名称', name: 'etlSystem', index: 'ETL_System', width: 50 },
			{ label: '作业系统描述', name: 'description', index: 'Description', width: 80 },
			{ label: '数据保存周期', name: 'datakeepperiod', index: 'DataKeepPeriod', width: 80, hidden:true },
			{ label: '日志保存周期', name: 'logkeepperiod', index: 'LogKeepPeriod', width: 80 },
			{ label: '记录保存周期', name: 'recordkeepperiod', index: 'RecordKeepPeriod', width: 80 , hidden:true},
			{ label: '优先级', name: 'priority', index: 'Priority', width: 80 },
			{ label: '并发数', name: 'concurrent', index: 'Concurrent', width: 80 }
        ],
		viewrecords: true,
       /* height: 385,*/
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


    //初始化表格高度
    initGridHeight("rrapp","jqGrid");
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			etlSystem:null
		},
		showList: true,
		title: null,
		sys: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sys = {logkeepperiod:10};
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
			var url = vm.sys.id == null ? "etl/sys/save" : "etl/sys/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.sys),
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
				    url: baseURL + "etl/sys/delete",
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
			$.get(baseURL + "etl/sys/info/"+etlSystem, function(r){
                vm.sys = r.sys;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'etlSystem': vm.q.etlSystem},
                page:page
            }).trigger("reloadGrid");
		}
	}
});