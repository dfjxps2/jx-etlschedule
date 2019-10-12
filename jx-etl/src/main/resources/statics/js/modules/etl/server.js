
var vm = new Vue({
	el:'#rrapp',
	data:{
        q:{
            etlServer: null
        },
		showList: true,
		title: null,
		server: {},
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
			if (init) {
				this.initPage();
			}
			var url = "etl/server/list";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				dataType:'json',
				data: {page:this.dataPage.currPage, limit:this.dataPage.pageSize, etlServer:this.q.etlServer},
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
			vm.server = {};
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
			var etlServer = vm.multipleSelection[0].id;
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
				vm.$alert('IP格式不正确', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}
			if (vm.server.livecount == null) {
				vm.$alert('请选择是否有效', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}
			if (vm.server.etlServer == null || vm.server.etlServer.trim() == '') {
				vm.$alert('服务器名称 不能为空', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}
			if (vm.server.agentport == null || (vm.server.agentport + "").trim() == '') {
				vm.$alert('请正确输入 服务器端口内容', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}
			if (vm.server.agentport <= 0 || vm.server.agentport > 65535) {
				vm.$alert('服务器端口 值不正确', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
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
		del: function (event) {
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}

			var etlServers = vm.multipleSelection.map(x=>{return x.id})
			vm.$confirm('确定要删除选中的记录?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				$.ajax({
					type: "POST",
					url: baseURL + "etl/server/delete",
					contentType: "application/json",
					data: JSON.stringify(etlServers),
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
		getInfo: function(etlServer){
			$.get(baseURL + "etl/server/info/"+etlServer, function(r){
                vm.server = r.server;
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
		}
	}
});
