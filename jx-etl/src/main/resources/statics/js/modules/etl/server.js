$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'etl/server/list',
        datatype: "json",
        colModel: [
            { label: '服务器ID', name: 'id', index: 'Id', width: 50, key: true , hidden:true},
			{ label: '服务器名称', name: 'etlServer', index: 'ETL_Server', width: 50 },
			{ label: '服务器描述', name: 'description', index: 'Description', width: 80 },
			{ label: '服务器IP', name: 'ipaddress', index: 'IPAddress', width: 80 },
			{ label: '服务器端口', name: 'agentport', index: 'AgentPort', width: 80 },
            { label: '是否有效', name: 'livecount', index: 'LiveCount', width: 80,formatter: function (value, options, row) {
                    if(value == "1"){
                        return  '<span class="label label-success">有效服务器</span>'
                    } else if (value == "0") {
                        return  '<span class="label label-default">无效服务器</span>'
                    } else {};
                }}
        ],
		viewrecords: true,
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
            etlServer: null
        },
		showList: true,
		title: null,
		server: {}
	},
	methods: {
		query: function () {
			$("#jqGrid").jqGrid('setGridParam',{
				page:1
			})
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.server = {};
		},
		update: function (event) {
			var etlServer = getSelectedRow();
			if(etlServer == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(etlServer)
		},
		checkIP: function(ip) {
			var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
			return reg.test(ip);
		},
		saveOrUpdate: function (event) {
			if (this.checkIP(vm.server.ipaddress) == false) {
				alert('IP格式不正确');
				return;
			}
			if (vm.server.livecount == null) {
				alert('请选择是否有效');
				return;
			}
			if (vm.server.etlServer == null || vm.server.etlServer.trim() == '') {
				alert('服务器名称 不能为空');
				return;
			}
			if (vm.server.agentport == null || vm.server.agentport.trim() == '') {
				alert('服务器端口 不能为空');
				return;
			}
			if (vm.server.agentport <= 0 || vm.server.agentport > 65535) {
				alert('服务器端口 值不正确');
				return;
			}
			var url = vm.server.id == null ? "etl/server/save" : "etl/server/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.server),
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
			var etlServers = getSelectedRows();
			if(etlServers == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "etl/server/delete",
                    contentType: "application/json",
				    data: JSON.stringify(etlServers),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								if ($("#jqGrid").getGridParam("reccount") == etlServers.length) {
									$("#jqGrid").jqGrid('setGridParam',{
										page:1
									})
								}
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(etlServer){
			$.get(baseURL + "etl/server/info/"+etlServer, function(r){
                vm.server = r.server;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'etlServer': vm.q.etlServer},
                page:page
            }).trigger("reloadGrid");
		}
	}
});