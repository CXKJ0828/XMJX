var editId;
var pageii = null;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/materialweight/findByPage.shtml';
    columns=[[
        {field:'id',title:'编号',hidden:true,width:230,align:'center',editor:'text'},
        {field:'materialQuality',title:'材质',width:230,align:'center',editor:'text'},
        {field:'outerCircle',title:'规格',width:230,align:'center',editor:'text'},
        {field:'length',title:'库存米数',width:180,align:'center',editor:'text'},
        {field:'weight',title:'重量',width:180,align:'center',editor:'text'},
        {field:'taxPrice',title:'含税单价',width:180,align:'center',editor:'text'},
        {field:'money',title:'金额',width:180,align:'center',editor:'text'},
        {field:'remarks',title:'备注',width:180,align:'center',editor:'text'},
    ]];
    initDataGrid(id,url,columns,function (rowIndex, rowData) {
        if(editId!=null){
            endEditDatagridByIndex('tt',editId);
            editId=rowIndex;
            beginEditDatagridByIndex('tt',editId);
        }else{
            editId=rowIndex;
            beginEditDatagridByIndex('tt',editId);
        }
        editAndAddSetContentAndChange();
    });
    id='ttselect';
    url='/materialweight/findAllByContent.shtml?content=空值';
    columnsDown=[[
        {field:'materialQuality',title:'材质',width:230,align:'center',editor:'text'},
        {field:'outerCircle',title:'规格',width:230,align:'center',editor:'text'},
        {field:'length',title:'库存米数',width:180,align:'center',editor:'text'},
        {field:'weight',title:'重量',width:180,align:'center',editor:'text'},
        {field:'taxPrice',title:'含税单价',width:180,align:'center',editor:'text'},
        {field:'money',title:'金额',width:180,align:'center',editor:'text'},
        {field:'remarks',title:'备注',width:180,align:'center',editor:'text'},
    ]];
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        idField:'id',
        fit:true,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        singleSelect:false,
        pagination:true,
        columns:columnsDown,
    });


    id='ttexport';
    url='/materialweight/findAllByContent.shtml';
    columnsDown=[[
        {field:'materialQuality',title:'材质',width:230,align:'center',editor:'text'},
        {field:'outerCircle',title:'规格',width:230,align:'center',editor:'text'},
        {field:'length',title:'库存米数',width:180,align:'center',editor:'text'},
        {field:'weight',title:'重量',width:180,align:'center',editor:'text'},
        {field:'taxPrice',title:'含税单价',width:180,align:'center',editor:'text'},
        {field:'money',title:'金额',width:180,align:'center',editor:'text'},
        {field:'remarks',title:'备注',width:180,align:'center',editor:'text'},
    ]];
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        idField:'id',
        fit:true,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        singleSelect:false,
        pagination:true,
        columns:columnsDown,
    });
});
function find() {
    $("#tt").datagrid("load",{
        "content":getValueById("content")
    });



    $("#ttexport").datagrid("load",{
        "content":getValueById("content")
    });
}
function add() {
    var rows = $('#tt').datagrid('getRows');
    $('#tt').datagrid('appendRow',
        {
        }
    );
    editAndAddSetContentAndChange();

}

function  cancel() {
    $('#tt').datagrid('rejectChanges');
}
function  del() {
    endEditDatagridByIndex('tt',editId);
    var rows=getDatagridSelections('tt');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    if(ids!=""){
        communateGet(rootPath +'/materialweight/deleteEntity.shtml?ids='+ids,function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function  save() {
    endEditDatagridByIndex('tt',editId);
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/materialweight/editEntity.shtml',ListToJsonString(rows),function back(data){
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
    var fileName='物料统计导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var fileName='物料统计全部导出.xls';
    $('#ttexport').datagrid('toExcel',fileName);
}
