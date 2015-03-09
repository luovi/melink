define [
    'backbone',
    'store',
    'spin',
    'notify',
    'modal',
    'settings',
    'filter',
    'validator'
], (Backbone, Store, Spinner, Notify, Modal, settings, filter, validator) ->
    'use strict'
    
    # 让backbone传urlencode数据到后台
    Backbone.emulateJSON = true

    ###
    转当前hash下的query为object
    如login?a=1&b=2, @_arguments() == {a:1,b:2}
    ###
    Backbone.Router.prototype._arguments =
    Backbone.View.prototype._arguments = (query) ->
        query ?= Backbone.history.fragment.split('?')[1]
        if not query
            return
        params = {}
        raw_vars = query.split("&")
        for v in raw_vars
            [key, val] = v.split("=")
            val = decodeURIComponent(val.replace(/\+/g, " "))
            if key in _.keys(params)
                if not _.isArray(params[key])
                    params[key] = [params[key]]
                params[key].push val
            else
                params[key] = val
        params

    ### 通过name获得urlquery的参数值
    如a=1&b=2, getArguments('a') == 1
    ###
    Backbone.Router.prototype.getArguments =
    Backbone.View.prototype.getArguments = (name) ->
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]")
        regex = new RegExp("[\\?&]" + name + "=([^&#]*)")
        results = regex.exec(Backbone.history.fragment)
        if results is null then "" else decodeURIComponent(results[1].replace(/\+/g, " "))

    ### underscorejs template
    使用方式:
    this.template(tmplStr, {})
    ###
    Backbone.Router.prototype.template =
    Backbone.View.prototype.template = (tmpl, params) ->
        _.extend params,
            getArguments:@getArguments
            formatDate:filter.formatDate
            formatCity:filter.formatCity
            settings:settings
        _.template tmpl, params

    ### 定义_super
    使用方式:
    remove: ->
        // do something...
        @_super('remove')
    ###
    Backbone.Model.prototype._super =
    Backbone.View.prototype._super =
    Backbone.Router.prototype._super =
    Backbone.Collection.prototype._super = (funcName) ->
        @constructor.__super__[funcName].apply(@, _.rest(arguments))

    ### 方便debug事件
    使用方式:
    var model = new Backbone.Model();
    model.debugEvents();
    model.trigger('change', 'foo', 'bar'); // event "change" with ['foo', 'bar']
    ####
    Backbone.Collection.prototype.debugEvents =
    Backbone.Model.prototype.debugEvents =
    Backbone.View.prototype.debugEvents =
    Backbone.Router.prototype.debugEvents = ->
        this.on 'all', (eventName) ->
            @log('event "' + eventName + '" with ', Array.prototype.slice.call(arguments, 1))

    ### 定义log方便打印哪个对象调用的方法
    this.log('test')
    ###
    Backbone.Router.prototype.log =
    Backbone.Model.prototype.log =
    Backbone.Collection.prototype.log =
    Backbone.View.prototype.log = ->
        try
            `console.log.apply(console, ['[' + this.cid + ']'].concat([].splice.call(arguments, 0)))`
        catch error
            "log the error is ... #{error}"

    # 当获取一个不存在对象时显示错误 便于调试
    _view$ = Backbone.View.prototype.$
    Backbone.View.prototype.$ = (selector) ->
        element = _view$.apply this, arguments
        if not element.length is 0
            console?.error("[Backbone.View] Warning: selector '" + selector + "' do not match any element")
        element
    
    ### 在model里正则匹配所有属性值
    使用方式:
    var model = new Backbone.Model({ first_name: 'Jordan', last_name: 'Aslam' });
    console.log(model.match(/Jo/)); // true
    ###
    Backbone.Model.prototype.match = (test) ->
        _.any this.attributes, (attr) ->
            if _.isRegExp(test) then test.test(attr) else attr == test

    ### 在collection中正则匹配出model
    返回列表
    ###
    Backbone.Collection.prototype.search = (test) ->
        @filter (model) ->
            model.match(test)

    ### 在route中实现before和after
    这两个方法已实现跳转后终止后续方法执行
    ###
    _route = Backbone.Router.prototype.route
    Backbone.Router.prototype.before = ->
    Backbone.Router.prototype.after = ->
    Backbone.Router.prototype.route = (route, name, callback) ->
        if _.isFunction(name)
            callback = name
            name = ''

        if not callback
            callback = this[name]

        # wrap our initial callback with before() and after()
        wrapped = _.bind ->
            if @before.apply(this, arguments) is false
                return
            if callback.apply(this, arguments) is false
                return
            @after.apply(this, arguments)
        , this

        _route.call(this, route, name, wrapped)

    ### 获得当前router的路由信息
    返回 route, fragment, params
    ###
    Backbone.Router.prototype.current_route = ->
        self = @
        fragment = Backbone.history.fragment
        routes = _.pairs(@routes)
        route = null
        params = null

        matched = _.find routes, (handler) ->
            route = `_.isRegExp(handler[0]) ? handler[0] : self._routeToRegExp(handler[0])`
            route.test(fragment)

        if matched
            params = @_extractParameters(route, fragment)
            route = matched[1]

        route : route
        fragment : fragment
        params : params

    ### 跳转hash
    当trigger为真时执行router里的函数
    否则不执行只改hash
    trigger默认为真
    ###
    Backbone.Router.prototype.redirect =
    Backbone.View.prototype.redirect = (hash, trigger) ->
        trigger ?= true
        Backbone.history.navigate(hash, {trigger: trigger})
        @

    ### hash中加入query值如a=1&b=2
    不执行跳转, 仅方便模板调用请求参数
    ###
    Backbone.View.prototype.changeQuery = (data, traditional) ->
        hash = Backbone.history.fragment.split('?')[0]
        uri = "##{ hash }"
        query = if data && not _.isEmpty(data) then $.param(data, traditional)
        if query
            uri = "##{ hash }?#{ query }"
        @redirect(uri, false)

    ### 设置model的请求数据格式
    原请求格式为 {'a':1,'b':2}
    改为a=1&b=2
    ###
    Backbone.Model.prototype.toJSON = (options) ->
        $.param @attributes, true
    
    # 获取current_user方法, 结果为object, 非model
    Backbone.Router.prototype._current_user =
    Backbone.View.prototype._current_user =
    Backbone.Model.prototype._current_user =
    Backbone.Collection.prototype._current_user = ->
        Store.get('current_user')

    ### 帮助使用到token的请求生成options
    将token加入到ajax头
    如果参数change为true时, 将改变hash中的query, 
    方便模板中使用getArguments获取到参数
    默认change为false
    TODO: 
        1. ajax加载失败的处理
    ###
    Backbone.View.prototype.fetchOptions =
    Backbone.Model.prototype.fetchOptions =
    Backbone.Collection.prototype.fetchOptions = (options, change) ->
        options ?= {}
        if change is true
            options.data = _.extend @_arguments?() ? {}, options.data
            if not _.isEmpty(options.data)
                data = _.clone(options.data)
                @changeQuery(data, options.traditional)
        # 已登录时传token
        current_user = @_current_user?()
        if current_user
            _.extend options,
                beforeSend: (xhr) ->
                    xhr.setRequestHeader('Authorization', current_user.token)

        # 设置ajax公共参数
        error_callback = options.error
        #delete options.error
        _.extend options,
            cache: false
            timeout: 10000
            error: (xhr, status, errors) ->
                if status.statusText is 'timeout'
                    if not $('.modal-content').length
                        modal = new Modal
                            title: "服务器请求超时"
                            content: """<p class="modal-content">请检查网络连接是否通畅，并重试操作!</p>"""
                            button: [
                                value: "确定"
                                class: "btn-small btn-confirm"
                                callback: ->
                                    $('.spinner').remove()
                                autoremove: true
                            ]
                else
                    error_callback?(xhr, status, errors)
        options
    
    ### 让所有view可以方便使用spin
    而且只创建一次，无副作用
    使用方法:
    $target.append(@spinner().el)
    ###
    Backbone.View.prototype._spinner = _.once ->
        #@log 'create spinner'
        new Spinner(width:3,radius:10,color:'#0079b3').spin()

    Backbone.View.prototype.spinner = ->
        $spin = $(@_spinner().el)
        $spin.css({top:$('body').scrollTop() + $(window).height()/2 })
        el: $spin
        
    
    Backbone.Router.prototype.notify =
    Backbone.View.prototype.notify = _.once ->
        #@log 'create notify'
        new Notify(debug: false, delay:3000)

    ### 重置get方法,当值为各种不存在时
    替换成新值
    ###
    Backbone.Model.prototype.get = (attr, newValue='') ->
        value = @attributes[attr]
        if _.isNaN(value) or _.isNull(value) or (value is '')
            return newValue
        value
    
    ### 传入Model类和data 返回model实例
    用于json数据格式化成model
    ###
    Backbone.Router.prototype.newModel =
    Backbone.View.prototype.newModel = (Model, data) ->
        if not data
            return data
        model = new Model
        model.set data
        model

    ### 验证单个表单项
    返回的是deferred.promise对象
    示例:
    ajaxValid: (event) ->
        $target = $(event.target)
        @model.validateOne($target).then(success,error)
    ###
    Backbone.Model.prototype.validateOne = (target, options) ->
        deferred = $.Deferred()
        errors = []
        key = target.attr('name')
        value = target.val()
        @set(key, value)
        callback = ->
            if errors.length > 0 then deferred.reject(errors) else deferred.resolve()
        name = if @validation?[key] then @validation[key] else key
        $.when(
            validator[name]?(@, key, options, errors)
        ).then(callback, callback)
        return deferred.promise()
    
    ### Backbone内部验证表单函数
    当model.save时默认执行
    返回的是deferred.promise对象
    且isValid()返回的也是promise对象
    ###
    Backbone.Model.prototype.validate = (deferred, attr, options) ->
        self = @
        errors = []
        callback = ->
            if errors.length > 0 then deferred.reject(errors) else deferred.resolve()

        queue = []
        _.each _.keys(attr), (key) ->
            name = if self.validation?[key] then self.validation[key] else key
            queue.push validator[name]?(self, key, options, errors)

        $.when.apply(null, queue).then(callback, callback)
        return deferred.promise()

    # 隐藏表单错误
    Backbone.View.prototype.hideErrors = (target) ->
        if target
            target.removeClass('error')
            $wrap = $(target.data('wrap'), @$el)
            if $wrap and $wrap.length > 0
                $wrap.next('span.error').remove()
            else
                target.next('span.error').remove()
        else
            @$('form input.error').removeClass('error')
            @$('form span.error').remove()
    Backbone.View.prototype.hideErrorsWeb = (target) ->
        if target
            target.removeClass('error')
            $wrap = $(target.data('wrap'), @$el)
            if $wrap?.length > 0
                $wrap.next('div.box_error').remove()
            else
                target.next('div.box_error').remove()
        else
            @$('form input.error').removeClass('error')
            @$('form div.box_error').remove()

    # 显示表单错误
    Backbone.View.prototype.showErrors = (errors, target) ->
        self = @
        _.each errors, (error) ->
            input = self.$('input[name=' + error.name + ']')
            input.addClass('error')
            $wrap = if input.data('wrap') then $(input.data('wrap'), self.$el) else undefined
            $error = if not ($wrap and $wrap.length > 0) then input.next('span.error') else $wrap.next('span.error')
            if $error.length
                $error.html error.message
            else
                if $wrap and $wrap.length > 0
                    $wrap.after '<span class="error">'+error.message+'</span>'
                else
                    input.after '<span class="error">'+error.message+'</span>'
        if target
            self.$('input.error:first').focus()
    Backbone.View.prototype.showErrorsWeb = (errors, target) ->
        self = @
        tmp_error = """
            <div class="prompt-box box_error">
                <div class="arrow-bg"><div class="arrow"></div></div>
                <p>
                    <i class="prompt"></i><span class=""><%= error%></span>
                </p>
            </div>
        """

        if target 
            $('.box_error', target.parent()).remove()
        _.each errors, (error) ->
            input = self.$('input[name=' + error.name + ']')
            input.addClass('error')
            $wrap = if input.data('wrap') then $(input.data('wrap'), self.$el) else undefined
            $error = if not ($wrap and $wrap.length > 0) then input.next('span.error') else $wrap.next('span.error')
            if $error.length
                $error.html error.message
            else
                if $wrap and $wrap.length > 0
                    $wrap.after self.template tmp_error,{error:error.message}
                else
                    input.after self.template tmp_error,{error:error.message}
   
    Backbone.View.prototype.miniPage = (cols, options={}) ->
        options.change ?= true
        setBtn = () ->
            aBtn = $('.btn-min', $page_obj)
            if cols.has_next 
                setClass aBtn.eq(2), 'btn-confirm', 'btn-cancel'
            else
                setClass aBtn.eq(2), 'btn-cancel', 'btn-confirm'

            if page == 1
                setClass aBtn.eq(0), 'btn-cancel', 'btn-confirm'
                setClass aBtn.eq(1), 'btn-cancel', 'btn-confirm'
            else
                setClass aBtn.eq(0), 'btn-confirm', 'btn-cancel'
                setClass aBtn.eq(1), 'btn-confirm', 'btn-cancel'

        setClass = (el, add, remove) ->
            el.addClass(add).removeClass(remove)

        getData = () ->
            $.when( cols.fetch self.fetchOptions(data:page_no:page, options.change) ).then( ->
                setBtn()
            )

        self = @
        page = parseInt(@_arguments().page_no)

        tmp = """
            <div class="page-box J_pagebox">
                <input type="button" class="btn-confirm btn-min home_page" value="首页">
                <input type="button" class="btn-confirm btn-min prev_page" value="上一页">
                <input type="button" class="btn-confirm btn-min next_page" value="下一页">
            </div>
        """
        $page_obj = $(self.template(tmp, {}))
        if $('.J_pagebox').length 
            $('.J_pagebox').remove()

        $('.reset_list').prepend($page_obj)

        setBtn()

        $('.next_page', $page_obj).on 'click', _.debounce () ->
            if cols.has_next
                page++
                getData()
        ,400,true

        $('.prev_page', $page_obj).on 'click', _.debounce () ->
            if page > 1
                page--
                getData()
        ,400,true

        $('.home_page', $page_obj).on 'click', _.debounce () ->
            if page>1
                page = 1
                getData()
        ,400,true

    ### 列表分页
    用法: 在所在列表的view中，@createPage(cols) 即可
    change参数设置changeQuery，默认为true
    page_no也可以options的data中传入，为不调用changeQuery时也能使用分页功能
    ###
    Backbone.View.prototype.createPage = (cols, options={}) ->
        $('.pagination')?.remove()
        options.change ?= true
        self = @
        if cols.url.indexOf('cars') >0
            colsname = 'carsPagesize'
        else if cols.url.indexOf('cargos') >0
            colsname = 'cargosPagesize'
        else if cols.url.indexOf('contacts') >0
            colsname = 'contactsPagesize'     
        else 
            colsname = 'otherPagesize'

        # if cols.url
        if cols.length > 0
            require ['text!templates/modules/pagination.html'], (tmp) ->
                options.data ?= {}
                data =
                    page:parseInt(options.data.page_no or self.getArguments('page_no') or 1)
                    has_next:cols.has_next
                    total:cols.total
                    colsname:colsname
                    Store:Store
                # self.log data
                $page_obj = $(self.template(tmp, data))

                $('li:not(.disabled,.active)>a', $page_obj).on 'click', ->
                    pagesize = $('.text', $page_obj).val() || 20
                    if cols
                        cols.fetch self.fetchOptions(data:page_no:$(this).data('page'),page_size:pagesize, options.change)
                if options.target and _.isElement(options.target[0])
                    options.target.append $page_obj
                else
                    self.$el.append $page_obj
                
                $('.btn-confirm', $page_obj).on 'click', ->
                    pagesize = $('.text', $page_obj).val()
                    Store.set(colsname,pagesize)
                    if cols
                        cols.fetch self.fetchOptions(data:page_no:1,page_size:pagesize, options.change)
                        
                   
    ### 车牌号控件
    用法：在view中 @plateSuggest(options) 即可
        option: object类型 
            {
                target: * 必填项 input选择器 $('input')
                wrap: 弹出el插入的dom。 默认在input父标签后面。
            }
    ###
    Backbone.View.prototype.plateSuggest = (opts) ->
        $el = """
            <% _.each(citys,function(list){ %>
            <p>
                <% _.each(list, function(v){ %>
                    <span><%= v %></span>
                <% }); %>
            </p>
            <% }); %>
        """
        
        plateNumber =
            p =
            C : ["川"]
            E : ["鄂"]
            G : ["赣", "桂", "贵", "甘"]
            H : ["沪", "黑"]
            J : ["京", "津", "冀", "吉", "晋"]
            L : ["辽", "鲁"]
            M : ["蒙", "闽"]
            N : ["宁"]
            Q : ["琼", "青"]
            S : ["苏", "陕"]
            W : ["皖"]
            X : ["湘", "新"]
            Y : ["渝", "豫", "粤", "云"]
            Z : ["浙", "藏"]
            
        originCitys = [
            [p['J'][0],p['J'][1],p['H'][0],p['Y'][0],p['J'][2],p['Y'][1],p['Y'][3],p['L'][0]],
            [p['H'][1],p['X'][0],p['W'][0],p['L'][1],p['X'][1],p['S'][0],p['Z'][0],p['G'][0]],
            [p['E'][0],p['G'][1],p['G'][3],p['J'][4],p['M'][0],p['S'][1],p['J'][3],p['M'][1]],
            [p['G'][2],p['Y'][2],p['C'][0],p['Q'][1],p['Z'][1],p['Q'][0],p['N'][0]]
        ]
        
        
        initialize = (opts) =>
            if not _.isObject(opts) or not opts.target
                return
            @target ?= $(opts.target[0])
            wrap = if opts.wrap then opts.wrap else @target.parent()
            @suggestWrap =  wrap.after("<div class='plate_number_suggest text-warning ml160' ></div>")
            runEvents()
        
        runEvents = () =>
            $(@target).on
                focus: plateSuggest
                keyup: plateSuggest
                
        render = (citys) =>
            input = @target
            suggest = $(_.template($el, {citys:citys}))
            citys = $('.plate_number_suggest')
            citys.html(suggest)
            citys.css 'display', 'block'
            $('span', citys).on 'click', (e) ->
                input.val($(e.currentTarget).text())
                citys.css 'display', 'none'
                input.focus()
                
        plateSuggest = (e) =>
            key = @target.val().toUpperCase()
            @target.val(key)
            originCitysValid = _.contains (_.flatten originCitys), key
            plateNumberValid = _.some plateNumber, (v, k) ->
                k == key
            if key.length == 0 
                if not originCitysValid 
                    render(originCitys)
            if plateNumberValid
                render([plateNumber[key]])
                
        initialize(opts) 

    ### tooltips
    用法: 所在views中，渲染页面后执行 @tooltips(options)
    ###
    Backbone.View.prototype.tooltips = (options) ->
        self = @
        # 默认位置参数
        defaults =
            length: 5 #显示内容长度
            position: 'bottom'

        options = _.extend defaults, options
        $tipBox = $('<div>').addClass('tip_box').hide()
        
        # 基本方法
        tooltip =
            add: (target, style) ->
                tip_text = target.attr('tip')
                $tipBox.css style
                $tipBox.text(tip_text).appendTo(target).fadeIn(100)
            remove: ->
                $tipBox.hide()
                #$tipBox.remove()
            follow: (event) ->
                target = $(event.target)
                tip_text = target.attr('tip')
                $tipBox.css('left', event.clientX + 10)
                $tipBox.css('top', event.clientY + $(document).scrollTop() + 10)
                $tipBox.css('opacity', 1)
                $tipBox.text(tip_text).appendTo('body').stop(false, true).show()
        
        # 绑事件
        elements = $('.tips')
        if elements.length > 0
            _.each elements, (el) ->
                $this = $(el)
                tip_text=$this.attr('tip')
                oldText=$this.html()
                if options.length is 'auto'
                    if not $this.hasClass('tips_text_auto')
                        if not $this.hasClass('no_change')
                            $this.text(_.str.truncate(_.str.clean(tip_text), $this.innerWidth()))
                        else
                            $this.text(_.str.truncate(_.str.clean(oldText), $this.innerWidth()))
                else
                    if not $this.hasClass('tips_text_auto')
                        if not $this.hasClass('no_change')
                            $this.text(_.str.truncate(_.str.clean(tip_text), options.length))
                        else
                            $this.text(_.str.truncate(_.str.clean(oldText), options.length))


                if $this.attr('tip') and $.trim($this.attr('tip')) isnt ''
                    if options.position is 'top'
                        tip_left = $this.position().left
                        tip_top = $this.position().top - 34
                        $this.hover(->
                            tooltip.add $this,
                                top: tip_top
                                left: tip_left
                                opacity:1
                        , tooltip.remove)
                        $()
                        $this.bind('focus', tooltip.remove)
                    else if options.position is 'bottom'
                        tip_left = $this.position().left
                        tip_top = $this.position().top + $this.height() + 15
                        $this.hover(->
                            tooltip.add $this,
                                top: tip_top
                                left: tip_left
                                opacity: 1
                        , tooltip.remove)
                        $this.bind('focus', tooltip.remove)
                    else if options.position is 'follow'
                        # $(this).hover(tooltip.add, tooltip.remove);
                        $this.hover ->
                            $this.bind('mousemove', tooltip.follow)
                        , tooltip.remove
                        $this.bind 'click', ->
                            $this.unbind('mousemove')
                            tooltip.remove()

    ### 获取车辆模型的值
    获取车辆值
    ###
    Backbone.Model.prototype.get_car_model_value =
    Backbone.View.prototype.get_car_model_value =
    Backbone.Collection.prototype.get_car_model_value = (models, name) ->
            self = @
            model = models.find (model) ->
                model.get('name') == name
            if model
                return model.get('value')
            else
                return name

    Backbone.View.prototype.setNavBtn= () ->
        data = Store.get(@key) or {}
        fn = if _.isEmpty(data) then 'addClass' else 'removeClass'
        $('.J_oncheckBtn')[fn]('un')
  
