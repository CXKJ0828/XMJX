var editId;
var pageii = null;
var amountSum=0;
var wagesSum=0;
var getMaxEstimateCompleteTime="否";
$(function() {
    initComboGridEditor();
    id='tt';
    url='/heattreat/findGetList.shtml';
    columns=[[
        {field:'contractNumber',title:'订单编号',width:150,align:'center'},
        {field:'goodCode',title:'图号',width:150,align:'center'},
        {field:'goodName',title:'产品名称',width:150,align:'center'},
        {field:'materialQualityName',title:'材质',width:150,align:'center'},
        {field:'deliveryTime',title:'交货日期',width:150,align:'center'},
        {field:'processName',title:'工序',width:80,align:'center'},
        {field:'submiterShow',title:'接收人',width:150,align:'center'},
        {field:'goodSize',sortable:true,title:'产品尺寸',width:150,align:'center'},
        {field:'amount',title:'接收数量',width:100,align:'center'},
        {field:'artificial',title:'单价工时',width:100,align:'center'},
        {field:'distributionWages',title:'对应工资',width:100,align:'center',editor:'text'},
        {field:'startTime',title:'接收时间',width:150,align:'center'},
        {field:'estimateCompleteTime',title:'应完成时间',width:150,align:'center'},
        {field:'overTimeLimit',title:'超时设定',width:100,align:'center',editor:'text'},
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
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.user=getValueById('user');
            params.goodName=getContentBycombogrid("goodName");
            params.content=getValueById("content");
            params.startTime=getDataboxValue("starttime");
            params.endTime=getDataboxValue("endtime");
            params.getMaxEstimateCompleteTime=getMaxEstimateCompleteTime;
        },
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
            getMaxEstimateCompleteTime="否";
            setContentToDivSpanById("allMoney","接收合计:"+data.money.amount);
            cancel();
        },
    });


    idexport='ttselect';
    url='/blank/findGetListAll.shtml?content=空值';
    columns=[[
        {field:'contractNumber',title:'订单编号',width:150,align:'center'},
        {field:'goodCode',title:'图号',width:150,align:'center'},
        {field:'goodName',title:'产品名称',width:150,align:'center'},
        {field:'materialQualityName',title:'材质',width:150,align:'center'},
        {field:'deliveryTime',title:'交货日期',width:150,align:'center'},
        {field:'processName',title:'工序',width:80,align:'center'},
        {field:'submiterShow',title:'接收人',width:150,align:'center'},
        {field:'goodSize',sortable:true,title:'产品尺寸',width:150,align:'center'},
        {field:'amount',title:'接收数量',width:100,align:'center'},
        {field:'artificial',title:'单价工时',width:100,align:'center'},
        {field:'distributionWages',title:'对应工资',width:100,align:'center',editor:'text'},
        {field:'startTime',title:'接收时间',width:150,align:'center'},
        {field:'estimateCompleteTime',title:'应完成时间',width:150,align:'center'},
        {field:'overTimeLimit',title:'超时设定',width:100,align:'center',editor:'text'},
    ]];
    initDataGridPaginationFalseToolbarFalse(idexport,url,columns,function (rowIndex, rowData) {

    });
});
function find() {
    reloadDatagridMessage('tt');
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

function  save() {
    endEditDatagridByIndex('tt',editId);
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/heattreat/editOverTimeLimitEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }
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
                communateGet(rootPath +'/heattreat/deleteEntityGetList.shtml?ids='+ids+'&password='+r,function back(data){
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
    downfileByUrl(rootPath +'/heattreat/exportGetlist.shtml?entity='+ListToJsonString(data));
}