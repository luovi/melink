define [
    'backbone',
    'store',
    'models/user'
    'text!templates/modules/header.html',
], (Backbone, Store,UserModel,tmp_header) ->
    'use strict'

    HeaderView = Backbone.View.extend
        initialize:->
            @user = new UserModel
            @current_user = @newModel(UserModel,@_current_user())
            @render()

        render: ->
                @$el.html @template(tmp_header, {current_user:@current_user})
            

        remove: ->
            @_super('remove')


