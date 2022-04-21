var editId;
var pageii = null;
var goodId=0;
var img;
$(function() {

});

function add() {

}

function  cancel() {
    $('#tt').datagrid('rejectChanges');
}
function  del() {
    endEditDatagridByIndex('tt1',editId);
    var rows=getDatagridSelections('tt1');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    if(ids!=""){
        communateGet(rootPath +'/good/deleteEntity.shtml?ids='+ids,function back(data){
            reloadDatagridMessage('tt1');
        });
    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function  save() {
    endEditDatagridByIndex('tt1',editId);
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/good/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt1');
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
        url:rootPath + '/good/uploadGoodImg.shtml',
        onSubmit: function(){
            return $(this).form('validate');
        },
        success:function(data){
            $.messager.progress('close');
            var jsonObj = eval( '(' + data + ')' ); // eval();方法
            if(jsonObj=="success"){
                reloadDatagridMessage('tt1');
            }else{
                showErrorAlert("警告",jsonObj);
            }
        }
    });
// submit the form
    $('#form').submit();
}

