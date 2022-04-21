var pageii = null;
var grid = null;
var editingId;
var guid;

function reload(){
    grid.datagrid('reload');
}

$(function() {
    var editRow = undefined;
    grid= $('#tt').treegrid({
        url:rootPath + '/resources/treelists.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        idField:'id',
        treeField:'name',
        fitColumns:true,
        showGroup: true,
        singleSelect : false,
        columns:[[
            {field:'name',title:'菜单名称',width:100,align:'center',editor:'text'},
                                    {field:'type',title:'菜单类型',width:100,align:'center',editor:'text'},
                                    {field:'resKey',title:'唯一key',width:100,align:'center',editor:'text'},
                                    {field:'resUrl',title:'URL地址',width:100,editor:'text',editor:'text'},
                                    {field:'description',title:'描述1',width:100,align:'center'},
                                    {field:'parentId',title:'父',width:0,align:'center',hidden:'true'},
        ]],
        toolbar:[{
            text : "添加",
            iconCls : "icon-add",
            handler : function() {
                $("#add").window({
                    width: 600,
                    modal: true,
                    height: 400,
                    top:50,
                    href: rootPath + '/resources/addUI.shtml',
                    onClose:function () {
                        $('#tt').datagrid("reload");
                    }
                });
            }
        },
        //     {
        //     text : "编辑",
        //     iconCls : "icon-edit",
        //     handler : function() {
        //         beginEditByTableIdSelects("tt");
        //     }
        // },{
        //     text : "取消",
        //     iconCls : "icon-undo",
        //     handler : function() {
        //         endEditAndcancelSelectByTableIdSelects("tt");
        //     }
        // },{
        //     text : "保存",
        //     iconCls : "icon-save",
        //     handler : function() {
        //         isAdd=false;
        //         var rows = $('#tt').datagrid('getEditingRowIndexs');
        //         var editEntityList=new Array();
        //         for(i=0;i<rows.length;i++){
        //             var row = $("#tt").treegrid("find",rows[i]);
        //             if(i==0&&row.id.length==32){
        //                 isAdd=true;
        //             }
        //             $('#tt').treegrid('endEdit',row.id);
        //             $('#tt').treegrid('unselect',row.id);
        //             delete row["children"];
        //             delete row["state"];
        //             editEntityList.push(row);
        //         }
        //         if(rows.length>0){
        //             editEntityStr=JSON.stringify(editEntityList);
        //             if(isAdd){
        //                 communatePost(rootPath +'/resources/addEntity.shtml',editEntityStr,function back(data){
        //                     // t.treegrid('reloadFooter');
        //                     console.log(data);
        //                     $('#tt').treegrid('reload');//刷新
        //                     endEditAndcancelSelectByTableIdSelects("tt");
        //                 });
        //             }else{
        //                 communatePost(rootPath +'/resources/editEntity.shtml',editEntityStr,function back(data){
        //                     $('#tt').treegrid('reload');//刷新
        //                     endEditAndcancelSelectByTableIdSelects("tt");
        //                 });
        //             }
        //         }
        //     }
        // },
            {
                text : "删除",
                iconCls : "icon-cancel",
                handler : function() {
                    $.messager.confirm('系统提示', '删除节点将同时删除其下面所有节点，您确定要删除吗?', function (r) {
                        if (r) {
                            var rows = $('#tt').datagrid('getSelections');
                            var ids="";
                            for(i=0;i<rows.length;i++){
                                var t = $('#tt');
                                editingId=rows[i].id;
                                ids=ids+editingId+",";
                            }
                            if(rows.length>0){
                                communatePost(rootPath +'/resources/deleteEntity.shtml',ids,function back(data){
                                    t.treegrid('reloadFooter');
                                    $('#tt').treegrid('reload');//刷新
                                });
                            }
                        }
                    });
                }
            },

        ]
    });

	$("#seach").click("click", function() {// 绑定查询按扭
		var searchParams = $("#searchForm").serializeJson();
		grid.setOptions({
			data : searchParams
		});
	});
	$("#addFun").click("click", function() {
		addFun();
	});
	$("#editFun").click("click", function() {
		editFun();
	});
	$("#delFun").click("click", function() {
		delFun();
	});
	$("#lyGridUp").click("click", function() {// 上移
		var jsonUrl=rootPath + '/background/resources/sortUpdate.shtml';
		grid.lyGridUp(jsonUrl);
	});
	$("#lyGridDown").click("click", function() {// 下移
		var jsonUrl=rootPath + '/background/resources/sortUpdate.shtml';
		grid.lyGridDown(jsonUrl);
	});
});
function editFun() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox.length > 1 || cbox == "") {
		layer.alert("只能选中一个");
		return;
	}
	pageii = layer.open({
		title : "编辑",
		type : 2,
		area : [ "600px", "80%" ],
		content : rootPath + '/resources/editUI.shtml?id=' + cbox
	});
}
function addFun() {
	pageii = layer.open({
		title : "新增",
		type : 2,
		area : [ "600px", "80%" ],
		content : rootPath + '/resources/addUI.shtml'
	});
}
function delFun() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.alert("请选择删除项！！");
		return;
	}
	layer.confirm('是否删除？', function(index) {
		var url = rootPath + '/resources/deleteEntity.shtml';
		var s = CommnUtil.ajax(url, {
			ids : cbox.join(",")
		}, "json");
		if (s == "success") {
			layer.msg('删除成功');
			grid.loadData();
		} else {
			layer.msg('删除失败');
		}
	});
}
