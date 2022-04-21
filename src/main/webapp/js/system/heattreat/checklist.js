var editId;
var pageii = null;
var amountSum=0;
var badAmountSum=0;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/heattreat/findCheckList.shtml';
        columns=[[
            {field:'id',title:'编号',hidden:true,width:80,align:'center'},
            {field:'contractNumber',title:'订单编号',width:120,align:'center'},
            {field:'mapNumber',title:'图号',width:100,align:'center'},
            {field:'name',title:'产品名称',width:80,align:'center'},
            {field:'materialQualityName',title:'材质',width:100,align:'center'},
            {field:'deliveryTime',title:'交货日期',width:100,align:'center'},
            {field:'processName',title:'工序',width:70,align:'center'},
            {field:'checkedUserShow',title:'被检验人',width:100,align:'center'},
            {field:'startTime',title:'接收时间',width:120,align:'center',editor:'text'},
            {field:'goodSize',sortable:true,title:'产品尺寸',width:100,align:'center'},
            {field:'goodSizeGoodTable',sortable:true,title:'成品尺寸',width:100,align:'center'},
            {field:'checkTime',title:'检验时间',width:150,align:'center'},
            {field:'amount',title:'检验数量',width:60,align:'center',editor:'text'},
            {field:'badAmount',title:'废品数量',width:60,align:'center',editor:'text'},
            {field:'checkUserShow',title:'检验员',width:100,align:'center'},
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
        fit:true,
        pagination:true,
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.user=getValueById("user");
            params.checkUser=getContentBycombogrid("checkUser");
            params.goodName=getContentBycombogrid("goodName");
            params.isCheck=getContentBySelect("isCheck");
            params.content=getValueById("content");
            params.startCheckTime=getDataboxValue("startCheckTime");
            params.endCheckTime=getDataboxValue("endCheckTime");
            params.startTimeGET=getDataboxValue("startTimeGET");
            params.endTimeGET=getDataboxValue("endTimeGET");
        },
        onCheck:function (rowIndex,rowData) {
            amountSum=amountSum+VarToInt(rowData.amount);
            badAmountSum=badAmountSum+VarToInt(rowData.badAmount);
            setContentToDivSpanById("amountSum","检验数量合计:"+amountSum+"废品数量合计:"+badAmountSum);
        },
        onUncheck:function (rowIndex,rowData) {
            if(amountSum>0){
                amountSum=amountSum-VarToInt(rowData.amount);
                badAmountSum=badAmountSum-VarToInt(rowData.badAmount);
                setContentToDivSpanById("amountSum","检验数量合计:"+amountSum+"废品数量合计:"+badAmountSum);
            }else{
                setContentToDivSpanById("amountSum","");
            }

        },
        columns:columns,
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
function  save() {
    endEditDatagridByIndex('tt',editId);
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/heattreat/editWagesEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
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
    var fileName='已完成列表导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var data={
        "user":getValueById("user"),
        "content":getValueById("content"),
        "startTime":getDataboxValue("starttime"),
        "endTime":getDataboxValue("endtime"),
        "startTimeGET":getDataboxValue("starttimeGET"),
        "endTimeGET":getDataboxValue("endtimeGET")
    }
    downfileByUrl(rootPath +'/heattreat/exportCompletelist.shtml?entity='+ListToJsonString(data));
}
