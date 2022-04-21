var editId;
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/materialQualityType/findByPage.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        nowrap:false,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        fit:true,
        onDblClickRow:function (rowIndex, rowData) {
            if(editId!=null){
                endEditDatagridByIndex('tt',editId)
                editId=rowIndex;
                var row = getDatagridJsonByIndexAndId('tt',editId);
                setContentToInputByJson(row);
                beginEditDatagridByIndex('tt',editId);
            }else{
                editId=rowIndex;
                var row = getDatagridJsonByIndexAndId('tt',editId);
                setContentToInputByJson(row);
                beginEditDatagridByIndex('tt',editId);
            }
        },
        columns:[[
            {field:'id',hidden:true,title:'编号',width:150,align:'center',editor:'text'},
            {field:'name',title:'名称',width:300,align:'center',editor:'text'},
        ]],
        onLoadSuccess:function(data){
        },
    });
});
function find() {
    $("#tt").datagrid("load",{
        "content":getValueById("content")
    });
}
function add() {
    var rows = $('#tt').datagrid('getRows');
    $('#tt').datagrid('appendRow',
        {
        }
    );
}

function  cancel() {
    $('#tt').datagrid('rejectChanges');
}
function  del() {
    var rows=getDatagridSelections('tt');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    if(ids!=""){
        $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
            if (r){
                communateGet(rootPath +'/materialQualityType/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
                    reloadDatagridMessage('tt');
                });
            }
        });

    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function  save() {
    endEditDatagridByIndex('tt',editId)
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/materialQualityType/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
