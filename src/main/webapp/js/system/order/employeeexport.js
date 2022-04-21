var editId;
var userName="";
var starttime="";
var endtime="";
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/order/getDetailsWorkersumitByUserName.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        fit:true,
        fitColumns:true,
        columns:[[
            { field: 'orderId', title: '订单号', width: 50, align: 'center',editor:'text' },
            { field: 'ordername', title: '产品名称', width: 50, align: 'center',editor:'text' },
            { field: 'goodId', title: '件号', width: 50, align: 'center',editor:'text' },
            { field: 'goodName', title: '件名', width: 50, align: 'center',editor:'text' },
            { field: 'name', title: '工序', width: 50, align: 'center',editor:'text' },
            { field: 'submitAmount', title: '数量', width: 50, align: 'center',editor:'text' },
            { field: 'time', title: '工时', width: 50, align: 'center',editor:'text' },
            { field: 'submitTime', title: '完成时间', width: 50, align: 'center',editor:'text' },
        ]],
        onLoadSuccess:function(data){
            $('#tt').datagrid('appendRow', {
                orderId: '',
                goodId: '',
                goodName: '',
                name: '',
                submitAmount: '<span style="font-weight: bold;">工时合计</span>',
                time: '<span style="font-weight: bold;">' + compute('tt',"time") + '</span>',
                submitTime: '',
            });
            $('#tt').datagrid('appendRow', {
                orderId: '<span style="font-weight: bold;">姓名：'+userName+'</span>',
                goodId: '<span style="font-weight: bold;">时间：</span>',
                goodName: '<span style="font-weight: bold;">'+starttime+'至'+endtime+'</span>',
                name: '',
                submitAmount: '',
                time: '',
                submitTime: '',
            });
        }
    });
});

function find() {
    userName=getValueById("name");
    starttime=getDataboxValue("starttime");
    endtime=getDataboxValue("endtime");
    $("#tt").datagrid("load",{
        "userName":userName,
        "starttime":starttime,
        "endtime":endtime
    });
}

function  downLoad() {
    $('#tt').datagrid('toExcel',userName+starttime+'至'+endtime+'个人工时导出.xls')
}
