var editId;
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/order/findproductionByPage.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        pagination:true,
        pagePosition:top,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        columns:[[
            {field:'id',title:'编号',hidden:true,width:100,align:'center',editor:'text'},
            {field:'orderId',title:'单号',width:170,align:'center',editor:'text'},
            {field:'time',title:'日期',width:180,align:'center',editor:'text'},
            {field:'name',title:'产品名称',width:150,align:'center',editor:'text'},
            {field:'amount',title:'数量',width:150,align:'center',editor:'text'},
            {field:'remarks',title:'备注',width:150,align:'center',editor:'text'},
            {field:'modifyTime',title:'制表时间',width:150,align:'center',editor:'text'},
            {field:'userName',title:'制表用户',width:150,align:'center',editor:'text'},
            {field:'completeRate',title:'完工率',width:150,align:'center',editor:'text',formatter: function (value, row, index) {
                if (row != null) {
                    var content=Math.floor(value * 100) / 100;
                    return toPercent(content);
                }
            }}
        ]],
        rowStyler: function(index,row){
            if (row.state =='未完成'){
                return 'background-color:#ff0030;color:#000;';
            }else{
                return 'background-color:#36ff00;color:#fff;';
            }
        },
        onLoadSuccess:function(data){
        },
        onClickRow:function (rowIndex, rowData) {
                editId=rowIndex;
        },
        onDblClickRow:function (rowIndex, rowData) {
            var order=rowData.id;
            if(order==undefined){
                order="";
            }
            $("#add").window({
                width:800,
                maximized:true,
                modal: true,
                height: 400,
                top:50,
                href: rootPath + '/order/productiondetailsUI.shtml?orderId='+order,
                onClose:function () {
                    $('#tt').datagrid("reload");
                }
            });
        }
    });
});
function find() {
    $("#tt").datagrid("load",{
        "content":getValueById("content")
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