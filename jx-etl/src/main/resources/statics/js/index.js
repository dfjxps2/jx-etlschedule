//生成菜单
var menuItem = Vue
        .extend({
            name : 'menu-item',
            props : {
                item : {}
            },
            template : [
                    '<li class="" v-if="item.menuId != 1">',
                    '	<a v-if="item.type === 0" href="javascript:;" class="nav-top-item">',
                    '		<i v-if="item.icon != null" :class="item.icon"></i>',
                    '		<span>{{item.name}}</span>',
                    '	</a>',
                    '	<ul v-if="item.type === 0" class="treeview-menu secondary-menu " style="display: block">',
                    '		<menu-item :item="item" v-for="item in item.list"></menu-item>',
                    '	</ul>',

                    '	<a v-if="item.type === 1 && item.parentId === 0" :href="\'#\'+item.url" class="nav-top-item2">',
                    '		 ',
                    '		<span>{{item.name}}</span>',
                    '	</a>',

                   // '	<a v-if="item.type === 1 && item.parentId != 0" :href="\'#\'+item.url"><i v-if="item.icon != null" :class="item.icon"></i><i v-else class="fa fa-circle-o"></i> {{item.name}}</a>',
                    ' <a v-if="item.type === 1 && item.parentId != 0" :href="\'#\'+item.url" class="submenu-item"><i style="width: 20px;;display: inline-block;"></i><i v-else style="width: 20px;;display: inline-block;"></i> {{item.name}}</a>',
                    '</li>' ].join('')
        });

// iframe自适应
$(window).on('resize', function() {
    var $content = $('.content');
    console.info('resize:' + ($(this).height()))
    $content.height($(this).height() + 20);//143
    // $content.height($(this).height());
    $content.find('iframe').each(function() {
        $(this).height($content.height());
    });
}).resize();
$(function() {
    $("#sidebarMenu").height($("#mainSidebar").height() - 10);
    //设置二级菜单图标为空
});
// 注册菜单组件
Vue.component('menuItem', menuItem);

var vm = new Vue({
    el : '#rrapp',
    data : {
        user : {},
        menuList : {},
        main : "main.html",
        password : '',
        newPassword : '',
        navTitle : "控制台",
        localMenu:[],
        headerMenus: [],
        headerMenus2: [],
    },
    methods : {
        getMenuList : function(event) {
            $.getJSON("sys/menu/nav?_" + $.now(), function(r) {

                var root = {
                    menuId: 42,
                    parentId: 0,
                    parentName: null,
                    name: "批量数据处理",
                    url: "",
                    perms: null,
                    type: 0,
                    icon: "icon1",
                    orderNum: 4,
                    open: null,
                    list: r.menuList
                }
                vm.menuList = [root]

                console.log('get_menu_nav', vm.menuList)
            });
        },
        getUser : function() {
            $.getJSON("sys/user/info?_" + $.now(), function(r) {
                vm.user = r.user;
            });
        },
        menuHoverOrClick : function(eventVal){
            // this.headerMenus.forEach(headerMenu=>{
            //     headerMenu.actived = false
            // })
            for(var i=0; i<this.headerMenus.length; i++) {
                this.headerMenus[i].actived = false;
            }
            eventVal.actived = !eventVal.actived

        },
        menuClick : function(eventVal){
            console.info('menuClick', eventVal)
            if(eventVal.url&&eventVal.url!=''&&eventVal.outLink=='Y'){
                window.location.href = eventVal.url
            }else{
                this.$emit('changeTaijiMenuList',eventVal.name)
            }
        },
        updatePassword : function() {
            layer.open({
                type : 1,
                skin : 'layui-layer-molv',
                title : "修改密码",
                area : [ '550px', '270px' ],
                shadeClose : false,
                content : jQuery("#passwordLayer"),
                btn : [ '修改', '取消' ],
                btn1 : function(index) {
                    var data = "password=" + vm.password + "&newPassword=" + vm.newPassword;
                    $.ajax({
                        type : "POST",
                        url : "sys/user/password",
                        data : data,
                        dataType : "json",
                        success : function(result) {
                            if (result.code == 0) {
                                layer.close(index);
                                layer.alert('修改成功', function(index) {
                                    location.reload();
                                });
                            } else {
                                layer.alert(result.msg);
                            }
                        }
                    });
                }
            });
        },
        donate : function() {
            layer.open({
                type : 2,
                title : false,
                area : [ '806px', '467px' ],
                closeBtn : 1,
                shadeClose : false,
                content : [ 'http://cdn.dfjx.io/donate.jpg', 'no' ]
            });
        },
        logout: function() {
            if (confirm('确定要退出？') ) {
                $.ajax({
                    type: "GET",
                    url: "ca/logout/",
                    dataType: "json",
                    success: function(result){
                        if(result.code == 0){//成功
                            Cookies.remove('authList');
                            Cookies.remove('user');
                            parent.location.href ='http://172.26.60.219/zyzx/logout?remPath=/zyzx/portal/index.htm';
                        }else{
                        }
                    }
                });
            }
        },

    },
    created : function() {
        this.getMenuList();
        this.getUser();

        var me = this;
        // /jx-etl/statics/js/top.json 测试
        $.getJSON("sys/menu/top?" + $.now(), function(taijiMenuResponse) {
            if(taijiMenuResponse&&taijiMenuResponse.code==0){
                const taijiMenuArray = taijiMenuResponse.data

                const fullMenuArrayList = JSON.parse(sessionStorage.getItem('menuList') || '[]')

                const taijiAuthMenuMapTMp = {}

                function parseTaijiMenu(menuArrayParam){
                    const parseResultMenu = []
                    if(menuArrayParam&&menuArrayParam.length>0){
                        // debugger
                        // menuArrayParam.forEach(taijiMenuData=>{
                        //     const menuCode = taijiMenuData.code
                        //     const menuName = taijiMenuData.name
                        //     const menuUrl = taijiMenuData.url
                        //     const children = taijiMenuData.subMenu
                        //
                        //     taijiAuthMenuMapTMp[menuName] = true
                        //
                        //     parseResultMenu.push({
                        //         id:menuCode,
                        //         name:menuName,
                        //         actived:menuName=='数据处理'?true:false,
                        //         url:menuUrl,
                        //         outLink:me.localMenu[menuName]?'Y':'N',
                        //         children:parseTaijiMenu(children)
                        //     })
                        // })
                        for (var i = 0; i < menuArrayParam.length; i++) {
                            var taijiMenuData = menuArrayParam[i];

                            const menuCode = taijiMenuData.code
                            const menuName = taijiMenuData.name
                            const menuUrl = taijiMenuData.url
                            const children = taijiMenuData.subMenu

                            taijiAuthMenuMapTMp[menuName] = true

                            parseResultMenu.push({
                                id:menuCode,
                                name:menuName,
                                actived:menuName=='数据处理'?true:false,
                                url:menuUrl,
                                outLink:me.localMenu[menuName]?'Y':'N',
                                children:parseTaijiMenu(children)
                            })
                        }
                    }
                    return parseResultMenu
                }

                function localMenuFilter(menuList){
                    if(menuList&&menuList.length>0){
                        const resultFilterMenu = new Array()
                    //     menuList.forEach(localMenuTmp=>{
                    //         const localMenuTmpName = localMenuTmp.name
                    //         if(taijiAuthMenuMapTMp[localMenuTmpName]){
                    //         const localMenuTmpParse = JSON.parse(JSON.stringify(localMenuTmp))
                    //         localMenuTmpParse.list=[]
                    //         resultFilterMenu.push(localMenuTmpParse)
                    //         // console.log(localMenuTmp)
                    //         if(localMenuTmp.list&&localMenuTmp.list.length>0){
                    //             localMenuTmpParse.list = localMenuFilter(localMenuTmp.list)
                    //         }
                    //     }
                    // })
                        for (var i = 0; i < menuList.length; i++) {
                            var localMenuTmp = menuList[i];
                            const localMenuTmpName = localMenuTmp.name
                            if (taijiAuthMenuMapTMp[localMenuTmpName]) {
                                const localMenuTmpParse = JSON.parse(JSON.stringify(localMenuTmp))
                                localMenuTmpParse.list = []
                                resultFilterMenu.push(localMenuTmpParse)
                                // console.log(localMenuTmp)
                                if (localMenuTmp.list && localMenuTmp.list.length > 0) {
                                    localMenuTmpParse.list = localMenuFilter(localMenuTmp.list)
                                }
                            }
                        }
                        return resultFilterMenu
                    }
                }
                me.headerMenus = parseTaijiMenu(taijiMenuArray)
                for (var i = 0; i < me.headerMenus.length; i++) {
                    if (me.headerMenus[i].id == '80000' || me.headerMenus[i].id == 'A0000') {
                        me.headerMenus2.push(me.headerMenus[i])
                        me.headerMenus.splice(i, 1)
                    }
                }
                console.info('topmenu', me.headerMenus)
                // console.log(fullMenuArrayList)
                const localShowMenus = localMenuFilter(fullMenuArrayList)
                // console.log(localShowMenus)
                //me.$emit('initTaijiMenuList',localShowMenus)

            }
        });

    },
    updated : function() {
        // 路由
        var router = new Router();
        routerList(router, vm.menuList);
        router.start();
    }
});

function routerList(router, menuList) {
    for ( var key in menuList) {
        var menu = menuList[key];
        if (menu.type == 0) {
            routerList(router, menu.list);
            // if (menu.name == '系统管理') {
            // //thp改 导航菜单初始化展开
            // $(".header").next().removeClass("active");
            // }
        } else if (menu.type == 1) {
            router.add('#' + menu.url, function() {
                var url = window.location.hash;

                // 替换iframe的url
                vm.main = url.replace('#', '');

                // 导航菜单展开
                $(".treeview-menu li").removeClass("active");
                $(".sidebar-menu li").removeClass("active");

                $("a[href='" + url + "']").parents("li").addClass("active");

                vm.navTitle = $("a[href='" + url + "']").text();
            });
        }
    }
}
// 首页
function firstPage() {
    window.location.href = "index.html";
}
