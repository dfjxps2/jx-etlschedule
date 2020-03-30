$(function () {

    vm.loadsys("etlSystemSelect");
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			etlJob:null,
			etlSystem:null,
            data_date:null
			// lastTxDate:null
		},
		showList: true,
		title: null,
		jobLog: {},
        etlSystem:"",
        data_date:Date(),
		allsys:null,
        joblogdetail:null,
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
            var postData = {
                'etlJob': this.q.etlJob,
                'etlSystem':this.q.etlSystem,
                'txdate':this.q.data_date,
                'page':this.dataPage.currPage,
                'limit':this.dataPage.pageSize,
            }
            var url = "etl/joblog/list";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                dataType:'json',
                data: postData,
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
			vm.jobLog = {};
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
            var id = vm.multipleSelection[0].id;
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.jobLog.id == null ? "etl/joblog/save" : "etl/joblog/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.jobLog),
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
            vm.jobLog={};
		},
		//添加loadsys方法加载作业系统下拉框
		loadsys: function(componentId){
			// alert("loadsys被调用");

			$.ajax({
				type: "GET",
				url: baseURL + "etl/sys/getsys",
                contentType: "application/json",
                success: function(data){
                    if(data.code == 0){
						vm.allsys = data.allsys;
                        window.setTimeout(function(){
                            $('#etlSystemSelect').selectpicker('refresh');
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

        //添加loadlog方法加载作业日志详情
        loadlog: function(){
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
            var rowdata = vm.multipleSelection[0];
            var etlSystem = rowdata.etlSystem;
            var jobsessionid = rowdata.jobsessionid;
            var scriptfile = rowdata.scriptfile;
            var txdate = rowdata.txdate;

            // alert("txdate=====" + txdate);
            $.ajax({
                type: "GET",
                url: baseURL + "etl/joblog/loadlog",
                contentType: "application/json",
                data: {'etlSystem':etlSystem,'jobsessionid':jobsessionid,'scriptfile':scriptfile,'txdate':txdate},
                success: function(data){
                    vm.myModal = true;
                    if(data.code == 0){
                        vm.joblogdetail = data.logresult;
                    }else{
                        vm.joblogdetail = data.msg
                    }
                }

            });
        },

		//添加日志下载
        logdload:function(){
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
            var rowdata = vm.multipleSelection[0];
            var etlSystem = rowdata.etlSystem;
            var jobsessionid = rowdata.jobsessionid;
            var scriptfile = rowdata.scriptfile;
            var txdate = rowdata.txdate;
            $.ajax({
                type: "GET",
                url: baseURL + "etl/joblog/loadlog",
                contentType: "application/json",
                data: {'etlSystem':etlSystem,'jobsessionid':jobsessionid,'scriptfile':scriptfile,'txdate':txdate},
                success: function(data){
                    if(data.code == 0){
                        location.href = baseURL+"etl/joblog/logdload?etlSystem="+etlSystem+"&jobsessionid=" + jobsessionid +"&scriptfile="+scriptfile+"&txdate="+txdate;
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

        clearlog: function(){
            vm.joblogdetail = null;
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

            var ids = vm.multipleSelection.map(x=>{return x.id})
            vm.$confirm('确定要删除选中的记录?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                $.ajax({
                    type: "POST",
                    url: baseURL + "etl/joblog/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
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
		getInfo: function(id){
			$.get(baseURL + "etl/joblog/info/"+id, function(r){
				// alert("===================="+JSON.stringify(r));
                vm.jobLog = r.jobLog;
                // alert(vm.jobLog.etlJob);
            });
		},
        timeline:function(){
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
            var row = vm.multipleSelection[0];
            $.ajax({
                type: "GET",
                url: baseURL + 'etl/joblog/list?&limit=100&page=1',
                contentType: "application/json",
                data:{'etlJob': row.etlJob,'etlSystem':row.etlSystem,'txdate':''},
                success: function(data){
                  console.info('timeline resp', data);
                    if(data.code != 0){
                        vm.$alert(data.msg, '系统提示', {
                            confirmButtonText: '确定',
                            callback: action => {
                            }
                        });
                        return;
                    }
                    $("#tableLayout").hide();
                    $("#timelineLayer").show();
                    timelineChart(data.page.list);
                    timelineClick();
                }
            });

        },
        backlist:function(){
            $("#timelineLayer").hide();
            $("#tableLayout").show();
            $("#btnlog1").show();
            $("#btnlog2").hide();
        },
        logdload2:function(){
            if(_tllog.etlSystem == null){
                vm.$alert("只能选择一条记录", '系统提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                    }
                });
                return ;
            }
            var etlSystem = _tllog.etlSystem;
            var jobsessionid = _tllog.jobsessionid;
            var scriptfile = _tllog.scriptfile;
            var txdate = _tllog.txdate;
            $.ajax({
                type: "GET",
                url: baseURL + "etl/joblog/loadlog",
                contentType: "application/json",
                data: _tllog,
                success: function(data){
                    if(data.code == 0){
                        location.href = baseURL+"etl/joblog/logdload?etlSystem="+etlSystem+"&jobsessionid=" + jobsessionid +"&scriptfile="+scriptfile+"&txdate="+txdate;
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
	}
});

function timelineChart(data){
    $("#btnlog2").show();
    $("#btnlog1").hide();
    var groups = {}, html = ['<div class="history">'];
    data.forEach(function(d){
        d.txdate = dateFormat('YYYY-mm-dd',new Date(d.txdate));
        var y = d.txdate.substr(0, 7);
        if(!groups[y]){
            groups[y] = [];
        }
        groups[y].push(d);
    });
    var years = Object.keys(groups);
    years.forEach(function(y, i){
        var ystr = y.replace('-0','年').replace('-','年') + '月';
        var hcss = i == 0 ? 'first' : 'date02';
        html.push('<div class="history-date"><ul>');
        html.push('<h2 class="',hcss, '"><a href="#nogo">',ystr,'</a></h2>');
        groups[y].forEach(function(d){
            html.push('<li><h3>', getDayStr(d.txdate), '</h3>');
            html.push('<dl>', getMsg(d) ,'</dl>', '</li>');
        });
        html.push('</ul></div>');
    });
    html.push('</div>');
    $("#timelineChart").html(html.join(''));

}
function getDayStr(s){
    var arr = s.split(' ');
    arr = arr[0].split('-');
    return arr[1] + '-' + arr[2] + '<span>'+arr[0] + '</span>';
}
function getMsg(d){
    var job = '【' + d.etlJob + '】';
    var sta = d.returncode == '0' ? '成功' : '失败(' + d.returncode + ')';
    var fnstr = 'showLog(\"'+ d.etlSystem +'\",\"'+ d.jobsessionid +'\",\"'+ d.scriptfile +'\",\"'+ d.txdate +'\")';
    return '<dt onclick=\''+fnstr+'\'>'+job+'执行状态：' + sta + ',从' + d.starttime + '至' + d.endtime + '</dt>';
}
function timelineClick() {
    var $warpEle = $(".history-date"),
        $targetA = $warpEle.find("h2 a,ul li dl dt a"),
        parentH = $warpEle.parent().height() + 100;
    setTimeout(function(){
        $warpEle.parent().animate({
            "height":parentH
        }, 1200);
    }, 50);

    $warpEle.find("ul").children(":not('h2:first')").addClass("bounceInDown").css({
        "-webkit-animation-duration":"2s",
        "-webkit-animation-delay":"0",
        "-webkit-animation-timing-function":"ease",
        "-webkit-animation-fill-mode":"both"
    }).end();
    $targetA.click(function () {
        $(this).parent().siblings().slideToggle();
        $warpEle.parent().removeAttr("style");
        return false;
    });

}
var _tllog = {};
function showLog(etlSystem, jobsessionid, scriptfile, txdate){
    _tllog = {'etlSystem':etlSystem,'jobsessionid':jobsessionid,'scriptfile':scriptfile,'txdate':txdate};
    $.ajax({
        type: "GET",
        url: baseURL + "etl/joblog/loadlog",
        contentType: "application/json",
        data: _tllog,
        success: function(data){
            vm.myModal = true;
            if(data.code == 0){
                vm.joblogdetail = data.logresult;

            }else{
                vm.joblogdetail = data.logresult;

            }
        }

    });
}
