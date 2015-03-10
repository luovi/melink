define [
    'backbone',
    '../models/car'
], (Backbone, CarModel) ->
    'use strict'

    Cars = Backbone.Collection.extend
        model: CarModel

        parse: (response) ->
            @total = response.total
            @has_next = response.has_next
            response.items

        initialize: (models=[], options={}) ->
            options.uid ?= @_current_user().id
            @url = "/api/cars/#{ options.uid }"

