//单独验证某一个input  class="checkpass"
jQuery.validator.addMethod("checkacc", function(value, element) {
	return this.optional(element)
			|| ((value.length <= 30) && (value.length >= 3));
}, "账号由3至30位字符组合构成");
$(function() {
    show=function (json) {
        $('#accountName').val(json);
    };

	$("form").validate({
		submitHandler : function(form) {// 必须写在验证前面，否则无法ajax提交
			ly.ajaxSubmit(form, {// 验证新增是否成功
				type : "post",
				dataType : "json",
				success : function(data) {
					if (data == "success") {
						layer.confirm('添加成功!是否关闭窗口?', function(index) {
							parent.grid.loadData();
							parent.layer.close(parent.pageii);
							return false;
						});
						$("#form")[0].reset();
					} else {
						layer.alert('添加失败！', 3);
					}
				}
			});
		},
		rules : {
			"userFormMap.accountName" : {
				required : true,
                remote : { // 异步验证是否存在
					type : "POST",
					url : 'isExist.shtml',
					data : {
						name : function() {
							return $("#accountName").val();
						}
					}
				}
			}
		},
		messages : {
			"userFormMap.accountName" : {
                required : "请输入账号",
                remote : "请确认所输入账号在账号表里不存在并且存在该员工编号",
                // isExistInEmployee : "所输入账号无相应员工信息，请重新输入或添加员工后继续操作"
			}
		},
		errorPlacement : function(error, element) {// 自定义提示错误位置
			$(".l_err").css('display', 'block');
			// element.css('border','3px solid #FFCCCC');
			$(".l_err").html(error.html());
		},
		success : function(label) {// 验证通过后
			$(".l_err").css('display', 'none');
		}
	});

    $("#selectEmployee").click("click", function() {//查看
        var url ='/user/employeeSelect.shtml';
        pageii = layer.open({
            title : "员工列表",
            type : 2,
            area : [ "99%", "90%" ],
            content : url
        });

    });

});

