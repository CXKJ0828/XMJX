var editId;
var pageii = null;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/stock/findByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
        {field:'goodId',hidden:true,title:'产品id',width:230,align:'center',editor:'text'},
        {field:'mapNumber',title:'图号',width:180,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 400,
                idField: 'mapNumber',
                textField: 'mapNumber',
                pagination:true,
                url: rootPath +'/good/goodSelect.shtml',
                mode: 'remote',
                columns: [[
                    {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                    {field:'mapNumber',title:'图号',width:100,align:'center'},
                ]],
                fitColumns: true,
                onSelect:selectGood
            }
        }},
        {field:'goodName',title:'产品名称',width:230,align:'center',editor:'text'},
        {field:'goodSize',title:'成品尺寸',width:180,align:'center',editor:'text'},
        {field:'amount',title:'库存量',width:230,align:'center',editor:'text'},
        {field:'price',title:'单价',width:230,align:'center',editor:'text'},
        {field:'money',title:'金额',width:230,align:'center',editor:'text'},
        {field:'remarks',title:'备注',width:230,align:'center',editor:'text'},
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

    id='ttexport';
    url='/stock/findAll.shtml';
    columns=[[
        {field:'mapNumber',title:'图号',width:180,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 400,
                idField: 'mapNumber',
                textField: 'mapNumber',
                pagination:true,
                url: rootPath +'/good/goodSelect.shtml',
                mode: 'remote',
                columns: [[
                    {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                    {field:'mapNumber',title:'图号',width:100,align:'center'},
                ]],
                fitColumns: true,
                onSelect:selectGood
            }
        }},
        {field:'goodName',title:'产品名称',width:230,align:'center',editor:'text'},
        {field:'goodSize',title:'成品尺寸',width:180,align:'center',editor:'text'},
        {field:'amount',title:'库存量',width:230,align:'center',editor:'text'},
        {field:'price',title:'单价',width:230,align:'center',editor:'text'},
        {field:'money',title:'金额',width:230,align:'center',editor:'text'},
        {field:'remarks',title:'备注',width:230,align:'center',editor:'text'},
    ]];
    initDataGridPaginationFalseToolbarFalse(id,url,columns,function (rowIndex, rowData) {

    });

    id='ttselect';
    url='/stock/findAll.shtml?content=空值';
    columns=[[
        {field:'mapNumber',title:'图号',width:180,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 400,
                idField: 'mapNumber',
                textField: 'mapNumber',
                pagination:true,
                url: rootPath +'/good/goodSelect.shtml',
                mode: 'remote',
                columns: [[
                    {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                    {field:'mapNumber',title:'图号',width:100,align:'center'},
                ]],
                fitColumns: true,
                onSelect:selectGood
            }
        }},
        {field:'goodName',title:'产品名称',width:230,align:'center',editor:'text'},
        {field:'goodSize',title:'成品尺寸',width:180,align:'center',editor:'text'},
        {field:'amount',title:'库存量',width:230,align:'center',editor:'text'},
        {field:'price',title:'单价',width:230,align:'center',editor:'text'},
        {field:'money',title:'金额',width:230,align:'center',editor:'text'},
        {field:'remarks',title:'备注',width:230,align:'center',editor:'text'},
    ]];
    initDataGridPaginationFalseToolbarFalse(id,url,columns,function (rowIndex, rowData) {

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
        $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
            if (r){
                communateGet(rootPath +'/stock/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
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
}
function  save() {
    endEditDatagridByIndex('tt',editId);
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/stock/editEntity.shtml',ListToJsonString(rows),function back(data){
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

    var fileName='产成品统计导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var fileName='产成品统计全部导出.xls';
    $('#ttexport').datagrid('toExcel',fileName);
}
