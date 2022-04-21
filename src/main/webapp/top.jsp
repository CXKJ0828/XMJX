<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<style>
    a:link, a:visited {
        text-decoration: none;
        color:#a99511;
    }
</style>
<div class="cs-north-bg">
    <div class="cs-north-logo">新明机械企业管理系统<span>
        <%--<div class="cs-north-logo">生产ERP系统<span>--%>
        <%--<div class="cs-north-logo">晨曦科技-生产ERP系统<span>--%>
        <a id="dd" href="javascript:void(0)" class="easyui-tooltip" data-options="
					hideEvent: 'none',
					content: function(){
						return $('#toolbar');
					},
					onShow: function(){
						var t = $(this);
						t.tooltip('tip').focus().unbind().bind('blur',function(){
							t.tooltip('hide');
						});
					}
				"><span style="font-weight:bold;color: black">-${userFormMap.userName}</span></a>
    </span></div>
    <ul class="ui-skin-nav">
        <li class="li-skinitem" title="gray"><span class="gray" rel="gray"></span></li>
        <li class="li-skinitem" title="default"><span class="default" rel="default"></span></li>
        <li class="li-skinitem" title="bootstrap"><span class="bootstrap" rel="bootstrap"></span></li>
        <li class="li-skinitem" title="black"><span class="black" rel="black"></span></li>
        <li class="li-skinitem" title="cupertino"><span class="cupertino" rel="cupertino"></span></li>
        <li class="li-skinitem" title="dark"><span class="dark" rel="dark"></span></li>
        <li class="li-skinitem" title="dark">


        </li>
    </ul>
</div>
<div style="display:none">
    <div id="toolbar">
        <a href="logout.shtml" class="easyui-linkbutton easyui-tooltip"  data-options="iconCls:'icon-no',plain:true">注销</a>
    </div>
</div>
<script>
</script>