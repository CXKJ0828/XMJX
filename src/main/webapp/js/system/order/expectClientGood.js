var editId;
var pageii = null;
var myArray=new Array();
var datagridDetailsArray=new Array();
function onExpandRow(index, row) {
    var url=rootPath+'/order/findClientGoodDetailsByPage.shtml?goodId='+row.goodId
        +"&clientId="+row.clientId
        +"&month="+getDataboxValue("month");
    var columns=[[
        {field:'id',hidden:true,title:'编号',width:100,align:'center'},
        {field:'fullName',title:'客户',width:100,align:'center'},
        {field:'contractNumber',title:'订单编号',width:100,align:'center'},
        {field:'mapNumber',title:'图号',width:100,align:'center'},
        {field:'goodName',title:'产品名称',width:100,align:'center'},
        {field:'makeTime',title:'来单日期',width:100,align:'center'},
        {field:'deliveryTime',title:'交货日期',width:100,align:'center'},
        {field:'amount',title:'订单数量',width:100,align:'center'},
        {field:'alreadysendAmount',title:'已发货数量',width:230,align:'center',editor:'text'},
        {field:'unsendAmount',title:'未发货数量',width:230,align:'center',editor:'text'},
    ]];
    var parentDatagridId='tt';
    if(row.id!=undefined){
        if(myArray[index]){
            myArray[index]=false;
            $('#ddv-' + index).datagrid({
                url:url,
                fitColumns: true,
                singleSelect: true,
                height: 'auto',
                columns:columns,
                onResize: function () {
                    $('#'+parentDatagridId).datagrid('fixDetailRowHeight', index);
                },
                rowStyler: function(index,row){
                    var isFinish=row.isFinish;
                    if (isFinish=='已接收未完成'){
                        return 'color:#ff0030;font-weight:bold';
                    }else if(isFinish=='已完成'){
                        return 'color:#048133;font-weight:bold';//绿色加粗字体
                    }
                },
                onLoadSuccess: function () {
                    setTimeout(function () {
                        $('#'+parentDatagridId).datagrid('fixDetailRowHeight', index);
                    }, 0);
                }
            });
            $('#'+parentDatagridId).datagrid('fixDetailRowHeight', index);

        }
    }
}


$(function() {
    initComboGridEditor();

});
function initTT(clientId) {
    id='tt';
    url='/order/findClientGoodByPage.shtml?clientDefault='+clientId;
    var frozencolumns=new Array();
    frozencolumns.push(new DatagridRow('fullName','客户',100,'center'));
    frozencolumns.push(new DatagridRow('mapNumber','图号',80,'center'));
    frozencolumns.push(new DatagridRow('goodName','产品名称',80,'center'));
    frozencolumns.push(new DatagridRow('goodSize','成品尺寸',80,'center'));
    var columns=new Array();
    for(i=1;i<32;i++){
        columns.push(new DatagridRow('amount'+i,i+'号<br>订单数-发货数',80,'center'));
    }
    columns.push(new DatagridRow('amountSum','合计<br>订单数-发货数',80,'center'));
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        singleSelect:false,
        pagination:true,
        frozenColumns:[frozencolumns],
        columns:[columns],
        onLoadSuccess:function(data){
            setContentToDivSpanById("all","订单数合计:"+data.sumEntity.amountSum+"  交货数合计:"+data.sumEntity.alreadysendAmountSum)
        },
        view:detailview,
        detailFormatter:detailFormatter,
        onExpandRow:onExpandRow
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
        "mapNumber":getValueById("mapNumber"),
        "goodName":getContentBySelect("goodName"),
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

    var fileName='预交货统计导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var data={
        "clientId":getValueById("clientId"),
        "mapNumber":getValueById("mapNumber"),
        "month":getDataboxValue("month")
    }
    downfileByUrl(rootPath +'/order/exportClientGoodAll.shtml?entity='+ListToJsonString(data));
}