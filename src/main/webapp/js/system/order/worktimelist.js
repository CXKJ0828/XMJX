var editId;

var myArray=new Array();
var datagridDetailsArray=new Array();
function onExpandRow(index, row) {
    var url=rootPath+'/order/getUseTimeByOrderId.shtml?id='+row.id;
    var columns=[[
        { field: 'processName', title: '工序名称', width: 50, align: 'center',editor:'text' },
        { field: 'alltime', title: '总工时', width: 50, align: 'center',editor:'text' },
        { field: 'usetime', title: '实际工时', width: 50, align: 'center',editor:'text' },
    ]];
    var parentDatagridId='orderdetails';
    initExpandRow(index,row,url,columns,parentDatagridId,true,editId,function (number) {
        $('#ddv-' + number).datagrid('appendRow', {
            processName: '<span style="font-weight: bold;">合计</span>',
            alltime: '<span style="font-weight: bold;">' + compute('ddv-'+number,"alltime") + '</span>',
            usetime: '<span style="font-weight: bold;">' + compute('ddv-'+number,"usetime") + '</span>'
        });
    });
}



$(function() {
    $('#tt').datagrid({
        url:rootPath + '/order/findworktimeByPage.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        fit:true,
        pagination:true,
        pagePosition:top,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        view:detailview,
        detailFormatter:detailFormatter,
        onExpandRow:onExpandRow,
        columns:[[
            {field:'id',title:'编号',hidden:true,width:150,align:'center',editor:'text'},
            {field:'orderId',title:'单号',width:270,align:'center',editor:'text'},
            {field:'time',title:'日期',width:180,align:'center',editor:'text'},
            {field:'name',title:'产品名称',width:150,align:'center',editor:'text'},
            {field:'amount',title:'数量',width:150,align:'center',editor:'text'},
            {field:'remarks',title:'备注',width:150,align:'center',editor:'text'},
            {field:'modifyTime',title:'制表时间',width:150,align:'center',editor:'text'},
            {field:'userName',title:'制表用户',width:150,align:'center',editor:'text'},
            {field:'state',title:'单据状态',width:150,align:'center',editor:'text'}
        ]],
        onLoadSuccess:function(data){
        }
    });
});
function find() {
    $("#tt").datagrid("load",{
        "content":getValueById("content"),
        "month":getDataboxValue("selectTime")
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