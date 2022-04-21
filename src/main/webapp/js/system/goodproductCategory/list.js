var editId;
var pageii = null;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/goodproductCategory/findByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
        {field:'clientName',title:'客户',width:230,align:'center',editor:{type:'combogrid',
            options: {panelWidth:200,
                idField: 'simpleName',
                textField: 'simpleName',
                url: rootPath+'/client/clientSelect.shtml',
                mode: 'remote',
                columns: [[
                    {field:'id',hidden:true,title:'客户编码',width:100},
                    {field:'simpleName',title:'客户简称',width:350},
                ]],
                fitColumns: true,
                onSelect:selectClient
            }
        }},
        {field:'mapNumber',title:'图号',width:180,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 400,
                idField: 'mapNumber',
                textField: 'mapNumber',
                pagination:true,
                url: rootPath +'/good/goodSelect.shtml',
                mode: 'remote',
                columns: [[
                    {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                    {field:'mapNumber',title:'图号',width:100,align:'center'},
                ]],
                fitColumns: true,
                onSelect:selectGood
            }
        }},
        {field:'goodName',title:'产品名称',width:200,align:'center',editor:'text'},
        {field:'goodSize',title:'成品尺寸',width:180,align:'center',editor:'text'},
        {field:'materialQualityName',title:'材质',width:150,align:'center',editor:'text'},
        {field:'amount',title:'数量',width:120,align:'center',editor:'text'},
        {field:'deliveryTime',sortable:true,title:'交货截止日期',width:200,align:'center',editor:'datebox'},
        {field:'receiptTime',sortable:true,title:'回单截止日期',width:200,align:'center',editor:'datebox'},
        {field:'leader',title:'负责人',width:150,align:'center',editor:'text'},
        {field:'isComplete',title:'是否完成',width:180,align:'center'},
        {field:'completeTime',title:'完成日期',width:180,align:'center'},
        {field:'completeAmount',title:'完成数量',width:180,align:'center',editor:'text'},
        {field:'badAmount',title:'废品数量',width:180,align:'center'},
        {field:'badUserName',title:'废品责任人',width:150,align:'center',editor:'text',editor:{type:'combogrid',
            options: {panelWidth: 400,
                idField: 'userName',
                textField: 'userName',
                url: rootPath+'/user/userSelect.shtml',
                method: 'get',
                columns: [[
                    {field:'accountName',title:'账号',width:120,align:'center'},
                    {field:'userName',title:'姓名',width:120,align:'center'},
                ]],
                fitColumns: true,
                onSelect:selectBadUser
            }
        } },
        {field:'isOverTime',title:'是否超时',width:180,align:'center'},
        {field:'overTimeUserName',title:'超时责任人',width:150,align:'center',editor:'text',editor:{type:'combogrid',
            options: {panelWidth: 400,
                idField: 'userName',
                textField: 'userName',
                url: rootPath+'/user/userSelect.shtml',
                method: 'get',
                columns: [[
                    {field:'accountName',title:'账号',width:120,align:'center'},
                    {field:'userName',title:'姓名',width:120,align:'center'},
                ]],
                fitColumns: true,
                onSelect:selectOverTimeUser
            }
        } },
        {field:'isBack',title:'是否回单',width:180,align:'center'},
        {field:'backTime',title:'回单日期',width:180,align:'center'},
        {field:'reason',title:'原因',width:230,align:'center',editor:'text'},
        {field:'goodId',hidden:true,title:'产品id',width:230,align:'center',editor:'text'},
        {field:'clientId',hidden:true,title:'客户id',width:230,align:'center',editor:'text'},
        {field:'badUserId',hidden:true,title:'废品责任人id',width:230,align:'center',editor:'text'},
        {field:'overTimeUserId',hidden:true,title:'超时责任人id',width:230,align:'center',editor:'text'},
    ]];
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
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
            var origin=getValueById('origin');
            params.origin=origin;
            params.reason=getValueById('reason');
            params.content=getValueById('content');
            params.materialQuality=getContentBySelect("materialQuality");
            params.startTimeDelivery=getDataboxValue('startTimeDelivery');
            params.endTimeDelivery=getDataboxValue('endTimeDelivery');
            params.startTimeComplete=getDataboxValue('startTimeComplete');
            params.endTimeComplete=getDataboxValue('endTimeComplete');
            params.startTimeBack=getDataboxValue('startTimeBack');
            params.endTimeBack=getDataboxValue('endTimeBack');
            params.startTimeReceipt=getDataboxValue('startTimeReceipt');
            params.endTimeReceipt=getDataboxValue('endTimeReceipt');
            params.leader=getContentBySelect("leader");
            params.clientId=getContentBySelect("clientId");
            params.isComplete=getContentBySelect("isComplete");
            params.isBack=getContentBySelect("isBack");
            params.isOverTime=getContentBySelect("isOverTime");
            params.isBad=getContentBySelect("isBad");
            params.overTimeUserId=getContentBySelect("overTimeUserId");
            params.badUserId=getContentBySelect("badUserId");
        },
        rowStyler: function(index,row){
            var isComplete=row.isComplete;
            var isOverTime=row.isOverTime;
            if (isComplete=='是'&&isOverTime=='是'){

                return 'color:#DC143C;font-weight:bold';//红色
            }else if(isComplete=='是'){
                return 'color:#048133;font-weight:bold';//绿色
            }
        },
        onDblClickRow:function (rowIndex, rowData) {
            if(editId!=null){
                endEditDatagridByIndex('tt',editId);
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }else{
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }
        },
        columns:columns,
        onLoadSuccess:function(data){
            setContentToDivSpanById("sumShow","完成合计:"+data.sumEntity.completeCount+"未完成合计:"+data.sumEntity.unCompleteCount
                +"超时天数合计:"+(data.sumEntity.deliveryoverTimeDaySum+data.sumEntity.receiptoverTimeDaySum)
                +"扣款合计:"+(data.sumEntity.deliveryoverTimeMoney+data.sumEntity.receiptoverTimeMoney)
                +"未完成数量合计:"+data.sumEntity.unCompleteSum
                +"完成数量合计:"+data.sumEntity.completeSum
                +"废品数量合计:"+data.sumEntity.badAmountSum
                );
        },
    });
});
function find() {
   reloadDatagridMessage('tt');
    reloadTreeMessage("tt1");
}
function add() {
    var rows = $('#tt').datagrid('getRows');
    $('#tt').datagrid('appendRow',
        {
        }
    );
    editAndAddSetContentAndChange();

}
function selectBadUser(rowIndex, rowData) {
    setContentToDatagridEditorText('tt',editId,"badUserId",rowData.id);
    setContentToDatagridEditorText('tt',editId,"badUserName",rowData.userName);
}
function selectOverTimeUser(rowIndex, rowData) {
    setContentToDatagridEditorText('tt',editId,"overTimeUserId",rowData.id);
    setContentToDatagridEditorText('tt',editId,"overTimeUserName",rowData.userName);
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
                communateGet(rootPath +'/goodproductCategory/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
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
        communatePost(rootPath +'/goodproductCategory/editEntity.shtml?origin='+getValueById("origin"),ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }
}

function upload() {
    $.messager.progress({
        title:'Please waiting',
        msg:'Loading data...'
    });
    $('#form').form({
        url:rootPath + '/goodproductCategory/upload.shtml?origin='+getValueById("origin"),
        onSubmit: function(){
            return $(this).form('validate');
        },
        success:function(data){
            $.messager.progress('close');
            var jsonObj = eval( '(' + data + ')' ); // eval();方法
            if(jsonObj=="success"){
                showMessagerCenter("提示","导入成功");
                reloadDatagridMessage('tt');
                reloadTreeMessage('tt1');
            }else{
                showErrorAlert("警告",jsonObj);
                reloadDatagridMessage('tt');
            }
        }
    });
// submit the form
    $('#form').submit();
}
