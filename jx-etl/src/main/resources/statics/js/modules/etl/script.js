$(function () {
	//初始系统
	vm.loadServer();
	//初始化导入
	uploadScript();

});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			filename: null
		},
		showList: true,
		title: null,
		isChange:false,
		etlServer:null,
		etlServerTxt:null,
		allServers:null,
		script: {
		},
		dataPage: {},
		multipleSelection:[],
		myModal:false
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
			var url = "etl/script/list";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				dataType:'json',
				data: {page:this.dataPage.currPage, limit:this.dataPage.pageSize, filename:this.q.filename},
				success: function(r){
					if(r.code === 0){
						vm.dataPage = r.page;
					}
				}
			});
		},
		add: function(){
			vm.showList = false;
			vm.isChange=false
			vm.title = "新增";
			vm.script = {
				filename:"",
				scripttype:"",
				filepath:"/app"
				// filepath:"/home/etl/ETLAuto/app/"
			};
			console.log('===',url)
			$.ajax({ url: baseURL + url, success: function(r){ vm.script.filepath=r.filePath}});
		},
		update: function (event) {
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			if(vm.multipleSelection.length > 1){
				vm.$alert('只能选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var scriptid = vm.multipleSelection[0].scriptid;
			vm.isChange=false
			vm.showList = false;
			vm.title = "修改";
			vm.getInfo(scriptid)
		},
		saveOrUpdate: function (event) {
			console.info(vm.script)
			if (vm.script.etlServer == null || vm.script.etlServer.trim() == ''){
				vm.$alert('请选择服务器名称', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}

			if (vm.script.filename == null || vm.script.filename.trim() == '') {
				vm.$alert('请导入模板', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}
			if(vm.script.scriptid == null && vm.isChange==true){
				ajaxupload.submit();
			}else if(vm.script.scriptid != null  && vm.isChange==true){
				ajaxupload.submit();
			}else if(vm.script.scriptid != null  && vm.isChange==false){
				vm.doSaveOrUpdate();
			}
			//console.log(m)
		},
		doSaveOrUpdate:function(){
			var url = vm.script.scriptid == null ? "etl/script/save" : "etl/script/update";
			$.ajax({
				type: "POST",
				url: baseURL + url,
				contentType: "application/json",
				data: JSON.stringify(vm.script),
				success: function(r){
					if(r.code === 0){
						vm.reBack();
						vm.query();
						vm.isChange=false
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

			var scriptids = vm.multipleSelection.map(x=>{return x.scriptid})
			vm.$confirm('确定要删除选中的记录?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				$.ajax({
					type: "POST",
					url: baseURL + "etl/script/delete",
					contentType: "application/json",
					data: JSON.stringify(scriptids),
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
		getInfo: function(scriptid){
			$.get(baseURL + "etl/script/info/"+scriptid, function(r){
				vm.script = r.script;
				$('#etlServerSelect').selectpicker('val', vm.script.etlServer);
				$('#enableid').selectpicker('val', vm.script.enable);
				$('#shareflagid').selectpicker('val', vm.script.shareflag);
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
		},
		loadServer: function(componentId){
			$.ajax({
				type: "GET",
				// url: baseURL + "etl/script/getService",
				url: baseURL + "etl/server/getService",
				contentType: "application/json",
				success: function(data){
					if(data.code == 0){
						vm.allServers = data.allServers;
						console.log(vm.allServers)
						window.setTimeout(function(){
							$('#etlServerSelect').selectpicker('refresh');
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
		readScripts:function(){
			vm.editScripts(true);
		},
		editScripts:function(isRead){
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});				return ;
			}
			if(vm.multipleSelection.length > 1){
				vm.$alert('只能选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var scriptid = vm.multipleSelection[0].scriptid;
			if(isRead===true){
				$("#showSaveScript").hide();
			}else{
				$("#showSaveScript").show();
			}
			// $("#myModal").modal('show');
			$.ajax({
				type: "GET",
				url: baseURL + "etl/script/readScripts/"+scriptid,
				contentType: "application/json",
				success: function(data){
					var str = '';
					if(data.code == 0){
						str = data.scriptDetail.replace(/<br>/g, '\n');
						$("#scriptDetail").empty();
						$("#scriptDetail").text(str);
						vm.myModal = true
					}else{
						vm.$alert(data.msg, '系统提示', {
							confirmButtonText: '确定',
							callback: action => {
							}
						});
						return;
					}
					vm.$nextTick(()=>{
						if(!editor){
							editor = CodeMirror.fromTextArea(vm.$refs.scriptDetail, {
								lineNumbers: true,
								lineWrapping: true,
								theme:'dracula',
								autofocus:true,
								mode: getMode(data.type)
							});
						}else{
							editor.setOption('mode', getMode(data.type));
						}
						editor.setValue(str);
						editor_refresh();
					})
				}
			});
		},
		saveScripts:function(){
			var scriptDetail = editor.getValue();
			if(!scriptDetail){
				vm.$alert("脚本内容不能为空", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return;
			}
			if(vm.multipleSelection.length == 0){
				vm.$alert('请选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			if(vm.multipleSelection.length > 1){
				vm.$alert('只能选择一条记录', '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return ;
			}
			var scriptid = vm.multipleSelection[0].scriptid;
			scriptDetail = base64(scriptDetail);
			$.ajax({
				type: "POST",
				url: baseURL + "etl/script/saveScripts",
				data: JSON.stringify({"scriptDetail":scriptDetail,"scriptid":scriptid }),
				contentType: "application/json",
				success: function(data){
					if(data.code == 0){
						vm.$message({
							message: '操作成功',
							type: 'success'
						});
						vm.myModal = false;
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
		closeScripts:function(){
			vm.scriptDetail = null;
			vm.myModal = false;
		},
		goScriptLog:function(){
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
			var scriptid = vm.multipleSelection[0].scriptid;
			window.location.href = 'script_log.html?scriptid=' + scriptid;
		}
	}
});


//导入脚本
var ajaxupload=null;
function uploadScript() {
	var URL=baseURL+"etl/script/upload";
	var callBakUpload="";
	ajaxupload=new AjaxUpload('#upload', {
		action : URL, // url自己写
		name : 'file',
		autoSubmit : false,
		responseType : "json",
		onSubmit : function(file, extension) {
			//alert(file);
			if (!(extension && /^(py|sh|java|perl)$/.test(extension.toLowerCase()))) {
				vm.$alert("模板必须是py、sh、java、perl类型!", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}
			this.setData({"filepath":vm.script.filepath});
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
			if (!(extension && /^(py|sh|java|perl)$/.test(extension.toLowerCase()))) {
				vm.$alert("模板必须是py、sh、java、perl类型", '系统提示', {
					confirmButtonText: '确定',
					callback: action => {
					}
				});
				return false;
			}
			vm.isChange=true;
			vm.script.filename=file;
			vm.script.scripttype=extension;
		}
	});
}

function getSelectdServer() {
	vm.etlServer =$("#etlServerSelect ").val();
	vm.etlServerTxt=$("#etlServerSelect ").find("option:selected").text();
}
var editor=null;
function getMode(tp){
	switch(tp){
		case "py": return 'text/x-python';
		case "sh": return 'text/x-sh';
		case "java": return 'text/x-java';
		case "perl": return 'text/x-perl';
	}
}
function editor_refresh(){
	setTimeout(function(){
		editor.refresh();
	}, 250);
}
function base64(str){
	return btoa(encodeURIComponent(str));
}
