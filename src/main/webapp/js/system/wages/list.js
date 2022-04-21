var editId;
var pageii = null;
$(function() {
    id='tt';
    url='/wages/findByPage.shtml';
    columns=[[
        {field:'id',title:'编号',hidden:true,width:80,align:'center',editor:'text'},
        {field:'contractNumber',title:'订单编号',width:120,align:'center',editor:'text'},
        {field:'mapNumber',title:'图号',width:120,align:'center',editor:'text'},
        {field:'goodName',title:'产品名称',width:80,align:'center',editor:'text'},
        { field: 'processName', title: '工序', width: 80, align: 'center',editor:{type:'combogrid',
            options: {panelWidth: 200,
                idField: 'name',
                textField: 'name',
                url: rootPath+'/process/processSelect.shtml',
                mode: 'remote',
                columns: [[
                    {field:'id',hidden:true,title:'编码',width:150,align:'center'},
                    {field:'name',title:'名称',width:300,align:'center'},
                ]],
                fitColumns: true,
                onSelect:selectProcess
            }
        } },
        { field: 'processId', title: '工序id',hidden:true, width: 50, align: 'center',editor:'text' },
        {field:'accountName',title:'完成人账号',width:80,align:'center',editor:'text',editor:{type:'combogrid',
            options: {panelWidth: 400,
                idField: 'accountName',
                textField: 'accountName',
                url: rootPath+'/user/userSelect.shtml',
                method: 'get',
                columns: [[
                    {field:'accountName',title:'账号',width:120,align:'center'},
                    {field:'userName',title:'姓名',width:120,align:'center'},
                ]],
                fitColumns: true,
                onSelect:selectUser
            }
        } },
        {field:'userName',title:'完成人姓名',width:80,align:'center',editor:'text'},
        {field:'userId',hidden:true,title:'userId',width:80,align:'center',editor:'text'},
        {field:'completeAmount',title:'完成数量',width:80,align:'center',editor:'text'},
        {field:'completeTime',title:'完成时间',width:150,align:'center',editor:'datebox'},
        {field:'wages',title:'应得工资',width:80,align:'center',editor:'text'},
        {field:'remarks',title:'备注',width:150,align:'center',editor:'text'},
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
        onDblClickRow:function (rowIndex, rowData) {
            if(editId!=null){
                endEditDatagridByIndex('tt',editId);
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }else{
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }
        },
        columns:columns,
        onLoadSuccess:function(data){
            setContentToDivSpanById("allMoney","应得工资合计:"+data.wages)
        },
    });



    idexport='ttexport';
    url='/wages/findByAll.shtml';
    columns=[[
        {field:'contractNumber',title:'订单编号',width:120,align:'center',editor:'text'},
        {field:'mapNumber',title:'图号',width:120,align:'center',editor:'text'},
        {field:'goodName',title:'产品名称',width:80,align:'center',editor:'text'},
        { field: 'processName', title: '工序', width: 80, align: 'center'},
        {field:'accountName',title:'完成人账号',width:80,align:'center',editor:'text' },
        {field:'userName',title:'完成人姓名',width:80,align:'center',editor:'text'},
        {field:'completeAmount',title:'完成数量',width:80,align:'center',editor:'text'},
        {field:'completeTime',title:'完成时间',width:150,align:'center',editor:'datebox'},
        {field:'wages',title:'应得工资',width:80,align:'center',editor:'text'},
        {field:'remarks',title:'备注',width:150,align:'center',editor:'text'},
    ]];
    $('#ttexport').datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        columns:columns,

    });


    idexport='ttselect';
    url='/wages/findByAll.shtml?content=空值';
    columns=[[
        {field:'contractNumber',title:'订单编号',width:120,align:'center',editor:'text'},
        {field:'mapNumber',title:'图号',width:120,align:'center',editor:'text'},
        {field:'goodName',title:'产品名称',width:80,align:'center',editor:'text'},
        { field: 'processName', title: '工序', width: 80, align: 'center'},
        {field:'accountName',title:'完成人账号',width:80,align:'center',editor:'text' },
        {field:'userName',title:'完成人姓名',width:80,align:'center',editor:'text'},
        {field:'completeAmount',title:'完成数量',width:80,align:'center',editor:'text'},
        {field:'completeTime',title:'完成时间',width:150,align:'center'},
        {field:'wages',title:'应得工资',width:80,align:'center',editor:'text'},
        {field:'remarks',title:'备注',width:150,align:'center',editor:'text'},
    ]];
    initDataGridPaginationFalseToolbarFalse(idexport,url,columns,function (rowIndex, rowData) {

    });
});
function selectProcess(rowIndex, rowData) {
        setContentToDatagridEditorText('tt',editId,"processId",rowData.id);
        setContentToDatagridEditorText('tt',editId,"processName",rowData.name);
}
function selectUser(rowIndex, rowData) {
    setContentToDatagridEditorText('tt',editId,"userId",rowData.id);
    setContentToDatagridEditorText('tt',editId,"userName",rowData.userName);
    setContentToDatagridEditorText('tt',editId,"accountName",rowData.accountName);
}

function find() {
    $("#tt").datagrid("load",{
        "user":getValueById("user"),
        "content":getValueById("content"),
        "startTime":getDataboxValue("starttime"),
        "endTime":getDataboxValue("endtime")
    });

    $("#ttexport").datagrid("load",{
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
    $('#tt').datagrid('rejectChanges');
}
function  del() {
    endEditDatagridByIndex('tt',editId);
    var rows=getDatagridSelections('tt');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    if(ids!=""){
        communateGet(rootPath +'/wages/deleteEntity.shtml?ids='+ids,function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function  save() {
    endEditDatagridByIndex('tt',editId);
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/wages/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
            if(!isContain(data,"success")){
                showErrorAlert("警告",data);
            }
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
    var fileName='二类工资列表导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}
function  downLoadAll() {
    var fileName='二类工资列表全部导出.xls';
    $('#ttexport').datagrid('toExcel',fileName);
}
