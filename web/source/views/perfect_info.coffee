define [
    'backbone',
    'store',
    'modal',
    'text!templates/modules/crumb.html'
    'text!templates/modules/sidebar.html'
    'text!templates/forms/perfect_info.html'
], (Backbone,Store,Modal,tmp_crumb,tmp_sidebar,tmp_perfect_info) ->
    'use strict'
            
    PerfectInfoView = Backbone.View.extend
        initialize: ->
            
            @render()

        render:->
            $container = $('<div class="container"></div>')
            $container.append(@template(tmp_crumb, {urls:[{url:'center',name:'用户中心'}],current:'修改资料'}))
                .append($('<div class="row content-box"><div class="span2"></div><div class="span8"></div></div>'))
            $('.row>.span2', $container).append(@template(tmp_sidebar,{}))
            $('.row>.span8', $container).append(@template(tmp_perfect_info,{}))
            @$el.html($container)
        
        remove: ->
            @_super('remove')


