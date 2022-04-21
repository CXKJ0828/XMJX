var editId;
var pageii = null;
var amountSum=0;
var wagesSum=0;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/blank/findCompleteList.shtml';
    var roleId=getValueById("roleId");
    var roleName=getValueById("roleName");
    if(roleId=='16'||isContain(roleName,'班组')){
        columns=[[
            {field:'id',title:'编号',hidden:true,width:80,align:'center'},
            {field:'contractNumber',title:'订单编号',width:120,align:'center'},
            {field:'mapNumber',title:'图号',width:120,align:'center'},
            {field:'goodName',title:'产品名称',width:80,align:'center'},
            {field:'goodSize',title:'产品尺寸',width:80,align:'center'},
            {field:'blankSize',sortable:true,title:'下料尺寸',width:80,align:'center'},
            {field:'materialqualityName',title:'材质',width:100,align:'center'},
            {field:'processName',title:'工序',width:80,align:'center'},
            {field:'accountShow',title:'完成人',width:100,align:'center'},
            {field:'completeAmount',title:'完成数量',width:80,align:'center'},
            {field:'badAmount',title:'废品数量',width:80,align:'center'},
            {field:'completeTime',title:'完成时间',width:150,align:'center',editor:'text'},
        ]];
    }else{
        columns=[[
            {field:'id',title:'编号',hidden:true,width:80,align:'center'},
            {field:'contractNumber',title:'订单编号',width:120,align:'center'},
            {field:'mapNumber',title:'图号',width:120,align:'center'},
            {field:'goodName',title:'产品名称',width:80,align:'center'},
            {field:'materialqualityName',title:'材质',width:100,align:'center'},
            {field:'processName',title:'工序',width:80,align:'center'},
            {field:'accountShow',title:'完成人',width:100,align:'center'},
            {field:'completeTime',title:'完成时间',width:150,align:'center'},
            {field:'goodSize',title:'产品尺寸',width:80,align:'center'},
            {field:'blankSize',sortable:true,title:'下料尺寸',width:80,align:'center'},
            {field:'completeAmount',title:'完成数量',width:80,align:'center'},
            {field:'badAmount',title:'废品数量',width:80,align:'center'},
            {field:'artificial',title:'单价工时',width:80,align:'center'},
            {field:'wages',title:'应得工资',width:80,align:'center',editor:'text'},
            {field: 'deductRate', title: '扣除比例', width: 80, align: 'center',editor:{
                type:'combobox',
                options:{
                    valueField:'value',
                    textField:'text',
                    url:rootPath+'/json/deductRate.json',
                    onChange : function(content,o){
                        var rows = $('#tt').datagrid('getRows');
                        var badAmount=rows[editId].badAmount;
                        var wages=getContentByEditor('tt',0);
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
            {field:'deductWages',title:'应扣工资',width:80,align:'center',editor:'text'},
            {field:'trueWages',title:'实得工资',width:80,align:'center',editor:'text'},
            {field:'startTime',title:'接收时间',width:80,align:'center',editor:'text'},
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
        pagination:true,
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
        rowStyler: function(index,row){
            var isHeattreat=row.isHeattreat;
            if(isHeattreat=='true'){
                return 'color:#048133;font-weight:bold';//绿色加粗字体
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
            setContentToDivSpanById("allMoney","应得工资合计:"+data.money.wages
                +";应扣工资合计:"+data.money.deductWages
                +";实得工资合计:"+data.money.trueWages
                +";完成合计:"+data.money.completeAmount
                +";废品合计:"+data.money.badAmount)
        },
    });


    idexport='ttselect';
    url='/blank/findCompleteListAll.shtml?content=空值';
    columns=[[
        {field:'contractNumber',title:'订单编号',width:80,align:'center'},
        {field:'mapNumber',title:'图号',width:80,align:'center'},
        {field:'goodName',title:'产品名称',width:80,align:'center'},
        {field:'goodSize',title:'产品尺寸',width:150,align:'center'},
        {field:'blankSize',sortable:true,title:'下料尺寸',width:80,align:'center'},
        {field:'materialqualityName',title:'材质',width:100,align:'center'},
        {field:'processName',title:'工序',width:80,align:'center'},
        {field:'accountShow',title:'完成人',width:100,align:'center'},
        {field:'completeAmount',title:'完成数量',width:80,align:'center'},
        {field:'badAmount',title:'废品数量',width:80,align:'center'},
        {field:'completeTime',title:'完成时间',width:150,align:'center',editor:'text'},
        {field:'wages',title:'应得工资',width:80,align:'center',editor:'text'},
        {field:'deductWages',title:'应扣工资',width:80,align:'center',editor:'text'},
        {field:'trueWages',title:'实得工资',width:80,align:'center',editor:'text'},
        {field:'startTime',title:'接收时间',width:150,align:'center',editor:'text'},
    ]];
    initDataGridPaginationFalseToolbarFalse(idexport,url,columns,function (rowIndex, rowData) {

    });
});
function find() {
    $("#tt").datagrid("load",{
        "user":getValueById("user"),
        "content":getValueById("content"),
        "startTime":getDataboxValue("starttime"),
        "endTime":getDataboxValue("endtime")
    });
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
                communateGet(rootPath +'/blank/deleteEntityGetList.shtml?ids='+ids+'&password='+r,function back(data){
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
        communatePost(rootPath +'/blank/editWagesEntity.shtml',ListToJsonString(rows),function back(data){
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
        "endTime":getDataboxValue("endtime")
    }
    downfileByUrl(rootPath +'/blank/exportCompletelist.shtml?entity='+ListToJsonString(data));
}
