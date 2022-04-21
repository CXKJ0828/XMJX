var editId;
var pageii = null;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/material/findByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
        {field:'materialQuality',title:'材质',width:230,align:'center',editor:'text'},
        {field:'outerCircle',title:'规格',width:230,align:'center',editor:'text'},
        {field:'taxPrice',title:'含税单价',width:180,align:'center',editor:'text'},
        {field:'remarks',title:'备注',width:180,align:'center',editor:'text'},
    ]];
    initDataGrid(id,url,columns,function (rowIndex, rowData) {
        if(editId!=null){
            endEditDatagridByIndex('tt',editId);
            editId=rowIndex;
            beginEditDatagridByIndex('tt',editId);
        }else{
            editId=rowIndex;
            beginEditDatagridByIndex('tt',editId);
        }
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
    editAndAddSetContentAndChange();

}

function  cancel() {
    $('#tt').datagrid('rejectChanges');
}
function  del() {
    endEditDatagridByIndex('tt',editId);
    var rows=getDatagridSelections('tt');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    if(ids!=""){
        communateGet(rootPath +'/material/deleteEntity.shtml?ids='+ids,function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function  save() {
    endEditDatagridByIndex('tt',editId);
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/material/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
