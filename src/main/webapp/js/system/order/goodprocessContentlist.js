var editId;
var pageii = null;
var goodId=0;
$(function() {
    id='tt';
    url='/good/findByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
        {field:'mapNumber',title:'图号',width:180,align:'center',editor:'text'},
        {field:'name',title:'产品名称',width:180,align:'center',editor:'text'},
        {field:'goodSize',title:'成品尺寸',width:180,align:'center',editor:'text'},
    ]];
    initDataGridsingleSelectTrue(id,url,columns,function (rowIndex, rowData) {
        var id='add';
        var url='/good/addProcessUI.shtml?goodId='+rowData.id;
        addUIToDatagrid(id,url,function () {
            $('#tt').datagrid("reload");
        });
    },function (rowIndex, rowData) {
        if(rowData.id==goodId){

        }else{
            endEditDatagridByIndex('goodprocessList',editId);
            var goodprocessList= $('#goodprocessList').datagrid('getChanges');
            if(goodprocessList.length>0){
                $("#tt").datagrid('clearSelections');
                $('#tt').datagrid('selectRow',editId);
                showErrorAlert("提示","上一产品有为保存的数据，请保存后查看")
            }else{
                editId=rowIndex;
                goodId=rowData.id;
                img=rowData.img;
                var url=rootPath+'/good/getGoodProcessByGoodId.shtml?id='+goodId;
                $('#goodprocessList').datagrid('options').url=url;
                $("#goodprocessList").datagrid('reload');
            }
        }


    });
});
function find() {
    $("#tt").datagrid("load",{
        "content":getValueById("content")
    });
}
function add() {

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
        communateGet(rootPath +'/good/deleteEntity.shtml?ids='+ids,function back(data){
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
        communatePost(rootPath +'/good/editEntity.shtml',ListToJsonString(rows),function back(data){
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
        url:rootPath + '/good/uploadGoodProcessContentContainMoney.shtml',
        onSubmit: function(){
            return $(this).form('validate');
        },
        success:function(data){
            $.messager.progress('close');
            var jsonObj = eval( '(' + data + ')' ); // eval();方法
            if(jsonObj=="success"){
                reloadDatagridMessage('tt');
            }else{
                showErrorAlert("警告",jsonObj);
            }
        }
    });
// submit the form
    $('#form').submit();
}
function uploadImg() {
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
                reloadDatagridMessage('tt');
            }else{
                showErrorAlert("警告",jsonObj);
            }
        }
    });
// submit the form
    $('#form').submit();
}
