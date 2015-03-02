define [
    'backbone',
    'store',
], (Backbone, Store) ->
    'use strict'
           
    IndexView = Backbone.View.extend
        initialize: ->
                
        render: ->
            self = @
            @subViews ?= []
            
        remove: ->
            _.invoke(@subViews, 'remove')
            @_super('remove')
