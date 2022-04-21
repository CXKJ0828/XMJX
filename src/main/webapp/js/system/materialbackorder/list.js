var editId;
var pageii = null;
var goodId=0;
var materialQualityAndMaterialbackorderId="";
var myArray=new Array();
var datagridDetailsArray=new Array();
function onExpandRow(index, row) {
    var url=rootPath+'/materialbackorder/findByMaterialBackOrderDetailsId.shtml?materialBackOrderDetailsId='+row.id;
    var columns=[[
        { field: 'id',hidden:true,title: '编号', width: 50, align: 'center',editor:'text' },
        { field: 'arrivalTime', title: '到货日期', width: 50, align: 'center',editor:'text' },
        { field: 'materialQuality', title: '材质', width: 50, align: 'center',editor:'text' },
        { field: 'outerCircle', title: '规格/Φ', width: 50, align: 'center',editor:'text' },
        { field: 'amount', title: '支数', width: 50, align: 'center',editor:'text' },
        { field: 'length', title: '回料米数', width: 50, align: 'center',editor:'text' },
        { field: 'weight', title: '重量', width: 50, align: 'center',editor:'text' },
        { field: 'buyLength',hidden:true,title: '订料米数', width: 50, align: 'center',editor:'text' },
        { field: 'lackAmount',hidden:true, title: '欠料米数', width: 50, align: 'center',editor:'text' },
        { field: 'taxPrice', title: '含税单价', width: 50, align: 'center',editor:'text' },
        { field: 'taxMoney', title: '含税金额', width: 50, align: 'center',editor:'text' },
        { field: 'materialBackOrderId',hidden:true,title: '回料单id', width: 50, align: 'center',editor:'text' },
        { field: 'materialBuyOrderDetailsId',hidden:true,title: '订料明细id', width: 50, align: 'center',editor:'text' },
    ]];
    var parentDatagridId='orderdetails';
    initExpandRow(index,row,url,columns,parentDatagridId,true,editId,function (number) {

    });
}

$(function() {
    $('#tt').tree({
        url:rootPath + '/materialbackorder/findlist.shtml',
        method:'post',
        toolbar: '#tb',
        lines:true,
        animate:true,
        onClick:function (node) {
            // if(isContain(node.id,"回料单id")){
            materialbackorderId=node.id;
                var url=rootPath+'/materialbackorder/findByMaterialQualityAndMaterialbackorderId.shtml?materialbackorderId='+node.id;
                $('#goodprocessList').datagrid('options').url=url;
                $("#goodprocessList").datagrid('reload');
            // }
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
    var url='/materialbackorder/upload.shtml';
    // var url='/materialbackorder/uploadLimit.shtml';
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
            }else{
                showErrorAlert("警告",jsonObj);
            }
        }
    });
    $('#form').submit();
}
function uploadMeters() {
    $.messager.progress({
        title:'Please waiting',
        msg:'Loading data...'
    });
    var url='/materialbackorder/uploadMeters.shtml';
    // var url='/materialbackorder/uploadLimit.shtml';
    $('#formMeters').form({
        url:rootPath + url,
        onSubmit: function(){
            return $(this).form('validate');
        },
        success:function(data){
            $.messager.progress('close');
            var jsonObj = eval( '(' + data + ')' ); // eval();方法
            if(jsonObj=="success"){
                showMessagerCenter("提示","导入成功");
            }else{
                showErrorAlert("警告",jsonObj);
            }
        }
    });
    $('#formMeters').submit();
}