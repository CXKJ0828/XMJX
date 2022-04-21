var editId;
var pageii = null;
var isWarning=false;
$(function() {
    initComboGridEditor();
    id='tt';
    url='/order/findOrderTrackByPage.shtml';
    columns=[[
        {field:'id',hidden:true,title:'编号',width:180,align:'center',editor:'text'},
        {field:'clientSimpleName',title:'客户',width:150,align:'center'},
        {field:'contractNumber',title:'订单编号',width:180,align:'center'},
        {field:'mapNumber',title:'图号',width:180,align:'center'},
        {field:'name',title:'产品名称',width:200,align:'center',editor:'text'},
        {field:'goodSize',sortable:true,title:'成品尺寸',width:150,align:'center',editor:'text'},
        {field:'blankSize',sortable:true,title:'下料尺寸',width:150,align:'center',editor:'text'},
        {field:'materialQualityName',title:'材质',width:150,align:'center',editor:'text'},
        {field:'makeTime',title:'来单日期',width:150,align:'center'},
        {field:'deliveryTime',sortable:true,title:'交货日期',width:150,align:'center'},
        {field:'amount',title:'数量',width:120,align:'center',editor:'text'},
        {field:'blankIsFinish',title:'下料是否完成',width:180,align:'center',styler:function(value,row,index){
            var blankIsFinish=row.blankIsFinish;
            if (blankIsFinish=='已接收未完成'){

                return 'color:#DC143C;';//红色
            }else if(blankIsFinish=='已完成'){
                return 'color:#048133;';//绿色
            }
        }},
        {field:'finishTime',sortable:true,title:'下料完成时间',width:200,align:'center'},
        {field:'turnerIsFinish',title:'车工是否完成',width:180,align:'center',styler:function(value,row,index){
            var turnerIsFinish=row.turnerIsFinish;
            if (turnerIsFinish=='已接收未完成'){

                return 'color:#DC143C;';//红色
            }else if(turnerIsFinish=='已完成'){
                return 'color:#048133;';//绿色
            }
        }},
        {field:'turnerCompleteTime',sortable:true,title:'车工完成时间',width:200,align:'center'},
        {field:'carburizationIsFinish',title:'渗碳是否完成',width:180,align:'center',styler:function(value,row,index){
            var carburizationIsFinish=row.carburizationIsFinish;
            if (carburizationIsFinish=='已接收未完成'){

                return 'color:#DC143C;';//红色
            }else if(carburizationIsFinish=='已完成'){
                return 'color:#048133;';//绿色
            }
        }},
        {field:'feedIsFinish',title:'装炉是否完成',width:180,align:'center',styler:function(value,row,index){
            var feedIsFinish=row.feedIsFinish;
            if (feedIsFinish=='已接收未完成'){

                return 'color:#DC143C;';//红色
            }else if(feedIsFinish=='已完成'){
                return 'color:#048133;';//绿色
            }
        }},
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
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.isWarning=isWarning;
            params.clientId=getContentBySelect("clientId");
            params.makeTimestart=getDataboxValue("makeTimestart");
            params.makeTimeend=getDataboxValue("makeTimeend");
            params.deliveryTimestart=getDataboxValue("deliveryTimestart");
            params.deliveryTimeend=getDataboxValue("deliveryTimeend");
            params.goodName=getContentBySelect("goodName");
            params.materialQuality=getContentBySelect("materialQuality");
            params.blankIsFinish=getContentBySelect("blankIsFinish");
            params.turnerIsFinish=getContentBySelect("turnerIsFinish");
            params.carburizationIsFinish=getContentBySelect("carburizationIsFinish");
            params.feedIsFinish=getContentBySelect("feedIsFinish");
            params.content=getValueById('content');
        },
        rowStyler: function(index,row){
        },
        onLoadSuccess:function(data){
            setContentToDivSpanById("amountSum","数量合计:"+data.amountSum)
        },
        columns:columns,
    });

    id='ttselect';
    url='';
    columnsDown=[[
        {field:'clientSimpleName',title:'客户',width:150,align:'center'},
        {field:'contractNumber',title:'订单编号',width:180,align:'center'},
        {field:'mapNumber',title:'图号',width:180,align:'center'},
        {field:'name',title:'产品名称',width:200,align:'center',editor:'text'},
        {field:'goodSize',sortable:true,title:'成品尺寸',width:150,align:'center',editor:'text'},
        {field:'blankSize',sortable:true,title:'下料尺寸',width:150,align:'center',editor:'text'},
        {field:'materialQualityName',title:'材质',width:150,align:'center',editor:'text'},
        {field:'makeTime',title:'来单日期',width:150,align:'center'},
        {field:'deliveryTime',sortable:true,title:'交货日期',width:150,align:'center'},
        {field:'amount',title:'数量',width:120,align:'center',editor:'text'},
        {field:'blankIsFinish',title:'下料是否完成',width:180,align:'center'},
        {field:'turnerIsFinish',title:'车工是否完成',width:180,align:'center'},
        {field:'turnerCompleteTime',sortable:true,title:'车工完成时间',width:200,align:'center'},
        {field:'carburizationIsFinish',title:'渗碳是否完成',width:180,align:'center'},
        {field:'feedIsFinish',title:'装炉是否完成',width:180,align:'center'},
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
    isWarning=false;
   reloadDatagridMessage('tt');
   reloadTreeMessage('tt1');
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
                communateGet(rootPath +'/order/deleteTrackEntity.shtml?ids='+ids+'&password='+r,function back(data){
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
    var fileName='订单追踪导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var data={
        "isWarning":isWarning,
        "clientId":getContentBySelect("clientId"),
        "makeTimestart":getDataboxValue("makeTimestart"),
        "makeTimeend":getDataboxValue("makeTimeend"),
        "deliveryTimestart":getDataboxValue("deliveryTimestart"),
        "deliveryTimeend":getDataboxValue("deliveryTimeend"),
        "goodName":getContentBySelect("goodName"),
        "materialQuality":getContentBySelect("materialQuality"),
        "blankIsFinish":getContentBySelect("blankIsFinish"),
        "turnerIsFinish":getContentBySelect("turnerIsFinish"),
        "carburizationIsFinish":getContentBySelect("carburizationIsFinish"),
        "feedIsFinish":getContentBySelect("feedIsFinish"),
        "content":getValueById('content'),
    }
    downfileByUrl(rootPath +'/order/exportTrackAll.shtml?entity='+ListToJsonString(data));
}
