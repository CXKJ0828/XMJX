var editId;
$(function() {
    $('#tt').datagrid({
        url:rootPath + '/toolStock/findByPage.shtml',
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
              if(!amountIsAlert()){
                  endEditDatagridByIndex('tt',editId)
                  editId=rowIndex;
                  var row = getDatagridJsonByIndexAndId('tt',editId);
                  beginEditDatagridByIndex('tt',editId);
              }

            }else{
                    editId=rowIndex;
                    var row = getDatagridJsonByIndexAndId('tt',editId);
                    beginEditDatagridByIndex('tt',editId);
            }
        },
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.content=getValueById('content');
            params.toolTypeId=toolTypeId;
        },
        columns:[[
            {field:'id',hidden:true,title:'编号',width:150,align:'center',editor:'text'},
            {field:'name',title:'品名规格',width:300,align:'center',editor:{type:'combogrid',
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
            {field:'typeName',title:'类别',width:300,align:'center',editor:'text'},
            {field:'unitName',title:'单位',width:300,align:'center',editor:'text'},
            {field:'amount',title:'数量',width:300,align:'center',editor:'text'},
            {field:'remarks',title:'备注',width:300,align:'center',editor:'text'},
            {field:'toolId',hidden:true,title:'toolId',width:300,align:'center',editor:'text'},
        ]],
        onLoadSuccess:function(data){
            editId=null;
            setContentToDivSpanById("sumAmount","数量合计:"+data.sumAmount);
        },
        rowStyler: function(index,row){
            var amount=row.amount;
            if (VarToInt(amount)<=2){
                return 'color:#ff0030;';//红色
            }else{
                return '';
            }
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
            {field:'name',title:'品名规格',width:300,align:'center',editor:{type:'combogrid',
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
            {field:'typeName',title:'种类',width:300,align:'center',editor:'text'},
            {field:'unitName',title:'单位',width:300,align:'center',editor:'text'},
            {field:'amount',title:'数量',width:300,align:'center',editor:'text'},
            {field:'remarks',title:'备注',width:300,align:'center',editor:'text'},

        ]],
    });
    initComboGridEditor();
});
function amountIsAlert() {
    if(editId!=null){
        var amount=getContentByEditor('tt',4);
        if(isNull(amount)){
            showErrorAlert("提示","请输入库存数量");
            return true;
        }else {
            return false;
        }
    }else{
        return false;
    }

}

function selectTool(rowIndex, rowData) {
    if(checkIsSelectEd(editId)){
        setContentToDatagridEditorText('tt',editId,"toolId",rowData.id);
        setContentToDatagridEditorText('tt',editId,"name",rowData.name);
        setContentToDatagridEditorText('tt',editId,"unitName",rowData.unitName);
        setContentToDatagridEditorText('tt',editId,"typeName",rowData.typeName);
    }
}

function find() {
    reloadDatagridMessage('tt');
}
function add() {
    if(!amountIsAlert()){
        var rows = $('#tt').datagrid('getRows');
        $('#tt').datagrid('appendRow',
            {
            }
        );
    }

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
                communateGet(rootPath +'/toolStock/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
                    reloadDatagridMessage('tt');
                });
            }
        });

    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function  save() {
    if(!amountIsAlert()){
        endEditDatagridByIndex('tt',editId)
        var rows= $('#tt').datagrid('getChanges');
        if(rows.length>0){
            communatePost(rootPath +'/toolStock/editEntity.shtml',ListToJsonString(rows),function back(data){
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
    var fileName='工具库存导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var data={
        "content":getValueById('content'),
        "toolTypeId":toolTypeId,
    }
    downfileByUrl(rootPath +'/toolStock/exportAll.shtml?entity='+ListToJsonString(data));
}