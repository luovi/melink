define [
    'backbone',
    'models/cargo'
    'text!templates/modules/crumb.html'
    'text!templates/pages/cargo_detail.html'
], (Backbone,CargoModel,tmp_crumb,tmp_cargo_detail) ->
    'use strict'

    cargoDetailView = Backbone.View.extend
        initialize:(opts={}) ->
            self = @
            @cargo = new CargoModel
            @cargo.set('id': opts.id)
            $.when(@cargo.fetch(@fetchOptions())).then(->
                self.render()
            )
        render:->
           
            $container = $('<div class="container"></div>')
            $container.append(@template(tmp_crumb, {urls:[{url:'#',name:'货源信息'}],current:'货源详情'}))
            $container.append(@template(tmp_cargo_detail, {item:@cargo}))
            @$el.html($container)
        
        remove: ->
            @_super('remove')


