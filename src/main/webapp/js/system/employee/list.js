var editId=null;
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/employee/findByPage.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        pagination:true,
        fit:true,
        onDblClickRow:function (rowIndex, rowData) {
            $('#roleId').combobox('clear');//清空选中项
            setContentToInputById("id","");
            setContentToInputById("name","");
            var $browsers = $("input[name=sex]");
            $browsers.attr("checked",false);
            setContentToInputById("sex","");
            setContentToInputById("age","");
            setContentToInputById("department","");
            setContentToInputById("title","");
            setContentToInputById("wages","");
            setContentToInputById("IDcard","");
            setContentToInputById("fixedPhone","");
            setContentToInputById("remark","");
            if(editId!=null){
                endEditDatagridByIndex('tt',editId)
                if(isNullDatagrid("tt","id","请输入编号",editId)){
                    editId=rowIndex;
                    var row = getDatagridJsonByIndexAndId('tt',editId);
                    beginEditDatagridByIndex('tt',editId)
                    setContentToInputByJson(row);
                    setRadioCheckedByIdAndContent('isUse','可用');
                }
            }else{
                editId=rowIndex;
                var row = getDatagridJsonByIndexAndId('tt',editId);
                beginEditDatagridByIndex('tt',editId)
                setContentToInputByJson(row);
                setRadioCheckedByIdAndContent('isUse','可用');
            }
        },
        columns:[[
            {field:'id',title:'编号',width:80,align:'center',editor:'text'},
            {field:'from',hidden:true,title:'来源',width:80,align:'center',editor:'text'},
            {field:'name',title:'姓名',width:100,align:'center',editor:'text'},
            {field:'sex',title:'性别',width:80,align:'center',editor:'text'},
            {field:'age',title:'年龄',width:150,align:'center',editor:'text'},
            {field:'roleId',hidden:true,title:'角色编号',width:80,align:'center',editor:'text'},
            {field:'roleName',title:'角色名称',width:150,align:'center',editor:'text'},
            {field:'department',title:'部门',width:150,align:'center',editor:'text'},
            {field:'title',title:'岗位',width:150,align:'center',editor:'text'},
            {field:'wages',title:'岗位工资',width:150,align:'center',editor:'text'},
            {field:'IDcard',title:'身份证',width:180,align:'center',editor:'text'},
            {field:'fixedPhone',title:'联系电话',width:150,align:'center',editor:'text'},
            {field:'remark',title:'备注',width:500,align:'center',editor:'text'},
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
var code="";
function add() {
    var rows = $('#tt').datagrid('getRows');
    if(rows.length==0){
        code="0001";
    }else{
        if(code==''){
            code=rows[rows.length-1].id;
        }
        code=parseInt(code)+1+"";
        if(code.length==1){
            code="000"+code;
        }else if(code.length==2){
            code="00"+code;
        }else if(code.length==3){
            code="0"+code;
        }

    }
    $('#tt').datagrid('appendRow',
        {
            id:code,
            isUse:"可用",
            from:"新增"
        }
    );
}

function  cancel() {
    $('#tt').datagrid('rejectChanges');
}

function resetPassword() {
    var rows=getDatagridSelections('tt');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    if(ids!=""){
        communateGet(rootPath +'/employee/resetPasswordEntity.shtml?ids='+ids,function back(data){
            showMessager("提示","密码重置成功");
        });
    }else{
        $.messager.alert('警告','不存在可以重置密码的数据','warning');
    }
}

function createAccount() {
    var rows=getDatagridSelections('tt');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    if(ids!=""){
        communateGet(rootPath +'/employee/createAccountEntity.shtml?ids='+ids,function back(data){
            showMessager("提示","密码重置成功");
        });
    }else{
        $.messager.alert('警告','不存在可以重置密码的数据','warning');
    }
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
                communateGet(rootPath +'/employee/deleteEntity.shtml?ids='+ids,function back(data){
                    reloadDatagridMessage('tt');
                });
            }else{
                $.messager.alert('警告','不存在可以删除数据','warning');
            }
        }
    });
}
function  save() {
    var message="";
    endEditDatagridByIndex('tt',editId);
    var rows= $('#tt').datagrid('getChanges');
    for(i=0;i<rows.length;i++){
        var roleId=rows[i].roleId;
        var id=rows[i].id;
        if(roleId==null||roleId==''){
            message=message+'编码【'+id+'】<br>'
        }
    }
    if(message!=""){
        showErrorAlert("错误",message+"未选择角色");
    }else{
        if(rows.length>0){
            communatePost(rootPath +'/employee/addEntity.shtml',ListToJsonString(rows),function back(data){
                if(data=='success'){
                    reloadDatagridMessage('tt');
                }else{
                    showErrorAlert("错误",data.split(':')[1]+"编号重复，新增失败");
                    reloadDatagridMessage('tt');
                }

            });
        }else{
            $.messager.alert('警告','不存在可以保存数据','warning');
        }
    }



}
