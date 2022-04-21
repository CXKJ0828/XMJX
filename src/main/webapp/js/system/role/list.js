var editId;
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/role/findByPage.shtml',
        queryParams:{
           // 'roleFormMap.total':total
        },
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        fit:true,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        singleSelect:false,
        pagination:true,
        pageSize:defaultPageNum,
        checkOnSelect:false,
        selectOnCheck:false,
        columns:[[
            {field:'id',title:'编号',width:50,align:'center',checkbox:true},
            {field:'name',title:'角色名',width:50,align:'center',editor:'text'},
            {field:'roleKey',title:'唯一key',width:50,align:'center',editor:'text'},
            {field:'state',title:'状态',width:50,editor:'text',editor:'text'},
            {field:'description',title:'描述',width:50,align:'center',editor:'text'},
        ]],
        onLoadSuccess:function(data){
        },
        onClickRow:function (rowIndex, rowData) {
            if(editId!=null){
                endEditDatagridByIndex('tt',editId);
            }
            editId=rowIndex;
            beginEditDatagridByIndex('tt',editId);
        },
        onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
            e.preventDefault(); //阻止浏览器捕获右键事件
            if(editId!=null){
                endEditDatagridByIndex('tt',editId)
            }
            editId=rowIndex;
            showMenu("rightMenuWatch",e.pageX,e.pageY);
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出
        },
    });
});
function add() {
    $('#tt').datagrid('appendRow',{}
    );
}

function  cancel() {
    endEditDatagridByIndex("tt",editId)
}
function  del() {
    showconfirmDialog("提示","是否确认删除?",function (r) {
        if(r){
            var selectList=$('#tt').datagrid('getChecked');
            if(selectList.length>0){
                var ids="";
                for(i=0;i<selectList.length;i++){
                    ids=ids+selectList[i].id+",";
                }
                if(ids!=""){
                    communateGet(rootPath +'/role/deleteEntity.shtml?ids='+ids,function back(data){
                        reloadMessage();
                    });
                }
            }else{
                $.messager.alert('警告','不存在可以删除数据','warning');
            }
        }
    });
}

function permissions(){
    var selectList=$('#tt').datagrid('getChecked');
    if(selectList.length!=1){
        $.messager.alert('警告','请选择一个角色','warning');
    }else{
        $("#add").window({
            width: 600,
            modal: true,
            height: 400,
            top:50,
            href: rootPath + '/resources/permissionsOpration.shtml?roleId='+selectList[0].id,
            onClose:function () {
                $('#tt').datagrid("reload");
            }
        });
    }
}

function reloadMessage() {
    $('#tt').datagrid('reload');//刷新
    $('#tt').datagrid('clearSelections');
    $('#tt').datagrid('clearChecked');
}
function  save() {
    if(editId!=null){
        endEditDatagridByIndex('tt',editId);
    }

    var rows = $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath + '/role/addEntity.shtml',ListToJsonString(rows),function back(data){
            reloadMessage();
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
