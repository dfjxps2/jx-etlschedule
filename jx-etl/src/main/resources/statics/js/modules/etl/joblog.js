$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'etl/joblog/list',
        datatype: "json",
        colModel: [
			{ label: '作业系统', name: 'etlSystem', index: 'ETL_System', width: 40},
			{ label: '作业名称', name: 'etlJob', index: 'ETL_Job', width: 40 },
			{ label: '会话ID', name: 'jobsessionid', index: 'JobSessionID', width: 40},
			{ label: '步骤ID', name: 'jobstepid', index: 'JobStepID', width: 80 ,hidden:true },
			{ label: '脚本名称', name: 'scriptfile', index: 'ScriptFile', width: 80,hidden:true  },
			{ label: '数据日期', name: 'txdate', index: 'TXDate', width: 40,formatter:"date"},
			{ label: '开始时间', name: 'starttime', index: 'StartTime', width: 80 },
			{ label: '结束时间', name: 'endtime', index: 'EndTime', width: 80 },
			{ label: '', name: 'returncode', index: 'ReturnCode', width: 80 ,hidden:true },
			{ label: '', name: 'seconds', index: 'Seconds', width: 80 ,hidden:true },
            // { label: '操作', name: 'id', index: 'ID', width: 40, key: true,
            //     formatter: function(value, options, row){
            //         return "<a href='/crm/user/"+value+"'><i class='fa fa-search'></i>&nbsp;&nbsp;查看详细日志</a>&nbsp;&nbsp;";
            //     }},
        ],
		viewrecords: true,
    /*    height: 385,*/
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
        joblogdetail:null
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.jobLog = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
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
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
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
                        alert(data.msg);
                    }
                }

			});
		},

        //添加loadlog方法加载作业日志详情
        loadlog: function(){
            // alert("loadlog被调用");
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            var rowdata = $("#jqGrid").jqGrid('getRowData',id);
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
                    if(data.code == 0){
                        $("#joblogdetail").html(data.logresult);
                        // vm.joblogdetail = data.logresult;
                    }else{
                        $("#joblogdetail").html("该日志不存在或已被清理");
                    }
                }

            });
        },

		//添加日志下载
        logdload:function(){
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            var rowdata = $("#jqGrid").jqGrid('getRowData',id);
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
                        alert(data.msg);
                    }
                }

            });



		},

        clearlog: function(){
            vm.joblogdetail = null;
        },
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "etl/joblog/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
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
		getInfo: function(id){
			$.get(baseURL + "etl/joblog/info/"+id, function(r){
				// alert("===================="+JSON.stringify(r));
                vm.jobLog = r.jobLog;
                // alert(vm.jobLog.etlJob);
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'etlJob': vm.q.etlJob,'etlSystem':vm.q.etlSystem,'txdate':vm.q.data_date},
                page:page
            }).trigger("reloadGrid");
		},
        timeline:function(){
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            var row = $('#jqGrid').jqGrid('getRowData',id);
            $.ajax({
                type: "GET",
                url: baseURL + 'etl/joblog/list?&limit=100&page=1',
                contentType: "application/json",
                data:{'etlJob': row.etlJob,'etlSystem':row.etlSystem,'txdate':''},
                success: function(data){
                    if(data.code != 0){
                        alert(data.msg);
                        return;
                    }
                    $(".grid-btn").parent().parent().hide();
                    $("#timelineLayer").show();
                    timelineChart(data.page.list);
                    timelineClick();
                }
            });

        },
        backlist:function(){
            $("#timelineLayer").hide();
            $(".grid-btn").parent().parent().show();
            $("#btnlog1").show();
            $("#btnlog2").hide();
        },
        logdload2:function(){
            if(_tllog.etlSystem == null){
                alert("请先选择一条记录");
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
                        alert(data.msg);
                    }
                }

            });



        },
	}
});

function timelineChart(data){
    $("#btnlog2").show();
    $("#btnlog1").hide();
    var groups = {}, html = ['<div class="history">'];
    data.forEach(function(d){
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
            $("#myModal").modal('show');
            if(data.code == 0){
                $("#joblogdetail").html(data.logresult);
            }else{
                $("#joblogdetail").html("该日志不存在或已被清理");
            }
        }

    });
}