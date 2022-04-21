$(function() {
    $('#tt').datagrid({
        url:rootPath + '/user/findByPage.shtml',
        queryParams:{
            // 'roleFormMap.total':total
        },
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        singleSelect : false,
        fit:true,
        pagination:true,
        pagePosition:top,
        columns:[[
            {field:'name',title:'角色名',width:100,align:'center',editor:'text'},
            {field:'roleKey',title:'唯一key',width:100,align:'center',editor:'text'},
            {field:'state',title:'状态',width:100,editor:'text',editor:'text'},
            {field:'description',title:'描述',width:100,align:'center',editor:'text'},
        ]],
        onLoadSuccess:function(data){
        },
    });
});
var listEditing=new Array();
function add() {
    $('#tt').datagrid('appendRow',{
    });
    editIndex = $('#tt').datagrid('getRows').length-1;
    $('#tt').datagrid('selectRow', editIndex)
        .datagrid('beginEdit', editIndex);
    listEditing.push(editIndex);
}

function  cancel() {
    editIndex =listEditing[listEditing.length-1];
    if(editIndex==undefined){
        $.messager.alert('警告','不存在可以编辑数据','warning');
    }else{
        $('#tt').datagrid('cancelEdit', editIndex)
            .datagrid('deleteRow', editIndex);
        listEditing.remove(editIndex);
    }

}
function  del() {
    var selectList=$('#tt').datagrid('getSelections');
    if(selectList.length>0){
        $.messager.confirm('系统提示', '删除节点，您确定要删除吗?', function (r) {
            if (r) {
                var rows = $('#tt').datagrid('getSelections');
                var ids="";
                for(i=0;i<rows.length;i++){
                    editingId=rows[i].id;
                    if(i==rows.length-1){
                        ids=ids+editingId;
                    }else{
                        ids=ids+editingId+",";
                    }

                }
                if(rows.length>0){
                    communateGet(rootPath +'/role/deleteEntity.shtml?ids='+ids,function back(data){
                        reloadMessage();
                    });
                }
            }
        });
    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function reloadMessage() {
    $('#tt').datagrid('reload');//刷新
    $('#tt').datagrid('clearSelections');
    $('#tt').datagrid('clearChecked');
}
function  save() {
    for(i=0;i<listEditing.length;i++){
        $('#tt').datagrid('endEdit', listEditing[i]);//结束编辑
    }

    var rows = $('#tt').datagrid('getChanges', 'inserted');
    if(rows.length>0){
        communatePost(rootPath +'/role/addEntity.shtml',ListToJsonString(rows),function back(data){
            reloadMessage();
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
