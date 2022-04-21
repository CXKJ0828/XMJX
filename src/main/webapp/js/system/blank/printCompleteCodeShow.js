var editId;
var pageii = null;
var clientId="";
var processId="";
$(function() {
    initComboGridEditor();
    id='tt';
    url='';
    columns=[[
        {field:'clientName',title:'客户',width:120,align:'center'},
        {field:'contractNumber',title:'订单号',width:100,align:'center'},
        {field:'mapNumber',title:'图号',width:80,align:'center'},
        {field:'number',title:'工序号',width:50,align:'center'},
        {field:'processName',title:'工序',width:70,align:'center'},
        {field:'content',title:'加工内容',width:300,align:'center'},
        {field:'amount',title:'数量',editor:'text' ,width:50,align:'center'},
        {field:'unreceiveAmount',editor:'text' ,title:'未接收数量',width:80,align:'center'},
        {field:'badAmount',hidden:true,title:'废品数量',width:80,align:'center'},
        {field:'completeAmount',hidden:true,title:'完成数量',width:80,align:'center'},
        { field: 'userName', title: '接收人', width: 60, align: 'center' },
        { field: 'startTime',title: '接收时间',width:100, align: 'center' },
        { field: 'receiveAmount', title: '接收数量', width:80, align: 'center'},
        {field:'startQRCode',hidden:true,title:'接收码',width:80,align:'center'},
        {field:'endQRCode',hidden:true,title:'完成码',width:80,align:'center'},
    ]];
    initDataGridNotFit(id,url,columns,function (rowIndex, rowData) {
        $('#ttSelect').datagrid('appendRow',
            rowData
        );
    });


    id='ttSelect';
    url='';
    columns=[[
        {field:'clientName',title:'客户',width:120,align:'center'},
        {field:'contractNumber',title:'订单号',width:80,align:'center'},
        {field:'mapNumber',title:'图号',width:70,align:'center'},
        {field:'number',title:'工序号',width:50,align:'center'},
        {field:'processName',title:'工序',width:80,align:'center'},
        { field: 'id', title: '编号',hidden:true,align:'center', editor:'text' },
        { field: 'userName', title: '接收人', width: 50, align: 'center' },
        { field: 'startTime', title: '接收时间',hidden:true, width: 50, align: 'center',editor:'text' },
        { field: 'receiveAmount', title: '接收数量', width: 50, align: 'center'},
        { field: 'completeTime', title: '完成时间', width: 50, align: 'center' },
        {field:'startQRCode',hidden:true,title:'接收码',width:80,align:'center'},
        {field:'endQRCode',hidden:true,title:'完成码',width:80,align:'center'},
        { field: 'completeAmount', title: '完成数量', width: 50, align: 'center',editor:'text' },
        { field: 'badAmount', title: '废品数', width: 50, align: 'center',editor:'text' },
        { field: 'deductRate', title: '扣除比例', width: 50, align: 'center',editor:{
            type:'combobox',
            options:{
                valueField:'value',
                textField:'text',
                url:rootPath+'/json/deductRate.json',
            }
        } }
    ]];

    $('#ttSelect').datagrid({
        url:'',
        title:'选中展示',
        iconCls:'icon-ok',
        toolbar: '#tbSelect',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        onDblClickRow:function (rowIndex, rowData) {
            if(editId!=null){
                endEditDatagridByIndex('ttSelect',editId);
                }else{
                }

            editId=rowIndex;
            beginEditDatagridByIndex('ttSelect',editId);
        },
        onClickRow:function (rowIndex, rowData) {
        },
        columns:columns
    });
});
function find() {
    var url= rootPath+'/blank/findUnCompleteProcess.shtml?clientId='+clientId
    +'&contractNumber='+getValueById("contractNumber")
    +'&mapNumber='+getValueById("mapNumber")
    +'&userName='+getValueById("userName")
    +'&state=已接收未完成'
    +'&processId='+processId
    $('#tt').datagrid('options').url=url;
    $("#tt").datagrid('reload');
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
    var rows=getDatagridSelections('ttSelect');
    var length=rows.length;
    for(i=length-1;i>=0;i--){
        var index=$('#ttSelect').datagrid('getRowIndex',rows[i]);
        $('#ttSelect').datagrid('deleteRow',index);
    }
}
function  del() {
    var rows=getDatagridSelections('ttSelect');
    var length=rows.length;
    for(i=length-1;i>=0;i--){
        var index=$('#ttSelect').datagrid('getRowIndex',rows[i]);
        $('#ttSelect').datagrid('deleteRow',index);
    }
}
function  save() {
    endEditDatagridByIndex('ttSelect',editId);
    var rows= $('#ttSelect').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/blank/completeReceiveEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
