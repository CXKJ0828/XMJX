var editId;
var pageii = null;
$(function() {
    id='tt';
    url='/po/findByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
        {field:'poCode',title:'编码',width:180,align:'center',editor:'text'},
        {field:'modifyTime',title:'修改时间',width:180,align:'center',editor:'text'},
        {field:'userId',title:'修改人',width:180,align:'center',editor:'text'}
    ]];
    initDataGridSingleSelect(id,url,columns,function (rowIndex, rowData) {

    },
    function (rowIndex, rowData) {
        var poId=rowData.id;
        var url='/po/findpoByPoId.shtml?poId='+poId;
        reloadDataGridByUrl('podetails',url);
    });
});
function find() {
    $("#tt").datagrid("load",{
        "content":getValueById("content")
    });
}
function add() {
    var id='add';
    var url='/po/addUI.shtml';
    addUIToDatagrid(id,url,function () {
        $('#tt').datagrid("reload");
    });
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
        communateGet(rootPath +'/po/deleteEntity.shtml?ids='+ids,function back(data){
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
        communatePost(rootPath +'/po/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }
}
function upload() {
    $.messager.progress({
        title:'Please waiting',
        msg:'Loading data...'
    });
    $('#form').form({
        url:rootPath + '/po/upload.shtml',
        onSubmit: function(){
            return $(this).form('validate');
        },
        success:function(data){
            $.messager.progress('close');
            var jsonObj = eval( '(' + data + ')' ); // eval();方法
            if(jsonObj=="success"){
                reloadDatagridMessage('tt');
            }else{
                alert("系统异常，请联系管理员");
            }
        }
    });
// submit the form
    $('#form').submit();
}
