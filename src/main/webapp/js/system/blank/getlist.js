var editId;
var pageii = null;
var amountSum=0;
var wagesSum=0;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/blank/findGetList.shtml';
    columns=[[
        {field:'contractNumber',title:'订单编号',width:150,align:'center'},
        {field:'mapNumber',title:'图号',width:150,align:'center'},
        {field:'goodName',title:'产品名称',width:150,align:'center'},
        {field:'materialqualityName',title:'材质',width:150,align:'center'},
        {field:'processName',title:'工序',width:150,align:'center'},
        {field:'accountShow',title:'接收人',width:150,align:'center'},
        {field:'goodSize',title:'产品尺寸',width:150,align:'center'},
        {field:'blankSize',sortable:true,title:'下料尺寸',width:150,align:'center'},
        {field:'amount',title:'接收数量',width:150,align:'center'},
        {field:'artificial',title:'单价工时',width:150,align:'center'},
        {field:'distributionWages',title:'对应工资',width:100,align:'center',editor:'text'},
        {field:'startTime',title:'接收时间',width:150,align:'center',editor:'text'},
        {field:'planneedDay',title:'预计用时(天)',width:150,align:'center'},
    ]];
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        singleSelect:false,
        pagination:true,
        onCheck:function (rowIndex,rowData) {
            amountSum=amountSum+VarToInt(rowData.amount);
            wagesSum=wagesSum+VarToFloat(rowData.distributionWages);
            setContentToDivSpanById("amountSum","数量合计:"+amountSum+" 对应工资合计:"+floatToVar2(wagesSum));
        },
        onUncheck:function (rowIndex,rowData) {
            if(amountSum>0){
                amountSum=amountSum-VarToInt(rowData.amount);
                wagesSum=wagesSum-VarToFloat(rowData.distributionWages);
                setContentToDivSpanById("amountSum","数量合计:"+amountSum+" 对应工资合计:"+floatToVar2(wagesSum));
            }else{
                setContentToDivSpanById("amountSum","");
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
            editAndAddSetContentAndChange();

            setContentToInputById("clientId",rowData.clientId);
            var ed=getDatagridEditorObj('tt',editId,'goodMapNumber');
            if(ed!=null){
                ed.combogrid('grid').datagrid('options').url =  '${pageContext.request.contextPath}/good/goodSelectByClientId.shtml?clientId='+getValueById('clientId')
                ed.combogrid('grid').datagrid('reload');
            }
        },
        columns:columns,
        onLoadSuccess:function(data){
            setContentToDivSpanById("allMoney","接收合计:"+data.money.amount)
        },
    });


    idexport='ttselect';
    url='/blank/findGetListAll.shtml?content=空值';
    columns=[[
        {field:'contractNumber',title:'订单编号',width:150,align:'center'},
        {field:'mapNumber',title:'图号',width:150,align:'center'},
        {field:'goodName',title:'产品名称',width:150,align:'center'},
        {field:'goodSize',title:'产品尺寸',width:150,align:'center'},
        {field:'blankSize',sortable:true,title:'下料尺寸',width:150,align:'center'},
        {field:'materialqualityName',title:'材质',width:150,align:'center'},
        {field:'processName',title:'工序',width:150,align:'center'},
        {field:'accountShow',title:'接收人',width:150,align:'center'},
        {field:'amount',title:'接收数量',width:150,align:'center'},
        {field:'startTime',title:'接收时间',width:150,align:'center',editor:'text'},
    ]];
    initDataGridPaginationFalseToolbarFalse(idexport,url,columns,function (rowIndex, rowData) {

    });
});
function find() {
    $("#tt").datagrid("load",{
        "user":getValueById("user"),
        "content":getValueById("content"),
        "startTime":getDataboxValue("starttime"),
        "endTime":getDataboxValue("endtime")
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
    clearDatagridSelections("tt");
    amountSum=0;
    wagesSum=0;
    setContentToDivSpanById("amountSum","数量合计:"+amountSum+" 应得工资合计:"+floatToVar2(wagesSum));
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
                communateGet(rootPath +'/blank/deleteEntityGetList.shtml?ids='+ids+'&password='+r,function back(data){
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
function  downLoad() {
    var rows = $('#ttselect').datagrid('getRows');
    for (var i=rows.length;i>0;i--){
        $('#ttselect').datagrid('deleteRow',i-1);
    }

    var rows=getDatagridSelections("tt");
    for(i=0;i<rows.length;i++){
        $('#ttselect').datagrid('appendRow',
            rows[i]
        );
    }
    var fileName='已接收列表导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var data={
        "user":getValueById("user"),
        "content":getValueById("content"),
        "startTime":getDataboxValue("starttime"),
        "endTime":getDataboxValue("endtime")
    }
    downfileByUrl(rootPath +'/blank/exportGetlist.shtml?entity='+ListToJsonString(data));
}