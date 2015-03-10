define [
    'backbone',
    'store',
    'modal',
    'text!templates/modules/user_info.html'
], (Backbone,Store,Modal,tmp_user_info) ->
    'use strict'
            
    centerView = Backbone.View.extend
        initialize: ->
            @render()
        render:->
            $container = $('<div></div>')
            $container.append(@template(tmp_user_info,{}))
            @$el.html($container)
        
        remove: ->
            @_super('remove')


