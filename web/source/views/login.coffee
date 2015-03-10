define [
    'backbone',
    'store',
    'base64',
    'common',
    'text!templates/modules/crumb.html'
    'text!templates/modules/login_banner.html',
    'text!templates/forms/login_form.html',
], (Backbone, Store,Base64,Common,tmp_crumb,tmp_login_banner,tmp_login_form) ->
    'use strict'

    LoginModel = Backbone.Model.extend
        validate: (attr, options) ->
            errors = []
            if not attr.login
                errors.push {name:'login',message:'用户名不能为空'}
            if not attr.password
                errors.push {name:'password',message:'密码不能为空'}
            return `errors.length > 0 ? errors : false`

    LoginView = Backbone.View.extend
        initialize: ->
            @render()
            @$login_error = @$('.login_error')
            @$input_login = @$('input#login')
            @$input_pass = @$('input#password')
        events:
            'submit form': 'submit'

        submit:_.debounce (event) ->
            event.preventDefault()
            @hideErrors()
            @model = new LoginModel(login: @$input_login.val(), password:@$input_pass.val())
            if not @model.isValid()
                @showErrors(@model.validationError)
            else
                $spinWrap = @$('#spinner-wrap')
                # 为spin加个容器方便失败时清除
                if $spinWrap.length is 0
                    $spinWrap = $('<div id="spinner-wrap"></div>')
                    @$el.append $spinWrap
                $spinWrap.html @spinner().el
                @getToken(@model.get('login'), @model.get('password'))

        ,800
        render: ->
            $container = $('<div class="container"></div>')
            $container.append(@template(tmp_crumb, {urls:[{url:'',name:'首页'}],current:'欢迎登录'}))
            $content = $('<div class="row"><div class="pull-left login_banner_box mr48"></div><div class="login_form_box pull-right"></div></div>');
            $('.login_banner_box',$content).append @template(tmp_login_banner, {})
            $('.login_form_box',$content).append @template(tmp_login_form, {})
            $container.append $content
            
            @$el.html $container
        # 显示表单错误
        showErrors: (errors) ->
            self = @
            errors = errors.reverse()
            _.each errors, (error) ->
                input = self.$('#' + error.name)
                input.addClass('error').focus()
                self.$login_error.html '<span class="error">'+error.message+'</span>'

        # 隐藏表单错误
        hideErrors: ->
            @$('input').removeClass('error')
            @$login_error.empty()
        getToken: (login, pass) ->
            self = @
            data = 'Basic ' + Base64.encode(login+':'+pass)
            $.ajax
                type:'GET'
                url:'/api/token' + '?time=' + (new Date()).getTime()
                beforeSend: (xhr) ->
                    xhr.setRequestHeader('Authorization', data)
            .done (data, success, xhr) ->
                #user = $.parseJSON(Base64.decode(data.token.split('|')[0]))
                user = Common.decodeToken(data)

                if window.isie6
                    Store.clear()
                Store.set('current_user', user)
                date = new Date()
                expiresDays = if $('#remember').is(':checked') then 14 else 2
                date.setTime(date.getTime() + expiresDays*24*3600*1000)
                document.cookie = "isLogin=1; path=/; expires=" + date.toGMTString()
                self.redirect('')

            .fail (xhr, error, message) ->
                if xhr.status == 401
                    self.showErrors [name:'login',message:'用户名或密码错误']
                else
                    self.showErrors [name:'login',message:'登录失败']
                # 清除spin
                $spinWrap = self.$('#spinner-wrap')
                $spinWrap.empty()
        remove: ->
            @_super('remove')


