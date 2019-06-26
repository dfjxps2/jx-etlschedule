var scriptid = req("scriptid");
var editor=null;
$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'etl/scriptlog/list?scriptid=' + scriptid,
        datatype: "json",
        colModel: [
			{ label: '日志ID', name: 'id', index: 'ID', width: 50, key: true,hidden:true },
			{ label: '修改类型', name: 'actions', index: 'Actions', width: 60 },
			{ label: '修改说明', name: 'message', index: 'Message', width: 100 },
			{ label: '修改人', name: 'author', index: 'Author', width: 80 },
			{ label: '修改时间', name: 'logdate', index: 'LogDate', width: 80 },
            { label: '操作', name: 'enable', index: 'Enable', width: 60,formatter: function (value, options, row) {
                return "<a href='javascript:void(0);' onclick='read(\""+row.id+"\")'>查看</a> | "
                    +"<a href='javascript:void(0);' onclick='restore(\""+row.id+"\")'>恢复</a>";
            }},
        ],
		viewrecords: true,
       /* height: 385,*/
        rowNum: 10,
		rowList : [10,30,50,100,200],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
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

    initGridHeight("rrapp","jqGrid");
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			scriptId: scriptid
		}
	},
	methods: {
		query: function () {
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{},
                page:1 
            }).trigger("reloadGrid");
		},
		back: function (event) {
			history.go(-1);
		}
	}
});

function req(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)
		return  decodeURI(r[2]);
	return null;
}
function read(id){
    $("#myModal").modal('show');
    $.ajax({
        type: "GET",
        url: baseURL + "etl/scriptlog/readScripts/"+id,
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
}
function restore(id){
    $.ajax({
        type: "POST",
        url: baseURL + "etl/scriptlog/saveScripts/" + id,
        contentType: "application/json",
        success: function(data){
            if(data.code == 0){
                doSuccess('操作成功', function(index) {
                    $("#jqGrid").trigger("reloadGrid");
                });
            }else{
                alert("操作失败，"+data.msg);
            }
        }
    });
}
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

