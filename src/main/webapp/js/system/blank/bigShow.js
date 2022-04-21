var editId;
var pageii = null;
var processId="46605";
var clientId="";
var code="";
var datagridDetailsArray=new Array();
var myArray=new Array();
function onExpandRow(index, row) {
    editId=index;
    var blankprocessId=row.id;
    var url=rootPath+'/blank/findWorkersubmitByBlankprocessId.shtml?blankprocessId='+blankprocessId;
    var columns=[[
        { field: 'id', title: '编号',hidden:true,align:'center', editor:'text' },
        { field: 'startTime', title: '接收时间', width: 50, align: 'center',editor:'text' },
        { field: 'userName', title: '接收人', width: 50, align: 'center',editor:'text' },
        { field: 'amount', title: '接收数量', width: 50, align: 'center',editor:'text' },
        { field: 'completeTime', title: '完成时间', width: 50, align: 'center',editor:'text' },
        { field: 'completeAmount', title: '完成数量', width: 50, align: 'center',editor:'text' },
        { field: 'badAmount', title: '废品数', width: 50, align: 'center',editor:'text' },
        { field: 'deductRate', title: '扣除比例', width: 50, align: 'center',editor:'text' },
    ]];
    var parentDatagridId='tt';
    initExpandRow(index,row,url,columns,parentDatagridId,true,editId,function (number) {
    });
}


$(function() {
    initComboGridEditor();
    id='tt';
    url='/blank/findBigShowByPage.shtml';
    columns=[[
        {field:'code',title:'编号',width:100,align:'center'},
        {field:'clientFullName',title:'客户',width:200,align:'center'},
        {field:'contractNumber',title:'订单编号',width:100,align:'center'},
        {field:'mapNumber',title:'图号',width:100,align:'center'},
        {field:'materialCode',title:'物料编码',width:80,align:'center'},
        {field:'goodName',title:'产品名称',width:80,align:'center'},
        {field:'goodSize',title:'成品尺寸',width:80,align:'center'},
        {field:'blankSize',sortable:true,title:'下料尺寸',width:80,align:'center'},
        {field:'materialQualityName',title:'材质',width:80,align:'center'},
        {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center'},
        {field:'number',title:'工序号',width:50,align:'center'},
        {field:'processName',title:'工序',width:50,align:'center'},
        {field:'amount',title:'数量',width:40,align:'center',editor:'text'},
        {field:'planneedDay',title:'预计用时',width:60,align:'center',editor:'text'},
        {field:'completeAmount',title:'完成数量',width:60,align:'center',editor:'text'},
        {field:'badAmount',title:'废品数量',width:60,align:'center',editor:'text'},
        {field:'id',hidden:true,title:'blankprocessId',width:60,align:'center',editor:'text'},
        {field:'state',title:'状态',width:80,align:'center',formatter: function(value,row,index){
            if (row.state=='已完成'){
                completeAmount=row.completeAmount;
                return '已完成:'+completeAmount;

            } else {
                return row.state;
            }
        }},
        {field:'progress',title:'进度',width:2000,align:'left',editor:'text'},
    ]];
    $('#'+id).datagrid({
        url:rootPath + url,
        idField:'state',
        //是否显示行号
        rownumbers:true,
        singleSelect:true,
        pagination:true,
        view:detailview,
        detailFormatter:detailFormatter,
        onExpandRow:onExpandRow,
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.processId=processId;
            params.contractNumber=getValueById("contractNumber");
            params.code=code;
            params.materialCode=materialCode;
            params.mapNumber=getValueById("mapNumber");
            params.startTime=getDataboxValue("startTime");
            params.endTime=getDataboxValue("endTime");
            params.state=getContentBycombogrid("state");
            params.goodName=getContentBycombogrid("goodName");
            params.materialQuality=materialQuality;
            params.blankSize=getValueById("blankSize");
            params.goodSize=getValueById("goodSize");
            params.clientId=clientId;
        },
        onDblClickRow:function (rowIndex, rowData) {
            var img=rowData.img;
            if(img==undefined){
                showErrorAlert("提示","暂无图纸");
            }else{
                var goodId=rowData.goodId;
                window.open(rootPath + '/good/showImgUI.shtml?goodId='+goodId);
            }
        },
        columns:columns,
        onLoadSuccess:function(data){
            var amount=data.amount;
            setContentToDivSpanById("allAmount","数量合计:"+amount);
        }

    });
});
function downLoadAll() {
    var data={
        "processId":processId,
        "contractNumber":getValueById("contractNumber"),
        "code":code,
        "goodId":goodId,
        "materialQuality":materialQuality,
        "orderId":orderId,
        "goodSize":getValueById("goodSize"),
        "blankSize":getValueById("blankSize"),
        "materialCode":materialCode,
        "mapNumber":getValueById("mapNumber"),
        "startTime":getDataboxValue("startTime"),
        "endTime":getDataboxValue("endTime"),
        "state":getContentBycombogrid("state"),
        "goodName":getContentBycombogrid("goodName"),
        "clientId":clientId

    }
    downfileByUrl(rootPath +'/blank/exportBigShow.shtml?entity='+ListToJsonString(data));
}

function find() {
    var startTime=getDataboxValue("startTime");
    var endTime=getDataboxValue("endTime");
    communateGet(rootPath + '/blank/findBigShowClientByContent.shtml?processId='+processId
        +"&contractNumber="+getValueById("contractNumber")
        +"&code="+code
        +"&materialCode="+materialCode
        +"&mapNumber="+getValueById("mapNumber")
        +"&startTime="+startTime
        +"&endTime="+endTime
        +"&state="+getContentBycombogrid("state")
        +"&goodName="+getContentBycombogrid("goodName")
        +"&materialQuality="+materialQuality
        +"&blankSize="+getValueById("blankSize")
        +"&goodSize="+getValueById("goodSize")
        ,function (data) {
            treeObjext.tree({data: data});
        })
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
