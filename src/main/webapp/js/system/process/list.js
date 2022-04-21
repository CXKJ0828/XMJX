var editId;
var TimeFn = null;
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/process/findByPage.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        pagination:true,
        pagePosition:top,
        fit:true,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        onDblClickRow:function (rowIndex, rowData) {
            if(editId!=null){
                endEditDatagridByIndex('tt',editId)
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }else{
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }
        },
        columns:[[
            {field:'id',title:'编号',hidden:true,width:80,align:'center',editor:'text'},
            {field:'name',title:'名称',width:80,align:'center',editor:'text'},
            {field:'artificial',hidden:true,title:'人工',width:80,align:'center',editor:'text'},
            {field:'remark',hidden:true,title:'备注',width:80,align:'center',editor:'text'},
            {field:'isMust',hidden:true,title:'是否必须',width:80,align:'center',editor:'text'},
            {field:'multiple',hidden:true,title:'倍数',width:80,align:'center',editor:'text'},
            {field:'modifyTime',title:'修改时间',width:80,align:'center',editor:'text'},
            {field:'userName',title:'修改用户',width:80,align:'center',editor:'text'},
        ]],
        onLoadSuccess:function(data){
        },
    });
});
function upload() {
    $.messager.progress({
        title:'Please waiting',
        msg:'Loading data...'
    });
    $('#form').form({
        url:rootPath + '/process/upload.shtml',
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

function find() {
    $("#tt").datagrid("load",{
        "content":getValueById("content")
    });
}
function add() {
    var rows = $('#tt').datagrid('getRows');
    $('#tt').datagrid('appendRow',
        {
            isMust:"是",
        }
    );
}

function  cancel() {
    $('#tt').datagrid('rejectChanges');
}
function  del() {
    showconfirmDialog("提示","是否确认删除?",function (r) {
        if(r){
            endEditDatagridByIndex('tt',editId);
            var rows=getDatagridSelections('tt');
            var ids="";
            for(i=0;i<rows.length;i++){
                ids=ids+rows[i].id+",";
            }
            if(ids!=""){
                communateGet(rootPath +'/process/deleteEntity.shtml?ids='+ids,function back(data){
                    reloadDatagridMessage('tt');
                    clearDatagridSelections("tt");
                });
            }else{
                $.messager.alert('警告','不存在可以删除数据','warning');
            }
        }
    });
}
function  save() {
    endEditDatagridByIndex('tt',editId)
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/process/addEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
