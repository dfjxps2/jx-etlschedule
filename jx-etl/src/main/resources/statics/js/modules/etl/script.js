$(function () {
	$("#jqGrid").jqGrid({
		url: baseURL + 'etl/script/list',
		datatype: "json",
		colModel: [			
		           { label: '模板ID', name: 'scriptid', index: 'ScriptID', width: 50, key: true},
		           { label: '服务器名称', name: 'etlServerName', index: 'EtlServerName', width: 80 },
		           { label: '模板名称', name: 'filename', index: 'FileName', width: 80 },
		           { label: '模板类型', name: 'scripttype', index: 'ScriptType', width: 80 },
		           { label: '模板描述', name: 'description', index: 'Description', width: 80 },
		           { label: '是否有效', name: 'enable', index: 'Enable', width: 60,formatter: function (value, options, row) {
						if(value == "1"){
							return  '<span class="label label-success">有效模板</span>'
						} else if (value == "0") {
							return  '<span class="label label-default">无效模板</span>'
						} else {
							return '<span class="label label-default">未知状态</span>'
						};
					}},
			       { label: '是否共享', name: 'shareflag', index: 'ShareFlag', width: 60,formatter: function (value, options, row) {
						if(value == "1"){
							return  '<span class="label label-success">共享</span>'
						} else if (value == "0") {
							return  '<span class="label label-default">私有</span>'
						} else {
							return '<span class="label label-default">未知状态</span>'
						};
					}},
		           { label: '创建人', name: 'username', index: 'Username', width: 60 }
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
		}
	},
	methods: {
		query: function () {
			vm.reload();
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
			$.ajax({ url: baseURL + url, success: function(r){ vm.script.filepath=r.filePath}});
		},
		update: function (event) {
			var scriptid = getSelectedRow();
			if(scriptid == null){
				return ;
			}
			vm.isChange=false
			vm.showList = false;
			vm.title = "修改";
			vm.getInfo(scriptid)
		},
		saveOrUpdate: function (event) {
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
						doSuccess('操作成功', function(index) {
							vm.reload();
							vm.isChange=false
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var scriptids = getSelectedRows();
			if(scriptids == null){
				return ;
			}
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
					url: baseURL + "etl/script/delete",
					contentType: "application/json",
					data: JSON.stringify(scriptids),
					success: function(r){
						if(r.code == 0){
							doSuccess('操作成功', function(index) {
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
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
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData:{'filename': vm.q.filename},
				page:page
			}).trigger("reloadGrid");
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
						window.setTimeout(function(){
							$('#etlServerSelect').selectpicker('refresh');
						},1000); 
					}else{
						alert(data.msg);
					}
				}

			});
		},
		readScripts:function(){
			vm.editScripts(true);
		},
		editScripts:function(isRead){
			var scriptid = getSelectedRow();
			if(scriptid == null){
				return ;
			}
			if(isRead===true){
				$("#showSaveScript").hide();
			}else{
				$("#showSaveScript").show();
			}
			$("#myModal").modal('show');
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
					}else{
						alert(data.msg);
						return;
					}
					if(!editor){
						editor = CodeMirror.fromTextArea(document.getElementById("scriptDetail"), {
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
				}
			});
		},
		saveScripts:function(){
			var scriptDetail = editor.getValue();
			if(!scriptDetail){
				alert("脚本内容不能为空");
				return;
			}
			var scriptid = getSelectedRow();
			if(scriptid == null){
				return ;
			}
			scriptDetail = base64(scriptDetail);
			$.ajax({
				type: "POST",
				url: baseURL + "etl/script/saveScripts",
				data: JSON.stringify({"scriptDetail":scriptDetail,"scriptid":scriptid }),
				contentType: "application/json",
				success: function(data){
					if(data.code == 0){
						alert("操作成功");
					}else{
						alert("操作失败，"+data.msg);
					}
				}
			});
		},
		closeScripts:function(){
			vm.scriptDetail = null;
		},
		goScriptLog:function(){
			var scriptid = getSelectedRow();
			if(scriptid == null){
				return ;
			}
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
				alert("模板必须是py、sh、java、perl类型!");
				return false;
			}
			this.setData({"filepath":vm.script.filepath});
		},
		onComplete : function(file, m) {
			if(m.code==1){
				alert("脚本已存在");
			}else{
				vm.doSaveOrUpdate();
			}
		},
		onChange:function(file, extension){
			if (!(extension && /^(py|sh|java|perl)$/.test(extension.toLowerCase()))) {
				alert("模板必须是py、sh、java、perl类型");
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
