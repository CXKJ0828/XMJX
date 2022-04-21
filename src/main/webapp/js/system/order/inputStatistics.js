var editId;
var pageii = null;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/order/findInputStatisticsByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
        {field:'clientName',title:'客户',width:180,align:'center',editor:'text'},
        {field:'contractNumber',title:'订单编号',width:230,align:'center',editor:'text'},
        {field:'mapNumber',title:'图号',width:230,align:'center',editor:'text'},
        {field:'goodName',title:'产品名称',width:230,align:'center',editor:'text'},
        {field:'goodSize',title:'成品尺寸',width:230,align:'center',editor:'text'},
        {field:'amount',title:'入库数量',width:230,align:'center',editor:'text'},
        {field:'userName',title:'入库人',width:230,align:'center',editor:'text'},
        {field:'modifyTime',title:'入库时间',width:230,align:'center',editor:'text'},
    ]];
    initDataGrid(id,url,columns,function (rowIndex, rowData) {
    });
});
function find() {
    $("#tt").datagrid("load",{
        "clientId":clientId,
        "contractNumber":getValueById("contractNumber"),
        "mapNumber":getValueById("mapNumber"),
        "modifyTime":getDataboxValue("modifyTime"),
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
        $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
            if (r){
                communateGet(rootPath +'/order/deleteSendInputEntity.shtml?ids='+ids+'&password='+r,function back(data){
                    if(data=='success'){
                        reloadDatagridMessage('tt');
                    }else{
                        showErrorAlert("操作",data);
                    }
                });
            }
        });
    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function  save() {
    endEditDatagridByIndex('tt',editId);
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/stock/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
