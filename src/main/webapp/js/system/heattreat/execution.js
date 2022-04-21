var editId;
$(function() {
            columns=
                [[
                    {field:'id',hidden:true,title:'编号',width:300,align:'center',editor:'text'},
                    {field:'clientName',title:'客户',width:150,align:'center'},
                    {field:'contractNumber',title:'合同号',width:100,align:'center',editor:'text'},
                    {field:'mapNumber',title:'图号',width:100,align:'center'},
                    {field:'goodName',title:'产品名称',width:100,align:'center',editor:'text'},
                    {field:'goodSize',sortable:true,title:'产品尺寸',width:100,align:'center',editor:'text'},
                    {field:'materialQualityName',title:'材质',width:60,align:'center'},
                    {field:'deliveryTime',sortable:true,title:'交货日期',width:100,align:'center',editor:'datebox'},
                    {field:'processName',title:'工序',width:60,align:'center',editor:'text'},
                    {field:'userName',title:'接收人',width:60,align:'center',editor:'text'},
                    {field:'amount',title:'接收数量',width:60,align:'center'},
                    {field:'startTime',title:'接收时间',width:120,align:'center'},
                    {field:'completeAmount',title:'完成数量',width:60,align:'center'},
                    {field:'badAmount',title:'废品数量',width:60,align:'center',editor:'text'},
                    {field:'completeTime',title:'完成时间',width:120,align:'center',editor:'text'}

                ]];
        $('#tt').datagrid({
            title:'数据展示',
            iconCls:'icon-ok',
            toolbar: '#tb',
            idField:'id',
            nowrap:false,
            pagination:true,
            url:rootPath + '/heattreat/findExecutionByPage.shtml',
            //是否显示行号
            rownumbers:true,
            fit:true,
            fitColumns:true,
            columns:columns,
            rowStyler: function(index,row){
                var completeTime=row.completeTime;
               if(!isNull(completeTime)){
                    return 'color:#048133;font-weight:bold';//绿色加粗字体
                }
            },
            onBeforeLoad: function (params) {
                params.pageNumber = params.page;
                params.sortName = params.sort;
                params.content=getValueById('content');
                params.oprateUserId=getContentBySelect("oprateUserId");
                params.goodName=getContentBySelect("goodName");
                params.clientId=getContentBySelect("clientId");
                params.materialQuality=getContentBySelect("materialQuality");
                params.state=getContentBySelect("state");
                params.startTime=getDataboxValue("starttime");
                params.endTime=getDataboxValue("endtime");
                params.startTimeGet=getDataboxValue('starttimeGet');
                params.endTimeGet=getDataboxValue('endtimeGet');
                params.startTimeComplete=getDataboxValue('starttimeComplete');
                params.endTimeComplete=getDataboxValue('endtimeComplete');
            },
            onLoadSuccess:function(data){
                setContentToDivSpanById("all","接收合计:"+data.sum.getAmount+"  完成合计:"+data.sum.completeAmount+"  废品合计:"+data.sum.badAmount+"  未完成合计:"+data.sum.unComplete)
            },
        });

    columns=
        [[
            {field:'clientName',title:'客户',width:150,align:'center'},
            {field:'contractNumber',title:'合同号',width:100,align:'center',editor:'text'},
            {field:'mapNumber',title:'图号',width:100,align:'center'},
            {field:'goodName',title:'产品名称',width:100,align:'center',editor:'text'},
            {field:'goodSize',sortable:true,title:'产品尺寸',width:100,align:'center',editor:'text'},
            {field:'materialQualityName',title:'材质',width:60,align:'center'},
            {field:'deliveryTime',sortable:true,title:'交货日期',width:100,align:'center',editor:'datebox'},
            {field:'processName',title:'工序',width:150,align:'center',editor:'text'},
            {field:'userName',title:'接收人',width:60,align:'center',editor:'text'},
            {field:'amount',title:'接收数量',width:100,align:'center'},
            {field:'startTime',title:'接收时间',width:200,align:'center'},
            {field:'completeAmount',title:'完成数量',width:60,align:'center'},
            {field:'badAmount',title:'废品数量',width:60,align:'center',editor:'text'},
            {field:'completeTime',title:'完成时间',width:200,align:'center',editor:'text'}

        ]];
    $('#ttselect').datagrid({
        title:'数据展示',
        iconCls:'icon-ok',
        nowrap:false,
        pagination:true,
        //是否显示行号
        rownumbers:true,
        fit:true,
        columns:columns
    });
    initComboGridEditor();
});


function find() {
    reloadDatagridMessage('tt');
    clearDatagridSelections("tt");
    reloadTreeMessage("tt1");
}

function  cancel() {
    reloadDatagridMessage('tt');
    clearDatagridSelections('tt');
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
    var fileName='接受完成情况导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var data={
        "content":getValueById('content'),
        "goodName":getContentBySelect('goodName'),
        "oprateUserId":getContentBySelect("oprateUserId"),
        "clientId":getContentBySelect('clientId'),
        "materialQuality":getContentBySelect('materialQuality'),
        "state":getContentBySelect('state'),
        "startTime":getDataboxValue('starttime'),
        "endTime":getDataboxValue('endtime'),
        "startTimeGet":getDataboxValue('starttimeGet'),
        "endTimeGet":getDataboxValue('endtimeGet'),
        "startTimeComplete":getDataboxValue('starttimeComplete'),
        "endTimeComplete":getDataboxValue('endtimeComplete'),
    }
    downfileByUrl(rootPath +'/heattreat/exportExecutionAll.shtml?entity='+ListToJsonString(data));
}




