var editId;
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/department/findByPage.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        fit:true,
        pagination:true,
        pagePosition:top,
        onDblClickRow:function (rowIndex, rowData) {
            if(editId!=null){
               endEditDatagridByIndex('tt',editId);
                if(isNullDatagrid("tt","code","请输入编号",editId)){
                    editId=rowIndex;
                    var row = getDatagridJsonByIndexAndId('tt',editId);
                    setContentToInputByJson(row);
                    beginEditDatagridByIndex('tt',editId);
                    setRadioCheckedByIdAndContent('isUse','可用');
                }
            }else{
                editId=rowIndex;
                var row = getDatagridJsonByIndexAndId('tt',editId);
                setContentToInputByJson(row);
                beginEditDatagridByIndex('tt',editId);
                setRadioCheckedByIdAndContent('isUse','可用');

            }
        },
        columns:[[
            {field:'id',title:'编号',hidden:true,width:150,align:'center',editor:'text'},
            {field:'code',title:'编码',width:150,align:'center',editor:'text'},
            {field:'parentId',title:'上级编号',width:150,align:'center',editor:'text'},
            {field:'fullName',title:'全称',width:500,align:'center',editor:'text'},
            {field:'simpleName',title:'简称',width:200,align:'center',editor:'text'},
            {field:'englishName',title:'英文简称',width:200,align:'center',editor:'text'},
            {field:'peopleNum',title:'员工人数',width:150,align:'center',editor:'numberbox'},
            {field:'leader',title:'负责人',width:200,align:'center',editor:'text'},
            {field:'contacts',title:'联系人',width:200,align:'center',editor:'text'},
            {field:'phone',title:'电话',width:200,align:'center',editor:'text'},
            {field:'costType',title:'成本类型',width:200,align:'center',editor:'text'},
            {field:'isUse',title:'是否可用',width:150,align:'center',editor:'text'},
            {field:'remark',title:'备注',width:400,align:'center',editor:'text'},
            {field:'organization',title:'组织',width:200,align:'center',editor:'text'},
            {field:'modifytime',title:'修改时间',width:200,align:'center',editor:'text'},
            {field:'userName',title:'操作用户',width:200,align:'center',editor:'text'},
        ]],
        onLoadSuccess:function(data){
        },
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
    showconfirmDialog("提示","是否确认删除?",function (r) {
        if(r){
            var rows=getDatagridSelections('tt');
            var ids="";
            for(i=0;i<rows.length;i++){
                ids=ids+rows[i].id+",";
            }
            if(ids!=""){
                communateGet(rootPath +'/department/deleteEntity.shtml?ids='+ids,function back(data){
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
        communatePost(rootPath +'/department/addEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
