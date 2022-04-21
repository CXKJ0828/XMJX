var editId;
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/order/findByPage.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        pagination:true,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        columns:[[
            {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
            {field:'makeTime',sortable:true,title:'来单日期',width:180,align:'center',editor:'text'},
            {field:'clientId',hidden:true,title:'客户ID',width:180,align:'center',editor:'text'},
            {field:'clientName',title:'客户',width:180,align:'center',editor:'text'},
            {field:'contractNumber',title:'订单编号',width:180,align:'center',editor:'text'},
            {field:'money',title:'金额',width:180,align:'center',editor:'text'},
            {field:'modifytime',title:'修改时间',width:180,align:'center',editor:'text'},
            {field:'userName',title:'修改人',width:180,align:'center',editor:'text'},
            {field:'state',title:'状态',width:180,align:'center',editor:'text'},
        ]],
        onLoadSuccess:function(data){
            // var allMoney=0;
            // // allMoney
            // for(i=0;i<data.rows.length;i++){
            //     var money=data.rows[i].money;
            //    if(money!=null&&money!=undefined){
            //        allMoney=allMoney+parseFloat(money);
            //    }
            // }
            // allMoney=floatToVar2(allMoney);
            setContentToDivSpanById("allMoney",data.money);
        },
        onClickRow:function (rowIndex, rowData) {
                editId=rowIndex;
        },
        rowStyler: function(index,row){
            var state=row.state;
            if(state==''){
                state="未完成";
            }
            if (state=='已全部发货'){
                return 'color:#048133;font-weight:bold';
            }else{
                return 'color:#000;';
            }
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
                href: rootPath + '/order/detailsUI.shtml?orderId='+order,
                onClose:function () {
                    $('#tt').datagrid("reload");
                }
            });
        }
    });
});
function find() {
    $("#tt").datagrid("load",{
        "content":getValueById("content"),
        "clientId":clientId,
        "startTime":getDataboxValue("starttime"),
        "endTime":getDataboxValue("endtime")
    });
    clearDatagridSelections("tt");
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
    // showconfirmDialog("提示","是否确认删除?",function (r) {
    //     if(r){
            endEditDatagridByIndex('tt',editId);
            var rows=getDatagridSelections('tt');
            var ids="";
            for(i=0;i<rows.length;i++){
                ids=ids+rows[i].id+",";
            }
            if(ids!=""){
                $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
                    if (r){
                        communateGet(rootPath +'/order/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
                            if(data=='success'){
                                reloadDatagridMessage('tt');
                            }else{
                                showErrorAlert("操作",data);
                            }
                        });
                    }
                });
            }else{
                $.messager.alert('警告','不存在可以删除数据','warning');
            }
    //     }
    // });


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
function upload2() {
    $.messager.progress({
        title:'Please waiting',
        msg:'Loading data...'
    });
    $('#form').form({
        url:rootPath + '/order/upload2.shtml',
        onSubmit: function(){
            return $(this).form('validate');
        },
        success:function(data){
            $.messager.progress('close');
            var jsonObj = eval( '(' + data + ')' ); // eval();方法
            if(jsonObj=="success"){
                reloadDatagridMessage('tt');
                showMessagerCenter("提示","导入成功");
            }else{
                showErrorAlert("警告",jsonObj);
            }
        }
    });
// submit the form
    $('#form').submit();
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
                showMessagerCenter("提示","导入成功");
                reloadDatagridMessage('tt');
            }else{
                showErrorAlert("警告",jsonObj);
            }
        }
    });
// submit the form
    $('#form').submit();
}