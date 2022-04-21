var editId;
var distributionAmountSum=0;
var heattreatEntity="";
$(function() {
    var roleId=getValueById("roleId");
    var roleName=getValueById("roleName");
    var origin=getValueById("origin");
    var remarks=getValueById("remarks");
    if(getValueById("origin")=='车后'){
        getObjectById("makeSendDiv").style.display="none";
    }else if((getValueById("origin")=='渗碳'&&getValueById("remarks")=='外圆粗磨')
        ||getValueById("origin")=='端面平磨'
        ||getValueById("origin")=='消差磨'
        ||getValueById("origin")=='统一尺寸'
        ||getValueById("origin")=='内孔磨'
        ||getValueById("origin")=='外圆精磨'
        ){
        getObjectById("isMakeCarburizationDiv").style.display="none";
        getObjectById("makeSendDiv").style.display="none";
    }else if(getValueById("origin")=='成品库'){
        getObjectById("divUserId").style.display="none";
        getObjectById("isMakeCarburizationDiv").style.display="none";
        getObjectById("makeHeattreatDiv").style.display="none";
    }else if(getValueById("origin")=='打磨'){
        getObjectById("divUserId").style.display="none";
        getObjectById("isMakeCarburizationDiv").style.display="none";
        getObjectById("makeHeattreatDiv").style.display="none";
        getObjectById("makeSendDiv").style.display="none";
    }else{
        getObjectById("divUserId").style.display="none";
        getObjectById("isMakeCarburizationDiv").style.display="none";
        getObjectById("makeSendDiv").style.display="none";
    }
    if(getValueById("origin")=='渗碳'
        ||getValueById("origin")=='中频'
        ||getValueById("origin")=='调质'
        ||getValueById("origin")=='正火'
        ||(getValueById("origin")=='渗碳'&&getValueById("remarks")=='外圆粗磨')
        ||getValueById("origin")=='端面平磨'
        ||getValueById("origin")=='消差磨'
        ||getValueById("origin")=='统一尺寸'
        ||getValueById("origin")=='内孔磨'
        ||getValueById("origin")=='外圆精磨'
        ||getValueById("origin")=='外磨内磨端面'
        ){
        getObjectById("makeHeattreatDiv").style.display="none";
    }
    if(getValueById("origin")!='外磨内磨端面'){
        getObjectById("makeGoodDiv").style.display="none";
    }
    if(getValueById("origin")!='调质'){
        getObjectById("makeMidfrequencyDiv").style.display="none";
    }
    if(getValueById("origin")=='渗碳'&&getValueById("remarks")=='平磨（垫片）'){
        getObjectById("makeGoodDiv").style.display="block";
        getObjectById("makeSendDiv").style.display="none";
    }
    if(roleId=='16'||isContain(roleName,'班组')){
        var roleId=getValueById("roleId");
        var roleName=getValueById("roleName");
        var origin=getValueById("origin");
        var remarks=getValueById("remarks");
        if(origin!='调质'){
            getObjectById("distribution").style.display="none";
            getObjectById("distributionOprate").style.display="none";
                columns=[[
                    {field:'id',hidden:true,title:'编号',width:300,align:'center'},
                    {field:'pickTime',sortable:true,title:'领料日期',width:300,align:'center'},
                    {field:'clientName',title:'客户',width:300,align:'center'},
                    {field:'contractNumber',title:'合同号',width:300,align:'center'},
                    {field:'mapNumber',title:'图号',width:300,align:'center'},
                    {field:'goodName',title:'产品名称',width:300,align:'center'},
                    {field:'goodSize',sortable:true,title:'产品尺寸',width:300,align:'center'},
                    {field:'amount',title:'数量',width:150,align:'center'},
                    {field:'goodWeight',title:'单重',width:150,align:'center'},
                    {field:'weight',title:'总重量',width:150,align:'center'},
                    {field:'materialQualityName',title:'材质',width:300,align:'center'},
                    {field:'hardnessName',title:'硬度',width:300,align:'center'},
                    {field:'deliveryTime',sortable:true,title:'交货日期',width:300,align:'center'},
                    {field:'remarks1',title:'备注1',width:300,align:'center'},
                    {field:'backTime',sortable:true,title:'回料日期',width:250,align:'center',editor:'datebox'},
                    {field:'backAmount',title:'回料数量',width:250,align:'center',editor:'text'},
                    {field:'distributionAmount',hidden:true,title:'待分配数量',width:80,align:'center',editor:'text'},
                    {field:'remarks2',title:'备注2',width:300,align:'center',editor:'text'},
                    {field:'origin',hidden:true,title:'来源',width:300,align:'center',editor:'text'},
                    {field:'goodId',hidden:true,title:'产品id',width:300,align:'center',editor:'text'},
                    {field:'materialQuality',hidden:true,title:'材质id',width:300,align:'center',editor:'text'},
                    {field:'hardnessId',hidden:true,title:'硬度id',width:300,align:'center',editor:'text'},
                    {field:'clientId',hidden:true,title:'clientId',width:300,align:'center',editor:'text'},
                    {field:'count',hidden:true,title:'count',width:300,align:'center',editor:'text'},
                ]];
            initSmallDatagird();
        }else{
            columns=
                [[
                    {field:'id',hidden:true,title:'编号',width:300,align:'center'},
                    {field:'pickTime',sortable:true,title:'领料日期',width:60,align:'center'},
                    {field:'clientName',title:'客户',width:200,align:'center'},
                    {field:'contractNumber',title:'合同号',width:100,align:'center'},
                    {field:'mapNumber',title:'图号',width:100,align:'center'},
                    {field:'goodName',title:'产品名称',width:80,align:'center'},
                    {field:'goodSize',sortable:true,title:'产品尺寸',width:80,align:'center'},
                    {field:'amount',title:'数量',width:50,align:'center'},
                    {field:'goodWeight',title:'单重',width:50,align:'center'},
                    {field:'weight',title:'总重量',width:50,align:'center'},
                    {field:'materialQualityName',title:'材质',width:80,align:'center'},
                    {field:'hardnessName',title:'硬度',width:80,align:'center'},
                    {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center'},
                    {field:'remarks1',title:'备注1',width:80,align:'center'},
                    {field:'backTime',sortable:true,title:'回料日期',width:80,align:'center',editor:'datebox'},
                    {field:'backAmount',title:'回料数量',width:50,align:'center',editor:'text'},
                    {field:'distributionAmount',title:'待分配数量',width:60,align:'center',editor:'text'},
                    {field:'remarks2',title:'备注2',width:80,align:'center',editor:'text'},
                    {field:'opreateUserShow',title:'接收员工',width:80,align:'center'},
                    {field:'opreateProcessShow',title:'接收工序',width:60,align:'center'},
                    {field:'oprateState',title:'状态',width:70,align:'center'},
                    {field:'progress',title:'进度',width:1000,align:'left'},
                    {field:'oprateProcessId',hidden:true,title:'oprateProcessId',width:300,align:'center',editor:'text'},
                    {field:'oprateUserId',hidden:true,title:'oprateUserId',width:300,align:'center',editor:'text'},
                    {field:'origin',hidden:true,title:'来源',width:300,align:'center',editor:'text'},
                    {field:'goodId',hidden:true,title:'产品id',width:300,align:'center',editor:'text'},
                    {field:'materialQuality',hidden:true,title:'材质id',width:300,align:'center',editor:'text'},
                    {field:'hardnessId',hidden:true,title:'硬度id',width:300,align:'center',editor:'text'},
                    {field:'clientId',hidden:true,title:'clientId',width:300,align:'center',editor:'text'},
                    {field:'count',hidden:true,title:'count',width:300,align:'center',editor:'text'},
                ]];
                initAllDatagrid(roleId);
        }


    }
    else{
        var roleId=getValueById("roleId");
        var roleName=getValueById("roleName");
        var origin=getValueById("origin");
        var remarks=getValueById("remarks");
        if(origin=='成品库'){
            columns=
                [[
                    {field:'id',hidden:true,title:'编号',width:300,align:'center',editor:'text'},
                    {field:'pickTime',sortable:true,title:'完成日期',width:80,align:'center',editor:'datebox'},
                    {field:'clientName',title:'客户',width:200,align:'center',editor:{type:'combogrid',
                        options: {panelWidth:200,
                            idField: 'fullName',
                            textField: 'fullName',
                            url: rootPath+'/client/clientSelect.shtml',
                            mode: 'remote',
                            columns: [[
                                {field:'id',hidden:true,title:'客户编码',width:100},
                                {field:'fullName',title:'客户全称',width:350},
                            ]],
                            fitColumns: true,
                            onSelect:selectClient
                        }
                    }},
                    {field:'contractNumber',title:'合同号',width:100,align:'center',editor:'text'},
                    {field:'mapNumber',title:'图号',width:100,align:'center',editor:{type:'combogrid',
                        options: {panelWidth:400,
                            idField: 'mapNumber',
                            textField: 'mapNumber',
                            url: rootPath+'/good/goodSelect.shtml',
                            mode: 'remote',
                            pagination:true,
                            columns: [[
                                {field:'id',hidden:true,title:'编码',width:150,align:'center'},
                                {field:'mapNumber',title:'图号',width:150,align:'center'},
                                {field:'name',title:'产品名称',width:80,align:'center'},
                                {field:'materialQuality',hidden:true,title:'材质',width:150,align:'center'},
                                {field:'goodSize',title:'产品尺寸',width:80,align:'center'},
                                {field:'blankSize',title:'下料尺寸',width:100,align:'center'},
                                {field:'goodWeight',hidden:true,title:'产品重量',width:150,align:'center'},
                            ]],
                            fitColumns: true,
                            onSelect:selectGood
                        }
                    }},
                    {field:'goodName',title:'产品名称',width:80,align:'center',editor:'text'},
                    {field:'goodSize',sortable:true,title:'产品尺寸',width:80,align:'center',editor:'text'},
                    {field:'amount',title:'数量',width:50,align:'center',editor:'text'},
                    {field:'materialQualityName',title:'材质',width:80,align:'center',editor:{type:'combogrid',
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
                    {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center',editor:'datebox'},
                    {field:'remarks1',title:'备注1',width:100,align:'center',editor:'text'},
                    {field:'sendTime',sortable:true,title:'发货日期',width:80,align:'center',editor:'datebox'},
                    {field:'sendAmount',title:'发货数量',width:80,align:'center',editor:'text'},
                    {field:'stockAmount',title:'库存数量',width:80,align:'center',editor:'text'},
                    {field:'userNames',title:'上一级员工',width:400,align:'left'},
                    {field:'oprateProcessId',hidden:true,title:'oprateProcessId',width:300,align:'center',editor:'text'},
                    {field:'oprateUserId',hidden:true,title:'oprateUserId',width:300,align:'center',editor:'text'},
                    {field:'origin',hidden:true,title:'来源',width:300,align:'center',editor:'text'},
                    {field:'goodId',hidden:true,title:'产品id',width:300,align:'center',editor:'text'},
                    {field:'materialQuality',hidden:true,title:'材质id',width:300,align:'center',editor:'text'},
                    {field:'hardnessId',hidden:true,title:'硬度id',width:300,align:'center',editor:'text'},
                    {field:'clientId',hidden:true,title:'clientId',width:300,align:'center',editor:'text'},
                    {field:'count',hidden:true,title:'count',width:300,align:'center',editor:'text'},
                ]];
        }
        else
            if(origin=='端面平磨'||origin=='消差磨'||origin=='统一尺寸'||origin=='内孔磨'
            || origin=='外圆精磨'
            || origin=='外磨内磨端面'
                || origin=='打磨'
            ){
            columns=
                [[
                    {field:'id',hidden:true,title:'编号',width:300,align:'center',editor:'text'},
                    {field:'pickTime',sortable:true,title:'完成日期',width:80,align:'center',editor:'datebox'},
                    {field:'clientName',title:'客户',width:200,align:'center',editor:{type:'combogrid',
                        options: {panelWidth:200,
                            idField: 'fullName',
                            textField: 'fullName',
                            url: rootPath+'/client/clientSelect.shtml',
                            mode: 'remote',
                            columns: [[
                                {field:'id',hidden:true,title:'客户编码',width:100},
                                {field:'fullName',title:'客户全称',width:350},
                            ]],
                            fitColumns: true,
                            onSelect:selectClient
                        }
                    }},
                    {field:'contractNumber',title:'合同号',width:100,align:'center',editor:'text'},
                    {field:'mapNumber',title:'图号',width:100,align:'center',editor:{type:'combogrid',
                        options: {panelWidth:400,
                            idField: 'mapNumber',
                            textField: 'mapNumber',
                            url: rootPath+'/good/goodSelect.shtml',
                            mode: 'remote',
                            pagination:true,
                            columns: [[
                                {field:'id',hidden:true,title:'编码',width:150,align:'center'},
                                {field:'mapNumber',title:'图号',width:150,align:'center'},
                                {field:'name',title:'产品名称',width:80,align:'center'},
                                {field:'materialQuality',hidden:true,title:'材质',width:150,align:'center'},
                                {field:'goodSize',title:'产品尺寸',width:80,align:'center'},
                                {field:'blankSize',title:'下料尺寸',width:100,align:'center'},
                                {field:'goodWeight',hidden:true,title:'产品重量',width:150,align:'center'},
                            ]],
                            fitColumns: true,
                            onSelect:selectGood
                        }
                    }},
                    {field:'goodName',title:'产品名称',width:80,align:'center',editor:'text'},
                    {field:'goodSize',sortable:true,title:'产品尺寸',width:80,align:'center',editor:'text'},
                    {field:'amount',title:'数量',width:50,align:'center',editor:'text'},
                    {field:'goodWeight',title:'单重',width:70,align:'center',editor:'text'},
                    {field:'weight',title:'总重量',width:80,align:'center',editor:'text'},
                    {field:'materialQualityName',title:'材质',width:80,align:'center',editor:{type:'combogrid',
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
                    {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center',editor:'datebox'},
                    {field:'remarks1',title:'备注1',width:100,align:'center',editor:'text'},
                    {field:'distributionAmount',title:'待分配数量',width:60,align:'center',editor:'text'},
                    {field:'userShow',title:'完成人',width:80,align:'center'},
                    {field:'opreateUserShow',title:'接收员工',width:80,align:'center'},
                    {field:'opreateProcessShow',title:'接收工序',width:60,align:'center'},
                    {field:'oprateState',title:'状态',width:70,align:'center'},
                    {field:'progress',title:'进度',width:1000,align:'left'},
                    {field:'oprateProcessId',hidden:true,title:'oprateProcessId',width:300,align:'center',editor:'text'},
                    {field:'oprateUserId',hidden:true,title:'oprateUserId',width:300,align:'center',editor:'text'},
                    {field:'origin',hidden:true,title:'来源',width:300,align:'center',editor:'text'},
                    {field:'goodId',hidden:true,title:'产品id',width:300,align:'center',editor:'text'},
                    {field:'materialQuality',hidden:true,title:'材质id',width:300,align:'center',editor:'text'},
                    {field:'hardnessId',hidden:true,title:'硬度id',width:300,align:'center',editor:'text'},
                    {field:'clientId',hidden:true,title:'clientId',width:300,align:'center',editor:'text'},
                    {field:'count',hidden:true,title:'count',width:300,align:'center',editor:'text'},
                ]];
        }
        else if((origin=='渗碳'&&remarks=='外圆粗磨')|| (origin=='渗碳'&&remarks=='平磨（垫片）')||(origin=='渗碳'&&remarks=='外圆磨（轴）')){
            columns=
                [[
                    {field:'id',hidden:true,title:'编号',width:300,align:'center',editor:'text'},
                    {field:'backTime',sortable:true,title:'完成日期',width:80,align:'center',editor:'datebox'},
                    {field:'clientName',title:'客户',width:200,align:'center',editor:{type:'combogrid',
                        options: {panelWidth:200,
                            idField: 'fullName',
                            textField: 'fullName',
                            url: rootPath+'/client/clientSelect.shtml',
                            mode: 'remote',
                            columns: [[
                                {field:'id',hidden:true,title:'客户编码',width:100},
                                {field:'fullName',title:'客户全称',width:350},
                            ]],
                            fitColumns: true,
                            onSelect:selectClient
                        }
                    }},
                    {field:'contractNumber',title:'合同号',width:100,align:'center',editor:'text'},
                    {field:'mapNumber',title:'图号',width:100,align:'center',editor:{type:'combogrid',
                        options: {panelWidth:400,
                            idField: 'mapNumber',
                            textField: 'mapNumber',
                            url: rootPath+'/good/goodSelect.shtml',
                            mode: 'remote',
                            pagination:true,
                            columns: [[
                                {field:'id',hidden:true,title:'编码',width:150,align:'center'},
                                {field:'mapNumber',title:'图号',width:150,align:'center'},
                                {field:'name',title:'产品名称',width:80,align:'center'},
                                {field:'materialQuality',hidden:true,title:'材质',width:150,align:'center'},
                                {field:'goodSize',title:'产品尺寸',width:80,align:'center'},
                                {field:'blankSize',title:'下料尺寸',width:100,align:'center'},
                                {field:'goodWeight',hidden:true,title:'产品重量',width:150,align:'center'},
                            ]],
                            fitColumns: true,
                            onSelect:selectGood
                        }
                    }},
                    {field:'goodName',title:'产品名称',width:80,align:'center',editor:'text'},
                    {field:'goodSize',sortable:true,title:'产品尺寸',width:80,align:'center',editor:'text'},
                    {field:'backAmount',title:'数量',width:50,align:'center',editor:'text'},
                    {field:'goodWeight',title:'单重',width:70,align:'center',editor:'text'},
                    {field:'weight',title:'总重量',width:80,align:'center',editor:'text'},
                    {field:'materialQualityName',title:'材质',width:80,align:'center',editor:{type:'combogrid',
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
                    {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center',editor:'datebox'},
                    {field:'remarks1',title:'备注1',width:100,align:'center',editor:'text'},
                    {field:'distributionAmount',title:'待分配数量',width:60,align:'center',editor:'text'},
                    {field:'opreateUserShow',title:'接收员工',width:80,align:'center'},
                    {field:'opreateProcessShow',title:'接收工序',width:60,align:'center'},
                    {field:'oprateState',title:'状态',width:70,align:'center'},
                    {field:'progress',title:'进度',width:1000,align:'left'},
                    {field:'oprateProcessId',hidden:true,title:'oprateProcessId',width:300,align:'center',editor:'text'},
                    {field:'oprateUserId',hidden:true,title:'oprateUserId',width:300,align:'center',editor:'text'},
                    {field:'origin',hidden:true,title:'来源',width:300,align:'center',editor:'text'},
                    {field:'goodId',hidden:true,title:'产品id',width:300,align:'center',editor:'text'},
                    {field:'materialQuality',hidden:true,title:'材质id',width:300,align:'center',editor:'text'},
                    {field:'hardnessId',hidden:true,title:'硬度id',width:300,align:'center',editor:'text'},
                    {field:'clientId',hidden:true,title:'clientId',width:300,align:'center',editor:'text'},
                    {field:'count',hidden:true,title:'count',width:300,align:'center',editor:'text'},
                ]];
        }else if(origin!='正火'&&origin!='车后'&&origin!='火前后'&&origin!='铣槽后'&&origin!='钳后情况'){
            if(origin=='调质'){
                columns=
                    [[
                        {field:'id',hidden:true,title:'编号',width:300,align:'center',editor:'text'},
                        {field:'pickTime',sortable:true,title:'领料日期',width:80,align:'center',editor:'datebox'},
                        {field:'clientName',title:'客户',width:200,align:'center',editor:{type:'combogrid',
                            options: {panelWidth:200,
                                idField: 'fullName',
                                textField: 'fullName',
                                url: rootPath+'/client/clientSelect.shtml',
                                mode: 'remote',
                                columns: [[
                                    {field:'id',hidden:true,title:'客户编码',width:100},
                                    {field:'fullName',title:'客户全称',width:350},
                                ]],
                                fitColumns: true,
                                onSelect:selectClient
                            }
                        }},
                        {field:'contractNumber',title:'合同号',width:100,align:'center',editor:'text'},
                        {field:'mapNumber',title:'图号',width:100,align:'center',editor:{type:'combogrid',
                            options: {panelWidth:400,
                                idField: 'mapNumber',
                                textField: 'mapNumber',
                                url: rootPath+'/good/goodSelect.shtml',
                                mode: 'remote',
                                pagination:true,
                                columns: [[
                                    {field:'id',hidden:true,title:'编码',width:150,align:'center'},
                                    {field:'mapNumber',title:'图号',width:150,align:'center'},
                                    {field:'name',title:'产品名称',width:80,align:'center'},
                                    {field:'materialQuality',hidden:true,title:'材质',width:150,align:'center'},
                                    {field:'goodSize',title:'产品尺寸',width:80,align:'center'},
                                    {field:'blankSize',title:'下料尺寸',width:100,align:'center'},
                                    {field:'goodWeight',hidden:true,title:'产品重量',width:150,align:'center'},
                                ]],
                                fitColumns: true,
                                onSelect:selectGood
                            }
                        }},
                        {field:'goodName',title:'产品名称',width:80,align:'center',editor:'text'},
                        {field:'goodSize',sortable:true,title:'产品尺寸',width:80,align:'center',editor:'text'},
                        {field:'amount',title:'数量',width:50,align:'center',editor:'text'},
                        {field:'goodWeight',title:'单重',width:70,align:'center',editor:'text'},
                        {field:'weight',title:'总重量',width:80,align:'center',editor:'text'},
                        {field:'materialQualityName',title:'材质',width:80,align:'center',editor:{type:'combogrid',
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
                        {field:'hardnessName',title:'硬度',width:80,align:'center',editor:{type:'combogrid',
                            options: {panelWidth: 400,
                                idField: 'name',
                                textField: 'name',
                                url: rootPath +'/hardness/hardnessSelect.shtml',
                                mode: 'remote',
                                columns: [[
                                    {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                                    {field:'name',title:'名称',width:100,align:'center'},
                                ]],
                                fitColumns: true,
                                onSelect:selectHardNess
                            }
                        }},
                        {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center',editor:'datebox'},
                        {field:'remarks1',title:'备注1',width:100,align:'center',editor:'text'},
                        {field:'backTime',sortable:true,title:'回料日期',width:80,align:'center',editor:'datebox'},
                        {field:'backAmount',title:'回料数量',width:50,align:'center',editor:'text'},
                        {field:'distributionAmount',title:'待分配数量',width:60,align:'center',editor:'text'},
                        {field:'remarks2',title:'备注2',width:80,align:'center',editor:'text'},
                        {field:'isPrint',title:'是否打印',width:50,align:'center'},
                        {field:'isMakeMidfrequency',title:'是否生成中频',width:80,align:'center'},
                        {field:'opreateUserShow',title:'接收员工',width:80,align:'center'},
                        {field:'opreateProcessShow',title:'接收工序',width:60,align:'center'},
                        {field:'oprateState',title:'状态',width:70,align:'center'},
                        {field:'progress',title:'进度',width:1000,align:'left'},
                        {field:'oprateProcessId',hidden:true,title:'oprateProcessId',width:300,align:'center',editor:'text'},
                        {field:'oprateUserId',hidden:true,title:'oprateUserId',width:300,align:'center',editor:'text'},
                        {field:'origin',hidden:true,title:'来源',width:300,align:'center',editor:'text'},
                        {field:'goodId',hidden:true,title:'产品id',width:300,align:'center',editor:'text'},
                        {field:'materialQuality',hidden:true,title:'材质id',width:300,align:'center',editor:'text'},
                        {field:'hardnessId',hidden:true,title:'硬度id',width:300,align:'center',editor:'text'},
                        {field:'clientId',hidden:true,title:'clientId',width:300,align:'center',editor:'text'},
                        {field:'count',hidden:true,title:'count',width:300,align:'center',editor:'text'},
                    ]];
            }else{
               if(origin=='渗碳'&&remarks!='外圆粗磨'){
                   columns=
                       [[
                           {field:'id',hidden:true,title:'编号',width:300,align:'center',editor:'text'},
                           {field:'pickTime',sortable:true,title:'领料日期',width:80,align:'center',editor:'datebox'},
                           {field:'clientName',title:'客户',width:200,align:'center',editor:{type:'combogrid',
                               options: {panelWidth:200,
                                   idField: 'fullName',
                                   textField: 'fullName',
                                   url: rootPath+'/client/clientSelect.shtml',
                                   mode: 'remote',
                                   columns: [[
                                       {field:'id',hidden:true,title:'客户编码',width:100},
                                       {field:'fullName',title:'客户全称',width:350},
                                   ]],
                                   fitColumns: true,
                                   onSelect:selectClient
                               }
                           }},
                           {field:'contractNumber',title:'合同号',width:100,align:'center',editor:'text'},
                           {field:'mapNumber',title:'图号',width:100,align:'center',editor:{type:'combogrid',
                               options: {panelWidth:400,
                                   idField: 'mapNumber',
                                   textField: 'mapNumber',
                                   url: rootPath+'/good/goodSelect.shtml',
                                   mode: 'remote',
                                   pagination:true,
                                   columns: [[
                                       {field:'id',hidden:true,title:'编码',width:150,align:'center'},
                                       {field:'mapNumber',title:'图号',width:150,align:'center'},
                                       {field:'name',title:'产品名称',width:80,align:'center'},
                                       {field:'materialQuality',hidden:true,title:'材质',width:150,align:'center'},
                                       {field:'goodSize',title:'产品尺寸',width:80,align:'center'},
                                       {field:'blankSize',title:'下料尺寸',width:100,align:'center'},
                                       {field:'goodWeight',hidden:true,title:'产品重量',width:150,align:'center'},
                                   ]],
                                   fitColumns: true,
                                   onSelect:selectGood
                               }
                           }},
                           {field:'goodName',title:'产品名称',width:80,align:'center',editor:'text'},
                           {field:'goodSize',sortable:true,title:'产品尺寸',width:80,align:'center',editor:'text'},
                           {field:'amount',title:'数量',width:50,align:'center',editor:'text'},
                           {field:'goodWeight',title:'单重',width:70,align:'center',editor:'text'},
                           {field:'weight',title:'总重量',width:80,align:'center',editor:'text'},
                           {field:'materialQualityName',title:'材质',width:80,align:'center',editor:{type:'combogrid',
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
                           {field:'hardnessName',title:'硬度',width:80,align:'center',editor:{type:'combogrid',
                               options: {panelWidth: 400,
                                   idField: 'name',
                                   textField: 'name',
                                   url: rootPath +'/hardness/hardnessSelect.shtml',
                                   mode: 'remote',
                                   columns: [[
                                       {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                                       {field:'name',title:'名称',width:100,align:'center'},
                                   ]],
                                   fitColumns: true,
                                   onSelect:selectHardNess
                               }
                           }},
                           {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center',editor:'datebox'},
                           {field:'remarks1',title:'备注1',width:100,align:'center',editor:'text'},
                           {field:'backTime',sortable:true,title:'回料日期',width:80,align:'center',editor:'datebox'},
                           {field:'backAmount',title:'回料数量',width:50,align:'center',editor:'text'},
                           {field:'distributionAmount',title:'待分配数量',width:60,align:'center',editor:'text'},
                           {field:'isJump',hidden:true,title:'是否跳转',width:50,align:'center',editor:'text'},
                           {field:'remarks2',title:'备注2',width:80,align:'center',editor:'text'},
                           {field:'isPrint',title:'是否打印',width:50,align:'center'},
                           {field:'opreateUserShow',title:'接收员工',width:80,align:'center'},
                           {field:'opreateProcessShow',title:'接收工序',width:60,align:'center'},
                           {field:'oprateState',title:'状态',width:70,align:'center'},
                           {field:'makeUser',title:'生成人',width:50,align:'center'},
                           {field:'progress',title:'进度',width:1000,align:'left'},
                           {field:'oprateProcessId',hidden:true,title:'oprateProcessId',width:300,align:'center',editor:'text'},
                           {field:'oprateUserId',hidden:true,title:'oprateUserId',width:300,align:'center',editor:'text'},
                           {field:'origin',hidden:true,title:'来源',width:300,align:'center',editor:'text'},
                           {field:'goodId',hidden:true,title:'产品id',width:300,align:'center',editor:'text'},
                           {field:'materialQuality',hidden:true,title:'材质id',width:300,align:'center',editor:'text'},
                           {field:'hardnessId',hidden:true,title:'硬度id',width:300,align:'center',editor:'text'},
                           {field:'clientId',hidden:true,title:'clientId',width:300,align:'center',editor:'text'},
                           {field:'count',hidden:true,title:'count',width:300,align:'center',editor:'text'},
                       ]];
               }else{
                   columns=
                       [[
                           {field:'id',hidden:true,title:'编号',width:300,align:'center',editor:'text'},
                           {field:'pickTime',sortable:true,title:'领料日期',width:80,align:'center',editor:'datebox'},
                           {field:'clientName',title:'客户',width:200,align:'center',editor:{type:'combogrid',
                               options: {panelWidth:200,
                                   idField: 'fullName',
                                   textField: 'fullName',
                                   url: rootPath+'/client/clientSelect.shtml',
                                   mode: 'remote',
                                   columns: [[
                                       {field:'id',hidden:true,title:'客户编码',width:100},
                                       {field:'fullName',title:'客户全称',width:350},
                                   ]],
                                   fitColumns: true,
                                   onSelect:selectClient
                               }
                           }},
                           {field:'contractNumber',title:'合同号',width:100,align:'center',editor:'text'},
                           {field:'mapNumber',title:'图号',width:100,align:'center',editor:{type:'combogrid',
                               options: {panelWidth:400,
                                   idField: 'mapNumber',
                                   textField: 'mapNumber',
                                   url: rootPath+'/good/goodSelect.shtml',
                                   mode: 'remote',
                                   pagination:true,
                                   columns: [[
                                       {field:'id',hidden:true,title:'编码',width:150,align:'center'},
                                       {field:'mapNumber',title:'图号',width:150,align:'center'},
                                       {field:'name',title:'产品名称',width:80,align:'center'},
                                       {field:'materialQuality',hidden:true,title:'材质',width:150,align:'center'},
                                       {field:'goodSize',title:'产品尺寸',width:80,align:'center'},
                                       {field:'blankSize',title:'下料尺寸',width:100,align:'center'},
                                       {field:'goodWeight',hidden:true,title:'产品重量',width:150,align:'center'},
                                   ]],
                                   fitColumns: true,
                                   onSelect:selectGood
                               }
                           }},
                           {field:'goodName',title:'产品名称',width:80,align:'center',editor:'text'},
                           {field:'goodSize',sortable:true,title:'产品尺寸',width:80,align:'center',editor:'text'},
                           {field:'amount',title:'数量',width:50,align:'center',editor:'text'},
                           {field:'goodWeight',title:'单重',width:70,align:'center',editor:'text'},
                           {field:'weight',title:'总重量',width:80,align:'center',editor:'text'},
                           {field:'materialQualityName',title:'材质',width:80,align:'center',editor:{type:'combogrid',
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
                           {field:'hardnessName',title:'硬度',width:80,align:'center',editor:{type:'combogrid',
                               options: {panelWidth: 400,
                                   idField: 'name',
                                   textField: 'name',
                                   url: rootPath +'/hardness/hardnessSelect.shtml',
                                   mode: 'remote',
                                   columns: [[
                                       {field:'id',hidden:true,title:'编码',width:100,align:'center'},
                                       {field:'name',title:'名称',width:100,align:'center'},
                                   ]],
                                   fitColumns: true,
                                   onSelect:selectHardNess
                               }
                           }},
                           {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center',editor:'datebox'},
                           {field:'remarks1',title:'备注1',width:100,align:'center',editor:'text'},
                           {field:'backTime',sortable:true,title:'回料日期',width:80,align:'center',editor:'datebox'},
                           {field:'backAmount',title:'回料数量',width:50,align:'center',editor:'text'},
                           {field:'distributionAmount',title:'待分配数量',width:60,align:'center',editor:'text'},
                           {field:'remarks2',title:'备注2',width:80,align:'center',editor:'text'},
                           {field:'isPrint',title:'是否打印',width:50,align:'center'},
                           {field:'opreateUserShow',title:'接收员工',width:80,align:'center'},
                           {field:'opreateProcessShow',title:'接收工序',width:60,align:'center'},
                           {field:'oprateState',title:'状态',width:70,align:'center'},
                           {field:'progress',title:'进度',width:1000,align:'left'},
                           {field:'oprateProcessId',hidden:true,title:'oprateProcessId',width:300,align:'center',editor:'text'},
                           {field:'oprateUserId',hidden:true,title:'oprateUserId',width:300,align:'center',editor:'text'},
                           {field:'origin',hidden:true,title:'来源',width:300,align:'center',editor:'text'},
                           {field:'goodId',hidden:true,title:'产品id',width:300,align:'center',editor:'text'},
                           {field:'materialQuality',hidden:true,title:'材质id',width:300,align:'center',editor:'text'},
                           {field:'hardnessId',hidden:true,title:'硬度id',width:300,align:'center',editor:'text'},
                           {field:'clientId',hidden:true,title:'clientId',width:300,align:'center',editor:'text'},
                           {field:'count',hidden:true,title:'count',width:300,align:'center',editor:'text'},
                       ]];
               }
            }

        }else{
            if(origin=='正火'){
                columns=
                    [[
                        {field:'id',hidden:true,title:'编号',width:300,align:'center',editor:'text'},
                        {field:'pickTime',sortable:true,title:'领料日期',width:80,align:'center',editor:'datebox'},
                        {field:'clientName',title:'客户',width:200,align:'center',editor:{type:'combogrid',
                            options: {panelWidth:200,
                                idField: 'fullName',
                                textField: 'fullName',
                                url: rootPath+'/client/clientSelect.shtml',
                                mode: 'remote',
                                columns: [[
                                    {field:'id',hidden:true,title:'客户编码',width:100},
                                    {field:'fullName',title:'客户全称',width:350},
                                ]],
                                fitColumns: true,
                                onSelect:selectClient
                            }
                        }},
                        {field:'contractNumber',title:'合同号',width:100,align:'center',editor:'text'},
                        {field:'mapNumber',title:'图号',width:100,align:'center',editor:{type:'combogrid',
                            options: {panelWidth:400,
                                idField: 'mapNumber',
                                textField: 'mapNumber',
                                url: rootPath+'/good/goodSelect.shtml',
                                mode: 'remote',
                                pagination:true,
                                columns: [[
                                    {field:'id',hidden:true,title:'编码',width:150,align:'center'},
                                    {field:'mapNumber',title:'图号',width:150,align:'center'},
                                    {field:'name',title:'产品名称',width:150,align:'center'},
                                    {field:'materialQuality',hidden:true,title:'材质',width:150,align:'center'},
                                    {field:'goodSize',title:'产品尺寸',width:80,align:'center'},
                                    {field:'blankSize',title:'下料尺寸',width:100,align:'center'},
                                    {field:'goodWeight',hidden:true,title:'产品重量',width:150,align:'center'},
                                ]],
                                fitColumns: true,
                                onSelect:selectGood
                            }
                        }},
                        {field:'goodName',title:'产品名称',width:80,align:'center',editor:'text'},
                        {field:'goodSize',sortable:true,title:'产品尺寸',width:80,align:'center',editor:'text'},
                        {field:'amount',title:'数量',width:50,align:'center',editor:'text'},
                        {field:'goodWeight',title:'单重',width:100,align:'center',editor:'text'},
                        {field:'weight',title:'总重量',width:80,align:'center',editor:'text'},
                        {field:'materialQualityName',title:'材质',width:80,align:'center',editor:{type:'combogrid',
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
                        {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center',editor:'datebox'},
                        {field:'remarks1',title:'备注1',width:100,align:'center',editor:'text'},
                        {field:'distributionAmount',title:'待分配数量',width:60,align:'center',editor:'text'},
                        {field:'opreateUserShow',title:'接收员工',width:80,align:'center'},
                        {field:'opreateProcessShow',title:'接收工序',width:60,align:'center'},
                        {field:'oprateState',title:'状态',width:70,align:'center'},
                        {field:'progress',title:'进度',width:1000,align:'left'},
                        {field:'oprateProcessId',hidden:true,title:'oprateProcessId',width:300,align:'center',editor:'text'},
                        {field:'oprateUserId',hidden:true,title:'oprateUserId',width:300,align:'center',editor:'text'},
                        {field:'origin',hidden:true,title:'来源',width:300,align:'center',editor:'text'},
                        {field:'goodId',hidden:true,title:'产品id',width:300,align:'center',editor:'text'},
                        {field:'materialQuality',hidden:true,title:'材质id',width:300,align:'center',editor:'text'},
                        {field:'hardnessId',hidden:true,title:'硬度id',width:300,align:'center',editor:'text'},
                        {field:'clientId',hidden:true,title:'clientId',width:300,align:'center',editor:'text'},
                        {field:'count',hidden:true,title:'count',width:300,align:'center',editor:'text'},
                    ]];
            }else{
                columns=
                    [[
                        {field:'id',hidden:true,title:'编号',width:300,align:'center',editor:'text'},
                        {field:'pickTime',sortable:true,title:'完成日期',width:80,align:'center',editor:'datebox'},
                        {field:'clientName',title:'客户',width:200,align:'center',editor:{type:'combogrid',
                            options: {panelWidth:200,
                                idField: 'fullName',
                                textField: 'fullName',
                                url: rootPath+'/client/clientSelect.shtml',
                                mode: 'remote',
                                columns: [[
                                    {field:'id',hidden:true,title:'客户编码',width:100},
                                    {field:'fullName',title:'客户全称',width:350},
                                ]],
                                fitColumns: true,
                                onSelect:selectClient
                            }
                        }},
                        {field:'contractNumber',title:'合同号',width:100,align:'center',editor:'text'},
                        {field:'mapNumber',title:'图号',width:100,align:'center',editor:{type:'combogrid',
                            options: {panelWidth:400,
                                idField: 'mapNumber',
                                textField: 'mapNumber',
                                url: rootPath+'/good/goodSelect.shtml',
                                mode: 'remote',
                                pagination:true,
                                columns: [[
                                    {field:'id',hidden:true,title:'编码',width:150,align:'center'},
                                    {field:'mapNumber',title:'图号',width:150,align:'center'},
                                    {field:'name',title:'产品名称',width:150,align:'center'},
                                    {field:'materialQuality',hidden:true,title:'材质',width:150,align:'center'},
                                    {field:'goodSize',title:'产品尺寸',width:80,align:'center'},
                                    {field:'blankSize',title:'下料尺寸',width:100,align:'center'},
                                    {field:'goodWeight',hidden:true,title:'产品重量',width:150,align:'center'},
                                ]],
                                fitColumns: true,
                                onSelect:selectGood
                            }
                        }},
                        {field:'goodName',title:'产品名称',width:80,align:'center',editor:'text'},
                        {field:'goodSize',sortable:true,title:'产品尺寸',width:80,align:'center',editor:'text'},
                        {field:'amount',title:'数量',width:50,align:'center',editor:'text'},
                        {field:'goodWeight',title:'单重',width:100,align:'center',editor:'text'},
                        {field:'weight',title:'总重量',width:80,align:'center',editor:'text'},
                        {field:'materialQualityName',title:'材质',width:80,align:'center',editor:{type:'combogrid',
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
                        {field:'deliveryTime',sortable:true,title:'交货日期',width:80,align:'center',editor:'datebox'},
                        {field:'remarks1',title:'备注1',width:100,align:'center',editor:'text'},
                        {field:'distributionAmount',title:'待分配数量',width:60,align:'center',editor:'text'},
                        {field:'userShow',title:'完成人',width:80,align:'center'},
                        {field:'isMakeCarburization',title:'是否生成渗碳',width:80,align:'center'},
                        {field:'opreateUserShow',title:'接收员工',width:80,align:'center'},
                        {field:'opreateProcessShow',title:'接收工序',width:60,align:'center'},
                        {field:'oprateState',title:'状态',width:70,align:'center'},
                        {field:'progress',title:'进度',width:1000,align:'left'},
                        {field:'oprateProcessId',hidden:true,title:'oprateProcessId',width:300,align:'center',editor:'text'},
                        {field:'oprateUserId',hidden:true,title:'oprateUserId',width:300,align:'center',editor:'text'},
                        {field:'origin',hidden:true,title:'来源',width:300,align:'center',editor:'text'},
                        {field:'goodId',hidden:true,title:'产品id',width:300,align:'center',editor:'text'},
                        {field:'materialQuality',hidden:true,title:'材质id',width:300,align:'center',editor:'text'},
                        {field:'hardnessId',hidden:true,title:'硬度id',width:300,align:'center',editor:'text'},
                        {field:'clientId',hidden:true,title:'clientId',width:300,align:'center',editor:'text'},
                        {field:'count',hidden:true,title:'count',width:300,align:'center',editor:'text'},
                    ]];
            }

        }
        initAllDatagrid(roleId);

    }



    $('#ttselect').datagrid({
        title:'数据展示',
        iconCls:'icon-ok',
        idField:'id',
        nowrap:false,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        columns:[[
            {field:'pickTime',title:'领料日期',width:300,align:'center',editor:'datebox'},
            {field:'contractNumber',title:'合同号',width:300,align:'center',editor:'text'},
            {field:'mapNumber',title:'图号',width:300,align:'center',editor:'text'},
            {field:'goodName',title:'产品名称',width:300,align:'center'},
            {field:'goodSize',title:'产品尺寸',width:300,align:'center',editor:'text'},
            {field:'amount',title:'数量',width:150,align:'center',editor:'text'},
            {field:'goodWeight',title:'单重',width:150,align:'center',editor:'text'},
            {field:'weight',title:'总重量',width:150,align:'center',editor:'text'},
            {field:'materialQualityName',title:'材质',width:300,align:'center'},
            {field:'hardnessName',title:'硬度',width:300,align:'center'},
            {field:'deliveryTime',title:'交货日期',width:300,align:'center',editor:'datebox'},
            {field:'remarks1',title:'备注1',width:200,align:'center',editor:'text'},
            {field:'backTime',title:'回料日期',width:300,align:'center',editor:'datebox'},
            {field:'backAmount',title:'回料数量',width:50,align:'center',editor:'text'},
            {field:'remarks2',title:'备注2',width:300,align:'center',editor:'text'},
        ]],
    });

    initComboGridEditor();
});

function initSmallDatagird() {
    $('#tt').datagrid({
        title:'数据展示',
        iconCls:'icon-ok',
        fitColumns:true,

        idField:'id',
        nowrap:false,
        pagination:true,
        url:rootPath + '/heattreat/findByPage.shtml',
        //是否显示行号
        rownumbers:true,
        fit:true,
        onDblClickRow:function (rowIndex, rowData) {
            if(editId!=null){
                endEditDatagridByIndex('tt',editId)
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }else{
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }
            setEditorOnChange("tt",1,function onChangeBack(index,content) {
                setContentToDatagridEditorText("tt",editId,'distributionAmount',content);
            });
        },
        onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
            e.preventDefault(); //阻止浏览器捕获右键事件
            heattreatEntity=rowData;
            var origin=getValueById("origin");
            var remarks=getValueById("remarks");
            if((origin=='渗碳'||origin=='成品库')&&remarks==''){
                printShow('打印废品单');
            }

        },
        rowStyler: function(index,row){
            var amount=row.amount;
            var backAmount=row.backAmount;
            if(VarToFloat(backAmount)>=VarToFloat(amount)){
                return 'color:#048133;font-weight:bold';//绿色加粗字体
            }
        },
        columns:columns,
        onBeforeLoad: function (params) {
            params.pageNumber = params.page;
            params.sortName = params.sort;
            var origin=getValueById('origin');
            params.origin=origin;
            params.content=getValueById('content');
            params.pickTimeStart=getDataboxValue('pickTimeStart');
            params.pickTimeEnd=getDataboxValue('pickTimeEnd');
            params.backTimeStart=getDataboxValue('backTimeStart');
            params.backTimeEnd=getDataboxValue('backTimeEnd');
            params.goodName=getContentBySelect("goodName");
            params.clientId=getContentBySelect("clientId");
            params.materialQuality=getContentBySelect("materialQuality");
            var userId="";
            var isMakeCarburization="";
            if(origin=='车后'
                ||(origin=='渗碳'&&remarks=='外圆粗磨')
                ||origin=='端面平磨'
                ||origin=='消差磨'
                ||origin=='统一尺寸'
                ||origin=='内孔磨'
                ||origin=='外圆精磨'){
                userId=getContentBySelect("userId");
                isMakeCarburization=getContentBySelect("isMakeCarburization");
            }
            params.userId=userId;
            params.isMakeCarburization=isMakeCarburization;
            params.oprateUserId=getContentBySelect("oprateUserId");
            params.getstate=getContentBySelect("getstate");
            params.sendstate=getContentBySelect("sendstate");
            params.state=getContentBySelect("state");
            params.isDistribution=getContentBySelect("isDistribution");
            params.oprateProcessId=getContentBySelect("oprateProcessId");
            params.startTime=getDataboxValue("starttime");
            params.endTime=getDataboxValue("endtime");
        },
        onLoadSuccess:function(data){
            setContentToDivSpanById("all","数量合计:"+data.sum.sumAmount+"  总重量合计:"+data.sum.sumWeight+"  回料合计:"+data.sum.sumBackAmount+"  未回料合计:"+data.sum.sumUnBackAmount+"  分配数合计:"+data.sum.sumAlDistributionAmount+"  未分配数合计:"+data.sum.sumUnDistributionAmount);
        },
    });
}

function initAllDatagrid(roleId) {
    $('#tt').datagrid({
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        nowrap:false,
        pagination:true,
        url:rootPath + '/heattreat/findByPage.shtml',
        //是否显示行号
        rownumbers:true,
        fit:true,
        onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
            e.preventDefault(); //阻止浏览器捕获右键事件
            heattreatEntity=rowData;
            var origin=getValueById("origin");
            var remarks=getValueById("remarks");
            if(roleId=='13'&&(origin=='渗碳'||origin=='成品库')&&remarks==''){
                showMenu("rightMenu",e.pageX,e.pageY);
            }else{
                showProgress();
            }

        },
        onCheck:function (rowIndex,rowData) {
            distributionAmountSum=distributionAmountSum+VarToInt(rowData.distributionAmount);
            setContentToDivSpanById("distributionAmountSum",distributionAmountSum);
        },
        onUncheck:function (rowIndex,rowData) {
            if(distributionAmountSum>0){
                distributionAmountSum=distributionAmountSum-VarToInt(rowData.distributionAmount);
                setContentToDivSpanById("distributionAmountSum",distributionAmountSum);
            }else{
                setContentToDivSpanById("distributionAmountSum",0);
            }

        },
        onDblClickRow:function (rowIndex, rowData) {
            if(editId!=null){
                endEditDatagridByIndex('tt',editId)
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }else{
                editId=rowIndex;
                beginEditDatagridByIndex('tt',editId);
            }
            editAndAddSetContentAndChange(roleId);
        },
        rowStyler: function(index,row){
            var origin=getValueById('origin');
            if(origin=='成品库'){
                var isSend=row.isSend;
                var backAmount=row.backAmount;
                if(isSend=='是'){
                    return 'color:#048133;font-weight:bold';//绿色加粗字体
                }
            }else{
                var amount=row.amount;
                var backAmount=row.backAmount;
                if(VarToFloat(backAmount)>=VarToFloat(amount)){
                    return 'color:#048133;font-weight:bold';//绿色加粗字体
                }
            }

        },
        columns:columns,
        onBeforeLoad: function (params) {
            distributionAmountSum=0;
            setContentToDivSpanById("distributionAmountSum",0);
            params.pageNumber = params.page;
            params.sortName = params.sort;
            var origin=getValueById('origin');
            params.origin=origin;
            var remarks=getValueById('remarks');
            params.remarks=remarks;
            params.content=getValueById('content');
            params.pickTimeStart=getDataboxValue('pickTimeStart');
            params.pickTimeEnd=getDataboxValue('pickTimeEnd');
            params.backTimeStart=getDataboxValue('backTimeStart');
            params.backTimeEnd=getDataboxValue('backTimeEnd');
            params.goodName=getContentBySelect("goodName");
            params.clientId=getContentBySelect("clientId");
            params.materialQuality=getContentBySelect("materialQuality");
            var userId="";
            var isMakeCarburization="";
            if(origin=='车后'
                ||(origin=='渗碳'&&remarks=='外圆粗磨')
                ||origin=='端面平磨'
                ||origin=='消差磨'
                ||origin=='统一尺寸'
                ||origin=='内孔磨'
                ||origin=='外圆精磨'){
                userId=getContentBySelect("userId");
                isMakeCarburization=getContentBySelect("isMakeCarburization");
            }
            params.userId=userId;
            params.isMakeCarburization=isMakeCarburization;
            params.oprateUserId=getContentBySelect("oprateUserId");
            params.getstate=getContentBySelect("getstate");
            params.sendstate=getContentBySelect("sendstate");
            params.state=getContentBySelect("state");
            params.isDistribution=getContentBySelect("isDistribution");
            params.oprateProcessId=getContentBySelect("oprateProcessId");
            params.startTime=getDataboxValue("starttime");
            params.endTime=getDataboxValue("endtime");
        },
        onLoadSuccess:function(data){
            setContentToDivSpanById("all","数量合计:"+data.sum.sumAmount+"  总重量合计:"+data.sum.sumWeight+"  回料合计:"+data.sum.sumBackAmount+"  未回料合计:"+data.sum.sumUnBackAmount+"  分配数合计:"+data.sum.alDistributionAmount+"  未分配数合计:"+data.sum.unDistributionAmount)
        },
    });
}

function find() {
    reloadDatagridMessage('tt');
    clearDatagridSelections('tt');
    reloadTreeMessage("tt1");
}
function  showTT() {
    submitFormNow('form',rootPath + '/heattreat/findByPage.shtml',function (data) {
        $('#tt').datagrid({data:JSON.parse(data)});
    })
}
function add() {
    var rows = $('#tt').datagrid('getRows');
    var origin=getValueById('origin');
    var hardnessName="";
    var hardnessId="";
    var materialQualityName="";
    var materialQuality="";
    if(origin=='渗碳'){
        materialQuality="19";
        materialQualityName="20CrMnTiH";
        hardnessId="40";
        hardnessName="HRC58-62";
    }
    if(origin=='中频'){
        materialQuality="";
        materialQualityName="";
        hardnessId="41";
        hardnessName="HRC58-60";
    }
    if(origin=='调质'){
        materialQuality="";
        materialQualityName="";
        hardnessId="42";
        hardnessName="HB270-300";
    }
    $('#tt').datagrid('appendRow',
        {
            'origin':origin,
            'materialQuality':materialQuality,
            'materialQualityName':materialQualityName,
            'hardnessId':hardnessId,
            'hardnessName':hardnessName,
        }
    );
}

function  cancel() {
    reloadDatagridMessage('tt');
    clearDatagridSelections('tt');
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
                communateGet(rootPath +'/heattreat/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
                    reloadDatagridMessage('tt');
                });
            }
        });

    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function  save() {
    endEditDatagridByIndex('tt',editId)
    var rows= $('#tt').datagrid('getChanges');
    if(rows.length>0){
        communatePost(rootPath +'/heattreat/editEntity.shtml',ListToJsonString(rows),function back(data){
            reloadDatagridMessage('tt');
        });
    }else{
        $.messager.alert('警告','不存在可以保存数据','warning');
    }
}


function selectGood(rowIndex, rowData) {
    var goodWeight=0;
    if(checkIsSelectEd(editId)){
        var origin=getValueById('origin');
        setContentToDatagridEditorText('tt',editId,"goodId",rowData.id);
        setContentToDatagridEditorText('tt',editId,"mapNumber",rowData.mapNumber);
        setContentToDatagridEditorText('tt',editId,"goodName",rowData.name);
        if(origin=='调质'||origin=='正火'){
            setContentToDatagridEditorText('tt',editId,"goodSize",rowData.blankSize);
            if(isNull(rowData.blankSize)){
                goodWeight=0;
            }else{
                goodWeight=getWeightBySize(rowData.blankSize);
            }
            setContentToDatagridEditorText('tt',editId,"goodWeight",goodWeight);
        }else{
            setContentToDatagridEditorText('tt',editId,"goodSize",rowData.goodSize);
            if(isNull(rowData.goodSize)){
                goodWeight=0;
            }else{
                goodWeight=getWeightBySize(rowData.goodSize);
            }
            setContentToDatagridEditorText('tt',editId,"goodWeight",goodWeight);
        }
        var amount=getContentByEditor('tt',7);
        var weight=Math.floor((VarToFloat(amount)*VarToFloat(goodWeight)) * 100) / 100
        setContentToDatagridEditorText("tt",editId,'weight',weight);
    }
}

function selectClient(rowIndex, rowData) {
    if(checkIsSelectEd(editId)){
        setContentToDatagridEditorText('tt',editId,"clientId",rowData.id);
        setContentToDatagridEditorText('tt',editId,"clientName",rowData.fullName);
    }
}

function selectMaterialQuality(rowIndex, rowData) {
    if(checkIsSelectEd(editId)){
        setContentToDatagridEditorText('tt',editId,"materialQuality",rowData.id);
        setContentToDatagridEditorText('tt',editId,"materialQualityName",rowData.name);
    }
}

function selectHardNess(rowIndex, rowData) {
    if(checkIsSelectEd(editId)){
        setContentToDatagridEditorText('tt',editId,"hardnessId",rowData.id);
        setContentToDatagridEditorText('tt',editId,"hardnessName",rowData.name);
    }
}

function editAndAddSetContentAndChange(roleId) {
    if(roleId==16){
        setEditorOnChange("tt",1,function onChangeBack(index,content) {
            setContentToDatagridEditorText("tt",editId,'distributionAmount',content);

        });
    }else{
        var origin=getValueById('origin');
        if(origin=='成品库'){
            setEditorOnChange("tt",12,function onChangeBack(index,content) {
                var amount=getContentByEditor('tt',7);
                setContentToDatagridEditorText("tt",editId,'stockAmount',VarToFloat(amount)-VarToFloat(content));
            });
        }else{
            setEditorOnChange("tt",7,function onChangeBack(index,content) {
                goodWeight=getContentByEditor("tt",8);
                var weight=Math.floor((VarToFloat(content)*VarToFloat(goodWeight)) * 100) / 100
                setContentToDatagridEditorText("tt",editId,'weight',weight);
                if(origin=='中频'||origin=='渗碳'||origin=='调质'){
                    setContentToDatagridEditorText("tt",editId,'distributionAmount',content);
                }


            });
            setEditorOnChange("tt",6,function onChangeBack(index,content) {
                goodWeight=getWeightBySize(content);
                setContentToDatagridEditorText("tt",editId,'goodWeight',goodWeight);

                var amount=getContentByEditor('tt',7);
                var weight=Math.floor((VarToFloat(amount)*VarToFloat(goodWeight)) * 100) / 100
                setContentToDatagridEditorText("tt",editId,'weight',weight);
            });
            setEditorOnChange("tt",15,function onChangeBack(index,content) {
                setContentToDatagridEditorText("tt",editId,'distributionAmount',content);
            });
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
    var fileName=getValueById('origin')+'情况导出.xls';
    $('#ttselect').datagrid('toExcel',fileName);
}

function  downLoadAll() {
    var data={
        "origin":getValueById('origin'),
        "content":getValueById('content'),
        "pickTimeStart":getDataboxValue('pickTimeStart'),
        "pickTimeEnd":getDataboxValue('pickTimeEnd'),
        "backTimeStart":getDataboxValue('backTimeStart'),
        "backTimeEnd":getDataboxValue('backTimeEnd'),
    }
    downfileByUrl(rootPath +'/heattreat/exportAll.shtml?entity='+ListToJsonString(data));
}

function getWeightBySize(content) {
    var outside=content.split("*")[0].replace("Φ","");
    var inside=content.split("*")[1];
    if(isContain(inside,"Φ")){
        inside=inside.replace("Φ","");
        length=content.split("*")[2];
        length=length.replace(/[^\d.]/g,'')
    }else{
        if(content.split("*").length==2){
            length=inside;
            inside="0";
        }else{
            length=content.split("*")[2];
        }

    }

    outside=VarToFloat(outside);
    inside=VarToFloat(inside);
    length=VarToFloat(length);
    goodWeight=outside*outside*length*(0.00617/1000)-inside*inside*length*(0.00617/1000);
    goodWeight=floatToVar4(goodWeight);
    return goodWeight;
}

function print() {
    var rows=getDatagridSelections('tt');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    $("#printOrder").window({
        width:1000,
        title:'打印',
        modal: true,
        top:10,
        href: rootPath + '/heattreat/printUI.shtml?ids='+ids+"&origin="+getValueById('origin'),
        onClose:function () {
        }
    });
}

function printDistributionEntity() {
    var rows=getDatagridSelections('tt');
    var ids="";
    for(i=0;i<rows.length;i++){
        ids=ids+rows[i].id+",";
    }
    $("#printDistribution").window({
        width:1000,
        title:'打印',
        modal: true,
        top:10,
        href: rootPath + '/heattreat/printDistributionUI.shtml?ids='+ids+"&origin="+getValueById('origin'),
        onClose:function () {
        }
    });
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
            href: rootPath + '/heattreat/printDrawingUI.shtml?ids='+ids+"&origin=热处理",
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
                    href: rootPath + '/heattreat/printDrawingUI.shtml?ids='+ids+"&origin=热处理",
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
            href: rootPath + '/heattreat/pringTechnologyUI.shtml?ids='+ids+"&origin=热处理",
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
                    href: rootPath + '/heattreat/pringTechnologyUI.shtml?ids='+ids+"&origin=热处理",
                    onClose:function () {
                    }
                });
            }
        })
    }

}