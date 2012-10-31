$('#loginSignup').bind('pagebeforeshow', function () {
	 jQuery.support.cors = true;

	    _.templateSettings = {
	        evaluate: /\{\{(.+?)\}\}/g
	    };

	    Backbone.emulateJSON = true;
    $("#usernamesignup").validate({
        expression: "if (VAL=='') return false; else return true;",
        message: "Please enter name.",
    });

    $("#emailsignup").validate({
        expression: "if (VAL.match(/^[^\\W][a-zA-Z0-9\\_\\-\\.]+([a-zA-Z0-9\\_\\-\\.]+)*\\@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\.[a-zA-Z]{2,4}$/)) return true; else return false;",
        message: "Please enter valid Email id",
    });


    $("#passwordsignup").validate({
        expression: "if (VAL==''||VAL.length<6) return false; else return true;",
        message: "Please enter Valid Password. Minimum size 6.",
    });

    var kpass = $("#passwordsignup").val();

    $("#passwordsignup_confirm").validate({
        expression: "if ((VAL == jQuery('#passwordsignup').val()) && VAL) return true; else return false;",
        message: "Confirm password field doesn't match the password field",
    });

    $('#accept').validate({
        expression: "if ( $('#loginkeeping').is(':checked')) return true; else return false;",
        message: "Please Read & Accept Terms and Condition",
    });


    $('#registration').validated(function () {

        var path = $('#registration').attr('action');
        var redirectPath = $('#authenticate').attr('action');
        var info = $('#registration').serialize();
        $.ajax({
            url: wSURL + path,
            data: info + "&protocol=REST",
            dataType: 'json',
            type: 'POST',
            success: function (data) {
                if (hasErr(data)) {
                    $('.loaderContainer').hide();
					popUpMob.show(data.message);
                } else {

                    $('#username').val($('#emailsignup').val());
                    $('#password').val($('#passwordsignup').val());

                    authenticateUser();


                }
            },
            error: function (e) {
                $('.loaderContainer').hide();
                alert( e);

            }
        });
    });
    /*
		login validations
	*/
    $("#username").validate({
        expression: "if (VAL=='') return false; else return true;",
        message: "Please enter Email address",
    });

    $("#password").validate({
        expression: "if (VAL=='') return false; else return true;",
        message: "Please enter password.",
    });
    
    $('#authenticate').validated(function () {
        $('.loaderContainer').show();
        authenticateUser();
        return false;
    });

    function authenticateUser(path, data) {
        var path = $('#authenticate').attr('action');
        var data = $('#authenticate').serialize();

        $.ajax({
            url: wSURL + path,
            data: data,
            type: 'post',
            dataType: 'json',
            async: false,
            success: function (data) {
                if (hasErr(data)) {
                    popUpMob.show(data.message);
                } else {
                    var rediFlag = getURLParameterValue((($.mobile.path.parseUrl(location.href)).search), 'target');
                    $.mobile.urlHistory.stack = [];
                    if (rediFlag == null || rediFlag == '') {
                        setTimeout(function () {
                            location.href = httpPath+ __hkG.urls.home
                        }, 500);
                    } else if (rediFlag.length > 1) {
                        setTimeout(function () {
                            location.href = httpPath+'/' + rediFlag + '.jsp'
                        }, 500);
                    }
                }
            },

            error: function (e) {
                popUpMob.show(__hkG.errs.requestFail);
            }
        });

    }

    $('#frgPwd').click(function () {
        popUpMob.showWithConfirm('Forgot Password', '<input type=text placeholder="Enter your email" style="border:none;background:#eee;width:90%;margin:0px auto;height:1.7em;;border-radius:0px;-webkit-border-radius:0px" id="frgPwdFld"/>', function () {
            popUpMob.message('', 'remove');
            if ($('#frgPwdFld').val() == '') {
                popUpMob.message(__hkG.msgs.specifyEmail, 'add');
                return;
            }
            $.ajax({
                url: wSURL + 'mForgotPassword/forgotPassword?email=' + $('#frgPwdFld').val(),
                dataType: 'json',
                success: function (response) {
                    if (hasErr(response)) {
                        popUpMob.message(response.message, 'add');
                    } else {
                        popUpMob.message(response.message, 'add');
                        $('#frgPwdFld').hide();
                        $('#popUpMobOk').hide();
                        $('#popUpMobCancel').attr('src', httpPath+'/images/ok.png');
                    }
                }
            })
        }, function () {
            popUpMob.hide();
        })
    })

    $('.gNav').click(function (e) {
        var ele = e.currentTarget;
        var eleId = $(ele).attr('id');
        $('.viewContent').hide();
        $('#' + eleId + 'Content').show();

    });

});