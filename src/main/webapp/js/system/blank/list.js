var editId;
var pageii = null;
var materialQuality="";
var printTime="";
var clientId="";
var code="";
$(function() {
    initComboGridEditor();
    id='tt';
    url='/blank/findByPage.shtml';
    columns=[[
        {field:'id',checkbox:true,title:'编号',width:100,align:'center'},
        {field:'state',title:'状态',width:50,align:'center'},
        {field:'clientFullName',title:'客户',width:100,align:'center'},
        {field:'contractNumber',title:'订单编号',width:100,align:'center'},
        {field:'makeTime',sortable:true,title:'来单日期',width:80,align:'center'},
        {field:'goodName',title:'产品名称',width:100,align:'center'},
        {field:'mapNumber',title:'图号',width:100,align:'center'},
        {field:'goodSize',title:'成品尺寸',width:80,align:'center'},
        {field:'orderAmount',title:'订货数量',width:80,align:'center'},
        {field:'blankSize',sortable:true,title:'下料尺寸',width:80,align:'center',editor:'text'},
        {field:'blankWeight',hidden:true,title:'下料重量',width:80,align:'center'},
        {field:'amount',title:'下料数量',width:80,align:'center',editor:'text'},
        {field:'stockAmount',title:'库存量',width:60,align:'center',editor:'text'},
        {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center'},
        {field:'materialQuality',hidden:true,title:'材质id',width:180,align:'center',editor:'text'},
        {field:'materialQualityName',title:'材质',width:100,align:'center',editor:{type:'combogrid',
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
        {field:'code',title:'下料单编号',width:100,align:'center',editor:'text'},
        {field:'isFinish',title:'下料是否完成',width:100,align:'center',editor:'text'},
        {field:'isConditioning',title:'调质是否完成',width:100,align:'center',editor:'text'},
        {field:'isNormalizing',title:'正火是否完成',width:100,align:'center',editor:'text'},
        {field:'remarks1',title:'备注1',width:100,align:'center'},
        {field:'remarks2',title:'备注2',width:100,align:'center'},
        {field:'remarks3',title:'备注3',width:100,align:'center'},
        {field:'isCheck',title:'工艺卡是否核实',width:100,align:'center'},
        {field:'modifytime',hidden:true,title:'修改时间',width:100,align:'center',editor:'text'},
        {field:'userName',hidden:true,title:'修改人',width:100,align:'center',editor:'text'},
        {field:'startQRCode',hidden:true,title:'startQRCode',width:100,align:'center'},
    ]];
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:false,
        singleSelect:false,
        pagination:true,
        onBeforeLoad: function (params) {
            code=getContentsByCombobox('code').toString();
            code=code.replace("[","");
            code=code.replace("]","");
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.printTime=printTime;
            params.materialQuality=materialQuality;
            params.code=code;
            params.makeTimestart=getDataboxValue("makeTimestart");
            params.makeTimeend=getDataboxValue("makeTimeend");
            params.blankSize=getValueById("blankSize");
            params.goodSize=getValueById("goodSize");
            params.deliveryTimestart=getDataboxValue("deliveryTimestart");
            params.deliveryTimeend=getDataboxValue("deliveryTimeend");
            clientId=getContentsByCombobox('clientId').toString();
            clientId=clientId.replace("[","");
            clientId=clientId.replace("]","");
            params.clientId=clientId;
            params.goodId=goodId;
            params.orderId=getValueById("orderId");
            params.isFinish=getContentBycombogrid("isFinish");
            params.state=getContentBycombogrid("state");
            params.goodName=getContentBycombogrid("goodName");
            params.content=getValueById("content");
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
            setContentToDivSpanById("all","订货数量:"+data.sum.allOrderAmount+"  下料数量:"+data.sum.allAmount+"  已订料数量:"+data.sum.allBuyAmount+"  未订料数量:"+data.sum.allUnBuyAmount)
        },
        rowStyler: function(index,row){
            var isFinish=row.isFinish;
            if (isFinish=='已接收未完成'){
                return 'color:#ff0030;font-weight:bold';
            }else if(isFinish=='已完成'){
                return 'color:#048133;font-weight:bold';//绿色加粗字体
            }
        },
    });



    columnsExport=[[
        {field:'state',title:'状态',width:50,align:'center'},
        {field:'clientFullName',title:'客户',width:100,align:'center'},
        {field:'contractNumber',title:'订单编号',width:100,align:'center'},
        {field:'makeTime',sortable:true,title:'来单日期',width:80,align:'center'},
        {field:'goodName',title:'产品名称',width:100,align:'center'},
        {field:'mapNumber',title:'图号',width:100,align:'center'},
        {field:'goodSize',title:'成品尺寸',width:80,align:'center'},
        {field:'orderAmount',title:'订货数量',width:80,align:'center'},
        {field:'blankSize',sortable:true,title:'下料尺寸',width:80,align:'center',editor:'text'},
        {field:'blankWeight',hidden:true,title:'下料重量',width:80,align:'center'},
        {field:'amount',title:'下料数量',width:80,align:'center',editor:'text'},
        {field:'stockAmount',title:'库存量',width:60,align:'center',editor:'text'},
        {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center'},
        {field:'materialQualityName',title:'材质',width:100,align:'center'},
        {field:'length',title:'米数',width:60,align:'center',editor:'text'},
        {field:'weight',title:'吨数',width:60,align:'center',editor:'text'},
        {field:'printTime',title:'下料单打印日期',width:100,align:'center',editor:'text'},
        {field:'code',title:'下料单编号',width:100,align:'center',editor:'text'},
        {field:'blankprocessState',title:'下料是否完成',width:100,align:'center',editor:'text'},
        {field:'isConditioning',title:'调质是否完成',width:100,align:'center',editor:'text'},
        {field:'isNormalizing',title:'正火是否完成',width:100,align:'center',editor:'text'},
        {field:'remarks1',title:'备注1',width:100,align:'center'},
        {field:'remarks2',title:'备注2',width:100,align:'center'},
        {field:'remarks3',title:'备注3',width:100,align:'center'},
        {field:'isCheck',title:'工艺卡是否核实',width:100,align:'center'},
    ]];
    $('#ttexport').datagrid({
        title:'数据展示',
        iconCls:'icon-ok',
        nowrap:false,
        pagination:true,
        //是否显示行号
        rownumbers:true,
        fit:true,
        columns:columnsExport
    });

});
function find() {
    setContentToInputById("orderId","");
    getLeftTreeClientMessage();
    reloadTT();
    clearDatagridSelections("tt");

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
function  downLoad() {
    var rows = $('#ttexport').datagrid('getRows');
    for (var i=rows.length;i>0;i--){
        $('#ttexport').datagrid('deleteRow',i-1);
    }

    var rows=getDatagridSelections("tt");
    for(i=0;i<rows.length;i++){
        $('#ttexport').datagrid('appendRow',
            rows[i]
        );
    }
    var fileName='下料单导出.xls';
    $('#ttexport').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    code=getContentsByCombobox('code').toString();
    code=code.replace("[","");
    code=code.replace("]","");
    var data={
        "printTime":printTime,
        "materialQuality":materialQuality,
        "code":code,
        "makeTimestart":getDataboxValue("makeTimestart"),
        "makeTimeend":getDataboxValue("makeTimeend"),
        "blankSize":getValueById("blankSize"),
        "goodSize":getValueById("goodSize"),
        "deliveryTimestart":getDataboxValue("deliveryTimestart"),
        "deliveryTimeend":getDataboxValue("deliveryTimeend"),
        "clientId":clientId,
        "goodId":goodId,
        "orderId":orderId,
        "state":getContentBycombogrid("state"),
        "goodName":getContentBycombogrid("goodName"),
        "content":getValueById("content")
    }
    downfileByUrl(rootPath +'/blank/exportAllByContent.shtml?entity='+ListToJsonString(data));
}


/**
 * 打印图纸
 */
function printDrawingEntity() {
    var rows=getDatagridSelections('tt');
    var ids="";
    var errorMessage="";
    for(i=0;i<rows.length;i++){
        var img=rows[i].img;
        var mapNumber=rows[i].mapNumber;
        if(isNull(img)){
            errorMessage=errorMessage+"图号:"+mapNumber+"不存在图纸<br>"
        }else{
            ids=ids+rows[i].id+",";
        }
    }
    if(isNull(errorMessage)){
        $("#printDrawing").window({
            width:1000,
            title:'打印',
            modal: true,
            top:10,
            href: rootPath + '/heattreat/printDrawingUI.shtml?ids='+ids+"&origin=下料单",
            onClose:function () {
            }
        });
    }else{
        showconfirmDialog("提示",errorMessage+"<br>是否确定继续打印图纸?",function (state) {
            if(state){
                $("#printDrawing").window({
                    width:1000,
                    title:'打印',
                    modal: true,
                    top:10,
                    href: rootPath + '/heattreat/printDrawingUI.shtml?ids='+ids,
                    onClose:function () {
                    }
                });
            }

        })
    }

}
/**
 * 打印工艺卡
 */
function pringTechnologyEntity() {
    var rows=getDatagridSelections('tt');
    var ids="";
    var errorMessage="";
    for(i=0;i<rows.length;i++){
        var count=rows[i].count;
        var mapNumber=rows[i].mapNumber;
        if(isNull(count)){
            errorMessage=errorMessage+"图号:"+mapNumber+"不存在工艺<br>"
        }else{
            ids=ids+rows[i].id+",";
        }
    }
    if(isNull(errorMessage)){
        $("#pringTechnology").window({
            width:1000,
            title:'打印',
            modal: true,
            top:10,
            href: rootPath + '/heattreat/pringTechnologyUI.shtml?ids='+ids+"&origin=下料单",
            onClose:function () {
            }
        });
    }else{
        showconfirmDialog("提示",errorMessage,function (state) {
            if(state){
                $("#pringTechnology").window({
                    width:1000,
                    title:'打印',
                    modal: true,
                    top:10,
                    href: rootPath + '/heattreat/pringTechnologyUI.shtml?ids='+ids,
                    onClose:function () {
                    }
                });
            }
        })
    }

}