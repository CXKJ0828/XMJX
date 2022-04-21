var editId;
var pageii = null;
var clientName="";
var myArray=new Array();
var datagridDetailsArray=new Array();

$(function() {
    initComboGridEditor();

});
function initTT(clientId) {
    id='tt';
    url='/order/findProductStatisticsByPage.shtml?clientDefault='+clientId;
    var frozencolumns=new Array();
    frozencolumns.push(new DatagridRow('process','工种',80,'center'));
    var columns=new Array();
    for(i=1;i<32;i++){
        columns.push(new DatagridRow('amount'+i,i+'号<br>总数-接收量-完成量',120,'center'));
    }
    columns.push(new DatagridRow('amountSum','合计<br>总数-接收量-完成量',120,'center'));
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        singleSelect:false,
        frozenColumns:[frozencolumns],
        columns:[columns]
    });


    id='ttselect';
    url='';
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        frozenColumns:[frozencolumns],
        columns:[columns],

    });
}
function find() {
    $("#tt").datagrid("load",{
        "clientId":getValueById("clientId"),
        "month":getDataboxValue("month")
    });
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

    var fileName=clientName+'生产统计导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var data={
        "clientId":getValueById("clientId"),
        "clientName":clientName,
        "month":getDataboxValue("month")
    }
    downfileByUrl(rootPath +'/order/exportProductStatisticsAll.shtml?entity='+ListToJsonString(data));
}