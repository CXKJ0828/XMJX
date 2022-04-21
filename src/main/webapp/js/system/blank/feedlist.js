var editId;
var pageii = null;
var materialQuality="";
var printTime="";
var clientId="";
$(function() {
    initComboGridEditor();
    id='tt';
    url='/blank/findFeedByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:100,align:'center'},
        {field:'state',title:'状态',width:50,align:'center'},
        {field:'clientFullName',title:'客户',width:100,align:'center'},
        {field:'contractNumber',title:'订单编号',width:100,align:'center'},
        {field:'makeTime',sortable:true,title:'来单日期',width:80,align:'center'},
        {field:'goodName',title:'产品名称',width:100,align:'center'},
        {field:'mapNumber',title:'图号',width:100,align:'center'},
        {field:'goodSize',title:'成品尺寸',width:80,align:'center'},
        {field:'orderAmount',title:'订货数量',width:80,align:'center'},
        {field:'blankSize',title:'下料尺寸',width:80,align:'center',editor:'text'},
        {field:'blankWeight',hidden:true,title:'下料重量',width:80,align:'center'},
        {field:'amount',title:'下料数量',width:80,align:'center',editor:'text'},
        {field:'stockAmount',title:'库存量',width:60,align:'center',editor:'text'},
        {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center'},
        {field:'materialQuality',hidden:true,title:'材质id',width:180,align:'center',editor:'text'},
        {field:'materialQualityName',title:'材质',width:180,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 400,
                idField: 'name',
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
        {field:'length',title:'米数',width:60,align:'center',editor:'text'},
        {field:'weight',title:'吨数',width:60,align:'center',editor:'text'},
        {field:'printTime',title:'下料单打印日期',width:100,align:'center',editor:'text'},
        {field:'completeTime',title:'下料单完成日期',width:100,align:'center',editor:'text'},
        {field:'isFinish',title:'下料是否完成',width:100,align:'center',editor:'text'},
        {field:'remarks1',title:'备注1',width:100,align:'center'},
        {field:'remarks2',title:'备注2',width:100,align:'center'},
        {field:'remarks3',title:'备注3',width:100,align:'center'},
        {field:'isCheck',title:'工艺卡是否核实',width:100,align:'center'},

        {field:'modifytime',hidden:true,title:'修改时间',width:100,align:'center',editor:'text'},
        {field:'userName',hidden:true,title:'修改人',width:100,align:'center',editor:'text'},
    ]];
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:false,
        singleSelect:false,
        pagination:true,
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
    // initDataGridFitColumnsFalse(id,url,columns,function (rowIndex, rowData) {
    //
    // });
});
function find() {
    $("#tt").datagrid("load",{
        "printTime":printTime,
        "materialQuality":materialQuality,
        "makeTime":getValueById("makeTime"),
        "blankSize":getValueById("blankSize"),
        "deliveryTime":getValueById("deliveryTime"),
        "clientId":clientId,
        "state":getContentBycombogrid("state"),
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
    clearDatagridSelections("tt");
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
                communateGet(rootPath +'/blank/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
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
        communatePost(rootPath +'/blank/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }


}
