function back(){
    drawChart = drawChart1;
    is_auto = false;
    clearTimeout(_tfn);
    clearState();
    $("#analysisLayer").hide();
    $(".grid-btn").parent().show();
    window.dispatchEvent(new Event('resize'));
}
//血缘影响分析
function analysis(){
    if(vm.multipleSelection.length == 0){
        vm.$alert("请选择一条记录", '系统提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
        });
        return ;
    }
    if(vm.multipleSelection.length  > 1){
        vm.$alert("只能选择一条记录", '系统提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
        });
        return ;
    }
    vm.showRadio = '0'
    var rowdata = vm.multipleSelection[0]
    var querydata = {
        limit:10000,
        'dep_etlSystem': rowdata.etlSystem,
        'dep_etlJob': rowdata.etlJob
    };
    $.ajax({
        type: "GET",
        url: baseURL + 'etl/job/analysis',
        contentType: "application/json",
        data:querydata,
        success: function(data){
            if(data.code != 0){
                vm.$alert(data.msg, '系统提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                    }
                });
                return;
            }
            $(".grid-btn").parent().hide();
            $("#analysisLayer").show();
            topoChart(data.data, querydata);
        }
    });
}

//血缘影响分析图
var reqData = [],allData = {}, myChart = null, _option = {}, _ejob = '', _esys = '', _jobid='', _queryPrm = {}, _box = {}, is_auto = true, _ts = 20000, _tfn = 0, _nMap = {}, _nGroup = {}, _lv = 0;
function topoChart(data, prm){
    reqData = data;
    _queryPrm = prm;
    is_auto = true;
    var uploadedDataURL = baseURL + "/statics/js/modules/etl/job-analysis/data-1479697763933-ByhDrJlGx.json";
    _box = initSize(data);
    $.get(uploadedDataURL, function (geoJson) {
        echarts.registerMap('wuhan', geoJson);

        drawChart(data, _box);
        setTimeout(auto_refresh, _ts);//添加定时
    });
}
function auto_refresh(){
    $.ajax({
        type: "GET",
        url: baseURL + 'etl/job/analysis',
        contentType: "application/json",
        data:_queryPrm,
        success: function(data){
            if(data.code != 0){
                return;
            }
            console.log('time elsape...');
            reqData = data.data;
            drawChart(reqData, _box);
        },complete:function(){
            if(is_auto){
                _tfn = setTimeout(auto_refresh, _ts);
            }
        }
    });
}
//设置画布大小
function initSize(data){
    console.info('initSize')
    var w = $("#analysisLayer").width(), h = $("#analysisLayer").height() - 45, lr = 0, lc = 0, lr0 = 2, xx = 160, yy = 100, yy0 = 80;
    var sta = 0, end = 0, i = 0;
    for(var n in data) {
        if(data[n].length > 0)
            lr++;
        else
            lr0++;
        lc = data[n].length > lc ? data[n].length : lc;
        if(i==0)
            end = n;
        else
            sta = n;
        i++;
    }
    if(lr>=4){
        h = h + yy * (lr - 5) + lr0 * yy0;
    }
    if(lc>6){
        w = w + xx * (lc - 5);
    }
    $("#analysisChart").width(w).height(h);
    return {w:w,h:h, sta:parseInt(sta), end:parseInt(end)};
}
function initData1(maps){
    //xy=[112.25, 30.25], xx = -0.2, yy=0.5
    var nodes = [], lines = [], xy = [110, 100], yy = 100, xx = 120, a = 0, b = 0;
    var imgURL = baseURL + "/statics/js/modules/etl/job-analysis/";
    var lvmap = ["0","-1","1"];
    for(var j in maps){
        var data = maps[j];
        if(data.length == 0)
            continue;
        if(_lv>0 && lvmap.indexOf(j)==-1)
            break;
        var y0 = xy[1] + (a++)*yy;
        b = 0;
        _nMap[j] = 0;
        data.forEach(function(o){
            if(hasNode(o, nodes))
                return true;
            _nMap[j]++;
            o.Status = checkStatus(o);
            var node = {
                "name": o.dependencySystem + '.' + o.dependencyJob
                ,"value":[xy[0] + (b++)*xx,y0,20]
                ,symbol: 'image://' + imgURL + 'svr_' + o.Status + '.png'
                ,symbolSize:40
                ,itemStyle:{}
                ,info:o
            };
            nodes.push(node);
        });
    }
    for(var j in maps){
        if(_lv>0 && lvmap.indexOf(j)==-1)
            break;
        maps[j].forEach(function(o){
            var n1 = o.dependencySystem + '.' + o.dependencyJob, n2 = o.etlSystem + '.' + o.etlJob;
            if(hasLine(lines, n1, n2))
                return true;
            var line = {
                "fromName":n1,"toName":n2
                ,"coords":getCoords(n1,n2, nodes, xy)
                ,lineStyle:{}
            };
            lines.push(line);
        });
    }
    return {
        nodes:nodes,
        lines:lines
    };
}
function initData2(maps){
    var width = $("#analysisChart").width(), height = $("#analysisChart").height();
    var nodes = [], lines = [],ids = {}, mapc = 0, xy = [100, 0], yy = 100, xx = 160, a = 0, b = 0;
    var imgURL = baseURL + "/statics/js/modules/etl/job-analysis/";
    var lvmap = ["0","-1","1"];
    for(var j in maps){
        var data = maps[j];
        if(data.length == 0)
            continue;
        if(_lv>0 && lvmap.indexOf(j)==-1)
            break;
        var x0 = ++a;
        b = 1;
        _nMap[j] = 0;
        data.forEach(function(o){
            if(hasNode(o, nodes))
                return true;
            _nMap[j]++;
            o.Status = checkStatus(o);
            var node = {
                "name": o.dependencySystem + '.' + o.dependencyJob
                ,"value":[x0, b++,20]
                ,symbol: 'image://' + imgURL + 'svr_' + o.Status + '.png'
                ,symbolSize:40
                ,itemStyle:{}
                ,info:o
            };
            nodes.push(node);
            ids[o.id] = j;
        });
        if(_nMap[j]>0)
            mapc++;
    }
    //计算各节点位置
    nodes.forEach(function(o){
        var i = o.info.id, pos = o.value, cnt = _nMap[ids[i]], step = height / (cnt + 1);
        var x0 = xy[0] + (mapc - pos[0])*xx;
        var y0 = xy[1] + pos[1]*step;
        o.value = [x0, y0];
    });

    for(var j in maps){
        if(_lv>0 && lvmap.indexOf(j)==-1)
            break;
        maps[j].forEach(function(o){
            var n1 = o.dependencySystem + '.' + o.dependencyJob, n2 = o.etlSystem + '.' + o.etlJob;
            if(hasLine(lines, n1, n2))
                return true;
            var cds = getCoords(n1,n2, nodes, xy);
            //横坐标不等时拐点
            if(cds[0][0] != cds[1][0]){
                var xm = (cds[0][0] + cds[1][0]) / 2;
                cds = [cds[0],[xm, cds[0][1]],[xm, cds[1][1]],cds[1]];
            }
            var line = {
                "fromName":n1,"toName":n2
                ,"coords":cds
                ,lineStyle:{}
            };
            lines.push(line);
        });
    }
    return {
        nodes:nodes,
        lines:lines
    };
}
function setLayer(lv){
    _lv = lv;
    drawChart(reqData, _box);
}
function hasNode(o, arr){
    for(var i = 0; i < arr.length; i++){
        if(arr[i].info.id == o.id)
            return true;
    }
    return false;
}
function hasLine(arr, n1, n2){
    for(var i = 0; i < arr.length; i++){
        if(arr[i].fromName == n1 && arr[i].toName == n2)
            return true;
    }
    return false;
}
function getCoords(n1,n2, nodes, df){
    var crds = [df, df], l = nodes.length;
    for(var i = 0; i < l; i++){
        var n = nodes[i];
        if(n.name == n1){
            crds[0] = [n.value[0], n.value[1]];
            break;
        }
    }
    for(var i = 0; i < l; i++){
        var n = nodes[i];
        if(n.name == n2){
            crds[1] = [n.value[0], n.value[1]];
            break;
        }
    }
    return crds;
}
function checkStatus(o){
    var s = o.lastJobstatus;
    // var d1 = lastDate();
    // var d2 = o.lastTxdate;
    // if(d1 != d2)
    // 	s = 'Stop';
    return s;
}

function lastDate(){
    var dd = new Date();
    dd.setTime(dd.getTime()-24*60*60*1000);
    return dd.getFullYear() + '-' + fix0(dd.getMonth()+1) + '-' + fix0(dd.getDate());
}


function fix0(n){
    var s = '0' + n;
    return s.substr(s.length - 2, 2);
}

function initOpt1(){
    return {
        backgroundColor: '#074883',
        color:['#074883','#020933'],
        title: [{
            text: '任务调度图',
            top: 0,
            left:10,
            textStyle: {
                color: '#fff'
            }
        }],
        toolbox:{
            left:150,
            itemSize:30,
            feature: {
                myLayout2:{
                    show:true,
                    title:'树型排列',
                    icon:'image://'+baseURL+'/statics/js/modules/etl/job-analysis/treeico.png',
                    onclick:function(){
                        drawChart = drawChart2;
                        drawChart(reqData, _box);
                    }
                },
            }
        },
        tooltip:{
            trigger:'item',
            formatter:function(o){
                if(o.data == null || o.data.info == null)
                    return o.name;
                var x = o.data.info;
                var str = x.dependencyJob + '<br />系统名称：' + x.dependencySystem + '<br />描述：' + x.description + '<br />任务状态：'+ x.lastJobstatus
                    + '<br />当前数据日期：'+ dateFormat('YYYY-mm-dd',new Date(x.lastTxdate));
                return str;
            }
        },
        legend: {
            show: false,
            orient: 'vertical',
            top: 'bottom',
            left: 'right',
            data: ['任务', '进度'],
            textStyle: {
                color: '#fff'
            }
        },
        grid: {
            width:'100%',
            height:'100%',
            left:0,
            right:0,
            bottom:0,
            top:0,
        },
        xAxis: {
            show: false,
        },
        geo: {
            map: 'wuhan1',
            label: {
                emphasis: {
                    show: false
                }
            },
            roam: true,
            itemStyle: {
                normal: {
                    color:'rgba(255,255,255,0)',
                    areaColor:'rgba(255,255,255,0)',
                    borderColor:'rgba(255,255,255,0)'
                },
                emphasis: {
                    color:'rgba(22,22,2,0)',
                    areaColor:'rgba(22,22,2,0)',
                    borderColor:'rgba(22,22,2,0)'
                }
            }
        },
        series: [
            {
                name: '任务',
                type: 'scatter',
                coordinateSystem: 'geo',
                zlevel: 2,
                rippleEffect: {
                    brushType: 'stroke',
                    period:7,
                    scale:26
                },
                label: {
                    normal:{
                        show:true,
                        position:'bottom',
                        formatter:function(o){
                            if(o.name.length <= 12)
                                return o.name;
                            var s = '';
                            for(var i = 0; i < o.name.length; i++){
                                s+= o.name.charAt(i);
                                if(i%12==0&&i>0)
                                    s+= '\n';
                            }
                            return s;
                        },
                        color:'white',
                    },
                    emphasis: {
                        show: true,
                        formatter: '{b}'
                    }
                },
                symbolSize: 20,
                showEffectOn: 'render',
                itemStyle: {
                    normal: {
                        color: '#46bee9'
                    }
                },
                data: allData.nodes
            },
            {
                name: '进度',
                type: 'lines',
                coordinateSystem: 'geo',
                zlevel: 1,
                large: true,
                effect: {
                    show: true,
                    period: 4,
                    //constantSpeed: 30,
                    //symbol: 'arrow',
                    color: '#64f2ff',
                    symbolSize: 4,
                    trailLength: 0.5,
                },
                lineStyle: {
                    normal: {
                        color:'#13aae6',
                        width: 1,
                        opacity: 0.6,
                        curveness: 0.1
                    }
                },
                data: allData.lines
            }
        ]
    };
}
function initOpt2(){
    return {
        backgroundColor: '#074883',
        color:['#074883','#020933'],
        title: [{
            text: '任务调度图',
            top: 0,
            left:10,
            textStyle: {
                color: '#fff'
            }
        }],
        toolbox:{
            left:150,
            itemSize:30,
            feature: {
                myLayout1:{
                    show:true,
                    title:'分层排列',
                    icon:'image://'+baseURL+'/statics/js/modules/etl/job-analysis/netico.png',
                    onclick:function(){
                        drawChart = drawChart1;
                        drawChart(reqData, _box);
                    }
                },
            }
        },
        tooltip:{
            trigger:'item',
            formatter:function(o){
                if(o.data == null || o.data.info == null)
                    return o.name;
                var x = o.data.info;
                var str = x.dependencyJob + '<br />系统名称：' + x.dependencySystem + '<br />描述：' + x.description + '<br />任务状态：'+ x.lastJobstatus
                    + '<br />当前数据日期：'+ dateFormat('YYYY-mm-dd',new Date(x.lastTxdate));
                return str;
            }
        },
        legend: {
            show: false,
            orient: 'vertical',
            top: 'bottom',
            left: 'right',
            data: ['任务', '进度'],
            textStyle: {
                color: '#fff'
            }
        },
        grid: {
            width:'100%',
            height:'100%',
            left:0,
            right:0,
            bottom:0,
            top:0,
        },
        xAxis: {
            show: false,
        },
        geo: {
            map: 'wuhan1',
            label: {
                emphasis: {
                    show: false
                }
            },
            roam: true,
            itemStyle: {
                normal: {
                    color:'rgba(255,255,255,0)',
                    areaColor:'rgba(255,255,255,0)',
                    borderColor:'rgba(255,255,255,0)'
                },
                emphasis: {
                    color:'rgba(22,22,2,0)',
                    areaColor:'rgba(22,22,2,0)',
                    borderColor:'rgba(22,22,2,0)'
                }
            }
        },
        series: [
            {
                name: '任务',
                type: 'scatter',
                coordinateSystem: 'geo',
                zlevel: 2,
                rippleEffect: {
                    brushType: 'stroke',
                    period:7,
                    scale:26
                },
                label: {
                    normal:{
                        show:true,
                        position:'bottom',
                        formatter:function(o){
                            if(o.name.length <= 12)
                                return o.name;
                            return o.name.substr(0, 12) + '\n' + o.name.substring(12);
                        },
                        color:'white',
                    },
                    emphasis: {
                        show: true,
                        formatter: '{b}'
                    }
                },
                symbolSize: 20,
                showEffectOn: 'render',
                itemStyle: {
                    normal: {
                        color: '#46bee9'
                    }
                },
                data: allData.nodes
            },
            {
                name: '进度',
                type: 'lines',
                coordinateSystem: 'geo',
                zlevel: 1,
                large: false,
                polyline:true,
                effect: {
                    show: true,
                    period: 4,
                    //constantSpeed: 30,
                    //symbol: 'arrow',
                    color: '#64f2ff',
                    symbolSize: 4,
                    trailLength: 0.5,
                },
                lineStyle: {
                    normal: {
                        color:'#13aae6',
                        width: 1,
                        opacity: 0.6,
                        curveness: 0
                    }
                },
                data: allData.lines
            }
        ]
    };
}

function drawMenu(myChart){
    var style_ul = "padding:0px;margin:0px;border: 1px solid #ccc;background-color: #fff;position: absolute;left: 0px;top: 0px;z-index: 2;display: none;";
    var style_li = "list-style:none;padding: 5px; cursor: pointer; padding: 5px 20px;margin:0px;";
    var style_li_hover = style_li + "background-color: #00A0E9; color: #fff;";
    var menubox = null;
    if($("#echartboxMenu").size() == 0){
        menubox = $("<div id='echartboxMenu' class='echartboxMenu' style='" + style_ul + "'><ul style='margin:0px;padding:0px;'></ul></div>")
            .appendTo($(document.body));
        $(document).click(function() {
            menubox.hide();
        });
    }else{
        menubox = $("#echartboxMenu");
    }

    myChart.getDom().oncontextmenu = menubox[0].oncontextmenu = function(){
        return false;
    }
    myChart.on("mousedown",function(e){
        if(e.event.event.button===2 && e.componentSubType == 'scatter'){
            _ejob = e.data.info.dependencyJob;
            _esys = e.data.info.dependencySystem;
            _jobid = e.data.info.id;
            showMenu([
                {
                    "name": "重跑",
                    "fn": function() {
                        vm.openRerun();
                    }
                },
                {
                    "name":"查看日志",
                    "fn":function(){
                        vm.loadlog();
                    }
                },
                {
                    "name":"下载日志",
                    "fn":function(){
                        vm.logdload();
                    }
                }
            ]);
        }
    });

    var showMenu = function(menus){
        var menulistbox = $("ul", menubox).empty();
        $(menus).each(function(i, item) {
            var li = $("<li style='" + style_li + "'>" + item.name + "</li>")
                .mouseenter(function() {
                    $(this).attr("style", style_li_hover);
                })
                .mouseleave(function() {
                    $(this).attr("style", style_li);
                })
                .click(function() {
                    item["fn"].call(this);
                    menubox.hide();
                });
            menulistbox.append(li);
        });
        menubox.css({
            "left": event.x,
            "top": event.y
        }).show();
    }
}

function drawChart1(data, box){
    if(myChart != null){
        myChart.dispose();
        $("#analysisChart").html('');
    }
    myChart = echarts.init($('#analysisChart')[0]);
    allData = initData1(data);
    var bgObj = setBg(data);
    _option = initOpt1();
    appendBg(_option, bgObj, box);

    myChart.setOption(_option);
    drawMenu(myChart);
    if(allData.nodes.length > 4){
        focusNode(myChart,_option);
    }
}

function focusNode(myChart, option){
    myChart.on("mouseover",function(prm){
        var name = prm.name;
        if(prm.componentSubType != "scatter")
            return;
        var gp = findGroup(name, option);
        setGroup(option, true, gp);
        myChart.setOption(option);

        _nGroup[name].groups.forEach(function(o){
            myChart.dispatchAction({
                type: 'highlight',
                name: o
            });
        });
    });
    myChart.on("mouseout",function(prm){
        var name = prm.name;
        if(prm.componentSubType != "scatter")
            return;
        setGroup(option, false, null);
        myChart.setOption(option);

        _nGroup[name].groups.forEach(function(o){
            myChart.dispatchAction({
                type: 'downplay',
                name: o
            });
        });
    });
}

function findGroup(name,option){
    if(_nGroup[name])
        return _nGroup[name];
    _nGroup[name] = {nodes:[],lines:[],groups:[]};
    var arr = _nGroup[name].groups;
    option.series[1].data.forEach(function(o, i){
        if(o.fromName == name){
            arr.push(o.toName);
            _nGroup[name].lines.push(i);
        }
        if(o.toName == name){
            arr.push(o.fromName);
            _nGroup[name].lines.push(i);
        }
    });
    option.series[0].data.forEach(function(o, i){
        if(o.name == name)
            _nGroup[name].nodes.push(i);
        else if(arr.indexOf(o.name)>-1)
            _nGroup[name].nodes.push(i);
    });
    return _nGroup[name];
}

function setGroup(option, ishide,g){
    var v1 = ishide ? 0.2 : 1;
    option.series[0].data.forEach(function(o){
        o.itemStyle.opacity = v1;
    });
    if(ishide){
        option.series[1].data.forEach(function(o){
            o.coords1 = o.coords;
            o.coords = [o.coords[0],o.coords[0]];
        });
    }else{
        option.series[1].data.forEach(function(o){
            o.coords = o.coords1;
        });
    }

    if(!g) return;
    g.nodes.forEach(function(i){
        option.series[0].data[i].itemStyle.opacity = 1;
    });
    g.lines.forEach(function(i){
        option.series[1].data[i].coords = option.series[1].data[i].coords1;
    });
}

function drawChart2(data, box){
    if(myChart != null){
        myChart.dispose();
        $("#analysisChart").html('');
    }
    myChart = echarts.init($('#analysisChart')[0]);

    allData = initData2(data);
    //var bgObj = setBg(data);
    _option = initOpt2();
    //appendBg(_option, bgObj, box);

    myChart.setOption(_option);
    drawMenu(myChart);
    if(allData.nodes.length > 4){
        focusNode(myChart,_option);
    }
}
var drawChart = drawChart1;
function setBg(data){
    var aStyle = [{color:'#f00'},{color:'#ff0'}];//#020933
    var ret = {min:0,max:0,s:[]},m = [], q = -1, curr = '', pre = '', h2 = 60, yy = 100, yy0 = 40;
    for(var n in data){
        if(data[n].length > 0 && (_nMap[n] && _nMap[n]>0)){
            h2 += yy;
            curr = data[n][0].dependencySystem;
            if(curr != pre){
                m[++q] = {
                    'name':curr,
                    'min':pre ? m[q-1].max : 0,
                    'max':h2
                };
                pre = curr;
            }else{
                m[q].max = h2;
            }
        }
    }
    ret.max = h2;
    for(var n = 0; n < m.length; n++){
        var x = m[n];
        ret.s.push({
            name: x.name + n,
            xname: x.name,
            type: 'line',
            animation: false,
            tooltip:{show:false},
            areaStyle: aStyle[n%2],
            lineStyle: {
                normal: {
                    width: 1
                }
            },
            markArea: {
                data: [
                    [{
                        yAxis: x.min + 1
                    }, {
                        yAxis: x.max
                    }]
                ]
            }
        });
    }
    return ret;
}
function appendBg(option, ret, box){
    var bl = box.h / ret.max;
    option.yAxis = {
        min: ret.min,
        max: box.h,
        inverse:true,
        //splitNumber: 10,
        show: false,
        axisLabel:{inside:true}
    };
    ret.s.forEach(function(o){
        option.series.push(o);
        var t1 = parseInt(((o.markArea.data[0][1].yAxis - o.markArea.data[0][0].yAxis) / 2 + o.markArea.data[0][0].yAxis));
        var txt = {
            text: o.xname,
            left:10,
            top:t1,
            textStyle:{
                color:'#e1e1e1',
                fontSize:10,
                fontStyle:'italic'
            }
        };
        option.title.push(txt);
    });
}

function clearState(){
    _ejob = '';
    _esys = '';
    _jobid='';
    _box = {};
    _nMap = {};
    _nGroup = {};
}
