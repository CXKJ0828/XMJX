var editId;
var pageii = null;
var goodId=0;
var remarks="";
var myArray=new Array();
var datagridDetailsArray=new Array();
function onExpandRow(index, row) {
    var url=rootPath+'/materialbackexcel/findByMaterialBackExcelId.shtml?materialbackexcelId='+row.id;
    var columns=[[
        { field: 'id',hidden:true,title: '编号', width: 50, align: 'center',editor:'text' },
        { field: 'time', title: '回料时间', width: 50, align: 'center',editor:'text' },
        { field: 'userShow', title: '回料人', width: 50, align: 'center',editor:'text' },
        { field: 'length', title: '回料米数', width: 50, align: 'center',editor:'text' },
        { field: 'weight', title: '回料重量', width: 50, align: 'center',editor:'text' },
        { field: 'materialbackexcelId',hidden:true,title: '回料单id', width: 50, align: 'center',editor:'text' },
    ]];
    var parentDatagridId='goodprocessList';
    initExpandRow(index,row,url,columns,parentDatagridId,true,editId,function (number) {

    });
}

$(function() {
    $('#tt').tree({
        url:rootPath + '/materialbackexcel/findlist.shtml',
        method:'post',
        toolbar: '#tb',
        lines:true,
        animate:true,
        onClick:function (node) {
            remarks=node.id;
            reloadDatagridMessage("goodprocessList");
        }
    });
});

function upload() {
    $.messager.progress({
        title:'Please waiting',
        msg:'Loading data...'
    });
    var url='/materialbackexcel/upload.shtml';
    $('#form').form({
        url:rootPath + url,
        onSubmit: function(){
            return $(this).form('validate');
        },
        success:function(data){
            $.messager.progress('close');
            var jsonObj = eval( '(' + data + ')' ); // eval();方法
            if(jsonObj=="success"){
                showMessagerCenter("提示","导入成功");
                reloadTreeMessage('tt');
                remarks="";
                reloadDatagridMessage('goodprocessList')
            }else{
                showErrorAlert("警告",jsonObj);
            }
        }
    });
    $('#form').submit();
}

function uploadBack() {
    $.messager.progress({
        title:'Please waiting',
        msg:'Loading data...'
    });
    var url='/materialbackexcel/uploadBack.shtml';
    $('#formBack').form({
        url:rootPath + url,
        onSubmit: function(){
            return $(this).form('validate');
        },
        success:function(data){
            $.messager.progress('close');
            var jsonObj = eval( '(' + data + ')' ); // eval();方法
            if(jsonObj=="success"){
                showMessagerCenter("提示","导入成功");
                reloadTreeMessage('tt');
                remarks="";
                reloadDatagridMessage('goodprocessList')
            }else{
                showErrorAlert("警告",jsonObj);
                reloadDatagridMessage('goodprocessList')
            }
        }
    });
    $('#formBack').submit();
}
