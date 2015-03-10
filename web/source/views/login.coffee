define [
    'backbone',
    'store',
    'text!templates/modules/login_banner.html',
    'text!templates/forms/login_form.html',
], (Backbone, Store,tmp_login_banner,tmp_login_form) ->
    'use strict'

    LoginView = Backbone.View.extend
        initialize: ->
        	@render()
            
        render: ->
            $container = $('<div class="container"><div class="row"><div class="pull-left login_banner_box mr20"></div><div class="span4"></div></div></div>')
            $('.login_banner_box',$container).append @template(tmp_login_banner, {})
            $('.span4',$container).append @template(tmp_login_form, {})
            @$el.html $container
        remove: ->
            @_super('remove')


