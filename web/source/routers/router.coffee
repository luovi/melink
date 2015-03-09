define [
    'backbone',
    'store',
    'views/header',
    'text!templates/modules/footer.html'
], (Backbone, Store, HeaderView,footer) ->
    'use strict'

    AppRouter = Backbone.Router.extend
        initialize: (el)->
            @el = el

        routes:
            # 入口
            '': 'home'
            'login': 'login'
            'logout': 'logout'
            'signup': 'signup'
            'password/reset/step1': 'pswReset'
            'user/login_by_api': 'loginByApi'

            #货物
            'cargos': 'cargos'
            'cargos/new': 'cargoAdd'
            'cargo/:id/edit': 'cargoEdit'
            'cargo/:id/detail': 'cargoDetail'
            'cargo/:id/push': 'cargoPush'

            # 车辆
            'cars': 'cars'
            'cars/new': 'carsNew'
            'cars/:id/edit': 'carsEdit'

            # 用户
            
            # 404
            '*path': 'notFound'

        # 匿名权限页面
        anonymous: [
            'notFound', 'login', 'signup', 'loginByApi', 'pswReset','cargoAdd'
            ]


        # 路由前执行
        before: ->
            if not (@current_route().route in @anonymous)
                _current_user = @_current_user()
                if _current_user
                    if parseInt(_current_user.age) < (((new Date)/1000).toFixed() - _current_user.max_age_days * 86400)
                        Store.remove('current_user')
                        @redirect('login')
                        return false
                else
                    @redirect('login')
                    return false

        # 路由后执行
        after: ->
            window.addRoute("/##{ Backbone.history.fragment }")

        # 不存在的地址显示
        notFound: (path) ->
            self = @
            require ['views/notfound'], (NotFoundView) ->
                self.switchView(new NotFoundView)

        # views start ->
        loginByApi: ->
            self = @
            require ['views/login_by_api'], (loginByApiView) ->
                self.switchView(new loginByApiView)
        
        login: ->
            self = @
            _current_user = @_current_user()
            if _current_user and parseInt(_current_user.age) > (((new Date)/1000).toFixed() - _current_user.max_age_days * 86400)
                self.redirect('')
                return false
            else
                Store.remove('current_user')

            require ['views/login'], (LoginView) ->
                self.switchView(new LoginView)

        logout: ->
            # 清除本地存储 
            # Store.remove('current_user')...
            if window.isie6
                Store.clear()
            date = new Date()
            date.setTime(date.getTime() - 10000)
            document.cookie = "isLogin" + "=a; expires=" + date.toGMTString()+ ";path=/"
            @redirect('login')
            return false

        signup: ->
            self = @
            current_user = new UserModel(@_current_user())
            if current_user.isGuest()
                Store.remove('current_user')
                Store.remove('mylist')
                Store.remove('pv_list')
                Store.remove('cg_list')
                Store.remove('pb_list')
                date = new Date()
                date.setTime(date.getTime() - 10000)
                document.cookie = "isLogin" + "=a; expires=" + date.toGMTString()+ ";path=/"


            if self._current_user() && (not current_user.isGuest())
                self.redirect('')
                return false
            require ['views/signup'], (SignupView) ->
                self.switchView(new SignupView)
                document.title='注册'

        cargoAdd:->
            self = @
            require ['views/cargo_new'], (cargoAddView) ->
                self.switchView(new cargoAddView)
                document.title='新增货源'
        home: ->
            @cargos()
         
        cargoDetail:->
            self = @
            require ['views/cargo_detail'], (cargoDetailView) ->
                self.switchView(new cargoDetailView)
                document.title='货源详情'

        cargos: ->
            self =@
            require ['views/cargos'], (cargosView) ->
                self.switchView(new cargosView)
                document.title='货物列表'

        # 切换页面
        switchView: (view) ->
            self = @
            @view?.remove()
            @view = view
            # 公共头部
            @header?.remove() # 因为是view所以需要remove
            @el.html view.el
            @header = new HeaderView
            @view.notify().init()
            @el.prepend(@header.el)
            @el.append(@template(footer,{}))

        hasChange: ->
            self = @
            (event) ->
                if this.cancelNavigate
                    event.stopImmediatePropagation()
                    this.cancelNavigate = false
                    return
                if self.view?.dirty
                    oldLocation = event.originalEvent.oldURL || window.oldURL
                    dialog = confirm("该页面内容未保存，是否离开?")
                    if dialog is false
                        event.stopImmediatePropagation()
                        this.cancelNavigate = true
                        window.location.href = oldLocation

            
        

