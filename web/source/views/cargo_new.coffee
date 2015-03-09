define [
    'backbone',
    'store',
    'lib/city_data'
    'lib/city_web'
    'text!templates/modules/crumb.html'
    'text!templates/forms/cargo_add.html'
], (Backbone,Store,City_data,City_web,tmp_crumb,tmp_cargo_add) ->
    'use strict'

    cargoAddView = Backbone.View.extend
        initialize: ->
            @render()

        events:
            'submit form': 'submit'
        submit: _.debounce (event) ->
            self = @
            @dirty = false
            event.preventDefault()
            $target = $(event.currentTarget)

            attr = _.extend @_arguments($target.serialize())
            @log attr
        ,2000
        render:->
            $container = $('<div class="container"></div>')
            user = Store.get('current_user').username
            $container.append(@template(tmp_crumb, {urls:[{url:'#',name:'货源信息'}],current:'发布货源'}))
            $container.append(@template(tmp_cargo_add, {user:user}))
            @$el.html($container)
        
        remove: ->
            @_super('remove')


