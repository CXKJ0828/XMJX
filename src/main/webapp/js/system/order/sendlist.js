var editId;
var pageii = null;
var goodId=0;
var clientId="空";
var detailsStock="";
var isFirst=true;
$(function() {
    $('#tt').tree({
        url:rootPath + '/order/findsendlist.shtml',
        method:'post',
        toolbar: '#tb',
        lines:true,
        animate:true,
        onClick:function (node) {
            setContentToInputById("fullName",node.text);
            clientId=node.id;
                // var url=rootPath+'/order/findSendByClientId.shtml?clientId='+clientId;
                // $('#goodprocessList').datagrid('options').url=url;
                // $("#goodprocessList").datagrid('reload');
            reloadDatagridMessage('goodprocessList');
        }
    });
});
function find() {
    reloadDatagridMessage('goodprocessList');
}
function add() {

}

function  cancel() {
    reloadDatagridMessage('goodprocessList');
}
function  deleteEntity() {
    endEditDatagridByIndex('goodprocessList',editId);
    var rows=getDatagridSelections('goodprocessList');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    if(ids!=""){
        $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
            if (r){
                communateGet(rootPath +'/order/hideOrderDetailsEntity.shtml?ids='+ids+'&password='+r,function back(data){
                    reloadDatagridMessage('goodprocessList');
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
        communatePost(rootPath +'/good/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
