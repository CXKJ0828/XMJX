var editId;
var pageii = null;
$(function() {
    id='tt';
    url='/good/findByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
        {field:'mapNumber',title:'图号',width:180,align:'center',editor:'text'},
        {field:'name',title:'产品名称',width:180,align:'center',editor:'text'},
        {field:'roughcastSize',title:'毛坯尺寸',width:230,align:'center',editor:'text'},
        {field:'materialQuality',hidden:true,title:'材质id',width:180,align:'center',editor:'text'},
        {field:'materialQualityName',title:'材质',width:180,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 400,
                idField: 'id',
                textField: 'name',
                url: rootPath +'/materialQualityType/materialQualityTypeSelect.shtml',
                mode: 'remote',
                columns: [[
                    {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                    {field:'name',title:'名称',width:100,align:'center'},
                ]],
                fitColumns: true,
                onSelect:selectMaterialQuality
            }
        }},
        {field:'goodSize',title:'成品尺寸',width:180,align:'center',editor:'text'},
        {field:'roughcastWeight',title:'毛坯重量',width:180,align:'center',editor:'text'},
        {field:'goodWeight',title:'产品重量',width:180,align:'center',editor:'text'},
        {field:'nottaxPrice',title:'不含税单价',width:180,align:'center',editor:'text'},
        {field:'taxPrice',title:'含税单价',width:180,align:'center',editor:'text'},
        {field:'materialCode',title:'物料编码',width:180,align:'center',editor:'text'},
        {field:'remarks',title:'备注',width:180,align:'center',editor:'text'},
        {field:'clientId',hidden:true,title:'客户id',width:180,align:'center',editor:'text'},
        {field:'clientFullName',title:'客户',width:180,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 400,
                idField: 'fullName',
                textField: 'fullName',
                url: rootPath +'/client/clientSelect.shtml',
                mode: 'remote',
                columns: [[
                    {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                    {field:'fullName',title:'客户名称',width:100,align:'center'},
                ]],
                fitColumns: true,
                onSelect:selectClient
            }
        }},
        {field:'modifytime',hidden:true,title:'修改时间',width:180,align:'center',editor:'text'},
        {field:'userName',hidden:true,title:'修改人',width:180,align:'center',editor:'text'},
        {field:'taxRate',title:'税率',hidden:true,width:180,align:'center',editor:'text'},
        {field:'oldMapNumber',hidden:true,title:'图号',width:180,align:'center',editor:'text'},
    ]];
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        fit:true,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        singleSelect:false,
        pagination:true,
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.clientId=clientId;
            params.content=getValueById('content');
        },
        onDblClickRow:function (rowIndex, rowData) {
            var taxRate=rowData.taxRate;
            if(taxRate!=null&&taxRate!=''){
                setContentToInputById("taxRate",taxRate);
            }
            if(editId!=null){
                endEditDatagridByIndex('tt',editId);
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }else{
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }
            editAndAddSetContentAndChange();
        },
        columns:columns,
        onLoadSuccess:function(data){
        },
    });

    id='ttExport';
    columns=[[
        {field:'mapNumber',title:'图号',width:180,align:'center',editor:'text'},
        {field:'name',title:'产品名称',width:180,align:'center',editor:'text'},
        {field:'roughcastSize',title:'毛坯尺寸',width:230,align:'center',editor:'text'},
        {field:'materialQualityName',title:'材质',width:180,align:'center'},
        {field:'goodSize',title:'成品尺寸',width:180,align:'center',editor:'text'},
        {field:'roughcastWeight',title:'毛坯重量',width:180,align:'center',editor:'text'},
        {field:'goodWeight',title:'产品重量',width:180,align:'center',editor:'text'},
        {field:'nottaxPrice',title:'不含税单价',width:180,align:'center',editor:'text'},
        {field:'taxPrice',title:'含税单价',width:180,align:'center',editor:'text'},
        {field:'materialCode',title:'物料编码',width:180,align:'center',editor:'text'},
        {field:'remarks',title:'备注',width:180,align:'center',editor:'text'},
        {field:'clientFullName',title:'客户',width:180,align:'center'},
    ]];
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        idField:'id',
        fit:true,
        fitColumns:true,
        columns:columns
    });
});
function find() {
   reloadDatagridMessage("tt");
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
                communateGet(rootPath +'/good/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
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
        communatePost(rootPath +'/good/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
            if(!isContain(data,"success")){
                showErrorAlert("警告",data);
            }
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }
}

function  downLoad() {
    var rows = $('#ttExport').datagrid('getRows');
    for (var i=rows.length;i>0;i--){
        $('#ttExport').datagrid('deleteRow',i-1);
    }

    var rows=getDatagridSelections("tt");
    for(i=0;i<rows.length;i++){
        $('#ttExport').datagrid('appendRow',
            rows[i]
        );
    }
    var fileName='产品导出.xls';
    $('#ttExport').datagrid('toExcel',fileName);
}
