define [
    'backbone',
    'text!templates/errors/404.html'
], (Backbone, template) ->
    'use strict'

    NotFoundView = Backbone.View.extend
        initialize: ->
            @render()

        render: ->
            @$el.html @template(template, {current_user:''})
    
    NotFoundView