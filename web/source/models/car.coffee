define [
    'backbone'
], (Backbone) ->
    'use strict'

    CarModel = Backbone.Model.extend
        parse: (response) ->
            if response.item then response.item else response

        initialize: (attributes={}, options={}) ->
            options.uid ?= @_current_user().id
            @urlRoot= "/api/cars/#{ options.uid }"


