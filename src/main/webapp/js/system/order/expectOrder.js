var editId;
var pageii = null;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/order/findExpectOrderByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
        {field:'fullName',title:'客户',width:180,align:'center',editor:'text'},
        {field:'contractNumber',title:'订单编号',width:230,align:'center',editor:'text'},
        {field:'mapNumber',title:'图号',width:230,align:'center',editor:'text'},
        {field:'goodName',title:'产品名称',width:230,align:'center',editor:'text'},
        {field:'goodSize',title:'成品尺寸',width:230,align:'center',editor:'text'},
        {field:'makeTime',title:'来单日期',width:230,align:'center',editor:'text'},
        {field:'deliveryTime',title:'交货日期',width:230,align:'center',editor:'text'},
        {field:'amount',title:'订单数量',width:230,align:'center',editor:'text'},
        {field:'alreadysendAmount',title:'已发货数量',width:230,align:'center',editor:'text'},
        {field:'unsendAmount',title:'未发货数量',width:230,align:'center',editor:'text'},
    ]];
    $('#'+id).datagrid({
        url:rootPath + url,
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
        columns:columns,
        onLoadSuccess:function(data){
            setContentToDivSpanById("all","订单数量合计:"+data.sum.sumAmount+"  已发货数量合计:"+data.sum.sumAlreadysendAmount+"  未发货数量合计:"+data.sum.sumUnsendAmount);
        }
    });
});
function find() {
    $("#tt").datagrid("load",{
        "clientId":getValueById("clientId"),
        "goodName":getContentBySelect("goodName"),
        "endTime":getDataboxValue("endtime"),
        "startTime":getDataboxValue("starttime"),
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
        communateGet(rootPath +'/stock/deleteEntity.shtml?ids='+ids,function back(data){
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
        communatePost(rootPath +'/stock/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
