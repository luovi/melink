# ###
# * 所有表单项要调用的验证函数，
# * 函数名对应表单项的name
# * 通过validateOne和Backbone内部的validate方法自动调用
# * 如果需要指定方法验证
# * 在model里通过validation方法指定，如:
# * validation:
# *     contact_number: 'reg_contact_number'
# ###
define ['jquery', 'underscore'], ($, _) ->
    'use strict'
    
    RegExps:
        # 手机号
        mobile:/^((((13[0-9])|(15[^4,\D])|(18[0-9])|(17[6-8])|(14[57]))\d{8})|((170[059])\d{7}))$/
        # 固定电话
        telephone : /^((0[1,2]{1}\d{1}-?\d{8})|(0[3-9]{1}\d{2}-?\d{7,8}))$/
        # 用户名
        username : /^[\u0391-\uFFE5\w]+$/
        # 车牌号码
        plate_number : /^[\u4E00-\u9FA5]{1}[a-zA-Z]\w{5}$/
        # 回单夹
        device : /^\d{7}$/
        money : /^[1-9](\d+)?$/
        #zero_num : /^(0|[1-9][0-9]*)$/
        #int_11:
        # 系统限定浮点数，有两种长度，6位和8位，小数固定两位
        float_6 : /^\d{1,4}(\.\d{1,2})?$/
        float_8 : /^\d{1,6}(\.\d{1,2})?$/
        # 身份证15位与18位
        id_card_15 : /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/
        #id_card_18 : /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/
        id_card_18 : /^\d{17}([\dxX])$/
        illegal_char: /[~!#$%\^&*()=+|\\\[\]{};':",?\/<>]/
        email : /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
        int_2: /^([1-9]){1,2}$/
        trailer_number: /^[\u4E00-\u9FA5]{1}[a-zA-Z]\w{4}\u6302?$/
    Messages:
        rangelength: (min,max) ->
            "长度应在#{ min }～#{ max }之间"
        maxlength: (max) ->
            "最大长度 (#{ max })"
        range: (min,max) ->
            "请输入有效数值（#{ min }～#{ max }）"
        required: "这是必填项"
        email: "请输入正确的邮箱"
        device: "回单夹不正确，格式如 0111001"
        #number: "请输入非零整数"
        #digits: "请输入非零小数"
        equalTo : "两次输入不一致"
        plate_number: "车牌格式不正确，正确格式如 浙A00000"
        trailer_number: "挂车车牌格式不正确，正确格式如 浙A0000"
        remote: "服务器验证失败"
        username: "只能包括中英文字母、数字和下划线"
        mobile: "请填写真实有效的手机号码"
        telephone: "请填写手机号码或固定电话，固话格式如 0571-56865686"
        illegal_char: "不能包含~!#&amp;%^&*()=+|\\[]{};&#39:&quot;,?/&lt;&gt;等字符"
        id_card: "证件号码格式不正确"
        float_6 : "请填写有效的数字, 范围1-9999.99"
        float_8 : "请填写有效的数字, 范围1-999999.99"
        money: "请输入正确有效的数值（1～999999)"
        invite_user:"邀请方手机号不能与注册手机号相同"
        int_2: "请填写有效地数字, 范围1-99"

    # 车牌号码
    plate_number: (model, attr, options, errors) ->
        if not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if not @RegExps.plate_number.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.plate_number}
        else if attr in model.needajax
            error_message = @Messages.remote
            data = {'plate_number':model.attributes[attr]}
            if model.id
                _.extend data, car_id:model.id
            $.ajax _.extend
                url: "/api/cars/#{ model._current_user().id }/plate_number/verify"
                data: data
                success: (data, status, xhr) ->
                    if not data.result
                        errors.unshift {name:attr,message: data.message or error_message}
                error: (xhr, status, message) ->
                    errors.unshift {name:attr, message:error_message}
            , model.fetchOptions()
            
    # 挂车车牌号码
    trailer_number: (model, attr, options, errors) ->
        if model.attributes[attr] and not @RegExps.trailer_number.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.trailer_number}

        

    # 随车电话
    locate_number: (model, attr, options, errors) ->
        if not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if not @RegExps.mobile.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.mobile}
        else if attr in model.needajax
            error_message = @Messages.remote
            data = {'locate_number':model.attributes[attr]}
            if model.isCarDriver
                cid = model.get('cid')
                if model.id
                    _.extend data, driver_id:model.id
                url = "/api/cars/#{ model._current_user().id }/#{ cid }/drivers/locate_number/verify"
            else
                if model.id
                    _.extend data, car_id:model.id
                url = "/api/cars/#{ model._current_user().id }/locate_number/verify"
            $.ajax _.extend
                url: url
                data: data
                success: (data, status, xhr) ->
                    if not data.result
                        errors.unshift {name:attr,message: data.message or error_message}
                error: (xhr, status, message) ->
                    errors.unshift {name:attr, message:error_message}
            , model.fetchOptions()
    
    # 注册时的联系电话
    reg_contact_number: (model, attr, options, errors) ->
        if not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if not @RegExps.mobile.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.mobile}
        else
            if attr in model.needajax
                $.ajax
                    url: "/api/users/contact/verify"
                    data: {'contact_number':model.attributes[attr]}
                    success: (data, status, xhr) ->
                        #if not data.result
                        #    errors.unshift {name:attr,message: data.message or error_message}
                        if data.result is 0
                            error_message = "该手机号已存在"
                            errors.unshift {name:attr,message: error_message}
                        else if data.result is -1
                            error_message = "此手机号是黑名单号码！"
                            errors.unshift {name:attr,message: error_message}
                        else if data.result is -2
                            error_message = "不是有效的手机号码，请检查重试"
                            errors.unshift {name:attr,message: error_message}
                        
                    error: (xhr, status, message) ->
                        errors.unshift {name:attr, message:error_message}

    #
    for_contact_number: (model, attr, options, errors) ->
        if not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if not @RegExps.mobile.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.mobile}
        else
            $.ajax
                url: "/api/users/contact/verify"
                data: {'contact_number':model.attributes[attr]}
                success: (data, status, xhr) ->
                    if data.result is 1
                        error_message = "此手机号未被注册，请检查重试"
                        errors.unshift {name:attr,message: error_message}
                    else if data.result is -1
                        error_message = "此手机号是黑名单号码！"
                        errors.unshift {name:attr,message: error_message}
                    else if data.result is -2
                        error_message = "不是有效的手机号码，请检查重试"
                        errors.unshift {name:attr,message: error_message}
                    
                error: (xhr, status, message) ->
                    errors.unshift {name:attr, message:error_message}

    # 联系电话普通验证
    contact_number: (model, attr, options, errors) ->
        model.requires ?= []
        if attr in model.requires and not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if model.attributes[attr] and not @RegExps.mobile.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.mobile}
    # 电话和座机验证
    telephone: (model, attr, options, errors) ->
        model.requires ?= []
        if attr in model.requires and not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if model.attributes[attr] and not (@RegExps.telephone.test(model.attributes[attr]) or  @RegExps.mobile.test(model.attributes[attr]))
            errors.unshift {name:attr,message:@Messages.telephone}

    # 用户名
    username: (model, attr, options={}, errors) ->
        model.requires ?= []
        #options.ajax = if options.ajax then options.ajax else false
        if attr in model.requires and not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if model.attributes[attr]
            if not @RegExps.username.test(model.attributes[attr])
                errors.unshift {name:attr,message:@Messages.username}
            else if model.attributes[attr].length < 2 or model.attributes[attr].length > 20
                errors.unshift {name:attr,message:@Messages.rangelength(2,20)}
            else
                error_message = @Messages.remote
                if attr in model.needajax
                    $.ajax
                        url: "/api/users/username/verify"
                        data: {'username':model.attributes[attr]}
                        success: (data, status, xhr) ->
                            if not data.result
                                errors.unshift {name:attr,message: data.message or error_message}
                        error: (xhr, status, message) ->
                            errors.unshift {name:attr, message:error_message}

    # 邮箱验证
    email: (model, attr, options, errors) ->
        if model.attributes[attr] and not @RegExps.email.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.email}
    
    # 回单夹
    device: (model, attr, options, errors) ->
        if model.attributes[attr] and not @RegExps.device.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.device}
    
    # 修改密码验证
    old_password: (model, attr, options, errors) ->
        model.requires ?= []
        if attr in model.requires and not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}

    password: (model, attr, options, errors) ->
        model.requires ?= []
        if attr not in model.requires then return
        if attr in model.requires and not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if model.attributes[attr].length < 6 or model.attributes[attr].length > 18
            errors.unshift {name:attr,message:@Messages.rangelength(6,18)}
    
    password_again: (model, attr, options, errors) ->
        model.requires ?= []
        if attr in model.requires and not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if model.attributes[attr].length < 6 or model.attributes[attr].length > 18
            errors.unshift {name:attr,message:@Messages.rangelength(6,18)}
        else if model.attributes[attr] != $('#password').val()
            errors.unshift {name:attr,message:@Messages.equalTo}

    # 司机名称
    driver_name: (model, attr, options, errors) ->
        model.requires ?= []
        if attr in model.requires and not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if model.attributes[attr] and not @RegExps.username.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.username}

    # 车高
    height: (model, attr, options, errors) ->
        if model.attributes[attr] and not @RegExps.float_6.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.float_6}
    
    # 车宽
    width: (model, attr, options, errors) ->
        if model.attributes[attr] and not @RegExps.float_6.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.float_6}
    
    # 容积
    volume: (model, attr, options, errors) ->
        if model.attributes[attr] and not @RegExps.float_6.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.float_6}
    
    # 长
    length: (model, attr, options, errors) ->
        if model.attributes[attr] and not @RegExps.float_6.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.float_6}

    # 吨位
    tonnage: (model, attr, options, errors) ->
        if model.attributes[attr] and not @RegExps.float_6.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.float_6}
    
    # 证件核查 身份证号
    id_card: (model, attr, options, errors) ->
        model.requires ?= []
        if attr in model.requires and not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if model.attributes[attr]
            value = model.attributes[attr]
            if value.length is 15
                if not @RegExps.id_card_15.test(value)
                    errors.unshift {name:attr,message:@Messages.id_card}
            else if not @RegExps.id_card_18.test(value)
                errors.unshift {name:attr,message:@Messages.id_card}
    
    # 证件核查 司机姓名
    name: (model, attr, options, errors) ->
        model.requires ?= []
        if attr in model.requires and not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}

    # 充值金额
    amount: (model, attr, options, errors) ->
        if not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if not @RegExps.money.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.money}

    # 委托注册 公司
    company: (model, attr, options, errors) ->
        model.requires ?= []
        if attr in model.requires
            if not model.attributes[attr]
                errors.unshift {name:attr,message:@Messages.required}
            else if model.attributes[attr].length < 2 or model.attributes[attr].length > 50
                errors.unshift {name:attr,message:@Messages.rangelength(2, 50)}
    
    #货物 发站城市
    origin: (model, attr, options, errors) ->

        model.requires ?= []
        if attr in model.requires and not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
    
    #货物名称
    short_title: (model, attr, options, errors) ->
        model.requires ?= []
        if attr in model.requires and not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if model.attributes[attr]
            value = model.attributes[attr]
            if value.length > 50
                errors.unshift {name:attr, message:@Messages.maxlength(50)}
            else if @RegExps.illegal_char.test(value)
                errors.unshift {name:attr, message:@Messages.illegal_char}
                
    #value length
    value_length: (model, attr, options, errors) ->
        if _.isObject model.valueLength
            return
        value = model.attributes[attr]
        if _.has(model.valueLength, attr)
            if model.valueLength.attr.max and value > model.valueLength.attr.max
                errors.unshift {name:attr, message:@Messages.maxlength( model.valueLength.attr.max)}
            else if model.valueLength.min and value <model.valueLength.min
                errors.unshift {name:attr, message:@Messages.minlength( model.valueLength.attr.min)}
                
    captcha: (model, attr, options, errors) ->
        if not model.attributes[attr]
            errors.unshift {name:attr, message:@Messages.required}
        else if attr in model.needajax
            error_message = '验证码错误'
            if not model.get('contact_number')
                errors.unshift {name:attr, message:"请先填写手机号码"}
            else
                data =
                    captcha:model.attributes[attr]
                    contact_number:model.get('contact_number')
                $.ajax
                    url: "/api/users/captcha/verify"
                    data: data
                    success: (data, status, xhr) ->
                        if not data.result
                            errors.unshift {name:attr,message: data.message or error_message}
                    error: (xhr, status, message) ->
                        errors.unshift {name:attr, message:error_message}


    city_seat: (model, attr, options, errors) ->
        if not model.attributes[attr]
            errors.unshift {name:attr, message:@Messages.required}

        
    invite_user:(model, attr, options, errors) ->
        model.requires ?= []
        if attr in model.requires and not model.attributes[attr]
            errors.unshift {name:attr,message:@Messages.required}
        else if model.attributes[attr] and not @RegExps.mobile.test(model.attributes[attr])
            errors.unshift {name:attr,message:@Messages.mobile}
        else if model.attributes[attr] == $('#contact_number').val() and model.attributes[attr].length isnt 0
            errors.unshift {name:attr,message:@Messages.invite_user}
    ### 
    'num-of-copies': (model, attr, options, errors) ->
        if not model.attributes[attr] or not @RegExps.int_2.test(model.attributes[attr])
            errors.unshift {name:attr, message:@Messages.int_2}
    ###

