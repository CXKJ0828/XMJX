var editId;
var myArray=new Array();
var datagridDetailsArray=new Array();
function onExpandRow(index, row) {
    var url=rootPath+'/order/getDetailsWorkersumitByUserId.shtml?userId='+row.userId;
    var columns=[[
        { field: 'orderId', title: '订单号', width: 50, align: 'center',editor:'text' },
        { field: 'goodId', title: '件号', width: 50, align: 'center',editor:'text' },
        { field: 'goodName', title: '件名', width: 50, align: 'center',editor:'text' },
        { field: 'name', title: '工序', width: 50, align: 'center',editor:'text' },
        { field: 'submitAmount', title: '数量', width: 50, align: 'center',editor:'text' },
        { field: 'time', title: '工时', width: 50, align: 'center',editor:'text' },
        { field: 'submitTime', title: '完成时间', width: 50, align: 'center',editor:'text' },
    ]];
    var parentDatagridId='orderdetails';
    initExpandRow(index,row,url,columns,parentDatagridId,true,editId,function (number) {

    });
}


$(function() {
    $('#tt').datagrid({
        url:rootPath + '/order/employeeShow.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        pagination:true,
        fit:true,
        pagePosition:top,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        view:detailview,
        detailFormatter:detailFormatter,
        onExpandRow:onExpandRow,
        columns:[[
            {field:'userName',title:'姓名',width:170,align:'center',editor:'text'},
            {field:'allTime',title:'总工时',width:180,align:'center',editor:'text'},
            {field:'allAmount',title:'总数量',width:150,align:'center',editor:'text'}
        ]],
        onLoadSuccess:function(data){
        },
        onClickRow:function (rowIndex, rowData) {
                editId=rowIndex;
        }
    });
});
function find() {
    $("#tt").datagrid("load",{
        "name":getValueById("name"),
        "starttime":getDataboxValue("starttime"),
        "endtime":getDataboxValue("endtime")
    });
}
function add() {
    var rows = $('#tt').datagrid('getRows');
    $('#tt').datagrid('appendRow',
        {
            isUse:"可用",
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
                communateGet(rootPath +'/order/deleteEntity.shtml?ids='+ids,function back(data){
                    reloadDatagridMessage('tt');
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
        communatePost(rootPath +'/order/addEntity.shtml',ListToJsonString(rows),function back(data){
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
        url:rootPath + '/order/upload.shtml',
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