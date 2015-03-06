define [
    'backbone'
], (Backbone) ->
    'use strict'

    cargoAddView = Backbone.View.extend
        initialize: ->
            @render()
            @log 8
        render: ->
            $container = $('<div class="container">aaaa</div>')
            @$el.html($container)
        remove: ->
            @_super('remove')


