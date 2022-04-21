var editId;
var pageii = null;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/blanksize/findByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
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
        {field:'goodMapNumber',title:'图号',width:180,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 400,
                idField: 'mapNumber',
                textField: 'mapNumber',
                pagePosition:top,
                pagination:true,
                url: rootPath +'/good/goodSelectByClientId.shtml?clientId='+getValueById('clientId'),
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
        {field:'materialQuality',title:'材质',width:180,align:'center',editor:'text'},
        {field:'blankSize',title:'下料尺寸',width:230,align:'center',editor:'text'},
        {field:'blankWeight',title:'下料重量',width:180,align:'center',editor:'text'},
        {field:'remarks1',title:'备注1',width:230,align:'center',editor:'text'},
        {field:'remarks2',title:'备注2',width:230,align:'center',editor:'text'},
        {field:'remarks3',title:'备注3',width:180,align:'center',editor:'text'},
        {field:'isCheck',title:'工艺卡是否核实',width:180,align:'center',editor:'text'},
        {field:'clientId',hidden:true,title:'客户id',width:180,align:'center',editor:'text'},
        {field:'goodId',hidden:true,title:'产品id',width:180,align:'center',editor:'text'},
        {field:'modifytime',hidden:true,title:'修改时间',width:180,align:'center',editor:'text'},
        {field:'userName',hidden:true,title:'修改人',width:180,align:'center',editor:'text'},
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
            if(editId!=null){
                endEditDatagridByIndex('tt',editId);
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }else{
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }
            editAndAddSetContentAndChange();

            setContentToInputById("clientId",rowData.clientId);
            var ed=getDatagridEditorObj('tt',editId,'goodMapNumber');
            if(ed!=null){
                ed.combogrid('grid').datagrid('options').url =  '${pageContext.request.contextPath}/good/goodSelectByClientId.shtml?clientId='+getValueById('clientId')
                ed.combogrid('grid').datagrid('reload');
            }
        },
        columns:columns,
        onLoadSuccess:function(data){
        },
    });


});
function find() {
   reloadDatagridMessage('tt');
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
        communateGet(rootPath +'/blanksize/deleteEntity.shtml?ids='+ids,function back(data){
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
        communatePost(rootPath +'/blanksize/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }
}

function upload() {
    $.messager.progress({
        title:'Please waiting',
        msg:'Loading data...'
    });
    $('#form').form({
        url:rootPath + '/blanksize/upload.shtml',
        onSubmit: function(){
            return $(this).form('validate');
        },
        success:function(data){
            $.messager.progress('close');
            if(isContain(ListToJsonString(data),"success")){
                reloadTreeMessage('tt');
            }else{
                showErrorAlert("警告",data);
            }
        }
    });
    $('#form').submit();
}

