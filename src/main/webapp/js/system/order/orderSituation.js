var editId;
var pageii = null;
var goodId=0;
var clientId="";
var detailsStock="";
var isFirst=true;
$(function() {
    $('#tt').tree({
        url:rootPath + '/order/findorderSituation.shtml',
        method:'post',
        toolbar: '#tb',
        lines:true,
        animate:true,
        onClick:function (node) {
            clientId=node.id;
                var url=rootPath+'/order/findUnSendByClientId.shtml?clientId='+node.id;
                $('#goodprocessList').datagrid('options').url=url;
                $("#goodprocessList").datagrid('reload');
        }
    });
});
function find() {
    var contractNumber=getValueById("contractNumber");
    var mapNumber=getValueById("mapNumber");
    var url=rootPath+'/order/findUnSendByClientId.shtml?clientId='+clientId+"&contractNumber="+contractNumber+"&mapNumber="+mapNumber;
    $('#goodprocessList').datagrid('options').url=url;
    $("#goodprocessList").datagrid('reload');
    // $("#goodprocessList").datagrid("load",{
    //     "contractNumber":getValueById("contractNumber"),
    //      "mapNumber":getValueById("mapNumber"),
    // });
    orderdetailsId="0";
    var url=rootPath+'/order/findOrderSituation.shtml?orderdetailsId='+orderdetailsId+'&origin=订单';
    $('#blankProductSituation').datagrid('options').url=url;
    $("#blankProductSituation").datagrid('reload');

    var url=rootPath+'/order/findOrderSituation.shtml?orderdetailsId='+orderdetailsId+'&origin=补料';
    $('#feedProductSituation').datagrid('options').url=url;
    $("#feedProductSituation").datagrid('reload');


    var url=rootPath+'/blank/findByOriginAndOrderDetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
    $('#blankSituation').datagrid('options').url=url;
    $("#blankSituation").datagrid('reload');

    var url=rootPath+'/blank/findByOriginAndOrderDetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
    $('#feedSituation').datagrid('options').url=url;
    $("#feedSituation").datagrid('reload');
}
function add() {

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
        communateGet(rootPath +'/good/deleteEntity.shtml?ids='+ids,function back(data){
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
        communatePost(rootPath +'/good/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
