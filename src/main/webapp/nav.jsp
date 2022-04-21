<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
    $(function() {
        $('#nav').tree({
            url:rootPath + '/resources/permissionsEntity.shtml',
            method:'post',
            lines:true,
            animate:true,
            loadFilter: function(data){
                return getNavTreeJsonByResources(data);
            },
            onSelect:function(node){
                $(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
                node.state = node.state === 'closed' ? 'open' : 'closed';
            },
            onClick:function (node) {
                var content=node.id.split(":");
                if(node.text=='调度大屏'){
                    window.open(rootPath + '/blank/showBigUI.shtml');
                }if(node.text=='小调度'){
                    window.open(rootPath + '/blank/showSmallUI.shtml');
                }else if(node.text=='小调度2'){
                    window.open(rootPath + '/blank/showSmallUI2.shtml?origin=正火调质');
                }else if(node.text=='小调度渗碳'){
                    window.open(rootPath + '/blank/showSmallUI2.shtml?origin=渗碳');
                }else{
                    var url=content[1];
                    if(content[0]=="yes"){
                        if(node.text!='调度大屏'
                            &&node.text!='小调度'
                            &&node.text!='小调度2'
                            &&node.text!='小调度渗碳'){
                            if(isContain(url,".shtml")){
                                addTab(node.text,url);
                            }

                        }
                    }
                }

            }
        });
    });
</script>
<div class="easyui-accordion" fit="false" border="false">
    <ul id="nav" class="easyui-tree" style="font-size: 50px!important;"></ul>
</div>