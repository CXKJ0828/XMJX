var editId;
var pageii = null;
var amountSum=0;
var wagesSum=0;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/heattreat/findCompleteList.shtml';
    var roleId=getValueById("roleId");
    var roleName=getValueById("roleName");
    if(roleId=='16'||isContain(roleName,'班组')){
        columns=[[
            {field:'id',title:'编号',hidden:true,width:80,align:'center'},
            {field:'contractNumber',title:'订单编号',width:120,align:'center'},
            {field:'goodCode',title:'图号',width:80,align:'center'},
            {field:'goodName',title:'产品名称',width:80,align:'center'},
            {field:'goodSize',sortable:true,title:'产品尺寸',width:80,align:'center'},
            {field:'goodSizeGoodTable',sortable:true,title:'成品尺寸',width:100,align:'center'},
            {field:'materialQualityName',title:'材质',width:100,align:'center'},
            {field:'deliveryTime',title:'交货日期',width:100,align:'center'},
            {field:'processName',title:'工序',width:100,align:'center'},
            {field:'submiterShow',title:'完成人',width:80,align:'center'},
            {field:'completeAmount',title:'完成数量',width:80,align:'center'},
            {field:'badAmount',title:'废品数量',width:80,align:'center'},
            {field:'completeTime',title:'完成时间',width:150,align:'center',editor:'text'},
        ]];
    }else{
        columns=[[
            {field:'id',title:'编号',hidden:true,width:80,align:'center'},
            {field:'contractNumber',title:'订单编号',width:120,align:'center'},
            {field:'goodCode',title:'图号',width:100,align:'center'},
            {field:'goodName',title:'产品名称',width:80,align:'center'},
            {field:'materialQualityName',title:'材质',width:100,align:'center'},
            {field:'deliveryTime',title:'交货日期',width:100,align:'center'},
            {field:'processName',title:'工序',width:90,align:'center'},
            {field:'completeTime',title:'完成时间',width:150,align:'center'},
            {field:'submiterShow',title:'完成人',width:100,align:'center'},
            {field:'goodSize',sortable:true,title:'产品尺寸',width:100,align:'center'},
            {field:'goodSizeGoodTable',sortable:true,title:'成品尺寸',width:100,align:'center'},
            {field:'completeAmount',title:'完成数量',width:60,align:'center',editor:'text'},
            {field:'badAmount',title:'废品数量',width:60,align:'center',editor:'text'},
            {field:'artificial',title:'单价工时',width:100,align:'center'},
            {field:'wages',title:'应得工资',width:60,align:'center',editor:'text'},
            {field: 'deductRate', title: '扣除比例', width: 80, align: 'center',editor:{
                type:'combobox',
                options:{
                    valueField:'value',
                    textField:'text',
                    url:rootPath+'/json/deductRate.json',
                    onChange : function(content,o){
                        var rows = $('#tt').datagrid('getRows');
                        var badAmount=getContentByEditor("tt",1);
                        var wages=getContentByEditor("tt",2);
                        var taxPrice=rows[editId].taxPrice;
                        var deductRate=content;
                        var deductWages=VarToFloat(taxPrice)*VarToFloat(badAmount)*VarToFloat(deductRate);
                        deductWages=floatToVar2(deductWages);
                        setContentToDatagridEditorText("tt",editId,'deductWages',deductWages);
                        var trueWages=VarToFloat(wages)-VarToFloat(deductWages);
                        trueWages=floatToVar2(trueWages);
                        setContentToDatagridEditorText("tt",editId,'trueWages',trueWages);
                    }
                }
            }},
            {field:'deductWages',title:'应扣工资',width:60,align:'center',editor:'text'},
            {field:'trueWages',title:'实得工资',width:60,align:'center',editor:'text'},
            {field:'startTime',title:'接收时间',width:120,align:'center',editor:'text'},
            {field:'estimateCompleteTime',title:'应完成时间',width:150,align:'center',editor:'text'},
            {field:'isOverTime',title:'是否超时',width:70,align:'center',editor:'text'},
            {field:'overTimeLimit',title:'超时设定',width:70,align:'center',editor:'text'},
            {field:'sureCompleteUser',title:'确认完成人',width:100,align:'center'},
            {field:'taxPrice',hidden:true,title:'含税单价',width:80,align:'center',editor:'text'},
        ]];
    }

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
            params.sureCompleteUser=getContentBycombogrid("sureCompleteUser");
            params.goodName=getContentBycombogrid("goodName");
            params.isOverTime=getContentBySelect("isOverTime");
            params.content=getValueById("content");
            params.startTime=getDataboxValue("starttime");
            params.endTime=getDataboxValue("endtime");
            params.startTimeGET=getDataboxValue("starttimeGET");
            params.endTimeGET=getDataboxValue("endtimeGET");
        },
        onCheck:function (rowIndex,rowData) {
            amountSum=amountSum+VarToInt(rowData.completeAmount);
            wagesSum=wagesSum+VarToFloat(rowData.wages);
            setContentToDivSpanById("amountSum","数量合计:"+amountSum+" 应得工资合计:"+floatToVar2(wagesSum));
        },
        onUncheck:function (rowIndex,rowData) {
            if(amountSum>0){
                amountSum=amountSum-VarToInt(rowData.completeAmount);
                wagesSum=wagesSum-VarToFloat(rowData.wages);
                setContentToDivSpanById("amountSum","数量合计:"+amountSum+" 应得工资合计:"+floatToVar2(wagesSum));
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
        rowStyler: function(index,row){
            var isOverTime=row.isOverTime;
            if (isOverTime=='是'){
                return 'color:#DC143C;font-weight:bold';//红色
            }else if (isOverTime=='否'){
                return 'color:#048133;font-weight:bold';
            }
        },
        onLoadSuccess:function(data){
            setContentToDivSpanById("allMoney","应得工资合计:"+data.money.wages
                +";应扣工资合计:"+data.money.deductWages
                +";完成合计:"+data.money.completeAmount
                +";废品合计:"+data.money.badAmount
                +";超时合计:"+data.overTime.overTimeCouont
                +";扣款合计:"+data.overTime.overTimeMoney
                +";实得工资合计:"+(data.money.trueWages-data.overTime.overTimeMoney))
        },
    });


    idexport='ttselect';
    url='/heattreat/findCompleteListAll.shtml?content=空值';
    columns=[[
        {field:'id',title:'编号',hidden:true,width:80,align:'center'},
        {field:'contractNumber',title:'订单编号',width:120,align:'center'},
        {field:'goodCode',title:'图号',width:100,align:'center'},
        {field:'goodName',title:'产品名称',width:80,align:'center'},
        {field:'materialQualityName',title:'材质',width:100,align:'center'},
        {field:'deliveryTime',title:'交货日期',width:100,align:'center'},
        {field:'processName',title:'工序',width:100,align:'center'},
        {field:'completeTime',title:'完成时间',width:150,align:'center'},
        {field:'submiterShow',title:'完成人',width:80,align:'center'},
        {field:'goodSize',sortable:true,title:'产品尺寸',width:100,align:'center'},
        {field:'goodSizeGoodTable',sortable:true,title:'成品尺寸',width:100,align:'center'},
        {field:'completeAmount',title:'完成数量',width:60,align:'center'},
        {field:'badAmount',title:'废品数量',width:60,align:'center'},
        {field:'artificial',title:'单价工时',width:100,align:'center'},
        {field:'wages',title:'应得工资',width:60,align:'center',editor:'text'},
        {field: 'deductRate', title: '扣除比例', width: 80, align: 'center'},
        {field:'deductWages',title:'应扣工资',width:60,align:'center',editor:'text'},
        {field:'trueWages',title:'实得工资',width:60,align:'center',editor:'text'},
        {field:'startTime',title:'接收时间',width:120,align:'center',editor:'text'},
        {field:'estimateCompleteTime',title:'应完成时间',width:150,align:'center',editor:'text'},
        {field:'isOverTime',title:'是否超时',width:80,align:'center',editor:'text'},
        {field:'overTimeLimit',title:'超时设定',width:80,align:'center',editor:'text'},
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
