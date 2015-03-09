define [
    'backbone',
    'store',
    'text!templates/modules/header.html',
], (Backbone, Store,tmp_header) ->
    'use strict'

    HeaderView = Backbone.View.extend
        initialize: ->
        	@render()
        render: ->
        	@$el.html @template(tmp_header, {aaa:0})
        remove: ->
            @_super('remove')


