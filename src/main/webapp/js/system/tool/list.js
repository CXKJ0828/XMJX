var editId;
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/tool/findByPage.shtml',
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        nowrap:false,
        pagination:true,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        fit:true,
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.content=getValueById('content');
            params.toolTypeId=toolTypeId;
        },
        onDblClickRow:function (rowIndex, rowData) {
            if(editId!=null){
                endEditDatagridByIndex('tt',editId)
                editId=rowIndex;
                var row = getDatagridJsonByIndexAndId('tt',editId);
                setContentToInputByJson(row);
                beginEditDatagridByIndex('tt',editId);
            }else{
                editId=rowIndex;
                var row = getDatagridJsonByIndexAndId('tt',editId);
                setContentToInputByJson(row);
                beginEditDatagridByIndex('tt',editId);
            }
        },
        columns:[[
            {field:'id',hidden:true,title:'编号',width:150,align:'center',editor:'text'},
            {field:'typeName',title:'类别',width:300,align:'center',editor:{type:'combogrid',
                options: {panelWidth: 400,
                    idField: 'name',
                    textField: 'name',
                    url: rootPath +'/toolType/toolTypeSelect.shtml',
                    mode: 'remote',
                    columns: [[
                        {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                        {field:'name',title:'名称',width:100,align:'center'},
                    ]],
                    fitColumns: true,
                    onSelect:selectToolType
                }
            }},
            {field:'name',title:'品名规格',width:300,align:'center',editor:'text'},
            {field:'unitName',title:'单位',width:300,align:'center',editor:{type:'combogrid',
                options: {panelWidth: 400,
                    idField: 'name',
                    textField: 'name',
                    url: rootPath +'/unit/unitSelect.shtml',
                    mode: 'remote',
                    columns: [[
                        {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                        {field:'name',title:'名称',width:100,align:'center'},
                    ]],
                    fitColumns: true,
                    onSelect:selectUnit
                }
            }},
            {field:'price',title:'单价',width:300,align:'center',editor:'text'},
            {field:'unitId',hidden:true,title:'unitId',width:300,align:'center',editor:'text'},
            {field:'toolTypeId',hidden:true,title:'toolTypeId',width:300,align:'center',editor:'text'},
        ]],
        onLoadSuccess:function(data){
        },
    });
    initComboGridEditor();
});

function selectToolType(rowIndex, rowData) {
    if(checkIsSelectEd(editId)){
        setContentToDatagridEditorText('tt',editId,"toolTypeId",rowData.id);
        setContentToDatagridEditorText('tt',editId,"typeName",rowData.name);
    }
}

function selectUnit(rowIndex, rowData) {
    if(checkIsSelectEd(editId)){
        setContentToDatagridEditorText('tt',editId,"unitId",rowData.id);
        setContentToDatagridEditorText('tt',editId,"unitName",rowData.name);
    }
}

function find() {
    $("#tt").datagrid("load",{
        "content":getValueById("content")
    });
}
function add() {
    var rows = $('#tt').datagrid('getRows');
    $('#tt').datagrid('appendRow',
        {
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
        $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
            if (r){
                communateGet(rootPath +'/tool/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
                    reloadDatagridMessage('tt');
                });
            }
        });

    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function  save() {
    endEditDatagridByIndex('tt',editId)
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/tool/editEntity.shtml',ListToJsonString(rows),function back(data){
            if(isContain(ListToJsonString(data),"success")){
                reloadDatagridMessage('tt');
            }else{
                reloadDatagridMessage('tt');
                showErrorAlert("提示",ListToJsonString(data));
            }

        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
