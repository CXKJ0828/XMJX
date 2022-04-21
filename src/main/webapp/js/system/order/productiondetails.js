var editId;
var myArray=new Array();
var datagridDetailsArray=new Array();
function onExpandRow(index, row) {
    var url=rootPath+'/order/getProcessByOrderDetailsId.shtml?id='+row.id;
    var columns=[[
        { field: 'id', title: '编号',hidden:true,align:'center', editor:'text' },
        { field: 'code', title: '工序编码', width: 50, align: 'center',editor:{type:'combogrid',
            options: {panelWidth: 600,
                idField: 'code',
                textField: 'code',
                url: rootPath+'/process/processSelect.shtml',
                mode: 'remote',
                columns: [[
                    {field:'id',title:'编号',hidden:true,width:80,align:'center'},
                    {field:'code',title:'编码',width:80,align:'center'},
                    {field:'name',title:'名称',width:120,align:'center'},
                    {field:'simpleName',title:'简称',width:80,align:'center'},
                    {field:'address',title:'地址',width:200,align:'center'},
                    {field:'oneTime',title:'单件加工时间',width:120,align:'center'},
                    {field:'oneTimeUnit',title:'单价时间单位',width:80,align:'center'},
                    {field:'oneCost',title:'单件成本',width:200,align:'center'},
                    {field:'organization',title:'组织',width:80,align:'center'},
                    {field:'remark',title:'备注',width:100,align:'center'}
                ]],
                fitColumns: true,
                onSelect:selectProcess
            }
        } },
        { field: 'name', title: '工序名称', width: 50, align: 'center',editor:'text' },
        { field: 'processId', title: '工序id',hidden:true, width: 50, align: 'center',editor:'text' },
        { field: 'time', title: '工时', width: 50, align: 'center',editor:'text' },
        { field: 'amount', title: '数量', width: 50, align: 'center',editor:'text' },
        { field: 'completeAmount', title: '已完成数量', width: 50, align: 'center',editor:'text' },
        { field: 'uncompleteAmount', title: '未完成数量', width: 50, align: 'center',editor:'text' },
        { field: 'state', title: '状态', width: 50, align: 'center',editor:'text' },
    ]];
    var parentDatagridId='orderdetails';
    initExpandRowContainState(index,row,url,columns,parentDatagridId,true,editId,function (number) {

    },function (rowIndex,rowData) {
        if(rowData.state=='异常'){
            showconfirmDialog("异常信息","由"+rowData.unusualName+"于"+rowData.unusualTime+"标记为异常")
        }else{
            $("#details").window({
                width: 700,
                modal: true,
                title:'生产查看',
                height: 400,
                top:50,
                href: rootPath + '/order/workersubmitShowUI.shtml?orderdetailsprocessId='+rowData.id,
                onClose:function () {
                }
            });
        }
    });
}

function selectProcess(rowIndex, rowData) {
    if(checkIsSelectEd(editId)){
        var parentRowNum=editDatagrid.replace("ddv-","");
        var parentDetailsAmount=getDatagridJsonByIndexAndId('orderdetails',parentRowNum).amount;
        setContentToDatagridEditorText(editDatagrid,editId,"processId",rowData.id);
        setContentToDatagridEditorText(editDatagrid,editId,"time",VarToFloat(rowData.oneTime)*VarToFloat(parentDetailsAmount));
        setContentToDatagridEditorText(editDatagrid,editId,"name",rowData.simpleName);
        setContentToDatagridEditorText(editDatagrid,editId,"amount",parentDetailsAmount);
        setContentToDatagridEditorText(editDatagrid,editId,"code",rowData.organization);
    }
}
var editId;
var editDatagrid;
function RowMeanu(rowIndex,x,y,datagridId){
    if(editId!=null||editId!=""){
        endEditDatagridByIndex(editDatagrid,editId);
    }
    editDatagrid=datagridId;
    clearSelectAndSelectRowDatagrid(datagridId,rowIndex);
    editId=rowIndex;
    showMenu("rightMenu",x,y);
}
function HeaderMeanu(x,y,datagridId){
    if(editId!=null||editId!=""){
        endEditDatagridByIndex(editDatagrid,editId);
    }
    editDatagrid=datagridId;
    showMenu("rightMenuHeader",x,y);
}
rightClickDatagrid("orderdetails",RowMeanu,HeaderMeanu,editId);

function delEntity() {
    var rowData=getDatagridJsonByIndexAndId(editDatagrid,editId);
    var url="";
    if(isContain(editDatagrid,'ddv')){
        url=rootPath+'/order/deleteDetailsEntity.shtml?orderdetailsprocessId='+rowData.id+"&orderdetailsId=";
    }else{
        url=rootPath+'/order/deleteDetailsEntity.shtml?orderdetailsId='+rowData.id+"&orderdetailsprocessId=";
    }
    if(editId!=null){
        communateGet(url,function back(data){
            deleteOneRowById(editDatagrid,editId);
        });
    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function addContent() {

    var rows = getAllRowsContent(editDatagrid);
    var row=rows[editId];
    if(isContain(editDatagrid,'ddv')){
        addRowInLast(editDatagrid,{state:"未完成"});
    }else{
        var batchNumber=getBatchNumber(getValueById('orderId'),rows.length);
        addRowInLast(editDatagrid,{state:"未完成",batchNumber:batchNumber});
    }

    if(rows.length==0){
        editId=0;
    }else{
        editId=rows.length-1;
    }
    editId=rows.length-1;
    beginEditDatagridByIndex(editDatagrid,editId);
}
function edit() {
    beginEditDatagridByIndex(editDatagrid,editId);

}



