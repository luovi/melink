define [
    'backbone',
    'store',
    'modal',
    'views/user_info'
    'text!templates/modules/crumb.html'
    'text!templates/modules/sidebar.html'
], (Backbone,Store,Modal,userInfoView,tmp_crumb,tmp_sidebar) ->
    'use strict'
            
    centerView = Backbone.View.extend
        initialize: ->
            
            @render()

        events:
            'submit form': 'submit'
        submit: _.debounce (event) ->
            self = @
            @dirty = false
            event.preventDefault()
            $target = $(event.currentTarget)
            modal = new Modal
                title: "计划计划"
                width:500
                content: tmp_1
                button: [
                    value: "确定"
                    class: "btn btn-xslarge btn-normal"
                    callback: ->
                       
                    autoremove: true
                ]

        ,800
        render:->
            @subViews ?= []
            @userInfo = new userInfoView
            @subViews.push(@userInfo)
            $container = $('<div class="container"></div>')
            $container.append(@template(tmp_crumb, {urls:[{url:'#',name:'用户中心'}],current:'修改资料'}))
                .append($('<div class="row content-box"><div class="span2"></div><div class="span8"></div></div>'))
            $('.row>.span2', $container).append(@template(tmp_sidebar,{}))
            $('.row>.span8', $container).append(@userInfo.el)
            @$el.html($container)
        
        remove: ->
            @_super('remove')


