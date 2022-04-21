var editId;
var pageii = null;
var clientId="";
$(function() {
    var origin=getValueById("origin");
    initComboGridEditor();
    id='tt';
    url='/blank/findSmallshowByPage2.shtml';
    if(origin=='正火调质'){
        columns=[[
            {field:'id',hidden:true,title:'编号',width:80,align:'center'},
            {field:'pickTime',sortable:true,title:'领料日期',width:80,align:'center'},
            {field:'clientFullName',title:'客户',width:200,align:'center'},
            {field:'contractNumber',title:'订单编号',width:100,align:'center'},
            {field:'mapNumber',title:'图号',width:80,align:'center'},
            {field:'goodName',title:'产品名称',width:80,align:'center'},
            {field:'goodSize',title:'成品尺寸',width:80,align:'center'},
            {field:'materialQualityName',title:'材质',width:80,align:'center'},
            {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center'},
            {field:'amount',title:'数量',width:40,align:'center',editor:'text'},
            {field:'process1',title:'车',width:30,align:'center'},
            {field:'process2',title:'火前',width:40,align:'center'},
            {field:'process3',title:'铣槽',width:40,align:'center'},
            {field:'process4',title:'油槽',width:40,align:'center'},
            {field:'process5',title:'钳',width:30,align:'center'},
            {field:'process6',title:'铣',width:30,align:'center'},
            {field:'process7',title:'氮前',width:40,align:'center'},
            {field:'process8',title:'热渗氮',width:50,align:'center'},
            {field:'process9',title:'氮后',width:40,align:'center'},
            {field:'process10',title:'线切割',width:50,align:'center'}
        ]];
    }else{
        columns=[[
            {field:'id',hidden:true,title:'编号',width:80,align:'center'},
            {field:'pickTime',sortable:true,title:'领料日期',width:80,align:'center'},
            {field:'clientFullName',title:'客户',width:200,align:'center'},
            {field:'contractNumber',title:'订单编号',width:100,align:'center'},
            {field:'mapNumber',title:'图号',width:80,align:'center'},
            {field:'goodName',title:'产品名称',width:80,align:'center'},
            {field:'goodSize',title:'成品尺寸',width:80,align:'center'},
            {field:'materialQualityName',title:'材质',width:80,align:'center'},
            {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center'},
            {field:'amount',title:'数量',width:40,align:'center',editor:'text'},
            {field:'process1',title:'外圆粗',width:50,align:'center'},
            {field:'process2',title:'平磨',width:40,align:'center'},
            {field:'process3',title:'消差',width:40,align:'center'},
            {field:'process4',title:'统一尺寸',width:60,align:'center'},
            {field:'process5',title:'内孔粗',width:50,align:'center'},
            {field:'process6',title:'内孔精',width:50,align:'center'},
            {field:'process7',title:'外圆精',width:50,align:'center'}

        ]];
    }

    $('#'+id).datagrid({
        url:rootPath + url,
        idField:'id',
        //是否显示行号
        rownumbers:true,
        pagination:true,
        fitColumns:true,
        onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
            e.preventDefault(); //阻止浏览器捕获右键事件
            var goodId=rowData.goodId;
            var contractNumber=rowData.contractNumber;
            var materialQuality=rowData.materialQuality;
            var deliveryTime=rowData.deliveryTime;
            var hearttreatId=rowData.id;
            var origin=getValueById("origin");
            var url=rootPath + '/blank/getSmallProgress.shtml?goodId='+goodId+"&contractNumber="+contractNumber+"&deliveryTime="+deliveryTime+"&materialQuality="+materialQuality+"&origin="+origin+"&hearttreatId="+hearttreatId;
            communateGet(url,function (data) {
                document.getElementById("progressContent").innerHTML=data;
                // setContentToDivSpanById("progressContent",progress);
                $('#progressWindow').window('open');
            })
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出
        },
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            params.contractNumber=getValueById("contractNumber");
            params.mapNumber=getValueById("mapNumber");
            params.startTime=getDataboxValue("startTime");
            params.endTime=getDataboxValue("endTime");
            params.startTimePick=getDataboxValue("startTimePick");
            params.endTimePick=getDataboxValue("endTimePick");
            params.goodName=getContentBycombogrid("goodName");
            params.materialQuality=materialQuality;
            params.goodSize=getValueById("goodSize");
            params.origin=getValueById("origin");
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
        "contractNumber":getValueById("contractNumber"),
        "materialQuality":materialQuality,
        "goodSize":getValueById("goodSize"),
        "origin":getValueById("origin"),
        "mapNumber":getValueById("mapNumber"),
        "startTime":getDataboxValue("startTime"),
        "endTime":getDataboxValue("endTime"),
        "startTimePick":getDataboxValue("startTimePick"),
        "endTimePick":getDataboxValue("endTimePick"),
        "goodName":getContentBycombogrid("goodName"),
        "clientId":clientId

    }
    downfileByUrl(rootPath +'/blank/exportSmall2Show.shtml?entity='+ListToJsonString(data));
}

function find() {
    reloadTreeMessage("tt1");
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
