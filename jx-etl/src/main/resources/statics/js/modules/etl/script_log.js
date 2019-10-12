var scriptid = req("scriptid");
var editor=null;


var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            scriptId: scriptid
        },
        dataPage: {
            list: [],
            currPage: 1,
            pageSize: 10,
            totalCount: 0
        },
        myModal:false
    },
    mounted(){
        this.query();
    },
    methods: {
        query: function (init) {
            var url = "etl/scriptlog/list";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                dataType:'json',
                data: {page:this.dataPage.currPage, limit:this.dataPage.pageSize, scriptid:this.q.scriptId},
                success: function(r){
                    if(r.code === 0){
                        vm.dataPage = r.page;
                    }
                }
            });
        },
        back: function (event) {
            history.go(-1);
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
        colIndex(row, column, cellValue, index) {
            return (vm.dataPage.currPage - 1) * vm.dataPage.pageSize + index + 1
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
    vm.myModal = true
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
}
function restore(id){
    $.ajax({
        type: "POST",
        url: baseURL + "etl/scriptlog/saveScripts/" + id,
        contentType: "application/json",
        success: function(data){
            if(data.code == 0){
                vm.query();
                vm.$message({
                    message: '操作成功',
                    type: 'success'
                });
            }else{
                vm.$alert(data.msg, '系统提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                    }
                });
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

