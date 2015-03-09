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
            user = Store.get('current_user').username
            @$el.html @template(tmp_header, {user:user})

        remove: ->
            @_super('remove')


