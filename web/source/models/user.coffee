define [
    'backbone',
    'common'
], (Backbone, Common) ->
    'use strict'

    UserModel = Backbone.Model.extend
        validation:
            "company-company_name":"company"
            "company-business_number":"telephone"
            "company-email":"email"
            "business_number":"contact_number"
            "contact_number":"reg_contact_number"
            
        initialize: (options={}) ->
            if @_current_user()
                options.uid ?= @_current_user().id
                @urlRoot = "/api/users/#{ options.uid }"
            else
                @urlRoot = "/api/users"

        parse: (response) ->
            if response.item then response.item else response

        isGuest: ->
            if @get('role')==190 then true else false

        isPartner: ->
            if @get('role') in [490, 480] then true else false

        isTopPartner: ->
            if @get('role')==490 then true else false

        isSecPartner: ->
            if @get('role')==480 then true else false

        isMaster: ->
            if @get('role')==390 then true else false
        
        isSub: ->
            if @get('role')==380 then true else false

        isExpired: ->
            deadline = Common.parseDate(@get('membership_use_deadline'))
            if deadline == null
                return false
            else
                deadline < new Date()

        requires: ['username', 'password', 'password_again', 'company-company_name', 'contact_number']

        needajax: ['username', 'contact_number', 'captcha']

