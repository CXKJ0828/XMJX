var editId;
var pageii = null;
var clientId="";
var processId="";
$(function() {
    initComboGridEditor();
    id='tt';
    url='';
    columns=[[
        {field:'code',title:'编号',width:100,align:'center'},
        {field:'deliveryTime',title:'交货日期',width:80,align:'center'},
        {field:'fullName',title:'客户',width:120,align:'center'},

        {field:'goodId',hidden:true,title:'goodId',width:80,align:'center'},
        {field:'goodprocessId',hidden:true,title:'goodprocessId',width:80,align:'center'},
        {field:'processId',hidden:true,title:'processId',width:80,align:'center'},
        {field:'contractNumber',title:'订单号',width:100,align:'center'},
        {field:'mapNumber',title:'图号',width:80,align:'center'},
        {field:'goodName',title:'产品名称',width:80,align:'center'},
        {field:'blankSize',title:'下料尺寸',width:80,align:'center'},
        {field:'materialQualityName',title:'材质',width:80,align:'center'},
        {field:'number',title:'工序号',width:50,align:'center'},
        {field:'processName',title:'工序',width:70,align:'center'},
        {field:'amount',title:'数量',width:40,align:'center',editor:'text'},
        {field:'unreceiveAmount',title:'未接收数量',width:80,align:'center',editor:'text'},
        {field:'state',title:'状态',width:50,align:'center',editor:'text',formatter: function(value,row,index){
        if (row.state=='已完成'){
            completeAmount=row.completeAmount;
                return '已完成:'+completeAmount;

        } else {
            return row.state;
        }
    }},
        {field:'badAmount',hidden:true,title:'废品数量',width:80,align:'center'},
        {field:'completeAmount',hidden:true,title:'完成数量',width:80,align:'center',editor:'text'},
        {field:'content',title:'加工内容',width:400,align:'center',editor:'text'}
    ]];
    initDataGridNotFit(id,url,columns,function (rowIndex, rowData) {
        if(editId!=null){
            endEditDatagridByIndex('ttSelect',editId);
        }else{
        }

        rowData.nowreceiveAmount=rowData.unreceiveAmount;
        $('#ttSelect').datagrid('appendRow',
            rowData
        );

        var length=getDatagridRows('ttSelect').length;
        editId=length-1;
        beginEditDatagridByIndex('ttSelect',length-1);
    });


    id='ttSelect';
    url='';
    columns=[[

        {field:'fullName',title:'客户',width:120,align:'center'},
        {field:'contractNumber',title:'订单号',width:100,align:'center'},
        {field:'mapNumber',title:'图号',width:80,align:'center'},
        {field:'number',title:'工序号',width:50,align:'center'},
        {field:'processName',title:'工序',width:80,align:'center'},
        {field:'content',hidden:true,title:'加工内容',width:100,align:'center'},
        {field:'amount',title:'数量',width:40,align:'center'},
        {field:'unreceiveAmount',title:'未接收数量',width:70,align:'center'},
        {field:'nowreceiveAmount',title:'当前接收数量',width:80,align:'center',editor:'text'},
        {field:'state',title:'状态',width:50,align:'center',editor:'text',formatter: function(value,row,index){
            if (row.state=='已完成'){
                completeAmount=row.completeAmount;
                return '已完成:'+completeAmount;

            } else {
                return row.state;
            }
        }},
        {field:'completeAmount',hidden:true,title:'完成数量',width:80,align:'center'},
        {field:'startQRCode',hidden:true,title:'接收码',width:80,align:'center'},
        {field:'endQRCode',hidden:true,title:'完成码',width:80,align:'center'}
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
    var url= rootPath+'/blank/findTechnologyByClientOrderGoodProcess.shtml?clientId='+clientId
    +'&contractNumber='+getValueById("contractNumber")
        +"&code="+code
    +'&mapNumber='+getValueById("mapNumber")
        +'&startTime='+getDataboxValue("starttime")
        +'&endTime='+getDataboxValue("endtime")
        +'&materialQuality='+materialQuality
    +'&goodName='+getContentBycombogrid("goodName")
        +'&blankSize='+getValueById("blankSize")
    +'&state=未完成'
    +'&processId='+processId;
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
    // var rows= $('#ttSelect').datagrid('getChanges');
    var rows=getDatagridRows('ttSelect');
    if(rows.length>0){
        communatePost(rootPath +'/blank/inputReceiveEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
