var pageii = null;
var grid = null;
var editingId;
var guid;
var roleId;
$(function() {
    $('#permissions').tree({
        url:rootPath + '/resources/permissionsEntity.shtml',
        method:'get',
        checkbox:true,
        lines:tree,
        loadFilter: function(data){
                    return getTreeJsonByResources(data);
                }
    });
});

function getChecked(){
    var nodes = $('#permissions').tree('getChecked',['checked','indeterminate']);
    var s = '';
    for(var i=0; i<nodes.length; i++){
        if (s != '') s += ',';
        s += nodes[i].id;
    }
    if(s==''){
        showMessager('提示','请选择菜单.');
    }else{
        communateGet(rootPath +'/resources/addUserRes.shtml?ids='+s,function back(data){
            showMessager('提示','权限分配成功.');
            $('#add').window('close');
        });
    }

}

