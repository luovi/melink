define ['jquery','underscore'], ($, _) ->
    class Tagging
        defaults:
            debug: true
            placeholder: 'tags'
            tagBoxClass: 'tagging-group pull-left'
            tagBoxFocusClass: 'focus'
            tagwrapClass: 'tagging-wrapper'
            tagClass: 'tagging-item'
            closeClass: 'tagging-close'
            closeChar: '&times;'
            inputZoneClass: 'tagging-input-zone'

        constructor: (elem, settings) ->

            self = @
            @settings = _.extend @defaults, settings
            @$elem = elem
            @tags = []
            #self.$elem
            #.attr "type", "hidden"

            @$input_zone = $(document.createElement("input"))
                .addClass(self.settings.inputZoneClass)
                .attr("placeholder", self.settings.placeholder)
                .attr("contenteditable", true)
                .attr("type", 'text')
                .attr('maxlength',16)
                .on('focus', ->
                    self.$box.addClass self.settings.tagBoxFocusClass
                )
                .on('blur', ->
                    self.$box.removeClass('focus')

                )

            @$box = $(document.createElement("div"))
                .addClass(self.settings.tagBoxClass)
                .append(self.$input_zone)
                .insertAfter(self.$elem)
                .on('click', ->
                    self.$input_zone.focus()
                )

            @$input_zone.on 'keydown', (e) ->
                actual_text = self.val()
                pressed_key = e.which
                # add
                if pressed_key in self.keys.add
                    if actual_text.length > 0
                        self.add actual_text

                    self.input ''
                    e.preventDefault()

                # remove
                else if pressed_key in self.keys.remove and actual_text.length is 0
                    $last = self.tags.pop()
                    if $last?
                        text = $last.attr "data-v"
                        self.remove($last)

                    self.input text if text?
                    e.preventDefault()

            @$button_add = $('<a href="javascript:;" class="btn-menu add-label" tabindex="-1"><i class="add-gray"></i>新增</a><span class="er error" style="display:none; margin-top:5px;"> 请输入标签内容</span>')
            @$button_add.insertAfter(self.$box).on 'click', ->
                
                text = self.$input_zone.val()
                if text.length > 0
                    self.$input_zone.val('')
                    self.add(text)
                    $('.er').css 'display', 'none'
                else                 
                    $('.er').css 'display', 'block'

            
            if @settings.tagwrap
                @$tagwrap = @settings.tagwrap
            else
                @$tagwrap = $(document.createElement("div")).addClass(self.settings.tagwrapClass).insertAfter(self.$elem)
        keys:
            add: [188, 13, 32]
            remove: [46, 8]

        val: () ->
            $.trim @.$input_zone.val()

        input: (text) ->
            @$input_zone.val text
            @$input_zone.trigger('change')

        add: (text) ->
            self = @
            values = self.getValues()
            if text in values or text== ''
                return
            $tag = $(document.createElement("div"))
                .addClass(self.settings.tagClass)
                .attr("data-v", text)
                .html(text)

            $(document.createElement("i"))
            .addClass(self.settings.closeClass)
            #.html(self.settings.closeChar)
            .on('click', ->
                self.remove $tag, text
            )
            .appendTo $tag

            #self.$input_zone.before $tag
            self.$tagwrap.append $tag

            self.tags.push $tag
            self.$elem.trigger('change')
            self.save()

        remove: ($tag) ->
            self = @
            self.tags = (tag for tag in self.tags when tag isnt $tag)
            $tag.remove()
            self.$elem.trigger('change')
            self.save()

        getValues: ->
            return (t.attr "data-v" for t in @tags)

        save: ->
            self = @
            values = self.getValues()
            self.$elem.val values.toString()


        reload: ->
            self = @
            val = @$elem.val()
            if val
                _.each val.split(','), (text) ->
                    self.add(text)

        release: ->
            @$tagwrap.remove()
            @$box.remove()

