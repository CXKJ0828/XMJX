var editId;
var pageii = null;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/Attendance/findList.shtml';
    columns=[[
        {field:'userName',title:'员工姓名',width:150,align:'center'},
        {field:'day',title:'日期',width:150,align:'center',editor:'text'},
        {field:'startTime',title:'开始时间',width:150,align:'center',editor:'text'},
        {field:'endTime',title:'结束时间',width:150,align:'center',editor:'text'},
    ]];
    initDataGrid(id,url,columns,function (rowIndex, rowData) {
    });

    idexport='ttexport';
    url='/Attendance/findListAll.shtml';
    columns=[[
        {field:'userName',title:'员工姓名',width:150,align:'center'},
        {field:'day',title:'日期',width:150,align:'center',editor:'text'},
        {field:'startTime',title:'开始时间',width:150,align:'center',editor:'text'},
        {field:'endTime',title:'结束时间',width:150,align:'center',editor:'text'},
    ]];
    initDataGridPaginationFalseToolbarFalse(idexport,url,columns,function (rowIndex, rowData) {
    });

    idexport='ttselect';
    url='/Attendance/findListAll.shtml?content=空值';
    columns=[[
        {field:'userName',title:'员工姓名',width:150,align:'center'},
        {field:'day',title:'日期',width:150,align:'center',editor:'text'},
        {field:'startTime',title:'开始时间',width:150,align:'center',editor:'text'},
        {field:'endTime',title:'结束时间',width:150,align:'center',editor:'text'},
    ]];
    initDataGridPaginationFalseToolbarFalse(idexport,url,columns,function (rowIndex, rowData) {
    });
});
function find() {
    $("#tt").datagrid("load",{
        "user":getValueById("user"),
        "startTime":getDataboxValue("starttime"),
        "endTime":getDataboxValue("endtime")
    });

    $("#ttexport").datagrid("load",{
        "user":getValueById("user"),
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
        $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
            if (r){
                communateGet(rootPath +'/Attendance/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
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

    var fileName='考勤导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var fileName='考勤全部导出.xls';
    $('#ttexport').datagrid('toExcel',fileName);
}