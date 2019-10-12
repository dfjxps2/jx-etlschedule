
var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			etlSystem:null
		},
		showList: true,
		title: null,
		sys: {},
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
			var url = "etl/sys/list";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				dataType:'json',
				data: {page:this.dataPage.currPage, limit:this.dataPage.pageSize, etlSystem:this.q.etlSystem},
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
			vm.sys = {logkeepperiod:10};
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
			var etlSystem = vm.multipleSelection[0].id;

			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(etlSystem)
		},
		saveOrUpdate: function (event) {
			if (vm.sys.etlSystem == null || vm.sys.etlSystem.trim() == '') {
				vm.$alert("作业系统名称 不能为空", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}
			if (!/^[A-Z]{3,3}$$/.test(vm.sys.etlSystem)) {
				vm.$alert("作业系统名称 只能输入三个英文大写字母", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}
			if (vm.sys.priority == null || (vm.sys.priority + "").trim() == '') {
				vm.$alert("请正确输入 优先级", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}
			if(vm.sys.logkeepperiod == null || (vm.sys.logkeepperiod + "").trim() == '') {
				vm.$alert("请正确输入 日志保存周期", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}
			if (vm.sys.concurrent == null || (vm.sys.concurrent + "").trim() == '') {
				vm.$alert("请正确输入 并发数", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}
			console.info('saveorUpdate', vm.sys)
			var url = vm.sys.id == null ? "etl/sys/save" : "etl/sys/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.sys),
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
				vm.$alert("请选择一条记录", '系统提示', {
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
					url: baseURL + "etl/sys/delete",
					contentType: "application/json",
					data: JSON.stringify(etlSystems),
					success: function(r){
						if(r.code == 0){
							vm.query(true);
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
		getInfo: function(etlSystem){
			$.get(baseURL + "etl/sys/info/"+etlSystem, function(r){
                vm.sys = r.sys;
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
