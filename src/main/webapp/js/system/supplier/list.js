var editId;
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/supplier/findByPage.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        nowrap:false,
        //是否显示行号
        rownumbers:true,
        fit:true,
        fitColumns:true,
        singleSelect:false,
        pagination:true,
        pagePosition:top,
        onDblClickRow:function (rowIndex, rowData) {
            setContentToInputById("fullName","");
            setContentToInputById("simpleName","");
            setContentToInputById("leader","");
            setContentToInputById("taxNumber","");
            setContentToInputById("bank1","");
            setContentToInputById("account1","");
            setContentToInputById("bank2","");
            setContentToInputById("account2","");
            setContentToInputById("contacts","");
            setContentToInputById("phone","");
            setContentToInputById("fax","");
            setContentToInputById("zipCode","");
            setContentToInputById("address","");
            setContentToInputById("remarks","");
            if(editId!=null){
               endEditDatagridByIndex('tt',editId)
                    editId=rowIndex;
                    var row = getDatagridJsonByIndexAndId('tt',editId);
                    setContentToInputByJson(row);
                    beginEditDatagridByIndex('tt',editId);
                    setRadioCheckedByIdAndContent('isUse','可用');
            }else{
                editId=rowIndex;
                var row = getDatagridJsonByIndexAndId('tt',editId);
                setContentToInputByJson(row);
                beginEditDatagridByIndex('tt',editId);
                setRadioCheckedByIdAndContent('isUse','可用');

            }
        },
        columns:[[
            {field:'id',hidden:true,title:'编号',width:150,align:'center',editor:'text'},
            {field:'fullName',title:'单位名称',width:300,align:'center',editor:'text'},
            {field:'simpleName',title:'单位简称',width:200,align:'center',editor:'text'},
            {field:'leader',title:'法人代表',width:150,align:'center',editor:'text'},
            {field:'taxNumber',title:'税号',width:150,align:'center',editor:'text'},
            {field:'bank1',title:'开户行1',width:150,align:'center',editor:'text'},
            {field:'account1',title:'账号1',width:150,align:'center',editor:'text'},
            {field:'bank2',title:'开户行2',width:150,align:'center',editor:'text'},
            {field:'account2',title:'账号2',width:150,align:'center',editor:'text'},
            {field:'contacts',title:'联系人',width:150,align:'center',editor:'text'},
            {field:'phone',title:'联系电话',width:150,align:'center',editor:'text'},
            {field:'fax',title:'传真',width:150,align:'center',editor:'text'},
            {field:'zipCode',title:'邮编',width:150,align:'center',editor:'text'},
            {field:'address',title:'地址',width:150,align:'center',editor:'text'},
            {field:'remarks',title:'备注',width:150,align:'center',editor:'text'},
            {field:'modifyTime',hidden:true,title:'修改时间',width:150,align:'center',editor:'text'},
            {field:'userId',hidden:true,title:'修改用户',width:150,align:'center',editor:'text'},
        ]],
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
                peopleNum:0,
            }
            );
}

function  cancel() {
        $('#tt').datagrid('rejectChanges');
}
function  del() {
    var rows=getDatagridSelections('tt');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    if(ids!=""){
        communateGet(rootPath +'/supplier/deleteEntity.shtml?ids='+ids,function back(data){
            reloadDatagridMessage('tt');
                        });
    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function  save() {
   endEditDatagridByIndex('tt',editId)
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/supplier/addEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
