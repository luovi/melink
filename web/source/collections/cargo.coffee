'use strict'
define [
    'backbone'
    '../models/cargo'
], (Backbone, CargoModel) ->
    cargos = Backbone.Collection.extend
        
        model: CargoModel
        
        parse: (response) ->
            self = @
            self.total = response.total
            self.has_next = response.has_next
            _.each response.items, (item) ->
                if item.user_id == self._current_user().id or item.carrier_user_id == self._current_user().id
                    item.has_right ?= true
                if item.user_id == self._current_user().id
                    item.is_owner = true
            
            response.items
            
        initialize: ->
            uid = @_current_user().id
            @url = "/api/cargos/#{ uid }"