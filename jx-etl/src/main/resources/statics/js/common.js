//jqGrid的配置信息
$.jgrid.defaults.width = 1000;
$.jgrid.defaults.responsive = true;
$.jgrid.defaults.styleUI = 'Bootstrap';

var baseURL = "../../";

//工具集合Tools
window.T = {};

//初始化表格高度
function initGridHeight(parentId,jqGrid,delHeight){
   _initGridHeight($("#"+parentId),$("#"+jqGrid),delHeight);
}

function _initGridHeight(_parentId,_jqGrid,delHeight){
    var allH=_parentId.css("height");
    if(delHeight==undefined  ||undefined =="" || undefined ==null){
        _jqGrid.jqGrid("setGridHeight",parseInt(allH)-130);
    }else{
        _jqGrid.jqGrid("setGridHeight",parseInt(allH)-delHeight);
    }
}

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function(name) {
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
};
T.p = url;


//操作成功提示
function doSuccess(msg, callback) {
	parent.layer.closeAll();
	layer.open({
		title:"",
		type : 1,
		offset: 'rb',//offset : 'auto',// 具体配置参考：offset参数项
		content : '<div class="do-success-content">' + msg + '</div>',
		//content : '<div class="do-success-content">操 作 成 功</div>',
		shade : 0,
		anim: 6,
		area: ['300px', '60px'],
		skin: 'do-success',
		time:3000,
		closeBtn:0
		//end:callback("ok")
	});
	if (typeof (callback) === "function") {
		callback("ok");
	}
}

//全局配置
$.ajaxSetup({
	dataType: "json",
	cache: false
});

//重写alert
window.alert = function(msg, callback){
	parent.layer.alert(msg, function(index){
		parent.layer.close(index);
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
}

//重写confirm式样框
window.confirm = function(msg, callback){
	parent.layer.confirm(msg, {btn: ['确定','取消']},
	function(){//确定事件
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow")
	// alert("rowKey====" + rowKey);
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    var selectedIDs = grid.getGridParam("selarrrow");
    if(selectedIDs.length > 1){
    	alert("只能选择一条记录");
    	return ;
    }
    
    return selectedIDs[0];
}




//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    return grid.getGridParam("selarrrow");
}



//选择一条记录
function getSelectedRowById(id) {
  var grid = $("#"+id);
  var rowKey = grid.getGridParam("selrow")
	// alert("rowKey====" + rowKey);
  if(!rowKey){
  	alert("请选择一条记录");
  	return ;
  }
  
  var selectedIDs = grid.getGridParam("selarrrow");
  if(selectedIDs.length > 1){
  	alert("只能选择一条记录");
  	return ;
  }
  return selectedIDs[0];
}

//选择多条记录
function getSelectedRowsById(id) {
    var grid = $("#"+id);
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    return grid.getGridParam("selarrrow");
}


//判断是否为空
function isBlank(value) {
    return !value || !/\S/.test(value)
}




