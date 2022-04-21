var editId;
var pageii = null;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/order/findSendStatisticsByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
        {field:'clientName',title:'客户',width:180,align:'center',editor:'text'},
        {field:'contractNumber',title:'订单编号',width:230,align:'center',editor:'text'},
        {field:'mapNumber',title:'图号',width:230,align:'center',editor:'text'},
        {field:'goodName',title:'产品名称',width:230,align:'center',editor:'text'},
        {field:'goodSize',title:'成品尺寸',width:230,align:'center',editor:'text'},
        {field:'amount',title:'发货数量',width:230,align:'center',editor:'text'},
        {field:'taxPrice',title:'单价',width:230,align:'center',editor:'text'},
        {field:'money',title:'金额',width:230,align:'center',editor:'text'},
        {field:'userName',title:'发货人',width:230,align:'center',editor:'text'},
        {field:'modifyTime',title:'发货时间',width:230,align:'center',editor:'text'},
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
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.clientId=clientId;
            params.goodName=getContentBySelect('goodName');
            params.contractNumber=getValueById('contractNumber');
            params.mapNumber=getValueById('mapNumber');
            params.starttime=getDataboxValue('starttime');
            params.endtime=getDataboxValue('endtime');
        },
        columns:columns,
        onLoadSuccess:function(data){
            var allMoney=0;
            var allAmount=0;
            // allMoney
            for(i=0;i<data.rows.length;i++){
                var money=data.rows[i].money;
               if(!isNull(money)){
                   allMoney=allMoney+parseFloat(money);
               }
                var amount=data.rows[i].amount;
                if(!isNull(amount)){
                    allAmount=allAmount+parseFloat(amount);
                }
            }
            allMoney=floatToVar2(allMoney);
            setContentToDivSpanById("all","发货数量合计:"+allAmount+"金额合计:"+allMoney);
        },
    });
});
function find() {
   reloadDatagridMessage("tt");
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
