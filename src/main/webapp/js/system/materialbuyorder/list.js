var editId;
var pageii = null;
var goodId=0;
var isCancel=false;
var myArray=new Array();
var datagridDetailsArray=new Array();
var materialbuyorderId=0;
function onExpandRow(index, row) {
    var starttime=getDataboxValue("starttime");
    var endtime=getDataboxValue("endtime");
    clientId=getContentsByCombobox('clientId').toString();
    clientId=clientId.replace('[','');
    clientId=clientId.replace(']','');
    var url=rootPath+'/materialbuyorder/findblankBymaterialBuyOrderDetailsId.shtml?materialBuyOrderDetailsId='+row.id
        +"&clientId="+clientId
        +"&starttime="+starttime
        +"&endtime="+endtime;
    var columns=[[
        {field:'id',hidden:true,title:'编号',width:100,align:'center'},
        {field:'deliveryTime',title:'交货日期',width:100,align:'center'},
        {field:'clientFullName',title:'客户',width:100,align:'center'},
        {field:'contractNumber',title:'订单编号',width:100,align:'center'},
        {field:'mapNumber',title:'图号',width:100,align:'center'},
        {field:'goodName',title:'产品名称',width:100,align:'center'},
        {field:'goodSize',title:'成品尺寸',width:100,align:'center'},
        {field:'orderAmount',title:'订单数量',width:100,align:'center'},
        {field:'blankSize',title:'下料尺寸',width:100,align:'center',editor:'text'},
        {field:'amount',title:'下料数量',width:100,align:'center',editor:'text'},
        {field:'materialQuality',hidden:true,title:'材质',width:100,align:'center',editor:'text'},
        {field:'materialQualityName',title:'材质',width:100,align:'center',editor:'text'},
        {field:'length',title:'米数',width:100,align:'center',editor:'text'},
        {field:'weight',title:'吨数',width:100,align:'center',editor:'text'},
    ]];
    var parentDatagridId='goodprocessList';
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
    $('#tt').tree({
        url:rootPath + '/materialbuyorder/findlist.shtml',
        method:'post',
        toolbar: '#tb',
        lines:true,
        animate:true,
        onClick:function (node) {
            materialbuyorderId=node.id;
                var text=node.text;
            var starttime=text.split("至")[0];
            setDataToDateBox("starttime",starttime);
            var endtime=text.split("至")[1];
            setDataToDateBox("endtime",endtime);
            reloadDatagridMessage("goodprocessList");
        }
    });
});
function add() {

}

function  cancel() {
    isCancel=true;
    $('#goodprocessList').datagrid('rejectChanges');
}
function  downLoad() {
    var fileName='订料明细导出.xls';
    $('#goodprocessListExport').datagrid('toExcel',fileName);
}