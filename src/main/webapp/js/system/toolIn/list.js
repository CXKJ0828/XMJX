var editId;
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/toolIn/findByPage.shtml',
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
        onDblClickRow:function (rowIndex, rowData) {
            if(editId!=null){
                endEditDatagridByIndex('tt',editId)
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }else{
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }
            editAndAddSetContentAndChange();
        },
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.content=getValueById('content');
            params.toolTypeId=toolTypeId;
            params.startTime=getDataboxValue('starttime');
            params.endTime=getDataboxValue('endtime');
        },
        columns:[[
            {field:'id',hidden:true,title:'编号',width:150,align:'center',editor:'text'},
            {field:'toolName',title:'品名规格',width:300,align:'center',editor:{type:'combogrid',
                options: {panelWidth: 400,
                    idField: 'name',
                    textField: 'name',
                    url: rootPath +'/tool/toolSelect.shtml',
                    mode: 'remote',
                    columns: [[
                        {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                        {field:'name',title:'名称',width:200,align:'center'},
                        {field:'typeName',title:'类别',width:100,align:'center'},
                        {field:'unitName',title:'单位',width:100,align:'center'},
                        {field:'unitId',hidden:true,title:'unitId',width:100,align:'center'},
                        {field:'toolTypeId',hidden:true,title:'toolTypeId',width:100,align:'center'},
                    ]],
                    fitColumns: true,
                    onSelect:selectTool
                }
            }},
            {field:'toolTypeName',title:'类别',width:300,align:'center',editor:'text'},
            {field:'unitName',title:'单位',width:300,align:'center',editor:'text'},
            {field:'amount',title:'数量',width:300,align:'center',editor:'text'},
            {field:'price',title:'单价',width:300,align:'center',editor:'text'},
            {field:'money',title:'金额',width:300,align:'center',editor:'text'},
            {field:'time',title:'入库时间',width:300,align:'center',editor:'datebox'},
            {field:'remarks',title:'备注',width:300,align:'center',editor:'text'},
            {field:'toolId',title:'toolId',hidden:true,width:300,align:'center',editor:'text'},
            {field:'oldAmount',title:'oldAmount',hidden:true,width:300,align:'center',editor:'text'},
        ]],
        onLoadSuccess:function(data){
            setContentToDivSpanById("sumShow","数量合计:"+data.sumEntity.sumAmount+"金额合计:"+data.sumEntity.sumMoney);
        },
    });

    $('#ttselect').datagrid({
        title:'数据展示',
        iconCls:'icon-ok',
        idField:'id',
        nowrap:false,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        columns:[[
            {field:'toolName',title:'品名规格',width:300,align:'center'},
            {field:'toolTypeName',title:'类别',width:300,align:'center',editor:'text'},
            {field:'unitName',title:'单位',width:300,align:'center',editor:'text'},
            {field:'amount',title:'数量',width:300,align:'center',editor:'text'},
            {field:'price',title:'单价',width:300,align:'center',editor:'text'},
            {field:'money',title:'金额',width:300,align:'center',editor:'text'},
            {field:'time',title:'入库时间',width:300,align:'center',editor:'text'},
            {field:'remarks',title:'备注',width:300,align:'center',editor:'text'},
        ]],
    });
    initComboGridEditor();
});
function editAndAddSetContentAndChange() {
    setEditorOnChange("tt",4,function onChangeBack(index,content) {
        price=getContentByEditor("tt",5);
        setContentToDatagridEditorText("tt",editId,'money',VarToFloat(content)*VarToFloat(price));
    });

    setEditorOnChange("tt",5,function onChangeBack(index,content) {
            amount=getContentByEditor("tt",4);
            setContentToDatagridEditorText("tt",editId,'money',floatToVar2(VarToFloat(content)*VarToFloat(amount)));
    });


}

function selectTool(rowIndex, rowData) {
    if(checkIsSelectEd(editId)){
        setContentToDatagridEditorText('tt',editId,"toolId",rowData.id);
        setContentToDatagridEditorText('tt',editId,"goodName",rowData.name);
        setContentToDatagridEditorText('tt',editId,"unitName",rowData.unitName);
        setContentToDatagridEditorText('tt',editId,"toolTypeName",rowData.typeName);
    }
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

function find() {
    reloadDatagridMessage('tt');
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
                communateGet(rootPath +'/toolIn/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
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
        communatePost(rootPath +'/toolIn/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}

function  downLoad() {
    var rows = $('#ttselect').datagrid('getRows');
    for (var i=rows.length;i>0;i--){
        $('#ttselect').datagrid('deleteRow',i-1);
    }

    var rows=getDatagridSelections("tt");
    for(i=0;i<rows.length;i++){
        $('#ttselect').datagrid('appendRow',
            rows[i]
        );
    }
    var fileName='工具入库统计导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var data={
        "content":getValueById('content'),
        "toolTypeId":toolTypeId,
        "startTime":getDataboxValue('starttime'),
        "endTime":getDataboxValue('endtime'),
    }
    downfileByUrl(rootPath +'/toolIn/exportAll.shtml?entity='+ListToJsonString(data));
}

function upload() {
    $.messager.progress({
        title:'Please waiting',
        msg:'Loading data...'
    });
    $('#form').form({
        url:rootPath + '/toolIn/upload.shtml',
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
                reloadDatagridMessage('tt');
            }
        }
    });
// submit the form
    $('#form').submit();
}