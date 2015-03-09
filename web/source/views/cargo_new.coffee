define [
    'backbone',
    'text!templates/modules/crumb.html'
    'text!templates/forms/cargo_add.html'
], (Backbone,tmp_crumb,tmp_cargo_add) ->
    'use strict'

    cargoAddView = Backbone.View.extend
        initialize: ->
            @render()
        render:->
            $container = $('<div class="container"></div>')
            $container.append(@template(tmp_crumb, {urls:[{url:'#',name:'货源信息'}],current:'发布货源'}))
            $container.append(@template(tmp_cargo_add, {}))
            @$el.html($container)
        
        remove: ->
            @_super('remove')


