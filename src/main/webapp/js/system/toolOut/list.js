var editId;
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/toolOut/findByPage.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        nowrap:false,
        pagination:true,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        fit:true,
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.content=getValueById('content');
            params.toolTypeId=toolTypeId;
            params.userId=getContentBySelect("userId");
            params.startTime=getDataboxValue('starttime');
            params.endTime=getDataboxValue('endtime');
        },
        columns:[[
            {field:'id',hidden:true,title:'编号',width:150,align:'center',editor:'text'},
            {field:'toolName',title:'品名规格',width:300,align:'center'},
            {field:'toolTypeName',title:'类别',width:300,align:'center',editor:'text'},
            {field:'userShow',title:'领料人',width:300,align:'center',editor:'text'},
            {field:'time',title:'领料时间',width:300,align:'center',editor:'text'},
            {field:'amount',title:'领料数量',width:300,align:'center',editor:'text'},
            {field:'money',title:'领料金额',width:300,align:'center',editor:'text'},
            {field:'remarks',title:'备注',width:300,align:'center',editor:'text'},
            {field:'toolId',hidden:true,title:'toolId',width:300,align:'center',editor:'text'},
        ]],
        onLoadSuccess:function(data){
            setContentToDivSpanById("sum","数量合计:"+data.sumEntity.amount+" 金额合计:"+data.sumEntity.money);
        },
    });

    $('#ttselect').datagrid({
        title:'数据展示',
        iconCls:'icon-ok',
        idField:'id',
        nowrap:false,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        columns:[[
            {field:'toolName',title:'品名规格',width:300,align:'center'},
            {field:'toolTypeName',title:'类别',width:300,align:'center',editor:'text'},
            {field:'userShow',title:'领料人',width:300,align:'center',editor:'text'},
            {field:'time',title:'领料时间',width:300,align:'center',editor:'text'},
            {field:'amount',title:'领料数量',width:300,align:'center',editor:'text'},
            {field:'money',title:'领料金额',width:300,align:'center',editor:'text'},
            {field:'remarks',title:'备注',width:300,align:'center',editor:'text'},
        ]],
    });
});



function find() {
    reloadDatagridMessage('tt');
}
function  del() {
    var rows=getDatagridSelections('tt');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    if(ids!=""){
        $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
            if (r){
                communateGet(rootPath +'/toolOut/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
                    reloadDatagridMessage('tt');
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
    var fileName='工具出库统计导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var data={
        "content":getValueById('content'),
        "toolTypeId":toolTypeId,
        "userId":getContentBySelect("userId"),
        "startTime":getDataboxValue('starttime'),
        "endTime":getDataboxValue('endtime'),
    }
    downfileByUrl(rootPath +'/toolOut/exportAll.shtml?entity='+ListToJsonString(data));
}