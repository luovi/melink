define [
    'jquery',
    'underscore',
    'text!templates/pages/plateNumber_control.html'
], ($, _, tmp_plateNumber_control) ->

    class Modal
        defaults:
            width: 450
            height: 'auto'
            content: ""

        constructor: (options = {}) ->
            options = $.extend {}, @defaults, options
            @options = options
            @arrEl = []
            @getEl()

        bindEvent: (oIpt, oEl)->
            self = @
            oFather = self.options.father

            oIpt.click ->
                # oIpt.select()
                oEl.show()

            $('.J_plate_close', oEl).click ->
                oEl.hide()
                oFather.entryValid.call(oFather, oIpt)

            $(document).click (event) ->
                oSrc = event.target
                if !self.isChild(oSrc, oEl[0]) and oSrc != oIpt[0] and oEl.css('display') == 'block' 
                    oEl.hide()
                    oFather.entryValid.call(oFather, oIpt)

            _.each $('a.word', oEl), (oBtn, i) ->
                $(oBtn).click ->
                    str = $(@).html()
                    val = oIpt.val()
                    if val.length >= self.options.maxlen
                        return
                    oIpt.val(val + str)



        isChild:(obj1,obj2) ->
            while obj1
                if obj1 == obj2
                    return true
                else
                    obj1 = obj1.parentNode;

            return false;

        create: (oIpt) ->
            oEl = $( _.template tmp_plateNumber_control, sClass:@options.sClass )

            oEl.css top: oIpt[0].offsetHeight
            oEl[0].style[@options.pos] = 0 + 'px'

            oIpt.parent().append(oEl)

            @bindEvent oIpt, oEl

        hide: ->

        getEl: ->
            self = @
            _.each $(@options.inputEl) , (oIpt) -> 
                self.create $(oIpt)





