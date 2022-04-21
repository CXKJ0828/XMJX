var editId;
var pageii = null;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/leave/findList.shtml';
    columns=[[
        {field:'userName',title:'员工姓名',width:150,align:'center'},
        {field:'days',title:'请假天数',width:150,align:'center',editor:'text'},
        {field:'modifyTime',title:'扫描时间',width:150,align:'center',editor:'text'},
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
        columns:columns,
        onLoadSuccess:function(data){
            setContentToDivSpanById("all","请假天数合计:"+data.days)
        },
    });

    idexport='ttexport';
    url='/leave/findListAll.shtml';
    columns=[[
        {field:'userName',title:'员工姓名',width:150,align:'center'},
        {field:'days',title:'请假天数',width:150,align:'center',editor:'text'},
        {field:'modifyTime',title:'扫描时间',width:150,align:'center',editor:'text'},
    ]];
    initDataGridPaginationFalseToolbarFalse(idexport,url,columns,function (rowIndex, rowData) {
    });

    idexport='ttselect';
    url='/leave/findListAll.shtml?content=空值';
    columns=[[
        {field:'userName',title:'员工姓名',width:150,align:'center'},
        {field:'days',title:'请假天数',width:150,align:'center',editor:'text'},
        {field:'modifyTime',title:'扫描时间',width:150,align:'center',editor:'text'},
    ]];
    initDataGridPaginationFalseToolbarFalse(idexport,url,columns,function (rowIndex, rowData) {
    });
});
function find() {
    $("#tt").datagrid("load",{
        "user":getContentBySelect("userId"),
        "startTime":getDataboxValue("starttime"),
        "endTime":getDataboxValue("endtime")
    });

    $("#ttexport").datagrid("load",{
        "user":getContentBySelect("userId"),
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
                communateGet(rootPath +'/leave/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
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

    var fileName='请假导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var fileName='请假全部导出.xls';
    $('#ttexport').datagrid('toExcel',fileName);
}