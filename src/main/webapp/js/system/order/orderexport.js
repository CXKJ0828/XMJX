var editId;
var orderCode="";
var orderName="";
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/order/getProcessStateAndProcessByOrderId.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        fit:true,
        singleSelect:true,
        columns:[[
            { field: 'goodId', title: '件号', width: 80, align: 'center',editor:'text' },
            { field: 'goodName', title: '件名', width: 80, align: 'center',editor:'text' },
            { field: 'amount', title: '数量', width: 50, align: 'center',editor:'text' },
            { field: 'processName1', title: '工序', width: 80, align: 'center',editor:'text' },
            { field: 'state1', title: '完成时间', width: 80, align: 'center',editor:'text' },
            { field: 'oprateCompleter1', title: '操作者', width: 80, align: 'center',editor:'text' },
            { field: 'ordergoodcode1', title: '产品编号', width: 80, align: 'center',editor:'text' },
            { field: 'processName2', title: '工序', width: 80, align: 'center',editor:'text' },
            { field: 'state2', title: '完成时间', width: 80, align: 'center',editor:'text' },
            { field: 'oprateCompleter2', title: '操作者', width: 80, align: 'center',editor:'text' },
            { field: 'ordergoodcode2', title: '产品编号', width: 80, align: 'center',editor:'text' },
            { field: 'processName3', title: '工序', width: 80, align: 'center',editor:'text' },
            { field: 'state3', title: '完成时间', width: 80, align: 'center',editor:'text' },
            { field: 'oprateCompleter3', title: '操作者', width: 80, align: 'center',editor:'text' },
            { field: 'ordergoodcode3', title: '产品编号', width: 80, align: 'center',editor:'text' },
            { field: 'processName4', title: '工序', width: 80, align: 'center',editor:'text' },
            { field: 'state4', title: '完成时间', width: 80, align: 'center',editor:'text' },
            { field: 'oprateCompleter4', title: '操作者', width: 80, align: 'center',editor:'text' },
            { field: 'ordergoodcode4', title: '产品编号', width: 80, align: 'center',editor:'text' },
            { field: 'processName5', title: '工序', width: 80, align: 'center',editor:'text' },
            { field: 'state5', title: '完成时间', width: 80, align: 'center',editor:'text' },
            { field: 'oprateCompleter5', title: '操作者', width: 80, align: 'center',editor:'text' },
            { field: 'ordergoodcode5', title: '产品编号', width: 80, align: 'center',editor:'text' },
        ]],
        onLoadSuccess:function(data){
            $('#tt').datagrid('appendRow', {
                goodId: '<span style="font-weight: bold;">工号：</span>',
                goodName: '<span style="font-weight: bold;">'+orderCode+'</span>',
                amount:'<span style="font-weight: bold;">'+orderName+'</span>',
                processName1: '',
                state1: '',
                processName2: '',
                state2: '',
                processName3: '',
                state3: '',
                processName4: '',
                state4: '',
                processName5: '',
                state5: '',
            });
        }
    });
});

function find() {
    id=getContentBySelect('orderId');
    $("#tt").datagrid("load",{
        "id":id
    });
}

function  downLoad() {
    var fileName=orderCode+orderName+'订单统计导出.xls';
    $('#tt').datagrid('toExcel',fileName);

    // var titleData=getDonwExcelTitleData("tt");
    // location.href=rootPath + '/order/exportOrderStatistics.shtml?title='+ListToJsonString(titleData)+"&fileName="+fileName+"&id="+id+"&sheetName="+orderCode+orderName;
}
